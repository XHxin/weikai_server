package cc.messcat.vo;

import java.util.Date;

/**
 * @author Nelson
 *
 */
public class StandardMade {

	private int id;
	private int memberId;
	private String email;
	private String message;
	private Date add_dateTime;
	private Date edit_dateTime;
	private String status;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getMemberId() {
		return memberId;
	}
	public void setMemberId(int memberId) {
		this.memberId = memberId;
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
	public Date getAdd_dateTime() {
		return add_dateTime;
	}
	public void setAdd_dateTime(Date add_dateTime) {
		this.add_dateTime = add_dateTime;
	}
	public Date getEdit_dateTime() {
		return edit_dateTime;
	}
	public void setEdit_dateTime(Date edit_dateTime) {
		this.edit_dateTime = edit_dateTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
