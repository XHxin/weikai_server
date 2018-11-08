package cc.messcat.vo;


import java.math.BigDecimal;

public class HotReplyPayVo {
	
	private String expertName;     //提问者的名字
	private String addTime;        //提问时间
	private String problem;		   //问题
	private String isReply;		   //是否回答(true代表已回答,false代表未回答)
	private String replyId;		   //若是已经回答,则可以通过这个Id去访问详情
	private BigDecimal money;          //提问价格
	private String replyStatus;    //0为未回答,1为问题已失效,2为问题已答,3为围观问题
	
	public String getExpertName() {
		return expertName;
	}
	public void setExpertName(String expertName) {
		this.expertName = expertName;
	}
	public String getProblem() {
		return problem;
	}
	public String getAddTime() {
		return addTime;
	}
	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}
	public void setProblem(String problem) {
		this.problem = problem;
	}
	public String getIsReply() {
		return isReply;
	}
	public void setIsReply(String isReply) {
		this.isReply = isReply;
	}
	public String getReplyId() {
		return replyId;
	}
	public void setReplyId(String replyId) {
		this.replyId = replyId;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public String getReplyStatus() {
		return replyStatus;
	}
	public void setReplyStatus(String replyStatus) {
		this.replyStatus = replyStatus;
	}
}
