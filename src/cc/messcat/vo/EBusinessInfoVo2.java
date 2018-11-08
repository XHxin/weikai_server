package cc.messcat.vo;

import java.math.BigDecimal;
import java.util.List;

public class EBusinessInfoVo2 {

	private String title;   //标题
	private Long ebusinessProductId;		//产品统称ID
	private String ebusinessProductName;	//产品统称
	private String subEbusinessProductId;	//子产品ID（用;号隔开）
	private String subEbusinessProductName;    //子产品名称
	
	private List<PlatformVo> platforms;
	
	private BigDecimal money;          //价格
	private String collectStatus;  //收藏状态
	private String buyStatus;      //购买状态
	private String shareURL;       //分享URL
	private String url;
	
	public Long getEbusinessProductId() {
		return ebusinessProductId;
	}
	public void setEbusinessProductId(Long ebusinessProductId) {
		this.ebusinessProductId = ebusinessProductId;
	}
	public String getEbusinessProductName() {
		return ebusinessProductName;
	}
	public void setEbusinessProductName(String ebusinessProductName) {
		this.ebusinessProductName = ebusinessProductName;
	}
	public String getSubEbusinessProductId() {
		return subEbusinessProductId;
	}
	public void setSubEbusinessProductId(String subEbusinessProductId) {
		this.subEbusinessProductId = subEbusinessProductId;
	}
	public String getSubEbusinessProductName() {
		return subEbusinessProductName;
	}
	public void setSubEbusinessProductName(String subEbusinessProductName) {
		this.subEbusinessProductName = subEbusinessProductName;
	}
	public List<PlatformVo> getPlatforms() {
		return platforms;
	}
	public void setPlatforms(List<PlatformVo> platforms) {
		this.platforms = platforms;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCollectStatus() {
		return collectStatus;
	}
	public void setCollectStatus(String collectStatus) {
		this.collectStatus = collectStatus;
	}
	public String getBuyStatus() {
		return buyStatus;
	}
	public void setBuyStatus(String buyStatus) {
		this.buyStatus = buyStatus;
	}
	public String getShareURL() {
		return shareURL;
	}
	public void setShareURL(String shareURL) {
		this.shareURL = shareURL;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
}
