package com.alipay.util;

import java.util.HashMap;
import java.util.Map;

import com.alipay.config.AlipayConfig;
/**
 * 自己构造支付宝相关util
 * @author Geoff
 * @Version 1.0
 */
public class AlipayUtil {
	
	public final static String SUBJECT="威凯支付宝支付";
	public final static String OBJECT="威凯支付宝支付";

	/**
	 * 生成要请求给支付宝的请求前的参数数组
	 * 
	 * @author Geoff
	 * @return
	 * @date 2016年9月30日
	 * @version 1.0
	 */
	public static Map<String, String> createRequestMap(String total_amount, String subject, String body, String out_trade_no) {

		Map<String, String> params = new HashMap<String, String>();
		/*
		 * params.put("app_id", AlipayConfig.appid); params.put("biz_content",
		 * getBizContent( total_amount, subject, body, out_trade_no));
		 * params.put("charset", AlipayConfig.input_charset);
		 * params.put("format", "json");//仅支持JSON params.put("method",
		 * AlipayConfig.app_pay_url); params.put("notify_url",
		 * AlipayConfig.app_pay_notify_url); params.put("sign_type",
		 * AlipayConfig.sign_type); params.put("timestamp",
		 * UtilDate.getDateFormatter()); params.put("version",
		 * "1.0");//调用的接口版本，固定为：1.0
		 */ 
		params.put("_input_charset", AlipayConfig.input_charset);
		params.put("service", AlipayConfig.service_pay);
		params.put("notify_url", AlipayConfig.app_pay_notify_url);
		// params.put("sign_type", AlipayConfig.SING_TYPE);
		params.put("out_trade_no", out_trade_no);
		params.put("payment_type", AlipayConfig.payment_type);
		params.put("seller_id", AlipayConfig.seller_id);
		params.put("total_fee", total_amount);
		params.put("body", body);
		params.put("subject", subject);
		params.put("partner", AlipayConfig.partner);
		params.put("it_b_pay",  AlipayConfig.timeout_express);
		return params;
	}
	
	/**
	 * 生成要请求给支付宝的请求前的参数数组
	 * @return
	 * @version 1.0
	 */
	public static Map<String, String> createRequestMapByCondition(String total_amount, String subject, String body, String out_trade_no,String app_pay_notify_url) {

		Map<String, String> params = new HashMap<String, String>();
		/*
		 * params.put("app_id", AlipayConfig.appid); params.put("biz_content",
		 * getBizContent( total_amount, subject, body, out_trade_no));
		 * params.put("charset", AlipayConfig.input_charset);
		 * params.put("format", "json");//仅支持JSON params.put("method",
		 * AlipayConfig.app_pay_url); params.put("notify_url",
		 * AlipayConfig.app_pay_notify_url); params.put("sign_type",
		 * AlipayConfig.sign_type); params.put("timestamp",
		 * UtilDate.getDateFormatter()); params.put("version",
		 * "1.0");//调用的接口版本，固定为：1.0
		 */ 
		params.put("_input_charset", AlipayConfig.input_charset);
		params.put("service", AlipayConfig.service_pay);
		params.put("notify_url", app_pay_notify_url);
		// params.put("sign_type", AlipayConfig.SING_TYPE);
		params.put("out_trade_no", out_trade_no);
		params.put("payment_type", AlipayConfig.payment_type);
		params.put("seller_id", AlipayConfig.seller_id);
		params.put("total_fee", total_amount);
		params.put("body", body);
		params.put("subject", subject);
		params.put("partner", AlipayConfig.partner);
		params.put("it_b_pay",  AlipayConfig.timeout_express);
		return params;
	}
	
	
	public static Map<String, String> createRequestMapVip(String total_amount, String subject, String body, String out_trade_no) {

		Map<String, String> params = new HashMap<String, String>();
		/*
		 * params.put("app_id", AlipayConfig.appid); params.put("biz_content",
		 * getBizContent( total_amount, subject, body, out_trade_no));
		 * params.put("charset", AlipayConfig.input_charset);
		 * params.put("format", "json");//仅支持JSON params.put("method",
		 * AlipayConfig.app_pay_url); params.put("notify_url",
		 * AlipayConfig.app_pay_notify_url); params.put("sign_type",
		 * AlipayConfig.sign_type); params.put("timestamp",
		 * UtilDate.getDateFormatter()); params.put("version",
		 * "1.0");//调用的接口版本，固定为：1.0
		 */ 
		params.put("_input_charset", AlipayConfig.input_charset);
		params.put("service", AlipayConfig.service_pay);
		params.put("notify_url", AlipayConfig.app_pay_notify_url_vip);
		// params.put("sign_type", AlipayConfig.SING_TYPE);
		params.put("out_trade_no", out_trade_no);
		params.put("payment_type", AlipayConfig.payment_type);
		params.put("seller_id", AlipayConfig.seller_id);
		params.put("total_fee", total_amount);
		params.put("body", body);
		params.put("subject", subject);
		params.put("partner", AlipayConfig.partner);
		params.put("it_b_pay",  AlipayConfig.timeout_express);
		return params;
	}

	
	public static Map<String, String> createRequestMapWallet(String total_amount, String subject, String body, String out_trade_no) {

		Map<String, String> params = new HashMap<String, String>();
		/*
		 * params.put("app_id", AlipayConfig.appid); params.put("biz_content",
		 * getBizContent( total_amount, subject, body, out_trade_no));
		 * params.put("charset", AlipayConfig.input_charset);
		 * params.put("format", "json");//仅支持JSON params.put("method",
		 * AlipayConfig.app_pay_url); params.put("notify_url",
		 * AlipayConfig.app_pay_notify_url); params.put("sign_type",
		 * AlipayConfig.sign_type); params.put("timestamp",
		 * UtilDate.getDateFormatter()); params.put("version",
		 * "1.0");//调用的接口版本，固定为：1.0
		 */ 
		params.put("_input_charset", AlipayConfig.input_charset);
		params.put("service", AlipayConfig.service_pay);
		params.put("notify_url", AlipayConfig.app_pay_notify_url_wallet);
		// params.put("sign_type", AlipayConfig.SING_TYPE);
		params.put("out_trade_no", out_trade_no);
		params.put("payment_type", AlipayConfig.payment_type);
		params.put("seller_id", AlipayConfig.seller_id);
		params.put("total_fee", total_amount);
		params.put("body", body);
		params.put("subject", subject);
		params.put("partner", AlipayConfig.partner);
		params.put("it_b_pay",  AlipayConfig.timeout_express);
		return params;
	}
	
	
	
	/**
	 * 构造biz_content--业务参数
	 * 
	 * @author Geoff
	 * @param total_amount
	 *            订单总金额，单位为元，精确到小数点后两位，
	 * @param subject
	 *            商品的标题/交易标题/订单标题/订单关键字等
	 * @param body
	 *            对一笔交易的具体描述信息
	 * @param out_trade_no
	 *            商户网站唯一订单号
	 * @return
	 * @date 2016年9月30日
	 */
	public static String getBizContent(String total_amount, String subject, String body, String out_trade_no) {
		StringBuffer content = new StringBuffer();
		content.append("{");
		content.append("\"timeout_express\":\"" + AlipayConfig.timeout_express + "\"" + ",");
		content.append("\"seller_id\":\"" + AlipayConfig.seller_id + "\"" + ",");
		content.append("\"product_code\":\"" + AlipayConfig.product_code + "\"" + ",");
		content.append("\"total_amount\":\"" + total_amount + "\"" + ",");
		content.append("\"subject\":\"" + subject + "\"" + ",");
		content.append("\"body\":\"" + body + "\"" + ",");
		content.append("\"out_trade_no\":\"" + out_trade_no + "\"");
		content.append("}");
		return content.toString();
	}

}
