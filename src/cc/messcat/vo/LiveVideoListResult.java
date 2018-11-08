package cc.messcat.vo;

import java.util.List;

public class LiveVideoListResult {
	
	private List<LiveVideoListVo3> videos;  
	private int rowCount;
	
	public List<LiveVideoListVo3> getVideos() {
		return videos;
	}
	public void setVideos(List<LiveVideoListVo3> videos) {
		this.videos = videos;
	}
	public int getRowCount() {
		return rowCount;
	}
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}    
	
}
