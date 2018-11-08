package cc.messcat.service.member;

import java.util.List;

import cc.messcat.entity.BankMember;
import cc.modules.commons.Pager;
import cc.messcat.entity.Bank;
import cc.messcat.bases.BaseManagerDaoImpl;

public class BankManagerDaoImpl extends BaseManagerDaoImpl implements BankManagerDao {

	public BankManagerDaoImpl() {
	}

	public void addBank(Bank packages) {
		this.bankDao.save(packages);
	}
	
	public void modifyBank(Bank packages) {
		this.bankDao.update(packages);
	}
	
	public void removeBank(Bank packages) {
		this.bankDao.delete(packages);
	}

	public void removeBank(Long id) {
		this.bankDao.delete(id);
	}
	
	public Bank retrieveBank(Long id) {
		return (Bank) this.bankDao.get(id);
	}

	@SuppressWarnings("unchecked")
	public List retrieveAllBanks() {
		return this.bankDao.findAll();
	}
	
	public Pager retrieveBanksPager(int pageSize, int pageNo) {
		return this.bankDao.getPager(pageSize, pageNo);
	}
	
	public Pager findBanks(int pageSize, int pageNo, String statu) {
		Pager pager = bankDao.getObjectListByClass(pageSize, pageNo, Bank.class, statu);
		return pager;
	}

	@Override
	public BankMember findBankMember(Long bankId) {
		return bankDao.findBankMember(bankId);
	}

	@Override
	public void updateBankMember(BankMember bankMember) {
		bankDao.update(bankMember);
	}

	@Override
	public BankMember addBankMember(BankMember bm) {
		return bankDao.addBankMember(bm);
	}
}