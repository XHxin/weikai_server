package cc.messcat.vo;

import java.util.List;

public class LiveVideoListResult2 {
	
	private List<LiveVideoListVo> liveVideoList;  
	private String subjectDetailCover;  //专题详情页图片
	private int rowCount;
	
	public List<LiveVideoListVo> getLiveVideoList() {
		return liveVideoList;
	}
	public void setLiveVideoList(List<LiveVideoListVo> liveVideoList) {
		this.liveVideoList = liveVideoList;
	}
	public int getRowCount() {
		return rowCount;
	}
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
	public String getSubjectDetailCover() {
		return subjectDetailCover;
	}
	public void setSubjectDetailCover(String subjectDetailCover) {
		this.subjectDetailCover = subjectDetailCover;
	}    
}
