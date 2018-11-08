package cc.messcat.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 直播视频实体类
 * @author HASEE
 *
 */
@Entity
@Table(name="live_video")
public class LiveVideo {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String title;//课程名称
	private Long duration;//视频的时长
	private Long viewers;//观看人数
	
	@Column(name="carousel_cover")
	private String carouselCover;  //视频课程的今日直播图片
	
	@Column(name="large_cover")
	private String largeCover;  //热门视频首页大图
	
	@Column(name="middle_cover")
	private String middleCover;  //系列课程首页大图
	
	@Column(name="detail_cover")
	private String detailCover;  //详情页的背景大图
	
	@Column(name="invited_cover")
	private String invitedCover; //邀请卡图片
	
	private String cover;//封面图片
	private Integer classify; //视频分类(1:系列,2:章节,3:章节的子视频,4:单一视频)
	
	@Column(name="fatherID")
	private Long fatherId;//-1为单一视频,0为系列视频,大于0的值用于访问连载里面子视频的详情
	
	@Column(name="video_type")
	private String videoType;//视频类型,0为直播,1为录播,2为点播
	
	private String videoUrl;//录播url
	private String introduct;//视频简介
	private String terminal;//观看设备，0为移动端，1为PC端
	private BigDecimal price;//价格
	
	@Column(name="line_price")
	private BigDecimal linePrice;  //划线价

	private String status;//状态，0为可不用，1为可用
	
	@Column(name="apply_date")
	private Date applyDate;//直播时间

	private Long expertId;//直播专家的ID
	
	@Column(name="bespeak_date")
	private Date bespeakDate;//预约的时间
	
	@Column(name="bespeak_status")
	private String bespeakStatus;//预约的状态 0-预约中，1-已预约，2-取消预约 3-预约失败  4-结束直播
	
	@Column(name="chiefly_show")
	private String chieflyShow;//在视频课程(百位代表在轮播图,十位数上代表热门视频,个位数上代表系列课程),如111代表在两个三地方都首页显示
	
	@Column(name="course_show")
    private Integer courseShow;  //在首页显示(十位数上代表今日直播,个位数上代表精品课程),(0:不显示,1:在首页,2:在查看更多)

	private String expertName;//直播专家的名称
	
	@Column(name="check_remark")
	private String checkRemark;//审核备注，审核通过与否发送给专家
	
	private String flvUrl;
	private String hlsUrl;
	private String rtmpUrl;
	private String photos;
	
	@Column(name="is_hot")
	private String isHot;//是否是热门	0-否，1-是
	
	@Column(name="only_whole_buy")
    private String onlyWholeBuy;   // 只能整套购买(0: 否 1:是)	
	
	@Column(name="video_status")
    private String videoStatus;   //直播状态(0:未开播, 1:直播中)
	
	@Column(name="is_silence")
    private Integer isSilence;   //是否禁言(0:否, 1:是)
	
	@Column(name="study_count")
    private Long studyCount;	//学习人数
	
	@Column(name="subject_id")
    private Integer subjectId;  //专题Id
	
	@Column(name="is_vip_view")
    private Integer isVIPView;  //是否VIP可免费看(0:年费和月费都不能看,1:年费可看,2:月费可看)
	
	@Column(name="documentID")
	private String documentId;  //百度文档Id
    @Column(name="is_free_order")
	private int freeOrder;//是否免单：1-是 0-否
    
    @Column(name="consult_member_id")
	private Long consultMemberId;   //付费咨询对应的memberId
    
    @Column(name="is_gift_card")
    private Integer isGiftCard;   //是否赠送答疑代金券(0:否 ,1:是)
    
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

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
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

	public Long getExpertId() {
		return expertId;
	}
	public void setExpertId(Long expertId) {
		this.expertId = expertId;
	}
	public String getExpertName() {
		return expertName;
	}
	public void setExpertName(String expertName) {
		this.expertName = expertName;
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
	public String getCheckRemark() {
		return checkRemark;
	}
	public void setCheckRemark(String checkRemark) {
		this.checkRemark = checkRemark;
	}
	public String getFlvUrl() {
		return flvUrl;
	}
	public void setFlvUrl(String flvUrl) {
		this.flvUrl = flvUrl;
	}
	public String getHlsUrl() {
		return hlsUrl;
	}
	public void setHlsUrl(String hlsUrl) {
		this.hlsUrl = hlsUrl;
	}
	public String getRtmpUrl() {
		return rtmpUrl;
	}
	public void setRtmpUrl(String rtmpUrl) {
		this.rtmpUrl = rtmpUrl;
	}
	public String getVideoUrl() {
		return videoUrl;
	}
	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}
	public String getPhotos() {
		return photos;
	}
	public void setPhotos(String photos) {
		this.photos = photos;
	}
	public String getIsHot() {
		return isHot;
	}
	public void setIsHot(String isHot) {
		this.isHot = isHot;
	}
	public String getOnlyWholeBuy() {
		return onlyWholeBuy;
	}
	public void setOnlyWholeBuy(String onlyWholeBuy) {
		this.onlyWholeBuy = onlyWholeBuy;
	}
	public String getVideoStatus() {
		return videoStatus;
	}
	public void setVideoStatus(String videoStatus) {
		this.videoStatus = videoStatus;
	}
	public Integer getIsSilence() {
		return isSilence;
	}
	public void setIsSilence(Integer isSilence) {
		this.isSilence = isSilence;
	}
	public Long getStudyCount() {
		return studyCount;
	}
	public void setStudyCount(Long studyCount) {
		this.studyCount = studyCount;
	}
	public Integer getClassify() {
		return classify;
	}
	public void setClassify(Integer classify) {
		this.classify = classify;
	}
	public String getLargeCover() {
		return largeCover;
	}
	public void setLargeCover(String photo1) {
		this.largeCover = photo1;
	}
	public String getMiddleCover() {
		return middleCover;
	}
	public void setMiddleCover(String photo2) {
		this.middleCover = photo2;
	}
	public Integer getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(Integer subjectId) {
		this.subjectId = subjectId;
	}
	public Integer getCourseShow() {
		return courseShow;
	}
	public void setCourseShow(Integer courseShow) {
		this.courseShow = courseShow;
	}
	public String getDetailCover() {
		return detailCover;
	}
	public void setDetailCover(String detailCover) {
		this.detailCover = detailCover;
	}

	public BigDecimal getLinePrice() {
		return linePrice;
	}

	public void setLinePrice(BigDecimal linePrice) {
		this.linePrice = linePrice;
	}

	public Integer getIsVIPView() {
		return isVIPView;
	}
	public void setIsVIPView(Integer isVIPView) {
		this.isVIPView = isVIPView;
	}
	public String getCarouselCover() {
		return carouselCover;
	}
	public void setCarouselCover(String carouselCover) {
		this.carouselCover = carouselCover;
	}
	public String getDocumentId() {
		return documentId;
	}
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}
	public int getFreeOrder() {
		return freeOrder;
	}
	public void setFreeOrder(int freeOrder) {
		this.freeOrder = freeOrder;
	}
	public Long getConsultMemberId() {
		return consultMemberId;
	}
	public void setConsultMemberId(Long consultMemberId) {
		this.consultMemberId = consultMemberId;
	}
	public Integer getIsGiftCard() {
		return isGiftCard;
	}
	public void setIsGiftCard(Integer isGiftCard) {
		this.isGiftCard = isGiftCard;
	}
	public String getInvitedCover() {
		return invitedCover;
	}
	public void setInvitedCover(String invitedCover) {
		this.invitedCover = invitedCover;
	}
}
