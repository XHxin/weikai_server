package cc.messcat.vo;

import java.math.BigDecimal;

public class StandardReadListVo5 {
	
	/**
	 * 第五期的精品文章所用
	 */
	private Long articleId;         //文章Id
	private String title;			//标题
	private String cover;			//封面
	private BigDecimal money;			//价格
	private BigDecimal linePrice;       //划线价
	private int isExistVoice;       //是否存在音频文件
	private String expertName;      //专家名字
	private int buyStatus;          //购买状态(0:否,1:是)
	private int isVIPView;          //是否VIP可免费看(0:年费和月费都不能看,1:月费、年费可看,2:年费可看)
	
	public Long getArticleId() {
		return articleId;
	}
	public void setArticleId(Long id) {
		this.articleId = id;
	}
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
	public String getExpertName() {
		return expertName;
	}
	public void setExpertName(String expertName) {
		this.expertName = expertName;
	}
	public int getIsExistVoice() {
		return isExistVoice;
	}
	public void setIsExistVoice(int isExistVoice) {
		this.isExistVoice = isExistVoice;
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

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public BigDecimal getLinePrice() {
		return linePrice;
	}

	public void setLinePrice(BigDecimal linePrice) {
		this.linePrice = linePrice;
	}
}
