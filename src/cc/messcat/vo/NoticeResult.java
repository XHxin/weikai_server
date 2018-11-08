package cc.messcat.vo;

import java.util.List;

public class NoticeResult {
	
	private String result;
	private String code;
	private List<NoticeVo> noticeList;
	
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public List<NoticeVo> getNoticeList() {
		return noticeList;
	}
	public void setNoticeList(List<NoticeVo> noticeList) {
		this.noticeList = noticeList;
	}

}
