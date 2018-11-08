package cc.messcat.service.member;

import java.util.List;
import cc.modules.commons.Pager;
import cc.messcat.entity.WithdrawRecord;
import cc.messcat.bases.BaseManagerDaoImpl;

public class WithdrawRecordManagerDaoImpl extends BaseManagerDaoImpl implements WithdrawRecordManagerDao {

	private static final long serialVersionUID = 1L;


	public WithdrawRecordManagerDaoImpl() {
	}

	public void addWithdrawRecord(WithdrawRecord withdrawRecord) {
		this.withdrawRecordDao.save(withdrawRecord);
	}
	
	public void modifyWithdrawRecord(WithdrawRecord withdrawRecord) {
		this.withdrawRecordDao.update(withdrawRecord);
	}
	
	public void removeWithdrawRecord(WithdrawRecord withdrawRecord) {
		this.withdrawRecordDao.delete(withdrawRecord);
	}

	public void removeWithdrawRecord(Long id) {
		this.withdrawRecordDao.delete(id);
	}
	
	public WithdrawRecord retrieveWithdrawRecord(Long id) {
		return (WithdrawRecord) this.withdrawRecordDao.get(id);
	}

	public List retrieveAllWithdrawRecords() {
		return this.withdrawRecordDao.findAll();
	}
	
	public Pager retrieveWithdrawRecordsPager(int pageSize, int pageNo) {
		return this.withdrawRecordDao.getPager(pageSize, pageNo);
	}
	
	public Pager findWithdrawRecords(int pageSize, int pageNo, String statu) {
		Pager pager = withdrawRecordDao.getObjectListByClass(pageSize, pageNo, WithdrawRecord.class, statu);
		return pager;
	}


}