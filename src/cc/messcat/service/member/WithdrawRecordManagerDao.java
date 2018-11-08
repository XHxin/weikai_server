package cc.messcat.service.member;

import java.util.List;

import cc.messcat.entity.WithdrawRecord;
import cc.modules.commons.Pager;

public interface WithdrawRecordManagerDao {

	public abstract void addWithdrawRecord(WithdrawRecord withdrawRecord);
	
	public abstract void modifyWithdrawRecord(WithdrawRecord withdrawRecord);
	
	public abstract void removeWithdrawRecord(WithdrawRecord withdrawRecord);
	
	public abstract void removeWithdrawRecord(Long id);
	
	public abstract WithdrawRecord retrieveWithdrawRecord(Long id);
	
	public abstract List retrieveAllWithdrawRecords();
	
	public abstract Pager retrieveWithdrawRecordsPager(int pageSize, int pageNo);
	
	public abstract Pager findWithdrawRecords(int i, int j, String s);
	
	
}