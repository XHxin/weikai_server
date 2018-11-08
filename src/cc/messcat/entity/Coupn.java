package cc.messcat.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 卡包实体
 * @author HASEE
 *
 */
@Entity
@Table(name="coupn")
public class Coupn implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name="coupn_money")
	private BigDecimal coupnMoney;
	
	@Column(name="use_scope")
	private String useScope;
	
	@Column(name="usable")
	private String usable;
	
	@Column(name="overlying")
	private String overlying;
	
	@Column(name="is_share")
	private String isShare;
	
	@Column(name="begin_date")
	private Date beginDate;
	
	@Column(name="end_date")
	private Date endDate;
	
	@Column(name="scope_num")
	private String scopeNum;
	
	@Column(name="video_id")
	private int videoId;
	
	@Column(name="is_remind")
	private Integer isRemind;  //是否做过推送提醒(0:否,1:是)

	public Coupn() {
	}

	public Coupn(BigDecimal coupnMoney, String useScope, String overlying, String isShare,Date endDate, String scopeNum, int videoId) {
		this.coupnMoney = coupnMoney;
		this.useScope = useScope;
		this.overlying = overlying;
		this.isShare = isShare;
		this.endDate = endDate;
		this.scopeNum = scopeNum;
		this.videoId = videoId;
		init();
	}

	private void init(){
		usable = "1";
		beginDate = new Date();
		isRemind = 0;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getCoupnMoney() {
		return coupnMoney;
	}

	public void setCoupnMoney(BigDecimal coupnMoney) {
		this.coupnMoney = coupnMoney;
	}

	public String getUseScope() {
		return useScope;
	}
	public void setUseScope(String useScope) {
		this.useScope = useScope;
	}
	public String getUsable() {
		return usable;
	}
	public void setUsable(String usable) {
		this.usable = usable;
	}
	public String getOverlying() {
		return overlying;
	}
	public void setOverlying(String overlying) {
		this.overlying = overlying;
	}
	public String getIsShare() {
		return isShare;
	}
	public void setIsShare(String isShare) {
		this.isShare = isShare;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Date getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getScopeNum() {
		return scopeNum;
	}
	public void setScopeNum(String scopeNum) {
		this.scopeNum = scopeNum;
	}
	public int getVideoId() {
		return videoId;
	}
	public void setVideoId(int videoId) {
		this.videoId = videoId;
	}
	public Integer getIsRemind() {
		return isRemind;
	}
	public void setIsRemind(Integer isRemind) {
		this.isRemind = isRemind;
	}
}
