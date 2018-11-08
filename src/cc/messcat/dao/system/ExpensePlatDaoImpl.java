package cc.messcat.dao.system;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import cc.modules.commons.Pager;
import cc.messcat.bases.BaseDaoImpl;
import cc.messcat.entity.Member;
import cc.messcat.entity.ExpensePlatform;

public class ExpensePlatDaoImpl extends BaseDaoImpl implements ExpensePlatDao {

	public ExpensePlatDaoImpl() {
	}
	
	public void save(ExpensePlatform platformIncome) {
		getHibernateTemplate().save(platformIncome);
	}
	
	public void update(ExpensePlatform platformIncome) {
		getHibernateTemplate().update(platformIncome);
	}
	
	public void delete(ExpensePlatform platformIncome) {
		getHibernateTemplate().update(platformIncome);
	}

	public void delete(Long id) {
		ExpensePlatform platformIncome = this.get(id);
		getHibernateTemplate().update(platformIncome);
	}
	
	public ExpensePlatform get(Long id) {
		return (ExpensePlatform) getHibernateTemplate().get(ExpensePlatform.class, id);
	}

	public List findAll() {
		return getHibernateTemplate().find("from Member where status = 1 order by editTime desc");
	}

	public Pager getPager(int pageSize, int pageNo) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(Member.class);
		
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
	
	public Pager getPager(int pageSize, int pageNo, ExpensePlatform platformIncome) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(Member.class);
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
	public List<ExpensePlatform> findByhql(String string) {
		return this.getHibernateTemplate().find(string);
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public List<ExpensePlatform> getBuysRecordList(String type, Long memberId, int pageNo, int pageSize) {
		Session session=this.getSession();// getHibernateTemplate().getSessionFactory().openSession();
		Criteria criteria=session.createCriteria(ExpensePlatform.class);
		criteria.add(Restrictions.eq("memberId", memberId));
		criteria.add(Restrictions.eq("type", type));
		int startIndex = pageSize * (pageNo - 1);
		criteria.setFirstResult(startIndex);
		criteria.setMaxResults(pageSize);
		List<ExpensePlatform> result = criteria.list();
		return result;
	}

	@Override
	public Pager findPlatformIncomeByCondition(int pageNo, int pageSize, ExpensePlatform platformIncome) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(Member.class);
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

	@Override
	public ExpensePlatform getExpensePlatformByExpenseTotalId(Long expenseTotalId) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(ExpensePlatform.class);
		criteria.add(Restrictions.eq("expenseTotalId",expenseTotalId));
		List<ExpensePlatform> expensePlatformList = criteria.list();
		if(expensePlatformList != null && expensePlatformList.size() > 0) {
			return expensePlatformList.get(0);
		}
		return null;
	}
}