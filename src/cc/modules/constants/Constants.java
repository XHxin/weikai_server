package cc.modules.constants;

import cc.modules.util.PropertiesFileReader;

import java.io.File;

public class Constants {

	public static final String ENCODING = "UTF-8";

	// 公用返回码
	public static final String SUCCESS_CODE_200 = "200";
	public static final String SUCCESS_CODE_300 = "300";
	public static final String FAIL_CODE_400 = "400";
	public static final String EXCEPTION_CODE_500 = "500";
	public static final String EXCEPTION_CODE_600 = "600"; // session过期

	// 通用提示信息
	public static final String MSG_SUCCESS = "操作成功";
	public static final String MSG_FAIL = "操作失败";
	public static final String MSG_EXCEPTION = "操作异常";
	public static final String MSG_EXCEPTION_POPUP = "系统错误，请稍后再试";
	public static final String MSG_NO_LOGIN = "请先登录";
	public static final String MSG_LOGIN_NAME_EMPTY = "登录名为空";
	public static final String MSG_EXECUTE_SUCCESS = "执行成功";
	public static final String MSG_FIND_SUCCESS = "查询成功";
	public static final String MSG_FIND_FAIL = "查询失败";
	public static final String MSG_ADD_SUCCESS = "添加成功";
	public static final String MSG_ADD_FAIL = "添加失败";
	public static final String MSG_UPDATE_SUCCESS = "修改成功";
	public static final String MSG_UPDATE_FAIL = "修改失败";
	public static final String MSG_DELETE_SUCCESS = "删除成功";
	public static final String MSG_DELETE_FAIL = "删除失败";
	public static final String MSG_DATA_EXCEPT = "数据异常";
	public static final String MSG_PARAMETER_EMPTY = "参数为空";
	public static final String MSG_INPUT_NOEMPTY = "输入不能为空";
	public static final String MSG_FIND_NULL = "查询到的数据为空";
	public static final String MSG_EXCHANGE_EXCEED = "超过允许兑换次数";
	public static final String MSG_EXCHANGE_FAIL = "兑换失败";
	public static final String MSG_COUPN_NOTUSE = "卡券不可用";
	public static final String MSG_PAYCONSULT_FAIL = "无法向本人提问";
	public static final String MSG_REPETITIVE_BUY = "您已购买该商品，无需重复购买";
	public static final String MSG_NO_QUALIFICATION = "你未满足兑换资格";
	public static final String MSG_COMMODITY_NOT_FREE = "该商品非免单商品";
	public static final String MSG_DATA_EXIST = "您已拥有该课程";
	public static final String MSG_MOBILE_USED = "该手机号码已绑定其他微信号,请更换其他手机号";
	public static final String SUCCESS = "success";
	/**
	 * 微信appId
	 */
	public static final String APPID = PropertiesFileReader.getByKey("wechat.appId").trim();
	/**
	 * 微信密钥
	 */
	public static final String APPSECRET = PropertiesFileReader.getByKey("wechat.appsecret").trim();
	/**
	 * 微信链接服务器域名
	 */
	public static String WEBSITE_URL = PropertiesFileReader.getByKey("wechat.websiteUrl").trim();
	/**
	 * 背景图X轴坐标
	 */
	public static int BACKGROUND_IMG_X = 0;
	 /**
	 * 背景图Y轴坐标
	 */
	public static int BACKGROUND_IMG_Y = 0;
	/**
	 * 微信二维码图片高
	 */
	public static int WX_QR_IMG_HEIGHT = 200;
	/**
	 * 微信二维码图片X轴坐标
	 */
	public static int WX_QR_IMG_X = 0;
	/**
	 * 微信二维码图片Y轴坐标
	 */
	public static int WX_QR_IMG_Y = 0;
	
	/**
	 * 微信二维码图片宽
	 */
	public static int WX_QR_IMG_WIDTH = 200;
	/**
	 * 生成二维码图片路径
	 */
	public static String QR_TEMP_PATH= "D://wwwroot/8082/yingxiao/upload/enterprice/member_qrcode/";  //服务器路径
	/**
	 * 状态 启用
	 */
	public static final String ENABLED = "1";
	/**
	 * 状态 停用
	 */
	public static final String DISABLED = "0";
	/**
	 * 请求成功
	 */
	public static final String REQ_RESULT_SUCCESS = "ok";

	// 短信业务信息--business
	public static final String BUSINESS_REGIST = "regist";// 注册
	public static final String BUSINESS_LOGIN = "login";// 登录
	public static final String BUSINESS_CHRISTMAS = "christmas";//参加圣诞节活动

	// 短信和logintoken的信息与时间分隔符
	public static final String CODE_LOGINTOKEN = ",";
	public static final String CODE_LIMIT_TIME = "15";// 分钟

	// token校验
	public static final String UM_LOGIN_VERIFY_TOKEN = "你还没登录，请先登录";
	public static final String LOGIN_VERIFY_TOKEN_ERROR = "你的帐号在别处登录，请重新登录";
	public static final String LOGIN_TIME_TOKEN_ERROR = "登录超时，请重新登录";
	/**
	 * 校验手机号码的正则
	 */
	public static final String PHONE_NUMBER_REG = "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$";

	/**
	 * 用于存放支付宝对账文件的文件夹路径
	 */
	public static final String SAVE_BILL_PATH = File.separator + "home" + File.separator + "data" + File.separator + "aliBillDir";
//	public static final String SAVE_BILL_PATH = "D:" + File.separator + "billDir";

	/**
	 * 加压支付宝对账文件的存放路径
	 */
	public static final String UN_COMPRESS_DIR = File.separator + "home" + File.separator + "data" + File.separator + "aliBillDir" + File.separator;
//	public static final String UN_COMPRESS_DIR = "D:" + File.separator + "billDir"+ File.separator;
}
