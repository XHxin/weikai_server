package cc.messcat.service.system;

import cc.messcat.bases.BaseManagerDaoImpl;
import cc.messcat.entity.AppCheck;

@SuppressWarnings("serial")
public class SystemManagerDaoImpl extends BaseManagerDaoImpl implements SystemManagerDao{

	@Override
	public Integer checkStatus(String version) {
		if(null != version) {
			AppCheck check = systemDao.findAppCheckByAppVersion(version);
			if(check != null) {
				return (check.getChecking() == 0 ? 0 : 1);
			}
		}
		return 0;
	}

}
