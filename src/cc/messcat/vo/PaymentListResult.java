package cc.messcat.vo;

import java.util.List;

public class PaymentListResult {
	
	private String result;
	private String code;
	private List<PaymentVo> paymentList;
	
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
	public List<PaymentVo> getPaymentList() {
		return paymentList;
	}
	public void setPaymentList(List<PaymentVo> paymentList) {
		this.paymentList = paymentList;
	}

}
