package cc.messcat.vo;

import java.util.List;

public class VideoBulletListResult {

	private List<VideoBulletVo> videoBulletVos;
	private int rowCount;
	public List<VideoBulletVo> getVideoBulletVos() {
		return videoBulletVos;
	}
	public void setVideoBulletVos(List<VideoBulletVo> videoBulletVos) {
		this.videoBulletVos = videoBulletVos;
	}
	public int getRowCount() {
		return rowCount;
	}
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
	
	
}
