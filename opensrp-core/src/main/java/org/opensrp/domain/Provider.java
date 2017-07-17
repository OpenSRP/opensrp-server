package org.opensrp.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.ektorp.support.TypeDiscriminator;

@TypeDiscriminator("doc.type == 'Provider'")
public class Provider extends BaseEntity {

	private String fullName;

	protected Provider() {
		
	}
	
	public Provider(String baseEntityId) {
		super(baseEntityId);
	}

	public Provider(String baseEntityId, String fullName) {
		super(baseEntityId);
		this.setFullName(fullName);
	}

	@Override
	public final boolean equals(Object o) {
		return EqualsBuilder.reflectionEquals(this, o, "id", "revision");
	}

	@Override
	public final int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this, "id", "revision");
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

}
