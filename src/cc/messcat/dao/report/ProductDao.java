package cc.messcat.dao.report;

import java.util.List;

import cc.messcat.bases.BaseDao;
import cc.messcat.entity.Legal;
import cc.messcat.entity.Product;
import cc.modules.commons.Pager;
public interface ProductDao extends BaseDao{

	public Product get(Long id);
	
	public List findAll();
	
	public Pager getPager(int pageSize, int pageNo);
	
	public Pager getObjectListByClass(int i, int j, Class class1, String s);
	
	/**
	 * 根据条件查询
	 */
	public List<Product> findByhql(String string);
    //获取分享URL
	public String getShareURL();

	public Legal getMarketReportItem(Long regionId, Long productId, Long regionFatherId);
	
}