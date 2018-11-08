package cc.messcat.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author leo
 * @date 2018/8/10 11:23
 */
@Entity
@Table(name="integral_exchange")
public class IntegralExchange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="memberid")
    private Long memberId;  //会员ID

    @Column(name="old_integral")
    private BigDecimal oldIntegral; //原积分

    @Column(name="exchange_integral")
    private BigDecimal exchangeIntegral; //兑换积分数量

    @Column(name="new_integral")
    private BigDecimal newIntegral; //兑换后积分

    @Column(name="old_balance")
    private BigDecimal oldBalance;  //原钱包余额

    @Column(name="exchange_balance")
    private BigDecimal exchangeBalance; //兑换金额

    @Column(name="new_balance")
    private BigDecimal newBalance;  //兑换后余额

    @Column(name="exchange_time")
    private Date exchangeTime;  //兑换时间

    public IntegralExchange() {
    }

    public IntegralExchange(Long memberId, BigDecimal oldIntegral, BigDecimal exchangeIntegral, BigDecimal newIntegral, BigDecimal oldBalance, BigDecimal exchangeBalance, BigDecimal newBalance) {
        this.memberId = memberId;
        this.oldIntegral = oldIntegral;
        this.exchangeIntegral = exchangeIntegral;
        this.newIntegral = newIntegral;
        this.oldBalance = oldBalance;
        this.exchangeBalance = exchangeBalance;
        this.newBalance = newBalance;
        this.exchangeTime = new Date();
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

    public BigDecimal getOldIntegral() {
        return oldIntegral;
    }

    public void setOldIntegral(BigDecimal oldIntegral) {
        this.oldIntegral = oldIntegral;
    }

    public BigDecimal getExchangeIntegral() {
        return exchangeIntegral;
    }

    public void setExchangeIntegral(BigDecimal exchangeIntegral) {
        this.exchangeIntegral = exchangeIntegral;
    }

    public BigDecimal getNewIntegral() {
        return newIntegral;
    }

    public void setNewIntegral(BigDecimal newIntegral) {
        this.newIntegral = newIntegral;
    }

    public BigDecimal getOldBalance() {
        return oldBalance;
    }

    public void setOldBalance(BigDecimal oldBalance) {
        this.oldBalance = oldBalance;
    }

    public BigDecimal getExchangeBalance() {
        return exchangeBalance;
    }

    public void setExchangeBalance(BigDecimal exchangeBalance) {
        this.exchangeBalance = exchangeBalance;
    }

    public BigDecimal getNewBalance() {
        return newBalance;
    }

    public void setNewBalance(BigDecimal newBalance) {
        this.newBalance = newBalance;
    }

    public Date getExchangeTime() {
        return exchangeTime;
    }

    public void setExchangeTime(Date exchangeTime) {
        this.exchangeTime = exchangeTime;
    }
}
