package cc.messcat.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="feedback")
public class FeedBack implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="memberID")
	private Long memberId;    //提交人ID
	
	@Column(name="expertID")
	private Long expertId;    //要反馈的专家ID
	
	@Column(name="problemID")
	private Long problemId;   //问题表ID
	
	@Column(name="level")
	private String level;	  //满意程度(1:十分满意,2:一般满意,3:不满意)
	
	@Column(name="content")
	private String content;   //反馈信息
	
	@Column(name="picture")
	private String picture;   //反馈的图片
	
	@Column(name="add_time")
	private Date addTime;     //添加时间
	
	@Column(name="edit_time")
	private Date editTime;	  //编辑时间
	
	private String status;    //状态(0：停用 1：启用)
	
	
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
	public Long getExpertId() {
		return expertId;
	}
	public void setExpertId(Long expertId) {
		this.expertId = expertId;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String contend) {
		this.content = contend;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
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
	public Long getProblemId() {
		return problemId;
	}
	public void setProblemId(Long problemId) {
		this.problemId = problemId;
	}
}
