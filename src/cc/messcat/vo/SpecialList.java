package cc.messcat.vo;

import java.util.List;

import cc.messcat.entity.LiveVideoSubject;

public class SpecialList {
	private int rowCount;
	private String searchKeyWord;
	private List<SpecialVo> specialList;
	
	
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
	public List<SpecialVo> getSpecialList() {
		return specialList;
	}
	public void setSpecialList(List<SpecialVo> specialList) {
		this.specialList = specialList;
	}
	
}
