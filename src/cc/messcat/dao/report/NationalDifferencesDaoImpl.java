package cc.messcat.dao.report;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import cc.modules.commons.Pager;
import cc.messcat.bases.BaseDaoImpl;
import cc.messcat.entity.NationalDifferences;

public class NationalDifferencesDaoImpl extends BaseDaoImpl implements NationalDifferencesDao {

	public NationalDifferencesDaoImpl() {
	}
	public NationalDifferences get(Long id) {
		return (NationalDifferences) getHibernateTemplate().get(NationalDifferences.class, id);
	}

	public List findAll() {
		return getHibernateTemplate().find("from NationalDifferences where status = 1 order by editTime desc");
	}

	public Pager getPager(int pageSize, int pageNo) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(NationalDifferences.class);
		criteria.add(Restrictions.eq("status","1"));
		criteria.addOrder(Order.desc("editTime"));
		int rowCount = ((Integer) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		criteria.setProjection(null);
		int startIndex = pageSize * (pageNo - 1);
		criteria.setFirstResult(startIndex);
		criteria.setMaxResults(pageSize);
		List result = criteria.list();
		return new Pager(pageSize, pageNo, rowCount, result);
	}
	
	/**
	 * 根据条件查询
	 */
	public List<NationalDifferences> findByhql(String string) {
		return this.getHibernateTemplate().find(string);
	}
	

}