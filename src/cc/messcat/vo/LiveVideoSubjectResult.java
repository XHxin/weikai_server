package cc.messcat.vo;

import java.util.List;

public class LiveVideoSubjectResult {

	private List<LiveVideoSubjectListVo> subjectList;
	private int rowCount;
	
	
	public List<LiveVideoSubjectListVo> getSubjectList() {
		return subjectList;
	}
	public void setSubjectList(List<LiveVideoSubjectListVo> subjectList) {
		this.subjectList = subjectList;
	}
	public int getRowCount() {
		return rowCount;
	}
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
}
