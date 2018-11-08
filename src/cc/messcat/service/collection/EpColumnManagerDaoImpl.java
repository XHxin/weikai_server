package cc.messcat.service.collection;


import cc.messcat.bases.BaseManagerDaoImpl;
import cc.messcat.entity.EnterpriseColumn;

@SuppressWarnings("serial")
public class EpColumnManagerDaoImpl extends BaseManagerDaoImpl implements EpColumnManagerDao {

	public EpColumnManagerDaoImpl() {
	}

	public EnterpriseColumn getEnterpriseColumn(Long id) {
		return epColumnDao.get(id);
	}

	
	public EnterpriseColumn getEnterpriseColumn(String frontNum) {
		return epColumnDao.getColumnByFrontNum(frontNum);
	}
}