package cc.messcat.vo;

import java.util.List;

public class PopularListResult {
	
	public PopularListVo popular;
	public List<PopularListVo> popularList;
	public int rowCount;
	
	
	public List<PopularListVo> getPopularList() {
		return popularList;
	}
	public void setPopularList(List<PopularListVo> popularList) {
		this.popularList = popularList;
	}
	public int getRowCount() {
		return rowCount;
	}
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
	public PopularListVo getPopular() {
		return popular;
	}
	public void setPopular(PopularListVo popular) {
		this.popular = popular;
	}
}
