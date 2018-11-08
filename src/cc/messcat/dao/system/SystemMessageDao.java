package cc.messcat.dao.system;

import java.util.List;

import cc.messcat.bases.BaseDao;
import cc.messcat.entity.Member;
import cc.messcat.entity.SystemMessage;
import cc.modules.commons.Pager;

public interface SystemMessageDao extends BaseDao{

	public void save(SystemMessage systemMessage);
	
	public void update(SystemMessage systemMessage);
	
	public void delete(SystemMessage systemMessage);
	
	public void delete(Long id);
	
	public SystemMessage get(Long id);
	
	public List findAll();
	
	public Pager getPager(int pageSize, int pageNo);
	
	public Pager getObjectListByClass(int i, int j, Class class1, String s);
	
	/**
	 * 根据条件查询
	 */
	public List<SystemMessage> findByhql(String string);

	public Member getMemberById(Long memberId);
	
	//根据条件查询 分页
	public List<SystemMessage> findByhql(String hql,int pageSize,int pageNo);

}