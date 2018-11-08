package cc.messcat.vo;

/**
 * 注册返回、提交专家信息返回、
 * @author May E-mail:mayyanmm@qq.com
 * @version Create_Time:2017年6月28日 上午11:35:40
 */

public class MemberInfoResult {
	
	private String result;
	private String code;
	private MemberVo member;
	
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public MemberVo getMember() {
		return member;
	}
	public void setMember(MemberVo member) {
		this.member = member;
	}

}
