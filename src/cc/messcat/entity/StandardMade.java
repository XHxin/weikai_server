package cc.messcat.entity;

import java.util.Date;

/**
 * StandardMade entity. @author MyEclipse Persistence Tools
 */

@SuppressWarnings("serial")
public class StandardMade implements java.io.Serializable {

	// Fields

	private Long id;
	private Member member;
	private String email;
	private String message;
	private Date addTime;
	private Date editTime;
	private String status;

	// Constructors

	/** default constructor */
	public StandardMade() {
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
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
}