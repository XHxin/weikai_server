package cc.messcat.service.pay;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cc.messcat.entity.ExpenseTotal;
import cc.messcat.entity.HotProblem;
import cc.messcat.entity.HotReply;
import cc.messcat.entity.Member;
import cc.messcat.vo.IOSVerifyVo;
import cc.modules.security.ExceptionManager;

public interface PayManagerDao {

	/**
	 * 生成支付宝支付参数
	 */
	public Object addRechargeOrder(Member member, BigDecimal changeMoney) throws ExceptionManager, UnsupportedEncodingException;

	/**
	 * 支付宝回调接口业务处理
	 */
	public void addHandlerOrder(Map<String, String> params, String result) throws ExceptionManager;

	/**
	 * 微信生成预订单
	 */
	public Object addWechatAppOrder(Member member, BigDecimal changeMoney, HttpServletRequest request) throws ExceptionManager, Exception;

	/**
	 * 微信回调接口
	 */
	public void addNotifyResult(HttpServletRequest request, HttpServletResponse response) throws Exception;

	/**
	 *  支付宝
	 */
/*	public Object addAliPayOrder(Member member, ExpenseTotal br)
			throws ExceptionManager, UnsupportedEncodingException;*/
	
	/**
	 * 苹果内购订单
	 */
/*	Object addBuysRecordRechargeOrderFromApple(Member member,Long regionId,Long productId)
		throws ExceptionManager, UnsupportedEncodingException;
	
	Object addBuysRecordRechargeOrderFromApple(Member member,Long standardReadId)
		throws ExceptionManager, UnsupportedEncodingException;
	
	Object addEBusinessProductBuysRecordRechargeOrderFromApple(Member member,Long eBusinessProductId)
		throws ExceptionManager, UnsupportedEncodingException;
	*/
	
	Object updateBuysRecordRechargeOrderFromApple(Member member,IOSVerifyVo iosVerifyVo,HttpServletRequest request,HttpServletResponse response)
		throws ExceptionManager, UnsupportedEncodingException, IOException;

//	public Object addWechatOrder(Member member, ExpenseTotal br, HttpServletRequest request) throws Exception;

	/**
	 * 虚拟币统一支付接口处理
	 */
//	public Object addVirtualCoinPay(ExpenseTotal record, Member member);

	/**
	 * 钱包统一支付接口处理
	 */
	public Object addWalletCoinPay(ExpenseTotal record, Member member);

	/**
	 * @param memberId  打赏人的钱包
	 * @param beRewardId  被打赏人的钱包
	 * @param rewardMoney
	 */
	public Object addIncomeByReward(Long memberId,Long beRewardId,Double rewardMoney);

	Object addIncomeByRewardV2(Long memberId,Long rewardVideoId,Double rewardMoney);

	public void addIncomeByPayNew(Integer distribute, Member member, ExpenseTotal expenseTotal);

	ExpenseTotal addExpenseTotal(ExpenseTotal recordNew);

	/**
	 * 视频分销写流水
	 * @param distribute
	 * @param expenseTotalId
	 */
    Object distributeExpense(Integer distribute, Long expenseTotalId);

	/**
	 *  付费咨询提问，不包括围观问题(微信)
	 * @param member
	 * @param record
	 * @param request
	 * @return
	 */
/*	Object addWechatPayConsult(Member member, ExpenseTotal record, HttpServletRequest request) throws Exception;
	*//**
	 *  付费咨询提问，不包括围观问题(支付宝)
	 * @param member
	 * @return
	 *//*
	Object addAliPayPayConsult(Member member, ExpenseTotal record)  throws Exception;*/

	Object addWalletPayConsult(ExpenseTotal expenseTotal, Member member, Long[] coupnId);

	ExpenseTotal getPayConsultExpenseTotal(HotProblem hotProblem);

	/**
	 * 给专家增加收入
	 * @param expenseTotal
	 * @param member
	 */
//    void addExpertWallet(ExpenseTotal expenseTotal, Member member,Member questioner);


}
