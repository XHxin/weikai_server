package cc.messcat.vo;

import java.util.List;

public class RecordEstimateVoList {

	
	private int rowCount;
	private String totalGrade;
	private List<RecordEstimateVo> recordEstimateVos;
	
	public int getRowCount() {
		return rowCount;
	}
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
	public List<RecordEstimateVo> getRecordEstimateVos() {
		return recordEstimateVos;
	}
	public void setRecordEstimateVos(List<RecordEstimateVo> recordEstimateVos) {
		this.recordEstimateVos = recordEstimateVos;
	}
	public String getTotalGrade() {
		return totalGrade;
	}
	public void setTotalGrade(String totalGrade) {
		this.totalGrade = totalGrade;
	}
	
}
