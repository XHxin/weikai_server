package cc.messcat.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Member entity. @author MyEclipse Persistence Tools
 */

//@Entity
//@Table(name = "national_differences")
public class NationalDifferences implements java.io.Serializable {

	// Fields
//	@Id
//	@GeneratedValue
	private Long id;
	
//	@Column(name = "regionID")
	private Region regionId; //地区ID
	
//	@Column(name = "region_name")
	private String regionName; //地区名（选填）
	
	private String voltage; //电压（V）
	private String hz; //频率（Hz）
	
//	@Column(name = "official_language")
	private String officialLanguage; //官方语言
	
	private String type; //类型
	private String form; //形式（图片名称）
	private String remark; //备注
	
//	@Column(name = "add_time")
	private Date addTime;
	
//	@Column(name = "edit_time")
	private Date editTime;
	
	private String status;

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getAddTime() {
		return this.addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public Date getEditTime() {
		return this.editTime;
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

	public Region getRegionId() {
		return regionId;
	}

	public void setRegionId(Region regionId) {
		this.regionId = regionId;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getVoltage() {
		return voltage;
	}

	public void setVoltage(String voltage) {
		this.voltage = voltage;
	}


	public String getHz() {
		return hz;
	}

	public void setHz(String hz) {
		this.hz = hz;
	}

	public String getOfficialLanguage() {
		return officialLanguage;
	}

	public void setOfficialLanguage(String officialLanguage) {
		this.officialLanguage = officialLanguage;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getForm() {
		return form;
	}

	public void setForm(String form) {
		this.form = form;
	}


}