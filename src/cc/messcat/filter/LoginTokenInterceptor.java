package cc.messcat.filter;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.StrutsStatics;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;

import cc.messcat.entity.Member;
import cc.messcat.service.member.MemberManagerDao;
import cc.messcat.vo.ResponseBean;
import cc.modules.constants.Constants;
import cc.modules.security.ExceptionManager;
import cc.modules.util.StringUtil;

/**
 * token校验拦截器，某些方法执行之前，先校验是否有携带必须的参数，请求接口
 * 
 * @author Geoff
 * @Version 1.0
 */
public class LoginTokenInterceptor extends MethodFilterInterceptor {

	private static final long serialVersionUID = 4311654657953323540L;

	@Override
	public void destroy() {
	}

	@Override
	public void init() {
	}

	/**
	 * 该方法是拦截器的核心方法，主要在此方法里面做拦截器相关的业务逻辑处理。
	 */
	@Override
	protected String doIntercept(ActionInvocation invocation) throws ExceptionManager {
		try {
			ActionContext actionContext = invocation.getInvocationContext();// 获取ActionContext
			HttpServletRequest request = (HttpServletRequest) actionContext.get(StrutsStatics.HTTP_REQUEST);// 获得HttpServletRequest请求

			Long memId =  Long.valueOf(request.getParameter("memberId") ) ;// 会员id
			String loginToken = (String)request.getParameter("accessToken");/* request.getAttribute("loginToken");*/// 会员登录token
			// 查询会员，并做登录token校验
			if (StringUtil.isBlank(loginToken) || memId == null) {
				actionContext.getValueStack().setParameter("object",
					new ResponseBean(Constants.EXCEPTION_CODE_600, Constants.UM_LOGIN_VERIFY_TOKEN));
				return "loginTokenInterceptor";
			}
			// bean注入
			ServletContext context = (ServletContext) actionContext.get(StrutsStatics.SERVLET_CONTEXT);
			ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(context);
			MemberManagerDao memberAppManagerDao = (MemberManagerDao) ctx.getBean("memberManagerDao");
			Member member = memberAppManagerDao.retrieveMember(memId);
			if (member == null) {
				actionContext.getValueStack().setParameter("object", new ResponseBean(Constants.EXCEPTION_CODE_600, "用户不存在"));// 给Action的已经定义好的object对象赋值
				return "loginTokenInterceptor";
			}
			String array = member.getAccessToken();
			if (!loginToken.equals(array)) {
				actionContext.getValueStack().setParameter("object",
					new ResponseBean(Constants.EXCEPTION_CODE_600, Constants.LOGIN_VERIFY_TOKEN_ERROR));// 给Action的已经定义好的object对象赋值
				return "loginTokenInterceptor";
			}
			// loginToken时限校验
			/*Long s = (System.currentTimeMillis() - Long.valueOf(array)) / (1000 * 60 * 60 * 24 * 30);
			if (s >= Long.valueOf(Constants.LOGINTOKEN_LIMIT_TIME)) {
				actionContext.getValueStack().setParameter("object", new ResponseBean(Constants.EXCEPTION_CODE_600, "登录超时，请重新登录"));// 给Action的已经定义好的object对象赋值
				return "loginTokenInterceptor";
			}*/
			actionContext.getValueStack().setParameter("member", member);// 可以给Action的某个对象赋值，关键是ValueStack。
			return invocation.invoke();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
