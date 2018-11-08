package cc.messcat.vo;

public class PopularListVo {

	private Long memberId;    //会员Id
	private String openId;    //微信的openId
	private String memberName;  //会员名
	private String mobile;	 //会员手机号
	private String photo;     //会员头像
	private String isAdmin;    //是否是聊天室的管理员(0:否,1:是)
	private int audienceCount; //观众人数
 	private int isOwner;      //是否是自己的课程(0:否,1:是)
 	private Long paidSum;     //购买人数
 	 
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public int getAudienceCount() {
		return audienceCount;
	}
	public void setAudienceCount(int audienceCount) {
		this.audienceCount = audienceCount;
	}
	public Long getMemberId() {
		return memberId;
	}
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public int getIsOwner() {
		return isOwner;
	}
	public void setIsOwner(int isOwner) {
		this.isOwner = isOwner;
	}
	public Long getPaidSum() {
		return paidSum;
	}
	public void setPaidSum(Long paidSum) {
		this.paidSum = paidSum;
	}
	public String getIsAdmin() {
		return isAdmin;
	}
	public void setIsAdmin(String isAdmin) {
		this.isAdmin = isAdmin;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
}
