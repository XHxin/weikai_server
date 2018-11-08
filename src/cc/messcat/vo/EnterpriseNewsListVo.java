package cc.messcat.vo;

import java.util.Date;
import java.util.List;

public class EnterpriseNewsListVo {

	private int pageNo;
	
	private int pageSize;
	
	private int rowCount;
	
	private List<EnterpriseNewsVo> enterpriseNewsVo;

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

	public List<EnterpriseNewsVo> getEnterpriseNewsVo() {
		return enterpriseNewsVo;
	}

	public void setEnterpriseNewsVo(List<EnterpriseNewsVo> enterpriseNewsVo) {
		this.enterpriseNewsVo = enterpriseNewsVo;
	}

	public int getRowCount() {
		return rowCount;
	}

	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
	
}
