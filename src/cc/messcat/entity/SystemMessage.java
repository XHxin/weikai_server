package cc.messcat.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="system_message")
public class SystemMessage implements Serializable {
    
		//消息类型（1：准入报告 2：标准解读 3：质量分享 4：付费咨询 5：网页新闻 6：电商报告 7：专家 8：购买信息  9:订阅专栏 10:视频和直播 11:卡券 12:会员到期    13：更新提醒 ）
		public static final int type0 = 0;//用于准入报告地区id
		public static final int type1 = 1;
		public static final int type2 = 2;
		public static final int type3 = 3;
		public static final int type4 = 4;
		public static final int type5 = 5;
		public static final int type6 = 6;
		public static final int type7 = 7;
		public static final int type8 = 8;
		public static final int type9 = 9;
		public static final int type10 = 10;
		public static final int type11 = 11;
		public static final int type12 = 12;
		public static Map<String, String> typeMap=new LinkedHashMap<String, String>();
		static{
			typeMap.put(""+type1, "report");
			typeMap.put(""+type2, "standardReading");
			typeMap.put(""+type3, "standardReading");
			typeMap.put(""+type4, "consult");
			typeMap.put(""+type5, "web");
			typeMap.put(""+type6, "ebReport");
			typeMap.put(""+type7, "expert");
			typeMap.put(""+type8, "buys");
			typeMap.put(""+type9, "standardReading");
			typeMap.put(""+type10, "course");
			typeMap.put(""+type11, "card");
			typeMap.put(""+type12, "vip");
		}
		public static Map<String, String> idMap=new LinkedHashMap<String, String>();
		static{
			idMap.put(""+type0, "regionId");
			idMap.put(""+type1, "productId");
			idMap.put(""+type2, "standardReadingId");
			idMap.put(""+type3, "standardReadingId");
			idMap.put(""+type4, "replyId");
			idMap.put(""+type5, "url");
			idMap.put(""+type6, "reportId");
			idMap.put(""+type7, "expertId");
			idMap.put(""+type9, "standardReadingId");
			idMap.put(""+type10, "id");
		}
		public static Map<String, String> titleMap=new LinkedHashMap<String, String>();
		static{
			titleMap.put(""+type1, "系统向你推荐了一份准入报告");
			titleMap.put(""+type2, "系统向你推荐了一份标准解读");
			titleMap.put(""+type3, "系统向你推荐了一份质量分享");
			titleMap.put(""+type4, "系统向你推荐了一条付费咨询");
			titleMap.put(""+type5, "系统向你推荐了一条新闻");
			titleMap.put(""+type6, "系统向你推荐了一份电商报告");
			titleMap.put(""+type7, "系统向你推荐了一位专家");
			titleMap.put(""+type8, "你购买了");
		}
		public static Map<String, String> buysMap=new LinkedHashMap<String, String>();
		static{
			buysMap.put(""+type0, "一份会员套餐");
			buysMap.put(""+type1, "一份准入报告");
			buysMap.put(""+type2, "一份标准解读");
			buysMap.put(""+type3, "一份质量分享");
			buysMap.put(""+type4, "一条付费咨询");
			buysMap.put(""+type6, "一份电商报告");
			buysMap.put(""+type7, "钱包金额");
			buysMap.put(""+type8, "直播视频");
		}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="relateID")
	private Long relateId;   		//关联id
	
	@Column(name="regionID")
	private Long regionId;			//地区id
	private String type;			//消息类型（1：准入报告 2：标准解读 3：质量分享 4：付费咨询 5：新闻 6：电商报告 7：专家 8：购买信息 9:订阅专栏 10:视频和直播 11:卡券 12:会员到期    13：更新提醒）
	private String title;			//标题
	private String remark;          //简介
	
	@Column(name="add_time")
	private Date addTime;			//创建时间
	
	@Column(name="edit_time")
	private Date editTime;			//编辑时间
	private String status;			//状态(0：停用  1：启用)
	private String pushType;        //推送类型（0：推送全部会员 1：推送部分会员）
	private String memberIds;       //推送会员id串（推送类型为1时使用）
	private String photo;			//页面显示的图片
	
	@Column(name="data_url")
	private String dataUrl;			//跳转链接
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Long getRelateId() {
		return relateId;
	}
	public void setRelateId(Long relateId) {
		this.relateId = relateId;
	}
	public Long getRegionId() {
		return regionId;
	}
	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getPushType() {
		return pushType;
	}
	public void setPushType(String pushType) {
		this.pushType = pushType;
	}
	public String getMemberIds() {
		return memberIds;
	}
	public void setMemberIds(String memberIds) {
		this.memberIds = memberIds;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getDataUrl() {
		return dataUrl;
	}
	public void setDataUrl(String dataUrl) {
		this.dataUrl = dataUrl;
	}
	
}
