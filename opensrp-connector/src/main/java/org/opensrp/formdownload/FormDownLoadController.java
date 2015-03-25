package org.opensrp.formdownload;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.ws.WebServiceException;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FormDownLoadController {

	private String FORMS_DIR;
	private String FORMS_DEFINITION_FILE_NAME;
	public FormDownLoadController(@Value("#{opensrp['form.directory']}") String FORMS_DIR,@Value("#{opensrp['form.definition.directory']}")
			String FORMS_DEFINITION_FILE_NAME) 
	{
		this.FORMS_DIR = FORMS_DIR;
		this.FORMS_DEFINITION_FILE_NAME = FORMS_DEFINITION_FILE_NAME;
	}
private static final String DS = "/"; 

@RequestMapping("/getAllAvailableVersion")
public @ResponseBody String getAllAvailableVersion() {
    return readFormDifinitionFromDirectories();
} 

/*
 * Service method transfer file as
 * a byte stream
 * */

@RequestMapping("/getForm")
public byte[] getForm(@RequestParam(value="formname", defaultValue="form_submission_samples") String formname) {
    String filePath =  FORMS_DIR + formname + "//" + formname + ".zip";   
    try {
        File file = new File(filePath);
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream inputStream = new BufferedInputStream(fis);
        byte[] fileBytes = new byte[(int) file.length()];
        inputStream.read(fileBytes);
        inputStream.close();             
        return fileBytes;
    } catch (IOException ex) {
        System.err.println(ex);
        throw new WebServiceException(ex);
    }      
}
/*
 * Utility method read Forms directories 
 * recursively find name of available forms 
 * and their respective versions and then 
 * return as a json string
 * */
private String  readFormDifinitionFromDirectories(){
    String formName_Version_DefinitionJson = "{\"formVersions\" : [";
    String formVersionDefinition = "";
    File file = new File(FORMS_DIR);
    File[] files = file.listFiles();
    for (int i = 0; i < files.length; i++) {
        formVersionDefinition = getFormName_Definition(files[i]+ DS + FORMS_DEFINITION_FILE_NAME);        
        if (files.length > 0 && i< files.length-1){
            formName_Version_DefinitionJson +=  formVersionDefinition + ",";
        }else{
            formName_Version_DefinitionJson +=  formVersionDefinition;
        }       
    }
    formName_Version_DefinitionJson += "]}";
    return formName_Version_DefinitionJson;
}
/*
 * Utility method read form definition json
 * find its name and version 
 * and return it as a json string
 * */
private String getFormName_Definition(String filePath) {
    String formName = "";
    String versionNo = "-1";
    String fieldname = "";
    JsonFactory jfactory = new JsonFactory();       
    JsonParser jParser;
    try {
        jParser = jfactory.createJsonParser(new File(filePath));
        while (jParser.nextToken() != JsonToken.END_OBJECT) {

            fieldname = jParser.getCurrentName();
            if ("form_data_definition_version".equals(fieldname)) {
                jParser.nextToken();
                versionNo = jParser.getText();
            }
            if ("form".equals(fieldname)) {
                while (jParser.nextToken() != JsonToken.FIELD_NAME) {
                    jParser.nextToken();
                    fieldname = jParser.getCurrentName();
                    if ("default_bind_path".equals(fieldname)) {
                        jParser.nextToken();
                        formName = jParser.getText().substring(16,
                                jParser.getText().length() - 1);
                    }
                }

            }
        }
        jParser.close();
    } catch (JsonParseException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
    return "{\"formName\": \"" + formName + "\", \"formDataDefinitionVersion\": \""
            + versionNo + "\"}";
}
}
