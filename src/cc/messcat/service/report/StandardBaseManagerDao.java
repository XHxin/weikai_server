package cc.messcat.service.report;

import java.util.List;

import cc.messcat.entity.StandardBase;
import cc.modules.commons.Pager;

public interface StandardBaseManagerDao {

	
	public abstract StandardBase retrieveStandardBase(Long id);
	
	public abstract List retrieveAllStandardBases();
	
	public abstract Pager retrieveStandardBasesPager(int pageSize, int pageNo);
	
	public abstract Pager findStandardBases(int i, int j, String s);
	
	
}