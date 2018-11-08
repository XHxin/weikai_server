package cc.messcat.vo;

import java.math.BigDecimal;

public class PayConsultExpertVo {
	
	private Long expertId;   //专家Id
	private String name;     //专家名字
	private String photo;    //专家头像
	private String attentionStatus;  //关注状态(0:未关注,1:已关注)
    private BigDecimal money;	 		//提问需支付的金额
    private BigDecimal privateMoney;    //私密提问价格
    private int readSum;	 //解读总数(IOS专用)
    private int readingSum;  //解读总数(Android专用)
    private int answerSum;	 //回答总数
    private int attentionSum; //关注总数
    private int LikeSum;	  //点赞数量
    private String likeTime;  //点赞时间
    
	private String field;		//专业领域
	private String profession;	//职称
	private String workYear;	//工作年限
	private String school;		//毕业学校
	private String major;		//所学专业
	private String company;		//公司名称
	private String position;	//职务
    private String intro;	 	//个人简介
	private String shareURL;	//分享URL
    
	public Long getExpertId() {
		return expertId;
	}
	public void setExpertId(Long expertId) {
		this.expertId = expertId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String phone) {
		this.photo = phone;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public int getAnswerSum() {
		return answerSum;
	}
	public void setAnswerSum(int answerSum) {
		this.answerSum = answerSum;
	}
	public int getAttentionSum() {
		return attentionSum;
	}
	public void setAttentionSum(int attentionSum) {
		this.attentionSum = attentionSum;
	}
	public String getAttentionStatus() {
		return attentionStatus;
	}
	public void setAttentionStatus(String attentionStatus) {
		this.attentionStatus = attentionStatus;
	}
	public int getReadSum() {
		return readSum;
	}
	public void setReadSum(int readSum) {
		this.readSum = readSum;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getProfession() {
		return profession;
	}
	public void setProfession(String profession) {
		this.profession = profession;
	}
	public String getWorkYear() {
		return workYear;
	}
	public void setWorkYear(String workYear) {
		this.workYear = workYear;
	}
	public String getSchool() {
		return school;
	}
	public void setSchool(String school) {
		this.school = school;
	}
	public String getMajor() {
		return major;
	}
	public void setMajor(String major) {
		this.major = major;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public int getLikeSum() {
		return LikeSum;
	}
	public void setLikeSum(int likedSum) {
		LikeSum = likedSum;
	}
	public String getLikeTime() {
		return likeTime;
	}
	public void setLikeTime(String likedTime) {
		this.likeTime = likedTime;
	}
	public String getShareURL() {
		return shareURL;
	}
	public void setShareURL(String shareURL) {
		this.shareURL = shareURL;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public BigDecimal getPrivateMoney() {
		return privateMoney;
	}

	public void setPrivateMoney(BigDecimal privateMoney) {
		this.privateMoney = privateMoney;
	}

	public int getReadingSum() {
		return readingSum;
	}
	public void setReadingSum(int readingSum) {
		this.readingSum = readingSum;
	}
}
