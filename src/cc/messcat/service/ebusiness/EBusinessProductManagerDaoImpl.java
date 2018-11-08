package cc.messcat.service.ebusiness;

import java.util.List;
import cc.modules.commons.Pager;
import cc.messcat.entity.EBusinessProduct;
import cc.messcat.bases.BaseManagerDaoImpl;

public class EBusinessProductManagerDaoImpl extends BaseManagerDaoImpl implements EBusinessProductManagerDao {

	private static final long serialVersionUID = 1L;


	public EBusinessProductManagerDaoImpl() {
	}

	public void addEProduct(EBusinessProduct eproduct) {
		this.businessProductDao.save(eproduct);
	}
	
	public void modifyEProduct(EBusinessProduct eproduct) {
		this.businessProductDao.update(eproduct);
	}
	
	public void removeEProduct(EBusinessProduct eproduct) {
		this.businessProductDao.delete(eproduct);
	}

	public void removeEProduct(Long id) {
		this.businessProductDao.delete(id);
	}
	
	public EBusinessProduct retrieveEProduct(Long id) {
		return (EBusinessProduct) this.businessProductDao.get(id);
	}

	public List retrieveAllEProducts() {
		return this.businessProductDao.findAll();
	}
	
	public Pager retrieveEProductsPager(int pageSize, int pageNo) {
		return this.businessProductDao.getPager(pageSize, pageNo);
	}
	
	public Pager findEProducts(int pageSize, int pageNo, String statu) {
		Pager pager = businessProductDao.getObjectListByClass(pageSize, pageNo, EBusinessProduct.class, statu);
		return pager;
	}
	
	/**
	 * 根据产品名查询产品
	 */
	public List getEProductByName(String name){
		StringBuffer sb = new StringBuffer("from EBusinessProduct where status = 1 ");
		sb.append(" and name like '%").append(name.trim()).append("%' ");
		List list = businessProductDao.findByhql(sb.toString());
		return list;
	}


}