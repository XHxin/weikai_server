package cc.messcat.vo;

import java.util.List;

public class StudentVo2 {
	
	private String studentId;
	private String name;//学员名称
	private String mainPic;//头像
	private String className;//班别
	private int carType;//车型
	private int examStatus;//考试状态
	private String lastExamTime;//上次考试时间
	private String pickupAddress;//接送地址
	private String mobile;//接送地址
	private int isNew;//是否新学员
	private int examTime;//考试次数
	private List<ScheduleVo> schStatusList;//学习进度列表
	private List orderList;//预约列表
	private int hasUsedApp;//是否已经安装app
	private String coachId;
	
	private String appVersion;
	private String cardNo;
	private String address;
	private String hourUsing;
	private String hourRemain;
	private List<FinancialVo> financialList;
	
	private String coachName;//教练名称
	private String groupingId;//所在分组Id
	private String groupingName;//所在分组
	private int viewAuthority;//是否能查看学员详情权限
	
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
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
	public String getLastExamTime() {
		return lastExamTime;
	}
	public void setLastExamTime(String lastExamTime) {
		this.lastExamTime = lastExamTime;
	}
	public String getPickupAddress() {
		return pickupAddress;
	}
	public void setPickupAddress(String pickupAddress) {
		this.pickupAddress = pickupAddress;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public int getIsNew() {
		return isNew;
	}
	public void setIsNew(int isNew) {
		this.isNew = isNew;
	}
	public int getExamTime() {
		return examTime;
	}
	public void setExamTime(int examTime) {
		this.examTime = examTime;
	}
	public List<ScheduleVo> getSchStatusList() {
		return schStatusList;
	}
	public void setSchStatusList(List<ScheduleVo> schStatusList) {
		this.schStatusList = schStatusList;
	}
	public List getOrderList() {
		return orderList;
	}
	public void setOrderList(List orderList) {
		this.orderList = orderList;
	}
	public int getHasUsedApp() {
		return hasUsedApp;
	}
	public void setHasUsedApp(int hasUsedApp) {
		this.hasUsedApp = hasUsedApp;
	}
	public String getCoachId() {
		return coachId;
	}
	public void setCoachId(String coachId) {
		this.coachId = coachId;
	}
	public String getAppVersion() {
		return appVersion;
	}
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getHourUsing() {
		return hourUsing;
	}
	public void setHourUsing(String hourUsing) {
		this.hourUsing = hourUsing;
	}
	public String getHourRemain() {
		return hourRemain;
	}
	public void setHourRemain(String hourRemain) {
		this.hourRemain = hourRemain;
	}
	public List<FinancialVo> getFinancialList() {
		return financialList;
	}
	public void setFinancialList(List<FinancialVo> financialList) {
		this.financialList = financialList;
	}
	public String getCoachName() {
		return coachName;
	}
	public void setCoachName(String coachName) {
		this.coachName = coachName;
	}
	public String getGroupingName() {
		return groupingName;
	}
	public void setGroupingName(String groupingName) {
		this.groupingName = groupingName;
	}
	public int getViewAuthority() {
		return viewAuthority;
	}
	public void setViewAuthority(int viewAuthority) {
		this.viewAuthority = viewAuthority;
	}
	public String getGroupingId() {
		return groupingId;
	}
	public void setGroupingId(String groupingId) {
		this.groupingId = groupingId;
	}

}
