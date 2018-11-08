package cc.messcat.vo;

import java.util.List;

public class FrontColumnNewsVo {

	private Long columnId;
	
	private String columnName;
	
	private List<EnterpriseNewsVo> news;

	public Long getColumnId() {
		return columnId;
	}

	public void setColumnId(Long columnId) {
		this.columnId = columnId;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public List<EnterpriseNewsVo> getNews() {
		return news;
	}

	public void setNews(List<EnterpriseNewsVo> news) {
		this.news = news;
	}
	
	
}
