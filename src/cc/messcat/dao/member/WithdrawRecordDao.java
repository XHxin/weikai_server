package cc.messcat.dao.member;

import java.util.List;
import cc.messcat.bases.BaseDao;
import cc.messcat.entity.WithdrawRecord;
import cc.modules.commons.Pager;

public interface WithdrawRecordDao extends BaseDao{

	public void save(WithdrawRecord withdrawRecord);
	
	public void update(WithdrawRecord withdrawRecord);
	
	public void delete(WithdrawRecord withdrawRecord);
	
	public void delete(Long id);
	
	public WithdrawRecord get(Long id);
	
	public List findAll();
	
	public Pager getPager(int pageSize, int pageNo);
	
	public Pager getObjectListByClass(int i, int j, Class class1, String s);
	
	/**
	 * 根据条件查询
	 */
	public List<WithdrawRecord> findByhql(String string);
	
}