package cc.messcat.vo;

import java.util.List;

public class StandardReadResult {
	
	private List<StandardReadListVo5> standReadList;
	private int rowCount;
	
	
	public List<StandardReadListVo5> getStandReadList() {
		return standReadList;
	}
	public void setStandReadList(List<StandardReadListVo5> standReadList) {
		this.standReadList = standReadList;
	}
	public int getRowCount() {
		return rowCount;
	}
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
}
