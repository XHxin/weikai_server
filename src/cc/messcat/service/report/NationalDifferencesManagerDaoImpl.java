package cc.messcat.service.report;

import java.util.List;
import cc.modules.commons.Pager;
import cc.modules.util.ObjValid;
import cc.messcat.entity.NationalDifferences;
import cc.messcat.bases.BaseManagerDaoImpl;

public class NationalDifferencesManagerDaoImpl extends BaseManagerDaoImpl implements NationalDifferencesManagerDao {

	private static final long serialVersionUID = 1L;


	public NationalDifferencesManagerDaoImpl() {
	}

	
	public NationalDifferences retrieveNationalDifferences(Long id) {
		return (NationalDifferences) this.nationalDifferencesDao.get(id);
	}

	public List retrieveAllNationalDifferencess() {
		return this.nationalDifferencesDao.findAll();
	}
	
	public Pager retrieveNationalDifferencessPager(int pageSize, int pageNo) {
		return this.nationalDifferencesDao.getPager(pageSize, pageNo);
	}
	
	public Pager findNationalDifferencess(int pageSize, int pageNo, String statu) {
		Pager pager = nationalDifferencesDao.getObjectListByClass(pageSize, pageNo, NationalDifferences.class, statu);
		return pager;
	}
	
	/**
	 * 根据地区ID和产品ID查询
	 */
	public NationalDifferences getDifferentByCon(Long regionId){
		StringBuffer sb = new StringBuffer("from NationalDifferences where status = 1 ");
		sb.append(" and regionId.id=").append(regionId).append(" ");
		List list = nationalDifferencesDao.findByhql(sb.toString());
		if(ObjValid.isValid(list)){
			return (NationalDifferences) list.get(0);
		}
		return null;
	}


}