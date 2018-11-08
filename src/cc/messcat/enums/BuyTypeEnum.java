package cc.messcat.enums;

/**
 * @author xiehuaxin
 * @createDate 2018年6月11日 上午11:30:01
 * @todo 消费类型0:购买会员 1:准入报告 2:标准解读 3:质量分享 4:付费咨询 5:新闻详情 6:电商准入报告 7:钱包充值 8:视频或直播 9:专栏订阅
 */
public enum BuyTypeEnum {

	VIP("0","会员购买"),
	CERTIFICATION_REPORT("1","准入报告"),
	STANDARD_READING("2","标准解读"),
	QUALITY_SHARE("3","质量分享"),
	PAY_CONSULT("4","付费咨询"),
	NEWS("5","新闻"),
	BUSINESS_REPORT("6","电商准入报告购买"),
	WALLET("7","钱包充值"),
	VIDEO("8","视频购买"),
	SPECIAL_COLUMN("9","专栏购买");
	
	private String type;
	
	private String name;
	
	private BuyTypeEnum(String type, String name) {
		this.type = type;
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public String getName() {
		return name;
	}
}
