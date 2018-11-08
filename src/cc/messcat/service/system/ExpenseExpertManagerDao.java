package cc.messcat.service.system;

import java.util.List;

import cc.messcat.entity.ExpenseExpert;
import cc.messcat.vo.ExpertIEListVo;
import cc.modules.commons.Pager;

public interface ExpenseExpertManagerDao {

	public abstract void addExpenseExpert(ExpenseExpert expertIE);
	
	public abstract void modifyExpertIE(ExpenseExpert expertIE);
	
	public abstract void removeExpertIE(ExpenseExpert expertIE);
	
	public abstract void removeExpertIE(Long id);
	
	public abstract ExpenseExpert retrieveExpertIE(Long id);
	
	public abstract List retrieveAllExpertIEs();
	
	public abstract Pager retrieveExpertIEsPager(int pageSize, int pageNo);
	
	public abstract Pager findExpertIEs(int i, int j, String s);
	
	/*
	 * 根据memberId查询
	 */
	public ExpertIEListVo getExpertIEByCon(Long memberId, int pageNo, int pageSize);
	
	
}