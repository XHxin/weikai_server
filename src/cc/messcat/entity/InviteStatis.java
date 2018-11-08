package cc.messcat.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="invite_statis")
public class InviteStatis {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="from_type")
	private Integer fromType;
	
	@Column(name="app_from_id")
	private Long appFromId; 
	
	@Column(name="mp_from_id")
	private String mpFromId;

	@Column(name="to_openid")
	private String toOpenid;
	
	@Column(name="create_at")
	private Date createAt;
	
	@Column(name="related_id")
	private Long relatedId;

	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getAppFromId() {
		return appFromId;
	}
	public void setAppFromId(Long fromId) {
		this.appFromId = fromId;
	}
	public String getToOpenid() {
		return toOpenid;
	}
	public void setToOpenid(String toOpenid) {
		this.toOpenid = toOpenid;
	}
	public Integer getFromType() {
		return fromType;
	}
	public void setFromType(Integer fromType) {
		this.fromType = fromType;
	}
	public String getMpFromId() {
		return mpFromId;
	}
	public void setMpFromId(String mpFromId) {
		this.mpFromId = mpFromId;
	}
	public Date getCreateAt() {
		return createAt;
	}
	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}
	public Long getRelatedId() {
		return relatedId;
	}
	public void setRelatedId(Long kid) {
		this.relatedId = kid;
	}
}
