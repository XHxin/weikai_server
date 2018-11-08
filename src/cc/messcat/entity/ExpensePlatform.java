package cc.messcat.entity;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

/**
 *  平台流水明细
 */
@Entity
@Table(name="expense_platform")
public class ExpensePlatform implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name="expense_total_id")
	private Long expenseTotalId;//流水记录表Id

	@Column(name="order_num")
	private String  orderNum; //订单号

	@Column(name="member_name")
	private String memberName;//会员名称

	@Column(name="member_id")
	private Long memberId;//会员id

	@Column(name="original_price")
	private BigDecimal originalPrice; //商品原价
	private BigDecimal money; //用户实际支付金额

	@Column(name="plat_income_money")
	private BigDecimal platIncomeMoney; //平台收入金额

	private String content; //内容
	private String remark; //备注

	@Column(name="add_time")
	private Date addTime;	//新增时间

	@Column(name = "platform_proportion")
	private double platformProportion;

	public ExpensePlatform() {
	}

	/**
	 * 统一流水处理
	 * @param id
	 * @param number
	 * @param realname
	 * @param memberId
	 * @param originalPrice
	 * @param money
	 * @param platIncomeMoney
	 * @param content
	 * @param remark
	 * @param addTime
	 */
	public ExpensePlatform(Long id, String number, String realname, Long memberId, BigDecimal originalPrice, BigDecimal money, BigDecimal platIncomeMoney, String content, String remark, Date addTime, double platformProportion) {
		this.expenseTotalId = id;
		this.orderNum = number;
		this.memberName = realname;
		this.memberId = memberId;
		this.originalPrice = originalPrice;
		this.money = money;
		this.platIncomeMoney = platIncomeMoney;
		this.content = content;
		this.remark = remark;
		this.addTime = addTime;
		this.platformProportion = platformProportion;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getExpenseTotalId() {
		return expenseTotalId;
	}

	public void setExpenseTotalId(Long expenseTotalId) {
		this.expenseTotalId = expenseTotalId;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public BigDecimal getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(BigDecimal originalPrice) {
		this.originalPrice = originalPrice;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public BigDecimal getPlatIncomeMoney() {
		return platIncomeMoney;
	}

	public void setPlatIncomeMoney(BigDecimal platIncomeMoney) {
		this.platIncomeMoney = platIncomeMoney;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public double getPlatformProportion() {
		return platformProportion;
	}

	public void setPlatformProportion(double platformProportion) {
		this.platformProportion = platformProportion;
	}
}