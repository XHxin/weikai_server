package cc.messcat.vo;

import cc.messcat.entity.Coupn;

public class UserCoupnVo {
	private Long id;
	private Long memberId;
	private Coupn coupnId;
	/**0代表未使用  1代表已使用  2代表已过期*/
	private String usedStatus;
	private String validityDate;
	
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
	public Coupn getCoupnId() {
		return coupnId;
	}
	public void setCoupnId(Coupn coupnId) {
		this.coupnId = coupnId;
	}

	public String getUsedStatus() {
		return usedStatus;
	}
	public void setUsedStatus(String usedStatus) {
		this.usedStatus = usedStatus;
	}
	public String getValidityDate() {
		return validityDate;
	}
	public void setValidityDate(String validityDate) {
		this.validityDate = validityDate;
	}

	
	
	
}
