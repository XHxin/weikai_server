package cc.messcat.dao.live;

import java.util.List;

import cc.messcat.entity.Attention;
import cc.messcat.entity.ExpenseTotal;
import cc.messcat.entity.ChatRoomAuth;
import cc.messcat.entity.Coupn;
import cc.messcat.entity.FreeVideo;
import cc.messcat.entity.LiveVideo;
import cc.messcat.entity.LiveVideoDiscount;
import cc.messcat.entity.LiveVideoSubject;
import cc.messcat.entity.Member;
import cc.messcat.entity.RecordEstimate;
import cc.messcat.entity.RedeemCodeOwner;
import cc.messcat.entity.RelevanceWechat;
import cc.messcat.entity.InviteStatis;
import cc.messcat.entity.SilenceList;
import cc.messcat.entity.CoupnUser;
import cc.messcat.entity.VideoBullet;
import cc.messcat.entity.ViewerBetweenLive;
import cc.messcat.vo.PopularListVo;
import cc.modules.commons.Pager;

public interface LiveDao {

	Member getExpert(String expertId);

	void update(LiveVideo liveVideos);

	List<LiveVideo> videoList(int pageNo, int pageSize);

	Pager getLiveVideos(String[] bespeakStatus,int pageNo, int pageSize, Long memberId);

	void addWatchRecord(ViewerBetweenLive vbLive);

	int countViewers(Long videoId);

	Long getWatchMark(Long experId);

	LiveVideo getViewers(Long videoId);

	//申请预约直播插入一条数据或者修改预约直播详情
	Long insertOrUpdate(LiveVideo liveVideo);

	//获取一条预约直播信息
	LiveVideo getLiveVideoById(Long id);

	//取消预约直播
	void updateBespeakStatus(LiveVideo liveVideo);

	Pager getTodayLive(int pageNo, int pageSize, String chieflyShow);

	Attention isAttention(Long memberId, String expertId);
	
	Pager getHotVideosChieflyShow(int pageNo, int pageSize);

	Pager getMoreHotVideos(int pageNo, int pageSize, String isSeries);

	List<LiveVideo> seriesVideosChieflyShow(String chieflyShow);

	List<LiveVideo> getSeriesVideoByFatherID(Long videoId);

	Pager getMoreSeriesVideos(String chieflyShow, int pageNo, int pageSize);

	List<LiveVideo> getLiveVideosById(Long[] relateId);

	List<LiveVideo> getLiveVideosByFatherId(Long[] chapterId);

	boolean isAdmin(Long memberId);

	void changeSilence(LiveVideo video);

	/**
	 * 根据fatherId,把fatherId相等的（同一系列下的章节）视频，以及videoId等于fatherId的（父级）都查出来
	 * @param fatherId
	 * @return
	 */
	List<LiveVideo> getWholeVideo(Long fatherId);

	LiveVideo afterLiveVideo(Long experId);

	LiveVideo searchFatherVideo(Long fatherId);
    
	Pager getSubjectList(int pageNo, int pageSize,String chiefShow);

	/*
	 * 模糊查询
	 */
	List<LiveVideo> getLiveVideBySearchKeyWord(String searchKeyWord);

	/**
	 * 根据关键字模糊查询专题
	 * @param searchKeyWord
	 * @return
	 */
	List<LiveVideoSubject> getAllLiveSubjectBySearchKeyWord(String searchKeyWord);

	/**
	 * 点击查看更多的视频课程(包括直播)
	 * @param searchKeyWord
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	List<LiveVideo> getMoreLiveVideo(String searchKeyWord, int pageNo, int pageSize);

	/**
	 * 点击查看更多的专题
	 * @param searchKeyWord
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	List<LiveVideoSubject> getMoreLiveSubject(String searchKeyWord, int pageNo, int pageSize);

	Pager getExcellentCourse(int pageNo, int pageSize, String chiefShow);

	Pager getCourse(Long memberId, int pageNo, int pageSize);

	Pager getSubjectDetail(int pageNo, int pageSize, Integer subjectId);

	LiveVideoSubject getSubject(Integer subjectId);

	ExpenseTotal getBuyStatus(Long memberId, Long relatedId);

	/**
	 * @param id
	 * @return
	 * 查询系列下的所有子视频
	 */
	List<LiveVideo> allSonVideosByFatherId(Long id);

	List<PopularListVo> getPopularList(Long videoId);

	PopularListVo getPopular(Long videoId,Long memberId);

	/**
	 * 获取会员信息
	 */
	Member getMember(Long memberId);

	List<ViewerBetweenLive> watchRecord(Long videoId, Long memberId);

	List<InviteStatis> getPopularListVoList(Long videoId, Long memberId);

	Long getPaidSum(Long fromId, Long videoId);

	List<LiveVideo> getChildVideoList(Long videoId);

	/**
	 * 判断是否是聊天室的管理员
	 */
	ChatRoomAuth isChatAdmin(Long memberId);

	RelevanceWechat getMemberByFromId(Long fromId, Long videoId);

	Member getMemberByMobile(String mobile);

	/**
	 * 根据手机号获取用户的兑换码
	 */
	RedeemCodeOwner getOwnCode(String mobile);
	/**
     * 查找出不是最新App版本的用户手机号列表
     */
	List<String> getIosNotNewestVersion();
	List<String> getAndroidNotNewestVersion();
	/**
     * 查找出最新App版本的用户手机号列表
     */
	List<String> getIosNewestVersion();
	List<String> getAndroidNewestVersion();

	void saveBullet(VideoBullet bullet);

	Pager pullBullet(Long videoId, int pageNo, int pageSize);

	List<ViewerBetweenLive> watchRecord(Long videoId);

	//获取视频评价的记录
	RecordEstimate getRecordEstimate(Long memberId, Long videoId, int seriesVideoId);

	//根据视频课程id与用户是否购买记录 获取评价记录    分页
	Pager getRecordEstimate(int buyStatus,Long videoId,int pageSize,int pageNo);

	//根据视频id 或 系列视频id 获取 评价记录
	List<RecordEstimate> getRecordEstimate(int videoId, int seriesVideoId);

	//编辑录播视频的评价
	void updateRecordEstimate(RecordEstimate recordEstimate);

	//保存录播视频的评价
	int saveRecordEstimate(RecordEstimate recordEstimate);

	//根据评价id获取评价记录
	RecordEstimate getRecordEstimate(Integer id);


	VideoBullet getNewBullet();
	
	//给还未使用兑换码  兑换卡券 的人发送短信提醒
	List<RedeemCodeOwner> sendCodeToCoupn();
	//给未满足"邀请三人"免费听取活动资格的用户发送短信
	List<PopularListVo> sendInviteActivity(Long videoId);
	//通知用户,卡券的有效期快到了,赶快去消费
	List<String> sendUseCoupn();

	Pager pullWalls(Long videoId, int pageNo, int pageSize);

	List<SilenceList> getSilenceList(String groupId);

	void relieveSilenceByGroupId(String groupId);

	FreeVideo getFreeVideo(Long videoId);

	Long getAppRecommendTimes(Long memberId, Long videoId);

	Long getWechatRecommendTimes(String openId, Long videoId);

	LiveVideoDiscount getLiveVideoDiscountById(long videoId);

	int getInviteStatis(Long memberId, long videoId);

	void saveCoupn(Coupn coupn);

	Coupn getCoupnByVideoId(long videoId,String scopeNum);

	void saveUserCoupn(CoupnUser coupnUser);

	Integer getDistributorIntegralByMemberId(Long memberId);

}
