package cc.messcat.vo;

import java.math.BigDecimal;

public class LiveVo {

	private Long liveId;
	private String title;
	private String cover;
	private Long duration;
	private Long studyCount;
	private BigDecimal price;
	private Long fatherId;
	private String videoType;//0直播    1表示不是直播
	private BigDecimal linePrice;       //划线价
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	public Long getDuration() {
		return duration;
	}
	public void setDuration(Long duration) {
		this.duration = duration;
	}
	public Long getStudyCount() {
		return studyCount;
	}
	public void setStudyCount(Long studyCount) {
		this.studyCount = studyCount;
	}
	public Long getLiveId() {
		return liveId;
	}
	public void setLiveId(Long liveId) {
		this.liveId = liveId;
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
}
