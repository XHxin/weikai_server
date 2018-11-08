package com.wxpay.pay;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 统一下单结果--APP支付
 * 
 * @author Geoff
 * @Version 1.0
 */
@XmlRootElement(name = "xml")
public class UnifiedOrderResultApp {

	/**
	 * 字段名：返回状态码
	 *
	 * 必填：是
	 *
	 * 类型：String(16)
	 *
	 * 描述：SUCCESS/FAIL
	 *
	 * 此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断
	 */
	private String return_code;
	/**
	 * 字段名：返回信息
	 *
	 * 必填：否
	 *
	 * 类型：String(128)
	 *
	 * 描述：返回信息，如非空，为错误原因
	 *
	 * 签名失败、参数格式校验错误等
	 */
	private String return_msg;
	// *** 以下字段在return_code为SUCCESS的时候有返回 ***//
	private String appid; // 调用接口提交的应用ID
	private String mch_id; // 商户号
	private String device_info; // 设备号
	private String nonce_str; // 随机字符串
	private String sign; // 签名
	private String result_code; // 业务结果 SUCCESS/FAIL
	private String err_code; // 错误代码
	private String err_code_des; // 错误代码描述
	// *** 以下字段在return_code 和result_code都为SUCCESS的时候有返回 ***//
	private String trade_type; // 交易类型 调用接口提交的交易类型，取值如下：JSAPI，NATIVE，APP
	private String prepay_id; // 预支付交易会话标识
	private String out_trade_no;//
	private String transaction_id;//
	private String time_end;
	private String total_fee;//
	private String bank_type;
	private String feedbackid;
	private String is_subscribe;
	private Integer fee_type;
	private Integer cash_fee;
	private String openid;

	/**
	 * 通信是否成功
	 *
	 * @return 成功返回True，否则返回false
	 */
	public boolean isSuccess() {
		if (return_code == null || return_code.equals("")) {
			return false;
		}
		return return_code.toUpperCase().equals("SUCCESS");
	}

	public String getReturn_code() {
		return return_code;
	}

	@XmlElement(name = "return_code")
	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}

	public String getReturn_msg() {
		return return_msg;
	}

	@XmlElement(name = "return_msg")
	public void setReturn_msg(String return_msg) {
		this.return_msg = return_msg;
	}

	public String getAppid() {
		return appid;
	}

	@XmlElement(name = "appid")
	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getMch_id() {
		return mch_id;
	}

	@XmlElement(name = "mch_id")
	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}

	public String getDevice_info() {
		return device_info;
	}

	@XmlElement(name = "device_info")
	public void setDevice_info(String device_info) {
		this.device_info = device_info;
	}

	public String getNonce_str() {
		return nonce_str;
	}

	@XmlElement(name = "nonce_str")
	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}

	public String getSign() {
		return sign;
	}

	@XmlElement(name = "sign")
	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getResult_code() {
		return result_code;
	}

	@XmlElement(name = "result_code")
	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}

	public String getErr_code() {
		return err_code;
	}

	@XmlElement(name = "err_code")
	public void setErr_code(String err_code) {
		this.err_code = err_code;
	}

	public String getErr_code_des() {
		return err_code_des;
	}

	@XmlElement(name = "err_code_des")
	public void setErr_code_des(String err_code_des) {
		this.err_code_des = err_code_des;
	}

	public String getTrade_type() {
		return trade_type;
	}

	@XmlElement(name = "trade_type")
	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}

	public String getPrepay_id() {
		return prepay_id;
	}

	@XmlElement(name = "prepay_id")
	public void setPrepay_id(String prepay_id) {
		this.prepay_id = prepay_id;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	@XmlElement(name = "out_trade_no")
	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getTransaction_id() {
		return transaction_id;
	}

	@XmlElement(name = "transaction_id")
	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}

	public String getTime_end() {
		return time_end;
	}

	@XmlElement(name = "time_end")
	public void setTime_end(String time_end) {
		this.time_end = time_end;
	}

	public String getTotal_fee() {
		return total_fee;
	}

	@XmlElement(name = "total_fee")
	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}

	public String getBank_type() {
		return bank_type;
	}

	@XmlElement(name = "bank_type")
	public void setBank_type(String bank_type) {
		this.bank_type = bank_type;
	}

	public String getFeedbackid() {
		return feedbackid;
	}

	@XmlElement(name = "feedbackid")
	public void setFeedbackid(String feedbackid) {
		this.feedbackid = feedbackid;
	}

	public String getIs_subscribe() {
		return is_subscribe;
	}

	@XmlElement(name = "is_subscribe")
	public void setIs_subscribe(String is_subscribe) {
		this.is_subscribe = is_subscribe;
	}

	public Integer getFee_type() {
		return fee_type;
	}

	@XmlElement(name = "fee_type")
	public void setFee_type(Integer fee_type) {
		this.fee_type = fee_type;
	}

	public Integer getCash_fee() {
		return cash_fee;
	}

	@XmlElement(name = "cash_fee")
	public void setCash_fee(Integer cash_fee) {
		this.cash_fee = cash_fee;
	}

	public String getOpenid() {
		return openid;
	}

	@XmlElement(name = "openid")
	public void setOpenid(String openid) {
		this.openid = openid;
	}

}
