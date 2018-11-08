package cc.messcat.vo;

import java.util.List;

public class CoachVo2 {
	
	private String coachId;
	private String schoolId;
	private String name;
	private String mainPic;
	private String mobile;
	private String star;
	private String smoke;
	private String teachExp;
	private String coachWord;
	private String coachDialect;
	private int hasExamBooking;
	private String accessToken;
	private String schoolName;
	private String schoolLogo;
	private String versionType;//app版本
	private String hobby;//爱好
	private String teachAge;//教龄
	private int isOpenGrouping;//是否开启了分组功能
	private List<CoachGroupingVo> coachGroupingVos;
	private String coachAesId;//加密过的教练id
	private String headImg;//教练新头像（七牛云）
	
	public String getCoachId() {
		return coachId;
	}
	public void setCoachId(String coachId) {
		this.coachId = coachId;
	}
	public String getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMainPic() {
		return mainPic;
	}
	public void setMainPic(String mainPic) {
		this.mainPic = mainPic;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getStar() {
		return star;
	}
	public void setStar(String star) {
		this.star = star;
	}
	public String getSmoke() {
		return smoke;
	}
	public void setSmoke(String smoke) {
		this.smoke = smoke;
	}
	public String getTeachExp() {
		return teachExp;
	}
	public void setTeachExp(String teachExp) {
		this.teachExp = teachExp;
	}
	public String getCoachWord() {
		return coachWord;
	}
	public void setCoachWord(String coachWord) {
		this.coachWord = coachWord;
	}
	public String getCoachDialect() {
		return coachDialect;
	}
	public void setCoachDialect(String coachDialect) {
		this.coachDialect = coachDialect;
	}
	public int getHasExamBooking() {
		return hasExamBooking;
	}
	public void setHasExamBooking(int hasExamBooking) {
		this.hasExamBooking = hasExamBooking;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	public String getSchoolLogo() {
		return schoolLogo;
	}
	public void setSchoolLogo(String schoolLogo) {
		this.schoolLogo = schoolLogo;
	}
	public String getVersionType() {
		return versionType;
	}
	public void setVersionType(String versionType) {
		this.versionType = versionType;
	}
	public String getHobby() {
		return hobby;
	}
	public void setHobby(String hobby) {
		this.hobby = hobby;
	}
	public String getTeachAge() {
		return teachAge;
	}
	public void setTeachAge(String teachAge) {
		this.teachAge = teachAge;
	}
	public int getIsOpenGrouping() {
		return isOpenGrouping;
	}
	public void setIsOpenGrouping(int isOpenGrouping) {
		this.isOpenGrouping = isOpenGrouping;
	}
	public List<CoachGroupingVo> getCoachGroupingVos() {
		return coachGroupingVos;
	}
	public void setCoachGroupingVos(List<CoachGroupingVo> coachGroupingVos) {
		this.coachGroupingVos = coachGroupingVos;
	}
	public String getCoachAesId() {
		return coachAesId;
	}
	public void setCoachAesId(String coachAesId) {
		this.coachAesId = coachAesId;
	}
	public String getHeadImg() {
		return headImg;
	}
	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

}
