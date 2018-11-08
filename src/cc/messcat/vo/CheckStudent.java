package cc.messcat.vo;


public class CheckStudent {
	
	private String trainId;//签到记录id
	private String name;//学员名称
	private String mainPic;//头像
	private String className;//班别
	private int carType;//车型
	private int examStatus;//考试状态
	private String mobile;//接送地址
	private String beginTime;//上车时间
	private String veNo;//车牌号
	private int status;
	private String trainTime;
	private String isOtherCoach;//是否其他教练的签到学员
	private String coachName;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMainPic() {
		return mainPic;
	}
	public void setMainPic(String mainPic) {
		this.mainPic = mainPic;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public int getCarType() {
		return carType;
	}
	public void setCarType(int carType) {
		this.carType = carType;
	}
	public int getExamStatus() {
		return examStatus;
	}
	public void setExamStatus(int examStatus) {
		this.examStatus = examStatus;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public String getVeNo() {
		return veNo;
	}
	public void setVeNo(String veNo) {
		this.veNo = veNo;
	}
	public String getTrainId() {
		return trainId;
	}
	public void setTrainId(String trainId) {
		this.trainId = trainId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getTrainTime() {
		return trainTime;
	}
	public void setTrainTime(String trainTime) {
		this.trainTime = trainTime;
	}
	public String getIsOtherCoach() {
		return isOtherCoach;
	}
	public void setIsOtherCoach(String isOtherCoach) {
		this.isOtherCoach = isOtherCoach;
	}
	public String getCoachName() {
		return coachName;
	}
	public void setCoachName(String coachName) {
		this.coachName = coachName;
	}
}
