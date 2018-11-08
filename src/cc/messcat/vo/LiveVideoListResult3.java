package cc.messcat.vo;

import java.util.List;

public class LiveVideoListResult3 {
	
	private List<LiveVideoListVo2> liveVideoList;  
	private int rowCount;
	
	public List<LiveVideoListVo2> getLiveVideoList() {
		return liveVideoList;
	}
	public void setLiveVideoList(List<LiveVideoListVo2> liveVideoList) {
		this.liveVideoList = liveVideoList;
	}
	public int getRowCount() {
		return rowCount;
	}
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}    
	
}
