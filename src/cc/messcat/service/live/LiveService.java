package cc.messcat.service.live;

import java.util.Date;
import java.util.List;

import cc.messcat.entity.FreeVideo;
import cc.messcat.entity.LiveVideo;
import cc.messcat.entity.LiveVideoSubject;
import cc.messcat.entity.Member;
import cc.messcat.entity.RecordEstimate;
import cc.messcat.entity.RedeemCodeOwner;
import cc.messcat.entity.VideoBullet;
import cc.messcat.entity.ViewerBetweenLive;
import cc.messcat.vo.ExpertCourseVo;
import cc.messcat.vo.LiveOpenVo;
import cc.messcat.vo.LiveVideoVo2;
import cc.messcat.vo.LiveVideoVo3;
import cc.messcat.vo.LiveViewVo;
import cc.messcat.vo.LiveVo;
import cc.messcat.vo.PopularListResult;
import cc.messcat.vo.RecordEstimateVo;
import cc.messcat.vo.SpecialVo;
import cc.modules.commons.Pager;

public interface LiveService {
    
	//专家打开直播
	LiveOpenVo openLive(String id, String expertId2);
    //观众观看直播
	LiveViewVo viewLive(Long memberId, String id);

	/**
	 * 更新视频信息
	 * @param liveVideos
	 */
	void update(LiveVideo liveVideos);
	/**
	 * 获取视频列表
	 * @param pageSize 
	 * @param pageNo 
	 */
	Pager videoList(int pageNo, int pageSize);
	/**
	 * 查询所有直播课程,无论预约状态是何状态的都返回
	 * @param memberId 
	 * @return
	 */
	Pager getLiveVideos(String[] bespeakStatus,int pageNo, int pageSize, Long memberId);
	/**
	 * 直播期间，每进入一次，往表里写一条数据
	 * @param vbLive 
	 */
	void addWatchRecord(ViewerBetweenLive vbLive);
	/**
	 * 统计观看次数，同一用户多次访问，只算一次
	 */
	int countViewers(Long videoId);
	/**
	 * 取出视频标识
	 * @param experId 
	 * @return
	 */
	Long getWatchMark(Long experId);
	/**根据视频id，查找该视频的观看人数
	 * @param videoId
	 */
	LiveVideo getViewers(Long videoId);

	// 专家申请预约直播或者专家修改预约直播详情
	Long insertOrUpdate(LiveVideo liveVideo);
	//查看预约直播的详情
	LiveVideo getLiveVideoById(Long id);
	//专家取消预约
	void updateBespeakStatus(LiveVideo liveVideo);
	/**
	 * 获取当天即将开始的直播
	 * @param pageSize 
	 * @param pageNo 
	 * @return
	 */
	Pager getTodayLive(int pageNo, int pageSize,String chieflyShow);

	/**
	 * 获取热门视频
	 * @param pageSize 
	 * @param pageNo 
	 * @return
	 */
	Pager getHotVideosChieflyShow(int pageNo, int pageSize);
	/**
	 * 热门视频-查看更多
	 * @param pageNo	页码
	 * @param pageSize	每页返回的条数
	 * @param isSeries	0（单一）	1（系列）
	 * @return	
	 */
	Pager getMoreHotVideos(int pageNo, int pageSize, String isSeries);
	/**
	 * 系列视频-展示
	 * @return
	 */
	List<LiveVideo> seriesVideosChieflyShow(String chieflyShow);
	/**
	 * 系列视频-查看更多
	 * @param chieflyShow
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	Pager getMoreSeriesVideos(String chieflyShow, int pageNo, int pageSize);
	/**
	 * 根据fatherId查询连载视频的子视频
	 * @param videoId
	 * @return
	 */
	List<LiveVideo> getSeriesVideoByFatherID(Long videoId);
	/**
	 * 根据视频id数组，查询多个视频
	 * @param relateId
	 * @return
	 */
	List<LiveVideo> getLiveVideosById(Long[] relateId);
	
	/**
	 * 根据fatherId数组，查询fatherId等于数组内元素的所有子视频
	 * @param chapterId
	 * @return
	 */
	List<LiveVideo> getLiveVideosByFatherId(Long[] chapterId);
	/**
	 * 发送系统消息
	 */
	LiveVideo sendSysNoti(Long videoId);
	/**
	 * 获取腾讯云回调需要修改的那个视频
	 * @param experId
	 * @return
	 */
	LiveVideo afterLiveVideo(Long experId);
	
	Pager getSubjectList(int pageNo, int pageSize,String chiefShow);
	
	/**
	 * 根据关键字模糊查询视频
	 * @param searchKeyWord
	 * @return
	 */
	List<LiveVo> getLiveVideBySearchKeyWord(String searchKeyWord);
	/**
	 * 获取首页专题的列表
	 */
	
	/**
	 * 根据关键字模糊查询专题
	 * @param searchKeyWord
	 * @return
	 */
	List<SpecialVo> getSpecialBySearchKeyWord(String searchKeyWord);
	/**
	 * 点击查看更多的视频课程(包括直播)
	 * @param searchKeyWord
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	Pager getMoreLiveVideo(String searchKeyWord, int pageNo, int pageSize);
	/**
	 * 点击查看更多的专题
	 * @param searchKeyWord
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	Pager getMoreSpecial(String searchKeyWord, int pageNo, int pageSize);
	/**
	 * 获取首页精品课程的列表
	 * @param member 
	 */
	Pager getExcellentCourse(int pageNo, int pageSize, String chiefShow, Member member);
	/**
	 * 根据会员id（专家id）获取该专家的视频课程
	 * @param memberId
	 * @param pageSize 
	 * @param pageNo 
	 * @return
	 * 
	 */
	List<ExpertCourseVo> getCourse(Long memberId, int pageNo, int pageSize);
	/**
	 * 专题Id
	 * @param subjectId
	 * @param subjectId2 
	 * @param pageSize 
	 * @return
	 */
	Pager getSubjectDetail(int pageNo, int pageSize, Integer subjectId);
	LiveVideoSubject getSubject(Integer subjectId);
	
	PopularListResult getPopularList(int pageNo, int pageSize, Long videoId, Long memberId);
	
	LiveVideoVo2 getVideoAdjunct(Long videoId);
	/**
	 * 查看该用户是否观看过这个视频
	 * @param videoId
	 * @param memberId
	 * 
	 * @return 
	 */
	List<ViewerBetweenLive> watchRecord(Long videoId, Long memberId);
	/**
	 *  视频课程详情
	 * @param videoId2 
	 */
	LiveVideoVo3 getVideoCourseDetail(Long memberId,Long videoId, String port);
	
	/**
	 * 根据手机号获取用户的兑换码
	 */
	RedeemCodeOwner getOwnCode(String mobile);
	/**
	 * 保存弹幕
	 * @param bullet
	 * 
	 */
	void saveBullet(VideoBullet bullet);
	
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
	
	/**
	 * 根据视频id，按时间倒叙拉取弹幕
	 * @param videoId 
	 * @param pageSize 
	 * @param pageNo 
	 * @return
	 */
	Pager pullBullet(Long videoId, int pageNo, int pageSize);
	/**
	 * 查询某个视频的所有观众
	 * @param videoId
	 * @return
	 */
	List<ViewerBetweenLive> watchRecord(Long videoId);
	/**
	 * 获取最新写进表里的弹幕
	 * @return
	 */
	VideoBullet getNewBullet();
	//获取视频评价的记录
	RecordEstimate getRecordEstimate(Long memberId, Long videoId, int seriesVideoId);
	
	//获取总的好评率
	double getSumGrade(int videoId, int seriesVideoId);
	
	//编辑录播视频的评价
	void updateRecordEstimate(RecordEstimate recordEstimate);
	
	//保存录播视频的评价
	int saveRecordEstimate(RecordEstimate recordEstimate);
	
	//根据id获取评价记录
	RecordEstimate getRecordEstimate(Integer id2);
	
	//根据视频id 获取评价记录
	Pager getRecordEstimate(Long memberId,Long videoId,int pageSize, int pageNo);
	
	List<RecordEstimate> getRecordEstimate(int videoId, int seriesVideoId);
	
	//给还未使用兑换码  兑换卡券 的人发送短信提醒
	void sendCodeToCoupn();
	//给未满足"邀请三人"免费听取活动资格的用户发送短信
	void sendInviteActivity(Long videoId, String title, String time);
	//通知用户,卡券的有效期快到了,赶快去消费
	void sendUseCoupn();
	//分頁拉取墻上的內容
	Pager pullWalls(Long videoId, int pageNo, int pageSize);
	//根據聊天室名稱解除這個聊天室內所有用戶的禁言
	void relieveSilenceByGroupId(String groupId);
	/**
	 * 根据视频id，查出这个视频的免单信息
	 * @param videoId
	 * @return
	 */
	FreeVideo getFreeVideo(Long videoId);
	//根据memberId统计app分享次数
	Long getAppRecommendTimes(Long memberId, Long videoId);
	//根据openId查看微信分享次数
	Long getWechatRecommendTimes(String openId, Long videoId);
	int sendDiscountMsg(long videoId, Long memberId,String wechatName);
	/**
	 * 根据mid查询这位分销员分销视频所获得的所有积分
	 * @param id
	 * @return
	 */
	Integer getDistributorIntegralByMemberId(Long id);
	

}
