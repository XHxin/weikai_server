package cc.messcat.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author nelson
 * 新的购买记录实体（优化数据库，拓展钱包功能）
 */
@Entity
@Table(name="expense_total")
public class ExpenseTotal {
	//消费类型：0：会员套餐 1：准入报告 2：标准解读 3：质量分享 4：付费咨询  5:新闻详情  6：电商信息   7:钱包充值   8:视频或直播  9:专栏订阅  10:打赏
	public static final int type0 = 0;
	public static final int type1 = 1;
	public static final int type2 = 2;
	public static final int type3 = 3;
	public static final int type4 = 4;
	public static final int type5 = 5;
	public static final int type6 = 6;
	public static final int type7 = 7;
	public static final int type8 = 8;
	public static final int type9 = 9;
	public static final int type10 = 10;
	public static Map<String, String> typeMap=new LinkedHashMap<String, String>();
	static{
		typeMap.put(""+type0, "购买会员套餐");
		typeMap.put(""+type1, "购买市场报告");
		typeMap.put(""+type2, "购买标准解读");
		typeMap.put(""+type3, "购买质量分享");
		typeMap.put(""+type4, "购买付费咨询");
		typeMap.put(""+type5, "购买新闻");
		typeMap.put(""+type6, "购买电商报告");
		typeMap.put(""+type7, "钱包充值成功");
		typeMap.put(""+type8, "购买视频或直播");
		typeMap.put(""+type9, "购买专栏订阅");
		typeMap.put(""+type10, "打赏");
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name="memberID")
	private Long memberId; //地区ID
	private String type;//消费类型：0：会员套餐 1：准入报告 2：标准解读 3：质量分享 4：付费咨询  5:新闻详情  6：电商信息   7:钱包充值   8:视频或直播  9:专栏订阅
	private String content;//消费具体内容（用于前台显示）

	@Column(name="relatedID")
	private Long relatedId;

	@Column(name="regionID")
	private Long regionId; //产品ID

	private BigDecimal originalPrice;//原价

	@Column(name="coupn_money")
	private BigDecimal coupnMoney;  //卡券金额
	/**消费金额  , 实付金额*/
	private BigDecimal money;

	@Column(name="pay_status")
	private String payStatus;//付款状态（0：未付款 1：已付款 2:退款）

	@Column(name="add_time")
	private Date addTime;

	@Column(name="pay_time")
	private Date payTime;//付款时间

	@Column(name="order_num")
	private String number;//订单号

	@Column(name="pay_type")
	private String payType;//付款类型(-1:福利赠送 1:微信 2:支付宝 3:苹果内购 4:钱包 5:兑换码)

	@Column(name="expert_id")
	private Long expertId;    //回答问题的专家ID

	@Column(name="youzan_relatedId")
	private Long youzanRelatedID;

	@Column(name = "divide_scale_id")
	private Integer divideScaleId;

	@Column(name = "version")
	private int version;

	
	public ExpenseTotal() {
		super();
	}
	
	/*
        没有传卡券金额
	 */
	public ExpenseTotal(String type, String content, Long memberId, String number, BigDecimal money, BigDecimal originalPrice, String payType,
						Long relatedId) {
		super();
		this.type = type;
		this.content = content;
		this.memberId = memberId;
		this.number = number;
		this.money = money;
		this.originalPrice = originalPrice;
		this.payType = payType;
		this.relatedId = relatedId;
		this.payStatus = "1";
		init();
	}

	/*
	   有传卡券金额
	 */
	public ExpenseTotal(String type, String content, Long memberId, String number, BigDecimal money,BigDecimal couponMoney, BigDecimal originalPrice, String payType,
						Long relatedId,Long expertId) {
		super();
		this.type = type;
		this.content = content;
		this.memberId = memberId;
		this.number = number;
		this.money = money;
		this.coupnMoney=couponMoney;
		this.originalPrice = originalPrice;
		this.payType = payType;
		this.relatedId = relatedId;
		this.payStatus = "1";
		this.addTime = new Date();
		this.payTime = new Date();
		this.regionId=0L;
		this.expertId=expertId;
	}


	/**
	 *  支付状态为0的构造函数
	 */
	public ExpenseTotal(String type, String content, Long memberId, String number, BigDecimal money,
						BigDecimal originalPrice, String payType) {
		this.type = type;
		this.content = content;
		this.memberId = memberId;
		this.number = number;
		this.money = money;
		this.originalPrice = originalPrice;
		this.payStatus = "0";
		this.payType = payType;
		this.relatedId = 0L;
		this.divideScaleId = 0;
		init();
	}

	/*
	    初始化方法
	 */
	private void init(){
		this.addTime = new Date();
		this.payTime = new Date();
		this.coupnMoney = new BigDecimal(0.0);
		this.regionId=0L;
		this.expertId=0L;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public Long getRegionId() {
		return regionId;
	}

	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}
	public Date getPayTime() {
		return payTime;
	}
	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public Long getRelatedId() {
		return relatedId;
	}
	public void setRelatedId(Long relatedId) {
		this.relatedId = relatedId;
	}

	public BigDecimal getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(BigDecimal originalPrice) {
		this.originalPrice = originalPrice;
	}

	public BigDecimal getCoupnMoney() {
		return coupnMoney;
	}

	public void setCoupnMoney(BigDecimal coupnMoney) {
		this.coupnMoney = coupnMoney;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public Long getYouzanRelatedID() {
		return youzanRelatedID;
	}

	public void setYouzanRelatedID(Long youzanRelatedID) {
		this.youzanRelatedID = youzanRelatedID;
	}

	public Long getExpertId() {
		return expertId;
	}

	public void setExpertId(Long expertId) {
		this.expertId = expertId;
	}

	public Integer getDivideScaleId() {
		return divideScaleId;
	}

	public void setDivideScaleId(Integer divideScaleId) {
		this.divideScaleId = divideScaleId;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
}
