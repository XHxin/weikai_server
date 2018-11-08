package cc.messcat.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="redeem_code_owner")
public class RedeemCodeOwner implements Serializable{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="redeem_code_id")
	private Long redeemCodeId;   //兑换码库存id
	
	@Column(name="redeem_code")
	private String redeemCode;  //兑换码
	
	@Column(name="owner_from")
	private int ownerFrom;   //来源 0：微信公众号 1：地图账号 2：手机号
	private String owner;    //拥有者身份 微信公众号为openId，地图账号为memberId，手机号即手机号
	
    @Column(name="user_id")
    private Long userId;     //使用者的memberId
	private int used;		 //是否已使用 0：未使用 1：已使用
	private int status;		 //状态 0：不可用 1：可用
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getRedeemCodeId() {
		return redeemCodeId;
	}
	public void setRedeemCodeId(Long redeemCodeId) {
		this.redeemCodeId = redeemCodeId;
	}
	public String getRedeemCode() {
		return redeemCode;
	}
	public void setRedeemCode(String redeemCode) {
		this.redeemCode = redeemCode;
	}
	public int getOwnerFrom() {
		return ownerFrom;
	}
	public void setOwnerFrom(int ownerFrom) {
		this.ownerFrom = ownerFrom;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public int getUsed() {
		return used;
	}
	public void setUsed(int used) {
		this.used = used;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
