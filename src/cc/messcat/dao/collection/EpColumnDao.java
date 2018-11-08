package cc.messcat.dao.collection;

import java.util.List;

import cc.messcat.entity.EnterpriseColumn;

public interface EpColumnDao {

	public EnterpriseColumn get(Long id);
	
	
	public EnterpriseColumn getColumnByFrontNum(String s);

	/*
	 * 根据column的father
	 */
	public List getColumnByFather(Long father);
}