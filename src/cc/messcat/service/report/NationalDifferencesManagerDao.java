package cc.messcat.service.report;

import java.util.List;

import cc.messcat.entity.NationalDifferences;
import cc.modules.commons.Pager;

public interface NationalDifferencesManagerDao {

	
	public abstract NationalDifferences retrieveNationalDifferences(Long id);
	
	public abstract List retrieveAllNationalDifferencess();
	
	public abstract Pager retrieveNationalDifferencessPager(int pageSize, int pageNo);
	
	public abstract Pager findNationalDifferencess(int i, int j, String s);
	
	/**
	 * 根据地区ID和产品ID查询
	 */
	public NationalDifferences getDifferentByCon(Long regionId);
	
	
}