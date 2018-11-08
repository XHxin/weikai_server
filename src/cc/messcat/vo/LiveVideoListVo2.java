package cc.messcat.vo;

import java.math.BigDecimal;

public class LiveVideoListVo2 {

	/**
	 * 今日直播专用
	 */
	private Long videoId; 
	private String title;//课程名称
	private String addDate;  //直播申请时间
	private String cover;//封面图片
	private Long fatherId;//如果有值，就代表该视频为连载
	private BigDecimal price;//价格
	private BigDecimal linePrice; //划线价
	private int isVIPView;    //是否VIP可免费看(0:年费和月费都不能看,1:月费、年费可看,2:年费可看)
	
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
	public String getAddDate() {
		return addDate;
	}
	public void setAddDate(String addDate) {
		this.addDate = addDate;
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

	public int getIsVIPView() {
		return isVIPView;
	}
	public void setIsVIPView(int isVIPView) {
		this.isVIPView = isVIPView;
	}
}
