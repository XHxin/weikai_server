package cc.messcat.vo;

import java.util.List;

public class StuExamInfoResult extends CommonResult {
	
	private List<StuExamInfo> stuExamInfoList;
	private int pageCount;

	public List<StuExamInfo> getStuExamInfoList() {
		return stuExamInfoList;
	}

	public void setStuExamInfoList(List<StuExamInfo> stuExamInfoList) {
		this.stuExamInfoList = stuExamInfoList;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}


}
