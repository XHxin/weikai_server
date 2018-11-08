package cc.messcat.vo;

import java.util.List;

public class StandardReadSearchResult2 {
   
	private List<StandardReadListVo>  standardReadingList;   //连体化解读
	private String type;
	private int rowCount;
	
	
	public List<StandardReadListVo> getStandardReadingList() {
		return standardReadingList;
	}
	public void setStandardReadingList(List<StandardReadListVo> standardReadingList) {
		this.standardReadingList = standardReadingList;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getRowCount() {
		return rowCount;
	}
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
}
