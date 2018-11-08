package cc.messcat.dao.member;

import java.math.BigDecimal;
import java.util.List;

import cc.messcat.bases.BaseDao;
import cc.messcat.entity.ActivityExchangeRecord;
import cc.messcat.entity.LiveVideoDistributor;
import cc.messcat.entity.RelevanceWechat;

public interface ActivityExchangeRecordDao extends BaseDao{

	//新增兑换记录
	public void addExchangeRecord(ActivityExchangeRecord activityExchangeRecord);
	
	//根据兑换的类型返回兑换该类型的次数
	public List<ActivityExchangeRecord> getExchangeCount(String changeType,Long memberId);

	public List<RelevanceWechat> exchangeCount(String openId, String mobile, int videoId);

	public void exchangeRecord(RelevanceWechat relevance);

	public ActivityExchangeRecord findRecordByOpenId(String openId,Long videoId);

	public LiveVideoDistributor getLiveVideoDistributor(Long videoId, Long memberId);

	public List<LiveVideoDistributor> getDistributorByMemberId(Long memberId);

	public BigDecimal getTotalIntegral(Long memberId);
}
