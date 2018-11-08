package cc.messcat.dao.system;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import cc.modules.commons.Pager;
import cc.messcat.bases.BaseDaoImpl;
import cc.messcat.entity.ExpenseExpert;

public class ExpenseExpertDaoImpl extends BaseDaoImpl implements ExpenseExpertDao {

	public ExpenseExpertDaoImpl() {
	}
	
	public void save(ExpenseExpert expertIE) {
		getHibernateTemplate().save(expertIE);
	}
	
	public void update(ExpenseExpert expertIE) {
		getHibernateTemplate().update(expertIE);
	}
	
	public void delete(ExpenseExpert expertIE) {
		getHibernateTemplate().update(expertIE);
	}

	public void delete(Long id) {
		ExpenseExpert expertIE = this.get(id);
		getHibernateTemplate().update(expertIE);
	}

	public ExpenseExpert get(Long id) {
		return (ExpenseExpert) getHibernateTemplate().get(ExpenseExpert.class, id);
	}

	public List findAll() {
		return getHibernateTemplate().find("from ExpenseExpert where status = 1 order by editTime desc");
	}

	public Pager getPager(int pageSize, int pageNo) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(ExpenseExpert.class);
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
	public List<ExpenseExpert> findByhql(String string) {
		return this.getHibernateTemplate().find(string);
	}
	

}