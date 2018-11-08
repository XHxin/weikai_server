package cc.messcat.service.member;

import java.util.List;

import cc.messcat.entity.ExpenseTotal;
import cc.messcat.vo.ExpenseTotalVo;
import cc.messcat.vo.MyBuysListResult;

public interface ExpenseTotalManagerDao {

	/**
	 * 综合已购列表
	 * @param memberId
	 * @param type
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public MyBuysListResult findBuysRecordsByCondiction(Long memberId, String type, int pageNo, int pageSize);
	//根据专家和问题的ID，查找记录
	public ExpenseTotal getProblemRecord(Long expertId, Long problemId);
	//把交易记录状态更改为退款
	public void updateProblemRecord(ExpenseTotal buyRecord);
	public void save(ExpenseTotal recordNew);
	/**
	 * 根据memberId以及商品id，查看用户是否购买过此商品
	 * @param memberId
	 * @param relateId
	 * @return
	 */
	public ExpenseTotal checkBuyStatus(Long memberId, Long relateId);
	
	/**
	 * 统计某个会员的购买总数
	 * @param memberId
	 * @return
	 */
	public abstract int findAllBuysByMemberId(Long memberId);
	/**
	 * 根据关联id查询购买记录
	 * @param relateId
	 * @return
	 */
	public List<ExpenseTotal> findAllBuysByLiveVideoId(Long[] relateId);
	/**
	 * @param memberId
	 * @param type  0:综合 1:准入报告 2:标准解读（质量分享） 4:付费咨询 5:新闻详情 6:电商准入报告 8:视频或直播
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public List<ExpenseTotalVo> findExpenseTotalByCondiction(Long memberId, String type, int pageNo, int pageSize);
	
	//根据type类型和关联的id查询有多少个不同的会员
	public List<Long> getRecord(String string, Long id);
	public List<ExpenseTotal> getBuysRecordByTypeAndRelatedId(String type, Long relateId);

	/**
	 * 根据订单号查找购买记录
	 * @param merchantOrderNum
	 */
	public ExpenseTotal getBuysRecordByOrderNum(String merchantOrderNum);
	
	/**
	 * 更新购买记录
	 * @param expenseTotal
	 */
	public void updateExpenseTotal(ExpenseTotal expenseTotal);

	/**
	 * 查询具体某一天的所有购买记录
	 *
	 * @param payType
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
    List<ExpenseTotal> getOneDayExpenseTotal(int year, int month, int day, Integer payType);
}
