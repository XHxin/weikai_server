package cc.messcat.bases;

import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import cc.messcat.entity.EnterpriseNews;
import cc.modules.commons.Pager;
import cc.modules.util.DateHelper;
import cc.modules.util.HQLUtil;

public class BaseDaoImpl extends BaseHibernateDaoSupport implements BaseDao {

	public BaseDaoImpl() {
	}

	/**
	 * 添加对象
	 */
	public void save(Object obj) {
		getHibernateTemplate().save(obj);
	}

	/**
	 * 修改对象
	 */
	public void update(Object obj) {
		Session session=getSession();
		session.setFlushMode(FlushMode.AUTO);
		session.flush();
		session.update(obj);
	}
	
	/**
	 * 根据ID删除对象
	 */
	public void delete(Long id, String objName) {
		getHibernateTemplate().delete(get(id, objName));
	}

	/**
	 * 查找所有对象
	 */
	public List getAll(String objName) {
		List all = getHibernateTemplate().find("from " + objName);
		return all;
	}

	/**
	 * 根据ID查找对象
	 */
	public Object get(Long id, String objName) {
		Object obj = getEntityClass(objName);
		if (obj == null) {
			return null;
		} else {
			return getHibernateTemplate().get(obj.getClass(), id);
		}
	}
	
	/**
	 * 获取相应的entity name
	 * @param objName
	 * @return
	 */
	private Object getEntityClass(String objName) {
		Object obj = null;
		try {
			obj = Class.forName(objName).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	public Pager getObjectListByClass(int pageSize, int pageNo, Class classObject, String statu) {
		Session session = null;
		Pager pager = null;
		try {
			session = this.getSession();//session = getHibernateTemplate().getSessionFactory().openSession();
			Criteria criteria = session.createCriteria(classObject);
			if (!"-1".equals(statu))
				criteria.add(Restrictions.eq("status", statu));
			int rowCount = ((Integer) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
			criteria.setProjection(null);
			int startIndex = pageSize * (pageNo - 1);
			criteria.setFirstResult(startIndex);
			criteria.setMaxResults(pageSize);
			List result = criteria.list();
			pager = new Pager(pageSize, pageNo, rowCount, result);
		} catch (Exception ex) {

		} finally {
			session.close();
		}

		return pager;
	}
	
	/**
	 * 批量更新数据
	 * @param hql 更新语句
	 * @param paramters 参数
	 * @return
	 */
	public int updateBatch(String hql,Object paramters[]) {
		Session session = null;
		int ret = 0;
		try {
			session = this.getSession();//session = getHibernateTemplate().getSessionFactory().openSession();
			Transaction trans=session.beginTransaction();
			Query queryupdate=session.createQuery(hql);
			if(null != paramters && paramters.length > 0) {
				for(int i=0; i<paramters.length; i++) {
					queryupdate.setParameter(i, paramters[i]);
				}
			}
			ret=queryupdate.executeUpdate();
			trans.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return ret;
	}
	
	public List getInfoByClassAndSize(String classObject, Long size, Long clickTimes, String isprimPhoto, String isCommend,
		String b_or_s, Long columnId, String isIndexPhoto) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("from ").append(classObject).append(" as temp where 1 = 1 ").toString();
		if (!"-1".equals(isprimPhoto))
			SQL.append(" and temp.isPrimPhoto = 1 ");
		if (!"-1".equals(isIndexPhoto))
			SQL.append(" and temp.isIndexPhoto = 1 ");
		if (!"-1".equals(isCommend))
			SQL.append(" and temp.isCommend = 1 ");
		SQL.append(" and temp.state = 1 ");
		if (!"-1".equals(columnId.toString().trim()))
			SQL.append(" and temp.enterpriseColumn.id = ").append(columnId).append(" ").toString();
		String date = (new DateHelper()).getCurrentDate().toString();
		if (!"EnterpriseInfo".equals(classObject)) {
			SQL.append(" and temp.initTime <= '").append(date.trim()).append("' ");
			SQL.append(" and temp.endTime >= '").append(date.trim()).append("' ");
		}
		if (!"-1".equals(clickTimes.toString()))
			SQL.append(" order by temp.clickTimes desc");
		if (!"-1".equals(clickTimes.toString()))
			SQL.append(" ,temp.isTop desc");
		else
			SQL.append(" order by temp.isTop desc");

		SQL.append(" ,temp.id desc");

		List result = getHibernateTemplate().find(SQL.toString());
		result = result.subList(0, (int) (size <= result.size() ? size : result.size()));
		return result;
	}

	public List getLinksAndAdByClassAndSize(Class classObject, Long size) {
		Session session = this.getSession();//Session session = getHibernateTemplate().getSessionFactory().openSession();
		Criteria criteria = session.createCriteria(classObject);
		criteria.add(Restrictions.eq("state", "1"));
		//		criteria.addOrder(Order.desc("id"));
		criteria.setProjection(null);
		criteria.setFirstResult(0);
		criteria.setMaxResults(Integer.valueOf(size.toString()).intValue());
		List result = criteria.list();
		session.close();
		return result;
	}

	public EnterpriseNews getNews(Long id) {
		List find = getHibernateTemplate().find(
			"from EnterpriseNews as e where e.state = 1 and e.isPrimPhoto = 1 and e.enterpriseColumn.id = ?", id);
		if (find.size() > 0)
			return (EnterpriseNews) find.get(0);
		else
			return null;
	}

	public List findNews(Long id) {
		List find = getHibernateTemplate().find(
			"from EnterpriseNews as e where e.state = 1 and e.enterpriseColumn.id = ?", id);
		return find;
	}
	
	@Override
	public <T> T query(Class<T> entityClass, Map<String,Object> attrs) {		
		return this.findObject(HQLUtil.createQueryHQL(entityClass.getSimpleName(), attrs.keySet()), attrs.values().toArray());
	}
	
}