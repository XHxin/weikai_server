package cc.messcat.entity;

import cc.modules.util.TokenProccessor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="member")
public class Member
        implements Serializable
{

    @Id
    @GeneratedValue
    private Long id;

    @Column(name="openID")
    private String openId;
    private String mobile;
    private String realname;
    private String role;

    @Column(name="expert_check_status")
    private String expertCheckStatus;
    private String sex;
    private String email;
    private String address;

    @Column(name="regist_source")
    private String registSource;

    @Column(name="regist_time")
    private Date registTime;

    @Column(name="edit_time")
    private Date editTime;
    private String photo;

    @Column(name="image_path")
    private String imagePath;
    private String status;

    @Column(name="access_token")
    private String accessToken;

    @Column(name="is_sync")
    private String isSync;
    private String company;
    private String job;

    @Column(name="init_time")
    private Date initTime;

    @Column(name="start_time")
    private Date startTime;
    private String grade;
    private String type;

    @Column(name="year_end_time")
    private Date yearEndTime;

    @Column(name="end_time")
    private Date endTime;
    private String field;
    private String school;
    private String major;
    private String profession;
    private String position;

    @Column(name="work_years")
    private String workYears;
    private String intro;

    @Column(name="work_card")
    private String workCard;

    @Column(name="visit_card")
    private String visitCard;

    @Column(name="idcard_front")
    private String idcardFront;

    @Column(name="idcard_back")
    private String idcardBack;

    @Column(name="app_version")
    private String appVersion;
    private String terminal;

    @Column(name="bankID")
    private Long bankId;
    private BigDecimal income;

    @Column(name="can_withdraw")
    private BigDecimal canWithdraw;
    private BigDecimal integral;

    @Column(name="virtual_coin")
    private BigDecimal virtualCoin;

    @Column(name="is_showIndex")
    private String isShowIndex;

    @Column(name="easemob_user_name")
    private String easemobUserName;

    @Column(name="easemob_password")
    private String easemobPassword;
    private Long recommender_id;
    private int recommend_times;

    @Column(name="uuid")
    private String uuid;

    @Column(name="tourist")
    private String tourist;

    @Column(name="is_remind")
    private Integer isRemind;

    @Column(name="old_login_time")
    private Date oldLoginTime;

    @Column(name="old_login_ip")
    private String oldLoginIp;

    @Column(name="login_time")
    private Date loginTime;

    @Column(name="login_ip")
    private String loginIp;

    @ManyToMany
    @JoinTable(name="expert_classify_member", joinColumns={@javax.persistence.JoinColumn(name="memberID")}, inverseJoinColumns={@javax.persistence.JoinColumn(name="classifyID")})
    private List<ExpertClassify> classifys = new ArrayList();

    public Member()
    {
    }

    public Member(String mobile, String role)
    {
        this.mobile = mobile;
        this.role = role;
        this.openId = "";
        this.expertCheckStatus = "0";
        this.grade = "0";
        this.type = "2";
        this.realname = mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        this.sex = "2";
        this.email = "";
        this.address = "";
        this.registSource = "1";
        this.photo = "";
        this.imagePath = "";
        this.company = "";
        this.job = "";
        this.field = "";
        this.school = "";
        this.major = "";
        this.profession = "";
        this.position = "";
        this.workYears = "";
        this.intro = "";
        this.workCard = "";
        this.visitCard = "";
        this.idcardFront = "";
        this.idcardBack = "";
        this.income = new BigDecimal(0);
        this.canWithdraw = new BigDecimal(0);
        this.integral = new BigDecimal(0);
        this.virtualCoin = new BigDecimal(0);
        this.isShowIndex = "0";
        this.tourist = "0";
        this.endTime = new Date();
        this.initTime = new Date();
        this.loginTime = new Date();
        this.startTime = new Date();
        this.registTime = new Date();
        this.yearEndTime = new Date();
        this.editTime = new Date();
        this.bankId = Long.valueOf(0L);
        this.isRemind = Integer.valueOf(0);
        this.status = "1";
        this.isSync = ((mobile == null) || (mobile.isEmpty()) ? "0" : "1");
        this.accessToken = TokenProccessor.getInstance().makeToken();
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOpenId() {
        return this.openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getSex() {
        return this.sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getRealname() {
        return this.realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getRegistTime() {
        return this.registTime;
    }

    public void setRegistTime(Date registTime) {
        this.registTime = registTime;
    }

    public Date getEditTime() {
        return this.editTime;
    }

    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }

    public String getPhoto() {
        return this.photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getImagePath() {
        return this.imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getIsSync() {
        return this.isSync;
    }

    public void setIsSync(String isSync) {
        this.isSync = isSync;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGrade() {
        return this.grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getCompany() {
        return this.company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getJob() {
        return this.job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public Date getInitTime() {
        return this.initTime;
    }

    public void setInitTime(Date initTime) {
        this.initTime = initTime;
    }

    public Date getStartTime() {
        return this.startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return this.endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getYearEndTime() {
        return this.yearEndTime;
    }

    public void setYearEndTime(Date yearEndTime) {
        this.yearEndTime = yearEndTime;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getField() {
        return this.field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getSchool() {
        return this.school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getMajor() {
        return this.major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getProfession() {
        return this.profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getPosition() {
        return this.position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getWorkYears() {
        return this.workYears;
    }

    public void setWorkYears(String workYears) {
        this.workYears = workYears;
    }

    public String getIntro() {
        return this.intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getWorkCard() {
        return this.workCard;
    }

    public void setWorkCard(String workCard) {
        this.workCard = workCard;
    }

    public String getVisitCard() {
        return this.visitCard;
    }

    public void setVisitCard(String visitCard) {
        this.visitCard = visitCard;
    }

    public String getIdcardFront() {
        return this.idcardFront;
    }

    public void setIdcardFront(String idcardFront) {
        this.idcardFront = idcardFront;
    }

    public String getIdcardBack() {
        return this.idcardBack;
    }

    public void setIdcardBack(String idcardBack) {
        this.idcardBack = idcardBack;
    }

    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRegistSource() {
        return this.registSource;
    }

    public void setRegistSource(String registSource) {
        this.registSource = registSource;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Date getOldLoginTime() {
        return this.oldLoginTime;
    }

    public void setOldLoginTime(Date oldLoginTime) {
        this.oldLoginTime = oldLoginTime;
    }

    public String getOldLoginIp() {
        return this.oldLoginIp;
    }

    public void setOldLoginIp(String oldLoginIp) {
        this.oldLoginIp = oldLoginIp;
    }

    public Date getLoginTime() {
        return this.loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public String getLoginIp() {
        return this.loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public String getAppVersion() {
        return this.appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getTerminal() {
        return this.terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getExpertCheckStatus() {
        return this.expertCheckStatus;
    }

    public void setExpertCheckStatus(String expertCheckStatus) {
        this.expertCheckStatus = expertCheckStatus;
    }

    public Long getBankId() {
        return this.bankId;
    }

    public void setBankId(Long bankId) {
        this.bankId = bankId;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public BigDecimal getCanWithdraw() {
        return canWithdraw;
    }

    public void setCanWithdraw(BigDecimal canWithdraw) {
        this.canWithdraw = canWithdraw;
    }

    public BigDecimal getVirtualCoin() {
        return virtualCoin;
    }

    public void setVirtualCoin(BigDecimal virtualCoin) {
        this.virtualCoin = virtualCoin;
    }

    public BigDecimal getIntegral() {
        return this.integral;
    }

    public void setIntegral(BigDecimal integral) {
        this.integral = integral;
    }


    public String getIsShowIndex() {
        return this.isShowIndex;
    }

    public void setIsShowIndex(String isShowIndex) {
        this.isShowIndex = isShowIndex;
    }

    public String getEasemobUserName() {
        return this.easemobUserName;
    }

    public void setEasemobUserName(String easemobUserName) {
        this.easemobUserName = easemobUserName;
    }

    public String getEasemobPassword() {
        return this.easemobPassword;
    }

    public void setEasemobPassword(String easemobPassword) {
        this.easemobPassword = easemobPassword;
    }

    public Long getRecommender_id() {
        return this.recommender_id;
    }

    public void setRecommender_id(Long recommender_id) {
        this.recommender_id = recommender_id;
    }

    public int getRecommend_times() {
        return this.recommend_times;
    }

    public void setRecommend_times(int recommend_times) {
        this.recommend_times = recommend_times;
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTourist() {
        return this.tourist;
    }

    public void setTourist(String tourist) {
        this.tourist = tourist;
    }

    public List<ExpertClassify> getClassifys() {
        return this.classifys;
    }

    public void setClassifys(List<ExpertClassify> classifys) {
        this.classifys = classifys;
    }

    public Integer getIsRemind() {
        return this.isRemind;
    }

    public void setIsRemind(Integer isRemind) {
        this.isRemind = isRemind;
    }
}