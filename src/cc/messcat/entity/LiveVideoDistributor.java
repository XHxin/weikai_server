package cc.messcat.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author xiehuaxin
 * @createDate 2018年5月26日 下午2:35:21
 * @todo TODO
 */
@Entity
@Table(name="live_video_distributor")
public class LiveVideoDistributor {

	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name="vid")
	private Long videoId;
	
	@Column(name="mid")
	private Long memberId;
	
	@Column(name="bring_num")
	private Integer bringNum;
	
	@Column(name="bought_num")
	private Integer boughtNum;
	
	@Column(name="integral")
	private Integer integral;
	
	@Column(name="status")
	private Integer status;
	
	@Column(name="create_at")
	private Date createAt;

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

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public Integer getBringNum() {
		return bringNum;
	}

	public void setBringNum(Integer bringNum) {
		this.bringNum = bringNum;
	}

	public Integer getBoughtNum() {
		return boughtNum;
	}

	public void setBoughtNum(Integer boughtNum) {
		this.boughtNum = boughtNum;
	}

	public Integer getIntegral() {
		return integral;
	}

	public void setIntegral(Integer integral) {
		this.integral = integral;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}
	
	
}
