package cc.messcat.wechat.weixin.popular.bean.pay;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class PayResult {

	@XmlElement(name="openId")
	private String openid;

	@XmlElement(name="appId")
	private String appid;

	@XmlElement(name="cash_fee")
	private Integer cash_fee;
	
	@XmlElement(name="fee_type")
	private Integer fee_type;

	@XmlElement(name="is_subscribe")
	private String is_subscribe;

	@XmlElement(name="feedbackid")
	private String feedbackid;

	@XmlElement(name="mch_id")
	private String mch_id;

	@XmlElement(name="nonce_str")
	private String nonce_str;

	@XmlElement(name="bank_type")
	private String bank_type;

	@XmlElement(name="out_trade_no")
	private String out_trade_no;

	@XmlElement(name="result_code")
	private String result_code;

	@XmlElement(name="return_code")
	private String return_code;
	
	@XmlElement(name="sign")
	private String sign;
	
	@XmlElement(name="time_end")
	private String time_end;
	
	@XmlElement(name="total_fee")
	private String total_fee;
	
	@XmlElement(name="trade_type")
	private String trade_type;
	
	@XmlElement(name="transaction_id")
	private String transaction_id;			//交易单号

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public Integer getCash_fee() {
		return cash_fee;
	}

	public void setCash_fee(Integer cashFee) {
		cash_fee = cashFee;
	}

	public Integer getFee_type() {
		return fee_type;
	}

	public void setFee_type(Integer feeType) {
		fee_type = feeType;
	}

	public String getIs_subscribe() {
		return is_subscribe;
	}

	public void setIs_subscribe(String isSubscribe) {
		is_subscribe = isSubscribe;
	}

	public String getFeedbackid() {
		return feedbackid;
	}

	public void setFeedbackid(String feedbackid) {
		this.feedbackid = feedbackid;
	}

	public String getMch_id() {
		return mch_id;
	}

	public void setMch_id(String mchId) {
		mch_id = mchId;
	}

	public String getNonce_str() {
		return nonce_str;
	}

	public void setNonce_str(String nonceStr) {
		nonce_str = nonceStr;
	}

	public String getBank_type() {
		return bank_type;
	}

	public void setBank_type(String bankType) {
		bank_type = bankType;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String outTradeNo) {
		out_trade_no = outTradeNo;
	}

	public String getResult_code() {
		return result_code;
	}

	public void setResult_code(String resultCode) {
		result_code = resultCode;
	}

	public String getReturn_code() {
		return return_code;
	}

	public void setReturn_code(String returnCode) {
		return_code = returnCode;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getTime_end() {
		return time_end;
	}

	public void setTime_end(String timeEnd) {
		time_end = timeEnd;
	}

	public String getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(String totalFee) {
		total_fee = totalFee;
	}

	public String getTrade_type() {
		return trade_type;
	}

	public void setTrade_type(String tradeType) {
		trade_type = tradeType;
	}

	public String getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(String transactionId) {
		transaction_id = transactionId;
	}

}
