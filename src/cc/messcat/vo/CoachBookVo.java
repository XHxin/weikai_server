package cc.messcat.vo;

import java.util.List;

public class CoachBookVo {
	private String coachId;//教练id
	private String coachName;//教练姓名
	private String coachPic;//教练头像
	private String coachPhone;//教练电话
	private String bookDate;//预约日期
	private List<BookPeriodVo> bookPeriodVos;//预约时间段Vo列表
	private String headImg;//教练新头像（七牛云）
	
	public String getCoachId() {
		return coachId;
	}
	public void setCoachId(String coachId) {
		this.coachId = coachId;
	}
	public String getCoachName() {
		return coachName;
	}
	public void setCoachName(String coachName) {
		this.coachName = coachName;
	}
	public String getCoachPic() {
		return coachPic;
	}
	public void setCoachPic(String coachPic) {
		this.coachPic = coachPic;
	}
	public String getBookDate() {
		return bookDate;
	}
	public void setBookDate(String bookDate) {
		this.bookDate = bookDate;
	}
	public List<BookPeriodVo> getBookPeriodVos() {
		return bookPeriodVos;
	}
	public void setBookPeriodVos(List<BookPeriodVo> bookPeriodVos) {
		this.bookPeriodVos = bookPeriodVos;
	}
	public String getCoachPhone() {
		return coachPhone;
	}
	public void setCoachPhone(String coachPhone) {
		this.coachPhone = coachPhone;
	}
	public String getHeadImg() {
		return headImg;
	}
	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}
	

}
