package cc.messcat.vo;

import java.util.List;

public class HotExpertListVoResult {
	
	List<HotExpertListVo> hotExpertList;
	private int rowCount;
	
	
	public List<HotExpertListVo> getHotExpertList() {
		return hotExpertList;
	}
	public void setHotExpertList(List<HotExpertListVo> hotExpertList) {
		this.hotExpertList = hotExpertList;
	}
	public int getRowCount() {
		return rowCount;
	}
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

}
