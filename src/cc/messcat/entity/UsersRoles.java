package cc.messcat.entity;

/**
 * UsersRoles entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class UsersRoles implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = -281470080210363713L;
	private UsersRolesId id;
	private Roles roles;
	private Users users;

	// Constructors

	/** default constructor */
	public UsersRoles() {
	}

	/** full constructor */
	public UsersRoles(UsersRolesId id, Roles roles, Users users) {
		this.id = id;
		this.roles = roles;
		this.users = users;
	}

	// Property accessors

	public UsersRolesId getId() {
		return this.id;
	}

	public void setId(UsersRolesId id) {
		this.id = id;
	}

	public Roles getRoles() {
		return this.roles;
	}

	public void setRoles(Roles roles) {
		this.roles = roles;
	}

	public Users getUsers() {
		return this.users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

}