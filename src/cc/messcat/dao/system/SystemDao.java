package cc.messcat.dao.system;

import cc.messcat.entity.AppCheck;

/**
 * @author xiehuaxin
 * @createDate 2018年5月30日 下午2:19:56
 * @todo TODO
 */
public interface SystemDao {

	AppCheck findAppCheckByAppVersion(String version);

}
