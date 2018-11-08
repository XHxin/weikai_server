package cc.messcat.vo;

public class BookingCoachVo {
	
	private String coachId;//教练id
	private String trainId;//练车记录id
	private String coachName;//教练名称
	private String mainPic;//教练头像
	private String mobile;//教练手机
	private String coachSart;//教练星级
	private String bookTime;//预约时间
	private String addTime;//添加时间
	private String status;//练车状态
	private String week;
	private String period;
	private String effTime;
	private String effNote;
	private String headImg;//教练新头像（七牛云）
	private String bookNote;//预约留言
	private String cancelPerson;//取消人
	private String cancelReason;//取消原因
	private String bookTimePoint;//预约时间点
	
	
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
	public String getMainPic() {
		return mainPic;
	}
	public void setMainPic(String mainPic) {
		this.mainPic = mainPic;
	}
	public String getBookTime() {
		return bookTime;
	}
	public void setBookTime(String bookTime) {
		this.bookTime = bookTime;
	}
	public String getAddTime() {
		return addTime;
	}
	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTrainId() {
		return trainId;
	}
	public void setTrainId(String trainId) {
		this.trainId = trainId;
	}
	public String getWeek() {
		return week;
	}
	public void setWeek(String week) {
		this.week = week;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getCoachSart() {
		return coachSart;
	}
	public void setCoachSart(String coachSart) {
		this.coachSart = coachSart;
	}
	public String getEffTime() {
		return effTime;
	}
	public void setEffTime(String effTime) {
		this.effTime = effTime;
	}
	public String getEffNote() {
		return effNote;
	}
	public void setEffNote(String effNote) {
		this.effNote = effNote;
	}
	public String getHeadImg() {
		return headImg;
	}
	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}
	public String getBookNote() {
		return bookNote;
	}
	public void setBookNote(String bookNote) {
		this.bookNote = bookNote;
	}
	public String getCancelPerson() {
		return cancelPerson;
	}
	public void setCancelPerson(String cancelPerson) {
		this.cancelPerson = cancelPerson;
	}
	public String getCancelReason() {
		return cancelReason;
	}
	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}
	public String getBookTimePoint() {
		return bookTimePoint;
	}
	public void setBookTimePoint(String bookTimePoint) {
		this.bookTimePoint = bookTimePoint;
	}

}
