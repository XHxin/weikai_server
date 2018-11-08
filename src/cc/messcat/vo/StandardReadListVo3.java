package cc.messcat.vo;

import java.math.BigDecimal;

public class StandardReadListVo3 {
	
	/**
	 * 首页的专栏订阅列表所用
	 */
	private Long articleId;         //文章Id
	private String title;			//标题
	private String time;			//最新的时间
	private String cover;			//封面
	private BigDecimal money;			//价格
	private BigDecimal linePrice;       //划线价
	private String columnIntro;		//专栏订阅简介
	private int isVIPView;          //是否VIP可免费看(0:年费和月费都不能看,1:月费、年费可看,2:年费可看)
	private int isStopUpdate;       //是否停止更新(0:否,1:是)
	private int buyStatus;			//0:未购买，1：已购买
	
	public Long getArticleId() {
		return articleId;
	}
	public void setArticleId(Long articleId) {
		this.articleId = articleId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	public String getColumnIntro() {
		return columnIntro;
	}
	public void setColumnIntro(String intro) {
		this.columnIntro = intro;
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

	public int getIsVIPView() {
		return isVIPView;
	}
	public void setIsVIPView(int isVIPView) {
		this.isVIPView = isVIPView;
	}
	public int getIsStopUpdate() {
		return isStopUpdate;
	}
	public void setIsStopUpdate(int isStopUpdate) {
		this.isStopUpdate = isStopUpdate;
	}
	public int getBuyStatus() {
		return buyStatus;
	}
	public void setBuyStatus(int buyStatus) {
		this.buyStatus = buyStatus;
	}
}
