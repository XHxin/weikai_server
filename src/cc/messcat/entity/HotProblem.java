package cc.messcat.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="hot_problem")
public class HotProblem implements Serializable {
    
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="memberID")
	private Long memberId;   		//提问者的会员ID
	
	@Column(name="expertID")
	private Long expertId;			//需回答问题的专家ID
	private String name;			//问题名称
	private String type;			//提问方式(0为公开,1为私密)
	
	@Column(name="add_time")
	private Date addTime;			//创建时间
	
	@Column(name="edit_time")
	private Date editTime;			//编辑时间
	private String status;			//状态(0：未付款 1：已付款  2:退款)
	private Integer dispose;      //是否被定时器做过处理

	public HotProblem() {
	}

	public HotProblem(Long memberId, Long expertId, String name, String type,String status) {
		this.memberId = memberId;
		this.expertId = expertId;
		this.name = name;
		this.status=status;
		this.type = type;
		init();
	}

	private void init(){
		this.addTime = new Date();
		this.editTime = new Date();
		this.dispose = 0;
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
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	public Date getEditTime() {
		return editTime;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Long getExpertId() {
		return expertId;
	}
	public void setExpertId(Long expertId) {
		this.expertId = expertId;
	}

	public Integer getDispose() {
		return dispose;
	}

	public void setDispose(Integer dispose) {
		this.dispose = dispose;
	}
}
