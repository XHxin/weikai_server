package cc.messcat.enums;

/**
 * 支付方式
 * @ClassName: PayTypeEnum 
 * @Description: TODO
 * @author StevenWang
 * @date 2016-11-17 下午05:02:58
 */
public enum PayTypeEnum {
	
	ALIPAY("2","支付宝"),
	WECHAT("1","微信"),
	APPLE("3","苹果内购"),
	WALLET("4","钱包"),
	CHANGE_CODE("5","兑换码");
    
    private String key;
    private String name;
     
    private PayTypeEnum(String key, String name){
        this.key = key;
        this.name = name;
    }

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
     
}
