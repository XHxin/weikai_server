package cc.messcat.entity;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Member entity. @author MyEclipse Persistence Tools
 */

//@Entity
//@Table(name = "standard")
public class Standard implements java.io.Serializable {

	// Fields
//	@Id
//	@GeneratedValue
	private Long id;
	
//	@Column(name = "regionID")
	private Region regionId; //地区ID
	
//	@Column(name = "region_name")
	private String regionName; //地区名（选填）
	
//	@Column(name = "productID")
	private Product productId; //产品ID
	
//	@Column(name = "product_name")
	private String productName; //产品名（选填）
	
	//	@Column(name = "standardIDs")
	private String standardIds; //标准ID集
	
	private String remark;
	
//	@Column(name = "add_time")
	private Date addTime;
	
//	@Column(name = "edit_time")
	private Date editTime;
	
	private String status;
	
	private List<StandardBase> bases;
	
	private Map map;

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

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Region getRegionId() {
		return regionId;
	}

	public void setRegionId(Region regionId) {
		this.regionId = regionId;
	}

	public Product getProductId() {
		return productId;
	}

	public void setProductId(Product productId) {
		this.productId = productId;
	}

	public String getStandardIds() {
		return standardIds;
	}

	public void setStandardIds(String standardIds) {
		this.standardIds = standardIds;
	}

	public List<StandardBase> getBases() {
		return bases;
	}

	public void setBases(List<StandardBase> bases) {
		this.bases = bases;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}


}