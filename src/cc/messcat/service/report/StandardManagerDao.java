package cc.messcat.service.report;

import java.util.List;
import cc.messcat.entity.Standard;
import cc.modules.commons.Pager;

public interface StandardManagerDao {

	
	public abstract Standard retrieveStandard(Long id);
	
	public abstract List retrieveAllStandards();
	
	public abstract Pager retrieveStandardsPager(int pageSize, int pageNo);
	
	public abstract Pager findStandards(int i, int j, String s);
	
	/**
	 * 根据地区ID和产品ID查询
	 */
	public Standard getStandardByCon(Long regionId, Long productId);
	
	/**
	 * 根据地区ID和产品ID查询(用于产品输入模糊查询时)
	 */
	public Standard getStandardByConSimple(Long regionId, Long productId);
	
	
}