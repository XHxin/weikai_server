package cc.messcat.dao.collection;

import java.util.List;

import cc.messcat.bases.BaseDaoImpl;
import cc.messcat.entity.EnterpriseColumn;

public class EpColumnDaoImpl extends BaseDaoImpl implements EpColumnDao {

	public EpColumnDaoImpl() {
	}

	public EnterpriseColumn get(Long id) {
		return (EnterpriseColumn) getHibernateTemplate().get(EnterpriseColumn.class, id);
	}
	
	public EnterpriseColumn getColumnByFrontNum(String frontNum) {
		List<?> list = getHibernateTemplate().find("from EnterpriseColumn where frontNum = ?", frontNum);
		EnterpriseColumn obj = null;
		if (list.size() > 0)
			obj = (EnterpriseColumn) list.get(0);
		return obj;
	}
	
	/*
	 * 根据column的father
	 */
	public List getColumnByFather(Long father){
		List<?> list = getHibernateTemplate().find("from EnterpriseColumn where father = ?", father);
		return list;
	}
}