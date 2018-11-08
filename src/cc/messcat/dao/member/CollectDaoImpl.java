package cc.messcat.dao.member;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import cc.modules.commons.Pager;
import cc.modules.util.ObjValid;
import cc.messcat.bases.BaseDaoImpl;
import cc.messcat.entity.Collect;
import cc.messcat.entity.Standard;
import cc.messcat.entity.StandardReading;

public class CollectDaoImpl extends BaseDaoImpl implements CollectDao {

	public CollectDaoImpl() {
	}
	
	public void save(Collect collect) {
		getHibernateTemplate().save(collect);
	}
	
	public void update(Collect collect) {
		getHibernateTemplate().update(collect);
	}
	
	public void delete(Collect collect) {
		collect.setStatus("0");
		getHibernateTemplate().update(collect);
	}

	public void delete(Long id) {
		Collect collect = this.get(id);
//		collect.setStatus("0");
		getHibernateTemplate().delete(collect);
	}
	
	public Collect get(Long id) {
		return (Collect) getHibernateTemplate().get(Collect.class, id);
	}

	public List findAll() {
		return getHibernateTemplate().find("from Collect where status = 1 order by editTime desc");
	}

	public Pager getPager(int pageSize, int pageNo) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(Collect.class);
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
	public List<Collect> findByhql(String string) {
		return this.getHibernateTemplate().find(string);
	}
	
	/**
	 * 根据条件查询收藏
	 */
	@SuppressWarnings("unchecked")
	public List<Collect> getCollectByCon(Collect collect){
		StringBuffer sb = new StringBuffer("from Collect where status = 1 ");
		if(ObjValid.isValid(collect.getMemberId())) {
			sb.append(" and memberId = ").append(collect.getMemberId()).append(" ");
		}
		if(ObjValid.isValid(collect.getRegionId())) {
			sb.append(" and regionId = ").append(collect.getRegionId()).append(" ");
		}
		if(ObjValid.isValid(collect.getRelatedId())) {
			sb.append(" and relatedId = ").append(collect.getRelatedId()).append(" ");
		}
		if(ObjValid.isValid(collect.getType())) {
			if(collect.getType() == 2||collect.getType() == 3) {
				sb.append(" and type= 2 or type=3 ");
			}else {
				sb.append(" and type = ").append(collect.getType()).append(" ");
			}
		}
		List<Collect> list = this.getHibernateTemplate().find(sb.toString());
		return list;
	}

	@Override
	public Standard getByRefionIdAndProductId(Long regionId, Long productId) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Standard.class);
		criteria.createAlias("regionId", "regionId");
		criteria.add(Restrictions.eq("regionId.id", regionId));
		criteria.createAlias("productId", "productId");
		criteria.add(Restrictions.eq("productId.id", productId));
		List list = criteria.list();
		if(null!=list&&list.size()>0){
			return (Standard) list.get(0);
		}
		return null;
	}

	@Override
	public StandardReading getByStandardReadingIdAndType(Long id, String type) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(StandardReading.class);
//		criteria.createAlias("regionId", "regionId");
		criteria.add(Restrictions.eq("id", id));
		criteria.add(Restrictions.eq("classify", type));
		List list = criteria.list();
		if(null!=list&&list.size()>0){
			return (StandardReading) list.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean getCollect(Long memberId, Long readId, String type) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(Collect.class);
		criteria.add(Restrictions.eq("memberId", memberId));
		criteria.add(Restrictions.eq("relatedId", readId));
		criteria.add(Restrictions.eq("type", Integer.valueOf(type)));
		List<Collect> list = criteria.list();
		if(null!=list&&list.size()>0){
			return true;
		}
		return false;
	}

	@Override
	public Collect getCollect(Long memberId, Long videoId) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(Collect.class);
		criteria.add(Restrictions.eq("memberId", memberId));
		criteria.add(Restrictions.eq("relatedId", videoId));
		List<Collect> collects = criteria.list();
		if(!collects.isEmpty() && collects.size() > 0) {
			return collects.get(0);
		}
		return null;
	}

	@Override
	public int findAllCollectByMemberId(Long memberId) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(Collect.class);
		criteria.add(Restrictions.eq("status", "1"));
		criteria.add(Restrictions.eq("memberId", memberId));
		List<Collect> collects = criteria.list();
		if(!collects.isEmpty() && collects.size() > 0) {
			return collects.size();
		}
		return 0;
	}
	

}