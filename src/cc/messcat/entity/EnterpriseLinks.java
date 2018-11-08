package cc.messcat.entity;

import java.util.Date;

/**
 * EnterpriseLinks entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class EnterpriseLinks implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = -7969141749772080759L;
	private Long id;
	private String names;
	private String address;
	private String intro;
	private Date initTime;
	private Date endTime;
	private String state;

	private Long orderColumn;
	private String frontNum;

	// Constructors
	/** default constructor */
	public EnterpriseLinks() {
	}

	/** minimal constructor */
	public EnterpriseLinks(String names) {
		this.names = names;
	}

	/** full constructor */
	public EnterpriseLinks(String names, String address, String intro, Date initTime, Date endTime, String state) {
		this.names = names;
		this.address = address;
		this.intro = intro;
		this.initTime = initTime;
		this.endTime = endTime;
		this.state = state;
	}

	public Long getOrderColumn() {
		return orderColumn;
	}

	public void setOrderColumn(Long orderColumn) {
		this.orderColumn = orderColumn;
	}

	public String getFrontNum() {
		return frontNum;
	}

	public void setFrontNum(String frontNum) {
		this.frontNum = frontNum;
	}

	public String getNames() {
		return this.names;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setNames(String names) {
		this.names = names;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getIntro() {
		return this.intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public Date getInitTime() {
		return this.initTime;
	}

	public void setInitTime(Date initTime) {
		this.initTime = initTime;
	}

	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

}