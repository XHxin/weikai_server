package cc.messcat.dao.report;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import cc.modules.commons.Pager;
import cc.modules.util.ObjValid;
import cc.messcat.bases.BaseDaoImpl;
import cc.messcat.entity.Legal;
import cc.messcat.entity.Product;
import cc.messcat.entity.Share;

public class ProductDaoImpl extends BaseDaoImpl implements ProductDao {

	public ProductDaoImpl() {
	}
	public Product get(Long id) {
		return (Product) getHibernateTemplate().get(Product.class, id);
	}

	public List findAll() {
		return getHibernateTemplate().find("from Product where status = 1 order by editTime desc");
	}

	public Pager getPager(int pageSize, int pageNo) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(Product.class);
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
	public List<Product> findByhql(String string) {
		return this.getHibernateTemplate().find(string);
	}
	
	/*
	 * 获取分享URL
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String getShareURL() {
		Criteria criteria=getSession().createCriteria(Share.class);
		criteria.add(Restrictions.eq("shareType", "2"));
		criteria.add(Restrictions.eq("status", "1"));
		List<Share> list=criteria.list();
		if(list!=null&&list.size()!=0){
			return list.get(0).getShareURL();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Legal getMarketReportItem(Long regionId, Long productId, Long regionFatherId) {
		StringBuffer sb = new StringBuffer("from Legal where status = 1 ");
		if(ObjValid.isValid(regionFatherId)){
			sb.append(" and (regionId.id=").append(regionId)
				.append(" or regionId.id=").append(regionFatherId).append(") ");
		}else{
			sb.append(" and regionId.id=").append(regionId).append(" ");
		}
		sb.append(" and (productIds like '%").append(productId).append(";%' or productIds like '%all%') ");
		Query query=getSession().createQuery(sb.toString());
		List<Legal> list=query.list();
		if(list!=null && list.size()>0) {
			return list.get(0);
		}
		return null;
	}
	

}