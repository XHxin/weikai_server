package cc.messcat.vo;

import java.math.BigDecimal;

public class LiveVideoListVo3 {

	/**
	 * 热门课程列表专用Vo
	 */
	private Long id; 
	private String title;//课程名称
	private Long duration;//视频的时长
	private Long viewers;//学习人数
	private String cover;//封面小图
	private String largeCover;  //热门视频列表的大图
	private String middleCover; //系列视频列表的中等图
	private Long fatherId;//值等于-1的为单一视频,值等于0的为系列,有值就是章节或章节里面的子视频
	private BigDecimal price;//价格
	private BigDecimal linePrice;  //划线价
	private int videoType;  //视频类型(0:直播  1:录播  2:点播)
	private int videoStatus;   //直播状态(0:未开播 1:直播中)
	private int isVIPView;    //是否VIP可免费看(0:年费和月费都不能看,1:月费、年费可看,2:年费可看)
	private int buyStatus;	//购买状态（0：否 1：是）
	
	
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
	public String getLargeCover() {
		return largeCover;
	}
	public void setLargeCover(String largeCover) {
		this.largeCover = largeCover;
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

	public int getIsVIPView() {
		return isVIPView;
	}
	public void setIsVIPView(int isVIPView) {
		this.isVIPView = isVIPView;
	}
	public int getVideoType() {
		return videoType;
	}
	public void setVideoType(int videoType) {
		this.videoType = videoType;
	}
	public int getVideoStatus() {
		return videoStatus;
	}
	public void setVideoStatus(int videoStatus) {
		this.videoStatus = videoStatus;
	}
	public int getBuyStatus() {
		return buyStatus;
	}
	public void setBuyStatus(int buyStatus) {
		this.buyStatus = buyStatus;
	}
	public String getMiddleCover() {
		return middleCover;
	}
	public void setMiddleCover(String middleCover) {
		this.middleCover = middleCover;
	}

}
