package org.opensrp.domain;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.ektorp.support.TypeDiscriminator;

@JsonIgnoreProperties(ignoreUnknown = true)
@TypeDiscriminator("doc.type == 'Stock'")
public class Stock extends BaseDataObject {
	
	@JsonProperty
	private Long identifier;
	
	@JsonProperty
	private String vaccine_type_id;
	
	@JsonProperty
	private String transaction_type;
	
	@JsonProperty
	private String providerid;
	
	@JsonProperty
	private int value;
	
	@JsonProperty
	private Long date_created;
	
	@JsonProperty
	private String to_from;
	
	@JsonProperty
	private Long date_updated;
	
	@JsonProperty
	private long version;
	
	public Stock() {
		this.version = System.currentTimeMillis();
	}
	
	public Stock(Long identifier, String vaccine_type_id, String transaction_type, String providerid, int value,
	    Long date_created, String to_from, Long date_updated, long version) {
		this.identifier = identifier;
		this.vaccine_type_id = vaccine_type_id;
		this.transaction_type = transaction_type;
		this.providerid = providerid;
		this.value = value;
		this.date_created = date_created;
		this.to_from = to_from;
		this.date_updated = date_updated;
		this.version = version;
	}
	
	public Long getIdentifier() {
		return identifier;
	}
	
	public void setIdentifier(Long identifier) {
		this.identifier = identifier;
	}
	
	public String getVaccine_type_id() {
		return vaccine_type_id;
	}
	
	public void setVaccine_type_id(String vaccine_type_id) {
		this.vaccine_type_id = vaccine_type_id;
	}
	
	public String getTransaction_type() {
		return transaction_type;
	}
	
	public void setTransaction_type(String transaction_type) {
		this.transaction_type = transaction_type;
	}
	
	public String getProviderid() {
		return providerid;
	}
	
	public void setProviderid(String providerid) {
		this.providerid = providerid;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	public Long getDate_created() {
		return date_created;
	}
	
	public void setDate_created(Long date_created) {
		this.date_created = date_created;
	}
	
	public String getTo_from() {
		return to_from;
	}
	
	public void setTo_from(String to_from) {
		this.to_from = to_from;
	}
	
	public Long getDate_updated() {
		return date_updated;
	}
	
	public void setDate_updated(Long date_updated) {
		this.date_updated = date_updated;
	}
	
	public long getVersion() {
		return version;
	}
	
	public void setVersion(long version) {
		this.version = version;
	}
	
}
