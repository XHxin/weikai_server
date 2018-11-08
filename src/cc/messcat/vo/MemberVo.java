package cc.messcat.vo;

import java.util.List;

import cc.messcat.entity.Bank;

public class MemberVo {
	
	private Long memberId;//会员id
	private String mobile;//手机号
	private int grade;//等级（0：普通会员 1：VIP会员）
	private int role;//会员角色（1：用户 2：专家）
	private int expertCheckStatus; //专家审核状态（0：未审核  1：审核通过  2：审核不通过）
	private String accessToken;//登录token
	private String versionType;//app版本
	private int type;//VIP会员类型 0：年费  1：月费 2:非会员
	private String photo;//头像
	private String realname;//用户名
	private String job;//职业
	private String company;//所在企业
	private String address;//地址
	private String endTime;//VIP会员到期时间
	private String field;//专业领域
	private String email;//邮箱
	private String school;//毕业学校
	private String major;//所学专业
	private String intro;//个人简介
	private String bankCard;//银行卡号
	private String cardholder;//银行卡持卡人
	private String openBank;//银行卡开户行
	private String bankMobile;//银行预留手机号
	private String profession;//职称
	private String position;//职务
	private String workCard ;//工卡照
	private String visitCard ;//名片照
	private String idcardFront ;//身份证正面照
	private String idcardBack ;//身份证背面照
	private String workYears;//工作年限
	private String uuid;
	private String tourist;//


	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public int getExpertCheckStatus() {
		return expertCheckStatus;
	}

	public void setExpertCheckStatus(int expertCheckStatus) {
		this.expertCheckStatus = expertCheckStatus;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getVersionType() {
		return versionType;
	}

	public void setVersionType(String versionType) {
		this.versionType = versionType;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}
	public String getBankCard() {
		return bankCard;
	}

	public void setBankCard(String bankCard) {
		this.bankCard = bankCard;
	}

	public String getCardholder() {
		return cardholder;
	}

	public void setCardholder(String cardholder) {
		this.cardholder = cardholder;
	}

	public String getOpenBank() {
		return openBank;
	}

	public void setOpenBank(String openBank) {
		this.openBank = openBank;
	}

	public String getBankMobile() {
		return bankMobile;
	}

	public void setBankMobile(String bankMobile) {
		this.bankMobile = bankMobile;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getWorkCard() {
		return workCard;
	}

	public void setWorkCard(String workCard) {
		this.workCard = workCard;
	}

	public String getVisitCard() {
		return visitCard;
	}

	public void setVisitCard(String visitCard) {
		this.visitCard = visitCard;
	}

	public String getIdcardFront() {
		return idcardFront;
	}

	public void setIdcardFront(String idcardFront) {
		this.idcardFront = idcardFront;
	}

	public String getIdcardBack() {
		return idcardBack;
	}

	public void setIdcardBack(String idcardBack) {
		this.idcardBack = idcardBack;
	}

	public String getWorkYears() {
		return workYears;
	}

	public void setWorkYears(String workYears) {
		this.workYears = workYears;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getTourist() {
		return tourist;
	}

	public void setTourist(String tourist) {
		this.tourist = tourist;
	}
}
