package cc.messcat.vo;

public class StudentLoginResult {
	
	private String result;
	private String code;
	private StudentVo student;
	
	public StudentVo getStudent() {
		return student;
	}
	public void setStudent(StudentVo student) {
		this.student = student;
	}
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

}
