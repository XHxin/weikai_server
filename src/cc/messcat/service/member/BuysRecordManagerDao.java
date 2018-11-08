package cc.messcat.service.member;

import java.util.List;

import cc.messcat.entity.BuysRecord;
import cc.messcat.entity.ExpenseTotal;
import cc.messcat.vo.MyBuysListResult;
import cc.modules.commons.Pager;

public interface BuysRecordManagerDao {

	public abstract void addBuysRecord(BuysRecord record);
	
	public abstract void modifyBuysRecord(BuysRecord record);
	
	public abstract void removeBuysRecord(BuysRecord record);
	
	public abstract void removeBuysRecord(Long id);
	
	public abstract BuysRecord retrieveBuysRecord(Long id);
	
	public abstract List retrieveAllBuysRecords();
	
	public abstract Pager retrieveBuysRecordsPager(int pageSize, int pageNo);
	
	public abstract Pager findBuysRecords(int i, int j, String s);
	
	/**
	 * 根据地区ID和产品ID查询(用于产品输入模糊查询时)
	 */
	public BuysRecord getBuysRecordByConSimple(Long regionId, Long productId, Long memberId);
	
	/**
	 * 根据会员ID查询
	 */
	public List getBuysRecordByCon(Long memberId, String type);
	
	/**
	 * 根据条件查询购买
	 * （修改了购买记录表）
	 */
	public List<ExpenseTotal> getBuyRecordByCon(ExpenseTotal record);
	
	/**
	 * 根据订单号查询
	 */
	public BuysRecord getBuysRecordByNumber(String number);

	public abstract MyBuysListResult findBuysRecordsByCondiction(Long memberId, String type, int pageNo, int pageSize);
    
	public abstract BuysRecord getProblemRecord(Long expertId, Long problemId);

	public abstract void updateProblemRecord(BuysRecord buyRecord);

	/**
	 * 统计某个会员的购买总数
	 * @param memberId
	 * @return
	 */
	public abstract int findAllBuysByMemberId(Long memberId);

}