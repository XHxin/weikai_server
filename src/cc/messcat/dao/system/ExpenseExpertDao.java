package cc.messcat.dao.system;

import java.util.List;

import cc.messcat.bases.BaseDao;
import cc.messcat.entity.ExpenseExpert;
import cc.modules.commons.Pager;

public interface ExpenseExpertDao extends BaseDao{

	public void save(ExpenseExpert expertIE);
	
	public void update(ExpenseExpert expertIE);
	
	public void delete(ExpenseExpert expertIE);

	public void delete(Long id);

	public ExpenseExpert get(Long id);
	
	public List findAll();
	
	public Pager getPager(int pageSize, int pageNo);
	
	public Pager getObjectListByClass(int i, int j, Class class1, String s);
	
	/**
	 * 根据条件查询
	 */
	public List<ExpenseExpert> findByhql(String string);
	
}