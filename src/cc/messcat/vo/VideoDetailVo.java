package cc.messcat.vo;


import java.math.BigDecimal;

public class VideoDetailVo {

	private String id;    //直播的预约Id
	private String largeCover;   //热门视频的封面
	private String middleCover;  //系列视频的封面
	private String detailCover;  //详情页的背景图
	private String cover;//封面图片
	private String applyDate;//直播时间
	private String title;//课程名称
	private Long viewers;//观看人数
	private Long duration;//视频的时长
	private String introduct;//视频简介
	private BigDecimal price;//价格
	private BigDecimal linePrice;  //画线价
	private String[] photos;   //详情页面的图片
	private String collectStatus;//收藏状态,0-未收藏,1-已收藏
	private Long expertId;     //专家Id
	private String shareURL;  //分享URL
	private String buyStatus;//购买状态,0-未购买,1-已购买
	private String flvUrl;
	private String hlsUrl;
	private String rtmpUrl;
	private String subhead;//副标题
	private String liveStatus;//直播情况	0-直播前	1-直播中
	private String videoUrl;//录播url
	private Long fatherId;
	private String videoType;
	private String videoStatus;  //0:未开播,1:直播中
	private int isVIPView;    //是否VIP可免费看(0:年费和月费都不能看,1:年费可看,2:月费可看)
	private Boolean freeOrder;//是否免单：1-是 0-否
	
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	public String getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(String applyDate) {
		this.applyDate = applyDate;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Long getViewers() {
		return viewers;
	}
	public void setViewers(Long viewers) {
		this.viewers = viewers;
	}
	public Long getDuration() {
		return duration;
	}
	public void setDuration(Long duration) {
		this.duration = duration;
	}
	public String getIntroduct() {
		return introduct;
	}
	public void setIntroduct(String introduct) {
		this.introduct = introduct;
	}
	public String[] getPhotos() {
		return photos;
	}
	public void setPhotos(String[] photos) {
		this.photos = photos;
	}
	public String getCollectStatus() {
		return collectStatus;
	}
	public void setCollectStatus(String collectStatus) {
		this.collectStatus = collectStatus;
	}
	public Long getExpertId() {
		return expertId;
	}
	public void setExpertId(Long expertId) {
		this.expertId = expertId;
	}
	public String getShareURL() {
		return shareURL;
	}
	public void setShareURL(String shareURL) {
		this.shareURL = shareURL;
	}
	public String getBuyStatus() {
		return buyStatus;
	}
	public void setBuyStatus(String buyStatus) {
		this.buyStatus = buyStatus;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getSubhead() {
		return subhead;
	}
	public void setSubhead(String subhead) {
		this.subhead = subhead;
	}
	public String getLiveStatus() {
		return liveStatus;
	}
	public void setLiveStatus(String liveStatus) {
		this.liveStatus = liveStatus;
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
	public String getVideoStatus() {
		return videoStatus;
	}
	public void setVideoStatus(String videoStatus) {
		this.videoStatus = videoStatus;
	}
	public String getLargeCover() {
		return largeCover;
	}
	public void setLargeCover(String largeCover) {
		this.largeCover = largeCover;
	}
	public String getMiddleCover() {
		return middleCover;
	}
	public void setMiddleCover(String middleCover) {
		this.middleCover = middleCover;
	}
	public String getDetailCover() {
		return detailCover;
	}
	public void setDetailCover(String detailCover) {
		this.detailCover = detailCover;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getLinePrice() {
		return linePrice;
	}

	public void setLinePrice(BigDecimal linePrice) {
		this.linePrice = linePrice;
	}

	public int getIsVIPView() {
		return isVIPView;
	}
	public void setIsVIPView(int isVIPView) {
		this.isVIPView = isVIPView;
	}
	public Boolean getFreeOrder() {
		return freeOrder;
	}
	public void setFreeOrder(Boolean freeOrder) {
		this.freeOrder = freeOrder;
	}
	
}
