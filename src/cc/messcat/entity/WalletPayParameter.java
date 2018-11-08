package cc.messcat.entity;

public class WalletPayParameter {

	private String accessToken;
	private Long memberId;
	private String stype;
	private String relateIdStr;
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public Long getMemberId() {
		return memberId;
	}
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	public String getStype() {
		return stype;
	}
	public void setStype(String stype) {
		this.stype = stype;
	}
	public String getRelateIdStr() {
		return relateIdStr;
	}
	public void setRelateIdStr(String relateIdStr) {
		this.relateIdStr = relateIdStr;
	}

	
}
