package cc.messcat.dao.ebusiness;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import cc.modules.commons.Pager;
import cc.messcat.bases.BaseDaoImpl;
import cc.messcat.entity.EBusinessProduct;

public class EBusinessProductDaoImpl extends BaseDaoImpl implements EBusinessProductDao {

	public EBusinessProductDaoImpl() {
	}
	
	public void save(EBusinessProduct eproduct) {
		getHibernateTemplate().save(eproduct);
	}
	
	public void update(EBusinessProduct eproduct) {
		getHibernateTemplate().update(eproduct);
	}
	
	public void delete(EBusinessProduct eproduct) {
		eproduct.setStatus("0");
		getHibernateTemplate().update(eproduct);
	}

	public void delete(Long id) {
		EBusinessProduct eproduct = this.get(id);
		eproduct.setStatus("0");
		getHibernateTemplate().update(eproduct);
	}
	
	public EBusinessProduct get(Long id) {
		return (EBusinessProduct) getHibernateTemplate().get(EBusinessProduct.class, id);
	}

	public List findAll() {
		return getHibernateTemplate().find("from EBusinessProduct where status = 1 order by editTime desc");
	}

	public Pager getPager(int pageSize, int pageNo) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(EBusinessProduct.class);
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
	public List<EBusinessProduct> findByhql(String string) {
		return this.getHibernateTemplate().find(string);
	}

}