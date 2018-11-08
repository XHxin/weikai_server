package cc.messcat.service.member;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cc.messcat.entity.*;
import cc.messcat.vo.IOSVerifyVo;
import cc.messcat.vo.MemberVo;
import cc.messcat.vo.MyBuysListResult;
import cc.messcat.vo.RedeemCodeVo;
import cc.messcat.vo.ResponseBean;
import cc.modules.commons.Pager;
import cc.modules.security.ExceptionManager;

public interface MemberManagerDao {

	public abstract void addMember(Member member);
	
	public abstract void modifyMember(Member member);
	
	/*
	 * 注册
	 */
	public abstract MemberVo addAppMember(Member member, HttpServletRequest request);
	
	/*
	 * 修改会员信息
	 */
	public abstract MemberVo updateMember(Member member);
	
	public abstract void removeMember(Member member);
	
	public abstract void removeMember(Long id);
	
	public abstract Member retrieveMember(Long id);
	
	void addNotifyResult(HttpServletRequest request, HttpServletResponse response) throws Exception;
	
	ResponseBean addWeChatVip(Member member, Long packagesId, HttpServletRequest request) throws Exception;
	
	ResponseBean addAppleVip(Member member, Long packagesId) throws Exception;
	
	Object updateBuysRecordFromApple(Member member, IOSVerifyVo iosVerifyVo, HttpServletRequest request, HttpServletResponse response)
		throws ExceptionManager, UnsupportedEncodingException, IOException;
	
	/**
	 * 支付宝充值续费
	 * @param member
	 * @param packagesId
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	ResponseBean addVip(Member member, Long packagesId) throws UnsupportedEncodingException;
	
	void addHandlerOrder(Map<String, String> params, String result) throws ExceptionManager;
	
	public abstract List retrieveAllMembers();
	
	public abstract Pager retrieveMembersPager(int pageSize, int pageNo);
	
	public abstract Pager findMembers(int i, int j, String s);
	
	public abstract Pager findMembersByCon(int i, int j, Member member);
	
	/**
	 * @描述 查询用户根据微信号
	 */
	public Member retrieveMemberByOpenId(Member member);
	
	/**
	 * 根据mobile查询member
	 */
	public Member findMemberByMobile(String mobile);
	
	/**
	 * 手机存在校验
	 */
	public boolean isExistMobile(String mobile);
	
	/**
	 * 手机号码唯一性校验
	 */
	public boolean isMobileUnique(String mobile, String orgMobile);
	
	/**
	 * 手机号码唯一性校验（微信）
	 */
	public boolean isMobileUnique3(String openid, String mobile, String orgMobile);
	
	/**
	 *  邮箱唯一性校验
	 */
	public boolean isEmailUnique(String email, String orgEmail);
	
	/**
	 * 校验验证码
	 */
	public boolean verifyMobileCode(String business, String mobile, String mobileCode, HttpSession session) throws ExceptionManager;
	
	/**
	 * 登录
	 */
	public MemberVo updateAppMember(Member member, HttpServletRequest request);

	/**
	 * 我的购买列表
	 *
	 */
	public abstract MyBuysListResult getBuysRecordList(String type, Long memberId, int pageNo, int pageSize);
	
	//
	public abstract Member retrieveMember(String mobile);
	
	/**
	 * 获取推荐人的推荐次数
	 */
	public Member getRecommend_times(Long recommender_id);

	public abstract Object haveRecommendId(Long id);

	public abstract MemAuth getMemberAuth(MemAuth memAuth);
	
	/*
	 * 第三方登录和 绑定手机用到的方法
	 */
	public abstract void updateLoginThirdRecord(MemAuth memAuth, MemAuth memAuth2, HttpServletRequest request);
	public abstract void addMemAuth(MemAuth memAuth, Long memberId, HttpServletRequest request);
	public abstract Member saveMember(Member member2);
	public abstract MemAuth getMemberAuth(Long memberId, MemAuth memAuth);
    //给刚刚注册的付费咨询专家添加一条初始收费记录
	public abstract void insertReplyFees(Long expertId);

	public abstract HotReplyFees getReplyFees(Long expertId);
    //当退款的时候,更新用户的虚拟币帐户
	public abstract void updateProMember(Member entity);
    //通过会员ID找到一条会员授权记录
	public abstract MemAuth getMemberAuth(Member member1);
	
	/**
	 * 此方法用于支付宝充值个人钱包
	 * @param member 充值的对象
	 * @param topUpMoney 充值的金额
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public ResponseBean alipayTopUp(Member member, BigDecimal topUpMoney) throws UnsupportedEncodingException;
	
	/**
	 * 此方法用于微信充值个人钱包
	 * @param member 充值de对象
	 * @param topUpMoney 充值的金额
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public ResponseBean wechatTopUp(Member member, BigDecimal topUpMoney, HttpServletRequest request) throws Exception;

	public void topUpHanlder(Map<String, String> params, String string);

	/**
	 * 微信充值钱包回调逻辑处理
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void addWalletNotifyResult(HttpServletRequest request, HttpServletResponse response)throws Exception;

	public Object addAppleWallet(Member member, BigDecimal topUpMoney) throws Exception;

	/*public Object updateWalletBuysRecordFromApple(Member member, IOSVerifyVo iosVerifyVo,
                                                  HttpServletRequest request, HttpServletResponse response) throws IOException,ExceptionManager, UnsupportedEncodingException;*/
	
	public Member getUUID(String uuid);

	public abstract Member getMobileById(Long memberId);

	public abstract List<CoupnUser> getUserCoupnByMemberIdAndType(String type, Long memberId, int pageNo, int pageSize, int videoId);

	/**
	 * 返回用户所有可用或不可用的卡券
	 * @param memberId
	 * @param used
	 * @return
	 */
	public abstract List<CoupnUser> getAllUserCoupn(Long memberId, String used);

	public abstract List<CoupnUser> getCoupnsById(Long[] coupnId);

	public abstract void updateUserCoupn(CoupnUser coupnUser);

	public abstract void updateCoupn(Coupn coupnId);

	/**根据memberId返回该用户所有卡券
	 * @param memberId
	 * @param pageSize 
	 * @param pageNo 
	 * @return
	 */
	public abstract List<CoupnUser> getAllUserCoupn(Long memberId, int pageNo, int pageSize);

	public abstract List<Coupn> getCoupnById(Long[] coupnId);

	public abstract void save(CoupnUser uCoupn);

	/**
	 * 查看该商品是否已购
	 * @param member
	 * @param stype
	 * @param relateId
	 * @return
	 */
	public abstract ExpenseTotal checkBuyStatus(Member member, String stype, Long[] relateId);

	public abstract int memberNum();

	public abstract void save(Object object);

	public abstract List<WechatWeiboAttention> retrieveAttention();

	/**
	 * @param interfaces
	 * @return
	 * 查询某个接口的调用频次
	 */
	public abstract Fusing fusingTimes(String interfaces);

	public abstract void updateFusing(Fusing fusing);

	/**
	 * @param typeNum
	 * @return
	 * 查询1微信/2微博目前表里的最新的关注数。如果取到的数据为0，则取前一条不为0的数据
	 */
	public abstract int maxAttentionNum(int typeNum);

	public abstract RedeemCodeVo exchangeCode(String redeemCode, Member member);

	/**
	 * @param videoId
	 * @return
	 * 根据视频id查找该视频对应的卡券
	 */
	public abstract Coupn coupnByVideoId(int videoId);

	public abstract int isExistCode(String redeemCode, Member member);

	/**
	 * 根据memberId集合，查找member
	 * @param memberIds
	 * @return
	 */
	public abstract List<Member> getMembersByMemberIds(List<Long> memberIds);

	public abstract void updateCouponExpire();
    
	/**
	 * 卡券到期提醒：【到期前3天发送】
	 */
	public abstract void couponExpireRemind();
	
	/**
	 * 会员到期提醒：【到期前3天发送】
	 */
	public abstract void memberExpireRemind();
	/**
	 * 开课提醒：【开课前30分钟发送】
	 */
	public abstract void classesToRemind();

	public abstract Coupn saveCoupn(Coupn coupn);

	public abstract void saveMemberMp(MemberMp mp);

	public abstract MemberMp findMemberMpByMemberId(Long id);

	public abstract void updateMemberMp(MemberMp memberMp);

	public abstract MemberMp findMemberMpByOpenId(String openId);

    BankMember saveBankMember(BankMember bankMember);

    MemberMp findMemberMpByOpenIdAndMemberId(Long id, String openId);

	/**
	 * 添加积分
	 * @param memberId
	 * @param expenseTotal
	 */
	void addIntegral(Long memberId, ExpenseTotal expenseTotal);

	/**
	 * 检查用户的会员的到期时间，更新状态
	 * @param member
	 * @return
	 */
    Member checkVipInfo(Member member);

	/**
	 * 获取app显示的会员到期时间
	 * @param member
	 * @param dateFormat 现在“我的”页面返回的时间格式是需要有时分秒的，点击续费进入的页面返回的到期时间的格式是没有时分秒的，所以暂时加个type区分返回的格式，两个版本后，统一不返回时分秒,1-没有时分秒，2-有时分秒
	 * @return
	 */
	String getVipEndTime(Member member,Integer dateFormat);

	/**
	 * 获取分销表的积分
	 * @param memberId
	 * @return
	 */
    BigDecimal getDistributorIntegral(Long memberId);

	/**
	 * 消费分销所获得的积分
	 * @param memberId 消费者
	 * @param spendIntegral 需要消费的积分
	 */
	void subtractDistributorIntegral(Long memberId, BigDecimal spendIntegral);
}