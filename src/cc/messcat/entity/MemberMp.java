package cc.messcat.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author xiehuaxin
 * @createDate 2018年5月21日 上午9:16:06
 * @todo memberId与微信openId、unionid绑定
 */
@Entity
@Table(name="member_mp")
public class MemberMp {

	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name="memberid")
	private Long memberId;
	
	@Column(name="openid")
	private String openId;
	
	@Column(name="unionid")
	private String unionId;

	
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

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getUnionId() {
		return unionId;
	}

	public void setUnionId(String unionId) {
		this.unionId = unionId;
	}
	
	
}
