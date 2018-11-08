package cc.messcat.vo;

public class GetMobileCodeResult {
	
	private String result;
	private String code;
	private MobileCodeRecordVo mobileCodeRecordVo;
	
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public MobileCodeRecordVo getMobileCodeRecordVo() {
		return mobileCodeRecordVo;
	}
	public void setMobileCodeRecordVo(MobileCodeRecordVo mobileCodeRecordVo) {
		this.mobileCodeRecordVo = mobileCodeRecordVo;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

}
