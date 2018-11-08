package cc.messcat.vo;

public class BookingStudentVo {
	
	private String studentId;
	private String bookId;//预约记录id
	private String stuName;
	private String mainPic;
	private String bookTime;//1上午，2下午，3晚上
	private String bookDate;//预约日期
	private String status;//1确定 2待定 3取消 
	private String outOfTime;//失效时间
	private String cancleReason;//取消原因
	private String cancelCoach;//取消人（教练）
	private String trainStatus;//练车状态 0未练车，1已练车
	private String isCoachGroup;//是否属于当前登录教练名下或者同组学员（0否/1是）
	private String bookStatus;//状态（新版本）1 未签到 ，2 已签到  ， 3 已签退 ， 4 取消（学员发起），5 取消（教练发起）
	private String studentPhone;//学员电话
	private String bookNote;//预约备注
	private String bookTimePoint;//预约时间点

	
	public BookingStudentVo(){
		trainStatus = "0";
	}
	
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public String getStuName() {
		return stuName;
	}
	public void setStuName(String stuName) {
		this.stuName = stuName;
	}
	public String getBookTime() {
		return bookTime;
	}
	public void setBookTime(String bookTime) {
		this.bookTime = bookTime;
	}
	public String getBookDate() {
		return bookDate;
	}
	public void setBookDate(String bookDate) {
		this.bookDate = bookDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getOutOfTime() {
		return outOfTime;
	}
	public void setOutOfTime(String outOfTime) {
		this.outOfTime = outOfTime;
	}
	public String getCancleReason() {
		return cancleReason;
	}
	public void setCancleReason(String cancleReason) {
		this.cancleReason = cancleReason;
	}
	public String getMainPic() {
		return mainPic;
	}
	public void setMainPic(String mainPic) {
		this.mainPic = mainPic;
	}
	public String getTrainStatus() {
		return trainStatus;
	}
	public void setTrainStatus(String trainStatus) {
		this.trainStatus = trainStatus;
	}

	public String getIsCoachGroup() {
		return isCoachGroup;
	}

	public void setIsCoachGroup(String isCoachGroup) {
		this.isCoachGroup = isCoachGroup;
	}

	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

	public String getCancelCoach() {
		return cancelCoach;
	}

	public void setCancelCoach(String cancelCoach) {
		this.cancelCoach = cancelCoach;
	}

	public String getBookStatus() {
		return bookStatus;
	}

	public void setBookStatus(String bookStatus) {
		this.bookStatus = bookStatus;
	}

	public String getStudentPhone() {
		return studentPhone;
	}

	public void setStudentPhone(String studentPhone) {
		this.studentPhone = studentPhone;
	}

	public String getBookNote() {
		return bookNote;
	}

	public void setBookNote(String bookNote) {
		this.bookNote = bookNote;
	}

	public String getBookTimePoint() {
		return bookTimePoint;
	}

	public void setBookTimePoint(String bookTimePoint) {
		this.bookTimePoint = bookTimePoint;
	}

	

}
