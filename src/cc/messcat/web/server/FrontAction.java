package cc.messcat.web.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import cc.messcat.entity.*;
import cc.messcat.vo.*;
import cc.modules.util.CollectionUtil;
import cc.modules.util.PropertiesFileReader;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import cc.messcat.entity.NationalDifferences;
import cc.modules.commons.PageAction;
import cc.modules.commons.Pager;
import cc.modules.constants.Constants;
import cc.modules.util.ObjValid;

@SuppressWarnings("serial")
public class FrontAction extends PageAction implements ServletRequestAware {

	private static String jointUrl = PropertiesFileReader.getByKey("static.domain");  //图片拼接地址
	private Object object;
	private String frontNum;
	private Long memberId;
	private String accessToken; // 请求accessToken
	private Long columnId;
	private Long newsId;
	private Long regionId;
	private String name;
	private Member member;
	private ProductVo productVo;
	private Long productId; // 产品ID
	private Long regionFatherId;
	private HttpServletRequest request;
	private String returnMsg;
	private String buyStatus;
	private String vipStatus;
	private String viewStatus;
	private String moreId;
	private String version;
	private String terminal;

	/**
	 * 6.1 首页轮播图
	 */
	public String frontBanners() {
		try {
			if (null != memberId) {
				member = memberManagerDao.retrieveMember(memberId);
			}
			List<EnterpriseNewsVo> news = epNewsManagerDao.listEnterpriseNews(pageSize, "index_photo", member);
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, news);

		} catch (Exception e) {
			e.printStackTrace();
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
		}
		return "success";
	}

	/**
	 * 首页轮播图（5期）
	 * @return
	 */
	public String indexCarousel () {
		if(memberId == null || memberId == 0L) {
			List<IndexCarouselVo> carouselVos = epNewsManagerDao.getCarousel();
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, carouselVos);
		} else {
			Member member = memberManagerDao.getMobileById(memberId);
			member.setAppVersion(version);
			member.setTerminal(terminal);
			memberManagerDao.updateProMember(member);
			
			String vers = member.getAppVersion();
			if (vers != null && vers.equals("2.0.2")) {
				List<IndexCarouselVo> carouselVos = epNewsManagerDao.getCarousel();
				List<IndexCarouselVo> newCarousel = new ArrayList<>();
				for (IndexCarouselVo carousel : carouselVos) {
					if(carousel.getTitle().equals("元旦送大礼")) {
						String[] url = carousel.getUri().split("url=");
						carousel.setUri(url[1]);
					}
					newCarousel.add(carousel);
				}
				object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, newCarousel);
			}else {
				List<IndexCarouselVo> carouselVos = epNewsManagerDao.getCarousel();
				object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, carouselVos);
			}
		}
		return "success";
	}
	/**
	 * 6.2 首页栏目信息
	 */
	public String frontNews() {
		try {
			Member member = memberManagerDao.retrieveMember(memberId);
			FrontColumnNewsVo columnNews = epNewsManagerDao.listFrontColumnNewsVo(pageSize, pageNo, frontNum, member);
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, columnNews);

		} catch (Exception e) {
			e.printStackTrace();
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
		}
		return "success";
	}

	/**
	 * 6.7 首页新闻栏目更多
	 */
	public String frontNewsMore() {
		try {
			Member member = memberManagerDao.retrieveMember(memberId);
			if (ObjValid.isValid(member)) {
				if (!accessToken.equals(member.getAccessToken())) {
					object = new ResponseBean(Constants.EXCEPTION_CODE_600, Constants.LOGIN_VERIFY_TOKEN_ERROR);
					return "success";
				}
			}
			EnterpriseNewsListVo listVo = epNewsManagerDao.getEpNewsByColumnId(pageSize, pageNo, columnId, member);
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, listVo);

		} catch (Exception e) {
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
		}
		return "success";
	}

	/**
	 * 6.3 新闻详情
	 */
	public String news() {
		try {
			EnterpriseNewsVo news = epNewsManagerDao.getEpNews(newsId,memberId);
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, news);

		} catch (Exception e) {
			e.printStackTrace();
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
		}
		return "success";
	}

	/**
	 * 6.4 首页搜索产品
	 */
	public String getProductsByName() throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("textml;charset=UTF-8");
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		response.setHeader("Access-Control-Max-Age", "0");
		response.setHeader("Access-Control-Allow-Headers",
				"Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("XDomainRequestAllowed", "1");
		ProductListVo productListVo = new ProductListVo();
		try {
			Member member = new Member();
			if (memberId > 0) { // memberId为-1,则为游客身份登录, 直接判断,不用再去数据库查询是否有此会员
				member = memberManagerDao.retrieveMember(memberId);
				if (member != null) {
					if (!accessToken.equals(member.getAccessToken())) {
						object = new ResponseBean(Constants.EXCEPTION_CODE_600, Constants.LOGIN_VERIFY_TOKEN_ERROR);
						return "success";
					}
				}
			}
			productListVo = productManagerDao.getProductByName(pageSize, pageNo, name, regionId, member);
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, productListVo);
		} catch (Exception e) {
			e.printStackTrace();
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
		}
		return "success";
	}

	/**
	 * 首页准入报告详情
	 */
	public String getMarketReportDetail() throws Exception {
		try {
			Member member = null;
			if (memberId > 0) { // memberId为-1,则为游客身份登录, 直接判断,不用再去数据库查询是否有此会员
				member = memberManagerDao.retrieveMember(memberId);
				if (member != null) {
					if (!accessToken.equals(member.getAccessToken())) {
						object = new ResponseBean(Constants.EXCEPTION_CODE_600, Constants.LOGIN_VERIFY_TOKEN_ERROR);
						return "success";
					}
				}
			}
			ProductVo entity = new ProductVo();
			entity = productManagerDao.getMarketReportDetail(regionId, memberId, productVo.getProductId());
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, entity);
		} catch (Exception e) {
			e.printStackTrace();
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
		}
		return "success";
	}

	/**
	 * 6.8 准入报告详情(H5)
	 */
	@SuppressWarnings("unchecked")
	public String getReportItemH5() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("textml;charset=UTF-8");
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		response.setHeader("Access-Control-Max-Age", "0");
		response.setHeader("Access-Control-Allow-Headers",
				"Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("XDomainRequestAllowed", "1");

		CertificationReportResult result = new CertificationReportResult();

		// 查询数据
		Region region = regionManagerDao.retrieveRegion(regionId);
		Product product = productManagerDao.retrieveProduct(productId);
		List<Legal> legals = new ArrayList<Legal>();
		if (ObjValid.isValid(region.getFatherId()) && region.getFatherId() != 0) {
			legals = legalManagerDao.getLegalByCon(regionId, productId, region.getFatherId());
		} else {
			legals = legalManagerDao.getLegalByCon(regionId, productId, null);
		}
		NationalDifferences different = nationalDifferencesManagerDao.getDifferentByCon(regionId); // 国家差异
		Standard standard = standardManagerDao.getStandardByCon(regionId, productId); // 执行标准
		List<Authentication> authens = new ArrayList<Authentication>();
		if (ObjValid.isValid(region.getFatherId()) && region.getFatherId() != 0) {
			authens = authenticationManagerDao.getAuthenByCon(regionId, productId, region.getFatherId());
		} else {
			authens = authenticationManagerDao.getAuthenByCon(regionId, productId, null); // 认证要求
		}

		// 获取认证机构
		List<CertificationAuthorityVo> authoritys = new ArrayList<CertificationAuthorityVo>();
		if (ObjValid.isValid()) {
			List<CertificationReport> cReport = new ArrayList<CertificationReport>();
			// 查询总体使用的机构(通过地区ID和产品ID)
			List<CertificationReport> allcReport = certificationReportService.findCertificationReportByRidPid(null,
					null);
			cReport.addAll(allcReport);
			// 查询部分使用的机构
			List<CertificationReport> partcReport = certificationReportService.findCertificationReportByRidPid(regionId,
					productId);
			cReport.addAll(partcReport);
			if (CollectionUtil.isListNotEmpty(cReport)) {
				for (CertificationReport cr : cReport) {
					CertificationAuthority auth = certificationAuthorityManagerDao
							.findCertificationAuthorityByid(cr.getCertificationid());
					if(auth!=null){
						CertificationAuthorityVo cav=new CertificationAuthorityVo();
						cav.setEmail(auth.getEmail());
						cav.setLinkman(auth.getLinkman());
						cav.setMobile(auth.getMobile());
						cav.setName(auth.getName());
						cav.setPhoto("http://www.cert-map.com/jsps/theme/default/image/" + auth.getPhoto());
						authoritys.add(cav);
					}
				}
			}
		}
		result.setCertifiAuthList(authoritys); // 认证机构

		List<AuthenticationVo> authenList = new ArrayList<AuthenticationVo>();
		for (Authentication entity : authens) {
			AuthenticationVo vo = new AuthenticationVo();
			vo.setName(entity.getName());
			vo.setRequest(entity.getRequests());
			AuthSignVo signVo = new AuthSignVo();
			List<String> images=new ArrayList<String>();
			if (entity.getSign().contains("images")) {
				String[] strings=entity.getSign().split(",");
				for(String s:strings){
					images.add(jointUrl+s);
				}
				signVo.setImage(images);
				signVo.setTitle("");
			} else {
				signVo.setImage(images);
				signVo.setTitle(entity.getSign());
			}
			vo.setSign(signVo);
			authenList.add(vo);
		}
		result.setAuthenList(authenList); // 认证要求
		List<LegalsVo> legalsVoList = new ArrayList<LegalsVo>();
		for (Legal entity : legals) {
			LegalsVo legalsVo = new LegalsVo();
			legalsVo.setItem(entity.getItem());
			legalsVo.setContent(entity.getContent());
			legalsVoList.add(legalsVo);
		}
		result.setLegalsList(legalsVoList); // 法规要求
		result.setRegionName(region.getName());
		result.setProductName(product.getName());
		result.setStandard(standard.getMap()); // 标准
		if(different!=null){
			NationalDifferenceVo ndv=new NationalDifferenceVo();
			List<String> forms=new ArrayList<String>();
			if(different.getForm()!=null && !"".equals(different.getForm())){
				String[] strings=different.getForm().split(",");
				for(String s:strings){
					forms.add(jointUrl+s);
				}
			}
			ndv.setForm(forms);
			ndv.setHz(different.getHz());
			ndv.setOfficialLanguage(different.getOfficialLanguage());
			ndv.setType(different.getType());
			ndv.setVoltage(different.getVoltage());
			result.setDifferent(ndv); // 国家差异
		}
		object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, result);
		return "success";
	}

	/**
	 * 根据地区名精确查找单条地区
	 * 
	 * @return
	 */
	public String getRegionByName() throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("textml;charset=UTF-8");
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		response.setHeader("Access-Control-Max-Age", "0");
		response.setHeader("Access-Control-Allow-Headers",
				"Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("XDomainRequestAllowed", "1");
		try {
			List<Region> regions = regionManagerDao.getRegionByName(name);
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, regions);
		} catch (Exception e) {
			e.printStackTrace();
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
		}
		return "success";
	}

	/**
	 * 5.9  第五期首页的一部分列表数据
	 */
	@SuppressWarnings("unchecked")
	public String getFirstPager() {
		Member member = new Member();
		if (memberId > 0) { // memberId为-1,则为游客身份登录, 直接判断,不用再去数据库查询是否有此会员
			member = memberManagerDao.retrieveMember(memberId);
			if (member != null) {
				if (!accessToken.equals(member.getAccessToken())) {
					object = new ResponseBean(Constants.EXCEPTION_CODE_600, Constants.LOGIN_VERIFY_TOKEN_ERROR);
					return "success";
				}
			}
		}

		FirstPagerResult resultList = new FirstPagerResult();
		String chiefShow = "1";
		// 今日直播
		Pager pager1 = liveService.getTodayLive(pageNo, pageSize, "2");
		// 专题
		Pager pager2 = liveService.getSubjectList(pageNo, pageSize, chiefShow);
		// 精品课程
		Pager pager3 = liveService.getExcellentCourse(pageNo, pageSize,chiefShow,member);
		// 专栏订阅
		Pager pager4 = standardReadManagerDao.getColumnList(pageNo, pageSize, chiefShow, member);
		// 精品文章
		Pager pager5 = standardReadManagerDao.getExcellentArticleList(pageNo, pageSize, chiefShow, member);
		if(pager1.getRowCount()>0) {
			resultList.setTodayLive(pager1.getResultList());
		}else {
			List<LiveVideoListVo> todayLive=new ArrayList<LiveVideoListVo>();
			resultList.setTodayLive(todayLive);
		}
		if(pager2.getRowCount()>0) {
			resultList.setSubject(pager2.getResultList());
		}else {
			List<LiveVideoSubjectListVo> subject=new ArrayList<LiveVideoSubjectListVo>();
			resultList.setSubject(subject);
		}
		if(pager3.getRowCount()>0) {
			resultList.setExcellentCourse(pager3.getResultList());
		}else {
			List<LiveVideoListVo> excellentCourse=new ArrayList<LiveVideoListVo>();
			resultList.setExcellentCourse(excellentCourse);
		}
		if(pager4.getRowCount()>0) {
			resultList.setColumnSubscribe(pager4.getResultList());
		}else {
			List<StandardReadListVo3> columnSubscribe=new ArrayList<StandardReadListVo3>();
			resultList.setColumnSubscribe(columnSubscribe);
		}
		if(pager5.getRowCount()>0) {
			resultList.setExcellentArticle(pager5.getResultList());
		}else {
			List<StandReadingVo> excellentArticle=new ArrayList<StandReadingVo>();
			resultList.setExcellentArticle(excellentArticle);
		}
		object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, resultList);
		return "success";
	}

	/**
	 * 5.10  首页的查看更多(moreId从上到下,依次从1到5)
	 */
	@SuppressWarnings("unchecked")
	public String getFirstMore() {
		Member member = new Member();
		if (memberId > 0) { // memberId为-1,则为游客身份登录, 直接判断,不用再去数据库查询是否有此会员
			member = memberManagerDao.retrieveMember(memberId);
			if (member != null) {
				if (!accessToken.equals(member.getAccessToken())) {
					object = new ResponseBean(Constants.EXCEPTION_CODE_600, Constants.LOGIN_VERIFY_TOKEN_ERROR);
					return "success";
				}
			}
		}
		//今日直播
		if(moreId.equals("1")) {
			LiveVideoListResult3 resultList=new LiveVideoListResult3();
			Pager pager1 = liveService.getTodayLive(pageNo, pageSize, "0");
			if(pager1.getRowCount()>0) {
				resultList.setRowCount(pager1.getRowCount());
				resultList.setLiveVideoList(pager1.getResultList());
			}else {
				List<LiveVideoListVo2> todayLive=new ArrayList<LiveVideoListVo2>();
				resultList.setLiveVideoList(todayLive);
			}
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, resultList)
					;
		//专题
		}else if(moreId.equals("2")){
			LiveVideoSubjectResult resultList=new LiveVideoSubjectResult();	
			Pager pager2 = liveService.getSubjectList(pageNo, pageSize, "0");
			if(pager2.getRowCount()>0) {
				resultList.setSubjectList(pager2.getResultList());
				resultList.setRowCount(pager2.getRowCount());
			}else {
				List<LiveVideoSubjectListVo> subject=new ArrayList<LiveVideoSubjectListVo>();
			    resultList.setSubjectList(subject);
			}
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, resultList);
		//精品课程
		}else if(moreId.equals("3")) {
			LiveVideoListResult2 resultList=new LiveVideoListResult2();
			Pager pager3 = liveService.getExcellentCourse(pageNo, pageSize, "0",member);
			if(pager3.getRowCount()>0) {
				resultList.setLiveVideoList(pager3.getResultList());
				resultList.setRowCount(pager3.getRowCount());
			}else {
				List<LiveVideoListVo> excellentCourse=new ArrayList<LiveVideoListVo>();
				resultList.setLiveVideoList(excellentCourse);
			}
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, resultList);
		//专栏订阅
		}else if(moreId.equals("4")) {
			StandardReadListResult2 resultList=new StandardReadListResult2();
			Pager pager4 = standardReadManagerDao.getColumnList(pageNo, pageSize, "0", member);
			if(pager4.getRowCount()>0) {
				resultList.setRowCount(pager4.getRowCount());
				resultList.setColumnSubscribe(pager4.getResultList());
			}else {
				List<StandardReadListVo3> columnSubscribe=new ArrayList<StandardReadListVo3>();
				resultList.setColumnSubscribe(columnSubscribe);
			}
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, resultList);
		//精品文章
		}else if(moreId.equals("5")) {
			StandardReadResult resultList=new StandardReadResult();
			Pager pager5 = standardReadManagerDao.getExcellentArticleList(pageNo, pageSize, "0", member);
			if(pager5.getRowCount()>0) {
				resultList.setRowCount(pager5.getRowCount());
				resultList.setStandReadList(pager5.getResultList());
			}else {
				List<StandardReadListVo5> excellentArticle=new ArrayList<StandardReadListVo5>();
				resultList.setStandReadList(excellentArticle);
			}
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, resultList);
		}else {
			object = new ResponseBean(Constants.FAIL_CODE_400, Constants.MSG_FAIL, "moreId为1到5的数字");
		}
		return "success";
	}
	
	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public void setFrontNum(String frontNum) {
		this.frontNum = frontNum;
	}

	public void setColumnId(Long columnId) {
		this.columnId = columnId;
	}

	public void setNewsId(Long newsId) {
		this.newsId = newsId;
	}

	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getRegionId() {
		return regionId;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public String getFrontNum() {
		return frontNum;
	}

	public Long getColumnId() {
		return columnId;
	}

	public Long getNewsId() {
		return newsId;
	}

	public String getName() {
		return name;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public ProductVo getProductVo() {
		return productVo;
	}

	public void setProductVo(ProductVo productVo) {
		this.productVo = productVo;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getRegionFatherId() {
		return regionFatherId;
	}

	public void setRegionFatherId(Long regionFatherId) {
		this.regionFatherId = regionFatherId;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public String getReturnMsg() {
		return returnMsg;
	}

	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}

	public String getBuyStatus() {
		return buyStatus;
	}

	public void setBuyStatus(String buyStatus) {
		this.buyStatus = buyStatus;
	}

	public String getVipStatus() {
		return vipStatus;
	}

	public void setVipStatus(String vipStatus) {
		this.vipStatus = vipStatus;
	}

	public String getViewStatus() {
		return viewStatus;
	}

	public void setViewStatus(String viewStatus) {
		this.viewStatus = viewStatus;
	}
	public String getMoreId() {
		return moreId;
	}

	public void setMoreId(String moreId) {
		this.moreId = moreId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getTerminal() {
		return terminal;
	}

	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}

}
