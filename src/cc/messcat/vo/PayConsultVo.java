package cc.messcat.vo;

import java.math.BigDecimal;

/**
 * @author HASEE
 *付费咨询-他的问答
 */
public class PayConsultVo {

	private Long replyId;           //回复表Id
	private String photo;			//专家头像
	private String expertId;        //专家Id
	private String expert;			//专家名字
	private String problem;			//问题
	private String contentCode;     //内容代号(由三位数字组成,百位上代表音频,十位代表图片,个位代表文字; 0代表没有该类型的内容,1代表有内容)
	private BigDecimal money;			//提问需支付的金额
	private String addTime;			//创建时间
	private int viewSum;			//观看量
    private String collectStatus;   //收藏状态(0代表未收藏,1代表已收藏)
    private String likeStatus;      //点赞状态(0代表未点赞,1代表已点赞)

    public Long getReplyId() {
		return replyId;
	}
	public void setReplyId(Long replyId) {
		this.replyId = replyId;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getExpertId() {
		return expertId;
	}
	public void setExpertId(String expertId) {
		this.expertId = expertId;
	}
	public String getExpert() {
		return expert;
	}
	public void setExpert(String expert) {
		this.expert = expert;
	}
	public String getProblem() {
		return problem;
	}
	public void setProblem(String problem) {
		this.problem = problem;
	}
	public String getContentCode() {
		return contentCode;
	}
	public void setContentCode(String contentCode) {
		this.contentCode = contentCode;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public String getAddTime() {
		return addTime;
	}
	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}
	public int getViewSum() {
		return viewSum;
	}
	public void setViewSum(int viewSum) {
		this.viewSum = viewSum;
	}
	public String getCollectStatus() {
		return collectStatus;
	}
	public void setCollectStatus(String collectStatus) {
		this.collectStatus = collectStatus;
	}
	public String getLikeStatus() {
		return likeStatus;
	}
	public void setLikeStatus(String likeStatus) {
		this.likeStatus = likeStatus;
	}

    
}
