package cc.messcat.vo;

public class RedeemCodeVo {
	
	/**
	 * 兑换码Vo
	 */
	private String productName;   //兑换的产品名称
	private Long relatedId;     //产品关联Id
	private String uri;     //跳转地址
	
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public Long getRelatedId() {
		return relatedId;
	}
	public void setRelatedId(Long relatedId) {
		this.relatedId = relatedId;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
}
