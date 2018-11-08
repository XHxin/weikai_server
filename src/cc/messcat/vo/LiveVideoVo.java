package cc.messcat.vo;

import java.util.Date;

public class LiveVideoVo {

	private Long id; 
	private String title;//课程名称
	private Long duration;//视频的时长
	private Long viewers;//观看人数
	private String cover;//封面图片
	private String videoUrl;//视频播放地址
	private Long fatherId;//如果有值，就代表该视频为连载
	private String videoType;//视频类型，0为直播，1为录播
	private String introduct;//视频简介
	private String terminal;//观看设备，0为移动端，1为PC端
	private Double price;//价格
	private String status;//状态，0为可不用，1为可用
	private Date applyDate;//直播时间
	private Long experId;//直播专家的ID
	private Date bespeakDate;//预约的时间
	private String bespeakStatus;//预约的状态 0-预约中，1-已预约，2-取消预约 3-预约失败
	private String chieflyShow;//是否首要展示：0-否 1-是（用于判断是否展示在轮播或首页模块上）
	private String experName;//直播专家的名称
	private String checkRemark;//审核备注，审核通过与否发送给专家
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Long getDuration() {
		return duration;
	}
	public void setDuration(Long duration) {
		this.duration = duration;
	}
	public Long getViewers() {
		return viewers;
	}
	public void setViewers(Long viewers) {
		this.viewers = viewers;
	}
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	public String getVideoUrl() {
		return videoUrl;
	}
	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}
	public Long getFatherId() {
		return fatherId;
	}
	public void setFatherId(Long fatherId) {
		this.fatherId = fatherId;
	}
	public String getVideoType() {
		return videoType;
	}
	public void setVideoType(String videoType) {
		this.videoType = videoType;
	}
	public String getIntroduct() {
		return introduct;
	}
	public void setIntroduct(String introduct) {
		this.introduct = introduct;
	}
	public String getTerminal() {
		return terminal;
	}
	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}
	public Long getExperId() {
		return experId;
	}
	public void setExperId(Long experId) {
		this.experId = experId;
	}
	public Date getBespeakDate() {
		return bespeakDate;
	}
	public void setBespeakDate(Date bespeakDate) {
		this.bespeakDate = bespeakDate;
	}
	public String getBespeakStatus() {
		return bespeakStatus;
	}
	public void setBespeakStatus(String bespeakStatus) {
		this.bespeakStatus = bespeakStatus;
	}
	public String getChieflyShow() {
		return chieflyShow;
	}
	public void setChieflyShow(String chieflyShow) {
		this.chieflyShow = chieflyShow;
	}
	public String getExperName() {
		return experName;
	}
	public void setExperName(String experName) {
		this.experName = experName;
	}
	public String getCheckRemark() {
		return checkRemark;
	}
	public void setCheckRemark(String checkRemark) {
		this.checkRemark = checkRemark;
	}
	
	
}
