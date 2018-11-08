package cc.messcat.vo;

import java.util.List;

/**
 * @author HASEE
 *付费咨询-他的回答
 */
public class PayConsultListVo {

	private List<PayConsultVo>  replyList;
	private int rowCount;
	
	public List<PayConsultVo> getReplyList() {
		return replyList;
	}
	public void setReplyList(List<PayConsultVo> replyList) {
		this.replyList = replyList;
	}
	public int getRowCount() {
		return rowCount;
	}
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
	
}
