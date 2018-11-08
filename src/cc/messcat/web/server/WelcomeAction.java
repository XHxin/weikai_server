package cc.messcat.web.server;
import cc.messcat.bases.BaseAction;
import cc.messcat.entity.Versions;
import cc.messcat.vo.ResponseBean;
import cc.modules.constants.Constants;

/**
 * @author nelson
 *
 */
public class WelcomeAction extends BaseAction{

	private Object object;
	private static final long serialVersionUID = 1L;
	private Long versionCode;// 用户手机上的版本号
	private String terminal;// 设备
	private String isForce;

	public String versionCode() throws Exception {
		// 查询数据库当前最新版本
		Versions versions = new Versions();
		versions = welcomeManagerDao.getAllVersion(terminal, versionCode);
		if (versions != null) {
			versions.setDescrption(versions.getDescrption().replace("\\r\\n", "\r\n"));
			versions.setVersionUrl(versions.getVersionUrl().replace("\\r\\n", "\r\n"));
			String isForceNum = versions.getIsForce();
			if (isForceNum != null && isForceNum.equals("0")) {
				versions.setIsForce("false");
			} else {
				versions.setIsForce("true");
			}
			// 与用户手机上的版本进行比较，如果版本比用户手机上的要新，则返回app更新地址
			if (versions.getVersionCode() > versionCode) {
				object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, versions);
			}
		}else {
			//object = new ResponseBean(Constants.SUCCESS_CODE_200, "当前已是最高版本");
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, versions);
		}
		//object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, new Object());------------------------
		return "success";
	}

	public Long getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(Long versionCode) {
		this.versionCode = versionCode;
	}

	public String getTerminal() {
		return terminal;
	}

	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}

	public String getIsForce() {
		return isForce;
	}

	public void setIsForce(String isForce) {
		this.isForce = isForce;
	}
	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

}
