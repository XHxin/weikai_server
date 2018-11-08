package cc.messcat.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;

/**
 * 提现记录
 */
@Entity
@Table(name="withdraw_record")
public class WithdrawRecord implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name="memberId")
	private Member member; //专家id
	
	private BigDecimal money;
	
	private String content;  //提现内容

	@OneToOne
	@JoinColumn(name="bank_mem_id")
	private BankMember bankMember;//银行id

	@Column(name="check_status")
	private String checkStatus;//审核状态（0：未审核   1：审核通过   2：审核不通过）

	@Column(name="remit_status")
	private String remitStatus; //汇款状态（0：未汇款   1：已汇款    2：汇款中）
	
	private String remark;    //审核备注

	@Column(name="add_time")
	private Date addTime;

	@Column(name="edit_time")
	private Date editTime;
	
	private String status;
	
	
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


	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
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

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public BankMember getBankMember() {
		return bankMember;
	}

	public void setBankMember(BankMember bankMember) {
		this.bankMember = bankMember;
	}

	public String getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}

	public String getRemitStatus() {
		return remitStatus;
	}

	public void setRemitStatus(String remitStatus) {
		this.remitStatus = remitStatus;
	}

 

}