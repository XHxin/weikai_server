package cc.messcat.dao.member;

import java.util.List;

import cc.messcat.bases.BaseDao;
import cc.messcat.entity.Bank;
import cc.messcat.entity.BankMember;
import cc.modules.commons.Pager;

public interface BankDao extends BaseDao{

	public void save(Bank bank);
	
	public void update(Bank bank);
	
	public void delete(Bank bank);
	
	public void delete(Long id);
	
	public Bank get(Long id);
	
	public List findAll();
	
	public Pager getPager(int pageSize, int pageNo);
	
	public Pager getObjectListByClass(int i, int j, Class class1, String s);
	
	/**
	 * 根据条件查询
	 */
	public List<Bank> findByhql(String string);

    BankMember findBankMember(Long bankId);

	BankMember addBankMember(BankMember bm);
}