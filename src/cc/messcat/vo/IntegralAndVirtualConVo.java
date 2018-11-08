package cc.messcat.vo;

import java.math.BigDecimal;

public class IntegralAndVirtualConVo {

	private BigDecimal integer;
	private BigDecimal virtualCon;
	private BigDecimal balance;
	
	public IntegralAndVirtualConVo(Double virtualCon) {
		this.virtualCon = new BigDecimal(0);
	}
	public IntegralAndVirtualConVo(BigDecimal integer, BigDecimal virtualCon, BigDecimal balance) {
		super();
		this.integer = integer;
		this.virtualCon = virtualCon;
		this.balance = balance;
	}
	public BigDecimal getInteger() {
		return integer;
	}
	public void setInteger(BigDecimal integer) {
		this.integer = integer;
	}

	public BigDecimal getVirtualCon() {
		return virtualCon;
	}

	public void setVirtualCon(BigDecimal virtualCon) {
		this.virtualCon = virtualCon;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
}
