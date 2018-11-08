package cc.messcat.dao.system;

import java.util.List;

import cc.messcat.bases.BaseDao;
import cc.messcat.entity.ExpensePlatform;
import cc.modules.commons.Pager;

public interface ExpensePlatDao extends BaseDao{

	public void save(ExpensePlatform platformIncome);
	
	public void update(ExpensePlatform platformIncome);
	
	public void delete(ExpensePlatform platformIncome);
	
	public void delete(Long id);
	
	public ExpensePlatform get(Long id);
	
	public List findAll();
	
	public Pager getPager(int pageSize, int pageNo);
	
	public Pager getPager(int pageSize, int pageNo, ExpensePlatform platformIncome);
	
	public Pager getObjectListByClass(int i, int j, Class class1, String s);
	public List<ExpensePlatform> findByhql(String string);
	public List<ExpensePlatform> getBuysRecordList(String type, Long memberId, int pageNo, int pageSize);

	public Pager findPlatformIncomeByCondition(int pageNo, int pageSize, ExpensePlatform platformIncome);

	/**
	 * 根据购买记录id查找平台流水（看这条购买记录是否已经做过流水处理）
	 * @param id
	 */
	ExpensePlatform getExpensePlatformByExpenseTotalId(Long id);
}