package cc.messcat.dao.member;

import java.util.List;

import cc.messcat.bases.BaseDao;
import cc.messcat.entity.Integral;
import cc.messcat.entity.IntegralExchange;
import cc.messcat.entity.LiveVideoDistributor;
import cc.messcat.entity.Member;
import cc.modules.commons.Pager;

public interface IntegralDao extends BaseDao{

	public void save(Integral integral);
	
	public void update(Integral integral);
	
	public void delete(Integral integral);
	
	public void delete(Long id);
	
	public Integral get(Long id);
	
	public List findAll();
	
	public Pager getPager(int pageSize, int pageNo);
	
	public Pager getPager(int pageSize, int pageNo, Integral integral);

	public Integral findIntegralBymemberId(Long memberId);

	public void updateLiveDistributor(LiveVideoDistributor liveVideoDistributor);

    void addIntegralExchange(IntegralExchange integralExchange);
}