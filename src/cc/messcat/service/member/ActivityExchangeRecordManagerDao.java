package cc.messcat.service.member;

import java.math.BigDecimal;
import java.util.List;

import cc.messcat.entity.ActivityExchangeRecord;
import cc.messcat.entity.LiveVideoDistributor;
import cc.messcat.entity.RelevanceWechat;

public interface ActivityExchangeRecordManagerDao {

	/**
	 * 新增兑换记录
	 * @param activityExchangeRecord
	 */
	public void addExchangeRecord(ActivityExchangeRecord activityExchangeRecord);
	
	/**
	 * 查询改会员兑换某种礼品的次数
	 * @param changeType
	 * @param memberId
	 * @return
	 */
	public List<ActivityExchangeRecord> getExchangeCount(String changeType,Long memberId);

	/**
	 * @param openId
	 * @param mobile
	 * @param videoId 
	 * @return
	 * 查询视频卡券的领取情况：一个微信号和一个手机号只能领取一次
	 */
	public List<RelevanceWechat> exchangeCount(String openId, String mobile, int videoId);

	/**
	 * @param relevance
	 * 新增记录
	 */
	public void exchangeRecord(RelevanceWechat relevance);

	public ActivityExchangeRecord findRecordByOpenId(String openId, Long videoId);

	public int gainIntegralByScan(Long videoId, Long memberId, String wechatName);

	public int gainIntegralByPaid(Long videoId, Long memberId, String wechatName,double money,double integral);

	public LiveVideoDistributor getLiveVideoDistributor(Long videoId, Long memberId);

	public List<LiveVideoDistributor> getDistributorByMemberId(Long memberId);

	public BigDecimal getTotalIntegral(Long memberId);

}
