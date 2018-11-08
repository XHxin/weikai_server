package com.mipush;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.parser.ParseException;

import com.xiaomi.xmpush.server.Constants;
import com.xiaomi.xmpush.server.Message;
import com.xiaomi.xmpush.server.Sender;

import cc.modules.util.PropertiesFileReader;

/**
 * 小米推送帮助类
 * 
 * @author tanxinye
 * 
 * 
 *
 */
public class MiPushHelper {

	private static final String APP_SECRET_KEY_ANDROID = "ZHcpeFcNg8Ry7/t1AVD5Aw==";
	private static final String APP_SECRET_KEY_IOS = "xwX/ZjfCSpvpgQXDea+gmw==";
	private static final String PACKAGENAME_ANDROID = "com.messcat.kaiwei";
	private static final String PACKAGENAME_IOS = "com.project.kaiwei";
	private static String domain = PropertiesFileReader.getByKey("web.domain");
	private static final int RETRIES = 3;

	/**
	 * 传送数据以URI协议 格式为 weikai://cert-map?target=? scheme : weikai host : cert-map
	 * path : target 打开指定页面
	 *
	 * ebReport为电商准入报告 示例： weikai://cert-map?target=ebReport&reportId=?
	 * 
	 * report为准入报告 示例： weikai://cert-map?target=report&productId=?&regionId=?
	 * 
	 * standardReading为准入报告 示例:
	 * weikai://cert-map?target=standardReading&standardReadingId=?
	 * 
	 * expert为专家 示例： weikai://cert-map?target=expert&expertId=?
	 * 
	 * web为网页 示例： weikai://cert-map?target=web&url=?
	 * 
	 * consult为付费咨询 示例： weikai://cert-map?target=consult&replyId=?
	 * 
	 * passThrough设置消息是否通过透传的方式送给app，1表示透传消息，0表示通知栏消息。
	 */

	/**
	 * 发送所有Android设备 通知栏
	 */
	public static void sendAllAndroidMessage(String title, String description, String ext) {
		Constants.useOfficial();
		Message message = new Message.Builder().title(title).description(description)
				.restrictedPackageName(PACKAGENAME_ANDROID).notifyType(1).extra("data", ext) // 使用默认提示音提示
				.build();
		try {
			new Sender(APP_SECRET_KEY_ANDROID).broadcastAll(message, RETRIES);
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}
	
	public static void sendAllAndroidMessage(String title, String description) {
		Constants.useOfficial();
		Message message = new Message.Builder().title(title).description(description)
				.restrictedPackageName(PACKAGENAME_ANDROID).notifyType(1) // 使用默认提示音提示
				.build();
		try {
			new Sender(APP_SECRET_KEY_ANDROID).broadcastAll(message, RETRIES);
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发送所有Android设备 透传
	 */
	public static void sendAllAndroidMessagePassThrough(String title, String description, String body) {
		Constants.useOfficial();
		Message message = new Message.Builder().title(title).description(description).passThrough(1).payload(body)
				.restrictedPackageName(PACKAGENAME_ANDROID).notifyType(1) // 使用默认提示音提示
				.build();
		try {
			new Sender(APP_SECRET_KEY_ANDROID).broadcastAll(message, RETRIES);
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据useraccount发送Android设备 通知栏
	 */
	public static void sendAndroidUserAccount(String title, String description, String ext, String useraccount) {
		Constants.useOfficial();
		Message message = new Message.Builder().title(title).description(description)
				.restrictedPackageName(PACKAGENAME_ANDROID).notifyType(1).extra("data", ext).build();
		try {
			new Sender(APP_SECRET_KEY_ANDROID).sendToUserAccount(message, useraccount, RETRIES);
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}
	
	public static void sendAndroidUserAccount(String title, String description, String useraccount) {
		Constants.useOfficial();
		Message message = new Message.Builder().title(title).description(description)
				.restrictedPackageName(PACKAGENAME_ANDROID).notifyType(1).build();
		try {
			new Sender(APP_SECRET_KEY_ANDROID).sendToUserAccount(message, useraccount, RETRIES);
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发送Android设备 系统消息
	 */
	public static void sendAndroidSysNotify(String title, String description, String useraccount) {
		if(useraccount==null){   //判断手机号是否为空
			throw new IllegalArgumentException();
		}
		Constants.useOfficial();
		Message message = new Message.Builder().title(title).description(description)
				.restrictedPackageName(PACKAGENAME_ANDROID).notifyType(1).extra("notify", "sys_msg").build();
		try {
			new Sender(APP_SECRET_KEY_ANDROID).sendToUserAccount(message, useraccount, RETRIES);
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据一组useraccount发送Android设备 通知栏
	 */
	public static void sendAndroidUserAccountList(String title, String description, String ext,
			List<String> useraccounts) {
		Constants.useOfficial();
		Message message = new Message.Builder().title(title).description(description)
				.restrictedPackageName(PACKAGENAME_ANDROID).notifyType(1).extra("data", ext) // 使用默认提示音提示
				.build();
		try {
			new Sender(APP_SECRET_KEY_ANDROID).sendToUserAccount(message, useraccounts, RETRIES);
		}catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据useraccount发送Android设备 透传
	 */
	public static void sendAndroidUserAccountPassThrough(String title, String description, String body,
			String useraccount) {
		Constants.useOfficial();
		Message message = new Message.Builder().title(title).description(description).passThrough(1).payload(body)
				.restrictedPackageName(PACKAGENAME_ANDROID).notifyType(1) // 使用默认提示音提示
				.build();
		try {
			new Sender(APP_SECRET_KEY_ANDROID).sendToUserAccount(message, useraccount, RETRIES);
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据一组useraccount发送Android设备 透传
	 */
	public static void sendAndroidUserAccountListPassThrough(String title, String description, String body,
			List<String> useraccounts) {
		Constants.useOfficial();
		Message message = new Message.Builder().title(title).description(description).passThrough(1).payload(body)
				.restrictedPackageName(PACKAGENAME_ANDROID).notifyType(1) // 使用默认提示音提示
				.build();
		try {
			new Sender(APP_SECRET_KEY_ANDROID).sendToUserAccount(message, useraccounts, RETRIES);
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发送所有IOS设备
	 * 
	 */
	public static void sendAllIOSMessage(String description, String ext) {
		// userSandbox是开发模式 发布之后要使用useOfficial
		if(domain.contains("cert-map")) {
			Constants.useOfficial();
		}else if(domain.contains("certmaptest")){
			Constants.useSandbox();
		}
		Message message = new Message.IOSBuilder().description(description).soundURL("default") // 消息铃声
				.badge(1) // 数字角标
				.category("action") // 快速回复类别
				.extra("data", ext) // 自定义键值对
				.build();
		try {
			new Sender(APP_SECRET_KEY_IOS).broadcastAll(message, RETRIES);
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据一组useraccount发送IOS设备
	 * 
	 */
	public static void sendIOSUserAccount(String description, String ext, String useraccount) {
		//  useSandbox是开发模式 发布之后要使用useOfficial
		if(domain.contains("cert-map")) {
			Constants.useOfficial();
		}else if(domain.contains("certmaptest")){
			Constants.useSandbox();
		}
		Message message = new Message.IOSBuilder().description(description).soundURL("default") // 消息铃声
				.badge(1) // 数字角标
				.category("action") // 快速回复类别
				.extra("data", ext) // 自定义键值对
				.build();
		try {
			new Sender(APP_SECRET_KEY_IOS).sendToUserAccount(message, useraccount, RETRIES);
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}
	
	public static void sendIOSUserAccount(String description,  String useraccount) {
		//  useSandbox是开发模式 发布之后要使用useOfficial
		if(domain.contains("cert-map")) {
			Constants.useOfficial();
		}else if(domain.contains("certmaptest")){
			Constants.useSandbox();
		}
		Message message = new Message.IOSBuilder().description(description).soundURL("default") // 消息铃声
				.badge(1) // 数字角标
				.category("action") // 快速回复类别
				.build();
		try {
			new Sender(APP_SECRET_KEY_IOS).sendToUserAccount(message, useraccount, RETRIES);
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}

	public static void sendIOSSysMsg(String description, String useraccount) {
		//  useSandbox是开发模式 发布之后要使用useOfficial
		if(domain.contains("cert-map")) {
			Constants.useOfficial();
		}else if(domain.contains("certmaptest")){
			Constants.useSandbox();
		}
		Message message = new Message.IOSBuilder().description(description).soundURL("default") // 消息铃声
				.badge(1) // 数字角标
				.category("action") // 快速回复类别
				.extra("notify", "sys_msg") // 自定义键值对
				.build();
		try {
			new Sender(APP_SECRET_KEY_IOS).sendToUserAccount(message, useraccount, RETRIES);
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据一组useraccount发送IOS设备

	 * 
	 */
	public static void sendIOSUserAccountList(String description, String ext, List<String> useraccount) {
		//  useSandbox是开发模式 发布之后要使用useOfficial
		if(domain.contains("cert-map")) {
			Constants.useOfficial();
		}else if(domain.contains("certmaptest")){
			Constants.useSandbox();
		}
		Message message = new Message.IOSBuilder().description(description).soundURL("default") // 消息铃声
				.badge(1) // 数字角标
				.category("action") // 快速回复类别
				.extra("data", ext) // 自定义键值对
				.build();
		try {
			new Sender(APP_SECRET_KEY_IOS).sendToUserAccount(message, useraccount, RETRIES);
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		List<String> mobileList=new ArrayList<String>();
//		mobileList.add("13073008457");
//		mobileList.add("18819218215");
		mobileList.add("13677336042");
		mobileList.add("18689505099");
//		mobileList.add("13427644456");
//		mobileList.add("13113365136");
		mobileList.add("15827625767");
		sendAndroidUserAccountList("世界认证地图", "更新世界认证地图App", "weikai://cert-map?target=web&url=http://t.cn/REi5ddX",mobileList);
		sendIOSUserAccountList("更新世界认证地图App", "weikai://cert-map?target=web&url=http://t.cn/REi5ddX", mobileList);

	}
}
