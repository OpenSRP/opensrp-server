package org.opensrp.form.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.ws.ServiceMode;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang.StringUtils;
import org.opensrp.form.domain.FormField;
import org.opensrp.form.domain.FormSubmission;
import org.opensrp.form.domain.SubFormData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

/**
 * The class is the bridge that allows parsing and mapping of formSubmission fields with 
 * those defined in xls form for external system entity mappings.
 */

@Service
public class FormAttributeParser {
	private String jsonFilePath;
	private String xmlFilePath;
	
	@Autowired
	public FormAttributeParser(@Value("#{opensrp['form.directory.name']}") String formDirPath) throws IOException
	{
		ResourceLoader loader=new DefaultResourceLoader();
		formDirPath = loader.getResource(formDirPath).getURI().getPath();
		this.jsonFilePath = formDirPath;
		this.xmlFilePath = formDirPath;
	}
	
	public FormSubmissionMap createFormSubmissionMap(FormSubmission fs) throws JsonIOException, JsonSyntaxException, ParserConfigurationException, SAXException, IOException, XPathExpressionException {
		JsonObject formDefinitionData = getFormDefinitionData(fs.formName());
		Document modelXml = getModelXmlData(fs.formName());
		JsonObject jsonForm = getJSONFormData(fs.formName());
		
		Map<String, String> formAttributes = getAttributesForBindPath(fs.defaultBindPath(), modelXml);
		List<FormFieldMap> fields = new ArrayList<FormFieldMap>();
		for (FormField fsf : fs.instance().form().fields()) {
			String bindPath = getPropertyBindFromFormDefinition(fsf.name(), fs.formName(), formDefinitionData);
			String type = bindPath==null?null:getFieldType(bindPath, jsonForm);
			Map<String, String> fieldAttributes = bindPath==null?new HashMap<String, String>():getAttributesForBindPath(bindPath, modelXml);
			
			boolean ismultiselect = bindPath==null?false:isMultiselect(bindPath, jsonForm);
			if(!StringUtils.isBlank(fsf.value())){
			if(ismultiselect){
				String[] vals = fsf.value().split(" ");
				Map<String, Map<String, String>> valCods = new HashMap<>();
				for (String v : vals) {
					valCods.put(v, getInstanceAttributesForFormFieldAndValue(bindPath, v, jsonForm));
				}
				fields.add(new FormFieldMap(fsf.name(), Arrays.asList(vals), fsf.source(), bindPath, type, fieldAttributes, valCods));
			}
			else {
				Map<String, String> valueCodes = bindPath==null?null:getInstanceAttributesForFormFieldAndValue(bindPath, fsf.value(), jsonForm);
				fields.add(new FormFieldMap(fsf.name(), fsf.value(), fsf.source(), bindPath, type, fieldAttributes, valueCodes));
			}
			}
		}
		
		List<SubformMap> subforms = new ArrayList<SubformMap>();
		if(fs.subForms() != null){
		for (SubFormData sf : fs.subForms()) {
			for (Map<String, String> flvl : sf.instances()) {
				Map<String, String> subformAttributes = getAttributesForBindPath(sf.defaultBindPath(), modelXml);
				List<FormFieldMap> sfFields = new ArrayList<>();
				for (Entry<String, String> sffl : flvl.entrySet()) {
					String source = getSourceFromSubformDefinition(sf.name(), sffl.getKey(), fs.formName(), formDefinitionData);
					String bindPath = getPathFromSubformDefinition(sf.name(), sffl.getKey(), fs.formName(), formDefinitionData);
					String type = bindPath==null?null:getFieldType(bindPath, jsonForm);
					Map<String, String> attributes = bindPath==null?new HashMap<String, String>():getAttributesForBindPath(bindPath, modelXml);
					boolean ismultiselect = bindPath==null?false:isMultiselect(bindPath, jsonForm);
					if(ismultiselect){
						String[] vals = sffl.getValue().split(" ");
						Map<String, Map<String, String>> valCods = new HashMap<>();
						for (String v : vals) {
							valCods.put(v, getInstanceAttributesForFormFieldAndValue(bindPath, v, jsonForm));
						}
						sfFields.add(new FormFieldMap(sffl.getKey(), Arrays.asList(vals), source, bindPath, type, attributes, valCods));
					}
					else {
						Map<String, String> valueCodes = bindPath==null?null:getInstanceAttributesForFormFieldAndValue(bindPath, sffl.getValue(), jsonForm);
						sfFields.add(new FormFieldMap(sffl.getKey(), sffl.getValue(), source, bindPath, type, attributes, valueCodes));
					}
				}
				subforms.add(new SubformMap(flvl.get("id"), sf.name(), sf.bindType(), sf.defaultBindPath(), subformAttributes, sfFields));
			}
		}
		}
		return new FormSubmissionMap(fs, formAttributes, fields, subforms);
	}
	
	public JsonObject getFormDefinitionData(String formName) throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		String filePath = this.jsonFilePath+"/"+formName+"/form_definition.json";
		JsonParser parser = new JsonParser();
		Object obj = parser.parse(new FileReader(filePath));
		return (JsonObject)obj;
	}
	
	public JsonObject getJSONFormData(String formName) throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		String filePath = this.jsonFilePath+"/"+formName+"/form.json";
		JsonParser parser = new JsonParser();
		Object obj = parser.parse(new FileReader(filePath));
		return (JsonObject)obj;
	}
	
	public Document getModelXmlData(String formName) throws ParserConfigurationException, SAXException, IOException {
		String filePath = this.xmlFilePath+"/"+formName+"/model.xml";
    	File file = new File(filePath);
    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		return builder.parse(file);
	}
	
	/**
	 * The method returns the field name in form submission mapped with custom attributes given as attributeMap.
	 * Ex: What is the field name in given form submission that is mapped with entity=person 
	 * and entity_id=first_name
	 * @param attributeMap
	 * @param formSubmission
	 * @return
	 * @throws IOException 
	 * @throws JsonSyntaxException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws XPathExpressionException 
	 */
	public String getFieldName(Map<String, String> attributeMap,FormSubmission formSubmission) throws JsonSyntaxException, IOException, XPathExpressionException, ParserConfigurationException, SAXException
	{
		String fieldName = "";
		Node fieldTag = getFieldTagFromModel(attributeMap,formSubmission);
		String bind =getXPath(fieldTag);
		fieldName = getFieldNameFromFormDefinition(bind,formSubmission.formName());
		return fieldName;
	}
	
	/**
	 * The method returns the field name in form submission in given subform(repeat group) mapped with custom attributes given as attributeMap.
	 * Ex: What is the field name in given form submission in subform=child_born that is 
	 * mapped with entity=person and entity_id=first_name
	 * @param attributeMap
	 * @param subform
	 * @param formSubmission
	 * @return
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws XPathExpressionException 
	 */
	public String getFieldName(Map<String, String> attributeMap, String subform, FormSubmission formSubmission) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException
	{
		String fieldName = "";
		Node fieldTag = getFieldTagFromModel(attributeMap, subform, formSubmission);
		String bind =getXPath(fieldTag);
		fieldName = getFieldNameFromFormDefinition(bind, subform, formSubmission);
		return fieldName;
	}
	
	/**
	 * Returns the field name in form submission with given bind path
	 * @param bind
	 * @param formName 
	 * @param formSubmission
	 * @return
	 * @throws IOException 
	 * @throws JsonSyntaxException 
	 */
	String getFieldNameFromFormDefinition(String bind, String formName) throws JsonSyntaxException, IOException
	{
		String fieldAttribute = "";
		JsonObject jsonObject = getFormDefinitionData(formName);
		JsonElement formElement = jsonObject.get("form");
		JsonElement subformElement = null;
		JsonObject fields = null;
		JsonObject individualField = null;
		JsonElement individualBindObj = null;
		JsonElement individualNameObj = null;
		JsonArray fieldArray = null;
		JsonArray subFormArray = null;
		if(formElement!=null)
		{
			fields = formElement.getAsJsonObject();
		
			for(Entry<String, JsonElement> element:fields.entrySet())
			{
				if(element.getKey().equalsIgnoreCase("fields"))
				{
					fieldArray = element.getValue().getAsJsonArray();
				}
				if(element.getKey().equalsIgnoreCase("sub_forms"))
				{						
					subFormArray = element.getValue().getAsJsonArray();
				}
			}
			for(JsonElement fieldElement:fieldArray)
			{
				individualField = fieldElement.getAsJsonObject();
				individualBindObj = individualField.get("bind");
				individualNameObj = individualField.get("name");
				if(individualBindObj!=null)
				{
					if(individualBindObj.getAsString().equalsIgnoreCase(bind))
					{
						fieldAttribute = individualNameObj.getAsString();
						return fieldAttribute;
					}						
				}					
			}
			for(JsonElement fieldElement:subFormArray)
			{
				individualField = fieldElement.getAsJsonObject();
				subformElement = individualField.get("fields");
			}
			for(JsonElement fieldElement:subformElement.getAsJsonArray())
			{
				individualField = fieldElement.getAsJsonObject();
				individualNameObj = individualField.get("name");
				individualBindObj = individualField.get("bind");
				if(individualBindObj!=null)
				{
					if(individualNameObj.getAsString().equalsIgnoreCase(bind))
					{
						fieldAttribute = individualNameObj.getAsString();
						return fieldAttribute;
					}						
				}					
			}
		}
			
    	
    	return null;
	}
	
	/**
	 * Returns the field name in given subform of given form submission with specified bind path
	 * @param bind
	 * @param subform
	 * @param formSubmission
	 * @return
	 * @throws IOException 
	 * @throws JsonSyntaxException 
	 */
	String getFieldNameFromFormDefinition(String bind, String subform, FormSubmission formSubmission) throws JsonSyntaxException, IOException
	{
		String formName = formSubmission.formName();
		JsonObject jsonObject = getFormDefinitionData(formName);
		JsonArray subforms = jsonObject.get("form").getAsJsonObject().get("sub_forms").getAsJsonArray();
		for (JsonElement jsonElement : subforms) {
			if(jsonElement.getAsJsonObject().get("name").getAsString().equalsIgnoreCase(subform)){
				JsonArray flarr = jsonElement.getAsJsonObject().get("fields").getAsJsonArray();
				for (JsonElement fl : flarr) {
					if(fl.getAsJsonObject().has
							("bind") && fl.getAsJsonObject().get("bind").getAsString().equalsIgnoreCase(bind)){
						return fl.getAsJsonObject().get("name").getAsString();
					}
				}
			}
		}
    	
    	return null;
	}
	
	/**
	 * Returns the Node in model.xml of given form submission that maps to given custom attributes
	 * @param attributeMapForm
	 * @param formSubmission
	 * @return
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws XPathExpressionException 
	 */
	Node getFieldTagFromModel(Map<String,String> attributeMapForm,FormSubmission formSubmission) throws IOException, XPathExpressionException, ParserConfigurationException, SAXException
	{
		Node lastNode = null;
		String formName = formSubmission.formName();
		Document document = getModelXmlData(formName);
		XPathFactory xPathFactory = XPathFactory.newInstance();
		XPath xpath = xPathFactory.newXPath();
		String expression = "//*[";
		String expressionQuery = "";
		NodeList nodeList;
		for(String key:attributeMapForm.keySet())
		{				
			if(expressionQuery.length()>0)
			{
				expressionQuery += " and ";
			}
			expressionQuery += "@"+key+"='"+attributeMapForm.get(key)+"'";
		}
		
		expression += expressionQuery;
		expression += "]";
		nodeList = (NodeList) xpath.evaluate(expression, document, XPathConstants.NODESET);
		lastNode = nodeList.item(0);			
		return lastNode;
	}
	
	/**
 	 * Returns the Node in model.xml of given subform of given form submission that maps to given custom attributes
	 * @param attributeMapForm
	 * @param subform
	 * @param formSubmission
	 * @return
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws XPathExpressionException 
	 */
	public Node getFieldTagFromModel(Map<String,String> attributeMapForm, String subform, FormSubmission formSubmission) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException
	{
		Node lastNode = null;
		String formName = formSubmission.formName();
		Document document = getModelXmlData(formName);
		XPathFactory xPathFactory = XPathFactory.newInstance();
		XPath xpath = xPathFactory.newXPath();
		String expression = getDefaultBindPathFromSubformDefinition(subform, formSubmission)+"/node()[";
		String expressionQuery = "";
		NodeList nodeList;
		for(String key:attributeMapForm.keySet())
		{				
			if(expressionQuery.length()>0)
			{
				expressionQuery += " and ";
			}
			expressionQuery += "@"+key+"='"+attributeMapForm.get(key)+"'";
		}
		
		expression += expressionQuery;
		expression += "]";
		nodeList = (NodeList) xpath.evaluate(expression, document, XPathConstants.NODESET);
		lastNode = nodeList.item(0);			
		return lastNode;
	}
	
	/**
	 * Get attributes and their values for given list of mappings. This should only be used for mapping those are 
	 * unique in xls forms. Otherwise may lead to inconsistent and incomplete data
	 * @param attributeName
	 * @param formSubmission
	 * @return
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws XPathExpressionException 
	 */
	public Map<String, String> getUniqueAttributeValue(List<String> attributeName, FormSubmission formSubmission) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException
	{
		Map<String, String> map = new HashMap<>();

		Node lastNode = null;
		String formName = formSubmission.formName();
		Document document = getModelXmlData(formName);
		XPathFactory xPathFactory = XPathFactory.newInstance();
		XPath xpath = xPathFactory.newXPath();
		String expression = "//*[";
		String expressionQuery = "";
		NodeList nodeList;
		for(String att:attributeName)
		{				
			if(expressionQuery.length()>0)
			{
				expressionQuery += " and ";
			}
			expressionQuery += "@"+att;
		}
		
		expression += expressionQuery;
		expression += "]";
		nodeList = (NodeList) xpath.evaluate(expression, document, XPathConstants.NODESET);
		lastNode = nodeList.item(0);
		
		NamedNodeMap attributes = lastNode.getAttributes();
    	for(int i=0;i<attributes.getLength();i++)
    	{
    		Node attributeNode = attributes.item(i);
    		map.put(attributeNode.getNodeName(), attributeNode.getNodeValue());
    	}
		return map;
	}
	
	private static String getXPath(Node node) {
	    if(node == null || node.getNodeType() != Node.ELEMENT_NODE) {
	        return "";
	    }

	    return getXPath(node.getParentNode()) + "/" + node.getNodeName();
	}
	
	/**
	 * Returns the list of custom attributes or mappings associated with given field in given form submission
	 * @param fieldName
	 * @param formSubmission
	 * @return
	 * @throws IOException 
	 * @throws JsonSyntaxException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws XPathExpressionException 
	 */
	public Map<String, String> getAttributesForField (String fieldName, String formName) throws JsonSyntaxException, IOException, XPathExpressionException, ParserConfigurationException, SAXException{
	
		String formBindForField = getPropertyBindFromFormDefinition(fieldName, formName); 
		Node tagAndAttributes = null;
		if(formBindForField!=null)
		{
			tagAndAttributes  = getFormPropertyNameForAttribute(formBindForField, getModelXmlData(formName));
		}

		return convertToMap(tagAndAttributes);
	    
	}
	public Map<String, String> getAttributesForField (String fieldName, String subform, String formName) throws JsonSyntaxException, IOException, XPathExpressionException, ParserConfigurationException, SAXException{
		
		String formBindForField = getPathFromSubformDefinition(subform, fieldName, formName, getFormDefinitionData(formName));
		Node tagAndAttributes = null;
		if(formBindForField!=null)
		{
			tagAndAttributes = getFormPropertyNameForAttribute(formBindForField, getModelXmlData(formName));
		}

		return convertToMap(tagAndAttributes);
	    
	}
	
	public Map<String, String> getAttributesForBindPath (String bindPath, Document xmlModel) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException{
		return convertToMap(getFormPropertyNameForAttribute(bindPath, xmlModel));
	}
	
	public Map<String, String> getAttributesForBindPath (String bindPath, String formName) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException{
		Document xmlModel = getModelXmlData(formName);
		return convertToMap(getFormPropertyNameForAttribute(bindPath, xmlModel));
	}
	
	private Map<String, String> convertToMap(Node tagAndAttributes){
	    Map<String,String> attributeMap = new HashMap<>();
	    Node attributeNode = null;
	    
	    if(tagAndAttributes!=null)
	    {
	    	NamedNodeMap attributes = tagAndAttributes.getAttributes();
	    	for(int i=0;i<attributes.getLength();i++)
	    	{
	    		attributeNode = attributes.item(i);
	    		attributeMap.put(attributeNode.getNodeName(), attributeNode.getNodeValue());
	    	}
	    }
	    
	    return attributeMap; 
	}
	
	
	public Map<String, String> convertToMap(JsonElement element) {
		if(element == null){
			return new HashMap<>();
		}
        Set<Entry<String, JsonElement>> set = element.getAsJsonObject().entrySet();
        Iterator<Entry<String, JsonElement>> iterator = set.iterator();
        HashMap<String, String> map = new HashMap<String, String>();
		while (iterator.hasNext()) {
			Entry<String, JsonElement> entry = iterator.next();
			String key = entry.getKey();
			JsonElement value = entry.getValue();
			map.put(key, value.getAsString());
		}
         return map;
	}
	/**
	 * Returns the custom attributes associated with the given subform in given form submission
	 * @param subformName
	 * @param formSubmission
	 * @return
	 * @throws IOException 
	 * @throws JsonSyntaxException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws XPathExpressionException 
	 */
	public Map<String, String> getAttributesForSubform (String subformName, FormSubmission formSubmission) throws JsonSyntaxException, IOException, XPathExpressionException, ParserConfigurationException, SAXException{
		String formBindForField = "";
		Node tagAndAttributes = null;
		// From form_definition.json
		formBindForField = getDefaultBindPathFromSubformDefinition(subformName, formSubmission); 
		// xpath in model.xml
		if(formBindForField!=null && !formBindForField.equals("null") && formBindForField.length()>0)
		{
			tagAndAttributes = getFormPropertyNameForAttribute(formBindForField, getModelXmlData(formSubmission.formName()));
		}

	    return convertToMap(tagAndAttributes); 
	}
	
	/**
	 * Returns the value of custom code associated with specified field and selected value for given subform and form submission.
	 * The function is used to get custom code value of selected option in form submission from choices sheet in xls form.
	 * @param fieldName
	 * @param fieldVal
	 * @param subform
	 * @param jsonForm 
	 * @param formSubmission
	 * @return
	 * @throws IOException 
	 * @throws JsonSyntaxException 
	 */
	public Map<String, String> getInstanceAttributesForFormFieldAndValue(String fieldName, String fieldVal, String subform, String formName, JsonObject formDefinition, JsonObject jsonForm) throws JsonSyntaxException, IOException
	{		
		String bindPath = null;
		if(StringUtils.isBlank(subform)){
			bindPath = getPropertyBindFromFormDefinition(fieldName, formName, formDefinition);
		}
		else {
			bindPath = getPathFromSubformDefinition(subform, fieldName, formName, formDefinition);
		}
		
		return getInstanceAttributesForFormFieldAndValue(bindPath, fieldVal, jsonForm);
	}
	
	public Map<String, String> getInstanceAttributesForFormFieldAndValue(String fieldName, String fieldVal, String subform, FormSubmission fs) throws JsonSyntaxException, IOException
	{		
		String bindPath = null;
		if(StringUtils.isBlank(subform)){
			bindPath = getPropertyBindFromFormDefinition(fieldName, fs.formName(), getFormDefinitionData(fs.formName()));
		}
		else {
			bindPath = getPathFromSubformDefinition(subform, fieldName, fs.formName(), getFormDefinitionData(fs.formName()));
		}
		
		return getInstanceAttributesForFormFieldAndValue(bindPath, fieldVal, getJSONFormData(fs.formName()));
	}
	
	public Map<String, String> getInstanceAttributesForFormFieldAndValue(String fieldBindPath, String fieldVal, JsonObject jsonForm) throws JsonSyntaxException, IOException
	{		
		String[] sps = fieldBindPath.split("/");
		int level = sps.length-4;
		String nodeNameToFind = sps[sps.length-1];
		JsonObject node = getChildrenOfLevel(level, jsonForm, nodeNameToFind);
		
		if(node != null && node.getAsJsonObject().has("children")){
			JsonArray nodeChAr = node.getAsJsonObject().get("children").getAsJsonArray();
			for (int j = 0; j < nodeChAr.size(); j++) {
				JsonObject option = nodeChAr.get(j).getAsJsonObject();
				if(option.get("name").getAsString().equalsIgnoreCase(fieldVal)){
					return convertToMap(option.get("instance"));
				}
			}
		}
    	
    	return null;
	}
	
	public String getFieldType(String fieldBindPath, JsonObject jsonForm)
	{		
		String[] sps = fieldBindPath.split("/");
		int level = sps.length-4;
		String nodeNameToFind = sps[sps.length-1];
		JsonObject node = getChildrenOfLevel(level, jsonForm, nodeNameToFind);
		
		if(node != null && node.getAsJsonObject().has("type")){
			return node.getAsJsonObject().get("type").getAsString();
		}
    	return null;
	}
	
	public boolean isMultiselect(String fieldBindPath, JsonObject jsonForm)
	{		
		String[] sps = fieldBindPath.split("/");
		int level = sps.length-4;
		String nodeNameToFind = sps[sps.length-1];
		JsonObject node = getChildrenOfLevel(level, jsonForm, nodeNameToFind);
		
		if(node != null && node.getAsJsonObject().has("children") 
				&& node.has("type") 
				&& (node.get("type").getAsString().startsWith("select all") || node.get("type").getAsString().startsWith("select multiple"))){
			return true;
		}
	
    	return false;
	}
	
	private JsonObject getChildrenOfLevel(int level, JsonObject node, String nodeName){
		for (JsonElement ch : getChildren(node)) {
			if(ch.getAsJsonObject().has("type")){
				for (int i = 1; i <= level; i++) {//3
					String ccurrchnmae = ch.getAsJsonObject().get("name").getAsString();
					if(i==level){
						if(ccurrchnmae.equalsIgnoreCase(nodeName)){
							return ch.getAsJsonObject();
						}
						continue;
					}
					
					if(!ch.getAsJsonObject().has("children")){
						break;
					}
					JsonObject obj = getChildrenOfLevel(level-i, ch.getAsJsonObject(), nodeName);
					if(obj != null){
						return obj;
					}
				}
			}
		}
		return null;
	}
	
	private JsonArray getChildren(JsonObject node){
		if(!node.has("children")){
			return new JsonArray();
		}
		
		return node.getAsJsonArray("children");
		
	}
	
	/**
	 * read default bind path from form_definition.json for given subform in given formSubmission. This is used to get the xpath in model.xml for subform.
	 * @throws  
	 * @throws JsonSyntaxException 
	 * @throws JsonIOException 
	 */
	String getDefaultBindPathFromSubformDefinition(String subformName, FormSubmission formSubmission) throws IOException, JsonSyntaxException
	{		
		JsonObject jsonObject = getFormDefinitionData(formSubmission.formName());
		JsonArray subforms = jsonObject.get("form").getAsJsonObject().get("sub_forms").getAsJsonArray();
		for (JsonElement jsonElement : subforms) {
			if(jsonElement.getAsJsonObject().get("name").getAsString().equalsIgnoreCase(subformName)){
				return jsonElement.getAsJsonObject().get("default_bind_path").getAsString();
			}
		}
    	
    	return null;
	}
	
	/**
	 * Gets the bind path of specified field for given subform in given form submission
	 * @param subformName
	 * @param field
	 * @param formSubmission
	 * @return
	 * @throws  
	 * @throws JsonSyntaxException 
	 * @throws JsonIOException 
	 */
	String getPathFromSubformDefinition(String subformName, String field, String formName, JsonObject formDefinition) throws IOException, JsonSyntaxException
	{		
		JsonArray subforms = formDefinition.get("form").getAsJsonObject().get("sub_forms").getAsJsonArray();
		for (JsonElement jsonElement : subforms) {
			if(jsonElement.getAsJsonObject().get("name").getAsString().equalsIgnoreCase(subformName)){
				JsonArray flarr = jsonElement.getAsJsonObject().get("fields").getAsJsonArray();
				for (JsonElement fl : flarr) {
					if(fl.getAsJsonObject().get("name").getAsString().equalsIgnoreCase(field)
							&&fl.getAsJsonObject().has("bind")){
						return fl.getAsJsonObject().get("bind").getAsString();
					}
				}
			}
		}
    	
    	return null;
	}
	/**
	 * Gets the bind path of specified field for given subform in given form submission
	 * @param subformName
	 * @param field
	 * @param formSubmission
	 * @return
	 * @throws JsonSyntaxException 
	 * @throws IOException 
	 */
	String getSourceFromSubformDefinition(String subformName, String field, String formName, JsonObject formDefinition) throws IOException, JsonSyntaxException
	{		
		JsonArray subforms = formDefinition.get("form").getAsJsonObject().get("sub_forms").getAsJsonArray();
		for (JsonElement jsonElement : subforms) {
			if(jsonElement.getAsJsonObject().get("name").getAsString().equalsIgnoreCase(subformName)){
				JsonArray flarr = jsonElement.getAsJsonObject().get("fields").getAsJsonArray();
				for (JsonElement fl : flarr) {
					if(fl.getAsJsonObject().get("name").getAsString().equalsIgnoreCase(field)
							&&fl.getAsJsonObject().has("source")){
						return fl.getAsJsonObject().get("source").getAsString();
					}
				}
			}
		}
    	
    	return null;
	}
	
	/**
	 * Gets the bind path from form_definition.json in given formSubmission for specified field 
	 * @throws JsonSyntaxException 
	 * @throws JsonIOException 
	 */
	String getPropertyBindFromFormDefinition(String fieldName, String formName) throws JsonSyntaxException, IOException
	{		
		JsonObject jsonObject = getFormDefinitionData(formName);
		return getPropertyBindFromFormDefinition(fieldName, formName, jsonObject);
	}
	
	String getPropertyBindFromFormDefinition(String fieldName, String formName, JsonObject formDefinition) throws JsonSyntaxException, IOException
	{		
		JsonElement formElement = formDefinition.get("form");
		JsonArray formFields = formElement.getAsJsonObject().get("fields").getAsJsonArray();

		for(JsonElement fieldElement : formFields)
		{
			if(fieldElement.getAsJsonObject().has("bind")){
				String bind = fieldElement.getAsJsonObject().get("bind").getAsString();
				String name = fieldElement.getAsJsonObject().get("name").getAsString();
				if(name.equalsIgnoreCase(fieldName)){
					return bind;
				}
			}
		}
    	
    	return null;
	}
	
	/**
	 * Gets the Node from model.xml that maps to specified bind path in given form submission   	
	 * @param xmlModel 
	 * @throws XPathExpressionException 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 */
	Node getFormPropertyNameForAttribute(String formBindForField, Document xmlModel) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException
	{
		if(formBindForField.endsWith("/")){
			formBindForField = formBindForField.substring(0, formBindForField.length()-1);
		}
    	Node lastNode = null;
		XPathFactory xPathFactory = XPathFactory.newInstance();
		XPath xPath = xPathFactory.newXPath();
		//put the xpath to get
		XPathExpression expr = xPath.compile(formBindForField);
		
		lastNode = (Node) expr.evaluate(xmlModel, XPathConstants.NODE);

		return lastNode;
	}
}
