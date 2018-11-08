package cc.messcat.dao.report;

import java.util.List;

import cc.messcat.bases.BaseDao;
import cc.messcat.entity.Region;
import cc.modules.commons.Pager;

public interface RegionDao extends BaseDao{

	public Region get(Long id);
	
	public List findAll();
	
	public Pager getPager(int pageSize, int pageNo);
	
	public Pager getObjectListByClass(int i, int j, Class class1, String s);
	
	/**
	 * 根据条件查询
	 */
	public List<Region> findByhql(String string);
	
	/**
	 * 查询最大地区ID
	 */
	public Long findMaxRegionID();
	
}