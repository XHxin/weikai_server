package cc.messcat.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author leo
 */
public class LiveVideoVo3 {

	/**
	 * 课程详情Vo(2018/1/31)
	 */
	private int buyStatus;    //购买状态(0:否,1:是)
	private int collectStatus; //收藏状态 0-未收藏，1-已收藏
	private String detailCover;  //详情页的背景图
	private Long duration;   //视频的时长
	private int isVIPView;   //是否VIP可免费看(0:年费和月费都不能看,1:年费可看,2:月费可看)
	private BigDecimal linePrice;  //划线价
	private List<String> photos;  //图片简介
	private BigDecimal price;   //销售价
	private String shareUrl;  //分享Url
	private String title;   //标题
	private String videoUrl; //视频播放Url
	private Long studyCount;  //学习人数
	private int videoStatus; //直播状态(0:未开播 1:直播中)
	private String applyDate;  //直播时间
	private List<SimpleVideoVo> videos;  //视频集
	private Boolean freeOrder; //是否是免单：1-是 0-否
	private Long videoId;      //视频Id
	private int videoType;     //视频类型,0为直播,1为回放,2为录播
	private Long consultMemberId;     //一对一服务的专家号

	
	public int getBuyStatus() {
		return buyStatus;
	}
	public void setBuyStatus(int buyStatus) {
		this.buyStatus = buyStatus;
	}
	public int getCollectStatus() {
		return collectStatus;
	}
	public void setCollectStatus(int collectStatus) {
		this.collectStatus = collectStatus;
	}
	public String getDetailCover() {
		return detailCover;
	}
	public void setDetailCover(String detailCover) {
		this.detailCover = detailCover;
	}
	public Long getDuration() {
		return duration;
	}
	public void setDuration(Long duration) {
		this.duration = duration;
	}
	public int getIsVIPView() {
		return isVIPView;
	}
	public void setIsVIPView(int isVIPView) {
		this.isVIPView = isVIPView;
	}

	public BigDecimal getLinePrice() {
		return linePrice;
	}

	public void setLinePrice(BigDecimal linePrice) {
		this.linePrice = linePrice;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public List<String> getPhotos() {
		return photos;
	}
	public void setPhotos(List<String> photos) {
		this.photos = photos;
	}
	public String getShareUrl() {
		return shareUrl;
	}
	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getVideoUrl() {
		return videoUrl;
	}
	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}
	
	public Long getStudyCount() {
		return studyCount;
	}
	public void setStudyCount(Long studyCount) {
		this.studyCount = studyCount;
	}
	public List<SimpleVideoVo> getVideos() {
		return videos;
	}
	public void setVideos(List<SimpleVideoVo> videos) {
		this.videos = videos;
	}
	public int getVideoStatus() {
		return videoStatus;
	}
	public void setVideoStatus(int videoStatus) {
		this.videoStatus = videoStatus;
	}
	public String getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(String applyDate) {
		this.applyDate = applyDate;
	}
	public Boolean getFreeOrder() {
		return freeOrder;
	}
	public void setFreeOrder(Boolean freeOrder) {
		this.freeOrder = freeOrder;
	}
	public Long getConsultMemberId() {
		return consultMemberId;
	}
	public void setConsultMemberId(Long consultMemberId) {
		this.consultMemberId = consultMemberId;
	}
	public Long getVideoId() {
		return videoId;
	}
	public void setVideoId(Long videoId) {
		this.videoId = videoId;
	}
	public int getVideoType() {
		return videoType;
	}
	public void setVideoType(int videoType) {
		this.videoType = videoType;
	}


}
