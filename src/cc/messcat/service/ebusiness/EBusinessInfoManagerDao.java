package cc.messcat.service.ebusiness;

import java.util.List;
import java.util.Map;

import cc.messcat.entity.EBusinessInfo;
import cc.messcat.entity.Member;
import cc.messcat.vo.EBusinessInfoVo;
import cc.messcat.vo.EBusinessInfoVo2;
import cc.modules.commons.Pager;

public interface EBusinessInfoManagerDao {

	public abstract void addEBusinessInfo(EBusinessInfo info);
	
	public abstract void modifyEBusinessInfo(EBusinessInfo info);
	
	public abstract void removeEBusinessInfo(EBusinessInfo info);
	
	public abstract void removeEBusinessInfo(Long id);
	
	public abstract EBusinessInfo retrieveEBusinessInfo(Long id);
	
	@SuppressWarnings("rawtypes")
	public abstract List retrieveAllEBusinessInfos();
	
	public abstract Pager retrieveEBusinessInfosPager(int pageSize, int pageNo);
	
	public abstract Pager findEBusinessInfos(int i, int j, String s);
	
	/**
	 * 根据产品名查询产品
	 */
	@SuppressWarnings("rawtypes")
	public List getEBusinessInfoByName(String name);
	
	Pager getEBusinessInfoByEProduct(String name, int pageNo, int pageSize);
	
	/**
	 * 根据产品id查产品
	 */
	public EBusinessInfoVo2 getEBusiness(Long id, Member member);

}