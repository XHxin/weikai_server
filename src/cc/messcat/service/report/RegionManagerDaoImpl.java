package cc.messcat.service.report;

import java.util.List;
import cc.modules.commons.Pager;
import cc.modules.util.ObjValid;
import cc.messcat.bases.BaseManagerDaoImpl;
import cc.messcat.entity.Region;

public class RegionManagerDaoImpl extends BaseManagerDaoImpl implements RegionManagerDao {
	
	private static final long serialVersionUID = 1L;
	public RegionManagerDaoImpl() {
	}
	public Region retrieveRegion(Long id) {
		return (Region) this.regionDao.get(id);
	}

	public List retrieveAllRegions() {
		return this.regionDao.findAll();
	}
	
	public Pager retrieveRegionsPager(int pageSize, int pageNo) {
		return this.regionDao.getPager(pageSize, pageNo);
	}
	
	public Pager findRegions(int pageSize, int pageNo, String statu) {
		Pager pager = regionDao.getObjectListByClass(pageSize, pageNo, Region.class, statu);
		return pager;
	}
	
	/**
	 * 根据地区名精确查找
	 */
	public Region getByName(String name) {
		StringBuffer sb = new StringBuffer("from Region where status = 1 ");
		if(null!=name&&!"".equals(name.trim())){
			sb.append(" and name = '").append(name.trim()).append("' ");
		}
		List list = regionDao.findByhql(sb.toString());
		if(ObjValid.isValid(list)){
			return (Region) list.get(0);
		}
		return null;
	}
	
	
	/**
	 * 查询最大地区ID
	 */
	public Long findMaxRegionID(){
		return regionDao.findMaxRegionID();
	}


	@Override
	public List<Region> getRegionByName(String name) {
		StringBuffer sb = new StringBuffer("from Region where status = 1 ");
		if(null!=name&&!"".equals(name.trim())){
			sb.append(" and name like '%").append(name.trim()).append("%'");
		}
		List list = regionDao.findByhql(sb.toString());
		return list;
	}
	


}