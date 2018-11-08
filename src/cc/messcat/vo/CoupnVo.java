package cc.messcat.vo;

import java.math.BigDecimal;

public class CoupnVo {

	private boolean have;
	private BigDecimal coupnMoney;
	private String useScope;
	private String endDate;
	private boolean change;//是否领取过true：是  false：否

	public BigDecimal getCoupnMoney() {
		return coupnMoney;
	}

	public void setCoupnMoney(BigDecimal coupnMoney) {
		this.coupnMoney = coupnMoney;
	}

	public String getUseScope() {
		return useScope;
	}
	public void setUseScope(String useScope) {
		this.useScope = useScope;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public boolean isHave() {
		return have;
	}
	public void setHave(boolean have) {
		this.have = have;
	}
	public boolean isChange() {
		return change;
	}
	public void setChange(boolean change) {
		this.change = change;
	}
	
	
}
