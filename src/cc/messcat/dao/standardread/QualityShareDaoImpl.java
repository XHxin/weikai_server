package cc.messcat.dao.standardread;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import cc.messcat.bases.BaseDaoImpl;
import cc.messcat.entity.Attention;
import cc.messcat.entity.ExpenseTotal;
import cc.messcat.entity.Collect;
import cc.messcat.entity.Member;
import cc.messcat.entity.QualityType;
import cc.messcat.entity.Share;
import cc.messcat.entity.StandardReading;
import cc.messcat.entity.StandardReadingCatalog;
import cc.messcat.vo.QualityTypeListVo;
import cc.modules.commons.Pager;
import cc.modules.util.CollectionUtil;

public class QualityShareDaoImpl extends BaseDaoImpl implements QualityShareDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<QualityTypeListVo> getQualityTypeList(Integer pageNo, Integer pageSize3) {
		
		List<QualityTypeListVo> resultList=new ArrayList<QualityTypeListVo>();
		Session session=this.getSession();
		String hql="FROM QualityType WHERE status=1 order by sort"; 
		Query query=session.createQuery(hql);
		
		int startIndex = pageSize3 * (pageNo - 1);
		query.setFirstResult(startIndex);
		query.setMaxResults(pageSize3);
		List<QualityType> list = query.list();
		
		for(QualityType entity:list){
			QualityTypeListVo vo=new QualityTypeListVo();
			vo.setQualityTypeId(String.valueOf(entity.getId()));
			vo.setName(entity.getName());
			resultList.add(vo);
		}
		return resultList;
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public Pager getQualityTypeList2(int pageNo, int pageSize,String qualityId,String type) {
		Session session= this.getSession(); 
		String hql="";
        if(type.equals("1")){//连载解读, 查询列表, 不返回子解读
        	hql="from StandardReading where  type=1 and fatherId =0 and status=? and checked=?  and classify=? and qualityId=? "
        	   +" and author.id in (select id from Member where status=1) order by editTime desc";   //避免数据库出现不存在的会员Id时出错
        }else{ //单一解读
        	hql="from StandardReading where  type=0 and fatherId =0 and status=? and checked=?  and classify=? and qualityId=? "
        	   +" and author.id in (select id from Member where status=1) order by editTime desc";   //避免数据库出现不存在的会员Id时出错
        }
        Query query=session.createQuery(hql);
		query.setParameter(0,  "1");    //启用状态
		query.setParameter(1, "1");		//审核（0：未审核  1：已审核）
		query.setParameter(2, "2");	    //classify=2的为质量分享
		query.setParameter(3, Long.valueOf(qualityId));		//质量分享分类Id
		int rowCount= query.list().size();
		query.setFirstResult(pageSize * (pageNo - 1));
		query.setMaxResults(pageSize);
		List<StandardReading> result = query.list();
		return new Pager(pageSize, pageNo, rowCount, result);
	}


	@Override
	public Member getMember(Long memberId) {
		Member member=(Member) getHibernateTemplate().get(Member.class, memberId);
		return member;
	}


	@SuppressWarnings("unchecked")
	@Override
	public boolean getCollect(Long memberID, Long standardId) {
		Session session= this.getSession(); 
		Criteria criteria=session.createCriteria(Collect.class)
				.add(Restrictions.eq("memberId", memberID))
				.add(Restrictions.eq("relatedId", standardId))
				.add(Restrictions.or(Restrictions.eq("type", 2), Restrictions.or(Restrictions.eq("type", 3),Restrictions.eq("type", 9)))) //3为质量分享
				.add(Restrictions.eq("status", "1"));		   //启用状态
		List<Collect> list=criteria.list();
		if(CollectionUtil.isListNotEmpty(list)){
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ExpenseTotal getBuys(Long memberId, Long standardId) {
		Session session= this.getSession();
		Criteria criteria=session.createCriteria(ExpenseTotal.class)
				.add(Restrictions.eq("memberId", memberId))
				.add(Restrictions.eq("relatedId", standardId))
				.add(Restrictions.eq("payStatus", "1"))
				.add(Restrictions.in("type", new String[] {"2","3","9"}));
		List<ExpenseTotal> list=criteria.list();
		if(CollectionUtil.isListNotEmpty(list)){
			return list.get(0);
		}
		return null;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<StandardReading> getSubQualityShare(Long standardId, Long memberID) {
		Session session=this.getSession();
		Criteria criteria=session.createCriteria(StandardReading.class);
		criteria.add(Restrictions.eq("author.id", memberID));
		criteria.add(Restrictions.eq("fatherId", standardId));
		criteria.add(Restrictions.eq("status", "1"));		   //启用状态
		criteria.add(Restrictions.eq("checked", "1")); 		   //审核（0：未审核  1：已审核）
		criteria.addOrder(Order.desc("editTime"));             //按编辑时间降序排列
		List<StandardReading> list=criteria.list();
		return list;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<StandardReading> getQualityShare(Long standardId,String qualityId) {
		Session session=this.getSession();
		Criteria criteria=session.createCriteria(StandardReading.class);
		criteria.add(Restrictions.eq("fatherId", standardId));
		criteria.add(Restrictions.eq("classify", "2"));
		criteria.add(Restrictions.eq("type", "0"));            //type=0,fatherId!=0的才是连载里面的解读
		criteria.add(Restrictions.eq("qualityId", Long.valueOf(qualityId)));
		criteria.add(Restrictions.eq("status", "1"));		   //启用状态
		criteria.add(Restrictions.eq("checked", "1")); 		   //审核（0：未审核  1：已审核）
		criteria.addOrder(Order.desc("editTime"));             //按编辑时间降序排列
		List<StandardReading> list=criteria.list();
		return list;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<StandardReading> getQualityShare2(Long standardId, String qualityId) {
		Session session=this.getSession();
		Criteria criteria=session.createCriteria(StandardReading.class);
		criteria.add(Restrictions.eq("fatherId", Long.valueOf("0")));
		criteria.add(Restrictions.eq("classify", "2"));
		criteria.add(Restrictions.eq("type", "1"));            //type=0,fatherId=0  的才是连载
		criteria.add(Restrictions.eq("qualityId", Long.valueOf(qualityId)));
		criteria.add(Restrictions.eq("id", standardId));
		criteria.add(Restrictions.eq("status", "1"));		   //启用状态
		criteria.add(Restrictions.eq("checked", "1")); 		   //审核（0：未审核  1：已审核）
		criteria.addOrder(Order.desc("editTime"));             //按编辑时间降序排列
		List<StandardReading> list=criteria.list();
		return list;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<StandardReading> getQualityShare3(Long standardId, String qualityId) {
		Session session=this.getSession();
		Criteria criteria=session.createCriteria(StandardReading.class);
		criteria.add(Restrictions.eq("id", standardId));
		criteria.add(Restrictions.eq("fatherId", Long.valueOf("0")));
		criteria.add(Restrictions.eq("classify", "2"));
		criteria.add(Restrictions.eq("type", "0"));            //type=0,fatherId=0  的才是解读
		criteria.add(Restrictions.eq("qualityId", Long.valueOf(qualityId)));
		criteria.add(Restrictions.eq("status", "1"));		   //启用状态
		criteria.add(Restrictions.eq("checked", "1")); 		   //审核（0：未审核  1：已审核）
		criteria.addOrder(Order.desc("editTime"));             //按编辑时间降序排列
		List<StandardReading> list=criteria.list();
		return list;
	}

    /*
     * 详情接口(标准解读,质量分享共享)
     */
	@SuppressWarnings("unchecked")
	@Override
	public StandardReading searchDetail(Long standardReadingId) {
		Criteria criteria=getSession().createCriteria(StandardReading.class);
		criteria.add(Restrictions.eq("id", standardReadingId));
		List<StandardReading> list=criteria.list();
		if(list!=null && list.size()!=0){
			return list.get(0);
		}
		return null;
	}


	@SuppressWarnings("unchecked")
	@Override
	public String getShareURL() {
		Criteria criteria=getSession().createCriteria(Share.class);
		criteria.add(Restrictions.eq("shareType", "1"));
		criteria.add(Restrictions.eq("status", "1"));
		List<Share> list=criteria.list();
		if(list!=null&&list.size()!=0){
			return list.get(0).getShareURL();
		}
		return null;
	}


	//查找该文章所有的一级目录
	@SuppressWarnings("unchecked")
	@Override
	public List<StandardReadingCatalog> getFirstLevel(Long standardReadingId) {
		Criteria criteria=getSession().createCriteria(StandardReadingCatalog.class);
		criteria.add(Restrictions.eq("standardReadId", standardReadingId));
		criteria.add(Restrictions.eq("firstLevel", 1L));
		List<StandardReadingCatalog> list=criteria.list();
		return list;
	}

	//查找该文章该一级目录的所有二级目录
	@SuppressWarnings("unchecked")
	@Override
	public List<StandardReadingCatalog> getSecondLevel(Long standardReadingId, Long id) {
		Criteria criteria=getSession().createCriteria(StandardReadingCatalog.class)
				.add(Restrictions.eq("standardReadId", standardReadingId))
				.add(Restrictions.eq("secondLevel", id));
		List<StandardReadingCatalog> list=criteria.list();
		return list;
	}


	//查找该文章该二级目录 的所有三级目录
	@SuppressWarnings("unchecked")
	@Override
	public List<StandardReadingCatalog> getThreeLevel(Long standardReadingId, Long id) {
		Criteria criteria=getSession().createCriteria(StandardReadingCatalog.class)
				.add(Restrictions.eq("standardReadId", standardReadingId))
				.add(Restrictions.eq("threeLevel", id));
		List<StandardReadingCatalog> list=criteria.list();
		return list;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Pager getArticleList(int pageNo, int pageSize, String qualityId) {
		String hql="FROM StandardReading WHERE status=1 AND checked=1 AND type=0 AND fatherId=0 ";
		//当qualityId没有值或为零时为标准分享
		if(qualityId.equals("0") || qualityId==null || qualityId.equals("")) {
			hql+=" AND classify=1  ORDER BY editTime DESC";
		}else {
			hql+=" AND classify=2 AND qualityId="+Long.valueOf(qualityId)+" ORDER BY editTime DESC";
		}
		Query query=getSession().createQuery(hql);
		List<StandardReading> list=query.list();
		query.setFirstResult((pageNo-1)*pageSize);
		query.setMaxResults(pageSize);
		List<StandardReading> resultList=query.list();
		return new Pager(pageSize,pageNo,list.size(),resultList);
	}


	@SuppressWarnings("unchecked")
	@Override
	public Pager getHisArticleList(int pageNo, int pageSize, Long expertId) {
		String	hql="FROM StandardReading WHERE  status=1 AND ((type=0 AND fatherId=0) OR  (type=1 AND fatherId=0)) AND checked=1 AND author.id=?  order by addTime desc";
		Query query=getSession().createQuery(hql);
		query.setParameter(0,expertId);
		List<StandardReading> list=query.list();
		query.setFirstResult((pageNo-1)*pageSize);
		query.setMaxResults(pageSize);
		List<StandardReading> resultList=query.list();
		return new Pager(pageSize,pageNo,list.size(),resultList);
	}


	@SuppressWarnings("unchecked")
	@Override
	public int getIsAttention(Member puMember, Member member) {
		String hql="FROM Attention WHERE memberId=? AND beMemberId=? AND status=1";
		Query query=getSession().createQuery(hql);
		query.setParameter(0,puMember.getId());
		query.setParameter(1, member.getId());
		List<Attention> list=query.list();
		if(list!=null && list.size()>0) {
			return 1;
		}
		return 0;
	}
}
