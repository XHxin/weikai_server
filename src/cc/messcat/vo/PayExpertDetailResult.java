package cc.messcat.vo;

import java.util.List;

public class PayExpertDetailResult {
	
	private PayConsultExpertVo expert;
	private List<HotReplyVo>  replyList;
	private int rowCount;
	
	
	public PayConsultExpertVo getExpert() {
		return expert;
	}
	public void setExpert(PayConsultExpertVo expert) {
		this.expert = expert;
	}
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
