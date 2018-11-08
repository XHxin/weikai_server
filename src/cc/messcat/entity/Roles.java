package cc.messcat.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Roles entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class Roles implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = -8618805835135137358L;
	private Long id;
	private String name;
	private String state;
	private Date editTime;
	private Set<RolesAuthorities> rolesAuthoritieses = new HashSet<RolesAuthorities>();
	private Set<UsersRoles> usersRoleses = new HashSet<UsersRoles>();

	// Constructors

	/** default constructor */
	public Roles() {
	}

	/** minimal constructor */
	public Roles(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	/** full constructor */
	public Roles(Long id, String name, Set rolesAuthoritieses, Set usersRoleses) {
		this.id = id;
		this.name = name;
		this.rolesAuthoritieses = rolesAuthoritieses;
		this.usersRoleses = usersRoleses;
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

	public Set<RolesAuthorities> getRolesAuthoritieses() {
		return this.rolesAuthoritieses;
	}

	public void setRolesAuthoritieses(Set<RolesAuthorities> rolesAuthoritieses) {
		this.rolesAuthoritieses = rolesAuthoritieses;
	}

	public Set getUsersRoleses() {
		return this.usersRoleses;
	}

	public void setUsersRoleses(Set usersRoleses) {
		this.usersRoleses = usersRoleses;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Date getEditTime() {
		return editTime;
	}

	public void setEditTime(Date editTime) {
		this.editTime = editTime;
	}


}