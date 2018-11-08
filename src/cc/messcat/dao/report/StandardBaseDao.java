package cc.messcat.dao.report;

import java.util.List;

import cc.messcat.bases.BaseDao;
import cc.messcat.entity.StandardBase;
import cc.modules.commons.Pager;

public interface StandardBaseDao extends BaseDao{

	public StandardBase get(Long id);
	
	public List findAll();
	
	public Pager getPager(int pageSize, int pageNo);
	
	public Pager getObjectListByClass(int i, int j, Class class1, String s);
	
	/**
	 * 根据条件查询
	 */
	public List<StandardBase> findByhql(String string);
	
}