package cc.messcat.dao.ebusiness;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import cc.modules.commons.Pager;
import cc.modules.util.CollectionUtil;
import cc.messcat.bases.BaseDaoImpl;
import cc.messcat.entity.ExpenseTotal;
import cc.messcat.entity.Collect;
import cc.messcat.entity.EBusinessInfo;
import cc.messcat.entity.EBusinessProduct;
import cc.messcat.entity.Share;

public class EBusinessInfoDaoImpl extends BaseDaoImpl implements EBusinessInfoDao {

	public EBusinessInfoDaoImpl() {
	}

	@Override
	public void save(EBusinessInfo info) {
		getHibernateTemplate().save(info);
	}

	@Override
	public void update(EBusinessInfo info) {
		getHibernateTemplate().update(info);
	}

	@Override
	public void delete(EBusinessInfo info) {
		info.setStatus("0");
		getHibernateTemplate().update(info);
	}

	@Override
	public void delete(Long id) {
		EBusinessInfo info = this.get(id);
		info.setStatus("0");
		getHibernateTemplate().update(info);
	}

	@Override
	public EBusinessInfo get(Long id) {
		return (EBusinessInfo) getHibernateTemplate().get(EBusinessInfo.class, id);
	}

	@Override
	public List findAll() {
		return getHibernateTemplate().find("from EBusinessInfo where status = 1 order by editTime desc");
	}

	@Override
	public Pager getPager(int pageSize, int pageNo) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(EBusinessInfo.class);
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
	@Override
	public List<EBusinessInfo> findByhql(String string) {
		return this.getHibernateTemplate().find(string);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean getCollect(Long relatedID, Long memberId) {
		Session session= this.getSession();//getHibernateTemplate().getSessionFactory().openSession();
		Criteria criteria=session.createCriteria(Collect.class);
		criteria.add(Restrictions.eq("memberId", memberId));
		criteria.add(Restrictions.eq("relatedId", relatedID));
		criteria.add(Restrictions.eq("status", "1"));
		criteria.add(Restrictions.eq("type", 6));    //type=6为电商信息
		List<Collect> list=criteria.list();
		if(CollectionUtil.isListNotEmpty(list)){
			//session.close();
			return true;
		}
		//session.close();
		return false;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean getBuys(Long productID, Long memberId) {
		Session session=this.getSession();
		Criteria criteria=session.createCriteria(ExpenseTotal.class);
		criteria.add(Restrictions.eq("type", "6"));       //type=6为电商信息
		criteria.add(Restrictions.eq("relatedId", productID));
		criteria.add(Restrictions.eq("memberId", memberId));
		criteria.add(Restrictions.eq("payStatus", "1"));
		List list=criteria.list();
		if(CollectionUtil.isListNotEmpty(list)){
			return true;
		}
		return false;
	}

	/**
	 * 获取电商产品信息
	 */
	@SuppressWarnings("unchecked")
	@Override
	public EBusinessProduct getProduct(Long subEbusinessProductId) {
		Session session=this.getSession();//getHibernateTemplate().getSessionFactory().openSession();
		Criteria criteria=session.createCriteria(EBusinessProduct.class);
		criteria.add(Restrictions.eq("id", subEbusinessProductId));
		List<EBusinessProduct> list=criteria.list();
		if(CollectionUtil.isListNotEmpty(list)){
			return list.get(0);
		}
		//session.close();
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getShareURL() {
		Criteria criteria=getSession().createCriteria(Share.class);
		criteria.add(Restrictions.eq("shareType", "3"));
		criteria.add(Restrictions.eq("status", "1"));
		List<Share> list=criteria.list();
		if(list!=null&&list.size()!=0){
			return list.get(0).getShareURL();
		}
		return null;
	}
}