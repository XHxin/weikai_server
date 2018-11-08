package cc.messcat.dao.payconsult;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import cc.messcat.bases.BaseDaoImpl;
import cc.messcat.entity.ExpertClassify;
import cc.messcat.entity.Member;
import cc.modules.commons.Pager;

public class ExpertClassifyDaoImpl extends BaseDaoImpl implements ExpertClassifyDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<ExpertClassify> getList() {
		Criteria criteria = getSession().createCriteria(ExpertClassify.class);
		criteria.add(Restrictions.eq("isShowIndex", "1"));
		criteria.addOrder(Order.asc("sort"));
		List<ExpertClassify> list = criteria.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Pager getAllList(int pageNo, int pageSize) {
		Criteria criteria=getSession().createCriteria(ExpertClassify.class);
		criteria.add(Restrictions.eq("status", "1"));
		criteria.addOrder(Order.asc("sort"));
		//criteria.add(Restrictions.eq("isShowIndex", "0"));   二期部署的时候说要显示全部
		int rowCount=(int) criteria.setProjection(Projections.rowCount()).uniqueResult();
		criteria.setProjection(null);
//		criteria.setFirstResult((pageNo-1)*pageSize);            //现在只有22个分类, 暂时不用分页
//		criteria.setMaxResults(pageSize);
		List<ExpertClassify> list=criteria.list();
		return new Pager(pageSize,pageNo,rowCount,list);
	}
	
	/*
	 * 通过Member与Classify的多对多关联关系找到专家选择的列表
	 */
	@Override
	public Member getMember(Long expertId) {
		return (Member) getSession().get(Member.class, expertId);
	}
	
	/*
	 * 找出全部的专家分类
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ExpertClassify> getAllClassify() {
		Criteria criteria=getSession().createCriteria(ExpertClassify.class);
		criteria.add(Restrictions.eq("status", "1"));
		criteria.addOrder(Order.asc("sort"));
		List<ExpertClassify> list=criteria.list();
		return list;
	}

	@Override
	public ExpertClassify getExpertClassify(String strId) {
		return (ExpertClassify) getSession().get(ExpertClassify.class,Long.valueOf(strId));
	}

	@Override
	public void updateMembers(Member member) {
        getSession().update(member);		
	}
    

}
