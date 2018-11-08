package cc.messcat.service.report;

import java.util.List;
import cc.messcat.entity.Legal;
import cc.messcat.entity.Member;
import cc.messcat.entity.Product;
import cc.messcat.vo.ProductListVo;
import cc.messcat.vo.ProductVo;
import cc.modules.commons.Pager;

public interface ProductManagerDao {

	
	public abstract Product retrieveProduct(Long id);
	
	public abstract List retrieveAllProducts();
	
	public abstract Pager retrieveProductsPager(int pageSize, int pageNo);
	
	public abstract Pager findProducts(int i, int j, String s);
	
	/**
	 * 根据产品名查询产品
	 */
	public List getProductByName(String name);
	
	ProductListVo getProductByName(Integer pageSize, Integer pageNo, String name,Long regionId,Member member);
    
	//获取市场准入报告的详情
	public abstract ProductVo getMarketReportDetail(Long regionId, Long memberId, Long productId);

	public abstract Legal getMarketReportItem(Long regionId, Long productId,Long regionFatherId);
	
}