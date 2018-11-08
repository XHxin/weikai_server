package cc.messcat.vo;

public class IOSVerifyVo {

	private String orderNum;  //订单号
	
	private Long memberId;    //用户ID
	
	private String receipt;   //证书
	
	private String type;   //地址类型：Sandbox沙盒测试，null正式url

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public String getReceipt() {
		return receipt;
	}

	public void setReceipt(String receipt) {
		this.receipt = receipt;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
}
