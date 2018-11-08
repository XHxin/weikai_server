package cc.messcat.dao.member;

import java.util.List;

import cc.messcat.entity.IntegralExchange;
import cc.modules.commons.Pager;
import cc.messcat.bases.BaseDaoImpl;
import cc.messcat.entity.Integral;
import cc.messcat.entity.LiveVideoDistributor;
import cc.messcat.entity.Member;

public class IntegralDaoImpl extends BaseDaoImpl implements IntegralDao {

	
	@Override
	public void save(Integral integral) {
		getHibernateTemplate().save(integral);
	}

	@Override
	public void update(Integral integral) {
		getHibernateTemplate().update(integral);
	}

	@Override
	public void delete(Integral integral) {
		integral.setStatus("0");
		getHibernateTemplate().update(integral);
		
	}

	@Override
	public void delete(Long id) {
		Integral integral = this.get(id);
		integral.setStatus("0");
		getHibernateTemplate().update(integral);
	}

	@Override
	public Integral get(Long id) {
		return (Integral) getHibernateTemplate().get(Integral.class, id);
	}

	@Override
	public List findAll() {
		return null;
	}

	@Override
	public Pager getPager(int pageSize, int pageNo) {
		return null;
	}

	@Override
	public Pager getPager(int pageSize, int pageNo, Integral integral) {
		return null;
	}

	@Override
	public Integral findIntegralBymemberId( Long memberId) {
		List<Integral> list = this.getHibernateTemplate().find("from Integral integral where integral.member.id=  " +   memberId + "  ");
		if(list.size()==0){
			return null;
		}
		return list.get(0);
	}

	@Override
	public void updateLiveDistributor(LiveVideoDistributor liveVideoDistributor) {
		getSession().update(liveVideoDistributor);
	}

	/**
	 * 积分兑换钱包余额记录
	 */
	@Override
	public void addIntegralExchange(IntegralExchange integralExchange) {
		getSession().save(integralExchange);
	}

}