package cc.messcat.vo;

import java.util.List;
/*
 * 
 * 收藏实体
 */
public class CollectListVo {
	private List<CollectList> collectList;
	private int pageNo;
	private int pageSize;
	private int rowCount;
	
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public List<CollectList> getCollectList() {
		return collectList;
	}
	public void setCollectList(List<CollectList> collectList) {
		this.collectList = collectList;
	}
	public int getRowCount() {
		return rowCount;
	}
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
	
}
