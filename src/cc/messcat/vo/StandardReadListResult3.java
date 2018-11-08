package cc.messcat.vo;

import java.util.List;

public class StandardReadListResult3 {
   
	private List<StandardReadListVo4>  articleList;  //文章列表(质量分享+标准分享+专栏订阅)
	private int rowCount;
	
	public List<StandardReadListVo4> getArticleList() {
		return articleList;
	}
	public void setArticleList(List<StandardReadListVo4> articleList) {
		this.articleList = articleList;
	}
	public int getRowCount() {
		return rowCount;
	}
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
}
