package cc.messcat.entity;

import java.util.Date;

/**
 * 微信返回的账单记录
 * @author xiehuaxin
 * @createDate 2018年7月26日 下午4:24:04
 * @todo TODO
 */
public class WechatBillRows {

	//交易时间
	private Date dealTime;
	
	//公众账号ID
	private String officialAccountsId;
	
	//商户号
	private String merchantCode;
	
	//子商户号
	private String sonOfMerchantCode;
	
	//设备号
	private String equipmentNum;
	
	//微信订单号
	private String wechatOrderNum;
	
	//商户订单号
	private String merchantOrderNum;
	
	//用户标识
	private String userMark;
	
	//交易类型
	private String dealType;
	
	//交易状态
	private String dealStatus;
	
	//付款银行
	private String bank;
	
	//货币种类
	private String currencyType;
	
	//总金额
	private String totalMoney;
	
	//企业红包金额
	private String enterpriseRedEnvelopeMoney;
	
	//微信退款单号
	private String wechatRefundOrderNum;
	
	//商户退款单号
	private String merchantRefundOrderNum;
	
	//退款金额
	private String refundMoney;
	
	//企业红包退款金额
	private String enterpriseRedEnvelopeRefundMoney;
	
	//退款类型
	private String refundType;
	
	//退款状态
	private String refundStatus;
	
	//商品名称
	private String goodsName;
	
	//商户数据包
	private String merchantDataPackage;
	
	//手续费
	private String serviceCharge;
	
	//费率
	private String rate;

	public Date getDealTime() {
		return dealTime;
	}

	public void setDealTime(Date dealTime) {
		this.dealTime = dealTime;
	}

	public String getOfficialAccountsId() {
		return officialAccountsId;
	}

	public void setOfficialAccountsId(String officialAccountsId) {
		this.officialAccountsId = officialAccountsId;
	}

	public String getMerchantCode() {
		return merchantCode;
	}

	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

	public String getSonOfMerchantCode() {
		return sonOfMerchantCode;
	}

	public void setSonOfMerchantCode(String sonOfMerchantCode) {
		this.sonOfMerchantCode = sonOfMerchantCode;
	}

	public String getEquipmentNum() {
		return equipmentNum;
	}

	public void setEquipmentNum(String equipmentNum) {
		this.equipmentNum = equipmentNum;
	}

	public String getWechatOrderNum() {
		return wechatOrderNum;
	}

	public void setWechatOrderNum(String wechatOrderNum) {
		this.wechatOrderNum = wechatOrderNum;
	}

	public String getMerchantOrderNum() {
		return merchantOrderNum;
	}

	public void setMerchantOrderNum(String merchantOrderNum) {
		this.merchantOrderNum = merchantOrderNum;
	}

	public String getUserMark() {
		return userMark;
	}

	public void setUserMark(String userMark) {
		this.userMark = userMark;
	}

	public String getDealType() {
		return dealType;
	}

	public void setDealType(String dealType) {
		this.dealType = dealType;
	}

	public String getDealStatus() {
		return dealStatus;
	}

	public void setDealStatus(String dealStatus) {
		this.dealStatus = dealStatus;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}

	public String getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}

	public String getEnterpriseRedEnvelopeMoney() {
		return enterpriseRedEnvelopeMoney;
	}

	public void setEnterpriseRedEnvelopeMoney(String enterpriseRedEnvelopeMoney) {
		this.enterpriseRedEnvelopeMoney = enterpriseRedEnvelopeMoney;
	}

	public String getWechatRefundOrderNum() {
		return wechatRefundOrderNum;
	}

	public void setWechatRefundOrderNum(String wechatRefundOrderNum) {
		this.wechatRefundOrderNum = wechatRefundOrderNum;
	}

	public String getMerchantRefundOrderNum() {
		return merchantRefundOrderNum;
	}

	public void setMerchantRefundOrderNum(String merchantRefundOrderNum) {
		this.merchantRefundOrderNum = merchantRefundOrderNum;
	}

	public String getRefundMoney() {
		return refundMoney;
	}

	public void setRefundMoney(String refundMoney) {
		this.refundMoney = refundMoney;
	}

	public String getEnterpriseRedEnvelopeRefundMoney() {
		return enterpriseRedEnvelopeRefundMoney;
	}

	public void setEnterpriseRedEnvelopeRefundMoney(String enterpriseRedEnvelopeRefundMoney) {
		this.enterpriseRedEnvelopeRefundMoney = enterpriseRedEnvelopeRefundMoney;
	}

	public String getRefundType() {
		return refundType;
	}

	public void setRefundType(String refundType) {
		this.refundType = refundType;
	}

	public String getRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(String refundStatus) {
		this.refundStatus = refundStatus;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getMerchantDataPackage() {
		return merchantDataPackage;
	}

	public void setMerchantDataPackage(String merchantDataPackage) {
		this.merchantDataPackage = merchantDataPackage;
	}

	public String getServiceCharge() {
		return serviceCharge;
	}

	public void setServiceCharge(String serviceCharge) {
		this.serviceCharge = serviceCharge;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}
	
	
}
