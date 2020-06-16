package org.opensrp.domain.postgres;

public class CustomQuery {
	
	private int id;
	
	private String uuid;
	
	private String name;
	
	private String code;
	
	private int leafLocationId;
	
	private int memberId;
	
	private String username;
	
	private String firstName;
	
	private String lastName;
	
	private String locationTagName;
	
	private Boolean enable;
	
	private Integer maxHealthId;
	
	private Boolean isAvailable;
	
	private String password;
	
	private String personUUID;
	
	private String userUUID;
	
	private String fullName;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public int getLeafLocationId() {
		return leafLocationId;
	}
	
	public void setLeafLocationId(int leafLocationId) {
		this.leafLocationId = leafLocationId;
	}
	
	public int getMemberId() {
		return memberId;
	}
	
	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getLocationTagName() {
		return locationTagName;
	}
	
	public void setLocationTagName(String locationTagName) {
		this.locationTagName = locationTagName;
	}
	
	public Boolean getEnable() {
		return enable;
	}
	
	public void setEnable(Boolean enable) {
		this.enable = enable;
	}
	
	public Integer getMaxHealthId() {
		return maxHealthId;
	}
	
	public void setMaxHealthId(Integer maxHealthId) {
		this.maxHealthId = maxHealthId;
	}
	
	public Boolean getAvailable() {
		return isAvailable;
	}
	
	public void setAvailable(Boolean available) {
		isAvailable = available;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPersonUUID() {
		return personUUID;
	}
	
	public void setPersonUUID(String personUUID) {
		this.personUUID = personUUID;
	}
	
	public String getUserUUID() {
		return userUUID;
	}
	
	public void setUserUUID(String userUUID) {
		this.userUUID = userUUID;
	}
	
	public String getFullName() {
		return fullName;
	}
	
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	@Override
	public String toString() {
		return "CustomQuery [id=" + id + ", uuid=" + uuid + ", name=" + name + ", code=" + code + ", leafLocationId="
		        + leafLocationId + ", memberId=" + memberId + ", username=" + username + ", firstName=" + firstName
		        + ", lastName=" + lastName + ", locationTagName=" + locationTagName + ", enable=" + enable + "]";
	}
	
}
