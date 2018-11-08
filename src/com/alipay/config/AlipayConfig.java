package com.alipay.config;

import cc.modules.util.PropertiesFileReader;

/*
 * * 类名：AlipayConfig 功能：基础配置类 详细：设置帐户有关信息及返回路径 版本：3.4 修改日期：2016-03-08 说明：
 * 以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 * 该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayConfig {

	// ↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

	// 支付宝分配给开发者的应用ID
	public static String appid = PropertiesFileReader.getByKey("alipay.appid");;

	// 该笔订单允许的最晚付款时间，逾期将关闭交易
	public static String timeout_express = "30m";

	// 销售产品码，商家和支付宝签约的产品码
	public static String product_code = "QUICK_MSECURITY_PAY";

	// APP支付接口
	public static String app_pay_url = "alipay.trade.app.pay";

	// APP支付接口1.0
	public static String service_pay = "mobile.securitypay.pay";

	// APP支付回调通知url
//	public static String app_pay_notify_url = "http://test13.messcat.com/sshang_app/payAction!returnAppNotify.do";

	public static String app_pay_notify_url_vip= PropertiesFileReader.getByKey("alipay.notify.vip.url");
	
//	支付宝充值钱包回调
	public static String app_pay_notify_url_wallet= PropertiesFileReader.getByKey("alipay.notify.wallet.url");
//	1准入报告购买接口开发 (支付宝，微信)
	public static String app_pay_notify_url= PropertiesFileReader.getByKey("alipay.notify.url");
	
//	2标准解读连载购买接口开发 (支付宝，微信)
	public static String app_pay_notify_url_interpretserial=PropertiesFileReader.getByKey("alipay.notify.url.interpretserial");
	
//	3单个标准解读购买接口开发 (支付宝，微信)
	public static String app_pay_notify_url_singlestdinterpret= PropertiesFileReader.getByKey("alipay.notify.url.singlestdinterpret");
	
//	4质量分享详情页购买接口开发(支付宝，微信)
	public static String app_pay_notify_url_qualitysharenotify= PropertiesFileReader.getByKey("alipay.notify.url.qualitysharenotify");
	
//	5电商准入报告详情购买接口开发(支付宝，微信)
	public static String app_pay_notify_url_shopaccessreport= PropertiesFileReader.getByKey("alipay.notify.url.shopaccessreport");
	
//	6支付宝充值钱包
	public static String app_pay_notify_url_topupalipay= PropertiesFileReader.getByKey("alipay.notity.url.topupalipay");
		
	
	// -------------------------------------以下是基础定义配置------------------------------------------

	// 合作身份者ID，签约账号，以2088开头由16位纯数字组成的字符串，查看地址：https://b.alipay.com/order/pidAndKey.htm
	public static String partner = PropertiesFileReader.getByKey("alipay.partner");

	// 收款支付宝账号，以2088开头由16位纯数字组成的字符串，一般情况下收款账号就是签约账号
	public static String seller_id = PropertiesFileReader.getByKey("alipay.seller.id");

	// 商户的私钥,需要PKCS8格式，RSA公私钥生成：https://doc.open.alipay.com/doc2/detail.htm?spm=a219a.7629140.0.0.nBDxfy&treeId=58&articleId=103242&docType=1
	public static String private_key =PropertiesFileReader.getByKey("alipay.key.private");

	// 支付宝的公钥,查看地址：https://b.alipay.com/order/pidAndKey.htm
	public static String alipay_public_key = PropertiesFileReader.getByKey("alipay.key.public");

	// 服务器异步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String notify_url = "http://商户网址/create_direct_pay_by_user-JAVA-UTF-8/notify_url.jsp";

	// 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String return_url = "http://商户网址/create_direct_pay_by_user-JAVA-UTF-8/return_url.jsp";

	// 签名方式
	public static String sign_type = "RSA2";

	// 调试用，创建TXT日志文件夹路径，见AlipayCore.java类中的logResult(String sWord)打印方法。
	public static String log_path = "C:\\";

	// 字符编码格式 目前支持 gbk 或 utf-8
	public static String input_charset = "utf-8";

	// 支付类型 ，无需修改
	public static String payment_type = "1";

	// 调用的接口名，无需修改
	public static String service = "create_direct_pay_by_user";

	// ↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

	// ↓↓↓↓↓↓↓↓↓↓ 请在这里配置防钓鱼信息，如果没开通防钓鱼功能，为空即可 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

	// 防钓鱼时间戳 若要使用请调用类文件submit中的query_timestamp函数
	public static String anti_phishing_key = "";

	// 客户端的IP地址 非局域网的外网IP地址，如：221.0.0.1
	public static String exter_invoke_ip = "";



	// ↑↑↑↑↑↑↑↑↑↑请在这里配置防钓鱼信息，如果没开通防钓鱼功能，为空即可 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

}
