package cc.messcat.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author HASEE
 *把微信公众号openId与手机号码关联起来
 */
@Entity
@Table(name="relevance_wechat")
public class RelevanceWechat {
	@Id
	@GeneratedValue
	private Long id;
	@Column(name="open_id")
	private String openId;
	@Column(name="mobile")
	private String mobile;
	@Column(name="video_id")
	private int videoId;
	@Column(name="from_id")
	private int fromId;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public int getVideoId() {
		return videoId;
	}
	public void setVideoId(int videoId) {
		this.videoId = videoId;
	}
	public int getFromId() {
		return fromId;
	}
	public void setFromId(int fromId) {
		this.fromId = fromId;
	}
	
	
}
