package cc.messcat.dao.member;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import cc.messcat.entity.*;
import cc.modules.util.CollectionUtil;
import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import cc.messcat.bases.BaseDaoImpl;
import cc.modules.commons.Pager;
import cc.modules.util.EmojiFilter;
import cc.modules.util.IpUtil;
import cc.modules.util.ObjValid;

public class MemberDaoImpl extends BaseDaoImpl implements MemberDao {

	public MemberDaoImpl() {
	}

	public void save(Member member) {
		getHibernateTemplate().save(member);
	}

	public void update(Member member) {
		getSession().setFlushMode(FlushMode.AUTO);
		getHibernateTemplate().update(member);
	}

	public void delete(Member member) {
		member.setStatus("0");
		getHibernateTemplate().update(member);
	}

	public void delete(Long id) {
		Member member = this.get(id);
		member.setStatus("0");
		getHibernateTemplate().update(member);
	}

	public Member get(Long id) {
		getSession().setFlushMode(FlushMode.AUTO);
		return (Member) getHibernateTemplate().get(Member.class, id);
	}

	public List findAll() {
		return getHibernateTemplate().find("from Member where status = 1 order by editTime desc");
	}

	public Pager getPager(int pageSize, int pageNo) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(Member.class);

		criteria.add(Restrictions.eq("status", "1"));
		criteria.addOrder(Order.desc("editTime"));
		int rowCount = ((Integer) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		criteria.setProjection(null);
		int startIndex = pageSize * (pageNo - 1);
		criteria.setFirstResult(startIndex);
		criteria.setMaxResults(pageSize);
		List result = criteria.list();
		return new Pager(pageSize, pageNo, rowCount, result);
	}

	public Pager getPager(int pageSize, int pageNo, Member member) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(Member.class);
		if (ObjValid.isValid(member)) {
			if (ObjValid.isValid(member.getMobile())) {
				criteria.add(Restrictions.eq("mobile", member.getMobile()));
			}
			if (ObjValid.isValid(member.getIsSync()) && !("-1").equals(member.getIsSync())) {
				criteria.add(Restrictions.eq("isSync", member.getIsSync()));
			}
		}
		criteria.add(Restrictions.eq("status", "1"));
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
	public List<Member> findByhql(String string) {
		return this.getHibernateTemplate().find(string);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BuysRecord> getBuysRecordList(String type, Long memberId, int pageNo, int pageSize) {
		Session session = this.getSession();// getHibernateTemplate().getSessionFactory().openSession();
		Criteria criteria = session.createCriteria(BuysRecord.class);
		criteria.add(Restrictions.eq("memberId", memberId));
		criteria.add(Restrictions.eq("type", type));

		int startIndex = pageSize * (pageNo - 1);
		criteria.setFirstResult(startIndex);
		criteria.setMaxResults(pageSize);
		List<BuysRecord> result = criteria.list();
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public StandardReading getStandard(Long memberId) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(StandardReading.class);
		criteria.add(Restrictions.eq("author.id", memberId));
		List<StandardReading> list = criteria.list();
		if (!list.isEmpty() && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 查询准入报告的价格
	 */
	@SuppressWarnings("unchecked")
	@Override
	public DivideScaleCommon getDivideScaleCommon() {
		Criteria criteria = getSession().createCriteria(DivideScaleCommon.class);
		List<DivideScaleCommon> list = criteria.list();
		if (list != null && list.size() != 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public Member retrieveMember(String mobile) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(Member.class);
		criteria.add(Restrictions.eq("mobile", mobile));
		criteria.add(Restrictions.eq("status", "1"));
		List<Member> list = criteria.list();
		if (!list.isEmpty() && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public Member getRecommend_times(Long recommender_id) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(Member.class);
		criteria.add(Restrictions.eq("id", recommender_id));
		List<Member> list = criteria.list();
		if (!list.isEmpty() && list.size() > 0) {
			return list.get(0);

		}
		return null;
	}

	@Override
	public Object existRecommendId(Long id) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(Member.class);
		criteria.add(Restrictions.eq("memberId", id));
		List<Member> list = criteria.list();
		if (!list.isEmpty() && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/*
	 * 按第三方登录的openId得到一个MemAuth
	 */
	@SuppressWarnings("unchecked")
	@Override
	public MemAuth getMemberAuth(MemAuth memAuth) {
		Criteria criteria = getSession().createCriteria(MemAuth.class);
		if (memAuth.getLoginType().equals("1")) {
			criteria.add(Restrictions.eq("weiXinOpenId", memAuth.getWeiXinOpenId()));
		} else if (memAuth.getLoginType().equals("2")) {
			criteria.add(Restrictions.eq("qqOpenId", memAuth.getQqOpenId()));
		}
		List<MemAuth> list = criteria.list();
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 以下三个方法在三方登录和绑定手机处用到
	 */
	@Override
	public void updateLoginThirdRecord(MemAuth entity) {
		getSession().update(entity);
	}

	@Override
	public void addMemAuth(MemAuth memAuth, Long memberId, HttpServletRequest request) {
		MemAuth entity = new MemAuth();
		entity.setMemberId(memberId);
		entity.setLoginType(memAuth.getLoginType());
		entity.setNewLoginIP(IpUtil.getIpAddr(request));
		entity.setNewLoginTime(new Date());
		if (memAuth.getWeiXinOpenId() != null && !"".equals(memAuth.getWeiXinOpenId())) {
			entity.setWeiXinOpenId(memAuth.getWeiXinOpenId());
			entity.setWeiXinNiceName(EmojiFilter.filterEmoji(memAuth.getWeiXinNiceName()));
			entity.setWeiXinPhoto(memAuth.getWeiXinPhoto());
		}
		if (memAuth.getQqOpenId() != null && !"".equals(memAuth.getQqOpenId())) {
			entity.setQqOpenId(memAuth.getQqOpenId());
			entity.setQqNickName(EmojiFilter.filterEmoji(memAuth.getQqNickName()));
			entity.setQqPhoto(memAuth.getQqPhoto());
		}
		getSession().save(entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Member saveMember(Member member2) {
		getSession().save(member2);
		Criteria criteria = getSession().createCriteria(Member.class);
		criteria.add(Restrictions.eq("mobile", member2.getMobile()));
		List<Member> list = criteria.list();
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public MemAuth getMemberAuth(Long memberId, MemAuth memAuth) {
		Criteria criteria = getSession().createCriteria(MemAuth.class);
		criteria.add(Restrictions.eq("memberId", memberId));
		if (memAuth.getWeiXinOpenId() != null && !"".equals(memAuth.getWeiXinOpenId())) {
			criteria.add(Restrictions.or(Restrictions.isNotNull("weiXinOpenId"),Restrictions.isNotEmpty("weiXinOpenId")));
		}
		if (memAuth.getQqOpenId() != null && !"".equals(memAuth.getQqOpenId())) {
			criteria.add(Restrictions.or(Restrictions.isNotNull("qqOpenId"),Restrictions.isNotEmpty("qqOpenId")));
		}
		List<MemAuth> list = criteria.list();
		if (list != null && list.size() > 0) {
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
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/*
	 * 通过会员ID找到一条会员授权记录
	 */
	@SuppressWarnings("unchecked")
	@Override
	public MemAuth getMemberAuth(Member member1) {
		Criteria criteria = getSession().createCriteria(MemAuth.class);
		criteria.add(Restrictions.eq("memberId", member1.getId()));
		List<MemAuth> list = criteria.list();
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/*
	 * 当退款的时候,更新用户的虚拟币帐户
	 */
	@Override
	public void updateProMember(Member entity) {
		// System.out.println("退用户虚拟币了...............");
		getSession().update(entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Member getUUID(String uuid) {
		Criteria criteria = getSession().createCriteria(Member.class);
		criteria.add(Restrictions.eq("uuid", uuid));
		List<Member> list = criteria.list();
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Member getMobileById(Long memberId) {
		Criteria criteria = getSession().createCriteria(Member.class);
		criteria.add(Restrictions.eq("id", memberId));
		List<Member> list = criteria.list();
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CoupnUser> getUserCoupnByMemberIdAndType(String type, Long memberId, int pageNo, int pageSize, int videoId) {
		Criteria criteria = getSession().createCriteria(CoupnUser.class);
		criteria.add(Restrictions.eq("memberId", memberId));
		criteria.add(Restrictions.eq("used", "0"));
		criteria.createAlias("coupnId", "coupn")
		.add(Restrictions.eq("coupn.usable", "1"))
		.add(Restrictions.lt("coupn.beginDate", new Date()))
		.add(Restrictions.ge("coupn.endDate", new Date()))
		.add(Restrictions.in("coupn.videoId", new Integer[]{0,videoId}))
		.add(Restrictions.like("coupn.scopeNum", "%"+type+"%"));
		List<CoupnUser> list = criteria.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CoupnUser> getAllUserCoupn(Long memberId, String used) {
		String hql = "from UserCoupn where memberId=? and used=? and coupnId.usable=?";
		Query query = getSession().createQuery(hql);
		query.setLong(0, memberId);
		query.setString(1, used);
		query.setString(2, "0");
		List<CoupnUser> coupns = query.list();
		if (!coupns.isEmpty() && coupns.size() > 0) {
			return coupns;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CoupnUser> getCoupnsById(Long[] coupnId) {
		Criteria criteria = getSession().createCriteria(CoupnUser.class);
		criteria.add(Restrictions.in("id", coupnId));
		List<CoupnUser> list = criteria.list();
		if (list != null && list.size() > 0) {
			return list;
		}
		return null;
	}

	@Override
	public void updateUserCoupn(CoupnUser coupnUser) {
		getSession().update(coupnUser);
	}

	@Override
	public void updateCoupn(Coupn coupnId) {
		getSession().update(coupnId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CoupnUser> getAllUserCoupn(Long memberId, int pageNo, int pageSize) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(CoupnUser.class);
		criteria.add(Restrictions.eq("memberId", memberId));
		criteria.addOrder(Order.desc("id"));
		int startIndex = pageSize * (pageNo - 1);
		criteria.setFirstResult(startIndex);
		criteria.setMaxResults(pageSize);
		List<CoupnUser> coupnUsers = criteria.list();
		return coupnUsers;
	}

	@Override
	public List<Coupn> getCoupnById(Long[] coupnId) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(Coupn.class);
		criteria.add(Restrictions.in("id", coupnId));
		criteria.add(Restrictions.eq("usable", "1"));
		List<Coupn> coupns = criteria.list();
		return coupns;
	}

	@Override
	public ExpenseTotal checkBuyStatus(Member member, String stype, Long[] relateId) {
		Criteria criteria = getSession().createCriteria(ExpenseTotal.class);
		criteria.add(Restrictions.eq("memberId", member.getId()));
		criteria.add(Restrictions.eq("type", stype));
		criteria.add(Restrictions.in("relatedId", relateId));
		List<ExpenseTotal> records = criteria.list();
		if(CollectionUtil.isListNotEmpty(records)){
			return records.get(0);
		}
		return null;
	}

	@Override
	public int memberNum() {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(Member.class);
		criteria.add(Restrictions.eq("status", "1"));
		return criteria.list().size();
		
	}

	@Override
	public List<WechatWeiboAttention> retrieveAttention() {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(WechatWeiboAttention.class);
		criteria.add(Restrictions.eq("typeNum", 1));
		List<WechatWeiboAttention> list = criteria.list();
		return list;
	}

	@Override
	public Fusing fusingTimes(String interfaces) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(Fusing.class);
		criteria.add(Restrictions.eq("intefaceName", interfaces));
		List<Fusing> list = criteria.list();
		if(!list.isEmpty() && list.size()>0){
			return list.get(0);
		}else {
			return null;
		}
		
	}

	@Override
	public int maxAttentionNum(int typeNum) {
		String sql = "SELECT * FROM wechat_weibo_attention WHERE typeNum = ? AND value <> 0 ORDER BY id DESC LIMIT 1";
		Session session = this.getSession();
		SQLQuery query = session.createSQLQuery(sql);
		query.setInteger(0, typeNum);
		query.addEntity(WechatWeiboAttention.class);
		List<WechatWeiboAttention> list = query.list();
		if(!list.isEmpty() && list.size()>0){
			return list.get(0).getValue();
		}
		return 0;
	}
	
	/**
	 * 是否存在此兑换码
	 */
	@SuppressWarnings("unchecked")
	@Override
	public RedeemCodeOwner isExistCode(String redeemCode) {
		String hql="from RedeemCodeOwner where redeemCode=? ";
		Query query=getSession().createQuery(hql);
		query.setParameter(0, redeemCode);
		List<RedeemCodeOwner> list=query.list();
		if(list!=null && list.size()>0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 是否存在未使用的兑换码
	 */

	@SuppressWarnings("unchecked")
	@Override
	public RedeemCodeOwner isExistUnUsedCode(String redeemCode) {
		String hql="from RedeemCodeOwner where redeemCode=? and used=0 ";
		Query query=getSession().createQuery(hql);
		query.setParameter(0, redeemCode);
		List<RedeemCodeOwner> list=query.list();
		if(list!=null && list.size()>0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public RedeemCodeStock getRedeemStockById(Long redeemCodeId) {
		return (RedeemCodeStock) getSession().get(RedeemCodeStock.class, redeemCodeId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Coupn coupnByVideoId(int videoId) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(Coupn.class);
		criteria.add(Restrictions.eq("usable", "1"))
		.add(Restrictions.ge("endDate", new Date()))
		.add(Restrictions.eq("videoId", videoId));
		List<Coupn> list = criteria.list();
		if(!list.isEmpty() && list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public StandardReading getStandardById(Long relatedId) {
		return (StandardReading) getSession().get(StandardReading.class, relatedId);
	}

	@Override
	public LiveVideo getLiveVideoById(Long relatedId) {
		return (LiveVideo) getSession().get(LiveVideo.class, relatedId);
	}

	@Override
	public Coupn getCoupnById(Long relatedId) {
		return (Coupn) getSession().get(Coupn.class, relatedId);
	}

	@Override
	public void updateRedeemOwn(RedeemCodeOwner owner, Member member) {
		owner.setUsed(1);  //更改兑换码的使用状态(0:未使用,1:已使用)
		owner.setUserId(member.getId());
		getSession().update(owner);
	}

	/**
	 * 判断此用户是否还能用兑换码
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean isCanExchange(Long redeemCodeId, Long userId) {
		String hql="from RedeemCodeOwner where redeemCodeId=? and userId=? and status=? and used=? ";
		Query query=getSession().createQuery(hql);
		query.setParameter(0, redeemCodeId);
		query.setParameter(1, userId);
		query.setParameter(2, 1);
		query.setParameter(3, 1);
		List<RedeemCodeOwner> list=query.list();
		if(list!=null && list.size()>0) {
			return false;
		}
		return true;
	}

	@Override
	public void addUserCoupn(CoupnUser coupnUser) {
		getSession().save(coupnUser);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isSameTypeActivity(Long redeemCodeId, Long memberId) {
		String hql="from RedeemCodeOwner where redeemCodeId=? and status=? and  userId=?";
		Query query=getSession().createQuery(hql);
		query.setParameter(0, redeemCodeId);
		query.setParameter(1, 1);
		query.setParameter(2, memberId);
		List<RedeemCodeOwner> list=query.list();
		if(list!=null && list.size()>0) {
			return true;
		}
		return false;
	}

	@Override
	public List<Member> getMembersByMemberIds(List<Long> memberIds) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(Member.class);
		criteria.add(Restrictions.in("id", memberIds))
		.add(Restrictions.eq("status", "1"));
		List<Member> members = criteria.list();
		return members;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Packages getPackages(Long relatedId) {
		String hql="FROM Packages WHERE type=? AND status=? ";
		Query query=getSession().createQuery(hql);
		query.setParameter(0, String.valueOf(relatedId));
		query.setParameter(1, "1");
		List<Packages> list=query.list();
		if(list!=null && list.size()>0) {
			return list.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Coupn> getCouponList(Date date) {
		String hql="FROM Coupn WHERE endDate<=? and usable=? ";
		Query query=getSession().createQuery(hql);
		query.setParameter(0, date);
		query.setParameter(1, "1");
		List<Coupn> list=query.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Coupn> getCouponNotExpire(Date afterThreeDays) {
		String hql="FROM Coupn WHERE endDate<=? AND usable=? ";  
		Query query=getSession().createQuery(hql);
		query.setParameter(0, afterThreeDays);
		query.setParameter(1, "1");
		List<Coupn> list=query.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Long> getUnusedCouponPerson(Long couponId) {
		String hql="SELECT memberId FROM UserCoupn WHERE coupnId.id=? AND  used=? ";
		Query query=getSession().createQuery(hql);
		query.setParameter(0, couponId);
		query.setParameter(1, "0");
		List<Long> list=query.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Member> getMemberNotExpire(Date time) {
		String sql="SELECT * FROM member WHERE ((year_end_time<=DATE_ADD(NOW(),INTERVAL 3 DAY) AND TYPE=? AND year_end_time>NOW()) "
				+ "OR (end_time<=DATE_ADD(NOW(),INTERVAL 3 DAY) AND TYPE=? AND end_time>NOW()))"
				+ " AND mobile!='' AND mobile IS NOT NULL AND STATUS=? ";
		List<Member> list=getSession().createSQLQuery(sql)
				.addEntity(Member.class)
				.setParameter(0, "0")
				.setParameter(1, "1")
				.setParameter(2, "1")
				.list();		
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LiveVideo> getLiveAirOn(Date time) {
		String hql="FROM LiveVideo WHERE videoType=? AND bespeakStatus=? AND videoStatus=? AND status=? AND applyDate< ? ";
		Query query=getSession().createQuery(hql);
		query.setParameter(0, "0");
		query.setParameter(1, "1");
		query.setParameter(2, "0");
		query.setParameter(3, "1");
		query.setParameter(4, time);
		List<LiveVideo> list=query.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Coupn saveCoupn(Coupn coupn) {
		getSession().save(coupn);
		String hql="FROM Coupn WHERE scopeNum=? AND videoId=? AND usable=? ORDER BY endDate DESC";
		List<Coupn> list=getSession().createQuery(hql)
		.setParameter(0, "4")
		.setParameter(1, coupn.getVideoId())
		.setParameter(2, "1").list();
		if(list!=null && list.size()>0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public MemberMp findMemberMpByMemberId(Long memberId) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(MemberMp.class);
		criteria.add(Restrictions.eq("memberId", memberId));
		Object memberMp = criteria.uniqueResult();
		if (memberMp != null) {
			return (MemberMp)memberMp;
		}
		return null;
	}

	@Override
	public MemberMp findMemberMpByOpenId(String openId) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(MemberMp.class);
		criteria.add(Restrictions.eq("openId", openId));
		List<MemberMp> memberMpList = criteria.list();
		if(memberMpList.size() > 0) {
			return memberMpList.get(0);
		}
		return null;
	}

	@Override
	public BankMember saveBankMember(BankMember bankMember) {
		getSession().save(bankMember);
		String hql="FROM BankMember WHERE openBank=? AND bankCard=? AND cardHolder=? AND bankMobile=? ";
		Query query = getSession().createQuery(hql);
		query.setParameter(0,bankMember.getOpenBank());
		query.setParameter(1,bankMember.getBankCard());
		query.setParameter(2,bankMember.getCardHolder());
		query.setParameter(3,bankMember.getBankMobile());
		List<BankMember> list=query.list();
		if(CollectionUtil.isListNotEmpty(list)){
			return list.get(0);
		}
		return null;
	}

	@Override
	public MemberMp findMemberMpByOpenIdAndMemberId(Long memberId, String openId) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(MemberMp.class);
		criteria.add(Restrictions.eq("openId", openId))
				.add(Restrictions.eq("memberId",memberId));
		List<MemberMp> memberMpList = criteria.list();
		if(memberMpList.size() > 0) {
			return memberMpList.get(0);
		}
		return null;
	}
}