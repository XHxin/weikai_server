package cc.messcat.service.standardread;


import cc.messcat.bases.BaseManagerDaoImpl;
import cc.messcat.entity.StandardMade;

@SuppressWarnings("serial")
public class StandardMadeManagerDaoImpl extends BaseManagerDaoImpl implements StandardMadeManagerDao {

	@Override
	public void commit(StandardMade standardMade) {
		this.standardMadeDao.commit(standardMade);
	}

}
