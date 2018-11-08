package cc.messcat.entity;

import javax.persistence.*;
import java.util.Date;

@SuppressWarnings("serial")
@Entity
@Table(name="certification_authority")
public class CertificationAuthority implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;

	private String module; //模块E
	
	@Column(name = "moduleID")
	private Long moduleId; //模块ID
	
	private String name; //认证机构名称
	private String linkman; //联系人
	private String mobile; //电话
	private String email; //email
	
	@Column(name = "add_time")
	private Date addTime;
	
	@Column(name = "edit_time")
	private Date editTime;
	
	private String status;
	
	private String photo;

	@Column(name="easemob_user_name")
	private String easemobUserName;		   //环信账号

	@Column(name="easemob_password")
	private String easemobPassword;		   //环信密码

	@Column(name="is_global")
	private String isGlobal;  //是否被全体准入报告使用（0：否（部分使用） 1：是）
	
	
	// Property accessors
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLinkman() {
		return linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public Date getEditTime() {
		return editTime;
	}

	public void setEditTime(Date editTime) {
		this.editTime = editTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getEasemobUserName() {
		return easemobUserName;
	}

	public void setEasemobUserName(String easemobUserName) {
		this.easemobUserName = easemobUserName;
	}

	public String getEasemobPassword() {
		return easemobPassword;
	}

	public void setEasemobPassword(String easemobPassword) {
		this.easemobPassword = easemobPassword;
	}

	public String getIsGlobal() {
		return isGlobal;
	}

	public void setIsGlobal(String isGlobal) {
		this.isGlobal = isGlobal;
	}

}