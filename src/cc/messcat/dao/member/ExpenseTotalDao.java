package cc.messcat.dao.member;

import java.util.List;
import cc.messcat.bases.BaseDao;
import cc.messcat.entity.ExpenseTotal;
import cc.modules.commons.Pager;

/**
 * 
 * @author nelson
 *
 */
public interface ExpenseTotalDao extends BaseDao{

	public void update(ExpenseTotal expenseTotal);
	
	public void save(ExpenseTotal expenseTotal);
	
	Pager findBuysRecordsByCondiction(Long memberId, String type, int pageNo, int pageSize);
	
	public ExpenseTotal getReplyBuys(Long memberId, Long expertId, Long problemId);
	/**
	 * 根据条件查询购买
	 */
	public List<ExpenseTotal> getBuyRecordByCon(ExpenseTotal record);
	
	public ExpenseTotal getBuys(Long memberId, Long readId, String type);
	
	public ExpenseTotal getEbusinessBuys(Long memberId, Long ebusinessProductId, String type);
    
	public ExpenseTotal getProblemRecord(Long expertId, Long problemId);

	public int findAllBuysByMemberId(Long memberId);

	/**
	 * 根据关联id查询购买记录
	 * @param relateId
	 * @return
	 */
	public List<ExpenseTotal> findAllBuysByLiveVideoId(Long[] relateId);

	public ExpenseTotal getBuyStatus(Long memberId, Long relateId, String type);

	public List<ExpenseTotal> findExpenseTotalByCondiction(Long memberId, String type, int pageNo, int pageSize);

	public ExpenseTotal checkAriticleBuyStatus(Long memberId, Long ariticle);

	public List<ExpenseTotal> getBuysRecordByTypeAndRelatedId(String type, Long relateId);

	public ExpenseTotal getBuysRecordByOrderNum(String merchantOrderNum);

    List<ExpenseTotal> getOneDayExpenseTotal(int year, int month, int day, Integer payType);

	/**
	 * 更新version，返回被更新的记录条数
	 * @param order
	 * @return
	 */
	int updateVersion(ExpenseTotal order);
}
