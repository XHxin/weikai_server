package cc.messcat.service.member;

import java.util.List;

import cc.messcat.entity.Integral;
import cc.messcat.entity.IntegralExchange;
import cc.messcat.entity.LiveVideoDistributor;
import cc.modules.commons.Pager;

public interface IntegralManagerDao {

	public   void addIntegral(Integral integral);
 
	public   void removeIntegral(Integral integral);
	
	public   void removeIntegral(Long id);
	
	public   Integral retrieveIntegral(Long id);
	
	public   List retrieveAllIntegrals();
	
	public   Pager retrieveIntegralsPager(int pageSize, int pageNo);
	
	public   Pager findIntegrals(int i, int j, String s);
	
	public   Pager findIntegralsByCon(int i, int j, Integral integral);

	public void updateLiveDistributor(LiveVideoDistributor liveVideoDistributor);

    void addIntegralExchange(IntegralExchange integralExchange);
}