package org.opensrp.service;



import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import org.apache.commons.lang3.ArrayUtils;
import org.codehaus.jackson.JsonProcessingException;
import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.Years;
import org.opensrp.util.FileCreator;
import org.opensrp.util.JsonParser;
import org.opensrp.util.NetClientGet;
import org.springframework.stereotype.Service;

/**
 * @author muhammad.ahmed@ihsinformatics.com
 *  Created on 17-September, 2015
 */
@Service
public class XlsFormDownloaderService {
	private NetClientGet netClientGet;
	private FileCreator fileCreator;
	private JsonParser jsonParser;
	
	private byte[] formJson=null; 
	public XlsFormDownloaderService() {
	netClientGet=new NetClientGet();
	fileCreator=new FileCreator();
	
	jsonParser=new JsonParser();
	}

	public static void main(String[] args) {
//		try {
//			new XlsFormDownloaderService().downloadFormFiles("D:\\opensrpVaccinatorWkspc\\forms", 
//					"maimoonak", "opensrp", JustForFun.Form, "offsite_woman_followup_form", "115135");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		System.out.println(DateTime.now().toString("dd-MM-yyyy"));
	}
	
	public boolean downloadFormFiles(String directory,String username ,String formPath, String password,String formId, String formPk) throws IOException{
		
		String xmlData=netClientGet.convertToString("", formPath, formId);
		System.out.println("xml data"+xmlData);
		String modelData=fileCreator.prettyFormat(netClientGet.getModel(xmlData),2);
		String formData=fileCreator.prettyFormat(netClientGet.getForm(xmlData));
		formJson=netClientGet.downloadJson(username,password,  formPk);
		//modelData=fileCreator.prettyFormat(modelData);
		//formData=fileCreator.prettyFormat(formData);
		System.out.println(getFormDefinition());
		fileCreator.createFile("form_definition.json", fileCreator.osDirectorySet(directory)+formId, getFormDefinition().getBytes());
		return fileCreator.createFormFiles(fileCreator.osDirectorySet(directory)+formId, formId, formData.getBytes(), modelData.getBytes(), formJson);
	}
	
	public String getFormDefinition() throws JsonProcessingException, IOException{
		if(formJson==null){
			return "Data not found on server . Please retry again !";
			
		}
		return jsonParser.getFormDefinition(formJson);
		
	}	
}
