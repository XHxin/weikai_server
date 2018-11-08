package cc.messcat.vo;

import java.util.List;

public class ReplyVoList {
	
	private int rowCount;
	private String searchKeyWord;
	private List<ReplyVo> hotReplyVos;
	
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
	public List<ReplyVo> getHotReplyVos() {
		return hotReplyVos;
	}
	public void setHotReplyVos(List<ReplyVo> hotReplyVos) {
		this.hotReplyVos = hotReplyVos;
	}
}
