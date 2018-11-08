package cc.messcat.service.report;

import java.util.List;
import cc.modules.commons.Pager;
import cc.modules.util.ObjValid;
import cc.messcat.entity.Legal;
import cc.messcat.bases.BaseManagerDaoImpl;

public class LegalManagerDaoImpl extends BaseManagerDaoImpl implements LegalManagerDao {

	private static final long serialVersionUID = 1L;


	public LegalManagerDaoImpl() {
	}

	
	public Legal retrieveLegal(Long id) {
		return (Legal) this.legalDao.get(id);
	}

	public List retrieveAllLegals() {
		return this.legalDao.findAll();
	}
	
	public Pager retrieveLegalsPager(int pageSize, int pageNo) {
		return this.legalDao.getPager(pageSize, pageNo);
	}
	
	public Pager findLegals(int pageSize, int pageNo, String statu) {
		Pager pager = legalDao.getObjectListByClass(pageSize, pageNo, Legal.class, statu);
		return pager;
	}
	
	/**
	 * 根据地区ID和产品ID查询
	 */
	public List getLegalByCon(Long regionId, Long productId, Long regionFatherId){
		StringBuffer sb = new StringBuffer("from Legal where status = 1 ");
		if(ObjValid.isValid(regionFatherId)){
			sb.append(" and (regionId.id=").append(regionId)
				.append(" or regionId.id=").append(regionFatherId).append(") ");
		}else{
			sb.append(" and regionId.id=").append(regionId).append(" ");
		}
		sb.append(" and (productIds like '%").append(productId).append(";%' or productIds like '%all%') ");
		
		List list = legalDao.findByhql(sb.toString());
		return list;
	}


}