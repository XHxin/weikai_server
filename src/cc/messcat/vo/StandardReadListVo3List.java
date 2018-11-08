package cc.messcat.vo;

import java.util.List;

public class StandardReadListVo3List {
	private int rowCount;
	private String searchKeyWord;
	private List<StandardReadListVo3> standReadingColumn;
	
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
	public List<StandardReadListVo3> getStandReadingColumn() {
		return standReadingColumn;
	}
	public void setStandReadingColumn(List<StandardReadListVo3> standReadingColumn) {
		this.standReadingColumn = standReadingColumn;
	}
}
