package cc.messcat.entity;

/**
 * RolesAuthorities entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class RolesAuthorities implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = -766486323407100323L;
	private RolesAuthoritiesId id;
	private Roles roles;
	private Authorities authorities;

	// Constructors

	/** default constructor */
	public RolesAuthorities() {
	}

	public RolesAuthorities(RolesAuthoritiesId id) {
		this.id = id;
	}

	/** full constructor */
	public RolesAuthorities(RolesAuthoritiesId id, Roles roles, Authorities authorities) {
		this.id = id;
		this.roles = roles;
		this.authorities = authorities;
	}

	// Property accessors

	public RolesAuthoritiesId getId() {
		return this.id;
	}

	public void setId(RolesAuthoritiesId id) {
		this.id = id;
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

	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if (!(obj instanceof RolesAuthorities))
			return false;
		
		final RolesAuthorities rolesAuthorities = (RolesAuthorities)obj;
		if(this.getId() != null)
			if(this.getId().getAuthorities() != null)
				if(this.getId().getAuthorities().getId() != null)
					if(rolesAuthorities.getId().getAuthorities().getId() == this.getId().getAuthorities().getId())
						return true;
		
		return false;
	}

	@Override
	public int hashCode() {
		int result = 17;

		result = 37 * result + (id == null ? 0 : 
			(id.getAuthorities() == null ? 0 : 
				(id.getAuthorities().getId() == null ? 0 : this.id.getAuthorities().getId().intValue())));
		return result;
	}
}