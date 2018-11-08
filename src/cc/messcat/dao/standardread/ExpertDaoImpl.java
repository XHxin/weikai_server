package cc.messcat.dao.standardread;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import cc.messcat.bases.BaseDaoImpl;
import cc.messcat.entity.Attention;
import cc.messcat.entity.ExpertClassify;
import cc.messcat.entity.HotReply;
import cc.messcat.entity.Liked;
import cc.messcat.entity.Member;
import cc.messcat.entity.HotReplyFees;
import cc.messcat.entity.Share;
import cc.messcat.entity.StandardReading;
import cc.messcat.entity.WebSite;
import cc.messcat.vo.HotExpertListVo;
import cc.modules.util.CollectionUtil;
import cc.modules.util.DateHelper;

public class ExpertDaoImpl extends BaseDaoImpl implements ExpertDao {

	/**
	 * 首页默认的四位推荐专家信息
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<HotExpertListVo> getHotExpertList(String recommendflag) {
		List<HotExpertListVo> resultList = new ArrayList<HotExpertListVo>();
		List<Member> list = new ArrayList<Member>();
		if (recommendflag.equals("standardRead")) {
			list = this.getStandardReadMember(); // 获取标准解读模块 四位专家列表
		} else if (recommendflag.equals("payConsult")) {
			list = this.getPayConsultMember(); // 获取标准解读模块 四位专家列表
		}
		Session session = null;
		for (Member member : list) {
			Long memberId = member.getId();
			StringBuffer sql = new StringBuffer();
			sql.append(
					"SELECT id AS expertId,realname AS name,photo AS photo,intro AS intro, field AS field, profession AS profession, work_years AS workYear,")
					.append("school AS school, major AS major, company AS company,position AS position ,")
					.append(" (SELECT COUNT(id) FROM  `standard_reading` WHERE ((TYPE=1 AND fatherID=0) OR (TYPE=0 AND fatherID=0)) AND classify='1' AND status='1' AND author="
							+ memberId)
					.append(") AS readingSum, (SELECT COUNT(be_memberID) FROM attention WHERE be_memberID=" + memberId);
			/*
			 * 若 flag为standardRead,则
			 * 找is_showIndex=1标准解读模块的专家;flag为payConsult,则找is_showIndex=
			 * 2付费咨询模块的专家
			 */
			if (recommendflag.equals("standardRead")) {
				sql.append(
						") AS attentionSum   FROM member where is_showIndex in (100,101,110,111) and id=" + memberId);
			} else if (recommendflag.equals("payConsult")) {
				sql.append(
						") AS attentionSum   FROM member where is_showIndex in (010,011,110,111) and id=" + memberId);
			}
			session = this.getSession();// getHibernateTemplate().getSessionFactory().openSession();
			SQLQuery query = session.createSQLQuery(sql.toString());

			query.addScalar("expertId", Hibernate.STRING);
			query.addScalar("name", Hibernate.STRING);
			query.addScalar("photo", Hibernate.STRING);
			query.addScalar("intro", Hibernate.STRING);
			query.addScalar("field", Hibernate.STRING);
			query.addScalar("profession", Hibernate.STRING);
			query.addScalar("workYear", Hibernate.STRING);
			query.addScalar("school", Hibernate.STRING);
			query.addScalar("major", Hibernate.STRING);
			query.addScalar("company", Hibernate.STRING);
			query.addScalar("position", Hibernate.STRING);
			query.addScalar("readingSum", Hibernate.INTEGER);
			query.addScalar("attentionSum", Hibernate.INTEGER);
			query.setResultTransformer(Transformers.aliasToBean(HotExpertListVo.class));

			List<HotExpertListVo> querylist = query.list();
			if (CollectionUtil.isListNotEmpty(querylist)) {
				HotExpertListVo vo = querylist.get(0);
				resultList.add(vo);
			}

			// session.close();
		}
		return resultList;
	}

	/**
	 * 热门专家推荐专栏上 点击"查看更多" 之后的专家列表
	 */
	@Override
	public List<HotExpertListVo> getHotExpertList2() {
		List<Member> list = this.getMemberList(); // 获取专家列表
		List<HotExpertListVo> resultList = getExpertList(list);
		return resultList;
	}

	@Override
	public List<HotExpertListVo> getHotExpertDetail(Long expertId) {
		List<Member> list = getSingleMemeber(expertId); // 获取单个专家,为了能够调用公共的方法,
														// 其它地方参照列表格式
		List<HotExpertListVo> resultList = getExpertList(list);
		return resultList;
	}

	/**
	 * 获取单个专家
	 */
	@SuppressWarnings("unchecked")
	private List<Member> getSingleMemeber(Long expertId) {
		Criteria criteria = getSession().createCriteria(Member.class);
		criteria.add(Restrictions.eq("id", expertId));
		List<Member> list = criteria.list();
		return list;
	}

	/**
	 * 获取全部的专家列表, 排除id=9375这个运营维护和10302这个android测试的 专家号
	 */
	@SuppressWarnings("unchecked")
	public List<Member> getMemberList() {
		// 没有头像的专家不显示
		String hql = "FROM Member WHERE status=? AND role=? AND expertCheckStatus=? AND id!=? AND id!=?  AND photo IS NOT NULL AND photo!=''";
		Query query = getSession().createQuery(hql);
		query.setParameter(0, "1");
		query.setParameter(1, "2");
		query.setParameter(2, "1"); // 审核通过
		query.setParameter(3, 9375L); // 运营专家号
		query.setParameter(4, 10302L); // android测试专家号
		List<Member> list = query.list();
		return list;
	}

	/**
	 * 获取标准解读模块 的四位专家信息
	 */
	@SuppressWarnings("unchecked")
	private List<Member> getStandardReadMember() {
		String hql = "from Member where status=1 and role=2 and expertCheckStatus=1 and isShowIndex in (100,101,110,111) order by registTime desc";
		Query query = getSession().createQuery(hql);
		List<Member> list = query.list();
		return list;
	}

	/**
	 * 获取付费咨询模块 的四位专家信息
	 */
	@SuppressWarnings("unchecked")
	private List<Member> getPayConsultMember() {
		String hql = "from Member where status=1 and role=2 and expertCheckStatus=1 and isShowIndex in (010,011,110,111) order by registTime desc";
		Query query = getSession().createQuery(hql);
		List<Member> list = query.list();
		return list;
	}

	/**
	 * 生成一条关注信息
	 */
	@Override
	public boolean addAttention(Attention attention) {
		getHibernateTemplate().save(attention);
		return true;
	}

	/**
	 * 拿到一条关注信息
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Attention getAttention(Long memberId, Long expertId) {
		Criteria criteria = getSession().createCriteria(Attention.class).add(Restrictions.eq("memberId", memberId))
				.add(Restrictions.eq("beMemberId", expertId));
		List<Attention> list = criteria.list();
		if (list != null && list.size() != 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 删除一条关注信息
	 */
	@Override
	public void deleteAttention(Attention attention) {
		getHibernateTemplate().delete(attention);
	}

	/**
	 * 我的关注 列表
	 */
	@Override
	public List<HotExpertListVo> getMyAttention(Long memberId) {
		List<Member> memberList = getBeMemberList(memberId);
		if (memberList != null && memberList.size() != 0) {
			List<HotExpertListVo> list = getExpertList(memberList);
			return list;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private List<Member> getBeMemberList(Long memberId) {
		String hql = "FROM Member m WHERE m.id in (SELECT beMemberId FROM Attention a WHERE a.memberId= " + memberId
				+ ")";
		Query query = getSession().createQuery(hql);
		List<Member> list = query.list();
		return list;
	}

	/**
	 * 公共方法 －－－专家列表
	 */
	@SuppressWarnings("unchecked")
	private List<HotExpertListVo> getExpertList(List<Member> list) {
		List<HotExpertListVo> resultList = new ArrayList<HotExpertListVo>();
		Session session = null;
		for (Member member : list) {
			StringBuffer sql = new StringBuffer();
			Long beMemberId = member.getId();
			sql.append(
					"SELECT M.id AS expertId,realname AS name,photo AS photo,intro AS intro, field AS field, profession AS profession, work_years AS workYear,");
			sql.append("school AS school, major AS major, company AS company,position AS position , R.money, R.private_money AS privateMoney, ");
			sql.append(
					" (SELECT COUNT(id) FROM  `standard_reading` WHERE ((TYPE=1 AND fatherID=0) OR (TYPE=0 AND fatherID=0))  AND classify='1'  AND status='1'  AND author="
							+ beMemberId);
			sql.append(") AS readingSum, (SELECT COUNT(be_memberID) FROM attention WHERE be_memberID=" + beMemberId
					+ ") AS attentionSum, (SELECT COUNT(expertID) FROM hot_reply WHERE expertID="+beMemberId+") AS answerSum ");
			sql.append(" FROM member AS M INNER JOIN hot_reply_fees AS R ON M.id=R.memberID  where  M.role='2' and M.id=" + beMemberId) ;

			session = this.getSession();
			SQLQuery query = session.createSQLQuery(sql.toString());

			query.addScalar("expertId", Hibernate.STRING);
			query.addScalar("name", Hibernate.STRING);
			query.addScalar("photo", Hibernate.STRING);
			query.addScalar("intro", Hibernate.STRING);
			query.addScalar("field", Hibernate.STRING);
			query.addScalar("profession", Hibernate.STRING);
			query.addScalar("workYear", Hibernate.STRING);
			query.addScalar("school", Hibernate.STRING);
			query.addScalar("major", Hibernate.STRING);
			query.addScalar("company", Hibernate.STRING);
			query.addScalar("position", Hibernate.STRING);
			query.addScalar("readingSum", Hibernate.INTEGER);
			query.addScalar("attentionSum", Hibernate.INTEGER);
			query.addScalar("answerSum",Hibernate.INTEGER);
			query.addScalar("money",Hibernate.INTEGER);
			query.addScalar("privateMoney",Hibernate.INTEGER);
			query.setResultTransformer(Transformers.aliasToBean(HotExpertListVo.class));

			List<HotExpertListVo> querylist = query.list();
			if (CollectionUtil.isListNotEmpty(querylist)) {
				HotExpertListVo vo = querylist.get(0);
				resultList.add(vo);
			}
		}
		return resultList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String isAttention(String expertId, Long memberId) {
		Criteria criteria = getSession().createCriteria(Attention.class);
		criteria.add(Restrictions.eq("memberId", memberId));
		criteria.add(Restrictions.eq("beMemberId", Long.valueOf(expertId)));
		List<Attention> list = criteria.list();
		if (!list.isEmpty() && list.size() != 0) {
			return "1";
		}
		return "0";
	}

	@SuppressWarnings("unchecked")
	@Override
	public int getLikeSum(String expertId) {
		Criteria criteria = getSession().createCriteria(Liked.class);
		criteria.add(Restrictions.eq("beMemberId", Long.valueOf(expertId)));
		List<Liked> list = criteria.list();
		int likeSum = list.size();
		return likeSum;
	}

	/*
	 * 获取点赞时间
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String getLikeTime(String expertId, Long memberId) {
		String hql = "FROM Liked WHERE addTime=(SELECT MAX(addTime) FROM Liked WHERE memberId=? AND beMemberId=?)";
		Query query = getSession().createQuery(hql);
		query.setParameter(0, memberId);
		query.setParameter(1, Long.valueOf(expertId));
		List<Liked> list = query.list();
		if (!list.isEmpty() && list.size() != 0) {
			String likeTime = DateHelper.dataToString(list.get(0).getAddTime(), "yyyy-MM-dd");
			return likeTime;
		}
		return null;
	}

	/*
	 * 点赞功能
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean clickLike(Long memberId, Long expertId, Date today) {
		Liked liked = new Liked();
		liked.setMemberId(memberId);
		liked.setBeMemberId(expertId);
		Date now = DateHelper.getCurrentDate_yyyy_MM_dd();
		liked.setAddTime(now);
		liked.setEditTime(now);
		liked.setStatus("1");
		Query query = getSession()
				.createQuery("FROM Liked WHERE memberId=? AND beMemberId=? AND addTime=? AND status=1");
		query.setParameter(0, memberId);
		query.setParameter(1, expertId);
		query.setParameter(2, today);
		List<Liked> list = query.list();
		if (list == null || list.size() == 0) {
			getSession().save(liked);
			return true;
		}
		return false;
	}

	/*
	 * 获取单一专家分类
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ExpertClassify getExpertList(Long classifyId) {
		Criteria criteria = getSession().createCriteria(ExpertClassify.class);
		criteria.add(Restrictions.eq("id", classifyId));
		List<ExpertClassify> list = criteria.list();
		if (!list.isEmpty() && list.size() != 0) {
			return list.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public HotReplyFees getReplyFees(Long expertId) {
		Criteria criteria = getSession().createCriteria(HotReplyFees.class);
		criteria.add(Restrictions.eq("memberId", expertId));
		List<HotReplyFees> list = criteria.list();
		if (!list.isEmpty() && list.size() != 0) {
			return list.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int getAnswerSum(Long expertId) {
		Criteria criteria = getSession().createCriteria(HotReply.class);
		criteria.add(Restrictions.eq("expertId", expertId));
		List<HotReply> list = criteria.list();
		return list.size();
	}

	@SuppressWarnings("unchecked")
	@Override
	public int getAttentionSum(Long expertId) {
		Criteria criteria = getSession().createCriteria(Attention.class);
		criteria.add(Restrictions.eq("beMemberId", expertId));
		criteria.add(Restrictions.eq("status", "1"));
		List<Attention> list = criteria.list();
		return list.size();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Member> getMember() {
		String disMem="(9375,10302)"; //排除掉"Android测试专家号" 和   "世界认证地图"  这两个专家号
		String hql = "FROM Member WHERE status=1 AND role=2 AND expertCheckStatus=1 AND (isShowIndex=0 OR isShowIndex IS NULL) AND photo IS NOT NULL AND photo!='' AND id NOT IN "+disMem;
		Query query = getSession().createQuery(hql);
		List<Member> list = query.list();
		return list;
	}

	// 找到专家实体
	@Override
	public Member getExpert(Long expertId) {
		return (Member) getSession().get(Member.class, expertId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public int getReadSum(Long expertId) {
		String hql = "FROM StandardReading WHERE ((type='1' AND fatherId=0) OR (type='0' AND fatherId=0))"
				+ " AND status='1' AND author.id=" + expertId;
		Query query = getSession().createQuery(hql);
		List<StandardReading> list = query.list();
		return list.size();
	}

	@SuppressWarnings("unchecked")
	@Override
	public int getLikeSum(Long expertId) {
		Criteria criteria = getSession().createCriteria(Liked.class);
		criteria.add(Restrictions.eq("beMemberId", expertId));
		criteria.add(Restrictions.eq("status", "1"));
		List<Liked> list = criteria.list();
		return list.size();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getShareURL() {
		Criteria criteria = getSession().createCriteria(Share.class);
		criteria.add(Restrictions.eq("shareType", "4"));
		criteria.add(Restrictions.eq("status", "1"));
		List<Share> list = criteria.list();
		if (list != null && list.size() != 0) {
			return list.get(0).getShareURL();
		}
		return "无此分享URL";
	}

	/*
	 * 获取统一报价
	 */
	@SuppressWarnings("unchecked")
	@Override
	public WebSite getWebSite() {
		Criteria criteria = getSession().createCriteria(WebSite.class);
		List<WebSite> list = criteria.list();
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int getArticleSum(Long expertId) {
		String hql = "FROM StandardReading WHERE ((type='1' AND fatherId=0) OR (type='0' AND fatherId=0))"
				+ " AND status='1' AND author.id=" + expertId;
		Query query = getSession().createQuery(hql);
		List<StandardReading> list = query.list();
		return list.size();
	}
	
	@Override
	public List<Member> getExpertBySearchKeyWord(String searchKeyWord) {
		String hql = "FROM Member WHERE status=1 AND role=2 AND expertCheckStatus=1 AND realname not like '%世界认证地图%' AND realname not like '%测试%' " + " and ( realname like '%"
				+ searchKeyWord + "%')"+" order by id desc";
		Query query = getSession().createQuery(hql);
		query.setFirstResult(0);
		query.setMaxResults(3);
		@SuppressWarnings("unchecked")
		List<Member> list = query.list();
		return list;
	}

	@Override
	public List<Member> getMoreExpert(String searchKeyWord,int pageNo, int pageSize) {
		String hql = "FROM Member WHERE status=1 AND role=2 AND expertCheckStatus=1 " + " and ( realname like '%"
				+ searchKeyWord + "%')"+" order by id desc";
		Query query = getSession().createQuery(hql);
		query.setFirstResult(pageSize * (pageNo - 1));
		query.setMaxResults(pageSize*pageNo);
		@SuppressWarnings("unchecked")
		List<Member> list = query.list();
		return list;
	}
}
