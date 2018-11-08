package cc.messcat.entity;

import java.io.Serializable;

/**
 * 用户拥有卡券-实体
 * @author HASEE
 *
 */
public class CoupnUser implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long memberId;
	private Coupn coupnId;
	private Long sharerId;
	private String sharer;
	private String used;
	private String memberName;

	public CoupnUser() {
	}

	public CoupnUser(Long memberId, Coupn coupnId, Long sharerId, String sharer, String memberName) {
		this.memberId = memberId;
		this.coupnId = coupnId;
		this.sharerId = sharerId;
		this.sharer = sharer;
		this.memberName = memberName;
		init();
	}

	private void init(){
		used = "0";
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getMemberId() {
		return memberId;
	}
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	public Coupn getCoupnId() {
		return coupnId;
	}
	public void setCoupnId(Coupn coupnId) {
		this.coupnId = coupnId;
	}
	public Long getSharerId() {
		return sharerId;
	}
	public void setSharerId(Long sharerId) {
		this.sharerId = sharerId;
	}
	public String getSharer() {
		return sharer;
	}
	public void setSharer(String sharer) {
		this.sharer = sharer;
	}
	public String getUsed() {
		return used;
	}
	public void setUsed(String used) {
		this.used = used;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	
}
