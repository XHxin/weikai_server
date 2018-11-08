package cc.messcat.service.ebusiness;

import java.util.List;

import cc.messcat.entity.EBusinessProduct;
import cc.modules.commons.Pager;

public interface EBusinessProductManagerDao {

	public abstract void addEProduct(EBusinessProduct eproduct);
	
	public abstract void modifyEProduct(EBusinessProduct eproduct);
	
	public abstract void removeEProduct(EBusinessProduct eproduct);
	
	public abstract void removeEProduct(Long id);
	
	public abstract EBusinessProduct retrieveEProduct(Long id);
	
	public abstract List retrieveAllEProducts();
	
	public abstract Pager retrieveEProductsPager(int pageSize, int pageNo);
	
	public abstract Pager findEProducts(int i, int j, String s);
	
	/**
	 * 根据产品名查询产品
	 */
	public List getEProductByName(String name);
	
	
}