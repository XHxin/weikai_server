package cc.messcat.service.report;

import java.util.List;
import cc.modules.commons.Pager;
import cc.messcat.entity.StandardBase;
import cc.messcat.bases.BaseManagerDaoImpl;

public class StandardBaseManagerDaoImpl extends BaseManagerDaoImpl implements StandardBaseManagerDao {

	private static final long serialVersionUID = 1L;

	public StandardBaseManagerDaoImpl() {
	}
	
	public StandardBase retrieveStandardBase(Long id) {
		return (StandardBase) this.standardBaseDao.get(id);
	}

	public List retrieveAllStandardBases() {
		return this.standardBaseDao.findAll();
	}
	
	public Pager retrieveStandardBasesPager(int pageSize, int pageNo) {
		return this.standardBaseDao.getPager(pageSize, pageNo);
	}
	
	public Pager findStandardBases(int pageSize, int pageNo, String statu) {
		Pager pager = standardBaseDao.getObjectListByClass(pageSize, pageNo, StandardBase.class, statu);
		return pager;
	}


}