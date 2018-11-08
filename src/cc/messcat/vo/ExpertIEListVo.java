package cc.messcat.vo;

import java.util.Date;
import java.util.List;

/**
 * @author Nelson
 *
 */

/*
 * 
 * 专家收益列表
 */
public class ExpertIEListVo {
	private List<ExpertIEVo> expertIEVos;
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
	public List<ExpertIEVo> getExpertIEVos() {
		return expertIEVos;
	}
	public void setExpertIEVos(List<ExpertIEVo> expertIEVos) {
		this.expertIEVos = expertIEVos;
	}
	public int getRowCount() {
		return rowCount;
	}
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
	
}
