package org.opensrp.api.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.opensrp.api.constants.Gender;

/**
 * The person who actually entered or modified data into system. The {@link User} before getting access to any 
 * service in system have to be authenticated by system. 
 * A user may also be linked with a {@link Provider} by {@link BaseEntity}
 */
public class User extends BaseDataObject{
	private String username;
	private String password;
	private String salt;
	private String status;
	private List<String> roles;
	private List<String> permissions;
	private String baseEntityId;
	private BaseEntity baseEntity;
	
	public User() {	}

	public User(String baseEntityId, String username, String password, String salt, 
			String firstName, String middleName, String lastName, 
			Date birthdate, Boolean birthdateApprox, String gender) {
		this.baseEntity = new BaseEntity(baseEntityId, firstName, middleName, lastName, birthdate, null, birthdateApprox, null, gender, null, null);
		this.baseEntityId = baseEntityId;
		this.username = username;
		this.password = password;
		this.salt = salt;
	}
	
	public User(String baseEntityId, String username, String password, String salt, 
			String firstName, String middleName, String lastName,
			Date birthdate, Date deathdate, Boolean birthdateApprox,
			Boolean deathdateApprox, Gender gender, List<Address> addresses,
			Map<String, Object> attributes) {
		this.baseEntity = new BaseEntity(baseEntityId, firstName, middleName, lastName, birthdate, deathdate, birthdateApprox,
				deathdateApprox, gender, addresses, attributes);
		this.baseEntityId = baseEntityId;
		this.username = username;
		this.password = password;
		this.salt = salt;
	}

	public User(String baseEntityId, String username, String password, String salt, List<String> roles, 
			List<String> permissions, String firstName, String middleName, String lastName,
			Date birthdate, Date deathdate, Boolean birthdateApprox, Boolean deathdateApprox, String gender, 
			List<Address> addresses, Map<String, Object> attributes) {
		this.baseEntity = new BaseEntity(baseEntityId, firstName, middleName, lastName, birthdate, deathdate, birthdateApprox,
				deathdateApprox, gender, addresses, attributes);
		this.baseEntityId = baseEntityId;
		this.username = username;
		this.password = password;
		this.salt = salt;
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

	public void addRole(String role) {
		if(roles == null){
			roles = new ArrayList<>();
		}
		roles.add(role);
	}
	
	public boolean removeRole(String role) {
		return roles.remove(role);
	}
	
	public boolean hasRole(String role) {
		if(roles != null)
		for (String r : roles) {
			if(role.equalsIgnoreCase(r)){
				return true;
			}
		}
		return false;
	}
	
	public boolean isDefaultAdmin() {
		if((baseEntity.getFirstName().equalsIgnoreCase("admin") || baseEntity.getFirstName().equalsIgnoreCase("administrator")) 
				&& (hasRole("admin") || hasRole("administrator"))){
			return true;
		}
		return false;
	}
	
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
	
	public void addPermission(String permission) {
		if(permissions == null){
			permissions = new ArrayList<>();
		}
		permissions.add(permission);
	}
	
	public boolean removePermission(String permission) {
		return permissions.remove(permission);
	}
	public boolean hasPermission(String permission) {
		if(permissions != null)
		for (String p : permissions) {
			if(permission.equalsIgnoreCase(p)){
				return true;
			}
		}
		return false;
	}
	
	public String getBaseEntityId() {
		return baseEntityId;
	}
	
	public void setBaseEntityId(String baseEntityId) {
		this.baseEntityId = baseEntityId;
	}

	public BaseEntity getBaseEntity() {
		return baseEntity;
	}

	public void setBaseEntity(BaseEntity baseEntity) {
		this.baseEntity = baseEntity;
		this.baseEntityId = baseEntity.getId();
	}

	public User withBaseEntity(BaseEntity baseEntity) {
		this.baseEntity = baseEntity;
		this.baseEntityId = baseEntity.getId();
		return this;
	}
	
	public User withBaseEntityId(String baseEntityId) {
		this.baseEntityId = baseEntityId;
		return this;
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
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
