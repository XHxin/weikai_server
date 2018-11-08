package cc.messcat.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author nelson
 *
 *咨询服务实体
 */
@Entity
@Table(name="consult_service")
public class ConsultService {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String linkman;
	private String phone;
	private String email;
	
	@Column(name="server_eas")
	private String serverEas;
	
	@Column(name="receptionNum")
	private int receptionNum;
	
	@Column(name="is_online")
	private String isOnline;
	
	@Column(name="server_type")
	private String serverType;
	
	@Column(name="is_contact_us")
	private Integer isContactUs;

	@Column(name="is_send_msg")
	private Integer isSendMsg;
	
	public ConsultService() {
		super();
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getLinkman() {
		return linkman;
	}
	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getServerEas() {
		return serverEas;
	}
	public void setServerEas(String serverEas) {
		this.serverEas = serverEas;
	}
	public int getReceptionNum() {
		return receptionNum;
	}
	public void setReceptionNum(int receptionNum) {
		this.receptionNum = receptionNum;
	}
	public String getIsOnline() {
		return isOnline;
	}
	public void setIsOnline(String isOnline) {
		this.isOnline = isOnline;
	}
	public String getServerType() {
		return serverType;
	}
	public void setServerType(String serverType) {
		this.serverType = serverType;
	}
	public Integer getIsSendMsg() {
		return isSendMsg;
	}

	public void setIsSendMsg(Integer isSendMsg) {
		this.isSendMsg = isSendMsg;
	}

	public Integer getIsContactUs() {
		return isContactUs;
	}

	public void setIsContactUs(Integer isContactUs) {
		this.isContactUs = isContactUs;
	}
}
