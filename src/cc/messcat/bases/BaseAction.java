package cc.messcat.bases;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import cc.messcat.service.member.*;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

import cc.messcat.service.collection.EpColumnManagerDao;
import cc.messcat.service.collection.EpNewsManagerDao;
import cc.messcat.service.ebusiness.EBusinessInfoManagerDao;
import cc.messcat.service.ebusiness.EBusinessProductManagerDao;
import cc.messcat.service.live.LiveService;
import cc.messcat.service.pay.PayManagerDao;
import cc.messcat.service.paycosult.ExpertClassifyService;
import cc.messcat.service.paycosult.HotReplyService;
import cc.messcat.service.report.AuthenticationManagerDao;
import cc.messcat.service.report.CertificationAuthorityManagerDao;
import cc.messcat.service.report.CertificationReportService;
import cc.messcat.service.report.LegalManagerDao;
import cc.messcat.service.report.NationalDifferencesManagerDao;
import cc.messcat.service.report.ProductManagerDao;
import cc.messcat.service.report.RegionManagerDao;
import cc.messcat.service.report.StandardBaseManagerDao;
import cc.messcat.service.report.StandardManagerDao;
import cc.messcat.service.sms.SmsService;
import cc.messcat.service.standardread.ExpertManagerDao;
import cc.messcat.service.standardread.QualityShareManagerDao;
import cc.messcat.service.standardread.StandardMadeManagerDao;
import cc.messcat.service.standardread.StandardReadManagerDao;
import cc.messcat.service.system.AdvertisementManagerDao;
import cc.messcat.service.system.ConsultRecordManagerDao;
import cc.messcat.service.system.ConsultServerManagerDao;
import cc.messcat.service.system.ExpertDivideIntoManagerDao;
import cc.messcat.service.system.ExpenseExpertManagerDao;
import cc.messcat.service.system.ExpensePlatManagerDao;
import cc.messcat.service.system.SystemManagerDao;
import cc.messcat.service.system.SystemMessageManagerDao;
import cc.messcat.service.system.WebSiteManagerDao;
import cc.messcat.service.system.WelcomeManagerDao;

public class BaseAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	protected BaseManagerAction baseManagerAction;
	//新闻模块
	protected EpColumnManagerDao epColumnManagerDao;
	protected EpNewsManagerDao epNewsManagerDao;
	//个人中心模块
	protected MemberManagerDao memberManagerDao;
	protected CollectManagerDao collectManagerDao;
	protected BuysRecordManagerDao buysRecordManagerDao;
	protected PackagesManagerDao packagesManagerDao;
	//准入报告模块
	protected ProductManagerDao productManagerDao;
	protected RegionManagerDao regionManagerDao;
	protected LegalManagerDao legalManagerDao;		// 法律法规
	protected NationalDifferencesManagerDao nationalDifferencesManagerDao;	// 国家差异
	protected StandardManagerDao standardManagerDao;	// 标准
	protected StandardBaseManagerDao standardBaseManagerDao;  // 标准基础
	protected CertificationAuthorityManagerDao certificationAuthorityManagerDao;	// 服务机构
	protected AuthenticationManagerDao authenticationManagerDao;      // 认证要求
	protected CertificationReportService certificationReportService;    //准入报告
	
	//标准解读管理模块
    protected StandardMadeManagerDao standardMadeManagerDao;
    protected StandardReadManagerDao standardReadManagerDao;
    protected QualityShareManagerDao qualityShareManagerDao;
    protected ExpertManagerDao  expertManagerDao;
    //电商信息模块
    protected EBusinessProductManagerDao businessProductManagerDao;
    protected EBusinessInfoManagerDao businessInfoManagerDao;
    
    protected PayManagerDao payManagerDao;
    protected WebSiteManagerDao webSiteManagerDao;
    protected IntegralManagerDao  integralManagerDao;
    protected ExpensePlatManagerDao expensePlatManagerDao;
 
    protected ExpenseExpertManagerDao expertIEManagerDao;
    protected BankManagerDao bankManagerDao;
    //
    protected WelcomeManagerDao welcomeManagerDao;
    protected AdvertisementManagerDao advertisementManagerDao;
    protected ConsultRecordManagerDao consultRecordManagerDao;
    protected ConsultServerManagerDao consultServerManagerDao;
    
    /*
     * 付费咨询模块
     */
    protected ExpertClassifyService expertClassifyService;
    protected HotReplyService hotReplyService;
    
  //虚拟币记录
  	protected VirtualCoinRecordManagerDao virtualCoinRecordManagerDao;
  	
  //系统消息
  	protected SystemMessageManagerDao systemMessageManagerDao;

  	//兑换活动
  	protected ActivityExchangeRecordManagerDao activityExchangeRecordManagerDao;  	
  	//钱包
  	protected WalletManagerDao walletManagerDao;
  	protected ExpenseTotalManagerDao expenseTotalManagerDao;
  	
  	//直播管理
  	protected LiveService liveService;
  	protected ExpertDivideIntoManagerDao expertDivideIntoManagerDao;
  	protected WhiteListManagerDao whiteListManagerDao;
  	
  	protected SmsService smsService;
  	protected SystemManagerDao systemManagerDao;
  	protected WithdrawRecordManagerDao withdrawRecordManagerDao;
  	
	public PayManagerDao getPayManagerDao() {
		return payManagerDao;
	}

	public void setPayManagerDao(PayManagerDao payManagerDao) {
		this.payManagerDao = payManagerDao;
	}


	public EBusinessInfoManagerDao getBusinessInfoManagerDao() {
		return businessInfoManagerDao;
	}

	public void setBusinessInfoManagerDao(EBusinessInfoManagerDao businessInfoManagerDao) {
		this.businessInfoManagerDao = businessInfoManagerDao;
	}

	public BaseAction() {
	}

	protected String renderText(String text) {
		return render(text, "text/plain;charset=UTF-8");
	}

	protected String render(String text, String contentType) {
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType(contentType);
			response.getWriter().write(text);
		} catch (IOException ioexception) {
		}
		return null;
	}
	

	public BaseManagerAction getBaseManagerAction() {
		return baseManagerAction;
	}

	public void setBaseManagerAction(BaseManagerAction baseManagerAction) {
		this.baseManagerAction = baseManagerAction;
	}

	public EpColumnManagerDao getEpColumnManagerDao() {
		return epColumnManagerDao;
	}

	public void setEpColumnManagerDao(EpColumnManagerDao epColumnManagerDao) {
		this.epColumnManagerDao = epColumnManagerDao;
	}

	public EpNewsManagerDao getEpNewsManagerDao() {
		return epNewsManagerDao;
	}

	public void setEpNewsManagerDao(EpNewsManagerDao epNewsManagerDao) {
		this.epNewsManagerDao = epNewsManagerDao;
	}

	public MemberManagerDao getMemberManagerDao() {
		return memberManagerDao;
	}

	public void setMemberManagerDao(MemberManagerDao memberManagerDao) {
		this.memberManagerDao = memberManagerDao;
	}

	public CollectManagerDao getCollectManagerDao() {
		return collectManagerDao;
	}

	public void setCollectManagerDao(CollectManagerDao collectManagerDao) {
		this.collectManagerDao = collectManagerDao;
	}

	public BuysRecordManagerDao getBuysRecordManagerDao() {
		return buysRecordManagerDao;
	}

	public void setBuysRecordManagerDao(BuysRecordManagerDao buysRecordManagerDao) {
		this.buysRecordManagerDao = buysRecordManagerDao;
	}

	public ProductManagerDao getProductManagerDao() {
		return productManagerDao;
	}

	public void setProductManagerDao(ProductManagerDao productManagerDao) {
		this.productManagerDao = productManagerDao;
	}

	public RegionManagerDao getRegionManagerDao() {
		return regionManagerDao;
	}

	public void setRegionManagerDao(RegionManagerDao regionManagerDao) {
		this.regionManagerDao = regionManagerDao;
	}

	public LegalManagerDao getLegalManagerDao() {
		return legalManagerDao;
	}

	public void setLegalManagerDao(LegalManagerDao legalManagerDao) {
		this.legalManagerDao = legalManagerDao;
	}

	public NationalDifferencesManagerDao getNationalDifferencesManagerDao() {
		return nationalDifferencesManagerDao;
	}

	public void setNationalDifferencesManagerDao(NationalDifferencesManagerDao nationalDifferencesManagerDao) {
		this.nationalDifferencesManagerDao = nationalDifferencesManagerDao;
	}

	public StandardManagerDao getStandardManagerDao() {
		return standardManagerDao;
	}

	public void setStandardManagerDao(StandardManagerDao standardManagerDao) {
		this.standardManagerDao = standardManagerDao;
	}

	public StandardBaseManagerDao getStandardBaseManagerDao() {
		return standardBaseManagerDao;
	}

	public void setStandardBaseManagerDao(StandardBaseManagerDao standardBaseManagerDao) {
		this.standardBaseManagerDao = standardBaseManagerDao;
	}

	public CertificationAuthorityManagerDao getCertificationAuthorityManagerDao() {
		return certificationAuthorityManagerDao;
	}

	public void setCertificationAuthorityManagerDao(CertificationAuthorityManagerDao certificationAuthorityManagerDao) {
		this.certificationAuthorityManagerDao = certificationAuthorityManagerDao;
	}

	public AuthenticationManagerDao getAuthenticationManagerDao() {
		return authenticationManagerDao;
	}

	public void setAuthenticationManagerDao(AuthenticationManagerDao authenticationManagerDao) {
		this.authenticationManagerDao = authenticationManagerDao;
	}


	public StandardMadeManagerDao getStandardMadeManagerDao() {
		return standardMadeManagerDao;
	}

	public void setStandardMadeManagerDao(StandardMadeManagerDao standardMadeManagerDao) {
		this.standardMadeManagerDao = standardMadeManagerDao;
	}

	public StandardReadManagerDao getStandardReadManagerDao() {
		return standardReadManagerDao;
	}

	public void setStandardReadManagerDao(StandardReadManagerDao standardReadManagerDao) {
		this.standardReadManagerDao = standardReadManagerDao;
	}

	public QualityShareManagerDao getQualityShareManagerDao() {
		return qualityShareManagerDao;
	}

	public void setQualityShareManagerDao(QualityShareManagerDao qualityShareManagerDao) {
		this.qualityShareManagerDao = qualityShareManagerDao;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public ExpertManagerDao getExpertManagerDao() {
		return expertManagerDao;
	}

	public void setExpertManagerDao(ExpertManagerDao expertManagerDao) {
		this.expertManagerDao = expertManagerDao;
	}

	public EBusinessProductManagerDao getBusinessProductManagerDao() {
		return businessProductManagerDao;
	}

	public void setBusinessProductManagerDao(EBusinessProductManagerDao businessProductManagerDao) {
		this.businessProductManagerDao = businessProductManagerDao;
	}

	public PackagesManagerDao getPackagesManagerDao() {
		return packagesManagerDao;
	}

	public void setPackagesManagerDao(PackagesManagerDao packagesManagerDao) {
		this.packagesManagerDao = packagesManagerDao;
	}

	public WebSiteManagerDao getWebSiteManagerDao() {
		return webSiteManagerDao;
	}

	public void setWebSiteManagerDao(WebSiteManagerDao webSiteManagerDao) {
		this.webSiteManagerDao = webSiteManagerDao;
	}

	public IntegralManagerDao getIntegralManagerDao() {
		return integralManagerDao;
	}

	public void setIntegralManagerDao(IntegralManagerDao integralManagerDao) {
		this.integralManagerDao = integralManagerDao;
	}
	public ExpensePlatManagerDao getExpensePlatManagerDao() {
		return expensePlatManagerDao;
	}

	public void setExpensePlatManagerDao(ExpensePlatManagerDao expensePlatManagerDao) {
		this.expensePlatManagerDao = expensePlatManagerDao;
	}
 

	public ExpenseExpertManagerDao getExpertIEManagerDao() {
		return expertIEManagerDao;
	}

	public void setExpertIEManagerDao(ExpenseExpertManagerDao expertIEManagerDao) {
		this.expertIEManagerDao = expertIEManagerDao;
	}

	public BankManagerDao getBankManagerDao() {
		return bankManagerDao;
	}

	public void setBankManagerDao(BankManagerDao bankManagerDao) {
		this.bankManagerDao = bankManagerDao;
	}

	public WelcomeManagerDao getWelcomeManagerDao() {
		return welcomeManagerDao;
	}

	public void setWelcomeManagerDao(WelcomeManagerDao welcomeManagerDao) {
		this.welcomeManagerDao = welcomeManagerDao;
	}
	
	
	

	public ExpertClassifyService getExpertClassifyService() {
		return expertClassifyService;
	}

	public void setExpertClassifyService(ExpertClassifyService expertClassifyService) {
		this.expertClassifyService = expertClassifyService;
	}

	public HotReplyService getHotReplyService() {
		return hotReplyService;
	}

	public void setHotReplyService(HotReplyService hotReplyService) {
		this.hotReplyService = hotReplyService;
	}

	public AdvertisementManagerDao getAdvertisementManagerDao() {
		return advertisementManagerDao;
	}

	public void setAdvertisementManagerDao(AdvertisementManagerDao advertisementManagerDao) {
		this.advertisementManagerDao = advertisementManagerDao;
	}

	public VirtualCoinRecordManagerDao getVirtualCoinRecordManagerDao() {
		return virtualCoinRecordManagerDao;
	}

	public void setVirtualCoinRecordManagerDao(VirtualCoinRecordManagerDao virtualCoinRecordManagerDao) {
		this.virtualCoinRecordManagerDao = virtualCoinRecordManagerDao;
	}

	public ConsultRecordManagerDao getConsultRecordManagerDao() {
		return consultRecordManagerDao;
	}

	public void setConsultRecordManagerDao(ConsultRecordManagerDao consultRecordManagerDao) {
		this.consultRecordManagerDao = consultRecordManagerDao;
	}

	public ConsultServerManagerDao getConsultServerManagerDao() {
		return consultServerManagerDao;
	}

	public void setConsultServerManagerDao(ConsultServerManagerDao consultServerManagerDao) {
		this.consultServerManagerDao = consultServerManagerDao;
	}

	public SystemMessageManagerDao getSystemMessageManagerDao() {
		return systemMessageManagerDao;
	}

	public void setSystemMessageManagerDao(SystemMessageManagerDao systemMessageManagerDao) {
		this.systemMessageManagerDao = systemMessageManagerDao;
	}

	public ActivityExchangeRecordManagerDao getActivityExchangeRecordManagerDao() {
		return activityExchangeRecordManagerDao;
	}

	public void setActivityExchangeRecordManagerDao(ActivityExchangeRecordManagerDao activityExchangeRecordManagerDao) {
		this.activityExchangeRecordManagerDao = activityExchangeRecordManagerDao;
	}

	public WalletManagerDao getWalletManagerDao() {
		return walletManagerDao;
	}

	public void setWalletManagerDao(WalletManagerDao walletManagerDao) {
		this.walletManagerDao = walletManagerDao;
	}

	public ExpenseTotalManagerDao getExpenseTotalManagerDao() {
		return expenseTotalManagerDao;
	}

	public void setExpenseTotalManagerDao(ExpenseTotalManagerDao expenseTotalManagerDao) {
		this.expenseTotalManagerDao = expenseTotalManagerDao;
	}

	public LiveService getLiveService() {
		return liveService;
	}

	public void setLiveService(LiveService liveService) {
		this.liveService = liveService;
	}

	public ExpertDivideIntoManagerDao getExpertDivideIntoManagerDao() {
		return expertDivideIntoManagerDao;
	}

	public void setExpertDivideIntoManagerDao(ExpertDivideIntoManagerDao expertDivideIntoManagerDao) {
		this.expertDivideIntoManagerDao = expertDivideIntoManagerDao;
	}

	public CertificationReportService getCertificationReportService() {
		return certificationReportService;
	}

	public void setCertificationReportService(CertificationReportService certificationReportService) {
		this.certificationReportService = certificationReportService;
	}
	public WhiteListManagerDao getWhiteListManagerDao() {
		return whiteListManagerDao;
	}
	public void setWhiteListManagerDao(WhiteListManagerDao whiteListManagerDao) {
		this.whiteListManagerDao = whiteListManagerDao;
	}
	public SmsService getSmsService() {
		return smsService;
	}
	public void setSmsService(SmsService smsService) {
		this.smsService = smsService;
	}

	public SystemManagerDao getSystemManagerDao() {
		return systemManagerDao;
	}

	public void setSystemManagerDao(SystemManagerDao systemManagerDao) {
		this.systemManagerDao = systemManagerDao;
	}

	public WithdrawRecordManagerDao getWithdrawRecordManagerDao() {
		return withdrawRecordManagerDao;
	}

	public void setWithdrawRecordManagerDao(WithdrawRecordManagerDao withdrawRecordManagerDao) {
		this.withdrawRecordManagerDao = withdrawRecordManagerDao;
	}
}