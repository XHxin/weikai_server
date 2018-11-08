package cc.messcat.vo;

import java.util.List;

public class NewsListResult {
	
	private String result;
	private String code;
	private List<ColumnVo> columnList;
	
	public List<ColumnVo> getColumnList() {
		return columnList;
	}
	public void setColumnList(List<ColumnVo> columnList) {
		this.columnList = columnList;
	}
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

}
