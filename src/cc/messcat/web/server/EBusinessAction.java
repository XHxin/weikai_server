package cc.messcat.web.server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;

import cc.messcat.entity.Member;
import cc.messcat.vo.EBusinessInfoVo2;
import cc.messcat.vo.EBusinessInfoVoResult;
import cc.messcat.vo.ResponseBean;
import cc.modules.commons.PageAction;
import cc.modules.commons.Pager;
import cc.modules.constants.Constants;
import cc.modules.util.ObjValid;

@SuppressWarnings("serial")
public class EBusinessAction extends PageAction implements ServletRequestAware{

	private String searchKeyWord;
	private String name;

	private Object object;

	private String accessToken;

	private Long memberId;

	private Long ebusinessProductId;
	
	private HttpServletRequest request;

	/**
	 * 3.9电商信息列表
	 * 第四期要优化后的接口
	 * 点击查看更多的接口
	 */
	@SuppressWarnings("unchecked")
	public String getEProductsBySearchKeyWord() throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("textml;charset=UTF-8");
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		response.setHeader("Access-Control-Max-Age", "0");
		response.setHeader("Access-Control-Allow-Headers",
				"Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("XDomainRequestAllowed", "1");
		try {
			EBusinessInfoVoResult result = new EBusinessInfoVoResult();
			Pager pager = businessInfoManagerDao.getEBusinessInfoByEProduct(searchKeyWord, pageNo, pageSize);
			result.setResultList(pager.getResultList());
			result.setPageNo(pager.getPageNo());
			result.setPageSize(pager.getPageSize());
			result.setRowCount(pager.getRowCount());
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, result);
		} catch (Exception e) {
			e.printStackTrace();
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
		}
		return "success";
	}
	
	
	/**
	 * 3.9  电商信息列表
	 * 
	 */
	@SuppressWarnings("unchecked")
	public String getEProductsByName() throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("textml;charset=UTF-8");
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		response.setHeader("Access-Control-Max-Age", "0");
		response.setHeader("Access-Control-Allow-Headers",
				"Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("XDomainRequestAllowed", "1");
		try {
			EBusinessInfoVoResult result = new EBusinessInfoVoResult();
			Pager pager = businessInfoManagerDao.getEBusinessInfoByEProduct(name, pageNo, pageSize);
			result.setResultList(pager.getResultList());
			result.setPageNo(pager.getPageNo());
			result.setPageSize(pager.getPageSize());
			result.setRowCount(pager.getRowCount());
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, result);
		} catch (Exception e) {
			e.printStackTrace();
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
		}
		return "success";
	}

	/**
	 * 3.10 电商详情
	 */
	public String getAppEBusiness() throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("textml;charset=UTF-8");
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		response.setHeader("Access-Control-Max-Age", "0");
		response.setHeader("Access-Control-Allow-Headers",
				"Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("XDomainRequestAllowed", "1");
		EBusinessInfoVo2 eBusinessInfoVo = null;
		try {
			Member member=new Member();
			if(memberId>0){   //memberId为-1,则为游客身份登录, 直接判断,不用再去数据库查询是否有此会员
				member = memberManagerDao.retrieveMember(memberId);
				if (ObjValid.isValid(member)) {
					if (!accessToken.equals(member.getAccessToken())) {
						object = new ResponseBean(Constants.EXCEPTION_CODE_600, Constants.LOGIN_VERIFY_TOKEN_ERROR);
						return "success";
					}
				}
			}
			// 根据传过来的产品id查
			eBusinessInfoVo = businessInfoManagerDao.getEBusiness(ebusinessProductId, member);
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, eBusinessInfoVo);
		} catch (Exception e) {
			e.printStackTrace();
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
		}
		return "success";
	}

	public String getSearchKeyWord() {
		return searchKeyWord;
	}

	public void setSearchKeyWord(String searchKeyWord) {
		this.searchKeyWord = searchKeyWord;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public Long getEbusinessProductId() {
		return ebusinessProductId;
	}

	public void setEbusinessProductId(Long ebusinessProductId) {
		this.ebusinessProductId = ebusinessProductId;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
