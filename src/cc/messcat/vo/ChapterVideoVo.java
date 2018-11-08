package cc.messcat.vo;

import java.math.BigDecimal;
import java.util.List;

public class ChapterVideoVo {

	private Long id;//章节id
	private String title;//课程名称
	private BigDecimal price;//价格
	private String buyStatus;//购买状态: 0-未购买,1-已购买
	private int isVIPView;    //是否VIP可免费看(0:年费和月费都不能看,1:年费可看,2:月费可看)
	private List<SimpleVideoVo> videos;//单一视频集合

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getBuyStatus() {
		return buyStatus;
	}

	public void setBuyStatus(String buyStatus) {
		this.buyStatus = buyStatus;
	}
	public List<SimpleVideoVo> getVideos() {
		return videos;
	}

	public void setVideos(List<SimpleVideoVo> videos) {
		this.videos = videos;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public int getIsVIPView() {
		return isVIPView;
	}
	public void setIsVIPView(int isVIPView) {
		this.isVIPView = isVIPView;
	}
	
}
