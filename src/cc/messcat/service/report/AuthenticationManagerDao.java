package cc.messcat.service.report;

import java.util.List;

import cc.messcat.entity.Authentication;
import cc.modules.commons.Pager;

public interface AuthenticationManagerDao {

	
	public abstract Authentication retrieveAuthentication(Long id);
	
	public abstract List retrieveAllAuthentications();
	
	public abstract Pager retrieveAuthenticationsPager(int pageSize, int pageNo);
	
	public abstract Pager findAuthentications(int i, int j, String s);
	
	/**
	 * 根据地区ID和产品ID查询
	 */
	public List getAuthenByCon(Long regionId, Long productId, Long regionFatherId);
	
	
}