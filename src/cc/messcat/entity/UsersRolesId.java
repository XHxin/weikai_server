package cc.messcat.entity;

/**
 * UsersRolesId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class UsersRolesId implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 5372528798588495565L;
	private Users users;
	private Roles roles;

	// Constructors

	/** default constructor */
	public UsersRolesId() {
	}

	/** full constructor */
	public UsersRolesId(Users users, Roles roles) {
		this.users = users;
		this.roles = roles;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof UsersRolesId))
			return false;
		UsersRolesId castOther = (UsersRolesId) other;

		return ((this.getUsers() == castOther.getUsers()) || (this.getUsers() != null && castOther.getUsers() != null && this
			.getUsers().equals(castOther.getUsers())))
			&& ((this.getRoles() == castOther.getRoles()) || (this.getRoles() != null && castOther.getRoles() != null && this
				.getRoles().equals(castOther.getRoles())));
	}

	public Users getUsers() {
		return this.users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	public Roles getRoles() {
		return this.roles;
	}

	public void setRoles(Roles roles) {
		this.roles = roles;
	}

}