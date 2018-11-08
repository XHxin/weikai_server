package cc.messcat.vo;

import java.math.BigDecimal;
import java.util.List;

public class SeriesVideoVo {

	private String cover;//封面图片
	private String applyDate;//直播时间
	private String title;//课程名称
	private Long viewers;//观看人数
	private Long duration;//视频的时长
	private String introduct;//视频简介
	private BigDecimal price;//价格
	private BigDecimal linePrice;   //画线价
	private String[] photos;
	private String collectStatus;//收藏状态	0-未收藏，1-已收藏
	private List<ChapterVideoVo> chapterVideoVos;//章节
	private String shareUrl;//分享系列的url
	private String buyStatus;  //购买状态(0:否,1:是)
	private String onlyWholeBuy;  //只能整套购买(0:否,1:是)
	private String videoUrl;     //视频url
	private String largeCover;   //热门视频的封面
	private String middleCover;  //系列视频的封面
	private String detailCover;  //详情页的背景图
	private int isVIPView;    //是否VIP可免费看(0:年费和月费都不能看,1:年费可看,2:月费可看)
	
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
	public List<ChapterVideoVo> getChapterVideoVos() {
		return chapterVideoVos;
	}
	public void setChapterVideoVos(List<ChapterVideoVo> chapterVideoVos) {
		this.chapterVideoVos = chapterVideoVos;
	}
	public String getShareUrl() {
		return shareUrl;
	}
	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}
	public String getBuyStatus() {
		return buyStatus;
	}
	public void setBuyStatus(String buyStatus) {
		this.buyStatus = buyStatus;
	}
	public String getOnlyWholeBuy() {
		return onlyWholeBuy;
	}
	public void setOnlyWholeBuy(String onlyWholeBuy) {
		this.onlyWholeBuy = onlyWholeBuy;
	}
	public String getVideoUrl() {
		return videoUrl;
	}
	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
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
}
