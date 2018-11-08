package cc.messcat.vo;

import java.util.Date;
import java.util.List;

public class ProductListVo {

	private int pageSize;
	private int pageNo;
	private int rowCount;
	private List<ProductVo> products;
	
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public int getRowCount() {
		return rowCount;
	}
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
	public List<ProductVo> getProducts() {
		return products;
	}
	public void setProducts(List<ProductVo> products) {
		this.products = products;
	}
	
	
}
