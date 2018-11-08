package cc.messcat.dao.member;

import java.util.List;
import cc.modules.commons.Pager;
import cc.messcat.entity.VirtualCoinRecord;
import cc.messcat.bases.BaseManagerDaoImpl;

public class VirtualCoinRecordManagerDaoImpl extends BaseManagerDaoImpl implements VirtualCoinRecordManagerDao {

	private static final long serialVersionUID = 1L;


	public VirtualCoinRecordManagerDaoImpl() {
	}

	public void addVirtualCoinRecord(VirtualCoinRecord virtualCoinRecord) {
		this.virtualCoinRecordDao.save(virtualCoinRecord);
	}
	
	public void modifyVirtualCoinRecord(VirtualCoinRecord virtualCoinRecord) {
		this.virtualCoinRecordDao.update(virtualCoinRecord);
	}
	
	public void removeVirtualCoinRecord(VirtualCoinRecord virtualCoinRecord) {
		this.virtualCoinRecordDao.delete(virtualCoinRecord);
	}

	public void removeVirtualCoinRecord(Long id) {
		this.virtualCoinRecordDao.delete(id);
	}
	
	public VirtualCoinRecord retrieveVirtualCoinRecord(Long id) {
		return (VirtualCoinRecord) this.virtualCoinRecordDao.get(id);
	}

	public List retrieveAllVirtualCoinRecords() {
		return this.virtualCoinRecordDao.findAll();
	}
	
	public Pager retrieveVirtualCoinRecordsPager(int pageSize, int pageNo) {
		return this.virtualCoinRecordDao.getPager(pageSize, pageNo);
	}
	
	public Pager findVirtualCoinRecords(int pageSize, int pageNo, String statu) {
		Pager pager = virtualCoinRecordDao.getObjectListByClass(pageSize, pageNo, VirtualCoinRecord.class, statu);
		return pager;
	}


}