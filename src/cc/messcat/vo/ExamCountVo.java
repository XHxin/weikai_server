package cc.messcat.vo;

/**
 * 教练绩效科目考试统计
 * @author StevenWang
 *
 */
public class ExamCountVo {
	
	private String examName;
	private int firExamPass;
	private int firExamTotal;
	private int secExamPass;
	private int secExamTotal;
	
	public String getExamName() {
		return examName;
	}
	public void setExamName(String examName) {
		this.examName = examName;
	}
	public int getFirExamPass() {
		return firExamPass;
	}
	public void setFirExamPass(int firExamPass) {
		this.firExamPass = firExamPass;
	}
	public int getFirExamTotal() {
		return firExamTotal;
	}
	public void setFirExamTotal(int firExamTotal) {
		this.firExamTotal = firExamTotal;
	}
	public int getSecExamPass() {
		return secExamPass;
	}
	public void setSecExamPass(int secExamPass) {
		this.secExamPass = secExamPass;
	}
	public int getSecExamTotal() {
		return secExamTotal;
	}
	public void setSecExamTotal(int secExamTotal) {
		this.secExamTotal = secExamTotal;
	}

}
