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
@Table(name="live_video_subject")
public class LiveVideoSubject implements Serializable{

	/**
	 * 视频专题表
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="subject_name")
	private String subjectName;  //专题名称
	
	private String introduct;  //视频简介
	
	@Column(name="subject_cover")
	private String subjectCover;  //专题背景图
	
	@Column(name="detail_cover")
	private String detailCover;   //专题详情页的图片
	
	@Column(name="add_time")  
	private Date addTime;     //添加时间
	
	@Column(name="edit_time")
	private Date editTime;    //编辑时间
	
	@Column(name="chiefly_show")   //首页显示设置(0:首页,1:更多,2首页和更多)
	private Integer chieflyShow;
	
	private String status;   //状态(0:停用,1:启用)
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public String getIntroduct() {
		return introduct;
	}
	public void setIntroduct(String introduct) {
		this.introduct = introduct;
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
	public void setStatus(String state) {
		this.status = state;
	}
	public Integer getChieflyShow() {
		return chieflyShow;
	}
	public void setChieflyShow(Integer chieflyShow) {
		this.chieflyShow = chieflyShow;
	}
	public String getSubjectCover() {
		return subjectCover;
	}
	public void setSubjectCover(String subjectCover) {
		this.subjectCover = subjectCover;
	}
	public String getDetailCover() {
		return detailCover;
	}
	public void setDetailCover(String detailCover) {
		this.detailCover = detailCover;
	}
}
