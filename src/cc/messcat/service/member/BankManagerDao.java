package cc.messcat.service.member;

import java.util.List;

import cc.messcat.entity.Bank;
import cc.messcat.entity.BankMember;
import cc.modules.commons.Pager;

public interface BankManagerDao {

	public abstract void addBank(Bank bank);
	
	public abstract void modifyBank(Bank bank);
	
	public abstract void removeBank(Bank bank);
	
	public abstract void removeBank(Long id);
	
	public abstract Bank retrieveBank(Long id);
	
	public abstract List retrieveAllBanks();
	
	public abstract Pager retrieveBanksPager(int pageSize, int pageNo);
	
	public abstract Pager findBanks(int i, int j, String s);


    BankMember findBankMember(Long bankId);

	void updateBankMember(BankMember bankMember);

	BankMember addBankMember(BankMember bm);
}