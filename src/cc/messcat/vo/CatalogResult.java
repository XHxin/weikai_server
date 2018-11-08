package cc.messcat.vo;

import java.util.List;

public class CatalogResult {

	private String standardReadId;   //标准解读Id
	private List<CatalogVo1> catalogList;  //目录列表
	
	public String getStandardReadId() {
		return standardReadId;
	}
	public void setStandardReadId(String standardReadId) {
		this.standardReadId = standardReadId;
	}
	public List<CatalogVo1> getCatalogList() {
		return catalogList;
	}
	public void setCatalogList(List<CatalogVo1> catalogList) {
		this.catalogList = catalogList;
	}
}
