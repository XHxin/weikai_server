package cc.messcat.dao.system;

import cc.messcat.bases.BaseDao;
import cc.messcat.entity.Versions;

public interface WelcomeDao extends BaseDao{

	//查询当前所有版本

	public Versions getAllVersion(String terminal, Long versionCode);
}
