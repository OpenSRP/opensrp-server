package org.opensrp.domain;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.ektorp.support.TypeDiscriminator;

@TypeDiscriminator("doc.type == 'User'")
public class User extends BaseEntity {
	@JsonProperty
	private String username;
	@JsonProperty
	private String password;
	@JsonProperty
	private String salt;
	@JsonProperty
	private String status;
	@JsonProperty
	private List<String> roles;
	@JsonProperty
	private List<String> permissions;
	
	protected User(){
		
	}
	
	public User(String baseEntityId) {
		super(baseEntityId);
	}

	public User(String baseEntityId, String username, String password, String salt) {
		super(baseEntityId);
		this.username = username;
		this.password = password;
		this.salt = salt;
	}
	
	public User(String baseEntityId, String username, String password, String salt, String status,
			List<String> roles, List<String> permissions) {
		super(baseEntityId);
		this.username = username;
		this.password = password;
		this.salt = salt;
		this.status = status;
		this.roles = roles;
		this.permissions = permissions;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<String> getRoles() {
		return roles;
	}

	/**
	 * WARNING: Overrides all existing roles
	 * @param roles
	 * @return
	 */
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	@JsonIgnore
	public void addRole(String role) {
		if(roles == null){
			roles = new ArrayList<>();
		}
		roles.add(role);
	}
	@JsonIgnore
	public boolean removeRole(String role) {
		return roles.remove(role);
	}
	
	@JsonIgnore
	public boolean hasRole(String role) {
		if(roles != null)
		for (String r : roles) {
			if(role.equalsIgnoreCase(r)){
				return true;
			}
		}
		return false;
	}

	@JsonIgnore
	public boolean isDefaultAdmin() {
		if((username.equalsIgnoreCase("admin") || username.equalsIgnoreCase("administrator")) 
				&& (hasRole("admin") || hasRole("administrator"))){
			return true;
		}
		return false;
	}

	@JsonIgnore
	public boolean hasAdminRights() {
		if(isDefaultAdmin() || hasRole("admin") || hasRole("administrator")){
			return true;
		}
		return false;
	}
	
	public List<String> getPermissions() {
		return permissions;
	}

	/**
	 * WARNING: Overrides all existing permissions
	 * @param permissions
	 * @return
	 */
	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}
	
	@JsonIgnore
	public void addPermission(String permission) {
		if(permissions == null){
			permissions = new ArrayList<>();
		}
		permissions.add(permission);
	}
	
	public boolean removePermission(String permission) {
		return permissions.remove(permission);
	}
	@JsonIgnore
	public boolean hasPermission(String permission) {
		if(permissions != null)
		for (String p : permissions) {
			if(permission.equalsIgnoreCase(p)){
				return true;
			}
		}
		return false;
	}

	public User withUsername(String username) {
		this.username = username;
		return this;
	}

	public User withPassword(String password) {
		this.password = password;
		return this;
	}

	public User withSalt(String salt) {
		this.salt = salt;
		return this;
	}

	public User withStatus(String status) {
		this.status = status;
		return this;
	}

	/**
	 * WARNING: Overrides all existing roles
	 * @param roles
	 * @return 
	 * @return
	 */
	public User withRoles(List<String> roles) {
		this.roles = roles;
		return this;
	}

	public User withRole(String role) {
		if(roles == null){
			roles = new ArrayList<>();
		}
		roles.add(role);
		return this;
	}
	
	/**
	 * WARNING: Overrides all existing permissions
	 * @param permissions
	 * @return 
	 * @return
	 */
	public User withPermissions(List<String> permissions) {
		this.permissions = permissions;
		return this;
	}
	
	public User withPermission(String permission) {
		if(permissions == null){
			permissions = new ArrayList<>();
		}
		permissions.add(permission);
		return this;
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

}
