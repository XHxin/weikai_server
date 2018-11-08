package cc.messcat.service.report;

import java.util.List;
import cc.messcat.entity.Region;
import cc.modules.commons.Pager;

public interface RegionManagerDao {

	
	public abstract Region retrieveRegion(Long id);
	
	public abstract List retrieveAllRegions();
	
	public abstract Pager retrieveRegionsPager(int pageSize, int pageNo);
	
	public abstract Pager findRegions(int i, int j, String s);
	
	/**
	 * 根据地区名精确查找
	 */
	public Region getByName(String name);
	
	List<Region> getRegionByName(String name);
	
	/**
	 * 查找最大地区ID
	 * @return
	 */
	public abstract Long findMaxRegionID();
	
	
}