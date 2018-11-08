package cc.messcat.entity;

import java.sql.Timestamp;

import cc.messcat.wechat.weixin.popular.bean.Ticket;

public class WechatSignature {

	private Long timestamp;//时间磋
	private String noncestr;//随机字符串
	private String signature;//签名
	private String appid;
//	private String ticket;
	
	
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	public String getNoncestr() {
		return noncestr;
	}
	public void setNoncestr(String noncestr) {
		this.noncestr = noncestr;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	/*public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}*/
	
	
	
}
