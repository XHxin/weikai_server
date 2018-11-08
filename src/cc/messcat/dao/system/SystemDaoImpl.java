package cc.messcat.dao.system;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import cc.messcat.bases.BaseDaoImpl;
import cc.messcat.entity.AppCheck;

/**
 * @author xiehuaxin
 * @createDate 2018年5月30日 下午2:20:20
 * @todo TODO
 */
public class SystemDaoImpl extends BaseDaoImpl implements SystemDao{

	@Override
	public AppCheck findAppCheckByAppVersion(String version) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(AppCheck.class);
		criteria.add(Restrictions.eq("appVersion", version));
		List<AppCheck> appCheckList = criteria.list();
		if(appCheckList.size() > 0) {
			return appCheckList.get(0);
		}
		return null;
	}

}
