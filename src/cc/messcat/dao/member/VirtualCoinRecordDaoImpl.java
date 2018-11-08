package cc.messcat.dao.member;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import cc.modules.commons.Pager;
import cc.messcat.bases.BaseDaoImpl;
import cc.messcat.entity.VirtualCoinRecord;

public class VirtualCoinRecordDaoImpl extends BaseDaoImpl implements VirtualCoinRecordDao {

	public VirtualCoinRecordDaoImpl() {
	}
	
	public void save(VirtualCoinRecord virtualCoinRecord) {
		getHibernateTemplate().save(virtualCoinRecord);
	}
	
	public void update(VirtualCoinRecord virtualCoinRecord) {
		getHibernateTemplate().update(virtualCoinRecord);
	}
	
	public void delete(VirtualCoinRecord virtualCoinRecord) {
		virtualCoinRecord.setStatus("0");
		getHibernateTemplate().update(virtualCoinRecord);
	}

	public void delete(Long id) {
		VirtualCoinRecord virtualCoinRecord = this.get(id);
		virtualCoinRecord.setStatus("0");
		getHibernateTemplate().update(virtualCoinRecord);
	}
	
	public VirtualCoinRecord get(Long id) {
		return (VirtualCoinRecord) getHibernateTemplate().get(VirtualCoinRecord.class, id);
	}

	public List findAll() {
		return getHibernateTemplate().find("from VirtualCoinRecord where status = 1 order by editTime desc");
	}

	public Pager getPager(int pageSize, int pageNo) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(VirtualCoinRecord.class);
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
	public List<VirtualCoinRecord> findByhql(String string) {
		return this.getHibernateTemplate().find(string);
	}
	

}