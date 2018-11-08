package cc.messcat.entity;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

/**
 * 专家流水明细
 */
@Entity
@Table(name="expense_expert")
public class ExpenseExpert implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name="expert_id")
	private Long expertId;//专家id

	@Column(name="expense_total_id")
	private Long ExpenseTotalId;  //流水记录表Id

	@Column(name= "order_num")
	private String orderNum; //订单号

	private String operate;  //操作(0：收入;  1：提现)

	@Column(name="original_price")
	private BigDecimal originalPrice; //商品原价
	private BigDecimal money; //交易金额(收入或提现)

	@Column(name="expense_expert_money")
	private BigDecimal expenseExpertMoney; //专家收入金额

	private String content;  	//内容
	private String remark;    //备注

	@Column(name="add_time")
	private Date addTime;

	@Column(name = "expert_proportion")
	private double expertProportion;

	public ExpenseExpert() {
	}

	/**
	 * 统一流水处理
	 * @param expertId
	 * @param expenseTotalId
	 * @param number
	 * @param operate
	 * @param originalPrice
	 * @param money
	 * @param content
	 * @param remark
	 * @param addTime
	 */
	public ExpenseExpert(Long expertId, Long expenseTotalId, String number, String operate, BigDecimal originalPrice, BigDecimal money,BigDecimal expenseExpertMoney, String content, String remark, Date addTime, double expertProportion) {
		this.expertId = expertId;
		this.ExpenseTotalId = expenseTotalId;
		this.orderNum = number;
		this.operate = operate;
		this.originalPrice = originalPrice;
		this.money = money;
		this.expenseExpertMoney = expenseExpertMoney;
		this.content = content;
		this.remark = remark;
		this.addTime = addTime;
		this.expertProportion = expertProportion;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getExpertId() {
		return expertId;
	}

	public void setExpertId(Long expertId) {
		this.expertId = expertId;
	}

	public Long getExpenseTotalId() {
		return ExpenseTotalId;
	}

	public void setExpenseTotalId(Long expenseTotalId) {
		this.ExpenseTotalId = expenseTotalId;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public String getOperate() {
		return operate;
	}

	public void setOperate(String operate) {
		this.operate = operate;
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

	public BigDecimal getExpenseExpertMoney() {
		return expenseExpertMoney;
	}

	public void setExpenseExpertMoney(BigDecimal expenseExpertMoney) {
		this.expenseExpertMoney = expenseExpertMoney;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public double getExpertProportion() {
		return expertProportion;
	}

	public void setExpertProportion(double expertProportion) {
		this.expertProportion = expertProportion;
	}
}