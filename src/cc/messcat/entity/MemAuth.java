package cc.messcat.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="mem_auth")
public class MemAuth implements Serializable {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="memberID")
	private Long memberId;     //会员Id
	
	@Column(name="login_type")
	private String loginType;   //登录类型(1.weixin,2.QQ)
	
	@Column(name="weixin_openID")
	private String weiXinOpenId;   //微信openId
	
	@Column(name="weixin_nickname")
	private String weiXinNiceName;   //微信昵称
	
	@Column(name="weixin_photo")
	private String weiXinPhoto;   //微信头像
	
	@Column(name="qq_openID")
	private String qqOpenId;    //qq登录openId
	
	@Column(name="qq_nickname")
	private String qqNickName;    //qq昵称
	
	@Column(name="qq_photo")
	private String qqPhoto;   //qq头像
	
	@Column(name="old_logintime")
	private Date oldLoginTime;    //上次登录时间
	
	@Column(name="old_loginIP")
	private String oldLoginIP;   //上次登录IP
	
	@Column(name="new_logintime")
	private Date newLoginTime;   //本次登录时间
	
	@Column(name="new_loginIP")
	private String newLoginIP;    //本次登录IP
	
	

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public String getLoginType() {
		return loginType;
	}

	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}

	public String getWeiXinOpenId() {
		return weiXinOpenId;
	}

	public void setWeiXinOpenId(String weiXinOpenId) {
		this.weiXinOpenId = weiXinOpenId;
	}

	public String getWeiXinNiceName() {
		return weiXinNiceName;
	}

	public void setWeiXinNiceName(String weiXinNiceName) {
		this.weiXinNiceName = weiXinNiceName;
	}

	public String getWeiXinPhoto() {
		return weiXinPhoto;
	}

	public void setWeiXinPhoto(String weiXinPhoto) {
		this.weiXinPhoto = weiXinPhoto;
	}

	public String getQqOpenId() {
		return qqOpenId;
	}

	public void setQqOpenId(String qqOpenId) {
		this.qqOpenId = qqOpenId;
	}

	public String getQqNickName() {
		return qqNickName;
	}

	public void setQqNickName(String qqNickName) {
		this.qqNickName = qqNickName;
	}

	public String getQqPhoto() {
		return qqPhoto;
	}
	public void setQqPhoto(String qqPhoto) {
		this.qqPhoto = qqPhoto;
	}
	public Date getOldLoginTime() {
		return oldLoginTime;
	}
	public void setOldLoginTime(Date oldLoginTime) {
		this.oldLoginTime = oldLoginTime;
	}
	public String getOldLoginIP() {
		return oldLoginIP;
	}
	public void setOldLoginIP(String oldLoginIP) {
		this.oldLoginIP = oldLoginIP;
	}
	public Date getNewLoginTime() {
		return newLoginTime;
	}
	public void setNewLoginTime(Date newLoginTime) {
		this.newLoginTime = newLoginTime;
	}
	public String getNewLoginIP() {
		return newLoginIP;
	}
	public void setNewLoginIP(String newLoginIP) {
		this.newLoginIP = newLoginIP;
	}

}
