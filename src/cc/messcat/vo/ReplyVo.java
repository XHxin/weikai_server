package cc.messcat.vo;

public class ReplyVo {
	
	private Long replyId;//回复表的id
	private String expertName;//专家的姓名
	//private Double money;//提问专家需要的钱
	//private int replyLikedSum;//多少人觉得值
	//private int viewSum;//观看量
	private String problem;//问题
	private String answerTime;//回答问题的时间
	private String picture;//专家的头像
	public Long getReplyId() {
		return replyId;
	}
	public void setReplyId(Long replyId) {
		this.replyId = replyId;
	}
	public String getExpertName() {
		return expertName;
	}
	public void setExpertName(String expertName) {
		this.expertName = expertName;
	}
//	public Double getMoney() {
//		return money;
//	}
//	public void setMoney(Double money) {
//		this.money = money;
//	}
//	public int getReplyLikedSum() {
//		return replyLikedSum;
//	}
//	public void setReplyLikedSum(int replyLikedSum) {
//		this.replyLikedSum = replyLikedSum;
//	}
//	public int getViewSum() {
//		return viewSum;
//	}
//	public void setViewSum(int viewSum) {
//		this.viewSum = viewSum;
//	}
	public String getProblem() {
		return problem;
	}
	public void setProblem(String problem) {
		this.problem = problem;
	}
	public String getAnswerTime() {
		return answerTime;
	}
	public void setAnswerTime(String answerTime) {
		this.answerTime = answerTime;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	
	
}
