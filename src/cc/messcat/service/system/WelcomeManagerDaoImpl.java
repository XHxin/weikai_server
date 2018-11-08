package cc.messcat.service.system;

import cc.messcat.bases.BaseManagerDaoImpl;
import cc.messcat.entity.Versions;

/**
 * @author nelson
 *	欢迎页
 */
@SuppressWarnings("serial")
public class WelcomeManagerDaoImpl extends BaseManagerDaoImpl implements WelcomeManagerDao{

	@Override
	public Versions getAllVersion(String terminal, Long versionCode) {
		return welcomeDao.getAllVersion(terminal,versionCode);
	}

	
}
