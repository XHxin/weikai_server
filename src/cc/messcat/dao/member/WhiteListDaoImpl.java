package cc.messcat.dao.member;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import cc.messcat.bases.BaseDaoImpl;
import cc.messcat.entity.WhiteList;

public class WhiteListDaoImpl extends BaseDaoImpl implements WhiteListDao {

	@Override
	public WhiteList getWhiteList(String mobile) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(WhiteList.class);
		criteria.add(Restrictions.eq("status", 1))
			.add(Restrictions.eq("mobile", mobile));
		List<WhiteList> whiteLists = criteria.list();
		if(whiteLists != null && whiteLists.size() > 0) {
			return whiteLists.get(0);
		}
		return null;
	}

}
