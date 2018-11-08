package cc.messcat.vo;

import java.util.List;

public class ExpertVoList {
	private int rowCount;
	private String searchKeyWord;
	private List<ExpertVo> experts;
	
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
	public List<ExpertVo> getExperts() {
		return experts;
	}
	public void setExperts(List<ExpertVo> experts) {
		this.experts = experts;
	}
	
	
}
