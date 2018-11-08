package cc.messcat.vo;

public class FinancialVo {
	
	private String chargeNum;//收费金额
	private String chargeTime;//收费时间
	private String chargeNotes;//收费备注
	private String chargeStatus;//收费状态，0未收，1已收
	
	public String getChargeNum() {
		return chargeNum;
	}
	public void setChargeNum(String chargeNum) {
		this.chargeNum = chargeNum;
	}
	public String getChargeTime() {
		return chargeTime;
	}
	public void setChargeTime(String chargeTime) {
		this.chargeTime = chargeTime;
	}
	public String getChargeNotes() {
		return chargeNotes;
	}
	public void setChargeNotes(String chargeNotes) {
		this.chargeNotes = chargeNotes;
	}
	public String getChargeStatus() {
		return chargeStatus;
	}
	public void setChargeStatus(String chargeStatus) {
		this.chargeStatus = chargeStatus;
	}

}
