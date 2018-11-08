package cc.messcat.bases;

import java.util.List;
import java.util.Map;

public interface BaseDao {

	/**
	 * 添加对象
	 */
	public void save(Object obj);

	/**
	 * 修改对象
	 */
	public void update(Object obj);

	/**
	 * 根据ID删除对象
	 */
	public void delete(Long id, String objName);

	/**
	 * 根据ID查找对象
	 */
	public Object get(Long id, String objName);

	/**
	 * 查找所有对象
	 */
	@SuppressWarnings("unchecked")
	public List getAll(String objName);
	
	/**
	 * 根据entityClass及attrs属性查询单个对象
	 * 
	 * @param entityClass
	 * @param attrs
	 * @return
	 */
	public <T> T query(Class<T> entityClass, Map<String, Object> attrs);
}
