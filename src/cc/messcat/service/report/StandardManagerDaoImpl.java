package cc.messcat.service.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import cc.modules.commons.Pager;
import cc.modules.util.ObjValid;
import cc.messcat.entity.Standard;
import cc.messcat.entity.StandardBase;
import cc.messcat.bases.BaseManagerDaoImpl;

public class StandardManagerDaoImpl extends BaseManagerDaoImpl implements StandardManagerDao {

	private static final long serialVersionUID = 1L;


	public StandardManagerDaoImpl() {
	}

	
	public Standard retrieveStandard(Long id) {
		return (Standard) this.standardDao.get(id);
	}

	public List retrieveAllStandards() {
		return this.standardDao.findAll();
	}
	
	public Pager retrieveStandardsPager(int pageSize, int pageNo) {
		return this.standardDao.getPager(pageSize, pageNo);
	}
	
	public Pager findStandards(int pageSize, int pageNo, String statu) {
		Pager pager = standardDao.getObjectListByClass(pageSize, pageNo, Standard.class, statu);
		return pager;
	}
	
	/**
	 * 根据地区ID和产品ID查询
	 */
	public Standard getStandardByCon(Long regionId, Long productId){
		StringBuffer sb = new StringBuffer("from Standard where status = 1 ");
		sb.append(" and regionId.id=").append(regionId).append(" ");
		sb.append(" and productId.id=").append(productId).append(" ");
		List<Standard> list = standardDao.findByhql(sb.toString());
		Map<String,List<String>> map = new HashMap<>();
		
		List<StandardBase> bases = new ArrayList<>();
		if(ObjValid.isValid(list)){
			Standard sd = list.get(0);
				String ids = sd.getStandardIds();
				if(ids != null){
					String[] adIDs = ids.split(";");
					for(String adId: adIDs){
						StandardBase base = standardBaseDao.get(Long.valueOf(adId.trim()));
						if(ObjValid.isValid(map.get(base.getType()))){
							List<String> codes = map.get(base.getType());
							codes.add(base.getCode());
							map.put(base.getType(), codes);
						}else{
							List<String> codes = new ArrayList<>();
							codes.add(base.getCode());
							map.put(base.getType(), codes);
						}
						//bases.add(base);
					}
					sd.setMap(map);
				}
				return sd;
		}
		return null;
	}
	
	/**
	 * 根据地区ID和产品ID查询(用于产品输入模糊查询时)
	 */
	public Standard getStandardByConSimple(Long regionId, Long productId){
		StringBuffer sb = new StringBuffer("from Standard where status = 1 ");
		sb.append(" and regionId.id=").append(regionId).append(" ");
		sb.append(" and productId.id=").append(productId).append(" ");
		List<Standard> list = standardDao.findByhql(sb.toString());
		if(ObjValid.isValid(list)){
			return list.get(0);
		}
		return null;
	}


}