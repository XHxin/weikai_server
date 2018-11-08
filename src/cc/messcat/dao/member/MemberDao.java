package cc.messcat.dao.member;

import cc.messcat.bases.BaseDao;
import cc.messcat.entity.*;
import cc.modules.commons.Pager;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

public interface MemberDao extends BaseDao{

	public void save(Member member);
	
	public void update(Member member);
	
	public void delete(Member member);
	
	public void delete(Long id);
	
	public Member get(Long id);
	
	public List findAll();
	
	public Pager getPager(int pageSize, int pageNo);
	
	public Pager getPager(int pageSize, int pageNo, Member member);
	
	public Pager getObjectListByClass(int i, int j, Class class1, String s);
	
	/**
	 * 根据条件查询
	 */
	public List<Member> findByhql(String string);
    /**
     *  我的购买记录
     */
	public List<BuysRecord> getBuysRecordList(String type, Long memberId, int pageNo, int pageSize);

	public StandardReading getStandard(Long memberId);
	public DivideScaleCommon getDivideScaleCommon();
	//
	
	public Member retrieveMember(String mobile);

	public Member getRecommend_times(Long recommender_id);

	public Object existRecommendId(Long id);

	public MemAuth getMemberAuth(MemAuth memAuth);

	/**
	 * 第三方登录和 绑定手机用到的方法
	 */
	public void updateLoginThirdRecord(MemAuth entity);

	public void addMemAuth(MemAuth memAuth, Long memberId, HttpServletRequest request);
	public Member saveMember(Member member2);
	public MemAuth getMemberAuth(Long memberId, MemAuth memAuth);

	public HotReplyFees getReplyFees(Long expertId);
	//当退款的时候,更新专家的虚拟币帐户
	public void updateProMember(Member entity);
    //通过会员ID找到一条会员授权记录
	public MemAuth getMemberAuth(Member member1);
	/**
	 * 查询是否由此uuid
	 */
	public Member getUUID(String uuid);
	
	/**
	 * 跟据memberId查找手机号码
	 * @param memberId
	 * @return
	 */
	public Member getMobileById(Long memberId);

	/**
	 * 根据购买类型和memberId返回该用户购买该商品所有可用的优惠券
	 * @param type
	 * @param memberId
	 * @param pageSize 
	 * @param pageNo 
	 * @param videoId 
	 * @return
	 */
	public List<CoupnUser> getUserCoupnByMemberIdAndType(String type, Long memberId, int pageNo, int pageSize, int videoId);

	/**
	 * 返回改用户所有未使用的优惠券
	 * @param memberId
	 * @param used 
	 * @return
	 */
	public List<CoupnUser> getAllUserCoupn(Long memberId, String used);
	
	/**
	 * 根据用户卡券id数组，查询用户卡券
	 * @param coupnId
	 * @return
	 */
	public abstract List<CoupnUser> getCoupnsById(Long[] coupnId);

	public void updateUserCoupn(CoupnUser coupnUser);

	/**
	 * 更新卡券
	 * @param coupnId
	 */
	public void updateCoupn(Coupn coupnId);

	public List<CoupnUser> getAllUserCoupn(Long memberId, int pageNo, int pageSize);

	/**
	 * 根据卡券id找出相应的卡券
	 * @param coupnId
	 * @return
	 */
	public List<Coupn> getCoupnById(Long[] coupnId);

	public ExpenseTotal checkBuyStatus(Member member, String stype, Long[] relateId);

	public int memberNum();

	public List<WechatWeiboAttention> retrieveAttention();

	public Fusing fusingTimes(String interfaces);

	public int maxAttentionNum(int typeNum);

	//是否存在未使用的兑换码
	public RedeemCodeOwner isExistUnUsedCode(String redeemCode);

	public RedeemCodeStock getRedeemStockById(Long redeemCodeId);

	public Coupn coupnByVideoId(int videoId);

	public StandardReading getStandardById(Long relatedId);

	public LiveVideo getLiveVideoById(Long relatedId);

	public Coupn getCoupnById(Long relatedId);

	public void updateRedeemOwn(RedeemCodeOwner owner, Member member);

	public boolean isCanExchange(Long redeemCodeId, Long userId);

	//是否有次兑换码
	public RedeemCodeOwner isExistCode(String redeemCode);

	public void addUserCoupn(CoupnUser coupnUser);
	
	public boolean isSameTypeActivity(Long redeemCodeId, Long memberId);

	public List<Member> getMembersByMemberIds(List<Long> memberIds);
	
	public Packages getPackages(Long relatedId);

	public List<Coupn> getCouponList(Date date);

	public List<Coupn> getCouponNotExpire(Date afterThreeDays);

	public List<Long> getUnusedCouponPerson(Long couponId);

	public List<Member> getMemberNotExpire(Date time);

	public List<LiveVideo> getLiveAirOn(Date time);

	public Coupn saveCoupn(Coupn coupn);

	public MemberMp findMemberMpByMemberId(Long memberId);

	public MemberMp findMemberMpByOpenId(String openId);

    BankMember saveBankMember(BankMember bankMember);

    MemberMp findMemberMpByOpenIdAndMemberId(Long memberId, String openId);
}