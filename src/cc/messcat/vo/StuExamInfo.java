package cc.messcat.vo;

public class StuExamInfo {
	
	private String studentId;//学员Id
	private String stuName;//学员名称
	private String stuType;//学员类型
	private String mainPic;//头像
	private String course;//科目
	private String courseStatus;//科目状态
	private String examDate;//考试日期
	private String num;//预约排队号
	
	public String getStuName() {
		return stuName;
	}
	public void setStuName(String stuName) {
		this.stuName = stuName;
	}
	public String getStuType() {
		return stuType;
	}
	public void setStuType(String stuType) {
		this.stuType = stuType;
	}
	public String getMainPic() {
		return mainPic;
	}
	public void setMainPic(String mainPic) {
		this.mainPic = mainPic;
	}
	public String getCourse() {
		return course;
	}
	public void setCourse(String course) {
		this.course = course;
	}
	public String getCourseStatus() {
		return courseStatus;
	}
	public void setCourseStatus(String courseStatus) {
		this.courseStatus = courseStatus;
	}
	public String getExamDate() {
		return examDate;
	}
	public void setExamDate(String examDate) {
		this.examDate = examDate;
	}
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	

}
