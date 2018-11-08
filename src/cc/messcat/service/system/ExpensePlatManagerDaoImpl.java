package cc.messcat.service.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import cc.modules.commons.Pager;
import cc.messcat.entity.Member;
import cc.messcat.entity.ExpensePlatform;
import cc.messcat.bases.BaseManagerDaoImpl;

public class ExpensePlatManagerDaoImpl extends BaseManagerDaoImpl implements ExpensePlatManagerDao {

	private static final long serialVersionUID = 1L;

	public ExpensePlatManagerDaoImpl() {
	}

	public void addPlatformIncome(ExpensePlatform platformIncome) {
		this.expensePlatDao.save(platformIncome);
	}
	public void removePlatformIncome(ExpensePlatform platformIncome) {
		this.expensePlatDao.delete(platformIncome);
	}
	public void removePlatformIncome(Long id) {
		this.expensePlatDao.delete(id);
	}
	public ExpensePlatform retrievePlatformIncome(Long id) {
		return (ExpensePlatform) this.expensePlatDao.get(id);
	}

	public List retrieveAllPlatformIncomes() {
		return this.expensePlatDao.findAll();
	}
	
	public Pager retrievePlatformIncomesPager(int pageSize, int pageNo) {
		return this.expensePlatDao.getPager(pageSize, pageNo);
	}
	
	public Pager findMembersByCon(int pageSize, int pageNo, ExpensePlatform platformIncome){
		Pager pager = expensePlatDao.getPager(pageSize, pageNo, platformIncome);
		return pager;
	}
	
	/**
	 * 查询用户根据微信号
	 */
	public ExpensePlatform retrievePlatformIncomeByOpenId(ExpensePlatform platformIncome) {
		Map<String, Object> attrs = new HashMap<String, Object>();
		attrs.put("status", "1");
		return expensePlatDao.query(ExpensePlatform.class, attrs);
	}
	
	/**
	 * 根据mobile查询member
	 */
	public ExpensePlatform findPlatformIncomeByUsername(String username){
		List<ExpensePlatform> list = this.expensePlatDao.findByhql("from PlatformIncome where mobile='" + username + "'");
		if(list.size()==0){
			return null;
		}
		return list.get(0);
	}

	@Override
	public Pager findPlatformIncomes(int i, int j, String s) {
		return null;
	}

	@Override
	public Pager findPlatformIncomesByCon(int i, int j, Member member) {
		return null;
	}

	@Override
	public Pager findPlatformIncomeByCondition(int pageNo, int pageSize, ExpensePlatform platformIncome) {
		return expensePlatDao.findPlatformIncomeByCondition(  pageNo,   pageSize,   platformIncome);
	}
	
 

}