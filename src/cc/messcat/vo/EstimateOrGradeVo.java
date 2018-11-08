package cc.messcat.vo;

public class EstimateOrGradeVo {
	
	
	private String isEstimate;// 1为已评价 0为未评价        
	private String isGrade;  // 1为已打星 0为未打星
	
	public String getIsEstimate() {
		return isEstimate;
	}
	public void setIsEstimate(String isEstimate) {
		this.isEstimate = isEstimate;
	}
	public String getIsGrade() {
		return isGrade;
	}
	public void setIsGrade(String isGrade) {
		this.isGrade = isGrade;
	}
	
	
}
