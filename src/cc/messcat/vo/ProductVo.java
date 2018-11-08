package cc.messcat.vo;

import java.math.BigDecimal;
import java.util.Date;

public class ProductVo {

	private Long productId;
	
	private String name; //产品
	
	private Long fatherId; //父级
	
//	@Column(name = "add_time")
	private Date addTime;
	
//	@Column(name = "edit_time")
	private Date editTime;
	
	private String status;
//	
	private String fatherName;
	
	private Long regionId;
	
	private String url;//域名/weikai/epReportAction!getReport.action?regionId=&productId=
	
	private String urlV2;   //url的第二期版本 
	
	private String collectStatus;//收藏状态
	
	private String buyStutas;//购买状态
	
	private BigDecimal money;//报告购买价格
	
	private String shareURL;   //分享URL

	

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getFatherId() {
		return fatherId;
	}

	public void setFatherId(Long fatherId) {
		this.fatherId = fatherId;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public Date getEditTime() {
		return editTime;
	}

	public void setEditTime(Date editTime) {
		this.editTime = editTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFatherName() {
		return fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	public Long getRegionId() {
		return regionId;
	}

	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCollectStatus() {
		return collectStatus;
	}

	public void setCollectStatus(String collectStatus) {
		this.collectStatus = collectStatus;
	}

	public String getBuyStutas() {
		return buyStutas;
	}

	public void setBuyStutas(String buyStutas) {
		this.buyStutas = buyStutas;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public String getShareURL() {
		return shareURL;
	}
	public void setShareURL(String shareURL) {
		this.shareURL = shareURL;
	}

	public String getUrlV2() {
		return urlV2;
	}

	public void setUrlV2(String urlV2) {
		this.urlV2 = urlV2;
	}
}
