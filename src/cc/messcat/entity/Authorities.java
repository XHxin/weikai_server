package cc.messcat.entity;

import java.util.HashSet;
import java.util.Set;

public class Authorities implements java.io.Serializable {

	private Long id;
	private String name;
	private String displayName;
	private String authoritiesType;
	private Long authoritiesId;
	private Set rolesAuthoritieses = new HashSet(0);

	// Constructors

	/** default constructor */
	public Authorities() {
	}

	/** minimal constructor */
	public Authorities(Long id, String name, String displayName) {
		this.id = id;
		this.name = name;
		this.displayName = displayName;
	}

	/** full constructor */
	public Authorities(Long id, String name, String displayName, String authoritiesType, Set rolesAuthoritieses) {
		this.id = id;
		this.name = name;
		this.displayName = displayName;
		this.authoritiesType = authoritiesType;
		this.rolesAuthoritieses = rolesAuthoritieses;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return this.displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getAuthoritiesType() {
		return this.authoritiesType;
	}

	public void setAuthoritiesType(String authoritiesType) {
		this.authoritiesType = authoritiesType;
	}

	public Set getRolesAuthoritieses() {
		return this.rolesAuthoritieses;
	}

	public void setRolesAuthoritieses(Set rolesAuthoritieses) {
		this.rolesAuthoritieses = rolesAuthoritieses;
	}

	public Long getAuthoritiesId() {
		return authoritiesId;
	}

	public void setAuthoritiesId(Long authoritiesId) {
		this.authoritiesId = authoritiesId;
	}

}