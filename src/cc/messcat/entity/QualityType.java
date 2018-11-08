package cc.messcat.entity;

import java.util.Date;

/**
 * QualityType entity. @author MyEclipse Persistence Tools
 */

@SuppressWarnings("serial")
public class QualityType implements java.io.Serializable {

	// Fields

	private Integer id;
	private String name;		//质量分享分类名称
	private String remark;		//备注
	private Date addTime;		//新增时间
	private Date editTime;		//编辑时间
	private String status;		//状态（0：停用  1：启用）
	
	private Integer sort;		//排序


	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
}