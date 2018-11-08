package cc.messcat.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@SuppressWarnings("serial")
@Entity
@Table(name="hot_reply_fees")
public class HotReplyFees implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name="memberID")
	private Long memberId;  //专家ID
	private BigDecimal money;	//向专家公开提问需支付的金额

	@Column(name="private_money")
	private BigDecimal privateMoney; //向专家私密提问需支付的金额
	
	
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

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public BigDecimal getPrivateMoney() {
		return privateMoney;
	}

	public void setPrivateMoney(BigDecimal privateMoney) {
		this.privateMoney = privateMoney;
	}
}
