package cc.messcat.enums;

/**
 * @author xiehuaxin
 * @createDate 2018年6月11日 下午1:28:30
 * @todo 注册来源
 */
public enum RegistSourceEnum {
	WECHAT("0"),//微信注册
	APP("1"),//app正常注册
	ACTIVITY("3"),//活动注册
	TOURIST("4");//游客注册
	
	private String type;
	
	
	public String getType() {
		return type;
	}

	private RegistSourceEnum(String type) {
		this.type = type;
	}
	
}
