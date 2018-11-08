package cc.messcat.dao.report;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import cc.modules.commons.Pager;
import cc.messcat.bases.BaseDaoImpl;
import cc.messcat.entity.Region;

public class RegionDaoImpl extends BaseDaoImpl implements RegionDao {

	public RegionDaoImpl() {
	}
	public Region get(Long id) {
		return (Region) getHibernateTemplate().get(Region.class, id);
	}

	public List findAll() {
		return getHibernateTemplate().find("from Region where status = 1 order by editTime desc");
	}

	public Pager getPager(int pageSize, int pageNo) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(Region.class);
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
	public List<Region> findByhql(String string) {
		return this.getHibernateTemplate().find(string);
	}
	
	/**
	 * 查询最大地区ID
	 */
	public Long findMaxRegionID() {
		Object o = getHibernateTemplate().find("SELECT MAX(regionId) FROM Region").get(0);
		return (Long) o;
	}
	

}