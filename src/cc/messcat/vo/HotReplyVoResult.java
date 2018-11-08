package cc.messcat.vo;

import java.util.List;

public class HotReplyVoResult {
	
	private List<HotReplyVo> replyList;
    private int rowCount;
    
    
	public List<HotReplyVo> getReplyList() {
		return replyList;
	}
	public void setReplyList(List<HotReplyVo> replyList) {
		this.replyList = replyList;
	}
	public int getRowCount() {
		return rowCount;
	}
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
}
