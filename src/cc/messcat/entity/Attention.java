package cc.messcat.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="attention")
public class Attention implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
    
    @Column(name="memberID")
	private Long memberId;		//主动关注的会员id
    
    @Column(name="be_memberID")
	private Long beMemberId;	//被动关注的会员id
    
    @Column(name="add_time")
	private Date addTime;		//添加时间
    
    @Column(name="edit_time")
	private Date editTime;		//编辑时间
	private String status; 	   //状态（0：停用 1：启用）
	
	
	// Property accessors
	
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
	public Long getBeMemberId() {
		return beMemberId;
	}
	public void setBeMemberId(Long beMemberId) {
		this.beMemberId = beMemberId;
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