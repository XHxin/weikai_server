package cc.messcat.vo;


public class SystemMessageVo {
	
	public static final String partData = "weikai://cert-map?target=";
//	public static final String and = "&";
//	public static final String equal = "=";
	
	private String time;
	private String title;
	private String remark;
	private String cover;
	private String data;
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	
}
