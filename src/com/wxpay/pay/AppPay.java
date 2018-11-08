package com.wxpay.pay;

import java.util.HashMap;
import java.util.Map;

/**
 * APP支付的请求参数
 * 
 * @author Geoff
 * @Version 1.0
 */
public class AppPay {

	private final String appId; // 公众号Id
	private final String partnerid;// 微信支付分配的商户号
	private final String prepayid;// 微信返回的支付交易会话ID
	private final String packages;// 暂时固定值
	private final String nonceStr; // 支付签名随机串
	private final String timestamp; // 支付签名时间戳
	private final String paySign; // 支付签名
	private final String out_trade_no;//订单号

	public AppPay(String appId, String partnerid, String prepayid, String partnerKey,String outTradeNo) {
		this.appId = appId;
		this.partnerid = partnerid;
		this.prepayid = prepayid;
		this.packages = "Sign=WXPay";
		this.nonceStr = java.util.UUID.randomUUID().toString().substring(0, 15);
		this.timestamp = System.currentTimeMillis() / 1000 + "";

		// 对提交的参数进行签名
		Map<String, String> paySignMap = new HashMap<String, String>();
		paySignMap.put("appid", appId);
		paySignMap.put("noncestr", nonceStr);
		paySignMap.put("package", packages);
		paySignMap.put("partnerid", partnerid);
		paySignMap.put("prepayid", prepayid);
		paySignMap.put("timestamp", timestamp);

		// 生成支付签名
		this.paySign = SignUtil.getSign(paySignMap, partnerKey);
		this.out_trade_no = outTradeNo;
	}
/*	public AppPay(String appId, String partnerid, String prepayid, String partnerKey) {
		this.appId = appId;
		this.partnerid = partnerid;
		this.prepayid = prepayid;
		this.packages = "Sign=WXPay";
		this.nonceStr = java.util.UUID.randomUUID().toString().substring(0, 15);
		this.timestamp = System.currentTimeMillis() / 1000 + "";

		// 对提交的参数进行签名
		Map<String, String> paySignMap = new HashMap<String, String>();
		paySignMap.put("appid", appId);
		paySignMap.put("noncestr", nonceStr);
		paySignMap.put("package", packages);
		paySignMap.put("partnerid", partnerid);
		paySignMap.put("prepayid", prepayid);
		paySignMap.put("timestamp", timestamp);

		// 生成支付签名
		this.paySign = SignUtil.getSign(paySignMap, partnerKey);
	}*/
	public String getTimeStamp() {
		return timestamp;
	}

	public String getNonceStr() {
		return nonceStr;
	}

	public String getPaySign() {
		return paySign;
	}

	public String getPartnerid() {
		return partnerid;
	}

	public String getPrepayid() {
		return prepayid;
	}

	public String getAppId() {
		return appId;
	}

	public String getPackages() {
		return packages;
	}
	public String getOut_trade_no() {
		return out_trade_no;
	}

}
