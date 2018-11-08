package cc.messcat.vo;

import java.util.List;

public class LiveVoList {
	private int rowCount;
	private String searchKeyWord;
	private List<LiveVo> liveVideos;
	
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
	public List<LiveVo> getLiveVideos() {
		return liveVideos;
	}
	public void setLiveVideos(List<LiveVo> liveVideos) {
		this.liveVideos = liveVideos;
	}
	
	
}
