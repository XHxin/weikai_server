package cc.messcat.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * @author nelson
 *
 *咨询记录实体
 */
@Entity
@Table(name="consult_record")
public class ConsultRecord {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name="memberid")
	private Long memberId;

	@Column(name="end_time")
	private Date endTime;

	@Column(name="server_eas")
	private String serverEas;

	@Column(name="server_type")
	private String serverType;
	
	
	public ConsultRecord() {
		super();
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
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getServerEas() {
		return serverEas;
	}
	public void setServerEas(String serverEas) {
		this.serverEas = serverEas;
	}
	public String getServerType() {
		return serverType;
	}
	public void setServerType(String serverType) {
		this.serverType = serverType;
	}

	
	
}
