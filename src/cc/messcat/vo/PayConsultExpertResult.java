package cc.messcat.vo;

import java.util.List;

public class PayConsultExpertResult {
	
	private List<PayConsultExpertVo> resultList;
	private int rowCount;
	
	
	public List<PayConsultExpertVo> getResultList() {
		return resultList;
	}
	public void setResultList(List<PayConsultExpertVo> resultList) {
		this.resultList = resultList;
	}
	public int getRowCount() {
		return rowCount;
	}
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
}
