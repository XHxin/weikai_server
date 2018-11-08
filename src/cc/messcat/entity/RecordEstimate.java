package cc.messcat.entity;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author wenyu
 *
 */
@Entity
@Table(name="record_estimate")
public class RecordEstimate {

	@Id
	@GeneratedValue
	private Integer id;    //录播评价表的唯一标示id
	
	@Column(name="member_name")
	private String memberName;	//用户昵称
	
	@Column(name="photo")
	private String photo;		//用户头像
	
	@Column(name="member_id")
	private Integer memberId;	//用户id
	
	@Column(name="content")
	private String content;	//评价的内容
	
	@Column(name="content_time")
	private Date contentTime;	//评价的时间

	@Column(name="reply")
	private String reply;	//回复的内容
	
	@Column(name="reply_time")
	private Date replyTime;	//回复内容的时间
	
	@Column(name="video_id")
	private Integer videoId;	//评价对应视频的id
	
	@Column(name="video_title")
	private String videoTitle;	//视频课程的标题
	
	@Column(name="series_video_id")
	private Integer seriesVideoId;	//对应视频的系列视频的id  -1代表这条评价的视频为单一视频，其他值代表为系列视频的id
	
	@Column(name="series_video_title")
	private String seriesVideoTitle;	//系列课程视频的标题
	
	@Column(name="grade")
	private Integer grade;	//录播评价的等级 2以上为好评，最高为5   2为中评，1为差评
	
	@Column(name="total_grade")
	private Double totalGrade;	//总的好评率
	
	@Column(name="is_series_record")
	private String isSeriesRecord;	// 1代表为该子视频的评价为系列视频的评价，0为否，默认为0
	
	@Column(name="add_time")
	private Date addTime;	//添加这条数据的时间
	
	@Column(name="edit_time")
	private Date editTime;	//编辑这条数据的时间

	@Column(name="check_status")
	private String checkStatus;	//评价审核的状态   0代表审核不通过，1代表审核通过，2代表未审核  
	
	@Column(name="status")
	private String status;	//1为启用，0为禁用，默认为1

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public Integer getMemberId() {
		return memberId;
	}

	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getContentTime() {
		return contentTime;
	}

	public void setContentTime(Date contentTime) {
		this.contentTime = contentTime;
	}

	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}

	public Date getReplyTime() {
		return replyTime;
	}

	public void setReplyTime(Date replyTime) {
		this.replyTime = replyTime;
	}

	public Integer getVideoId() {
		return videoId;
	}

	public void setVideoId(Integer videoId) {
		this.videoId = videoId;
	}

	public String getVideoTitle() {
		return videoTitle;
	}

	public void setVideoTitle(String videoTitle) {
		this.videoTitle = videoTitle;
	}

	public Integer getSeriesVideoId() {
		return seriesVideoId;
	}

	public void setSeriesVideoId(Integer seriesVideoId) {
		this.seriesVideoId = seriesVideoId;
	}

	public String getSeriesVideoTitle() {
		return seriesVideoTitle;
	}

	public void setSeriesVideoTitle(String seriesVideoTitle) {
		this.seriesVideoTitle = seriesVideoTitle;
	}

	public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	public Double getTotalGrade() {
		return totalGrade;
	}

	public void setTotalGrade(Double totalGrade) {
		this.totalGrade = totalGrade;
	}

	public String getIsSeriesRecord() {
		return isSeriesRecord;
	}

	public void setIsSeriesRecord(String isSeriesRecord) {
		this.isSeriesRecord = isSeriesRecord;
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

	public String getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}
	
	
}
