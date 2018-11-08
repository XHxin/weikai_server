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
@Table(name="hot_reply")
public class HotReply implements Serializable {
    
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="expertID")
	private Long expertId;   		//回答问题的专家ID
	
	@Column(name="problemID")
	private Long problemId;			//问题表Id
	
	@Column(name="problem_name")
	private String problemName;     //问题
	private String type;            //提问方式(0为公开,1为私密)
	private String content;			//内容(图文)
	private String picture;			//图片
	private String voice;			//音频文件
	
	@Column(name="voice_duration")
	private String voiceDuration;	//音频时长
	
	@Column(name="is_showIndex")
	private String isShowIndex;		//是否在热门问题推荐专栏显示(0:否 1:是)
	
	@Column(name="add_time")
	private Date addTime;			//创建时间
	
	@Column(name="edit_time")
	private Date editTime;			//编辑时间
	private String status;			//状态(0：停用  1：启用)
	private int dispose;		//用于标记该记录是否已经做过流水处理，避免定时器重复处理：0-未处理，1-已处理
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getExpertId() {
		return expertId;
	}
	public void setExpertId(Long memberId) {
		this.expertId = memberId;
	}
	public Long getProblemId() {
		return problemId;
	}
	public void setProblemId(Long problemId) {
		this.problemId = problemId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public String getVoice() {
		return voice;
	}
	public void setVoice(String voice) {
		this.voice = voice;
	}
	public String getVoiceDuration() {
		return voiceDuration;
	}
	public void setVoiceDuration(String voiceDuration) {
		this.voiceDuration = voiceDuration;
	}
	public String getIsShowIndex() {
		return isShowIndex;
	}
	public void setIsShowIndex(String isShowIndex) {
		this.isShowIndex = isShowIndex;
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
	public String getProblemName() {
		return problemName;
	}
	public void setProblemName(String problemName) {
		this.problemName = problemName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getDispose() {
		return dispose;
	}
	public void setDispose(int dispose) {
		this.dispose = dispose;
	}
	
}
