package cc.messcat.service.member;

import java.util.List;

import cc.messcat.entity.IntegralExchange;
import cc.modules.commons.Pager;
import cc.messcat.entity.Integral;
import cc.messcat.entity.LiveVideoDistributor;
import cc.messcat.bases.BaseManagerDaoImpl;

public class IntegralManagerDaoImpl extends BaseManagerDaoImpl implements IntegralManagerDao {
	
	private static final long serialVersionUID = 1L;

	
	@Override
	public void addIntegral(Integral integral) {
		this.integralDao.save(integral);
	}

	@Override
	public void removeIntegral(Integral integral) {
		this.integralDao.delete(integral);
	}

	@Override
	public void removeIntegral(Long id) {
		this.integralDao.delete(id);
	}

	@Override
	public Integral retrieveIntegral(Long id) {
		return integralDao.get(id);
	}

	@Override
	public List retrieveAllIntegrals() {
		 
		return null;
	}

	@Override
	public Pager retrieveIntegralsPager(int pageSize, int pageNo) {
		return integralDao.getPager(pageSize, pageNo);
	}

	@Override
	public Pager findIntegrals(int i, int j, String s) {
		return null;
	}

	@Override
	public Pager findIntegralsByCon(int i, int j, Integral integral) {
		return null;
	}

	@Override
	public void updateLiveDistributor(LiveVideoDistributor liveVideoDistributor) {
		integralDao.updateLiveDistributor(liveVideoDistributor);
	}

	/**
	 * 积分兑换钱包余额记录
	 */
	@Override
	public void addIntegralExchange(IntegralExchange integralExchange) {
		integralDao.addIntegralExchange(integralExchange);
	}

}