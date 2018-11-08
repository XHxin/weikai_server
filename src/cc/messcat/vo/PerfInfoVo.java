package cc.messcat.vo;

import java.util.List;

/**
 * 教练绩效统计
 * @author StevenWang
 *
 */
public class PerfInfoVo {
	
	private String dayTotal;
	private String monthTotal;
	private List<ExamCountVo> examCountList;
	
	public String getDayTotal() {
		return dayTotal;
	}
	public void setDayTotal(String dayTotal) {
		this.dayTotal = dayTotal;
	}
	public String getMonthTotal() {
		return monthTotal;
	}
	public void setMonthTotal(String monthTotal) {
		this.monthTotal = monthTotal;
	}
	public List<ExamCountVo> getExamCountList() {
		return examCountList;
	}
	public void setExamCountList(List<ExamCountVo> examCountList) {
		this.examCountList = examCountList;
	}

}
