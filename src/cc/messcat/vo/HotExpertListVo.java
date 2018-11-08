package cc.messcat.vo;

public class HotExpertListVo {

	private String expertId;  //专家id
	private String name;	  //专家名称
	private String photo;	  //专家头像
	private String intro;	  //专家的个人介绍
	private int readingSum;   //专家的解读数
	private int attentionSum; //专家的被关注数
	private int answerSum;     //他的回答量
	private String field;		//专业领域
	private String profession;	//职称
	private String workYear;	//工作年限
	private String school;		//毕业学校
	private String major;		//所学专业
	private String company;		//公司名称
	private String position;	//职务
	
	private String attentionStatus;   //0代表未关注，1代表已关注
	private int likeSum;              //点赞总数
	private String likeTime;		  //点赞时间
	private String shareURL;	//分享URL
	private int money;
    private int privateMoney;
	
	public String getExpertId() {
		return expertId;
	}
	public void setExpertId(String expertId) {
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
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public int getReadingSum() {
		return readingSum;
	}
	public void setReadingSum(int readingSum) {
		this.readingSum = readingSum;
	}
	public int getAttentionSum() {
		return attentionSum;
	}
	public void setAttentionSum(int attentionSum) {
		this.attentionSum = attentionSum;
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
	public String getAttentionStatus() {
		return attentionStatus;
	}
	public void setAttentionStatus(String attentionStatus) {
		this.attentionStatus = attentionStatus;
	}
	public int getLikeSum() {
		return likeSum;
	}
	public void setLikeSum(int likeSum) {
		this.likeSum = likeSum;
	}
	public String getLikeTime() {
		return likeTime;
	}
	public void setLikeTime(String likeTime) {
		this.likeTime = likeTime;
	}
	public String getShareURL() {
		return shareURL;
	}
	public void setShareURL(String shareURL) {
		this.shareURL = shareURL;
	}
	public int getAnswerSum() {
		return answerSum;
	}
	public void setAnswerSum(int answerSum) {
		this.answerSum = answerSum;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public int getPrivateMoney() {
		return privateMoney;
	}
	public void setPrivateMoney(int privateMoney) {
		this.privateMoney = privateMoney;
	}
	
}
