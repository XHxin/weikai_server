package cc.messcat.vo;

import java.util.List;

public class StandReadingVoList {

	private int rowCount;
	private String searchKeyWord;
	private List<StandReadingVo> standReadingShare;
	
	public int getRowCount() {
		return rowCount;
	}
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
	public String getSearchKeyWord() {
		return searchKeyWord;
	}
	public void setSearchKeyWord(String searchKeyWord) {
		this.searchKeyWord = searchKeyWord;
	}
	public List<StandReadingVo> getStandReadingShare() {
		return standReadingShare;
	}
	public void setStandReadingShare(List<StandReadingVo> standReadingShare) {
		this.standReadingShare = standReadingShare;
	}
	
	
}
