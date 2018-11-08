package cc.modules.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderSnUtil {

	private static long orderNum = 0l;
	private static long payNum = 0l;
	private static String date;

	/**
	 * 生成实物订单编号
	 * 
	 * @return
	 */
	public static synchronized String generateOrderSn() {
		String str = new SimpleDateFormat("yyMMddHHmmssSSS").format(new Date());
		if (date == null || !str.equals(date)) {//不是同一毫米初始化
			date = str;
			orderNum = 0l; // 同一毫秒生成多少个订单
		}
		orderNum++;
		long orderSn = Long.parseLong((date));
		orderSn += orderNum;
		return "" + orderSn;
	}

	/**
	 * 生成实物订单交易编号
	 * 
	 * @return
	 */
	public static synchronized String generatePaySn() {
		// String str = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new
		// Date());
		String str = new SimpleDateFormat("yyMMddHHmmssSSS").format(new Date());
		if (date == null || !str.equals(date)) {
			date = str;
			payNum = 0l; // 同一毫秒生成多少个支付订单
		}
		orderNum++;
		long paySn = Long.parseLong((date)); // 2015天猫双十一：1秒钟14万订单
		paySn += payNum;
		return "5" + paySn; // 支付编号由数字5开头
	}

	/**
	 * 生成会员等级订单编号
	 * 
	 * @return
	 */
	public static synchronized String generateMemberGradeOrderSn() {
		String str = new SimpleDateFormat("yyMMddHHmmssSSS").format(new Date());
		if (date == null || !str.equals(date)) {
			date = str;
			orderNum = 0l; // 同一毫秒生成多少个订单
		}
		orderNum++;
		long orderSn = Long.parseLong((date));
		orderSn += orderNum;
		return "6" + orderSn;
	}

	/**
	 * 生成会员充值编号
	 * 
	 * @return
	 */
	public static synchronized String generateMemberRechargeSn() {
		String str = new SimpleDateFormat("yyMMddHHmmssSSS").format(new Date());
		if (date == null || !str.equals(date)) {
			date = str;
			orderNum = 0l; // 同一毫秒生成多少个订单
		}
		orderNum++;
		long orderSn = Long.parseLong((date));
		orderSn += orderNum;
		return "7" + orderSn;
	}

	/**
	 * 生成会员提现编号
	 * 
	 * @return
	 */
	public static synchronized String generateMemberPdCashSn() {
		String str = new SimpleDateFormat("yyMMddHHmmssSSS").format(new Date());
		if (date == null || !str.equals(date)) {
			date = str;
			orderNum = 0l; // 同一毫秒生成多少个订单
		}
		orderNum++;
		long orderSn = Long.parseLong((date));
		orderSn += orderNum;
		return "8" + orderSn;
	}

	/**
	 * 生成会员充值交易编号
	 * 
	 * @return
	 */
	public static synchronized String generateMemberRechargePaySn() {
		// String str = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new
		// Date());
		String str = new SimpleDateFormat("yyMMddHHmmssSSS").format(new Date());
		if (date == null || !str.equals(date)) {
			date = str;
			payNum = 0l; // 同一毫秒生成多少个支付订单
		}
		orderNum++;
		long paySn = Long.parseLong((date)); // 2015天猫双十一：1秒钟14万订单
		paySn += payNum;
		return "cz" + paySn; // 支付编号由cz开头
	}
	
	/**
	 * 生成钱包充值交易编号
	 * @return
	 */
	public static synchronized String generateTopUpWalletSn(){
		String str = new SimpleDateFormat("yymmddHHmmssSSS").format(new Date());
		if(date == null || !str.equals(date)){
			date =str;
			payNum = 01;
		}
		orderNum++;
		long paySn = Long.parseLong((date));
		paySn +=payNum;
		return "w" + paySn;
	}

	public static void main(String[] args) {
		System.out.println(Long.MAX_VALUE);
		System.out.println(generateOrderSn());
		System.out.println(Long.MAX_VALUE);
		System.out.println(generatePaySn());
	}
}
