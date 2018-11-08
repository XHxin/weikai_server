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
//@Table(name = "legal")
public class Legal implements java.io.Serializable {

	// Fields
//	@Id
//	@GeneratedValue
	private Long id;
	
//	@Column(name = "regionID")
	private Region regionId; //地区ID
	
//	@Column(name = "region_name")
	private String regionName; //地区名（选填）
	
//	@Column(name = "productIDs")
	private String productIds; //产品ID集
	
//	@Column(name = "product_names")
	private String productNames; //产品名集（选填）
	
	private String item; //条目名称
	private String content; //内容
	private String remark; //备注
	
	private Long citeId;
	
	private String citeName;
	
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

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Region getRegionId() {
		return regionId;
	}

	public void setRegionId(Region regionId) {
		this.regionId = regionId;
	}

	public String getProductIds() {
		return productIds;
	}

	public void setProductIds(String productIds) {
		this.productIds = productIds;
	}

	public String getProductNames() {
		return productNames;
	}

	public void setProductNames(String productNames) {
		this.productNames = productNames;
	}

	public Long getCiteId() {
		return citeId;
	}

	public void setCiteId(Long citeId) {
		this.citeId = citeId;
	}

	public String getCiteName() {
		return citeName;
	}

	public void setCiteName(String citeName) {
		this.citeName = citeName;
	}



}