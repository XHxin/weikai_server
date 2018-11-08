package cc.messcat.dao.member;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import cc.modules.commons.Pager;
import cc.messcat.bases.BaseDaoImpl;
import cc.messcat.entity.Packages;

public class PackagesDaoImpl extends BaseDaoImpl implements PackagesDao {

	public PackagesDaoImpl() {
	}
	
	public void save(Packages packages) {
		getHibernateTemplate().save(packages);
	}
	
	public void update(Packages packages) {
		getHibernateTemplate().update(packages);
	}
	
	public void delete(Packages packages) {
		packages.setStatus("0");
		getHibernateTemplate().update(packages);
	}

	public void delete(Long id) {
		Packages packages = this.get(id);
		packages.setStatus("0");
		getHibernateTemplate().update(packages);
	}
	
	public Packages get(Long id) {
		return (Packages) getHibernateTemplate().get(Packages.class, id);
	}

	public List findAll() {
		return getHibernateTemplate().find("from Packages where status = 1 order by editTime desc");
	}

	public Pager getPager(int pageSize, int pageNo) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(Packages.class);
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
	public List<Packages> findByhql(String string) {
		return this.getHibernateTemplate().find(string);
	}
	

}