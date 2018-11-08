package cc.messcat.vo;

import java.math.BigDecimal;

public class LiveVideoListVo {
    
	/**
	 * 视频专用列表Vo
	 */
	private Long videoId; 
	private String title;//课程名称
	private Long duration;//视频的时长
	private Long viewers;//学习人数
	private String cover;//封面小图
	private String largeCover;  //封面大图
	private Long fatherId;//如果有值，就代表该视频为连载
	private BigDecimal price;//价格
	private BigDecimal linePrice;  //划线价
	private int isVIPView;    //是否VIP可免费看(0:年费和月费都不能看,1:月费、年费可看,2:年费可看)
	private int buyStatus;   //购买状态(0:未购买,1:已购买)
	private Long id;
	private String videoType;
	
	public Long getVideoId() {
		return videoId;
	}
	public void setVideoId(Long id) {
		this.videoId = id;
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

	public String getLargeCover() {
		return largeCover;
	}
	public void setLargeCover(String largeCover) {
		this.largeCover = largeCover;
	}
	public int getIsVIPView() {
		return isVIPView;
	}
	public void setIsVIPView(int isVIPView) {
		this.isVIPView = isVIPView;
	}
	public int getBuyStatus() {
		return buyStatus;
	}
	public void setBuyStatus(int buyStatus) {
		this.buyStatus = buyStatus;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getVideoType() {
		return videoType;
	}
	public void setVideoType(String videoType) {
		this.videoType = videoType;
	}
	
}
