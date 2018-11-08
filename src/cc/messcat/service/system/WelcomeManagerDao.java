package cc.messcat.service.system;

import cc.messcat.entity.Versions;

/**
 * @author nelson
 *	欢迎页
 */
public interface WelcomeManagerDao {

	//查询当前所有版本
	public Versions getAllVersion(String terminal,Long versionCode);
}
