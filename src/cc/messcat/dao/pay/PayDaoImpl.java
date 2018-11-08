package cc.messcat.dao.pay;

import java.util.List;

import cc.messcat.entity.*;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import cc.messcat.bases.BaseDaoImpl;
import cc.modules.security.ExceptionManager;
import cc.modules.util.CollectionUtil;

public class PayDaoImpl extends BaseDaoImpl implements PayDao {

	@SuppressWarnings("unchecked")
	@Override
	public BuysRecord findRechargeOrderByPn(String pn) throws ExceptionManager {
		Session session = super.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(BuysRecord.class);
		criteria.add(Restrictions.eq("number", pn));
		List<BuysRecord> list = criteria.list();
		if (CollectionUtil.isListEmpty(list)) {
			return null;
		}
		return list.get(0);
	}

	@Override
	public ExpenseTotal findRechargeOrderByPnNew(String pn) throws ExceptionManager {
		Session session = super.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ExpenseTotal.class);
		criteria.add(Restrictions.eq("number", pn));
		List<ExpenseTotal> list = criteria.list();
		if (CollectionUtil.isListEmpty(list)) {
			return null;
		}
		return list.get(0);
	}

	@Override
	public ExpenseTotal addExpenseTotal(ExpenseTotal expenseTotal) {
		Session session=getSession();
		session.save(expenseTotal);
		String hql="FROM ExpenseTotal WHERE type=? AND memberId=? AND relatedId=? ORDER BY payTime DESC ";
		Query query=session.createQuery(hql);
		query.setParameter(0, expenseTotal.getType());
		query.setParameter(1, expenseTotal.getMemberId());
		query.setParameter(2, expenseTotal.getRelatedId());
		query.setFirstResult(0); //取一条数据
		query.setMaxResults(1);
		List<ExpenseTotal> list=query.list();
		if(CollectionUtil.isListNotEmpty(list)){
			return list.get(0);
		}
		return null;
	}

	@Override
	public void addExpenseMember(ExpenseMember expenseMember) {
		getSession().save(expenseMember);
	}

	@Override
	public void addExpensePlatform(ExpensePlatform expensePlatform) {
		getSession().save(expensePlatform);
	}

	@Override
	public ExpenseTotal getPayConsultExpenseTotal(HotProblem hotProblem) {
		List<ExpenseTotal> list=getSession().createCriteria(ExpenseTotal.class)
				.add(Restrictions.eq("type","4"))
				.add(Restrictions.eq("memberId",hotProblem.getMemberId()))
				.add(Restrictions.eq("expertId",hotProblem.getExpertId()))
				.add(Restrictions.eq("payStatus","1"))
				.add(Restrictions.eq("relatedId",hotProblem.getId()))
				.list();
		if(CollectionUtil.isListNotEmpty(list)){
			return list.get(0);
		}
		return null;
	}

	@Override
	public void addExpenseExpert(ExpenseExpert expenseExpert) {
		getSession().save(expenseExpert);
	}
}
