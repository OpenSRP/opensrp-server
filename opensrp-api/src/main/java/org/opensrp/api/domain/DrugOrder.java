package org.opensrp.api.domain;

import java.util.Map;

import org.codehaus.jackson.annotate.JsonProperty;
import org.ektorp.support.TypeDiscriminator;

@TypeDiscriminator("doc.type == 'DrugOrder'")
public class DrugOrder extends BaseDataObject {

	@JsonProperty
	private Map<String, String> codes;
	@JsonProperty
	private String orderType;
	@JsonProperty
	private String drugName;
	@JsonProperty
	private String orderNumber;
	@JsonProperty
	private String action;
	@JsonProperty
	private String previousOrder;
	@JsonProperty
	private String dateActivated;
	@JsonProperty
	private String dateStopped;
	@JsonProperty
	private String autoExpireDate;
	@JsonProperty
	private String urgency;
	@JsonProperty
	private String instructions;
	@JsonProperty
	private String orderReason;
	@JsonProperty
	private String dosingType;
	@JsonProperty
	private String dose;
	@JsonProperty
	private String descriptions;
	@JsonProperty
	private String quantity;
	 
	public DrugOrder(String drugName,String orderType, Map<String, String> codes,
			String orderNumber, String action, String previousOrder
			, String dateActivated, String dateStopped,String autoExpireDate,String urgency,
			String instructions, String dosingType, String description,String quantity)
	{
		this.drugName=drugName;
		this.orderType=orderType;
		this.codes=codes;
		this.orderNumber=orderNumber;
		this.action=action;
		this.previousOrder=previousOrder;
		this.dateActivated=dateActivated;
		this.dateStopped=dateStopped;
		this.autoExpireDate=autoExpireDate;
		this.urgency=urgency;
		this.instructions=instructions;
		this.dosingType=dosingType;
		this.descriptions=description;
		this.quantity=quantity;
	}
	
	public DrugOrder()
	{
		
	}
	
	public DrugOrder(String drugName)
	{
		this.drugName=drugName;
	}
	public Map<String, String> getCodes() {
		return codes;
	}

	public void setCodes(Map<String, String> codes) {
		this.codes = codes;
	}

	
	 
	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getDrugName() {
		return drugName;
	}

	public void setDrugName(String drugName) {
		this.drugName = drugName;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getPreviousOrder() {
		return previousOrder;
	}

	public void setPreviousOrder(String previousOrder) {
		this.previousOrder = previousOrder;
	}

	public String getDateActivated() {
		return dateActivated;
	}

	public void setDateActivated(String dateActivated) {
		this.dateActivated = dateActivated;
	}

	public String getDateStopped() {
		return dateStopped;
	}

	public void setDateStopped(String dateStopped) {
		this.dateStopped = dateStopped;
	}

	public String getAutoExpireDate() {
		return autoExpireDate;
	}

	public void setAutoExpireDate(String autoExpireDate) {
		this.autoExpireDate = autoExpireDate;
	}

	public String getUrgency() {
		return urgency;
	}

	public void setUrgency(String urgency) {
		this.urgency = urgency;
	}

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	public String getOrderReason() {
		return orderReason;
	}

	public void setOrderReason(String orderReason) {
		this.orderReason = orderReason;
	}

	public String getDosingType() {
		return dosingType;
	}

	public void setDosingType(String dosingType) {
		this.dosingType = dosingType;
	}

	public String getDose() {
		return dose;
	}

	public void setDose(String dose) {
		this.dose = dose;
	}

	public String getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(String descriptions) {
		this.descriptions = descriptions;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	
}
