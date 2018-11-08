package cc.messcat.entity;

/**
 * RolesAuthoritiesId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

@SuppressWarnings("serial")
public class RolesAuthoritiesId implements java.io.Serializable {

	// Fields

	private Roles roles;
	private Authorities authorities;

	// Constructors

	/** default constructor */
	public RolesAuthoritiesId() {
	}

	/** full constructor */
	public RolesAuthoritiesId(Roles roles, Authorities authorities) {
		this.roles = roles;
		this.authorities = authorities;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof RolesAuthoritiesId))
			return false;
		RolesAuthoritiesId castOther = (RolesAuthoritiesId) other;

		return ((this.getRoles() == castOther.getRoles()) || (this.getRoles() != null && castOther.getRoles() != null && this
			.getRoles().equals(castOther.getRoles())))
			&& ((this.getAuthorities() == castOther.getAuthorities()) || (this.getAuthorities() != null
				&& castOther.getAuthorities() != null && this.getAuthorities().equals(castOther.getAuthorities())));
	}

	public Roles getRoles() {
		return this.roles;
	}

	public void setRoles(Roles roles) {
		this.roles = roles;
	}

	public Authorities getAuthorities() {
		return this.authorities;
	}

	public void setAuthorities(Authorities authorities) {
		this.authorities = authorities;
	}

}