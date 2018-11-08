package cc.messcat.dao.member;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import cc.messcat.bases.BaseDaoImpl;
import cc.messcat.entity.ActivityExchangeRecord;
import cc.messcat.entity.LiveVideoDistributor;
import cc.messcat.entity.RelevanceWechat;

public class ActivityExchangeRecordDaoImpl extends BaseDaoImpl implements ActivityExchangeRecordDao{

	@Override
	public void addExchangeRecord(ActivityExchangeRecord activityExchangeRecord) {
		getHibernateTemplate().save(activityExchangeRecord);
	}

	@Override
	public List<ActivityExchangeRecord> getExchangeCount(String changeType,Long memberId) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(ActivityExchangeRecord.class);
		criteria.add(Restrictions.eq("changeType", changeType));
		criteria.add(Restrictions.eq("memberId", memberId));
		List<ActivityExchangeRecord> list = criteria.list();
		if(!list.isEmpty() && list.size()>0){
			return list;
		}
		return null;
	}

	@Override
	public List<RelevanceWechat> exchangeCount(String openId, String mobile, int videoId) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(RelevanceWechat.class);
		criteria.add(Restrictions.or(Restrictions.eq("openId", openId), Restrictions.eq("mobile", mobile)));
		criteria.add(Restrictions.eq("videoId", videoId));
		List<RelevanceWechat> list = criteria.list();
		return list;
	}

	@Override
	public void exchangeRecord(RelevanceWechat relevance) {
		getHibernateTemplate().save(relevance);
	}

	@Override
	public ActivityExchangeRecord findRecordByOpenId(String openId,Long videoId) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(ActivityExchangeRecord.class);
		criteria.add(Restrictions.eq("openId", openId))
		.add(Restrictions.eq("changeType", "3"))
		.add(Restrictions.eq("relatedId", videoId));
		List<ActivityExchangeRecord> records = criteria.list();
		if(records.size() > 0) {
			return records.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public LiveVideoDistributor getLiveVideoDistributor(Long videoId, Long memberId) {
		String hql="FROM LiveVideoDistributor WHERE videoId=? AND memberId=? ";
		Query query=getSession().createQuery(hql);
		query.setParameter(0, videoId);
		query.setParameter(1, memberId);
		List<LiveVideoDistributor> list=query.list();
		if(list!=null && list.size()>0) {
			return list.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LiveVideoDistributor> getDistributorByMemberId(Long memberId) {
		String hql="FROM LiveVideoDistributor WHERE memberId=? ";
		Query query=getSession().createQuery(hql);
		query.setParameter(0, memberId);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public BigDecimal getTotalIntegral(Long memberId) {
		String sql="SELECT COALESCE(SUM(integral),0) FROM live_video_distributor WHERE mid=? ";
		SQLQuery query=getSession().createSQLQuery(sql);
		query.setParameter(0, memberId);
		List<BigDecimal> list=query.list();
		return list.get(0);
	}
}
