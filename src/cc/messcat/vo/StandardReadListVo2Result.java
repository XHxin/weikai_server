package cc.messcat.vo;

import java.util.List;

public class StandardReadListVo2Result {
	
	private List<StandardReadListVo2> standardReadingList;
    private String type;
    private int rowCount;
    
    
	public List<StandardReadListVo2> getStandardReadingList() {
		return standardReadingList;
	}
	public void setStandardReadingList(List<StandardReadListVo2> standardReadingList) {
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
