package cc.messcat.vo;

import java.util.List;

public class CatalogVo1 {

	private String catalog;   //目录名
	private String catalogLevel;  //目录级别
	private List<CatalogVo2> catalogVoList1;  //一级目录的子目录
	
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
	public List<CatalogVo2> getCatalogVoList1() {
		return catalogVoList1;
	}
	public void setCatalogVoList1(List<CatalogVo2> catalogVoList1) {
		this.catalogVoList1 = catalogVoList1;
	}
	
}
