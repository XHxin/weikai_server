package cc.messcat.dao.payconsult;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import cc.messcat.bases.BaseDaoImpl;
import cc.messcat.entity.ExpenseTotal;
import cc.messcat.entity.Collect;
import cc.messcat.entity.FeedBack;
import cc.messcat.entity.HotProblem;
import cc.messcat.entity.HotReply;
import cc.messcat.entity.Member;
import cc.messcat.entity.HotReplyFees;
import cc.messcat.entity.HotReplyLiked;
import cc.messcat.entity.Share;
import cc.messcat.entity.WebSite;
import cc.modules.commons.Pager;
import cc.modules.util.EmojiFilter;

public class HotReplyDaoImpl extends BaseDaoImpl implements HotReplyDao {

	@SuppressWarnings("unchecked")
	@Override
	public Pager getRecommendList(int pageNo, int pageSize3) {
		Criteria criteria = getSession().createCriteria(HotReply.class);
		criteria.add(Restrictions.eq("isShowIndex", "1"));
		criteria.add(Restrictions.eq("type", "0")); // type=0为公开提问的问题 ,
													// 因为私密提问只会在专家的个人中心处显示
		criteria.add(Restrictions.eq("status", "1"));
		criteria.addOrder(Order.desc("addTime")); // 按照添加时间降序排列
		int rowCount = ((Integer) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		criteria.setProjection(null); // 不能少了这一句
		criteria.setFirstResult((pageNo - 1) * pageSize3);
		criteria.setMaxResults(pageSize3);
		List<HotReply> resultList = criteria.list();
		return new Pager(pageSize3, pageNo, rowCount, resultList);
	}

	@SuppressWarnings("unchecked")
	@Override
	public HotReplyFees getReplyFees(Long memberId) {
		Criteria criteria = getSession().createCriteria(HotReplyFees.class);
		criteria.add(Restrictions.eq("memberId", memberId));
		List<HotReplyFees> list = criteria.list();
		if (!list.isEmpty() && list.size() != 0) {
			return list.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Pager getReplyList(int pageNo, int pageSize, Long expertId) {
		Criteria criteria = getSession().createCriteria(HotReply.class);
		criteria.add(Restrictions.eq("status", "1"));
		criteria.add(Restrictions.eq("expertId", expertId));
		criteria.add(Restrictions.eq("type", "0")); // type=0为公开提问的问题 ,
													// 因为私密提问只会在专家的个人中心处显示
		criteria.addOrder(Order.desc("addTime")); // 按照添加时间降序排列
		int rowCount = (int) criteria.setProjection(Projections.rowCount()).uniqueResult();
		criteria.setProjection(null);
		criteria.setFirstResult((pageNo - 1) * pageSize);
		criteria.setMaxResults(pageSize);
		List<HotReply> list = criteria.list();
		return new Pager(pageSize, pageNo, rowCount, list);
	}

	@Override
	public HotProblem getProblem(Long problemId) {
		return (HotProblem) getSession().get(HotProblem.class, problemId);
	}

	// 获取会员
	@Override
	public Member getMember(Long memberId) {
		return (Member) getSession().get(Member.class, memberId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public int getReplyLikedSum(Long expertId, Long replyId) {
		String hql = "FROM HotReplyLiked WHERE beMemberId=? and replyId=? and status=1";
		Query query = getSession().createQuery(hql);
		query.setParameter(0, expertId);
		query.setParameter(1, replyId);
		List<HotReplyLiked> list = query.list();
		return list.size();
	}

	@Override
	public void commitProblem(HotProblem entity) {
		getSession().saveOrUpdate(entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Pager getAllReply(int pageNo, int pageSize) {
		Criteria criteria = getSession().createCriteria(HotReply.class);
		criteria.add(Restrictions.eq("status", "1"));
		criteria.add(Restrictions.eq("type", "0")); // type=0为公开提问的问题 ,
													// 因为私密提问只会在专家的个人中心处显示
		criteria.addOrder(Order.desc("addTime")); // 按照添加时间降序排列
		int rowCount = (int) criteria.setProjection(Projections.rowCount()).uniqueResult();
		criteria.setProjection(null);
		criteria.setFirstResult((pageNo - 1) * pageSize);
		criteria.setMaxResults(pageSize);
		List<HotReply> list = criteria.list();
		return new Pager(pageSize, pageNo, rowCount, list);
	}

	@Override
	public HotReply getHotReply(Long id) {
		return (HotReply) getSession().get(HotReply.class, id);
	}

	@Override
	public boolean commitFeedBack(FeedBack feedBack) {
		getSession().save(feedBack);
		return true;
	}

	/**
	 * 以下两个方法分别为个人中心处的 未答问答和 历史问答, 所以不用区分问题是否是公开的
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Pager getNotReply(int pageNo, int pageSize, Long memberId) {
		Criteria criteria = getSession().createCriteria(HotProblem.class);
		criteria.add(Restrictions.eq("expertId", memberId));
		criteria.add(Restrictions.eq("status", "1"));
		criteria.addOrder(Order.desc("addTime")); // 按照添加时间降序排列
		int rowCount = (int) criteria.setProjection(Projections.rowCount()).uniqueResult();
		criteria.setProjection(null);
		criteria.setFirstResult((pageNo - 1) * pageSize);
		criteria.setMaxResults(pageSize);
		List<HotProblem> list = criteria.list();
		return new Pager(pageSize, pageNo, rowCount, list);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Pager getHistoryReply(int pageNo, int pageSize, Long memberId) {
		Criteria criteria = getSession().createCriteria(HotReply.class);
		criteria.add(Restrictions.eq("expertId", memberId));
		criteria.add(Restrictions.eq("status", "1"));
		criteria.addOrder(Order.desc("addTime")); // 按照添加时间降序排列
		int rowCount = (int) criteria.setProjection(Projections.rowCount()).uniqueResult();
		criteria.setProjection(null);
		criteria.setFirstResult((pageNo - 1) * pageSize);
		criteria.setMaxResults(pageSize);
		List<HotReply> list = criteria.list();
		return new Pager(pageSize, pageNo, rowCount, list);
	}

	@Override
	public void commitReply(HotReply hotReply, Long expertId,HotProblem problem) {
		HotReply entity = new HotReply();
		entity.setAddTime(new Date());
		entity.setEditTime(new Date());
		entity.setType(problem.getType());
		entity.setStatus("1");
		entity.setIsShowIndex("0"); // 默认不在首页显示
		entity.setExpertId(expertId);
		entity.setProblemId(hotReply.getProblemId());
		entity.setProblemName(EmojiFilter.filterEmoji(problem.getName())); // 过滤掉表情符
		entity.setVoice(hotReply.getVoice());
		entity.setVoiceDuration(hotReply.getVoiceDuration());
		entity.setContent(hotReply.getContent());
		entity.setPicture(hotReply.getPicture());
		entity.setDispose(0);
		getSession().save(entity);
	}

	// 获取最大问题id
	@Override
	public Long getMaxHotProblemId() {
		Criteria criteria = getSession().createCriteria(HotProblem.class);
		criteria.setProjection(Projections.max("id"));
		Long id = (Long) criteria.uniqueResult();
		return id;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int getViewSum(Long expertId, Long problemId) {
		String hql = "FROM ExpenseTotal  WHERE expertId=? AND relatedId=? AND payStatus=? AND type=? ";
		Query query = getSession().createQuery(hql);
		query.setParameter(0, expertId);
		query.setParameter(1, problemId);
		query.setParameter(2, "1");
		query.setParameter(3, "4"); // type=4为付费咨询
		List<ExpenseTotal> list = query.list();
		return list.size();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FeedBack> getFeedBackList(Long memberId, Long expertId) {
		Criteria criteria = getSession().createCriteria(FeedBack.class);
		criteria.add(Restrictions.eq("memberId", memberId));
		criteria.add(Restrictions.eq("expertId", expertId));
		criteria.add(Restrictions.eq("status", "1"));
		criteria.addOrder(Order.desc("addTime")); // 按照添加时间降序排列
		List<FeedBack> list = criteria.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getCollectStatus(Long memberId, Long replyId) {
		Criteria criteria = getSession().createCriteria(Collect.class).add(Restrictions.eq("memberId", memberId))
				.add(Restrictions.eq("relatedId", replyId)).add(Restrictions.eq("status", "1"))
				.add(Restrictions.eq("type", 4)); // type为4代表着付费咨询
		List<Collect> list = criteria.list();
		if (list != null && list.size() > 0) {
			return "1"; // 1代表收藏,0代表未收藏
		}
		return "0";
	}

	@SuppressWarnings("unchecked")
	@Override
	public int getCollectSum(Long replyId) {
		Criteria criteria = getSession().createCriteria(Collect.class).add(Restrictions.eq("relatedId", replyId))
				.add(Restrictions.eq("type", 4)).add(Restrictions.eq("status", "1"));
		List<Collect> list = criteria.list();
		return list.size();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<HotReply> getHotReplyList(Long replyId) {
		Criteria criteria = getSession().createCriteria(HotReply.class);
		criteria.add(Restrictions.eq("id", replyId));
		criteria.add(Restrictions.eq("status", "1"));
		criteria.addOrder(Order.desc("addTime")); // 按照添加时间降序排列
		List<HotReply> list = criteria.list();
		return list;
	}

	@Override
	public void addReplyLiked(HotReplyLiked like) {
		getSession().save(like);
	}

	@SuppressWarnings("unchecked")
	@Override
	public HotReplyLiked getReplyLiked(Long replyId, Long expertId, Long memberId) {
		Criteria criteria = getSession().createCriteria(HotReplyLiked.class).add(Restrictions.eq("memberId", memberId))
				.add(Restrictions.eq("beMemberId", expertId)).add(Restrictions.eq("replyId", replyId))
				.add(Restrictions.eq("status", "1"));
		List<HotReplyLiked> list = criteria.list();
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public void delete(HotReplyLiked hotReplyLiked) {
		getSession().delete(hotReplyLiked);
	}

	/*
	 * 专家操作提问收费价格
	 */
	@Override
	public void updateReplyFees(HotReplyFees hotReplyFees) {
		getSession().update(hotReplyFees);
	}

	@Override
	public void saveReplyFees(HotReplyFees entity) {
		getSession().save(entity);
	}

	// 更改问题状态
	@Override
	public void update(HotProblem hotProblem) {
		getHibernateTemplate().update(hotProblem);
	}

	@Override
	public WebSite getWebSite() {
		Criteria criteria = getSession().createCriteria(WebSite.class);
		return (WebSite) criteria.list().get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public HotReply getReply(Long expertId, Long problemId) {
		Criteria criteria = getSession().createCriteria(HotReply.class);
		criteria.add(Restrictions.eq("problemId", problemId));
		criteria.add(Restrictions.eq("expertId", expertId));
		criteria.add(Restrictions.eq("status", "1"));
		List<HotReply> list = criteria.list();
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Share getShare() {
		Criteria criteria = getSession().createCriteria(Share.class);
		criteria.add(Restrictions.eq("status", "1"));
		criteria.add(Restrictions.eq("shareType", "5"));
		List<Share> list = criteria.list();
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<HotProblem> getProblemList() {
		Criteria criteria = getSession().createCriteria(HotProblem.class);
		criteria.add(Restrictions.eq("status", "1")); // 只查询已付款的记录，退款的和未付款的不查
		criteria.add(Restrictions.eq("dispose",0));
		List<HotProblem> list = criteria.list();
		if (list != null && list.size() != 0) {
			return list;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public HotReply getHotReply(Long problemId, Long expertId) {
		Criteria criteria = getSession().createCriteria(HotReply.class).add(Restrictions.eq("expertId", expertId))
				.add(Restrictions.eq("problemId", problemId)).add(Restrictions.eq("status", "1"));
		List<HotReply> list = criteria.list();
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public void updateProblem(HotProblem problem) {
		getSession().update(problem);
	}

	// 根据问题id查找回复
	@Override
	public HotReply getHotReplyByProblem(Long problemId) {
		Criteria criteria = getSession().createCriteria(HotReply.class).add(Restrictions.eq("problemId", problemId))
				.add(Restrictions.eq("status", "1"));
		List<HotReply> list = criteria.list();
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public void updateHotReply(HotReply hotReply) {
		getSession().update(hotReply);
	}

	@Override
	public List<HotReply> getReplyBySearchKeyWord(String searchKeyWord) {
		String hql = "FROM HotReply  WHERE type=0 AND status=1 AND problemName like '%"+ searchKeyWord +"%'" + " order by editTime desc";
		Query query = getSession().createQuery(hql);
		query.setFirstResult(0);
		query.setMaxResults(3);
		@SuppressWarnings("unchecked")
		List<HotReply> list = query.list();
		return list;
	}

	@Override
	public List<HotReply> getMoreHotReply(String searchKeyWord,int pageNo, int pageSize) {
		String hql = "FROM HotReply  WHERE type=0 AND status=1 AND problemName like '%"+ searchKeyWord +"%'" + " order by editTime desc";
		Query query = getSession().createQuery(hql);
		query.setFirstResult(pageSize*(pageNo-1));
		query.setMaxResults(pageSize*pageNo);
		@SuppressWarnings("unchecked")
		List<HotReply> list = query.list();
		return list;
	}

	//专家是否回复
	@SuppressWarnings("unchecked")
	@Override
	public HotReply isResubmit(Long problemId, Long expertId) {
		Criteria criteria=getSession().createCriteria(HotReply.class)
				.add(Restrictions.eq("expertId", expertId))
				.add(Restrictions.eq("problemId", problemId))
				.add(Restrictions.eq("status", "1"));
		List<HotReply> list=criteria.list();
		if(list!=null && list.size()>0) {
			return list.get(0);
		}
		return null;
	}

}
