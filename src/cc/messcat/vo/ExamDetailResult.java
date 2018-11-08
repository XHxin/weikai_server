package cc.messcat.vo;

import java.util.List;

public class ExamDetailResult extends CommonResult {
	
	private List<ExamDetailVo> examDetailList;

	public List<ExamDetailVo> getExamDetailList() {
		return examDetailList;
	}

	public void setExamDetailList(List<ExamDetailVo> examDetailList) {
		this.examDetailList = examDetailList;
	}

}
