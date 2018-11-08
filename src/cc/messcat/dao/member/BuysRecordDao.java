package cc.messcat.dao.member;

import java.util.List;

import cc.messcat.bases.BaseDao;
import cc.messcat.entity.BuysRecord;
import cc.messcat.entity.ExpenseTotal;
import cc.modules.commons.Pager;

public interface BuysRecordDao extends BaseDao{

	public void save(BuysRecord record);
	
	public void update(BuysRecord record);
	
	public void delete(BuysRecord record);
	
	public void delete(Long id);
	
	public BuysRecord get(Long id);
	
	public List findAll();
	
	public Pager getPager(int pageSize, int pageNo);
	
	public Pager getObjectListByClass(int i, int j, Class class1, String s);
	
	/**
	 * 根据条件查询
	 */
	public List<BuysRecord> findByhql(String string);
	
	/**
	 * 根据条件查询购买
	 * （修改了购买记录表）
	 */
	public List<ExpenseTotal> getBuyRecordByCon(ExpenseTotal record);

	Pager findBuysRecordsByCondiction(Long memberId, String type, int pageNo, int pageSize);

	public BuysRecord getBuys(Long memberId, Long readId, String type);

	public BuysRecord getEbusinessBuys(Long memberId, Long ebusinessProductId, String type);

	public BuysRecord getReplyBuys(Long memberId,Long expertId, Long problemId);

	public BuysRecord getProblemRecord(Long expertId, Long problemId);
    //删除无效的订单
	public void deleteRecord(BuysRecord buys);
}