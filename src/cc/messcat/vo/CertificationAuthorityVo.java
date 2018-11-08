package cc.messcat.vo;

import javax.persistence.*;
import java.util.Date;

public class CertificationAuthorityVo{

	private String name; //认证机构名称
	private String linkman; //联系人
	private String mobile; //电话
	private String email; //email
	private String photo; //图片

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

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}
}