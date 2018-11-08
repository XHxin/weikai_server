package cc.messcat.vo;

import java.util.List;

public class CheckStudentListResult extends CommonResult {
	
	private List<CheckStudent> checkStudentList;
	private List<CheckStudent> otherCoachStudentList;

	public List<CheckStudent> getCheckStudentList() {
		return checkStudentList;
	}

	public void setCheckStudentList(List<CheckStudent> checkStudentList) {
		this.checkStudentList = checkStudentList;
	}

	public List<CheckStudent> getOtherCoachStudentList() {
		return otherCoachStudentList;
	}

	public void setOtherCoachStudentList(List<CheckStudent> otherCoachStudentList) {
		this.otherCoachStudentList = otherCoachStudentList;
	}

}
