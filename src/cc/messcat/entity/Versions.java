package cc.messcat.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author nelson
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name="version")
public class Versions implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="Version_Code")
	private Long versionCode;  //版本号
	
	@Column(name="Version_Name")
	private String versionName;  //版本名
	
	@Column(name="TIME")
	private Date time;   //版本发布时间
	
	@Column(name="Version_Url")
	private String versionUrl;
	
	@Column(name="Descrption")
	private String descrption;  //版本描述
	
	@Column(name="Terminal")
	private String terminal;  //设备类型：ios/android
	
	@Column(name="is_Force")
	private String isForce;   //是否强制更新
	
	public String getIsForce() {
		return isForce;
	}
	public void setIsForce(String isForce) {
		this.isForce = isForce;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(Long versionCode) {
		this.versionCode = versionCode;
	}
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public String getVersionUrl() {
		return versionUrl;
	}
	public void setVersionUrl(String versionUrl) {
		this.versionUrl = versionUrl;
	}
	public String getDescrption() {
		return descrption;
	}
	public void setDescrption(String descrption) {
		this.descrption = descrption;
	}
	public String getTerminal() {
		return terminal;
	}
	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}

	
	
}
