package cc.messcat.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="expert_classify")
public class ExpertClassify implements Serializable {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;       
	private String name;     //专家分类名称
	private String icon;	 //图标
	@Column(name="add_time")
	private Date addTime;	 //创建时间
	@Column(name="edit_time")
	private Date editTime;   //编辑时间
    private String status;   //状态： 0：停用  1：启用
    @Column(name="is_showIndex",length=1)
    private String isShowIndex;		//是否在 付费咨询模块的 首页 显示(0:否 , 1:是)
    private Integer sort;     //排序ID
    
    @ManyToMany(mappedBy="classifys")
    private List<Member> experts=new ArrayList<Member>();
    
    @Column(name="blue_icon")
    private String blueIcon;//5期新图标
    
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public String getIsShowIndex() {
		return isShowIndex;
	}
	public void setIsShowIndex(String isShowIndex) {
		this.isShowIndex = isShowIndex;
	}
	public List<Member> getExperts() {
		return experts;
	}
	public void setExperts(List<Member> experts) {
		this.experts = experts;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getBlueIcon() {
		return blueIcon;
	}
	public void setBlueIcon(String blueIcon) {
		this.blueIcon = blueIcon;
	}
	
}
