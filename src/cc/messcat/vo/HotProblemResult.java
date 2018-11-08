package cc.messcat.vo;

import java.util.List;

public class HotProblemResult {
	
	private List<HotReplyPayVo> resultList;
    private int rowCount;
    
    
	public List<HotReplyPayVo> getResultList() {
		return resultList;
	}
	public void setResultList(List<HotReplyPayVo> resultList) {
		this.resultList = resultList;
	}
	public int getRowCount() {
		return rowCount;
	}
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
}
