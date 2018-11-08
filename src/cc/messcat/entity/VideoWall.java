package cc.messcat.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author HASEE
 * 视频上墙实体
 */
@Entity
@Table(name="live_video_wall")
public class VideoWall {

	@Id
	@GeneratedValue
	private Long id;
	@Column(name="video_id")
	private Long videoId;
	@Column(name="content")
	private String content;
	@Column(name="nick_name")
	private String nickName;
	@Column(name="photo")
	private String photo;
	@Column(name="is_usable")
	private int usable;
	@Column(name="gmt_create")
	private Date gmtCreate;
	@Column(name="gmt_modified")
	private Date gmtModified;
	@Column(name="reply_img")
	private String replyImg;
	@Column(name="reply_content")
	private String replyContent;
	@Column(name="wall_img")
	private String wallImg;
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
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
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
	public String getReplyImg() {
		return replyImg;
	}
	public void setReplyImg(String replyImg) {
		this.replyImg = replyImg;
	}
	public String getReplyContent() {
		return replyContent;
	}
	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}
	public String getWallImg() {
		return wallImg;
	}
	public void setWallImg(String wallImg) {
		this.wallImg = wallImg;
	}
	
	
}
