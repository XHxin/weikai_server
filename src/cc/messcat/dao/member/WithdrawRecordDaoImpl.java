package cc.messcat.dao.member;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import cc.modules.commons.Pager;
import cc.messcat.bases.BaseDaoImpl;
import cc.messcat.entity.WithdrawRecord;
public class WithdrawRecordDaoImpl extends BaseDaoImpl implements WithdrawRecordDao {

	public WithdrawRecordDaoImpl() {
	}
	
	public void save(WithdrawRecord withdrawRecord) {
		getHibernateTemplate().save(withdrawRecord);
	}
	
	public void update(WithdrawRecord withdrawRecord) {
		getHibernateTemplate().update(withdrawRecord);
	}
	
	public void delete(WithdrawRecord withdrawRecord) {
		withdrawRecord.setStatus("0");
		getHibernateTemplate().update(withdrawRecord);
	}

	public void delete(Long id) {
		WithdrawRecord withdrawRecord = this.get(id);
		withdrawRecord.setStatus("0");
		getHibernateTemplate().update(withdrawRecord);
	}
	
	public WithdrawRecord get(Long id) {
		return (WithdrawRecord) getHibernateTemplate().get(WithdrawRecord.class, id);
	}

	public List findAll() {
		return getHibernateTemplate().find("from WithdrawRecord where status = 1 order by editTime desc");
	}

	public Pager getPager(int pageSize, int pageNo) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(WithdrawRecord.class);
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
	public List<WithdrawRecord> findByhql(String string) {
		return this.getHibernateTemplate().find(string);
	}
	

}