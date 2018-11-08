package cc.messcat.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "fusing")
public class Fusing {

	@Id
	@GeneratedValue
	private int id;
	@Column(name="call_date")
	private Date callDate;
	@Column(name="times")
	private int times;
	@Column(name="inteface_name")
	private String intefaceName;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getCallDate() {
		return callDate;
	}
	public void setCallDate(Date callDate) {
		this.callDate = callDate;
	}
	public int getTimes() {
		return times;
	}
	public void setTimes(int times) {
		this.times = times;
	}
	public String getIntefaceName() {
		return intefaceName;
	}
	public void setIntefaceName(String intefaceName) {
		this.intefaceName = intefaceName;
	}
	
	
}
