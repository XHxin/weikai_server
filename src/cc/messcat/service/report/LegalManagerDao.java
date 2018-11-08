package cc.messcat.service.report;

import java.util.List;

import cc.messcat.entity.Legal;
import cc.modules.commons.Pager;

public interface LegalManagerDao {

	
	public abstract Legal retrieveLegal(Long id);
	
	public abstract List retrieveAllLegals();
	
	public abstract Pager retrieveLegalsPager(int pageSize, int pageNo);
	
	public abstract Pager findLegals(int i, int j, String s);
	
	/**
	 * 根据地区ID和产品ID查询
	 */
	public List getLegalByCon(Long regionId, Long productId, Long regionFatherId);
	
	
}