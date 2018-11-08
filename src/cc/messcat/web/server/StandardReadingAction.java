package cc.messcat.web.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;

import cc.messcat.entity.Member;
import cc.messcat.entity.StandardMade;
import cc.messcat.entity.StandardReading;
import cc.messcat.vo.Adjunct;
import cc.messcat.vo.CatalogResult;
import cc.messcat.vo.CatalogVo1;
import cc.messcat.vo.HotExpertListVo;
import cc.messcat.vo.HotExpertListVoResult;
import cc.messcat.vo.ResponseBean;
import cc.messcat.vo.StandardReadListResult;
import cc.messcat.vo.StandardReadListResult3;
import cc.messcat.vo.StandardReadListResultH5;
import cc.messcat.vo.StandardReadListVo;
import cc.messcat.vo.StandardReadListVo2Result;
import cc.messcat.vo.StandardReadListVo4;
import cc.messcat.vo.StandardReadSearchResult;
import cc.messcat.vo.StandardReadSearchResult2;
import cc.modules.commons.PageAction;
import cc.modules.commons.Pager;
import cc.modules.constants.Constants;
import cc.modules.util.ObjValid;

public class StandardReadingAction extends PageAction implements ServletRequestAware {

	private static final long serialVersionUID = 1L;
	private String accessToken; // 请求accessToken
	private Integer pageSize1 = 4; // 专家栏发现页面默认显示4条
	private Integer pageSize3 = 6; // 质量分享栏发现页面默认显示6条
	private String standardCode; // 标准号
	private String searchKeyWord; // 搜索关键字
	private String type; // 解读类型（1：体系化解读 0：单一解读）
	private String isRecommend; // 是否推荐热门（0：否 1：是）
	private String qualityId; // 质量分享id
	private Long standardReadingId; // 标准解读id
	private Long memberId;
	private String email; // 邮箱
	private String message; // 留言
	private Member member;
	private Object object;
	private HttpServletRequest request;

	private StandardReading standardReading;
	private Long expertId; // 专家ID
	private String attentionStatus; // 关注状态(0代表未关注，1代表已关注)
	private Date today; // 当前日期

	/**
	 * 2.1 解读分享首页信息
	 */
	@SuppressWarnings("unchecked")
	public String execute() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("textml;charset=UTF-8");
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		response.setHeader("Access-Control-Max-Age", "0");
		response.setHeader("Access-Control-Allow-Headers",
				"Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("XDomainRequestAllowed", "1");
		try {
			Member member = new Member();
			if (memberId > 0) { // memberId为-1,则为游客身份登录, 直接判断,不用再去数据库查询是否有此会员
				member = memberManagerDao.retrieveMember(memberId);
				if (ObjValid.isValid(member)) {
					if (!accessToken.equals(member.getAccessToken())) {
						object = new ResponseBean(Constants.EXCEPTION_CODE_600, Constants.LOGIN_VERIFY_TOKEN_ERROR);
						return "success";
					}
				}
			}
			StandardReadListResult result = new StandardReadListResult();
			Pager pager1 = expertManagerDao.getHotExpertList(pageNo, pageSize1, member); // 热门专家推荐专栏
			Pager pager2 = standardReadManagerDao.getStandardReadingList(pageNo, pageSize, member); // 热门专栏
			Pager pager3 = qualityShareManagerDao.getQualityTypeList(pageNo, pageSize3); // 质量分享

			result.setExpertList(pager1.getResultList());
			result.setExpertRowCount(pager1.getRowCount());
			result.setStandardReadingList(pager2.getResultList());
			result.setReadRowCount(pager2.getRowCount());
			result.setQualityTypeList(pager3.getResultList());
			result.setQualityRowCount(pager3.getRowCount());
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, result);

		} catch (Exception e) {
			e.printStackTrace();
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
		}
		return "success";
	}

	/**
	 * 2.2 按标准号搜索接口 =========================>>>>>>>>>>第四期要优化的接口
	 * 
	 */
	@SuppressWarnings("unchecked")
	public String codeSearchList() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("textml;charset=UTF-8");
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		response.setHeader("Access-Control-Max-Age", "0");
		response.setHeader("Access-Control-Allow-Headers",
				"Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("XDomainRequestAllowed", "1");
		try {
			Member member = new Member();
			if (memberId > 0) { // memberId为-1,则为游客身份登录, 直接判断,不用再去数据库查询是否有此会员
				member = memberManagerDao.retrieveMember(memberId);
				if (ObjValid.isValid(member)) {
					if (!accessToken.equals(member.getAccessToken())) {
						object = new ResponseBean(Constants.EXCEPTION_CODE_600, Constants.LOGIN_VERIFY_TOKEN_ERROR);
						return "success";
					}
				}
			}
			// type=2专为H5所设的参数值,可以把连载和单一的全部查找出来
			if (type.equals("0") || type.equals("1") || type.equals("2")) {
				StandardReadSearchResult result = new StandardReadSearchResult();
				Pager pager = standardReadManagerDao.getStandardReadingList3(standardCode, type, pageNo, pageSize,
						member);
				result.setStandardCode(standardCode);
				result.setType(type);
				result.setStandardReadingList(pager.getResultList());
				result.setRowCount(pager.getRowCount());
				object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, result);
			} else {
				object = new ResponseBean(Constants.EXCEPTION_CODE_500, "type参数错误,参数只能为从0,1,2中选其一", "");
			}

		} catch (Exception e) {
			e.printStackTrace();
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
		}
		return "success";
	}

	/**
	 * 2.3.1 热门专家推荐专栏的查看更多
	 */
	@SuppressWarnings("unchecked")
	public String getExpertList() {
		try {
			Member member = new Member();
			if (memberId > 0) { // memberId为-1,则为游客身份登录, 直接判断,不用再去数据库查询是否有此会员
				member = memberManagerDao.retrieveMember(memberId);
				if (ObjValid.isValid(member)) {
					if (!accessToken.equals(member.getAccessToken())) {
						object = new ResponseBean(Constants.EXCEPTION_CODE_600, Constants.LOGIN_VERIFY_TOKEN_ERROR);
						return "success";
					}
				}
			}
			HotExpertListVoResult resultList = new HotExpertListVoResult();
			Pager pager = expertManagerDao.getHotExpertList2(pageNo, pageSize, member);
			resultList.setHotExpertList(pager.getResultList());
			resultList.setRowCount(pager.getRowCount());
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, resultList);

		} catch (Exception e) {
			e.printStackTrace();
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
		}
		return "success";
	}

	/**
	 * 2.3.2 热门专家详情接口
	 */
	public String getExpertDetail() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("textml;charset=UTF-8");
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		response.setHeader("Access-Control-Max-Age", "0");
		response.setHeader("Access-Control-Allow-Headers",
				"Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("XDomainRequestAllowed", "1");
		try {
			Member member = new Member();
			if (memberId > 0) { // memberId为-1,则为游客身份登录, 直接判断,不用再去数据库查询是否有此会员
				member = memberManagerDao.retrieveMember(memberId);
				if (ObjValid.isValid(member)) {
					if (!accessToken.equals(member.getAccessToken())) {
						object = new ResponseBean(Constants.EXCEPTION_CODE_600, Constants.LOGIN_VERIFY_TOKEN_ERROR);
						return "success";
					}
				}
			}
			HotExpertListVo result = expertManagerDao.getHotExpertDetail(expertId, member);
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, result);

		} catch (Exception e) {
			e.printStackTrace();
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
		}
		return "success";
	}

	/**
	 * 2.4 热门专栏的连载 /解读列表
	 */
	@SuppressWarnings("unchecked")
	public String getStandardReadingList2() {
		try {
			if (memberId > 0) { // memberId为-1,则为游客身份登录, 直接判断,不用再去数据库查询是否有此会员
				member = memberManagerDao.retrieveMember(memberId);
				if (ObjValid.isValid(member)) {
					if (!accessToken.equals(member.getAccessToken())) {
						object = new ResponseBean(Constants.EXCEPTION_CODE_600, Constants.LOGIN_VERIFY_TOKEN_ERROR);
						return "success";
					}
				}
			}
			if (type.equals("1") || type.equals("0")) {
				StandardReadSearchResult2 result = new StandardReadSearchResult2();
				Pager pager = standardReadManagerDao.getStandardReadingList5(type, pageNo, pageSize, isRecommend,
						member);
				result.setStandardReadingList(pager.getResultList());
				result.setRowCount(pager.getRowCount());
				if (type.equals("1")) {
					result.setType("1");
				} else {
					result.setType("0");
				}
				object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, result);
			} else {
				object = new ResponseBean(Constants.EXCEPTION_CODE_500, "亲，请输入正确的解读类型(1：连载解读 0：单一解读)");
			}

		} catch (Exception e) {
			e.printStackTrace();
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
		}
		return "success";
	}

	/**
	 * 2.5 标准解读详情页
	 */
	public String standardReading() {
		try {
			Member member = memberManagerDao.retrieveMember(memberId);
			if (member == null) {
				object = new ResponseBean(Constants.EXCEPTION_CODE_600, "用户不存在");
				return "success";
			}
			if (!accessToken.equals(member.getAccessToken())) {
				object = new ResponseBean(Constants.EXCEPTION_CODE_600, Constants.LOGIN_VERIFY_TOKEN_ERROR);
				return "success";
			}
			List<StandardReadListVo> resultList = standardReadManagerDao.searchStandardReading(type, standardReadingId,
					member);
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, resultList);

		} catch (Exception e) {
			e.printStackTrace();
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
		}
		return "success";
	}

	/**
	 * 2.6 质量分享列表
	 */
	@SuppressWarnings("unchecked")
	public String getQualityList() {
		try {
			Member member = new Member();
			if (memberId > 0) { // memberId为-1,则为游客身份登录, 直接判断,不用再去数据库查询是否有此会员
				member = memberManagerDao.retrieveMember(memberId);
				if (ObjValid.isValid(member)) {
					if (!accessToken.equals(member.getAccessToken())) {
						object = new ResponseBean(Constants.EXCEPTION_CODE_600, Constants.LOGIN_VERIFY_TOKEN_ERROR);
						return "success";
					}
				}
			}
			if (type.equals("0") || type.equals("1")) {
				StandardReadSearchResult2 result = new StandardReadSearchResult2();
				Pager pager = qualityShareManagerDao.getQualityTypeList2(pageNo, pageSize, qualityId, type, member);
				result.setStandardReadingList(pager.getResultList());
				result.setType(type);
				result.setRowCount(pager.getRowCount());
				object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, result);
			} else {
				object = new ResponseBean(Constants.EXCEPTION_CODE_500, "亲，请输入正确的解读类型(1：连载解读 0：单一解读)");
			}

		} catch (Exception e) {
			e.printStackTrace();
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
		}
		return "success";
	}

	/**
	 * 2.7 专家详情中点击"他的文章"
	 */
	@SuppressWarnings("unchecked")
	public String hisStandardList() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("textml;charset=UTF-8");
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		response.setHeader("Access-Control-Max-Age", "0");
		response.setHeader("Access-Control-Allow-Headers",
				"Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("XDomainRequestAllowed", "1");
		try {
			Member member = new Member();
			if (memberId > 0) { // memberId为-1,则为游客身份登录, 直接判断,不用再去数据库查询是否有此会员
				member = memberManagerDao.retrieveMember(memberId);
				if (ObjValid.isValid(member)) {
					if (!accessToken.equals(member.getAccessToken())) {
						object = new ResponseBean(Constants.EXCEPTION_CODE_600, Constants.LOGIN_VERIFY_TOKEN_ERROR);
						return "success";
					}
				}
			}
			StandardReadSearchResult2 result = new StandardReadSearchResult2();
			Pager pager = standardReadManagerDao.getHisStandardReadList(pageNo, pageSize, expertId, type, member);
			result.setStandardReadingList(pager.getResultList());
			result.setType(type);
			result.setRowCount(pager.getRowCount());
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, result);

		} catch (Exception e) {
			e.printStackTrace();
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
		}
		return "success";
	}

	/**
	 * 2.8 标准定制提交接口
	 */
	public String commitStandardMade() {
		try {
			Member member = memberManagerDao.retrieveMember(memberId);
			if (member == null) {
				object = new ResponseBean(Constants.EXCEPTION_CODE_600, "用户不存在");
				return "success";
			}
			String array = member.getAccessToken();
			if (!accessToken.equals(array)) {
				object = new ResponseBean(Constants.EXCEPTION_CODE_600, Constants.LOGIN_VERIFY_TOKEN_ERROR);// 给Action的已经定义好的object对象赋值
				return "success";
			}
			StandardMade made = new StandardMade();
			made.setEmail(email);
			made.setMessage(message);
			made.setStatus("1");
			made.setMember(member);
			made.setAddTime(new Date());
			made.setEditTime(new Date());
			standardMadeManagerDao.commit(made);
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_ADD_SUCCESS);

		} catch (Exception e) {
			e.printStackTrace();
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
		}
		return "success";
	}

	/**
	 * 4.9 个人中心我的解读列表（只用于专家角色）
	 */
	@SuppressWarnings("unchecked")
	public String getMyReadingList() {
		try {
			Member member = memberManagerDao.retrieveMember(memberId);
			if (member == null) {
				object = new ResponseBean(Constants.EXCEPTION_CODE_600, "用户不存在");
				return "success";
			}
			if (!accessToken.equals(member.getAccessToken())) {
				object = new ResponseBean(Constants.EXCEPTION_CODE_600, Constants.LOGIN_VERIFY_TOKEN_ERROR);// 给Action的已经定义好的object对象赋值
				return "success";
			}
			StandardReadListVo2Result result = new StandardReadListVo2Result();
			if (type.equals("1") || type.equals("0")) {
				Pager pager = standardReadManagerDao.findList(type, pageNo, pageSize, member.getId());
				result.setStandardReadingList(pager.getResultList());
				result.setType(type);
				result.setRowCount(pager.getRowCount());
				object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, result);
			} else {
				object = new ResponseBean(Constants.EXCEPTION_CODE_500, "亲，请输入正确的解读类型(1：连载解读 0：单一解读)");
			}

		} catch (Exception e) {
			e.printStackTrace();
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
		}
		return "success";
	}


	/**
	 * 2.12 质量分享的附件列表
	 */
	public String getQualityShare() {
		try {
			Member member = null;
			if (memberId > 0) { // memberId为-1,则为游客身份登录, 直接判断,不用再去数据库查询是否有此会员
				member = memberManagerDao.retrieveMember(memberId);
				if (ObjValid.isValid(member)) {
					if (!accessToken.equals(member.getAccessToken())) {
						object = new ResponseBean(Constants.EXCEPTION_CODE_600, Constants.LOGIN_VERIFY_TOKEN_ERROR);
						return "success";
					}
				}
			}
			List<Adjunct> subStandardReadingList = new ArrayList<Adjunct>();
			subStandardReadingList = qualityShareManagerDao.getQualityShare(qualityId, standardReadingId, type);
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, subStandardReadingList);

		} catch (Exception e) {
			e.printStackTrace();
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
		}
		return "success";
	}

	/**
	 * 2.13 标准解读的附件列表
	 */
	public String getStandardRead() {
		try {
			Member member = null;
			if (memberId > 0) { // memberId为-1,则为游客身份登录, 直接判断,不用再去数据库查询是否有此会员
				member = memberManagerDao.retrieveMember(memberId);
				if (ObjValid.isValid(member)) {
					if (!accessToken.equals(member.getAccessToken())) {
						object = new ResponseBean(Constants.EXCEPTION_CODE_600, Constants.LOGIN_VERIFY_TOKEN_ERROR);
						return "success";
					}
				}
			}
			List<Adjunct> subStandardReadingList = new ArrayList<Adjunct>();
			subStandardReadingList = standardReadManagerDao.getStandardRead(standardReadingId, type);
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, subStandardReadingList);

		} catch (Exception e) {
			e.printStackTrace();
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
		}
		return "success";
	}

	/**
	 * 2.14 详情接口(标准解读,质量分享共享)
	 */
	public String searchDetail() {
		try {
			Member member = new Member();
			if(memberId!=null) {
				if (memberId > 0) { // memberId为-1,则为游客身份登录, 直接判断,不用再去数据库查询是否有此会员
					member = memberManagerDao.retrieveMember(memberId);
					if (ObjValid.isValid(member)) {
						if (!accessToken.equals(member.getAccessToken())) {
							object = new ResponseBean(Constants.EXCEPTION_CODE_600, Constants.LOGIN_VERIFY_TOKEN_ERROR);
							return "success";
						}
					}
				}
			}
			StandardReadListVo result = qualityShareManagerDao.searchDetail(standardReadingId, member);
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, result);

		} catch (Exception e) {
			e.printStackTrace();
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
		}
		return "success";
	}

	/**
	 * 2.15 点击"关注",返回一个操作结果(true/false)
	 */
	public String attention() {
		try {
			Member member = memberManagerDao.retrieveMember(memberId);
			if (member == null) {
				object = new ResponseBean(Constants.EXCEPTION_CODE_600, "用户不存在");
				return "success";
			}
			String array = member.getAccessToken();
			if (!accessToken.equals(array)) {
				object = new ResponseBean(Constants.EXCEPTION_CODE_600, Constants.LOGIN_VERIFY_TOKEN_ERROR);// 给Action的已经定义好的object对象赋值
				return "success";
			}
			boolean bok = expertManagerDao.clickAttention(member.getId(), expertId, attentionStatus); // 返回是否已关注的一个布尔值
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_UPDATE_SUCCESS, bok);
		} catch (Exception e) {
			e.printStackTrace();
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
		}
		return "success";
	}

	/**
	 * 2.16 “我的关注” 列表
	 */
	@SuppressWarnings("unchecked")
	public String myAttention() {
		try {
			Member member = memberManagerDao.retrieveMember(memberId);
			if (member == null) {
				object = new ResponseBean(Constants.EXCEPTION_CODE_600, "用户不存在");
				return "success";
			}
			if (!accessToken.equals(member.getAccessToken())) {
				object = new ResponseBean(Constants.EXCEPTION_CODE_600, Constants.LOGIN_VERIFY_TOKEN_ERROR);// 给Action的已经定义好的object对象赋值
				return "success";
			}
			HotExpertListVoResult resultList = new HotExpertListVoResult();
			Pager pager = expertManagerDao.getMyAttention(pageNo, pageSize, member); // 返回是否已关注的一个布尔值
			if (pager != null) {
				resultList.setHotExpertList(pager.getResultList());
				resultList.setRowCount(pager.getRowCount());
			} else {
				resultList.setHotExpertList(new ArrayList<HotExpertListVo>());
				resultList.setRowCount(0);
			}
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_UPDATE_SUCCESS, resultList);
		} catch (Exception e) {
			e.printStackTrace();
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
		}
		return "success";
	}

	/**
	 * 2.17 点赞(每天只能点赞一次,不可取消"点赞")
	 */
	public String clickLike() {
		try {
			Member member = memberManagerDao.retrieveMember(memberId);
			if (member == null) {
				object = new ResponseBean(Constants.EXCEPTION_CODE_600, "用户不存在");
				return "success";
			}
			String array = member.getAccessToken();
			if (!accessToken.equals(array)) {
				object = new ResponseBean(Constants.EXCEPTION_CODE_600, Constants.LOGIN_VERIFY_TOKEN_ERROR);// 给Action的已经定义好的object对象赋值
				return "success";
			}
			boolean bok = expertManagerDao.clickLike(member.getId(), expertId, today); // 返回是否点赞的一个布尔值
			if (bok == true) {
				object = new ResponseBean(Constants.SUCCESS_CODE_200, "点赞成功", bok);
			} else {
				object = new ResponseBean(Constants.FAIL_CODE_400, "点赞失败", bok);
			}

		} catch (Exception e) {
			e.printStackTrace();
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
		}
		return "success";
	}

	/**
	 * 2.33 专供H5的 热门专栏推荐列表
	 */
	public String hotStandardReadH5() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("textml;charset=UTF-8");
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		response.setHeader("Access-Control-Max-Age", "0");
		response.setHeader("Access-Control-Allow-Headers",
				"Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("XDomainRequestAllowed", "1");
		try {
			Member member = null;
			if (memberId > 0) { // memberId为-1,则为游客身份登录, 直接判断,不用再去数据库查询是否有此会员
				member = memberManagerDao.retrieveMember(memberId);
				if (ObjValid.isValid(member)) {
					if (!accessToken.equals(member.getAccessToken())) {
						object = new ResponseBean(Constants.EXCEPTION_CODE_600, Constants.LOGIN_VERIFY_TOKEN_ERROR);
						return "success";
					}
				}
			}
			StandardReadListResultH5 resultList = new StandardReadListResultH5();
			resultList = standardReadManagerDao.getStandardReadingListH5(pageNo, pageSize);

			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_UPDATE_SUCCESS, resultList);
		} catch (Exception e) {
			e.printStackTrace();
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
		}
		return "success";
	}

	/**
	 * 2.34 质量分享的附件列表
	 */
	public String getQualityShareH5() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("textml;charset=UTF-8");
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		response.setHeader("Access-Control-Max-Age", "0");
		response.setHeader("Access-Control-Allow-Headers",
				"Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("XDomainRequestAllowed", "1");
		try {
			Member member = null;
			if (memberId > 0) { // memberId为-1,则为游客身份登录, 直接判断,不用再去数据库查询是否有此会员
				member = memberManagerDao.retrieveMember(memberId);
				if (ObjValid.isValid(member)) {
					if (!accessToken.equals(member.getAccessToken())) {
						object = new ResponseBean(Constants.EXCEPTION_CODE_600, Constants.LOGIN_VERIFY_TOKEN_ERROR);
						return "success";
					}
				}
			}
			List<Adjunct> subStandardReadingList = new ArrayList<Adjunct>();
			subStandardReadingList = qualityShareManagerDao.getQualityShareH5(qualityId, standardReadingId, type);
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, subStandardReadingList);

		} catch (Exception e) {
			e.printStackTrace();
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
		}
		return "success";
	}

	/**
	 * 2.35 标准解读的附件列表
	 */
	public String getStandardReadH5() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("textml;charset=UTF-8");
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		response.setHeader("Access-Control-Max-Age", "0");
		response.setHeader("Access-Control-Allow-Headers",
				"Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("XDomainRequestAllowed", "1");
		try {
			Member member = null;
			if (memberId > 0) { // memberId为-1,则为游客身份登录, 直接判断,不用再去数据库查询是否有此会员
				member = memberManagerDao.retrieveMember(memberId);
				if (ObjValid.isValid(member)) {
					if (!accessToken.equals(member.getAccessToken())) {
						object = new ResponseBean(Constants.EXCEPTION_CODE_600, Constants.LOGIN_VERIFY_TOKEN_ERROR);
						return "success";
					}
				}
			}
			List<Adjunct> subStandardReadingList = new ArrayList<Adjunct>();
			subStandardReadingList = standardReadManagerDao.getStandardReadH5(standardReadingId, type);
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, subStandardReadingList);

		} catch (Exception e) {
			e.printStackTrace();
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
		}
		return "success";
	}

	/**
	 * 2.36 详情接口(标准解读,质量分享共享)
	 */
	public String searchDetailH5() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("textml;charset=UTF-8");
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		response.setHeader("Access-Control-Max-Age", "0");
		response.setHeader("Access-Control-Allow-Headers",
				"Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("XDomainRequestAllowed", "1");
		try {
			Member member = new Member();
			if (memberId > 0) { // memberId为-1,则为游客身份登录, 直接判断,不用再去数据库查询是否有此会员
				member = memberManagerDao.retrieveMember(memberId);
				if (ObjValid.isValid(member)) {
					if (!accessToken.equals(member.getAccessToken())) {
						object = new ResponseBean(Constants.EXCEPTION_CODE_600, Constants.LOGIN_VERIFY_TOKEN_ERROR);
						return "success";
					}
				}
			}
			StandardReadListVo result = qualityShareManagerDao.searchDetailH5(standardReadingId, member);
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, result);

		} catch (Exception e) {
			e.printStackTrace();
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
		}
		return "success";
	}

	/**
	 * 2.37 H5-- 质量分享列表
	 */
	@SuppressWarnings("unchecked")
	public String getQualityListH5() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("textml;charset=UTF-8");
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		response.setHeader("Access-Control-Max-Age", "0");
		response.setHeader("Access-Control-Allow-Headers",
				"Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("XDomainRequestAllowed", "1");
		try {
			Member member = new Member();
			if (memberId > 0) { // memberId为-1,则为游客身份登录, 直接判断,不用再去数据库查询是否有此会员
				member = memberManagerDao.retrieveMember(memberId);
				if (ObjValid.isValid(member)) {
					if (!accessToken.equals(member.getAccessToken())) {
						object = new ResponseBean(Constants.EXCEPTION_CODE_600, Constants.LOGIN_VERIFY_TOKEN_ERROR);
						return "success";
					}
				}
			}
			if (type.equals("0") || type.equals("1")) {
				StandardReadSearchResult2 result = new StandardReadSearchResult2();
				Pager pager = qualityShareManagerDao.getQualityTypeList2H5(pageNo, pageSize, qualityId, type, member);
				result.setStandardReadingList(pager.getResultList());
				result.setType(type);
				result.setRowCount(pager.getRowCount());
				object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, result);
			} else {
				object = new ResponseBean(Constants.EXCEPTION_CODE_500, "亲，请输入正确的解读类型(1：连载解读 0：单一解读)");
			}

		} catch (Exception e) {
			e.printStackTrace();
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
		}
		return "success";
	}

	/**
	 * 2.38 Web端首页推荐专家
	 */
	public String getExpertH5() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("textml;charset=UTF-8");
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		response.setHeader("Access-Control-Max-Age", "0");
		response.setHeader("Access-Control-Allow-Headers",
				"Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("XDomainRequestAllowed", "1");
		try {
			CatalogResult resultList = new CatalogResult();
			List<CatalogVo1> cataList = qualityShareManagerDao.getExpertH5(standardReadingId);
			resultList.setStandardReadId(String.valueOf(standardReadingId));
			resultList.setCatalogList(cataList);
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, resultList);

		} catch (Exception e) {
			e.printStackTrace();
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
		}
		return "success";
	}

	/**
	 * 2.39 第五期的文章(质量分享+标准分享)列表
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getArticleList() {
		StandardReadListResult3 resultList=new StandardReadListResult3();
		Pager pager=qualityShareManagerDao.getArticelList(pageNo,pageSize,qualityId,memberId);
		resultList.setRowCount(pager.getRowCount());
		if (pager.getRowCount() > 0) {
			resultList.setArticleList(pager.getResultList());
		} else {
			List<StandardReadListVo4> articleList = new ArrayList<StandardReadListVo4>();
			resultList.setArticleList(articleList);
		}
		object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, resultList);
		return "success";
	}

	/**
	 * 2.40  第五期"他的文章"或"我的文章"列表
	 */
	@SuppressWarnings("unchecked")
	public String getHisArticleList() {
		StandardReadListResult3 resultList = new StandardReadListResult3();
		Member member = new Member();
		if (memberId > 0) { // memberId为-1,则为游客身份登录, 直接判断,不用再去数据库查询是否有此会员
			member = memberManagerDao.retrieveMember(memberId);
			if (ObjValid.isValid(member)) {
				if (!accessToken.equals(member.getAccessToken())) {
					object = new ResponseBean(Constants.EXCEPTION_CODE_600, Constants.LOGIN_VERIFY_TOKEN_ERROR);
					return "success";
				}
			}
		}
		Pager pager = qualityShareManagerDao.getHisArticleList(pageNo, pageSize, member,expertId);
		resultList.setRowCount(pager.getRowCount());
		if (pager.getRowCount() > 0) {
			resultList.setArticleList(pager.getResultList());
		} else {
			List<StandardReadListVo4> articleList = new ArrayList<StandardReadListVo4>();
			resultList.setArticleList(articleList);
		}
		object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, resultList);
		return "success";
	}

	// =========================各种getter/setter方法=============================

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getPageSize1() {
		return pageSize1;
	}

	public void setPageSize1(Integer pageSize1) {
		this.pageSize1 = pageSize1;
	}

	public Integer getPageSize3() {
		return pageSize3;
	}

	public void setPageSize3(Integer pageSize3) {
		this.pageSize3 = pageSize3;
	}

	public String getSearchKeyWord() {
		return searchKeyWord;
	}

	public void setSearchKeyWord(String searchKeyWord) {
		this.searchKeyWord = searchKeyWord;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIsRecommend() {
		return isRecommend;
	}

	public void setIsRecommend(String isRecommend) {
		this.isRecommend = isRecommend;
	}

	public String getQualityId() {
		return qualityId;
	}

	public void setQualityId(String qualityId) {
		this.qualityId = qualityId;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public Long getStandardReadingId() {
		return standardReadingId;
	}

	public void setStandardReadingId(Long standardReadingId) {
		this.standardReadingId = standardReadingId;
	}

	public StandardReading getStandardReading() {
		return standardReading;
	}

	public void setStandardReading(StandardReading standardReading) {
		this.standardReading = standardReading;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getExpertId() {
		return expertId;
	}

	public void setExpertId(Long expertId) {
		this.expertId = expertId;
	}

	public String getAttentionStatus() {
		return attentionStatus;
	}

	public void setAttentionStatus(String status) {
		this.attentionStatus = status;
	}

	public Date getToday() {
		return today;
	}

	public void setToday(Date today) {
		this.today = today;
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

	public String getStandardCode() {
		return standardCode;
	}

	public void setStandardCode(String standardCode) {
		this.standardCode = standardCode;
	}

}