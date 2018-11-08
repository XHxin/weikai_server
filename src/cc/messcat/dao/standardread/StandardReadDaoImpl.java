package cc.messcat.dao.standardread;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import cc.messcat.bases.BaseDaoImpl;
import cc.messcat.entity.ExpenseTotal;
import cc.messcat.entity.Collect;
import cc.messcat.entity.Member;
import cc.messcat.entity.Share;
import cc.messcat.entity.StandardReading;
import cc.modules.commons.Pager;
import cc.modules.util.CollectionUtil;

public class StandardReadDaoImpl extends BaseDaoImpl implements StandardReadDao {

	/*
	 * 标准解读列表
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<StandardReading> searchStandardReading(String type, Long standardReadingId) {
		List<StandardReading> resultList = new ArrayList<StandardReading>();
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(StandardReading.class);
		criteria.add(Restrictions.eq("type", type));
		criteria.add(Restrictions.eq("id", standardReadingId));
		criteria.add(Restrictions.eq("status", "1")); // 启用状态
		criteria.add(Restrictions.eq("checked", "1")); // 审核（0：未审核 1：审核 2:审核不通过）
		criteria.addOrder(Order.desc("editTime")); // 按编辑时间降序排列
		resultList = criteria.list();
		if (CollectionUtil.isListNotEmpty(resultList)) {
			StandardReading entity = new StandardReading();
			entity = resultList.get(0);
			resultList.clear();
			resultList.add(entity);
		}
		return resultList;
	}

	@Override
	public Member getMember(Long memberID) {
		Member member = (Member) getHibernateTemplate().get(Member.class, memberID);
		return member;
	}

	public StandardReading get(Long id) {
		return (StandardReading) getHibernateTemplate().get(StandardReading.class, id);
	}

	/**
	 * 首页中的热门专栏列表
	 */
	@SuppressWarnings("unchecked")
	public List<StandardReading> getStandardReadList(Integer pageNo, Integer pageSize2) {
		Session session = this.getSession();
		String hql = "from StandardReading where classify=? and status=? and isRecommend=? and isShowIndex in (10,11)  and checked=? "
				+ " and author.id in (select id from Member where status=1) order by type desc"; // 根据type降序，把连载解读放在了第一条
		Query query = session.createQuery(hql);
		query.setParameter(0, "1"); // classify=1的为标准解读
		query.setParameter(1, "1"); // 启用状态
		query.setParameter(2, "1"); // 是热门
		query.setParameter(3, "1"); // 审核（0：未审核 1：审核 2:审核不通过）
		query.setFirstResult(pageSize2 * (pageNo - 1));
		query.setMaxResults(pageSize2);
		List<StandardReading> result = query.list();
		return result;
	}


	@SuppressWarnings("unchecked")
	@Override
	public boolean getCollect(Long memberID, Long standardID) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(Collect.class).add(Restrictions.eq("memberId", memberID))
				.add(Restrictions.eq("relatedId", standardID))
				// .add(Restrictions.eq("type", 2))
				.add(Restrictions.eq("status", "1")); // 启用状态
		List<Collect> list = criteria.list();
		if (CollectionUtil.isListNotEmpty(list)) {
			return true;
		}
		return false;
	}


	@SuppressWarnings("unchecked")
	@Override
	public ExpenseTotal getBuys(Long memberID, Long standardID) {
		ExpenseTotal buys = new ExpenseTotal();
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(ExpenseTotal.class).add(Restrictions.eq("memberId", memberID))
				.add(Restrictions.eq("relatedId", standardID)); // 启用状态
		List<ExpenseTotal> list = criteria.list();
		if (CollectionUtil.isListNotEmpty(list)) {
			buys = list.get(0);
			return buys;
		}
		return null;
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public List<StandardReading> getStandardReadingList3(String standardCode, String type, int pageNo,
			Integer pageSize) {
		StringBuffer str = new StringBuffer();
		String hql = null;
		if (type.equals("1")) {// 连载解读,查询列表,不返回子解读
			hql = "from StandardReading where  type=1 and fatherId =0 and status=1 and checked=1";
		} else if (type.equals("0")) { // 单一解读
			hql = "from StandardReading where  type=0 and fatherId =0 and status=1 and checked=1";
		} else if (type.equals("2")) { // 给H5用的Hql
			hql = "from StandardReading where ((type=1 and fatherId =0) or (type=0 and fatherId =0)) and status=1 and checked=1";
		}
		String hql2 = " and author.id in (select id from Member where status=1)"; // 避免数据库出现不存在的会员Id时出错
		str.append(hql).append(hql2);
		if (standardCode != null && !"".equals(standardCode)) {
			str.append(" and  (code like '%" + standardCode + "%'" + " or title like '%" + standardCode + "%'"
					+ " or authorName like '%" + standardCode + "%')");
		}
		if (type.equals("0") || type.equals("1")) {
			str.append(" order by editTime desc");
		} else if (type.equals("2")) {
			str.append(" order by classify asc,type desc"); // H5页面要按照classify升序排列
		}
		Session session = this.getSession();
		Query query = session.createQuery(str.toString());
		List<StandardReading> result = query.list();
		return result;
	}

	/**
	 * 根据关键字模糊查询标准解读
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<StandardReading> getStandardReadingList4(String searchKeyWord, int pageNo, int pageSize) {
		StringBuffer str = new StringBuffer();
		String hql = "from StandardReading where fatherId =0 and status=1 and checked=1";
		String hql2 = " and author.id in (select id from Member where status=1)"; // 避免数据库出现不存在的会员Id时出错
		str.append(hql).append(hql2);
		searchKeyWord = searchKeyWord.trim();
		if (searchKeyWord != null && !"".equals(searchKeyWord)) {
			str.append(" and  (code like '%" + searchKeyWord + "%'" + " or title like '%" + searchKeyWord + "%'"
					+ " or authorName like '%" + searchKeyWord + "%')");
		}
		str.append(" order by editTime desc");
		Session session = this.getSession();
		Query query = session.createQuery(str.toString());
		List<StandardReading> result = query.list();
		return result;
	}

	/**
	 * 热门专栏
	 */

	@SuppressWarnings("unchecked")
	@Override
	public Pager getStandardReadingList5(String type, int pageNo, int pageSize, String isRecommend) {
		Session session = this.getSession();
		String hql = "";
		if (type.equals("1")) {// 连载解读
			hql = "from StandardReading where  type=1 and fatherId =0 and status=? and checked=? and isRecommend=? and classify=? "
					+ " and author.id in (select id from Member where status=1)  order by editTime desc"; // 避免数据库出现不存在的会员Id时出错
		} else { // 单一解读
			hql = "from StandardReading where  type=0 and fatherId =0 and status=? and checked=? and isRecommend=? and classify=? "
					+ " and author.id in (select id from Member where status=1)  order by editTime desc"; // 避免数据库出现不存在的会员Id时出错
		}
		Query query = session.createQuery(hql);
		query.setParameter(0, "1"); // 启用状态
		query.setParameter(1, "1"); // 审核（0：未审核 1：审核 2:审核不通过）
		query.setParameter(2, "1"); // 是热门
		query.setParameter(3, "1"); // classify=1的为标准解读
		int rowCount = query.list().size();
		query.setFirstResult(pageSize * (pageNo - 1));
		query.setMaxResults(pageSize);
		List<StandardReading> result = query.list();
		return new Pager(pageSize, pageNo, rowCount, result);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Pager getHisStandardReadList(int pageNo, int pageSize, Long expertId, String type) {
		String hql="from StandardReading where type=? and fatherId='0' and author.id=?  and status=? and checked=? order by editTime desc";
		Query query=getSession().createQuery(hql);
		query.setParameter(0, type);
		query.setParameter(1, expertId);
		query.setParameter(2, "1");
		query.setParameter(3, "1");
		List<StandardReading> result = query.list();
		query.setFirstResult((pageNo-1)*pageSize);
		query.setMaxResults(pageSize);
		List<StandardReading> list=query.list();
		return new Pager(pageSize, pageNo, result.size(), list);
	}

	/**
	 * 加载连载解读的子解读
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<StandardReading> getSubStandardList2(Long standardId) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(StandardReading.class);
		criteria.add(Restrictions.eq("fatherId", standardId));
		criteria.add(Restrictions.eq("type", "0")); // type=0,fatherId!=0的才是连载里面的子解读
		criteria.add(Restrictions.eq("status", "1")); // 启用状态
		criteria.add(Restrictions.eq("checked", "1"));
		criteria.addOrder(Order.desc("editTime")); // 按编辑时间降序排列
		List<StandardReading> list = criteria.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StandardReading> getSubStandardList3(Long standardId) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(StandardReading.class);
		criteria.add(Restrictions.eq("fatherId", Long.valueOf("0")));
		criteria.add(Restrictions.eq("type", "1")); // type=1,fatherId=0的才是连载
		criteria.add(Restrictions.eq("status", "1")); // 启用状态
		criteria.add(Restrictions.eq("id", standardId));
		criteria.add(Restrictions.eq("checked", "1")); // 审核（0：未审核 1：审核 2:审核不通过）
		criteria.addOrder(Order.desc("editTime")); // 按编辑时间降序排列
		List<StandardReading> list = criteria.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StandardReading> getSubStandardList4(Long standardId) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(StandardReading.class);
		criteria.add(Restrictions.eq("id", standardId));
		criteria.add(Restrictions.eq("fatherId", Long.valueOf("0")));
		criteria.add(Restrictions.eq("type", "0")); // type=0,fatherId=0的才是单一解读
		criteria.add(Restrictions.eq("status", "1")); // 启用状态
		//criteria.add(Restrictions.eq("classify", "1"));  //五期的时候文章全部调此接口,所以过滤过此筛选条件
		criteria.add(Restrictions.eq("checked", "1")); // 审核（0：未审核 1：审核 2:审核不通过）
		criteria.addOrder(Order.desc("editTime")); // 按编辑时间降序排列
		List<StandardReading> list = criteria.list();
		return list;
	}

	/**
	 * 把连载解读的Id做为子解读的fatherID，去加载子解读列表
	 */
	@SuppressWarnings("unchecked")
	public List<StandardReading> getSubByfatherId(Long fatherID, String type) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(StandardReading.class);
		criteria.add(Restrictions.eq("fatherId", fatherID));
		// criteria.add(Restrictions.eq("classify", type)); //classify=1的为标准解读
		criteria.add(Restrictions.eq("status", "1")); // 启用状态
		criteria.add(Restrictions.eq("checked", "1")); // 审核（0：未审核 1：审核 2:审核不通过）
		criteria.addOrder(Order.desc("editTime")); // 按编辑时间降序排列
		List<StandardReading> result = criteria.list();
		return result;
	}

	/*
	 * 获取分享URL
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Share getShare() {
		Criteria criteria = getSession().createCriteria(Share.class);
		criteria.add(Restrictions.eq("shareType", "1"));
		criteria.add(Restrictions.eq("status", "1"));
		List<Share> list = criteria.list();
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/*
	 * 专供H5的 热门专栏推荐列表
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<StandardReading> getStandardReadingListH5(int pageNo, int pageSize) {
		String hql = "from StandardReading where status=1 and checked=1 and isShowIndex in (01,11,1) order by editTime desc";
		Query query = getSession().createQuery(hql);
		query.setFirstResult((pageNo - 1) * pageSize * 3);
		query.setMaxResults(pageSize * 3);
		List<StandardReading> list = query.list();
		return list;
	}

	@Override
	public List<StandardReading> getStandardReadListBySearchKeyWord(String searchKeyWord) {
		String hql = "from StandardReading where status=1 and checked=1 and type = 0 and fatherId = 0"
				+ " and( title like '%" + searchKeyWord + "%'" + " or authorName like '%" + searchKeyWord + "%')"
				+ " order by editTime desc";

		Query query = getSession().createQuery(hql);
		query.setFirstResult(0);
		query.setMaxResults(3);
		@SuppressWarnings("unchecked")
		List<StandardReading> list = query.list();

		return list;
	}

	/**
	 * 首页的专栏订阅和查看更多(连载)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Pager getColumnList(int pageNo, int pageSize, String chiefShow) {
		StringBuffer sbf=new StringBuffer();
		String hql="from StandardReading where status=1 and checked=1 and type=1 ";
		//查看更多
		if(chiefShow.equals("0")) {
			sbf.append(hql).append(" and isColumnSubscribe in (1,2)");
			Query query=getSession().createQuery(sbf.toString());
			List<StandardReading> list=query.list();
			query.setFirstResult((pageNo-1)*pageSize);
			query.setMaxResults(pageSize);
			List<StandardReading> resultList=query.list();
			return new Pager(pageSize,pageNo,list.size(),resultList);
		//首页推荐
		}else {
			sbf.append(hql).append(" and isColumnSubscribe=1 ");
			Query query=getSession().createQuery(sbf.toString());
			List<StandardReading> resultList=query.list();
			return new Pager(pageSize,pageNo,resultList.size(),resultList);
		}
	}

	/**
	 * 首页的精品文章和查看更多(单一)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Pager getExcellentArticleList(int pageNo, int pageSize, String chiefShow) {
		StringBuffer sbf=new StringBuffer();
		String hql="from StandardReading where status=1 and checked=1 and type=0 and fatherId=0 ";
		//查看更多
		if(chiefShow.equals("0")) {
			sbf.append(hql).append(" and isColumnSubscribe=2");
			Query query=getSession().createQuery(sbf.toString());
			List<StandardReading> list=query.list();
			query.setFirstResult((pageNo-1)*pageSize);
			query.setMaxResults(pageSize);
			List<StandardReading> resultList=query.list();
			return new Pager(pageSize,pageNo,list.size(),resultList);
		//首页推荐
		}else {
			sbf.append(hql).append(" and isColumnSubscribe=1");
			Query query=getSession().createQuery(sbf.toString());
			List<StandardReading> resultList=query.list();
			return new Pager(pageSize,pageNo,resultList.size(),resultList);
		}
	}

	@Override
	public List<StandardReading> getStandardReadSerialise(String searchKeyWord) {
		String hql = "from StandardReading where status=1 and checked=1 and type = 1 and fatherId = 0 and( title like '%"
				+ searchKeyWord + "%'" + " or authorName like '%" + searchKeyWord + "%')" + " order by editTime desc";
		Query query = getSession().createQuery(hql);
		query.setFirstResult(0);
		query.setMaxResults(3);
		@SuppressWarnings("unchecked")
		List<StandardReading> list = query.list();

		return list;
	}

	@Override
	public List<StandardReading> getMoreStandardRead(String searchKeyWord, String qualityId, int pageNo, int pageSize) {

		String hql = "";
		if(StringUtils.isNotBlank(qualityId)){
			hql = "from StandardReading where status=1 and checked=1 and type = 0 and fatherId = 0 and qualityId="
					+ qualityId + " and( title like '%" + searchKeyWord + "%'" + " or authorName like '%" + searchKeyWord
					+ "%')" + " order by editTime desc";
		}else{
			hql = "from StandardReading where status=1 and checked=1 and type = 0 and fatherId = 0 "+ " and( title like '%" + searchKeyWord + "%'" + " or authorName like '%" + searchKeyWord
					+ "%')" + " order by editTime desc";
		}

		Query query = getSession().createQuery(hql);
		query.setFirstResult(pageSize*(pageNo-1));
		query.setMaxResults(pageSize*pageNo);
		@SuppressWarnings("unchecked")
		List<StandardReading> list = query.list();

		return list;
	}

	@Override
	public List<StandardReading> getStandardReadSerialise(String searchKeyWord, int pageNo, int pageSize) {
		String hql = "from StandardReading where status=1 and checked=1 and type = 1 and fatherId = 0 and( title like '%"
				+ searchKeyWord + "%'" + " or authorName like '%" + searchKeyWord + "%')" + " order by editTime desc";
		Query query = getSession().createQuery(hql);
		query.setFirstResult(pageSize*(pageNo-1));
		query.setMaxResults(pageSize*pageNo);
		@SuppressWarnings("unchecked")
		List<StandardReading> list = query.list();

		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ExpenseTotal getisBuyStand(Long memberId, Long id) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(ExpenseTotal.class).add(Restrictions.eq("memberId", memberId))
				.add(Restrictions.eq("relatedId", id))
				.add(Restrictions.or(Restrictions.eq("type", "2"), Restrictions.eq("type", "3"))); // 启用状态
		List<ExpenseTotal> list = criteria.list();
		if (CollectionUtil.isListNotEmpty(list)) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 专栏订阅的购买状态
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ExpenseTotal getColumnBuyStatus(Long memberId, Long articleId) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(ExpenseTotal.class).add(Restrictions.eq("memberId", memberId))
				.add(Restrictions.eq("relatedId", articleId)).add(Restrictions.eq("payStatus", "1"))
				.add(Restrictions.in("type", new String[] {"2","3","9"})); // 启用状态
		List<ExpenseTotal> list = criteria.list();
		if (CollectionUtil.isListNotEmpty(list)) {
			return list.get(0);
		}
		return null;
	}

}
