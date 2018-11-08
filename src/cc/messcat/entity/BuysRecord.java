package cc.messcat.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Member entity. @author MyEclipse Persistence Tools
 */

public class BuysRecord implements java.io.Serializable {
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//消费类型：0：购买会员套餐 1：购买报告 2：购买标准解读 3：购买质量分享 4：付费咨询  6：电商信息
	public static final int type0 = 0;
	public static final int type1 = 1;
	public static final int type2 = 2;
	public static final int type3 = 3;
	public static final int type4 = 4;
	public static final int type6 = 6;
	public static Map<String, String> typeMap=new LinkedHashMap<String, String>();
	static{
		typeMap.put(""+type0, "购买会员套餐");
		typeMap.put(""+type1, "购买报告");
		typeMap.put(""+type2, "购买标准解读");
		typeMap.put(""+type3, "购买质量分享");
		typeMap.put(""+type4, "付费咨询");
		typeMap.put(""+type6, "电商信息");
	}
	
	private Long id;
	private String type;//消费类型：0：购买会员套餐 1：购买报告 2：购买标准解读 3：购买质量分享 4：付费咨询  6：电商信息
	
	
	private String content;//消费具体内容（用于前台显示）
	
	private Date addTime;
	
	private Date editTime;
	
	private String status;//状态（0：停用 1：启用）
	
	private Member memberId;

	private Region regionId; //地区ID
	
	private Product productId; //产品ID
	
	private String number;//订单号
	
	private BigDecimal money;//消费金额  //实付金额
	
	private BigDecimal originalPrice;//原价
	
	private String payStatus;//付款状态（0：未付款 1：已付款 2:退款）
	
	private Date payTime;//付款时间
	
	private String payType;//付款类型（1、微信  2：支付宝）
	
	private Packages packagesId;//套餐id
	
	private StandardReading standardReadId;//标准解读id、质量分享id
	
	private Long ebusinessProductId;  //电商信息Id
	
	private Long hotProblemId;     //问题表ID(用于付费咨询)
	
	private Long replyExpertId;    //回答问题的专家ID

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getAddTime() {
		return this.addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public Date getEditTime() {
		return this.editTime;
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

	public Region getRegionId() {
		return regionId;
	}

	public void setRegionId(Region regionId) {
		this.regionId = regionId;
	}

	public Product getProductId() {
		return productId;
	}

	public void setProductId(Product productId) {
		this.productId = productId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Member getMemberId() {
		return memberId;
	}

	public void setMemberId(Member memberId) {
		this.memberId = memberId;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public void setOriginalPrice(BigDecimal originalPrice) {
		this.originalPrice = originalPrice;
	}

	public String getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public Packages getPackagesId() {
		return packagesId;
	}

	public StandardReading getStandardReadId() {
		return standardReadId;
	}

	public void setStandardReadId(StandardReading standardReadId) {
		this.standardReadId = standardReadId;
	}

	public void setPackagesId(Packages packagesId) {
		this.packagesId = packagesId;
	}

	public Long getEbusinessProductId() {
		return ebusinessProductId;
	}

	public void setEbusinessProductId(Long ebusinessProductId) {
		this.ebusinessProductId = ebusinessProductId;
	}

	public BigDecimal getOriginalPrice() {
		return originalPrice;
	}

	public Long getHotProblemId() {
		return hotProblemId;
	}

	public void setHotProblemId(Long hotProblemId) {
		this.hotProblemId = hotProblemId;
	}

	public Long getReplyExpertId() {
		return replyExpertId;
	}

	public void setReplyExpertId(Long replyExpertId) {
		this.replyExpertId = replyExpertId;
	}
}