package cc.modules.mipush;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.parser.ParseException;

import com.xiaomi.xmpush.server.Constants;
import com.xiaomi.xmpush.server.Message;
import com.xiaomi.xmpush.server.Sender;

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
		Constants.useOfficial();
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
		//  userSandbox是开发模式 发布之后要使用useOfficial
		Constants.useOfficial();
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

	public static void sendIOSSysMsg(String description, String useraccount) {
		//  userSandbox是开发模式 发布之后要使用useOfficial
		Constants.useOfficial();
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
		//  userSandbox是开发模式 发布之后要使用useOfficial
		Constants.useOfficial();
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
		// sendAndroidUserAccount("测试 ", "无法描述",
		// ExtraFactory.openWeb(Target.ANDROID, "http://www.baidu.com"),
		// "18819218215");
		//sendAndroidUserAccount("String title", "String description", "weikai://cert-map?target=web&url=http://sj.qq.com/myapp/detail.htm?apkName=com.messcat.kaiwei", "18819213121");//weikai://cert-map?target=standardReading&standardReadingId=229
		
		//sendAllAndroidMessage("世界认证地图更新", "为了您有更好地APP体验，请点击此条消息到腾讯应用宝下载最新的版本", "weikai://cert-map?target=web&url=http://sj.qq.com/myapp/detail.htm?apkName=com.messcat.kaiwei");
		//sendAndroidSysNotify("应用宝地址", "http://sj.qq.com/myapp/detail.htm?apkName=com.messcat.kaiwei", "13677336042");
		// sendAllAndroidMessage("威凯特种部队", "无法描述");
		 sendIOSUserAccount("今晚加班\njiaban", "","18689505099");
		
//		sendAndroidUserAccount("视频推荐", "视频推荐", "weikai://cert-map?target=course&id=1&type=2", "13677336042");
		//sendIOSUserAccount("666666","888888","");
		//sendIOSSysMsg(description, useraccount);
//	    sendIOSUserAccount("realname", "今晚加班", "18689505099");
		 
//		 sendAndroidSysNotify("111", "111", "13677336042");
		 
		 
		List<String> mobileList=new ArrayList<>();
		mobileList.add("13427644456");
		 
//		sendAndroidUserAccountList("测试信息", "111", "weikai://cert-map?target=course&id=1&type=2", mobileList); // 给多个用户发送消息
		sendIOSUserAccountList("直播课程《热电偶法温升试验与能力验证统计方法》福利资料全额卡券已送达您的账户，请至“我的”-“卡券”查看哟！", "1", mobileList);
	}
}
