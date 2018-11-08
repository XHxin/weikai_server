package cc.modules.util;

import javax.servlet.ServletContext;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author HASEE
 * 全局缓存servletcontext
 */
public class ServletContextUtil {

	private static ServletContext servletContext = null;
	private ServletContextUtil(){};
	
	public synchronized static ServletContext get(){
		if(null == servletContext){
			WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
			servletContext= webApplicationContext.getServletContext();
		}
		return servletContext;
	}
}
