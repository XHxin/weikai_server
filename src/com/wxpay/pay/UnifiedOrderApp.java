package com.wxpay.pay;

import java.util.HashMap;
import java.util.Map;

/**
 * 统一下单-APP支付 
 * <b>应用场景</b>
 * 商户系统先调用该接口在微信支付服务后台生成预支付交易单，返回正确的预支付交易回话标识后再在APP里面调起支付。
 * 
 * <b>接口链接</b> 
 * URL地址：https://api.mch.weixin.qq.com/pay/unifiedorder
 * 
 * <b>是否需要证书</b> 
 * 不需要
 * 
 * @author Geoff
 * @Version 1.0
 */
public class UnifiedOrderApp {

	private String appid; // 微信开放平台审核通过的应用APPID 是
	private String mch_id; // 商户号 是
	private String device_info; // 设备号
	private String nonce_str; // 随机字符串 是
	private String sign; // 签名 是
	private String body; // 商品描述 是
	private String detail; // 商品详情
	private String attach; // 附加数据
	private String out_trade_no; // 商户订单号 是
	private String fee_type; // 货币类型
	private String total_fee; // 总金额 是
	private String spbill_create_ip; // 终端IP 是
	private String time_start; // 交易起始时间
	private String time_expire; // 交易结束时间
	private String goods_tag; // 商品标记
	private String notify_url; // 通知地址 是
	private String trade_type; // 交易类型 是 取值如下：JSAPI，NATIVE，APP
	private String limit_pay; // no_credit--指定不能使用信用卡支付

	public Map<String, String> toMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("appid", appid);
		map.put("body", body);
		map.put("mch_id", mch_id);
		map.put("nonce_str", nonce_str);
		map.put("notify_url", notify_url);
		map.put("out_trade_no", out_trade_no);
		map.put("spbill_create_ip", spbill_create_ip);
		map.put("total_fee", total_fee);
		map.put("trade_type", trade_type);
		return map;
	}

	public String toXML() {
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		sb.append("<appid><![CDATA[").append(appid).append("]]></appid>");
		sb.append("<body><![CDATA[").append(body).append("]]></body>");
		sb.append("<mch_id><![CDATA[").append(mch_id).append("]]></mch_id>");
		sb.append("<nonce_str><![CDATA[").append(nonce_str).append("]]></nonce_str>");
		sb.append("<notify_url><![CDATA[").append(notify_url).append("]]></notify_url>");
		sb.append("<out_trade_no><![CDATA[").append(out_trade_no).append("]]></out_trade_no>");
		sb.append("<spbill_create_ip><![CDATA[").append(spbill_create_ip).append("]]></spbill_create_ip>");
		sb.append("<total_fee><![CDATA[").append(total_fee).append("]]></total_fee>");
		sb.append("<trade_type><![CDATA[").append(trade_type).append("]]></trade_type>");
		sb.append("<sign><![CDATA[").append(sign).append("]]></sign>");
		sb.append("</xml>");
		return sb.toString();
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}

	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}

	public void setSpbill_create_ip(String spbill_create_ip) {
		this.spbill_create_ip = spbill_create_ip;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}

}
