package cc.messcat.vo;

/**
 * @author Nelson
 *
 */

/*
 * 
 * 专家收益列表
 */
public class ExpertIEVo {
	private String title;
	private String time;
	private String money;// "-100或+100"
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	
	
}
