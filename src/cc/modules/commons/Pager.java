package cc.modules.commons;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class Pager {

	protected int pageSize;
	protected int pageNo;
	protected int rowCount;
	protected int pageCount;
	protected int startIndex;
	protected int endIndex;
	protected int firstPageNo;
	protected int prePageNo;
	protected int nextPageNo;
	protected int lastPageNo;
	protected List resultList;

	
	public Pager() {
		super();
	}

	public Pager(int pageSize, int pageNo, int rowCount, List resultList) {
		this.pageSize = 10;
		this.pageNo = 1;
		this.rowCount = 0;
		pageCount = 1;
		startIndex = 1;
		endIndex = 1;
		firstPageNo = 1;
		prePageNo = 1;
		nextPageNo = 1;
		lastPageNo = 1;
		this.pageSize = pageSize;
		this.pageNo = pageNo;
		this.rowCount = rowCount;
		this.resultList = resultList;
		if (rowCount % pageSize == 0)
			pageCount = rowCount / pageSize;
		else
			pageCount = rowCount / pageSize + 1;
		startIndex = pageSize * (pageNo - 1);
		endIndex = startIndex + resultList.size();
		lastPageNo = pageCount;
		if (this.pageNo > 1)
			prePageNo = this.pageNo - 1;
		if (this.pageNo == lastPageNo)
			nextPageNo = lastPageNo;
		else
			nextPageNo = this.pageNo + 1;
	}

	public Object[] getPageSizeIndexs() {
		List result = new ArrayList(pageSizeList.length);
		for (int i = 0; i < pageSizeList.length; i++)
			result.add(String.valueOf(pageSizeList[i]));

		Object indexs[] = result.toArray();
		return indexs;
	}

	public Object[] getPageNoIndexs() {
		List result = new ArrayList(pageCount);
		for (int i = 0; i < pageCount; i++)
			result.add(String.valueOf(i + 1));

		Object indexs[] = result.toArray();
		return indexs;
	}

	public int getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

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

	public int[] getPageSizeList() {
		return pageSizeList;
	}

	public void setPageSizeList(int pageSizeList[]) {
		this.pageSizeList = pageSizeList;
	}

	public List getResultList() {
		return resultList;
	}

	public void setResultList(List resultList) {
		this.resultList = resultList;
	}

	public int getRowCount() {
		return rowCount;
	}

	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public int getFirstPageNo() {
		return firstPageNo;
	}

	public void setFirstPageNo(int firstPageNo) {
		this.firstPageNo = firstPageNo;
	}

	public int getLastPageNo() {
		return lastPageNo;
	}

	public void setLastPageNo(int lastPageNo) {
		this.lastPageNo = lastPageNo;
	}

	public int getNextPageNo() {
		return nextPageNo;
	}

	public void setNextPageNo(int nextPageNo) {
		this.nextPageNo = nextPageNo;
	}

	public int getPrePageNo() {
		return prePageNo;
	}

	public void setPrePageNo(int prePageNo) {
		this.prePageNo = prePageNo;
	}

	protected int pageSizeList[] = { 5, 10, 30, 50, 100 };

}