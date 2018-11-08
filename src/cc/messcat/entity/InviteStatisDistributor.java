package cc.messcat.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="invite_statis_distributor")
public class InviteStatisDistributor {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="invite_id")
	private Long inviteId;
	
	@Column(name="is_exchange_integral")
	private Integer isExchangeIntegral;
	
	@Column(name="create_at")
	private Date createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getInviteId() {
		return inviteId;
	}

	public void setInviteId(Long inviteId) {
		this.inviteId = inviteId;
	}

	public Integer getIsExchangeIntegral() {
		return isExchangeIntegral;
	}

	public void setIsExchangeIntegral(Integer isExchangeIntegral) {
		this.isExchangeIntegral = isExchangeIntegral;
	}

	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
