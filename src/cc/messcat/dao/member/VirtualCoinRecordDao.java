package cc.messcat.dao.member;

import java.util.List;

import cc.messcat.bases.BaseDao;
import cc.messcat.entity.VirtualCoinRecord;
import cc.modules.commons.Pager;

public interface VirtualCoinRecordDao extends BaseDao{

	public void save(VirtualCoinRecord virtualCoinRecord);
	
	public void update(VirtualCoinRecord virtualCoinRecord);
	
	public void delete(VirtualCoinRecord virtualCoinRecord);
	
	public void delete(Long id);
	
	public VirtualCoinRecord get(Long id);
	
	public List findAll();
	
	public Pager getPager(int pageSize, int pageNo);
	
	public Pager getObjectListByClass(int i, int j, Class class1, String s);
	
	/**
	 * 根据条件查询
	 */
	public List<VirtualCoinRecord> findByhql(String string);
	
}