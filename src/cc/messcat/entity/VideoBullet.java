package cc.messcat.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author HASEE
 * 弹幕实体
 */
@Entity
@Table(name="live_video_bullet")
public class VideoBullet {

	@Id
	@GeneratedValue
	private Long id;
	@Column(name="video_id")
	private Long videoId;
	@Column(name="content")
	private String content;
	@Column(name="member_id")
	private Long memberId;
	@Column(name="is_usable")
	private int usable;
	@Column(name="gmt_create")
	private Date gmtCreate;
	@Column(name="gmt_modified")
	private Date gmtModified;
	@Column(name="bullet_id")
	private String bulletId;
	@Column(name="silence_status")
	private int silenceStatus;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getVideoId() {
		return videoId;
	}
	public void setVideoId(Long videoId) {
		this.videoId = videoId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Long getMemberId() {
		return memberId;
	}
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	public int getUsable() {
		return usable;
	}
	public void setUsable(int usable) {
		this.usable = usable;
	}
	public Date getGmtCreate() {
		return gmtCreate;
	}
	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}
	public Date getGmtModified() {
		return gmtModified;
	}
	public void setGmtModified(Date gmtModified) {
		this.gmtModified = gmtModified;
	}
	public String getBulletId() {
		return bulletId;
	}
	public void setBulletId(String bulletId) {
		this.bulletId = bulletId;
	}
	public int getSilenceStatus() {
		return silenceStatus;
	}
	public void setSilenceStatus(int silenceStatus) {
		this.silenceStatus = silenceStatus;
	}
	
	
}
