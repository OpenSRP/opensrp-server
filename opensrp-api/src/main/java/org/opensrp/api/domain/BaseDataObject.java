package org.opensrp.api.domain;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
/**
 * The Base Object for keeping audit data for any business entity.
 */
public abstract class BaseDataObject {

	private User creator;
	
	private Date dateCreated;
	
	private User editor;
	
	private Date dateEdited;
	
	private Boolean voided = Boolean.FALSE;
	
	private Date dateVoided;
	
	private User voider;
	
	private String voidReason;

	public BaseDataObject() {}
	
	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	/**
	 * The last User who edited the data
	 * @return
	 */
	public User getEditor() {
		return editor;
	}

	/**
	 * The last User who edited the data
	 */
	public void setEditor(User editor) {
		this.editor = editor;
	}

	/**
	 * The last edited date of the data
	 * @return
	 */
	public Date getDateEdited() {
		return dateEdited;
	}

	/**
	 * The last edited date of the data
	 */
	public void setDateEdited(Date dateEdited) {
		this.dateEdited = dateEdited;
	}

	public Boolean getVoided() {
		return voided;
	}

	public void setVoided(Boolean voided) {
		this.voided = voided;
	}

	public Date getDateVoided() {
		return dateVoided;
	}

	public void setDateVoided(Date dateVoided) {
		this.dateVoided = dateVoided;
	}

	public User getVoider() {
		return voider;
	}

	public void setVoider(User voider) {
		this.voider = voider;
	}

	public String getVoidReason() {
		return voidReason;
	}

	public void setVoidReason(String voidReason) {
		this.voidReason = voidReason;
	}
	
	public BaseDataObject withCreator(User creator) {
		this.creator = creator;
		return this;
	}

	public BaseDataObject withDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
		return this;
	}

	public BaseDataObject withEditor(User editor) {
		this.editor = editor;
		return this;
	}

	public BaseDataObject withDateEdited(Date dateEdited) {
		this.dateEdited = dateEdited;
		return this;
	}

	public BaseDataObject withVoided(Boolean voided) {
		this.voided = voided;
		return this;
	}

	public BaseDataObject withDateVoided(Date dateVoided) {
		this.dateVoided = dateVoided;
		return this;
	}

	public BaseDataObject withVoider(User voider) {
		this.voider = voider;
		return this;
	}

	public BaseDataObject withVoidReason(String voidReason) {
		this.voidReason = voidReason;
		return this;
	}
	
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
