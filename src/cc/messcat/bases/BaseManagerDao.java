package cc.messcat.bases;

import java.util.List;

public interface BaseManagerDao {

	/**
	 * 添加对象
	 */
	public void saveObject(Object obj);

	/**
	 * 修改对象
	 */
	public void updateObject(Object obj);

	/**
	 * 根据ID删除对象
	 */
	public void deleteObject(Long id, String objName);

	/**
	 * 根据ID查找对象
	 */
	public Object getObjectById(Long id, String objName);

	/**
	 * 查找所有对象
	 */
	@SuppressWarnings("unchecked")
	public List getAllObjects(String objName);
}
