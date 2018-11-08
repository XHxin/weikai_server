package cc.messcat.vo;

import java.math.BigDecimal;

public class HotReplyVo {

	private Long replyId;           //回复表Id
	private String photo;			//专家头像
	private String expertId;        //专家Id
	private String expert;			//专家名字
	private String memberName;      //提问者的名字
	private Long problemId;			//提问表Id
	private String problem;			//问题
	private String contentCode;     //内容代号(由三位数字组成,百位上代表音频,十位代表图片,个位代表文字; 0代表没有该类型的内容,1代表有内容)
	private BigDecimal money;			//提问需支付的金额
	private String payStatus;       //支付状态(0代表未付款,1代表付款)
	private String addTime;			//创建时间
	private int viewSum;			//观看量
	private int replyLikedSum;		//多少人觉得值
	private String shareURL;		//分享URL
    private String collectStatus;   //收藏状态(0代表未收藏,1代表已收藏)
    private String likeStatus;      //点赞状态(0代表未点赞,1代表已点赞)
    private String classify;        //专家分类信息
	
	public Long getReplyId() {
		return replyId;
	}
	public void setReplyId(Long id) {
		this.replyId = id;
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
	public String getProblem() {
		return problem;
	}
	public void setProblem(String problemName) {
		this.problem = problemName;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getExpert() {
		return expert;
	}
	public void setExpert(String expert) {
		this.expert = expert;
	}
	public int getViewSum() {
		return viewSum;
	}
	public void setViewSum(int heardSum) {
		this.viewSum = heardSum;
	}
	public int getReplyLikedSum() {
		return replyLikedSum;
	}
	public void setReplyLikedSum(int likedSum) {
		this.replyLikedSum = likedSum;
	}
	public Long getProblemId() {
		return problemId;
	}
	public void setProblemId(Long problemId) {
		this.problemId = problemId;
	}
	public String getCollectStatus() {
		return collectStatus;
	}
	public void setCollectStatus(String collectStatus) {
		this.collectStatus = collectStatus;
	}
	public String getContentCode() {
		return contentCode;
	}
	public void setContentCode(String contentCode) {
		this.contentCode = contentCode;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public String getClassify() {
		return classify;
	}
	public void setClassify(String classify) {
		this.classify = classify;
	}
	public String getExpertId() {
		return expertId;
	}
	public void setExpertId(String expertId) {
		this.expertId = expertId;
	}
	public String getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}
	public String getShareURL() {
		return shareURL;
	}
	public void setShareURL(String shareURL) {
		this.shareURL = shareURL;
	}
	public String getLikeStatus() {
		return likeStatus;
	}
	public void setLikeStatus(String likeStatus) {
		this.likeStatus = likeStatus;
	}
}
