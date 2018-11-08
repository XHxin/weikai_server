package cc.messcat.vo;

import java.util.List;

public class ColumnNewsListResult {
	
	private String result;
	private String code;
	private List<NewsVo> newsList;
	
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
	public List<NewsVo> getNewsList() {
		return newsList;
	}
	public void setNewsList(List<NewsVo> newsList) {
		this.newsList = newsList;
	}

}
