package cc.messcat.service.report;

import java.util.List;
import cc.modules.commons.Pager;
import cc.modules.util.ObjValid;
import cc.messcat.entity.Authentication;
import cc.messcat.bases.BaseManagerDaoImpl;

public class AuthenticationManagerDaoImpl extends BaseManagerDaoImpl implements AuthenticationManagerDao {

	private static final long serialVersionUID = 1L;


	public AuthenticationManagerDaoImpl() {
	}

	public Authentication retrieveAuthentication(Long id) {
		return (Authentication) this.authenticationDao.get(id);
	}

	public List retrieveAllAuthentications() {
		return this.authenticationDao.findAll();
	}
	
	public Pager retrieveAuthenticationsPager(int pageSize, int pageNo) {
		return this.authenticationDao.getPager(pageSize, pageNo);
	}
	
	public Pager findAuthentications(int pageSize, int pageNo, String statu) {
		Pager pager = authenticationDao.getObjectListByClass(pageSize, pageNo, Authentication.class, statu);
		return pager;
	}
	
	/**
	 * 根据地区ID和产品ID查询
	 */
	public List getAuthenByCon(Long regionId, Long productId, Long regionFatherId){
		StringBuffer sb = new StringBuffer("from Authentication where status = 1 ");
		if(ObjValid.isValid(regionFatherId)){
			sb.append(" and (regionId.id=").append(regionId)
				.append(" or regionId.id=").append(regionFatherId).append(") ");
		}else{
			sb.append(" and regionId.id=").append(regionId).append(" ");
		}
		sb.append(" and (productIds like '%").append(productId).append(";%' or productIds like '%all%')  ");
		List list = authenticationDao.findByhql(sb.toString());
		return list;
	}


}