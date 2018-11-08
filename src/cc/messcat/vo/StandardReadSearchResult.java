package cc.messcat.vo;

import java.util.List;

public class StandardReadSearchResult {
   
	private List<StandardReadListVo>  standardReadingList;
	private String standardCode;
	private String type;
	private int rowCount;
	
	public List<StandardReadListVo> getStandardReadingList() {
		return standardReadingList;
	}
	public void setStandardReadingList(List<StandardReadListVo> standardReadingList) {
		this.standardReadingList = standardReadingList;
	}
	
	public String getStandardCode() {
		return standardCode;
	}
	public void setStandardCode(String standardCode) {
		this.standardCode = standardCode;
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
