package cc.messcat.dao.ebusiness;

import java.util.List;

import cc.messcat.bases.BaseDao;
import cc.messcat.entity.EBusinessInfo;
import cc.messcat.entity.EBusinessProduct;
import cc.modules.commons.Pager;

public interface EBusinessInfoDao extends BaseDao{

	public void save(EBusinessInfo info);
	
	public void update(EBusinessInfo info);
	
	public void delete(EBusinessInfo info);
	
	public void delete(Long id);
	
	public EBusinessInfo get(Long id);
	
	public List findAll();
	
	public Pager getPager(int pageSize, int pageNo);
	
	public Pager getObjectListByClass(int i, int j, Class class1, String s);
	
	/**
	 * 根据条件查询
	 */
	public List<EBusinessInfo> findByhql(String string);

	public boolean getCollect(Long relatedID, Long memberId);

	public boolean getBuys(Long productID, Long memberId);

	public EBusinessProduct getProduct(Long subEbusinessProductId);

	public String getShareURL();
	
}