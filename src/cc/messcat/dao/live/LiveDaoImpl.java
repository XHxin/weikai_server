package cc.messcat.dao.live;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import cc.messcat.bases.BaseDaoImpl;
import cc.messcat.entity.Attention;
import cc.messcat.entity.ExpenseTotal;
import cc.messcat.entity.ChatRoomAuth;
import cc.messcat.entity.Coupn;
import cc.messcat.entity.FreeVideo;
import cc.messcat.entity.LiveVideo;
import cc.messcat.entity.LiveVideoDiscount;
import cc.messcat.entity.LiveVideoSubject;
import cc.messcat.entity.Member;
import cc.messcat.entity.RecordEstimate;
import cc.messcat.entity.RedeemCodeOwner;
import cc.messcat.entity.RelevanceWechat;
import cc.messcat.entity.InviteStatis;
import cc.messcat.entity.SilenceList;
import cc.messcat.entity.CoupnUser;
import cc.messcat.entity.VideoBullet;
import cc.messcat.entity.VideoWall;
import cc.messcat.entity.ViewerBetweenLive;
import cc.messcat.vo.ExpertCourseVo;
import cc.messcat.vo.PopularListVo;
import cc.modules.commons.Pager;

class LiveDaoImpl extends BaseDaoImpl implements LiveDao {

	@SuppressWarnings("unchecked")
	@Override
	public Member getExpert(String expertId) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Member.class);
		criteria.add(Restrictions.eq("id", Long.valueOf(expertId)));
		criteria.add(Restrictions.eq("role", "2"));
		criteria.add(Restrictions.eq("status", "1"));
		List<Member> list = criteria.list();
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public void update(LiveVideo liveVideo) {
		getHibernateTemplate().update(liveVideo);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LiveVideo> videoList(int pageNo, int pageSize) {
		Session session = getSession();
		String hql = "from LiveVideo where status =1" + " order by applyDate desc";
		Query query = session.createQuery(hql);
		query.setFirstResult(pageSize * (pageNo - 1));
		query.setMaxResults(pageSize);
		List<LiveVideo> videos = query.list();
		return videos;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Pager getLiveVideos(String[] bespeakStatus, int pageNo, int pageSize, Long memberId) {
		Criteria criteria = getSession().createCriteria(LiveVideo.class);
		criteria.add(Restrictions.in("bespeakStatus", bespeakStatus));// 0表示直播, 1-录播
		criteria.add(Restrictions.eq("status", "1"));// 1表示可用
		criteria.add(Restrictions.eq("expertId", memberId));
		criteria.addOrder(Order.desc("applyDate"));
		int rowCount = (int) criteria.setProjection(Projections.rowCount()).uniqueResult();
		criteria.setProjection(null);
		criteria.setFirstResult(pageSize * (pageNo - 1));
		criteria.setMaxResults(pageSize);
		List<LiveVideo> videos = criteria.list();
		return new Pager(pageSize, pageNo, rowCount, videos);
	}

	@Override
	public void addWatchRecord(ViewerBetweenLive vbLive) {
		getHibernateTemplate().save(vbLive);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Long getWatchMark(Long experId) {
		Session session = getSession();
		String hql = "from LiveVideo where expertId=? and videoType = 0 order by applyDate asc limit 1";
		Query query = session.createQuery(hql);
		query.setLong(0, experId);
		List<LiveVideo> mark = query.list();
		if (!mark.isEmpty() && mark.size() > 0) {
			return mark.get(0).getId();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int countViewers(Long videoId) {
		// 查询视频id为X，然后根据memberId去重，再统计行数
		/*
		 * Criteria criteria = getSession().createCriteria(ViewerBetweenLive.class);
		 * criteria.add(Restrictions.eq("id", videoId)); int rowCount = ((Integer)
		 * criteria.setProjection(Projections.rowCount()).uniqueResult()). intValue();
		 * criteria.setProjection(null);
		 */
		if (videoId == null) {
			System.out.println("video Id is null");
			return 0;
		}
		String hql = "from ViewerBetweenLive where videoId=? group by memberId";
		Query query = getSession().createQuery(hql);
		query.setLong(0, videoId);
		List<ViewerBetweenLive> records = query.list();
		if (!records.isEmpty() && records.size() > 0) {
			return records.size();
		}
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public LiveVideo getViewers(Long videoId) {
		Criteria criteria = getSession().createCriteria(LiveVideo.class);
		criteria.add(Restrictions.eq("id", videoId));
		List<LiveVideo> videos = criteria.list();
		if (!videos.isEmpty() && videos.size() > 0) {
			return videos.get(0);
		}
		return null;
	}

	// 申请预约直播插入一条数据或者修改预约直播详情
	@Override
	public Long insertOrUpdate(LiveVideo liveVideo) {
		if (liveVideo.getId() != null) {
			this.getHibernateTemplate().saveOrUpdate(liveVideo);
			return liveVideo.getId();
		} else {
			Serializable s = this.getHibernateTemplate().save(liveVideo);
			return Long.valueOf(s.toString());
		}
	}

	// 获取一条预约直播的信息
	@Override
	public LiveVideo getLiveVideoById(Long id) {
		return (LiveVideo)getSession().get(LiveVideo.class,id);
	}

	// 取消预约直播
	@Override
	public void updateBespeakStatus(LiveVideo liveVideo) {
		this.getHibernateTemplate().update(liveVideo);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Pager getTodayLive(int pageNo, int pageSize, String chieflyShow) {
		//今日直播不仅仅局限于是今日的直播,有可能是刚刚结束直播的录播视频,   这个模块完全由courseShow决定
		//在首页显示(十位数上代表今日直播,个位数上代表精品课程),(0:不显示,1:在首页,2:在查看更多)
		StringBuffer sbf = new StringBuffer();
		String hql = "from LiveVideo where  status=1 ";
		// 首页今日直播查看更多(不做推荐)
		if (chieflyShow.equals("0")) {
//			sbf.append(hql).append(" and (courseShow=101 or courseShow=111)");   排除外面的推荐直播
			sbf.append(hql).append(" and videoType=0 and bespeakStatus=1");  //不排除外面的推荐直播
			// 视频课程模块里面的"今日直播"全部做推荐
		} else if (chieflyShow.equals("1")) {
			sbf.append(hql).append(" and videoType=0 and bespeakStatus=1 and chieflyShow like '1__'");
			// 首页今日直播模块显示(推荐)
		} else if (chieflyShow.equals("2")) {
			
			sbf.append(hql).append(" and (courseShow=110 or courseShow=111)");
		}
		Query query = getSession().createQuery(sbf.toString());
		List<LiveVideo> list = query.list();
		query.setFirstResult(pageSize * (pageNo - 1));
		query.setMaxResults(pageSize);
		List<LiveVideo> resultList = query.list();
		return new Pager(pageSize, pageNo, list.size(), resultList);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Attention isAttention(Long memberId, String expertId) {
		Criteria criteria = getSession().createCriteria(Attention.class);
		criteria.add(Restrictions.eq("memberId", memberId));
		criteria.add(Restrictions.eq("beMemberId", Long.valueOf(expertId)));
		criteria.add(Restrictions.eq("status", "1"));
		List<Attention> list = criteria.list();
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Pager getHotVideosChieflyShow(int pageNo, int pageSize) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(LiveVideo.class);
		criteria.add(Restrictions.eq("status", "1"));
		criteria.add(Restrictions.or(Restrictions.eq("chieflyShow", "10"), Restrictions.eq("chieflyShow", "11")));
		criteria.add(Restrictions.eq("isHot", "1"));
		criteria.add(Restrictions.in("fatherId", new Long[] { -1L, 0L }));
		criteria.addOrder(Order.desc("applyDate")); // 把最新直播的视频放在最前面
		int rowCount = (int) criteria.setProjection(Projections.rowCount()).uniqueResult();
		criteria.setProjection(null);
		criteria.setFirstResult((pageNo - 1) * pageSize);
		criteria.setMaxResults(pageSize);
		List<LiveVideo> videos = criteria.list();
		return new Pager(pageSize, pageNo, rowCount, videos);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Pager getMoreHotVideos(int pageNo, int pageSize, String isSeries) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(LiveVideo.class);
		criteria.add(Restrictions.eq("status", "1"));
		criteria.add(Restrictions.eq("isHot", "1"));
		if (isSeries.equals("0")) {// 单一
			criteria.add(Restrictions.eq("fatherId", -1L));
		} else if (isSeries.equals("1")) {// 系列
			criteria.add(Restrictions.eq("fatherId", 0L));
		}
		criteria.add(Restrictions.or(Restrictions.eq("bespeakStatus", "1"), Restrictions.eq("bespeakStatus", "4")));
		criteria.addOrder(Order.desc("applyDate"));
		int rowCount = (int) criteria.setProjection(Projections.rowCount()).uniqueResult();
		criteria.setProjection(null);
		criteria.setFirstResult(pageSize * (pageNo - 1));
		criteria.setMaxResults(pageSize);
		List<LiveVideo> videos = criteria.list();
		return new Pager(pageSize, pageNo, rowCount, videos);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LiveVideo> seriesVideosChieflyShow(String chieflyShow) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(LiveVideo.class);
		criteria.add(Restrictions.eq("fatherId", 0L));
		criteria.add(Restrictions.eq("status", "1"));
		String[] strs = { "01", "1", "11" };
		criteria.add(Restrictions.in("chieflyShow", strs));
		List<LiveVideo> videos = criteria.list();
		return videos;
	}

	/**
	 * 由系列往单一方向找
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<LiveVideo> getSeriesVideoByFatherID(Long videoId) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(LiveVideo.class);
		criteria.add(Restrictions.eq("fatherId", videoId));
		criteria.add(Restrictions.eq("status", "1"));
		List<LiveVideo> videos = criteria.list();
		return videos;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Pager getMoreSeriesVideos(String chieflyShow, int pageNo, int pageSize) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(LiveVideo.class);
		criteria.add(Restrictions.eq("fatherId", 0L));
		criteria.add(Restrictions.eq("status", "1"));
		criteria.addOrder(Order.desc("applyDate"));
		int rowCount = (int) criteria.setProjection(Projections.rowCount()).uniqueResult();
		criteria.setProjection(null);
		criteria.setFirstResult(pageSize * (pageNo - 1));
		criteria.setMaxResults(pageSize);
		List<LiveVideo> videos = criteria.list();
		return new Pager(pageSize, pageNo, rowCount, videos);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LiveVideo> getLiveVideosById(Long[] relateId) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(LiveVideo.class);
		criteria.add(Restrictions.in("id", relateId));
		criteria.add(Restrictions.eq("status", "1"));
		List<LiveVideo> videos = criteria.list();
		return videos;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LiveVideo> getLiveVideosByFatherId(Long[] chapterId) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(LiveVideo.class);
		criteria.add(Restrictions.eq("status", "1"));
		criteria.add(Restrictions.in("fatherId", chapterId));
		List<LiveVideo> videos = criteria.list();
		return videos;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isAdmin(Long memberId) {
		Criteria criteria = getSession().createCriteria(ChatRoomAuth.class);
		criteria.add(Restrictions.eq("memberId", memberId));
		List<ChatRoomAuth> list = criteria.list();
		if (list != null && list.size() > 0) {
			return true;
		}
		return false;
	}

	@Override
	public void changeSilence(LiveVideo video) {
		getSession().update(video);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LiveVideo> getWholeVideo(Long fatherId) {
		String hql = "from LiveVideo where id=? or fatherId=?";
		Query query = this.getSession().createQuery(hql);
		query.setLong(0, fatherId);
		query.setLong(1, fatherId);
		List<LiveVideo> videos = query.list();
		return videos;
	}

	@SuppressWarnings("unchecked")
	@Override
	public LiveVideo afterLiveVideo(Long experId) {
		Session session = this.getSession();
		String hql = "FROM LiveVideo WHERE expertId=? AND videoType = 0 AND applyDate = (SELECT MIN(applyDate) FROM LiveVideo WHERE expertId=? AND videoType = 0)";
		Query query = session.createQuery(hql);
		query.setLong(0, experId);
		query.setLong(1, experId);
		List<LiveVideo> videos = query.list();
		if (!videos.isEmpty() && videos.size() > 0) {
			return videos.get(0);
		}
		return null;
	}

	/**
	 * 由单一往系列方向找
	 */
	@SuppressWarnings("unchecked")
	@Override
	public LiveVideo searchFatherVideo(Long fatherId) {
		Criteria criteria = getSession().createCriteria(LiveVideo.class);
		criteria.add(Restrictions.eq("id", fatherId));
		criteria.add(Restrictions.eq("status", "1"));
		List<LiveVideo> list = criteria.list();
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<LiveVideo> getLiveVideBySearchKeyWord(String searchKeyWord) {
		String hql = "from LiveVideo where status=1 and (classify=4 OR classify=1) and  bespeakStatus<>0 and ( title like '%"
				+ searchKeyWord + "%'" + " or expertName like '%" + searchKeyWord + "%'" + " or introduct like '%"
				+ searchKeyWord + "%')" + " order by applyDate desc";
		Query query = this.getSession().createQuery(hql);
		query.setFirstResult(0);
		query.setMaxResults(3);
		@SuppressWarnings("unchecked")
		List<LiveVideo> videos = query.list();
		return videos;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Pager getSubjectList(int pageNo, int pageSize, String chiefShow) {
		String hql = "";
		/*
		 * 首页显示设置(0:首页,1:更多,2首页和更多)
		 */
		if (chiefShow.equals("1")) {
			hql = "FROM LiveVideoSubject WHERE chieflyShow in (0,2) AND status=1";
		} else {
			hql = "FROM LiveVideoSubject WHERE chieflyShow in (1,2)  AND status=1";
		}
		Query query = getSession().createQuery(hql);
		List<LiveVideoSubject> list = query.list();
		query.setFirstResult((pageNo - 1) * pageSize);
		query.setMaxResults(pageSize);
		List<LiveVideoSubject> resultList = query.list();
		return new Pager(pageSize, pageNo, list.size(), resultList);

	}

	/**
	 * 获取首页精品课程的列表
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Pager getExcellentCourse(int pageNo, int pageSize, String chiefShow) {
		String hql="";
		/*
		 * chiefShow=1的为首页显示推荐的精品课程,否则为查看更多精品课程
		 */
		//courseShow   精品课程显示(0:不显示,1:在首页,2:在查看更多)
		if(chiefShow.equals("1")) {
			//fatherId<=0的即为连载或单一视频,排除掉连载的子视频
			hql="FROM LiveVideo WHERE status=1 AND isHot=1 AND (courseShow=210 or courseShow=211) AND fatherId<=0 AND bespeakStatus=4 ORDER BY applyDate DESC";
		}else {
			hql="FROM LiveVideo WHERE status=1 AND isHot=1 AND (courseShow=201 or courseShow=211) AND fatherId<=0 AND bespeakStatus=4 ORDER BY applyDate DESC";
		}
		Query query=getSession().createQuery(hql);
		List<LiveVideo> list=query.list();
		query.setFirstResult((pageNo-1)*pageSize);
		query.setMaxResults(pageSize);
		List<LiveVideo> resultList=query.list();
		return new Pager(pageSize,pageNo,list.size(),resultList);
	}

	@Override
	public List<LiveVideoSubject> getAllLiveSubjectBySearchKeyWord(String searchKeyWord) {
		String hql = "from LiveVideoSubject where status=1 and ( introduct like '%"
				+ searchKeyWord + "%'"+ " or subjectName like '%"
				+ searchKeyWord + "%')" + " order by editTime desc";
		Query query = this.getSession().createQuery(hql);
		query.setFirstResult(0);
		query.setMaxResults(3);
		@SuppressWarnings("unchecked")
		List<LiveVideoSubject> videos = query.list();
		return videos;
	}

	@Override
	public List<LiveVideo> getMoreLiveVideo(String searchKeyWord, int pageNo, int pageSize) {
		String hql = "from LiveVideo where status=1 and (classify=4 OR classify=1) and bespeakStatus<>0 and ( title like '%"
				+ searchKeyWord + "%'" + " or expertName like '%" + searchKeyWord + "%'" + " or introduct like '%"
				+ searchKeyWord + "%')" + " order by applyDate desc";
		Query query = this.getSession().createQuery(hql);
		query.setFirstResult(pageSize*(pageNo-1));
		query.setMaxResults(pageSize*pageNo);
		@SuppressWarnings("unchecked")
		List<LiveVideo> videos = query.list();
		return videos;
	}

	@Override
	public List<LiveVideoSubject> getMoreLiveSubject(String searchKeyWord, int pageNo, int pageSize) {
		String hql = "from LiveVideoSubject where status=1 and ( introduct like '%"
				+ searchKeyWord + "%'"+ " or subjectName like '%"
				+ searchKeyWord + "%')" + " order by editTime desc";
		Query query = this.getSession().createQuery(hql);
		query.setFirstResult(pageSize*(pageNo-1));
		query.setMaxResults(pageSize*pageNo);
		@SuppressWarnings("unchecked")
		List<LiveVideoSubject> videos = query.list();
		return videos;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Pager getCourse(Long memberId,int pageNo, int pageSize) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(LiveVideo.class);
		criteria.add(Restrictions.eq("status", "1"));
		criteria.add(Restrictions.eq("expertId", memberId));
		criteria.add(Restrictions.in("videoType", new String[]{"1","2"}));
		criteria.addOrder(Order.desc("applyDate"));
		int rowCount = (int)criteria.setProjection(Projections.rowCount()).uniqueResult();
		criteria.setProjection(null);
		criteria.setFirstResult(pageSize * (pageNo - 1));
		criteria.setMaxResults(pageSize);
		List<ExpertCourseVo> expertCourseVos = criteria.list();
		return new Pager(pageSize, pageNo, rowCount, expertCourseVos);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Pager getSubjectDetail(int pageNo, int pageSize,Integer subjectId) {
		String hql="FROM LiveVideo WHERE fatherId<=0 AND subjectId=? AND status=1 order by applyDate desc";
		Query query=getSession().createQuery(hql);
		query.setParameter(0, subjectId);
		List<LiveVideo> list=query.list();
		query.setFirstResult((pageNo-1)*pageSize);
		query.setMaxResults(pageSize);
		List<LiveVideo> resultList=query.list();
		return new Pager(pageSize,pageNo,list.size(),resultList);
	}

	@Override
	public LiveVideoSubject getSubject(Integer subjectId) {
		return (LiveVideoSubject) getHibernateTemplate().get(LiveVideoSubject.class, subjectId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ExpenseTotal getBuyStatus(Long memberId, Long relatedId) {
		Criteria criteria=getSession().createCriteria(ExpenseTotal.class)
				.add(Restrictions.eq("memberId", memberId))
				.add(Restrictions.eq("relatedId", relatedId))
				.add(Restrictions.eq("type", "8"))
				.add(Restrictions.eq("payStatus", "1"));
		List<ExpenseTotal> list=criteria.list();
		if(list!=null && list.size()>0) {
			return list.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LiveVideo> allSonVideosByFatherId(Long id) {
		String sql = "SELECT * FROM live_video WHERE fatherid IN (SELECT id FROM live_video WHERE fatherId=?)";
		SQLQuery query = getSession().createSQLQuery(sql).addEntity(LiveVideo.class);
		query.setLong(0, id);
		List<LiveVideo> list = query.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PopularListVo> getPopularList(Long videoId) {
		String sql="SELECT  app_from_id as memberId,mp_from_id AS openId,memberName,mobile,photo, COUNT(identity) AS audienceCount FROM (SELECT app_from_id,mp_from_id,realname AS memberName,mobile,photo, "
				+ " CASE WHEN from_type=0 THEN app_from_id  WHEN from_type=1 THEN  mp_from_id  ELSE 2 END  AS identity FROM invite_statis AS A LEFT JOIN member"
				+ " AS B ON A.app_from_id=B.id WHERE related_id=?) AS C GROUP BY identity ORDER BY COUNT(identity) DESC";
		SQLQuery query=getSession().createSQLQuery(sql);
		query.setParameter(0, videoId);
		query.addScalar("memberId",Hibernate.LONG);
		query.addScalar("openId",Hibernate.STRING);
		query.addScalar("memberName", Hibernate.STRING);
		query.addScalar("mobile", Hibernate.STRING);
		query.addScalar("photo", Hibernate.STRING);
		query.addScalar("audienceCount", Hibernate.INTEGER);
		query.setResultTransformer(Transformers.aliasToBean(PopularListVo.class));
		List<PopularListVo> list=query.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public PopularListVo getPopular(Long videoId,Long memberId) {
		String sql="SELECT member.id as memberId,realname as memberName,mobile,photo,COUNT(*) AS audienceCount FROM invite_statis LEFT JOIN live_video ON invite_statis.related_id = live_video.id " + 
				"LEFT JOIN member ON member.id=invite_statis.app_from_id WHERE related_id=? and app_from_id=? AND from_type=0 GROUP BY app_from_id ";
		SQLQuery query=getSession().createSQLQuery(sql);
		query.setParameter(0, videoId);
		query.setParameter(1, memberId);
		query.addScalar("memberId",Hibernate.LONG);
		query.addScalar("memberName", Hibernate.STRING);
		query.addScalar("mobile", Hibernate.STRING);
		query.addScalar("photo", Hibernate.STRING);
		query.addScalar("audienceCount", Hibernate.INTEGER);
		query.setResultTransformer(Transformers.aliasToBean(PopularListVo.class));
		List<PopularListVo> list=query.list();
		if(list!=null && list.size()>0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public Member getMember(Long memberId) {
		return (Member) getSession().get(Member.class, memberId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ViewerBetweenLive> watchRecord(Long videoId, Long memberId) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(ViewerBetweenLive.class);
		criteria.add(Restrictions.eq("videoId", videoId))
		.add(Restrictions.eq("memberId", memberId));
		List<ViewerBetweenLive> list = criteria.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<InviteStatis> getPopularListVoList(Long videoId, Long memberId) {
		String hql="FROM InviteStatis WHERE appFromId=? and relatedId=?";
		Query query=getSession().createQuery(hql);
		query.setParameter(0, memberId);
		query.setParameter(1, videoId);
		List<InviteStatis> list=query.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Long getPaidSum(Long fromId,Long videoId) {
		String hql="SELECT COUNT(id) FROM ExpenseTotal WHERE type=? AND relatedId=? AND memberId IN"
				+ "(SELECT id FROM Member WHERE mobile IN (SELECT mobile FROM  RelevanceWechat WHERE fromId=? ))";
		Query query=getSession().createQuery(hql);
		query.setParameter(0, "8");
		query.setParameter(1, videoId);
		query.setParameter(2, fromId.intValue());
		List<Long> list=query.list();
		if(list!=null && list.size()>0) {
			return list.get(0);
		}
		return 0L;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LiveVideo> getChildVideoList(Long videoId) {
		String hql="FROM LiveVideo WHERE fatherId IN (SELECT id FROM LiveVideo WHERE fatherId = ?)  ORDER BY bespeak_date ASC";
		Query query=getSession().createQuery(hql);
		query.setParameter(0, videoId);
		List<LiveVideo> list=query.list();
		return list;
	}

	/**
	 * 判断用户是否是聊天室的管理员
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ChatRoomAuth isChatAdmin(Long memberId) {
		String hql="from ChatRoomAuth where memberId=? ";
		Query query=getSession().createQuery(hql);
		query.setParameter(0,memberId);
		List<ChatRoomAuth> list=query.list();
		if(list!=null && list.size()>0) {
			return list.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public RelevanceWechat getMemberByFromId(Long fromId, Long videoId) {
		String hql="from RelevanceWechat where fromId=? and videoId=? ";
		Query query=getSession().createQuery(hql);
		query.setParameter(0, fromId);
		query.setParameter(1, videoId);
		List<RelevanceWechat> list=query.list();
		if(list!=null && list.size()>0) {
			return list.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Member getMemberByMobile(String mobile) {
		String hql="from Member where mobile=? and status=1";
		Query query=getSession().createQuery(hql);
		query.setParameter(0, mobile);
		List<Member> list=query.list();
		if(list!=null && list.size()>0) {
			return list.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public RedeemCodeOwner getOwnCode(String mobile) {
		String hql="from RedeemCodeOwner where owner=? and status=?";
		Query query=getSession().createQuery(hql);
		query.setParameter(0, mobile);
		query.setParameter(1, 1);
		List<RedeemCodeOwner> list=query.list();
		if(list!=null && list.size()>0) {
			return list.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getIosNotNewestVersion() {
		String hql="SELECT mobile AS iosNum FROM Member WHERE terminal='ios' AND status=1 AND mobile IS NOT NULL AND mobile!='' AND appVersion != (SELECT versionName FROM Versions " + 
				"WHERE terminal = 'ios' AND versionCode =(SELECT MAX(versionCode) FROM Versions WHERE terminal = 'ios'))";
		Query query=getSession().createQuery(hql);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getAndroidNotNewestVersion() {
		String hql="SELECT mobile AS androidNum FROM Member WHERE terminal='android' AND status=1 AND mobile IS NOT NULL AND mobile!='' AND appVersion != (SELECT versionName " + 
				"FROM Versions WHERE terminal = 'android' AND versionCode =(SELECT MAX(versionCode) FROM Versions WHERE terminal = 'android'))";
		Query query=getSession().createQuery(hql);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getIosNewestVersion() {
		String hql="SELECT mobile AS iosNum FROM Member WHERE terminal='ios' AND status=1 AND mobile IS NOT NULL AND mobile!='' AND appVersion = (SELECT versionName FROM Versions " + 
				"WHERE terminal = 'ios' AND versionCode =(SELECT MAX(versionCode) FROM Versions WHERE terminal = 'ios'))";
		Query query=getSession().createQuery(hql);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getAndroidNewestVersion() {
		String hql="SELECT mobile AS androidNum FROM Member WHERE terminal='android' AND status=1 AND mobile IS NOT NULL AND mobile!='' AND appVersion = (SELECT versionName " + 
				"FROM Versions WHERE terminal = 'android' AND versionCode =(SELECT MAX(versionCode) FROM Versions WHERE terminal = 'android'))";
		Query query=getSession().createQuery(hql);
		return query.list();
	}

	@Override
	public void saveBullet(VideoBullet bullet) {
		getHibernateTemplate().save(bullet);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Pager pullBullet(Long videoId, int pageNo, int pageSize) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(VideoBullet.class);
		criteria.add(Restrictions.eq("videoId", videoId))
		.add(Restrictions.eq("usable", 1))
		.addOrder(Order.desc("gmtCreate"));
		int rowCount = (int)criteria.setProjection(Projections.rowCount()).uniqueResult();
		criteria.setProjection(null);
		criteria.setFirstResult(pageSize * (pageNo - 1));
		criteria.setMaxResults(pageSize);
		List<VideoBullet> videoBulletList = criteria.list();
		return new Pager(pageSize, pageNo, rowCount, videoBulletList);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ViewerBetweenLive> watchRecord(Long videoId) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(ViewerBetweenLive.class);
		criteria.add(Restrictions.eq("videoId", videoId));
		List<ViewerBetweenLive> viewers = criteria.list();
		return viewers;
	}

	// 获取用户评价的记录
	@Override
	public RecordEstimate getRecordEstimate(Long memberId, Long videoId, int seriesVideoId) {

			String hql = "from RecordEstimate where memberId = " + memberId + " and videoId = " + videoId;

			if (seriesVideoId != -1) {
				hql = hql + " and seriesVideoId = " + seriesVideoId;
			}
			Query query = getSession().createQuery(hql);
			List<RecordEstimate> list = query.list();
			RecordEstimate recordEstimate = null;
			if (list.size() == 1) {
				recordEstimate = (RecordEstimate) list.get(0);
			}

			return recordEstimate;
		}

	// 获取评价记录 分页
	@Override
	public Pager getRecordEstimate(int buyStatus, Long videoId, int pageSize, int pageNo) {

			Integer classify = getLiveVideoById(videoId).getClassify();

			String hql = "from RecordEstimate where status = 1 ";

			if (classify == 1) {
				hql += " and seriesVideoId = " + videoId;
			} else {
				hql += " and videoId = " + videoId;
			}

			hql += " and content IS NOT NULL order by contentTime desc";
			int rowCount = getRecordEstimateCount(classify, videoId);
			Query query = getSession().createQuery(hql);
			query.setFirstResult(pageSize * (pageNo - 1));
			query.setMaxResults(pageSize);

			List<RecordEstimate> list = query.list();
			return new Pager(pageSize, pageNo, rowCount, list);
		}

		@SuppressWarnings("unchecked")
		private int getRecordEstimateCount(Integer classify, Long videoId) {
			String hql = "select count(*) from RecordEstimate where status = 1 ";
			if (classify == 1) {
				hql += " and seriesVideoId = " + videoId;
			} else {
				hql += " and videoId = " + videoId;
			}
			
			hql += " and content IS NOT NULL order by contentTime desc";
			Query query = getSession().createQuery(hql);
			List list = query.list();
			return Integer.valueOf(list.get(0).toString());
		}
		
		// 根据视频id 或 系列视频id 获取 评价记录
		@Override
		public List<RecordEstimate> getRecordEstimate(int videoId, int seriesVideoId) {

			String hql = "from RecordEstimate where status = 1 ";

			if (seriesVideoId != -1 && seriesVideoId != 0) {
				hql += " and seriesVideoId = " + seriesVideoId;
			} else if (videoId != -1 && seriesVideoId == -1 && seriesVideoId != 0) {
				hql += " and videoId = " + videoId;
			}

			if (seriesVideoId == 0) {
				Long fatherId = this.getLiveVideoById((long)videoId).getFatherId();
				if(fatherId==0L){
					hql += " and seriesVideoId = " + videoId;
				}else{
					hql += " and videoId = " + videoId;
				}
			}

			hql += " order by contentTime desc";

			Query query = getSession().createQuery(hql);

			@SuppressWarnings("unchecked")
			List<RecordEstimate> list = query.list();

			return list;
		}

		@Override
		public void updateRecordEstimate(RecordEstimate recordEstimate) {
			getHibernateTemplate().update(recordEstimate);
		}

		@Override
		public int saveRecordEstimate(RecordEstimate recordEstimate) {
			Serializable save = getHibernateTemplate().save(recordEstimate);
			return (Integer) save;
		}

		@Override
		public RecordEstimate getRecordEstimate(Integer id2) {

			return (RecordEstimate) this.getHibernateTemplate().get(RecordEstimate.class, id2);
		}
	@SuppressWarnings("unchecked")
	@Override
	public VideoBullet getNewBullet() {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(VideoBullet.class);
		criteria.add(Restrictions.eq("usable", 1))
		.addOrder(Order.desc("id"));
		List<VideoBullet> bullets = criteria.list();
		if(!bullets.isEmpty() && bullets.size()>0) {
			return bullets.get(0);
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<RedeemCodeOwner> sendCodeToCoupn() {
		String hql="FROM RedeemCodeOwner WHERE redeemCodeId=2 AND used=0";
		Query query=getSession().createQuery(hql);
		List<RedeemCodeOwner> list=query.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PopularListVo> sendInviteActivity(Long videoId) {
		String sql = "SELECT member.id as memberId,realname as memberName,mobile,photo,COUNT(*) AS audienceCount FROM invite_statis LEFT JOIN live_video ON invite_statis.related_id = live_video.id"
				+ " LEFT JOIN member ON member.id=invite_statis.app_from_id WHERE related_id=? AND from_type=0 GROUP BY app_from_id HAVING COUNT(*) <=2 ORDER BY COUNT(*) DESC";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter(0, videoId);
		query.addScalar("memberId", Hibernate.LONG);
		query.addScalar("memberName", Hibernate.STRING);
		query.addScalar("mobile", Hibernate.STRING);
		query.addScalar("photo", Hibernate.STRING);
		query.addScalar("audienceCount", Hibernate.INTEGER);
		query.setResultTransformer(Transformers.aliasToBean(PopularListVo.class));
		List<PopularListVo> list = query.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> sendUseCoupn() {
		String sql="SELECT B.owner FROM user_coupn A INNER JOIN redeem_code_owner B ON A.memberId=B.user_id WHERE A.coupnId=14 AND A.used=0";
		SQLQuery query=getSession().createSQLQuery(sql);
		List<String> list=query.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Pager pullWalls(Long videoId, int pageNo, int pageSize) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(VideoWall.class);
		criteria.add(Restrictions.eq("videoId", videoId))
		.add(Restrictions.eq("usable", 1))
		.addOrder(Order.asc("gmtCreate"));
		int rowCount = (int)criteria.setProjection(Projections.rowCount()).uniqueResult();
		criteria.setProjection(null);
		criteria.setFirstResult(pageSize * (pageNo - 1));
		criteria.setMaxResults(pageSize);
		List<VideoWall> walls = criteria.list();
		return new Pager(pageSize, pageNo, rowCount, walls);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SilenceList> getSilenceList(String groupId) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(SilenceList.class);
		criteria.add(Restrictions.eq("groupId", groupId))
		.add(Restrictions.eq("status", 1));
		List<SilenceList> silenceLists = criteria.list();
		return silenceLists;
	}

	@Override
	public void relieveSilenceByGroupId(String groupId) {
		Session session = this.getSession();
		String sql = "UPDATE silence_list SET status=0 WHERE group_id = :groupId";
		session.createSQLQuery(sql).setString("groupId", groupId).executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public FreeVideo getFreeVideo(Long videoId) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(FreeVideo.class);
		criteria.add(Restrictions.eq("videoId", videoId));
		List<FreeVideo> freeVideoList = criteria.list();
		if(freeVideoList.size() > 0) {
			return freeVideoList.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Long getAppRecommendTimes(Long memberId, Long videoId) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(InviteStatis.class);
		criteria.add(Restrictions.eq("appFromId", memberId))
		.add(Restrictions.eq("relatedId", videoId));
		List<InviteStatis> inviteStatisList = criteria.list();
		return new Long((long)inviteStatisList.size());
	}

	@SuppressWarnings("unchecked")
	@Override
	public Long getWechatRecommendTimes(String openId, Long videoId) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(InviteStatis.class);
		criteria.add(Restrictions.eq("mpFromId", openId))
		.add(Restrictions.eq("relatedId", videoId));
		List<InviteStatis> inviteStatis = criteria.list();
		return new Long((long)inviteStatis.size());
	}

	@SuppressWarnings("unchecked")
	@Override
	public LiveVideoDiscount getLiveVideoDiscountById(long videoId) {
		Query query=getSession().createQuery("FROM LiveVideoDiscount WHERE vid=? ");
		query.setParameter(0, videoId);
		List<LiveVideoDiscount> list=query.list();
		if(list!=null && list.size()>0) {
			return list.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int getInviteStatis(Long memberId, long videoId) {
		String hql="FROM InviteStatis WHERE appFromId=? AND relatedId=? ";
		Query query=getSession().createQuery(hql);
		query.setParameter(0, memberId);
		query.setParameter(1, videoId);
		List<InviteStatis> list=query.list();
		return list.size();
	}

	@Override
	public void saveCoupn(Coupn coupn) {
		getSession().save(coupn);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Coupn getCoupnByVideoId(long videoId,String scopeNum) {
		String hql="FROM Coupn WHERE scopeNum=? AND videoId=? ORDER BY id DESC ";
		Query query=getSession().createQuery(hql);
		query.setParameter(0, scopeNum);
		query.setParameter(1, Integer.valueOf(String.valueOf(videoId)));
		List<Coupn> list=query.list();
		if(list!=null && list.size()>0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public void saveUserCoupn(CoupnUser coupnUser) {
		getSession().save(coupnUser);
	}

	@Override
	public Integer getDistributorIntegralByMemberId(Long memberId) {
		String sql = "select sum(integral) as integral from live_video_distributor where mid = ? and status = 1";
		SQLQuery query = this.getSession().createSQLQuery(sql);
		query.setLong(0, memberId);
		query.addScalar("integral", Hibernate.INTEGER);
		List<Integer> list = query.list();
		if(list.size() > 0) {
			if(null == list.get(0)) {
				return null;
			}
			return (Integer)list.get(0);
		}
		return null;
	}

}
