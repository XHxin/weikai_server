package cc.messcat.vo;

import java.util.Date;

public class WechatVo {

	private Date ref_date;
	private int cumulate_user;
	private int user_source;
	public Date getRef_date() {
		return ref_date;
	}
	public void setRef_date(Date ref_date) {
		this.ref_date = ref_date;
	}
	public int getCumulate_user() {
		return cumulate_user;
	}
	public void setCumulate_user(int cumulate_user) {
		this.cumulate_user = cumulate_user;
	}
	public int getUser_source() {
		return user_source;
	}
	public void setUser_source(int user_source) {
		this.user_source = user_source;
	}
	
}
