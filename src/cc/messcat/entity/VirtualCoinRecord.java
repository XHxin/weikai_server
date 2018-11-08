package cc.messcat.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 积分表
 * Member entity. @author MyEclipse Persistence Tools
 */
public class VirtualCoinRecord implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	private Member memberId; //会员id
	private BigDecimal virtualCoin; //虚拟币
	private String content;//内容
	private Date addTime;//新增时间
	private Date editTime; //编辑时间
	private String status;  //状态  1正常 0删除
	private String remark;//备注
	public Long getId() {
		return this.id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getAddTime() {
		return this.addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	public Date getEditTime() {
		return this.editTime;
	}
	public void setEditTime(Date editTime) {
		this.editTime = editTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public Member getMemberId() {
		return memberId;
	}
	public void setMemberId(Member memberId) {
		this.memberId = memberId;
	}

	public BigDecimal getVirtualCoin() {
		return virtualCoin;
	}

	public void setVirtualCoin(BigDecimal virtualCoin) {
		this.virtualCoin = virtualCoin;
	}
}