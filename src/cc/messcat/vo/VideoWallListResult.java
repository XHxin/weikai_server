package cc.messcat.vo;

import java.util.List;

public class VideoWallListResult {

	private List<VideoWallVo> wallVos;
	private int rowCount;
	public List<VideoWallVo> getWallVos() {
		return wallVos;
	}
	public void setWallVos(List<VideoWallVo> wallVos) {
		this.wallVos = wallVos;
	}
	public int getRowCount() {
		return rowCount;
	}
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
	
	
}
