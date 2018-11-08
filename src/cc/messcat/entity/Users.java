package cc.messcat.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Users entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class Users implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = -8406166823777698417L;
	private Long id;
	private String loginName;
	private String password;
	private String name;
	private String workunit;
	private String idcardno;
	private String sex;
	private String workphone;
	private String fax;
	private String email;
	private String area;
	private String mobile;
	private String state;
	private Long editor;
	private Date edittime;
	private String remark;
	private String address;
	private String county;

	private Set<UsersRoles> usersRoleses = new HashSet<UsersRoles>();
	private Set enterpriseAds = new HashSet(0);
	private Set enterpriseNewses = new HashSet(0);

	// Constructors

	/** default constructor */
	public Users() {
	}

	/** minimal constructor */
	public Users(Long id, String loginName) {
		this.id = id;
		this.loginName = loginName;
	}

	/** full constructor */
	public Users(Long id, String loginName, String password, String name, String workunit, String idcardno, String sex,
		String area, String workphone, String fax, String email, String mobile, String status, Long editor, Date edittime,
		String remark, Set usersRoleses, Set enterpriseAds, Set enterpriseNewses) {
		this.id = id;
		this.loginName = loginName;
		this.password = password;
		this.name = name;
		this.workunit = workunit;
		this.idcardno = idcardno;
		this.sex = sex;
		this.area = area;
		this.workphone = workphone;
		this.fax = fax;
		this.email = email;
		this.mobile = mobile;
		this.editor = editor;
		this.edittime = edittime;
		this.remark = remark;
		this.usersRoleses = usersRoleses;
		this.enterpriseAds = enterpriseAds;
		this.enterpriseNewses = enterpriseNewses;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLoginName() {
		return this.loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWorkunit() {
		return this.workunit;
	}

	public void setWorkunit(String workunit) {
		this.workunit = workunit;
	}

	public String getIdcardno() {
		return this.idcardno;
	}

	public void setIdcardno(String idcardno) {
		this.idcardno = idcardno;
	}

	public String getSex() {
		return this.sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getWorkphone() {
		return this.workphone;
	}

	public void setWorkphone(String workphone) {
		this.workphone = workphone;
	}

	public String getFax() {
		return this.fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Long getEditor() {
		return this.editor;
	}

	public void setEditor(Long editor) {
		this.editor = editor;
	}

	public Date getEdittime() {
		return this.edittime;
	}

	public void setEdittime(Date edittime) {
		this.edittime = edittime;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Set<UsersRoles> getUsersRoleses() {
		return this.usersRoleses;
	}

	public void setUsersRoleses(Set<UsersRoles> usersRoleses) {
		this.usersRoleses = usersRoleses;
	}

	public Set getEnterpriseAds() {
		return this.enterpriseAds;
	}

	public void setEnterpriseAds(Set enterpriseAds) {
		this.enterpriseAds = enterpriseAds;
	}

	public Set getEnterpriseNewses() {
		return this.enterpriseNewses;
	}

	public void setEnterpriseNewses(Set enterpriseNewses) {
		this.enterpriseNewses = enterpriseNewses;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

}