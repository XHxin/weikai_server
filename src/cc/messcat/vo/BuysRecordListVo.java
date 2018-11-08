package cc.messcat.vo;

import java.math.BigDecimal;

public class BuysRecordListVo {

	private Long buysRecordId;	    //购买id
	private String title;			//购买信息标题
	private BigDecimal money;			//购买金额
	private String type;			//消费类型(1:准入报告  2：标准解读  3：质量分享  4：付费咨询)
	private String isDown;          //1 百度云，0非百度云
	private String  payTime;        //购买日期
	
	private String url;             //准入报告url
	private Long regionId;		    //地区id（用于准入报告）
	private Long productId; 		//产品id（用于准入报告）
	private String collectStatus;   //收藏状态
	private String buyStatus;   	//购买状态
	private Long ebusinessId; 		//电商产品id
	
	private EBusinessInfoVo2 eBusinessInfoVo;   //电商信息
	private HotReplyPayVo  payProblem;          //付费咨询专用
	private String shareURL;        //分享URL
	private Long videoId;
	private Long fatherId;
	private String videoType;
	
    /**
     * 因为质量分享列表是根据qualityID区分的 ,所以就重新写了一个VoList
     */
	private QualityShareListVo standardReadListVo; //解读、质量分享信息
	
	
	public Long getBuysRecordId() {
		return buysRecordId;
	}
	public void setBuysRecordId(Long buysRecordId) {
		this.buysRecordId = buysRecordId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getIsDown() {
		return isDown;
	}
	public void setIsDown(String isDown) {
		this.isDown = isDown;
	}
	public String getPayTime() {
		return payTime;
	}
	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Long getRegionId() {
		return regionId;
	}
	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
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
	public QualityShareListVo getStandardReadListVo() {
		return standardReadListVo;
	}
	public void setStandardReadListVo(QualityShareListVo standardReadListVo) {
		this.standardReadListVo = standardReadListVo;
	}
	public Long getEbusinessId() {
		return ebusinessId;
	}
	public void setEbusinessId(Long ebusinessId) {
		this.ebusinessId = ebusinessId;
	}
	public EBusinessInfoVo2 geteBusinessInfoVo() {
		return eBusinessInfoVo;
	}
	public void seteBusinessInfoVo(EBusinessInfoVo2 eBusinessInfoVo) {
		this.eBusinessInfoVo = eBusinessInfoVo;
	}
	public HotReplyPayVo getPayProblem() {
		return payProblem;
	}
	public void setPayProblem(HotReplyPayVo payProblem) {
		this.payProblem = payProblem;
	}
	public String getShareURL() {
		return shareURL;
	}
	public void setShareURL(String shareURL) {
		this.shareURL = shareURL;
	}
	public Long getVideoId() {
		return videoId;
	}
	public void setVideoId(Long videoId) {
		this.videoId = videoId;
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
	
}
