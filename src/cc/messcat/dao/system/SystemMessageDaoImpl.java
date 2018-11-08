package cc.messcat.dao.system;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import cc.messcat.bases.BaseDaoImpl;
import cc.messcat.entity.Member;
import cc.messcat.entity.SystemMessage;
import cc.modules.commons.Pager;

public class SystemMessageDaoImpl extends BaseDaoImpl implements SystemMessageDao {

	public SystemMessageDaoImpl() {
	}
	
	public void save(SystemMessage systemMessage) {
		getHibernateTemplate().save(systemMessage);
	}
	
	public void update(SystemMessage systemMessage) {
		getHibernateTemplate().update(systemMessage);
	}
	
	public void delete(SystemMessage systemMessage) {
		systemMessage.setStatus("0");
		getHibernateTemplate().update(systemMessage);
	}

	public void delete(Long id) {
		SystemMessage systemMessage = this.get(id);
		systemMessage.setStatus("0");
		getHibernateTemplate().update(systemMessage);
	}
	
	public SystemMessage get(Long id) {
		return (SystemMessage) getHibernateTemplate().get(SystemMessage.class, id);
	}

	public List findAll() {
		return getHibernateTemplate().find("from SystemMessage where status = 1 order by editTime desc");
	}

	public Pager getPager(int pageSize, int pageNo) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(SystemMessage.class);
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
	public List<SystemMessage> findByhql(String string) {
		return this.getHibernateTemplate().find(string);
	}

	@Override
	public Member getMemberById(Long memberId) {
		return (Member) getSession().get(Member.class,memberId);
	}

	@Override
	public List<SystemMessage> findByhql(String hql, int pageSize, int pageNo) {
		
		Query query = getSession().createQuery(hql);
		query.setFirstResult(pageSize * (pageNo - 1));
		query.setMaxResults(pageSize * pageNo);

		@SuppressWarnings("unchecked")
		List<SystemMessage> list = query.list();

		return list;
	}
	
}