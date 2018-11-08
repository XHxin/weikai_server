package cc.messcat.vo;

import java.util.List;

public class CatalogVo2 {

	private String catalog;   //目录名
	private String catalogLevel;  //目录级别
	private List<CatalogVo3> catalogVoList2;  //二级目录的子目录
	
	public String getCatalog() {
		return catalog;
	}
	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}
	public String getCatalogLevel() {
		return catalogLevel;
	}
	public void setCatalogLevel(String catalogLevel) {
		this.catalogLevel = catalogLevel;
	}
	public List<CatalogVo3> getCatalogVoList2() {
		return catalogVoList2;
	}
	public void setCatalogVoList2(List<CatalogVo3> catalogVoList3) {
		this.catalogVoList2 = catalogVoList3;
	}

}
