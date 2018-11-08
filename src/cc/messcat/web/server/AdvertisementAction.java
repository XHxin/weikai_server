package cc.messcat.web.server;
import cc.messcat.vo.AdvertisementVo;
import cc.messcat.vo.ResponseBean;
import cc.modules.commons.PageAction;
import cc.modules.constants.Constants;
/**
 * @author nelson
 */
@SuppressWarnings("serial")
public class AdvertisementAction extends PageAction{

	private Object object;
	private int type;
	
	public String getAdvertisement(){
		AdvertisementVo vo = advertisementManagerDao.getAdvertisement(type);
		object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, vo);
		return "success";
	}
	
	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	
}
