package cc.messcat.entity;

import java.util.Date;

/**
 * EnterpriseAd entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class EnterpriseAd implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = -4011891020846287022L;
	private Long id;
	private Users users;

	private String frontNum;
	private Long orderColumn;

	private String names;
	private String photo;
	private String address;
	private String intro;
	private Double acost;
	private String clientName;
	private Date initTime;
	private Date endTime;
	private Date editTime;
	private String state;

	// Constructors

	/** default constructor */
	public EnterpriseAd() {
	}

	/** minimal constructor */
	public EnterpriseAd(String names) {
		this.names = names;
	}

	/** full constructor */
	public EnterpriseAd(Users users, String names, String photo, String address, String intro, Double acost, String clientName,
		Date initTime, Date endTime, Date editTime, String state) {
		this.users = users;
		this.names = names;
		this.photo = photo;
		this.address = address;
		this.intro = intro;
		this.acost = acost;
		this.clientName = clientName;
		this.initTime = initTime;
		this.endTime = endTime;
		this.editTime = editTime;
		this.state = state;
	}

	public Users getUsers() {
		return this.users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	public String getNames() {
		return this.names;
	}

	public void setNames(String names) {
		this.names = names;
	}

	public String getPhoto() {
		return this.photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
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

	public Double getAcost() {
		return this.acost;
	}

	public void setAcost(Double acost) {
		this.acost = acost;
	}

	public String getClientName() {
		return this.clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
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

	public Date getEditTime() {
		return this.editTime;
	}

	public void setEditTime(Date editTime) {
		this.editTime = editTime;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public String getFrontNum() {
		return frontNum;
	}

	public void setFrontNum(String frontNum) {
		this.frontNum = frontNum;
	}

	public Long getOrderColumn() {
		return orderColumn;
	}

	public void setOrderColumn(Long orderColumn) {
		this.orderColumn = orderColumn;
	}

}