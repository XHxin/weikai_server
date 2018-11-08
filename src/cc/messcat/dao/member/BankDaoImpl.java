package cc.messcat.dao.member;

import java.util.List;

import cc.messcat.entity.BankMember;
import cc.modules.util.CollectionUtil;
import org.apache.tools.ant.types.resources.Restrict;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import cc.modules.commons.Pager;
import cc.messcat.bases.BaseDaoImpl;
import cc.messcat.entity.Bank;

public class BankDaoImpl extends BaseDaoImpl implements BankDao {

	public BankDaoImpl() {
	}
	
	public void save(Bank bank) {
		getHibernateTemplate().save(bank);
	}
	
	public void update(Bank bank) {
		getHibernateTemplate().update(bank);
	}
	
	public void delete(Bank bank) {
		bank.setStatus("0");
		getHibernateTemplate().update(bank);
	}

	public void delete(Long id) {
		Bank bank = this.get(id);
		bank.setStatus("0");
		getHibernateTemplate().update(bank);
	}
	
	public Bank get(Long id) {
		return (Bank) getHibernateTemplate().get(Bank.class, id);
	}

	public List findAll() {
		return getHibernateTemplate().find("from Bank where status = 1 order by editTime desc");
	}

	public Pager getPager(int pageSize, int pageNo) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(Bank.class);
		criteria.add(Restrictions.eq("status","1"));
		criteria.addOrder(Order.desc("editTime"));
		int rowCount = ((Integer) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		criteria.setProjection(null);
		int startIndex = pageSize * (pageNo - 1);
		criteria.setFirstResult(startIndex);
		criteria.setMaxResults(pageSize);
		List result = criteria.list();
		return new Pager(pageSize, pageNo, rowCount, result);
	}
	
	/**
	 * 根据条件查询
	 */
	public List<Bank> findByhql(String string) {
		return this.getHibernateTemplate().find(string);
	}

	@Override
	public BankMember findBankMember(Long bankId) {
		return (BankMember) getSession().get(BankMember.class,bankId);
	}

	@Override
	public BankMember addBankMember(BankMember bm) {
		getSession().save(bm);
		String hql="FROM BankMember WHERE openBank LIKE '"+bm.getOpenBank()+"' AND cardHolder LIKE '"+bm.getCardHolder()+"' AND bankCard LIKE '"+bm.getBankCard()+"' AND bankMobile LIKE '"+bm.getBankMobile()+"'";
		List<BankMember> list=getSession().createQuery(hql).list();
		if(CollectionUtil.isListNotEmpty(list)){
			return list.get(0);
		}
		return null;
	}
}