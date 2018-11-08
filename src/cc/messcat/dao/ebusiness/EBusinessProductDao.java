package cc.messcat.dao.ebusiness;

import java.util.List;

import cc.messcat.bases.BaseDao;
import cc.messcat.entity.EBusinessProduct;
import cc.modules.commons.Pager;

public interface EBusinessProductDao extends BaseDao{

	public void save(EBusinessProduct eproduct);
	
	public void update(EBusinessProduct eproduct);
	
	public void delete(EBusinessProduct eproduct);
	
	public void delete(Long id);
	
	public EBusinessProduct get(Long id);
	
	public List findAll();
	
	public Pager getPager(int pageSize, int pageNo);
	
	public Pager getObjectListByClass(int i, int j, Class class1, String s);
	
	/**
	 * 根据条件查询
	 */
	public List<EBusinessProduct> findByhql(String string);

}