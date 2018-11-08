package cc.messcat.bases;

import java.io.Serializable;
import java.util.List;

import cc.messcat.dao.collection.EpColumnDao;
import cc.messcat.dao.collection.EpNewsDao;
import cc.messcat.dao.ebusiness.EBusinessInfoDao;
import cc.messcat.dao.ebusiness.EBusinessProductDao;
import cc.messcat.dao.live.LiveDao;
import cc.messcat.dao.member.*;
import cc.messcat.dao.pay.PayDao;
import cc.messcat.dao.payconsult.ExpertClassifyDao;
import cc.messcat.dao.payconsult.HotReplyDao;
import cc.messcat.dao.report.AuthenticationDao;
import cc.messcat.dao.report.CertificationAuthorityDao;
import cc.messcat.dao.report.CertificationReportDao;
import cc.messcat.dao.report.LegalDao;
import cc.messcat.dao.report.NationalDifferencesDao;
import cc.messcat.dao.report.ProductDao;
import cc.messcat.dao.report.RegionDao;
import cc.messcat.dao.report.StandardBaseDao;
import cc.messcat.dao.report.StandardDao;
import cc.messcat.dao.sms.SmsDao;
import cc.messcat.dao.standardread.ExpertDao;
import cc.messcat.dao.standardread.QualityShareDao;
import cc.messcat.dao.standardread.StandardMadeDao;
import cc.messcat.dao.standardread.StandardReadDao;
import cc.messcat.dao.style.WebSkinDao;
import cc.messcat.dao.system.*;

public class BaseManagerDaoImpl implements BaseManagerDao, Serializable {

	private static final long serialVersionUID = -2308147651026388956L;
	private BaseDao baseDao;
	// 新闻模块
	protected EpNewsDao epNewsDao;// 新闻
	protected EpColumnDao epColumnDao;// 栏目
	// 个人中心模块
	protected MemberDao memberDao;// 会员
	protected CollectDao collectDao;// 收藏
	protected BuysRecordDao buysRecordDao;// 购买记录
	protected ExpenseTotalDao expenseTotalDao;//新增钱包功能后，顺便优化BuysRecord表，为了不影响一二期的功能，新建BuysRecordNew
	protected PackagesDao packagesDao;// 会员套餐
	// 准入报告模块
	protected ProductDao productDao;// 产品
	protected RegionDao regionDao;// 地区
	protected AuthenticationDao authenticationDao;// 认证要求
	protected CertificationAuthorityDao certificationAuthorityDao;// 服务机构
	protected LegalDao legalDao;// 法律法规
	protected NationalDifferencesDao nationalDifferencesDao;// 国家差异
	protected StandardDao standardDao;// 标准
	protected StandardBaseDao standardBaseDao;// 标准基础
	protected CertificationReportDao certificationReportDao;  //准入报告
	
	// 标准解读管理模块
	protected StandardMadeDao standardMadeDao; // 标准定制
	protected StandardReadDao standardReadDao; // 标准解读
	protected QualityShareDao qualityShareDao; // 质量分享
	protected ExpertDao expertDao; // 专家
	// 电商信息模块
	protected EBusinessProductDao businessProductDao;
	protected EBusinessInfoDao businessInfoDao;

	protected PayDao payDao;
	protected WebSiteDao webSiteDao;
	protected WebSkinDao webSkinDao;
	protected IntegralDao integralDao;
	protected ExpensePlatDao expensePlatDao;

	protected ExpenseExpertDao expertIEDao;
	protected BankDao bankDao;
	protected WelcomeDao welcomeDao;
	protected AdvertisementDao advertisementDao;
	protected ConsultRecordDao consultRecordDao;
	protected ConsultServerDao consultServerDao;
	protected ActivityExchangeRecordDao activityExchangeRecordDao;
	/*
	 * 付费咨询模块
	 */
	protected ExpertClassifyDao expertClassifyDao;
	protected HotReplyDao hotReplyDao;
	
	//虚拟币记录
    protected VirtualCoinRecordDao virtualCoinRecordDao;
    
    //系统消息
    protected SystemMessageDao systemMessageDao;

    //钱包
    protected WalletDao walletDao;
    //直播管理
    protected LiveDao liveDao;
    protected ExpertDivideIntoDao expertDivideIntoDao;
    protected WhiteListDao whiteListDao;
    
    //互亿无线发送回执推送和上行回复推送
    protected SmsDao smsDao;
    protected SystemDao systemDao;
    protected WithdrawRecordDao withdrawRecordDao;
    protected DivideScaleLableDao divideScaleLableDao;

    protected DivideScaleExpertDao divideScaleExpertDao;
    
    public PayDao getPayDao() {
		return payDao;
	}

	public WebSkinDao getWebSkinDao() {
		return webSkinDao;
	}

	public void setWebSkinDao(WebSkinDao webSkinDao) {
		this.webSkinDao = webSkinDao;
	}

	public void setPayDao(PayDao payDao) {
		this.payDao = payDao;
	}

	public BaseManagerDaoImpl() {
	}

	public BaseDao getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}

	public void deleteObject(Long id, String objName) {
		this.baseDao.delete(id, objName);
	}

	@SuppressWarnings("rawtypes")
	public List getAllObjects(String objName) {
		return this.baseDao.getAll(objName);
	}

	public void saveObject(Object obj) {
		this.baseDao.save(obj);
	}

	public void updateObject(Object obj) {
		this.baseDao.update(obj);
	}

	public Object getObjectById(Long id, String objName) {
		return this.baseDao.get(id, objName);
	}

	public EpNewsDao getEpNewsDao() {
		return epNewsDao;
	}

	public void setEpNewsDao(EpNewsDao epNewsDao) {
		this.epNewsDao = epNewsDao;
	}

	public EpColumnDao getEpColumnDao() {
		return epColumnDao;
	}

	public void setEpColumnDao(EpColumnDao epColumnDao) {
		this.epColumnDao = epColumnDao;
	}

	public MemberDao getMemberDao() {
		return memberDao;
	}

	public void setMemberDao(MemberDao memberDao) {
		this.memberDao = memberDao;
	}

	public CollectDao getCollectDao() {
		return collectDao;
	}

	public void setCollectDao(CollectDao collectDao) {
		this.collectDao = collectDao;
	}

	public BuysRecordDao getBuysRecordDao() {
		return buysRecordDao;
	}

	public void setBuysRecordDao(BuysRecordDao buysRecordDao) {
		this.buysRecordDao = buysRecordDao;
	}

	public ProductDao getProductDao() {
		return productDao;
	}

	public void setProductDao(ProductDao productDao) {
		this.productDao = productDao;
	}

	public RegionDao getRegionDao() {
		return regionDao;
	}

	public void setRegionDao(RegionDao regionDao) {
		this.regionDao = regionDao;
	}

	public AuthenticationDao getAuthenticationDao() {
		return authenticationDao;
	}

	public void setAuthenticationDao(AuthenticationDao authenticationDao) {
		this.authenticationDao = authenticationDao;
	}

	public CertificationAuthorityDao getCertificationAuthorityDao() {
		return certificationAuthorityDao;
	}

	public void setCertificationAuthorityDao(CertificationAuthorityDao certificationAuthorityDao) {
		this.certificationAuthorityDao = certificationAuthorityDao;
	}

	public LegalDao getLegalDao() {
		return legalDao;
	}

	public void setLegalDao(LegalDao legalDao) {
		this.legalDao = legalDao;
	}

	public NationalDifferencesDao getNationalDifferencesDao() {
		return nationalDifferencesDao;
	}

	public void setNationalDifferencesDao(NationalDifferencesDao nationalDifferencesDao) {
		this.nationalDifferencesDao = nationalDifferencesDao;
	}

	public StandardDao getStandardDao() {
		return standardDao;
	}

	public void setStandardDao(StandardDao standardDao) {
		this.standardDao = standardDao;
	}

	public StandardBaseDao getStandardBaseDao() {
		return standardBaseDao;
	}

	public void setStandardBaseDao(StandardBaseDao standardBaseDao) {
		this.standardBaseDao = standardBaseDao;
	}

	public StandardMadeDao getStandardMadeDao() {
		return standardMadeDao;
	}

	public void setStandardMadeDao(StandardMadeDao standardMadeDao) {
		this.standardMadeDao = standardMadeDao;
	}

	public PackagesDao getPackagesDao() {
		return packagesDao;
	}

	public void setPackagesDao(PackagesDao packagesDao) {
		this.packagesDao = packagesDao;
	}

	public StandardReadDao getStandardReadDao() {
		return standardReadDao;
	}

	public void setStandardReadDao(StandardReadDao standardReadDao) {
		this.standardReadDao = standardReadDao;
	}

	public QualityShareDao getQualityShareDao() {
		return qualityShareDao;
	}

	public void setQualityShareDao(QualityShareDao qualityShareDao) {
		this.qualityShareDao = qualityShareDao;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public ExpertDao getExpertDao() {
		return expertDao;
	}

	public void setExpertDao(ExpertDao expertDao) {
		this.expertDao = expertDao;
	}

	public EBusinessProductDao getBusinessProductDao() {
		return businessProductDao;
	}

	public void setBusinessProductDao(EBusinessProductDao businessProductDao) {
		this.businessProductDao = businessProductDao;
	}

	public EBusinessInfoDao getBusinessInfoDao() {
		return businessInfoDao;
	}

	public void setBusinessInfoDao(EBusinessInfoDao businessInfoDao) {
		this.businessInfoDao = businessInfoDao;
	}

	public WebSiteDao getWebSiteDao() {
		return webSiteDao;
	}

	public void setWebSiteDao(WebSiteDao webSiteDao) {
		this.webSiteDao = webSiteDao;
	}

	public IntegralDao getIntegralDao() {
		return integralDao;
	}

	public void setIntegralDao(IntegralDao integralDao) {
		this.integralDao = integralDao;
	}

	public ExpensePlatDao getExpensePlatDao() {
		return expensePlatDao;
	}

	public void setExpensePlatDao(ExpensePlatDao expensePlatDao) {
		this.expensePlatDao = expensePlatDao;
	}

	public ExpenseExpertDao getExpertIEDao() {
		return expertIEDao;
	}

	public void setExpertIEDao(ExpenseExpertDao expertIEDao) {
		this.expertIEDao = expertIEDao;
	}

	public BankDao getBankDao() {
		return bankDao;
	}

	public void setBankDao(BankDao bankDao) {
		this.bankDao = bankDao;
	}

	public WelcomeDao getWelcomeDao() {
		return welcomeDao;
	}

	public void setWelcomeDao(WelcomeDao welcomeDao) {
		this.welcomeDao = welcomeDao;
	}
	

	public ExpertClassifyDao getExpertClassifyDao() {
		return expertClassifyDao;
	}

	public void setExpertClassifyDao(ExpertClassifyDao expertClassifyDao) {
		this.expertClassifyDao = expertClassifyDao;
	}

	public HotReplyDao getHotReplyDao() {
		return hotReplyDao;
	}

	public void setHotReplyDao(HotReplyDao hotReplyDao) {
		this.hotReplyDao = hotReplyDao;
	}

	public AdvertisementDao getAdvertisementDao() {
		return advertisementDao;
	}

	public void setAdvertisementDao(AdvertisementDao advertisementDao) {
		this.advertisementDao = advertisementDao;
	}

	public VirtualCoinRecordDao getVirtualCoinRecordDao() {
		return virtualCoinRecordDao;
	}

	public void setVirtualCoinRecordDao(VirtualCoinRecordDao virtualCoinRecordDao) {
		this.virtualCoinRecordDao = virtualCoinRecordDao;
	}

	public ConsultRecordDao getConsultRecordDao() {
		return consultRecordDao;
	}

	public void setConsultRecordDao(ConsultRecordDao consultRecordDao) {
		this.consultRecordDao = consultRecordDao;
	}

	public ConsultServerDao getConsultServerDao() {
		return consultServerDao;
	}

	public void setConsultServerDao(ConsultServerDao consultServerDao) {
		this.consultServerDao = consultServerDao;
	}

	public SystemMessageDao getSystemMessageDao() {
		return systemMessageDao;
	}

	public void setSystemMessageDao(SystemMessageDao systemMessageDao) {
		this.systemMessageDao = systemMessageDao;
	}

	public ActivityExchangeRecordDao getActivityExchangeRecordDao() {
		return activityExchangeRecordDao;
	}

	public void setActivityExchangeRecordDao(ActivityExchangeRecordDao activityExchangeRecordDao) {
		this.activityExchangeRecordDao = activityExchangeRecordDao;
	}

	public ExpenseTotalDao getExpenseTotalDao() {
		return expenseTotalDao;
	}

	public void setExpenseTotalDao(ExpenseTotalDao expenseTotalDao) {
		this.expenseTotalDao = expenseTotalDao;
	}

	public WalletDao getWalletDao() {
		return walletDao;
	}

	public void setWalletDao(WalletDao walletDao) {
		this.walletDao = walletDao;
	}

	public LiveDao getLiveDao() {
		return liveDao;
	}

	public void setLiveDao(LiveDao liveDao) {
		this.liveDao = liveDao;
	}

	public ExpertDivideIntoDao getExpertDivideIntoDao() {
		return expertDivideIntoDao;
	}

	public void setExpertDivideIntoDao(ExpertDivideIntoDao expertDivideIntoDao) {
		this.expertDivideIntoDao = expertDivideIntoDao;
	}

	public CertificationReportDao getCertificationReportDao() {
		return certificationReportDao;
	}

	public void setCertificationReportDao(CertificationReportDao certificationReportDao) {
		this.certificationReportDao = certificationReportDao;
	}

	public WhiteListDao getWhiteListDao() {
		return whiteListDao;
	}

	public void setWhiteListDao(WhiteListDao whiteListDao) {
		this.whiteListDao = whiteListDao;
	}

	public SmsDao getSmsDao() {
		return smsDao;
	}

	public void setSmsDao(SmsDao smsDao) {
		this.smsDao = smsDao;
	}

	public SystemDao getSystemDao() {
		return systemDao;
	}

	public void setSystemDao(SystemDao systemDao) {
		this.systemDao = systemDao;
	}

	public WithdrawRecordDao getWithdrawRecordDao() {
		return withdrawRecordDao;
	}

	public void setWithdrawRecordDao(WithdrawRecordDao withdrawRecordDao) {
		this.withdrawRecordDao = withdrawRecordDao;
	}

	public DivideScaleExpertDao getDivideScaleExpertDao() {
		return divideScaleExpertDao;
	}

	public void setDivideScaleExpertDao(DivideScaleExpertDao divideScaleExpertDao) {
		this.divideScaleExpertDao = divideScaleExpertDao;
	}

	public DivideScaleLableDao getDivideScaleLableDao() {
		return divideScaleLableDao;
	}

	public void setDivideScaleLableDao(DivideScaleLableDao divideScaleLableDao) {
		this.divideScaleLableDao = divideScaleLableDao;
	}
}