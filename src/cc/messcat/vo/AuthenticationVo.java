package cc.messcat.vo;

public class AuthenticationVo {
	
	/*
	 * 认证要求
	 */ 
	private String name;   //认证名称
	private String request;  //认证要求
	private AuthSignVo sign;    //认证标志
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRequest() {
		return request;
	}
	public void setRequest(String request) {
		this.request = request;
	}
	public AuthSignVo getSign() {
		return sign;
	}
	public void setSign(AuthSignVo sign) {
		this.sign = sign;
	}
	
}
