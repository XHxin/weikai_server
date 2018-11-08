package cc.messcat.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author nelson
 * 兑换记录实体
 */
@Entity
@Table(name="activity_exchange_record")
public class ActivityExchangeRecord {

	@Id
	@GeneratedValue
	private Long id;
	@Column(name="memberid")
	private Long memberId;
	@Column(name="change_type")
	private String changeType;
	@Column(name="change_times")
	private Date changeTimes;
	@Column(name="train_code")
	private String trainCode;
	@Column(name="openId")
	private String openId;
	@Column(name="related_id")
	private Long relatedId;
	
	public ActivityExchangeRecord() {
		super();
	}
	public Long getRelatedId() {
		return relatedId;
	}

	public void setRelatedId(Long relatedId) {
		this.relatedId = relatedId;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getMemberId() {
		return memberId;
	}
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	public String getChangeType() {
		return changeType;
	}
	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}
	public Date getChangeTimes() {
		return changeTimes;
	}
	public void setChangeTimes(Date changeTimes) {
		this.changeTimes = changeTimes;
	}
	public String getTrainCode() {
		return trainCode;
	}
	public void setTrainCode(String trainCode) {
		this.trainCode = trainCode;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
}
