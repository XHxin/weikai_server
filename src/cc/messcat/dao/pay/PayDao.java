package cc.messcat.dao.pay;

import cc.messcat.entity.*;
import cc.modules.security.ExceptionManager;
import org.springframework.beans.factory.parsing.Problem;

public interface PayDao {

	/**
	 * 查询充值订单
	 * 
	 * @author Geoff
	 * @param pn
	 *            充值订单号
	 * @return
	 * @throws ExceptionManager
	 * @date 2016年11月27日
	 */
	public BuysRecord findRechargeOrderByPn(String pn) throws ExceptionManager;
	
	
	/**
	 * 建了ExpenseTotal表之后的查询订单方法
	 * @param pn
	 * @return
	 * @throws ExceptionManager
	 */
	public ExpenseTotal findRechargeOrderByPnNew(String pn) throws ExceptionManager;

    ExpenseTotal addExpenseTotal(ExpenseTotal expenseTotal);

    void addExpenseMember(ExpenseMember expenseMember);

    void addExpensePlatform(ExpensePlatform expensePlatform);

    ExpenseTotal getPayConsultExpenseTotal(HotProblem hotProblem);

    void addExpenseExpert(ExpenseExpert expenseExpert);
}
