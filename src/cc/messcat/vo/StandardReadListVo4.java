package cc.messcat.vo;

import java.math.BigDecimal;

public class StandardReadListVo4 {
	
	/**
	 * 第五期的质量分享和标准分享列表所用
	 */
	private Long articleId;         //文章Id
	private String title;			//标题
	private String cover;			//封面
	private BigDecimal money;			//价格
	private BigDecimal linePrice;       //画线价
	private String expertName;		//专家名字
	private int isExistVoice;       //是否存在音频文件
	private int isVIPView;          //是否VIP可免费看(0:年费和月费都不能看,1:月费、年费可看,2:年费可看)
	private int buyStatus;			//0:未购买，1：已购买
	private String intro;			//专栏订阅简介
	private int isStopUpdate;       //是否停止更新(0:否,1:是)
	private String time;			//专栏更新时间
	private int type;               //文章类型(0:文章,1:订阅)
	
	
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

	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public int getIsStopUpdate() {
		return isStopUpdate;
	}
	public void setIsStopUpdate(int isStopUpdate) {
		this.isStopUpdate = isStopUpdate;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
}
