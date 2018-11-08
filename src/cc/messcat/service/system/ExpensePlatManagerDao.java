package cc.messcat.service.system;

import java.util.List;

import cc.messcat.entity.Member;
import cc.messcat.entity.ExpensePlatform;
import cc.modules.commons.Pager;

public interface ExpensePlatManagerDao {

	public abstract void addPlatformIncome(ExpensePlatform platformIncome);
	public abstract void removePlatformIncome(ExpensePlatform platformIncome);
	public abstract void removePlatformIncome(Long id);
	public abstract ExpensePlatform retrievePlatformIncome(Long id);
	public abstract List retrieveAllPlatformIncomes();
	public abstract Pager retrievePlatformIncomesPager(int pageSize, int pageNo);
	public abstract Pager findPlatformIncomes(int i, int j, String s);
	public abstract Pager findPlatformIncomesByCon(int i, int j, Member member);
	public ExpensePlatform findPlatformIncomeByUsername(String username);
	public abstract Pager findPlatformIncomeByCondition(int pageNo, int pageSize, ExpensePlatform platformIncome);
}