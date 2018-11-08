package cc.messcat.dao.member;

import java.util.List;

import cc.messcat.bases.BaseDao;
import cc.messcat.entity.Packages;
import cc.modules.commons.Pager;

public interface PackagesDao extends BaseDao{

	public void save(Packages packages);
	
	public void update(Packages packages);
	
	public void delete(Packages packages);
	
	public void delete(Long id);
	
	public Packages get(Long id);
	
	public List findAll();
	
	public Pager getPager(int pageSize, int pageNo);
	
	public Pager getObjectListByClass(int i, int j, Class class1, String s);
	
	/**
	 * 根据条件查询
	 */
	public List<Packages> findByhql(String string);
	
}