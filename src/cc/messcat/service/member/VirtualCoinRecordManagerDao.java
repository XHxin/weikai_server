package cc.messcat.service.member;

import java.util.List;

import cc.messcat.entity.VirtualCoinRecord;
import cc.modules.commons.Pager;

public interface VirtualCoinRecordManagerDao {

	public abstract void addVirtualCoinRecord(VirtualCoinRecord virtualCoinRecord);
	
	public abstract void modifyVirtualCoinRecord(VirtualCoinRecord virtualCoinRecord);
	
	public abstract void removeVirtualCoinRecord(VirtualCoinRecord virtualCoinRecord);
	
	public abstract void removeVirtualCoinRecord(Long id);
	
	public abstract VirtualCoinRecord retrieveVirtualCoinRecord(Long id);
	
	public abstract List retrieveAllVirtualCoinRecords();
	
	public abstract Pager retrieveVirtualCoinRecordsPager(int pageSize, int pageNo);
	
	public abstract Pager findVirtualCoinRecords(int i, int j, String s);
	
	
}