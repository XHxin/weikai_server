package cc.messcat.dao.report;

import java.util.List;

import cc.messcat.bases.BaseDao;
import cc.messcat.entity.Authentication;
import cc.modules.commons.Pager;

public interface AuthenticationDao extends BaseDao{

	public Authentication get(Long id);
	
	public List findAll();
	
	public Pager getPager(int pageSize, int pageNo);
	
	public Pager getObjectListByClass(int i, int j, Class class1, String s);
	
	/**
	 * 根据条件查询
	 */
	public List<Authentication> findByhql(String string);
	
}