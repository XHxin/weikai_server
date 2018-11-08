package cc.messcat.vo;

public class ExpertListVo {
	
	private String expertId;		//专家Id(对应会员中的role)
	private String realname;        //专家名称(对应会员中的realname)
	private String photo;			//专家图片(对应会员中的会员头像)
	
	public String getExpertId() {
		return expertId;
	}
	public void setExpertId(String expertId) {
		this.expertId = expertId;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
}
