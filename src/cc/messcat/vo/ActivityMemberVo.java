package cc.messcat.vo;

/**
 * 分享活动，邀请有礼页面所需数据
 * @author HASEE
 *
 */
public class ActivityMemberVo {

	private Long memberId;
	private int recommendTimes;
	public Long getMemberId() {
		return memberId;
	}
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	public int getRecommendTimes() {
		return recommendTimes;
	}
	public void setRecommendTimes(int recommendTimes) {
		this.recommendTimes = recommendTimes;
	}
	
	
}
