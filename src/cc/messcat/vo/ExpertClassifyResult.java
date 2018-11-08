package cc.messcat.vo;

import java.util.List;

public class ExpertClassifyResult {
	
	private List<ExpertClassifyVo> classifyList;
	private int rowCount;
	

	public List<ExpertClassifyVo> getClassifyList() {
		return classifyList;
	}
	public void setClassifyList(List<ExpertClassifyVo> classifyList) {
		this.classifyList = classifyList;
	}
	public int getRowCount() {
		return rowCount;
	}
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
}
