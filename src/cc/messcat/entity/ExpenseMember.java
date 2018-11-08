package cc.messcat.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author leo
 * @date 2018/7/4 15:20
 */
@Entity
@Table(name="expense_member")
public class ExpenseMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="member_id")
    private Long memberId;

    @Column(name="expense_total_id")
    private Long ExpenseTotalId;

    @Column(name="order_num")
    private String orderNum;

    @Column(name="original_price")
    private BigDecimal originalPrice;

    private BigDecimal money;

    @Column(name="expense_member_money")
    private BigDecimal expenseMemberMoney;

    @Column(name="add_time")
    private Date addTime;

    /**
     * 统一流水处理，提问者分成
     * @param memberId
     * @param ExpenseTotalId
     * @param number
     * @param originalPrice
     * @param money
     * @param expenseMemberMoney
     * @param addTime
     */
    public ExpenseMember(Long memberId, Long ExpenseTotalId, String number, BigDecimal originalPrice, BigDecimal money, BigDecimal expenseMemberMoney, Date addTime) {
        this.memberId = memberId;
        this.ExpenseTotalId = ExpenseTotalId;
        this.orderNum = number;
        this.originalPrice = originalPrice;
        this.money = money;
        this.expenseMemberMoney = expenseMemberMoney;
        this.addTime = addTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
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

    public BigDecimal getExpenseMemberMoney() {
        return expenseMemberMoney;
    }

    public void setExpenseMemberMoney(BigDecimal expenseMemberMoney) {
        this.expenseMemberMoney = expenseMemberMoney;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }
}
