package cc.messcat.vo;

import java.util.List;

public class StudentListResult {
	
	private String result;
	private String code;
	private List<StudentVo2> studentList;
	private String studentCount;
	
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public List<StudentVo2> getStudentList() {
		return studentList;
	}
	public void setStudentList(List<StudentVo2> studentList) {
		this.studentList = studentList;
	}
	public String getStudentCount() {
		return studentCount;
	}
	public void setStudentCount(String studentCount) {
		this.studentCount = studentCount;
	}


}
