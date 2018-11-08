package cc.messcat.vo;


public class EBusinessInfoVo {

	private String title;					 //列表显示名称
	private Long ebusinessProductId;		//产品统称ID

	
	public Long getEbusinessProductId() {
		return ebusinessProductId;
	}
	public void setEbusinessProductId(Long ebusinessProductId) {
		this.ebusinessProductId = ebusinessProductId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
}
