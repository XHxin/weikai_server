package cc.messcat.web.server;


import cc.messcat.bases.BaseAction;
import cc.messcat.vo.ResponseBean;
import cc.modules.constants.Constants;

/**
 * @author xiehuaxin
 * @createDate 2018年5月30日 下午2:07:24
 * @todo 用于写一些非模块性的接口
 */
public class SystemAction extends BaseAction{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String version;//ios App版本号
	private Object object;

	/**
	 * ios传一个版本号，返回这个版本是否正在审核
	 * @return
	 */
	public String getCheckStatus() {
		Integer checkStatus = systemManagerDao.checkStatus(version);
		object = new ResponseBean(Constants.SUCCESS_CODE_200,Constants.MSG_SUCCESS, checkStatus);
		return "success";
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}
	
	
}
