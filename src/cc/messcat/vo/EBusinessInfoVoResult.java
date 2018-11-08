package cc.messcat.vo;

import java.util.List;

public class EBusinessInfoVoResult {

    private List<EBusinessInfoVo>  resultList;
	private int pageNo;
	private int pageSize;
	private int rowCount;
	
	public List<EBusinessInfoVo> getResultList() {
		return resultList;
	}
	public void setResultList(List<EBusinessInfoVo> resultList) {
		this.resultList = resultList;
	}
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getRowCount() {
		return rowCount;
	}
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
}
