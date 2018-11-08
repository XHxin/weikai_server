package cc.messcat.dao.member;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import cc.messcat.bases.BaseDaoImpl;
import cc.messcat.entity.BuysRecord;
import cc.messcat.entity.ExpenseTotal;
import cc.modules.commons.Pager;
import cc.modules.util.ObjValid;

public class BuysRecordDaoImpl extends BaseDaoImpl implements BuysRecordDao {

	public BuysRecordDaoImpl() {
	}
	
	public void save(BuysRecord record) {
		getHibernateTemplate().save(record);
	}
	
	public void update(BuysRecord record) {
		getHibernateTemplate().update(record);
	}
	
	public void delete(BuysRecord record) {
		record.setStatus("0");
		getHibernateTemplate().update(record);
	}

	public void delete(Long id) {
		BuysRecord record = this.get(id);
//		collect.setStatus("0");
		getHibernateTemplate().delete(record);
	}
	
	public BuysRecord get(Long id) {
		return (BuysRecord) getHibernateTemplate().get(BuysRecord.class, id);
	}

	public List findAll() {
		return getHibernateTemplate().find("from BuysRecord where status = 1 order by editTime desc");
	}

	public Pager getPager(int pageSize, int pageNo) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(BuysRecord.class);
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
	public List<BuysRecord> findByhql(String string) {
		return this.getHibernateTemplate().find(string);
	}
	
	/**
	 * 根据条件查询购买
	 */
	public List<ExpenseTotal> getBuyRecordByCon(ExpenseTotal record){
		StringBuffer sb = new StringBuffer("from ExpenseTotal where payStatus = 1 ");
		if(ObjValid.isValid(record.getMemberId()))
			sb.append(" and memberId = ").append(record.getMemberId()).append(" ");
		if(ObjValid.isValid(record.getRelatedId()))
			sb.append(" and relatedId = ").append(record.getRelatedId()).append(" ");
		List<ExpenseTotal> list = this.getHibernateTemplate().find(sb.toString());
		return list;
	}
	
	/**
	 * 
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Pager  findBuysRecordsByCondiction( Long memberId, String type, int pageNo, int pageSize ){
		try {
			Session session = this.getSession();
			Criteria criteria = session.createCriteria(BuysRecord.class);
			criteria.add(Restrictions.eq("memberId",memberId));
			criteria.add(Restrictions.eq("payStatus","1"));
			if(!"0".equals(type  )){
				criteria.add(Restrictions.eq("type",type));
			}
			criteria.addOrder(Order.desc("editTime"));
			int rowCount = ((Integer) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
			criteria.setProjection(null);
			int startIndex = pageSize * (pageNo - 1);
			criteria.setFirstResult(startIndex);
			criteria.setMaxResults(pageSize);
			List result = criteria.list();
			return new Pager(pageSize, pageNo, rowCount, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Pager(pageSize, pageNo, 0, new ArrayList());
	}

	@SuppressWarnings("unchecked")
	@Override
	public BuysRecord getBuys(Long memberId, Long readId, String type) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(BuysRecord.class);
		criteria.add(Restrictions.eq("memberId", memberId));
		criteria.add(Restrictions.eq("standardReadId.id", readId));
		criteria.add(Restrictions.eq("payStatus", "1"));
		criteria.add(Restrictions.eq("status", "1"));
		List<BuysRecord> list = criteria.list();
		if(list != null && list.size() != 0 ){
			return list.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BuysRecord getEbusinessBuys(Long memberId, Long ebusinessProductId, String type) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(BuysRecord.class);
		criteria.add(Restrictions.eq("memberId", memberId));
		criteria.add(Restrictions.eq("ebusinessProductId", ebusinessProductId));
		criteria.add(Restrictions.eq("payStatus", "1"));
		criteria.add(Restrictions.eq("status", "1"));
		List<BuysRecord> list = criteria.list();
		if(list != null && list.size() != 0 ){
			return list.get(0);
		}
		return null;
	}

	/**
	 * 用于判断问题是否被购买
	 */
	@SuppressWarnings("unchecked")
	@Override
	public BuysRecord getReplyBuys(Long memberId,Long expertId, Long problemId) {
		Criteria criteria=getSession().createCriteria(BuysRecord.class)
		.add(Restrictions.eq("memberId", memberId))
		.add(Restrictions.eq("hotProblemId", problemId))
//		.add(Restrictions.eq("expertId", expertId))         //有可能出现,用户向不同的专家提同样的问题,所以用这个字段区别不同的专家
		.add(Restrictions.eq("payStatus", "1"))
		.add(Restrictions.eq("type", "4"))
		.add(Restrictions.eq("status", "1"));
		List<BuysRecord> list=criteria.list();
		if(list != null && list.size() != 0 ){
			return list.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BuysRecord getProblemRecord(Long expertId, Long problemId) {
		Criteria criteria=getSession().createCriteria(BuysRecord.class)
				.add(Restrictions.eq("hotProblemId", problemId))
				.add(Restrictions.eq("expertId", expertId))
				.add(Restrictions.eq("payStatus", "1"));
		List<BuysRecord> list=criteria.list();
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public void deleteRecord(BuysRecord buys) {
		getSession().delete(buys);
	}

}