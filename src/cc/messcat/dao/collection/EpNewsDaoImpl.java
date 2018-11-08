package cc.messcat.dao.collection;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import cc.messcat.bases.BaseDaoImpl;
import cc.messcat.entity.Collect;
import cc.messcat.entity.EnterpriseNews;
import cc.messcat.entity.IndexCarousel;
import cc.messcat.vo.IndexCarouselVo;
public class EpNewsDaoImpl extends BaseDaoImpl implements EpNewsDao {

	public EpNewsDaoImpl() {
	}

	/**
	 * 查看一条新闻--根据ID
	 */
	@Override
	public EnterpriseNews get(Long id) {
		return (EnterpriseNews) getHibernateTemplate().get(EnterpriseNews.class, id);
	}

	@Override
	public List findNewsByWhere(String where) {
		List ecList = getHibernateTemplate().find(
			(new StringBuffer("from EnterpriseNews ")).append(where).append(" order by editeTime DESC ").toString());
		return ecList;
	}

	@Override
	public Collect getByTypeAndRelatedId(Integer type, Long id,Long memberId) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Collect.class);
//		criteria.createAlias("regionId", "regionId");
		criteria.add(Restrictions.eq("memberId", memberId));
		criteria.add(Restrictions.eq("relatedId", id));
		criteria.add(Restrictions.eq("type", type));
		List list = criteria.list();
		if(null!=list&&list.size()>0){
			return (Collect) list.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<IndexCarousel> getCarousel() {
		String hql="FROM IndexCarousel WHERE status=1 order by id desc";
		List<IndexCarousel> carousels = getSession().createQuery(hql).list();
		return carousels;
	}

}