package cc.messcat.bases;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * 该类扩展了HibernateDaoSupport,用来提供特殊的查询方式
 */
public class BaseHibernateDaoSupport extends HibernateDaoSupport {
	/**
	 * 查询单个持久对象
	 * @param hql
	 * @param conditions
	 * @return
	 */
	public <T> T findObject(String hql, Object... conditions) {
		List list = this.getHibernateTemplate().find(hql, conditions);
		if (list != null && list.size() > 0) {
			return (T)list.get(0);
		} else {
			return null;
		}
	}
	
	/**
	 * 分页查询
	 * @param hql
	 * @param start
	 * @param pageSize
	 * @param conditions
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> findPageObjects(final String hql, final int pageStart, final int pageSize ,final Object... conditions) {
		return (List<T>) this.getHibernateTemplate().executeWithNativeSession(new HibernateCallback() {
			@Override
			public List<?> doInHibernate(Session session) throws HibernateException {
				Query query = session.createQuery(hql);				
				for(int i=0;i<conditions.length;i++){
					query.setParameter(i, conditions[i]);
				}
				int startIndex = pageSize * (pageStart - 1);
				query.setFirstResult(startIndex);
				query.setMaxResults(pageSize);
				return query.list();
			}
		});
	}
	
	/**
	 * 查询多个持久对象
	 * @param hql
	 * @param conditions
	 * @return
	 */
	public <T> List<T> findObjects(String hql,Object... conditions) {		
		return (List<T>)this.getHibernateTemplate().find(hql, conditions);
	}
	
	/**
	 * 查询多个持久对象，指定记录条数
	 */
	public <T> List<T> findLimitObjects(final String hql,final int limit,final Object... conditions) {		
		return (List<T>) this.getHibernateTemplate().executeWithNativeSession(new HibernateCallback() {
			@Override
			public List<?> doInHibernate(Session session) throws HibernateException {
				Query query = session.createQuery(hql);				
				for(int i=0;i<conditions.length;i++){
					query.setParameter(i, conditions[i]);
				}				
				query.setMaxResults(limit);
				return query.list();
			}
		});
	}
	
	/**
	 * 批量删除记录
	 * @param hql
	 * @param conditions
	 * @return
	 */
	public int updateObjects(final String hql, final Object... conditions) {
		return (Integer) this.getHibernateTemplate().executeWithNativeSession(new HibernateCallback() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException {
				try{
					Query query = session.createQuery(hql);
					for(int i=0;i<conditions.length;i++){
						query.setParameter(i, conditions[i]);
					}				
					return query.executeUpdate();
				}catch(Exception e){
					e.printStackTrace();
				}
				return 0;
			}
		});		
	}
}
