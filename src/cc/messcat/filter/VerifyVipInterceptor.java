package cc.messcat.filter;

import java.util.Date;

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
import cc.modules.util.StringUtil;

/**
 * @author HASEE
 * 拦截那些与vip权限有关的方法，校验vip信息
 */
public class VerifyVipInterceptor extends MethodFilterInterceptor{

	/**
	 * 
	 */
	private static final long serialVersionUID = -685195048305531099L;

	@Override
	protected String doIntercept(ActionInvocation invocation) throws Exception {
		ActionContext actionContext = invocation.getInvocationContext();//获取actionContext
		HttpServletRequest request = (HttpServletRequest)actionContext.get(StrutsStatics.HTTP_REQUEST);
		
		Long memberId = Long.valueOf(request.getParameter("memberId"));
		String loginToken = (String) request.getParameter("accessToken");
		if(StringUtil.isBlank(loginToken) || memberId == null) {
			actionContext.getValueStack().setParameter("object", new ResponseBean(Constants.EXCEPTION_CODE_600, Constants.UM_LOGIN_VERIFY_TOKEN));
			return "loginTokenInterceptor";
		}
		
		//bean注入
		ServletContext context = (ServletContext)actionContext.get(StrutsStatics.SERVLET_CONTEXT);
		ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(context);
		MemberManagerDao memberManagerDao = (MemberManagerDao)ctx.getBean("memberManagerDao");
		Member member = memberManagerDao.retrieveMember(memberId);
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
		
		//校验会员是否已到期
		if(member.getYearEndTime().before(new Date())) {//年费到期
			if(member.getEndTime().before(new Date())) {//月费到期
				member.setGrade("0");
				member.setType("2");
			}else {
				member.setGrade("1");
				member.setType("1");
			}
		}else {
			member.setGrade("1");
			member.setType("0");
		}
		
		return null;
	}

	
}
