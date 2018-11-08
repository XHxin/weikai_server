package cc.messcat.web.server;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;
import com.vdurmont.emoji.EmojiParser;
import cc.messcat.entity.ExpenseTotal;
import cc.messcat.entity.Collect;
import cc.messcat.entity.DibblerResponse;
import cc.messcat.entity.EventMessageNotification;
import cc.messcat.entity.FreeVideo;
import cc.messcat.entity.LiveVideo;
import cc.messcat.entity.LiveVideoSubject;
import cc.messcat.entity.Member;
import cc.messcat.entity.RecordEstimate;
import cc.messcat.entity.VideoBullet;
import cc.messcat.entity.VideoWall;
import cc.messcat.entity.ViewerBetweenLive;
import cc.messcat.vo.ChapterVideoVo;
import cc.messcat.vo.EstimateOrGradeVo;
import cc.messcat.vo.ExpertCourseVo;
import cc.messcat.vo.LiveOpenVo;
import cc.messcat.vo.LiveVideoBespeakVo;
import cc.messcat.vo.LiveVideoListResult;
import cc.messcat.vo.LiveVideoListResult2;
import cc.messcat.vo.LiveVideoListVo3;
import cc.messcat.vo.LiveVideoVo2;
import cc.messcat.vo.LiveVideoVo3;
import cc.messcat.vo.LiveViewVo;
import cc.messcat.vo.PopularListResult;
import cc.messcat.vo.RecordEstimateVo;
import cc.messcat.vo.RecordEstimateVoList;
import cc.messcat.vo.ResponseBean;
import cc.messcat.vo.SeriesVideoVo;
import cc.messcat.vo.SimpleVideoVo;
import cc.messcat.vo.VideoBulletListResult;
import cc.messcat.vo.VideoBulletVo;
import cc.messcat.vo.VideoDetailVo;
import cc.messcat.vo.VideoWallListResult;
import cc.messcat.vo.VideoWallVo;
import cc.modules.commons.PageAction;
import cc.modules.commons.Pager;
import cc.modules.constants.Constants;
import cc.modules.util.DateHelper;
import cc.modules.util.GetJsonData;
import cc.modules.util.HttpRequest;
import cc.modules.util.MD5;
import cc.modules.util.ObjValid;
import cc.modules.util.PropertiesFileReader;

@SuppressWarnings("serial")
public class LiveAction extends PageAction implements ServletRequestAware {

	private static Logger log = LoggerFactory.getLogger(LiveAction.class);
	private static String jointUrl = PropertiesFileReader.getByKey("static.domain");// 图片拼接
	private static String singleCourse = PropertiesFileReader.getByKey("single_course"); // 单一课程
	private static String seriesCourse = PropertiesFileReader.getByKey("series_course"); // 系列课程
	private static String singleLive = PropertiesFileReader.getByKey("single_live"); // 单一直播
	private static String appAccessor = PropertiesFileReader.getByKey("app.accessor");
	private static String webAccessor = PropertiesFileReader.getByKey("web.accessor");
	private static String memberImgUrl = PropertiesFileReader.getByKey("member.photo.url");// 图片拼接
	private static String saveBulletUrl = PropertiesFileReader.getByKey("saveBulletUrl");
	private static String userSig = "eJx10EFvgjAYxvE7n6LpdWYrtEJZ4sEtMzPgBpFsxEvT0UpqhVUoi2L87mZqMi57r88v*Sfv0QEAwCxe3vOi*O5qy*zBSAgeAXQpwRSO-oAxSjBuGW7EFRCEEAn9AA2U3BvVSMbXVjZXNQ58in5voJSQtVVrdTNcVKoezK3Q7JL7v9Oq8jIuXtLn*Sxvtc6iTZyGXeYLb4GKn-ee9rXxcDLHRCafD695JU3jTtW026zsqr*ju6jY0zf5dPgwZUK63TblOilFHuvoS3rhchuXk8kgaVV1*8zYRYEXYEKhc3LOE9lY1w__";
	private Object object;
	private Member member;// 会员
	private String id; // 直播课程的ID
	private String title;// 课程名称
	private String introduct;// 视频简介
	private String terminal;// 观看设备，0为移动端，1为PC端
	private String applyDate;// 直播时间
	private String expertId; // 直播专家Id
	private String expertName;// 直播专家的名称
	private LiveVideo liveVideo;// 直播的实体
	private String videoType;
	private Long videoId;
	private Long memberId; // 观众Id
	private String bespeakType;
	private HttpSession session;
	private HttpServletRequest request;
	private String chieflyShow;// 1-热门课程展示 0-热门课程查看更多
	private String isSeries;// 0-单一（热门视频点击查看更多时传0） 1-系列
	private String accessToken;
	private String groupId; // 聊天室Id
	private Integer subjectId;
	private String port; // 访问的端口,有app和web
	private String content;//弹幕内容
	private String bulletContent;//弹幕内容
	private Date appearTime;//弹幕的发送时间
	private int grade; // 打星 评价的等级 1为差 2为中 3-5为好评
	private int seriesVideoId; // 系列视频的id
	private RecordEstimate recordEstimate; // 录播视频评价的实体对象
	private String bulletId;//弹幕的唯一Id，腾讯云返回

	
	public LiveAction() {
		liveVideo = new LiveVideo();
	}

	/**
	 * 7.1 专家开启直播
	 */
	public String openLive() {
		String method = "https://console.tim.qq.com/v4/group_open_http_svc/create_group";
		String groupUrl = method + "?userSig=" + userSig
				+ "&identifier=admin&sdkappid=1400049670&apn=1&contenttype=json";
		LiveVideo video = liveService.getLiveVideoById(Long.valueOf(id));
		LiveOpenVo result = liveService.openLive(id, String.valueOf(video.getExpertId()));
		/*
		 * 云通信创建互动直播聊天室房间
		 */
		JSONObject jsonParam = new JSONObject();
		jsonParam.put("Owner_Account", "admin"); // 群主的UserId
		jsonParam.put("Type", "AVChatRoom"); // 互动直播聊天室支持游客加入
		jsonParam.put("GroupId", "weikai_" + String.valueOf(video.getExpertId())); // 自定义群组Id
		jsonParam.put("Name", "weikaiChat" + String.valueOf(video.getExpertId())); // 群名称
		String str = GetJsonData.getJsonData(jsonParam, groupUrl);
		JSONObject dataJson = new JSONObject(str);
		String groupId = dataJson.getString("GroupId");
		result.setGroupId(groupId);
		object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, result);
		return "success";
	}

	/**
	 * 7.2 观众获取播放地址
	 */
	public String viewLive() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("textml;charset=UTF-8");
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		response.setHeader("Access-Control-Max-Age", "0");
		response.setHeader("Access-Control-Allow-Headers",
				"Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("XDomainRequestAllowed", "1");
		LiveViewVo result = liveService.viewLive(memberId, id);

		if (result != null) {
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, result);
		} else {
			object = new ResponseBean(Constants.FAIL_CODE_400, Constants.MSG_DATA_EXCEPT);
		}
		return "success";
	}

	/**
	 * 7.3 专家申请(预约/修改)直播
	 */
	public String bespeakLive() throws Exception {
		try {
			liveVideo.setBespeakStatus("0");
			liveVideo.setBespeakDate(new Date());
			liveVideo.setStatus("1");
			liveVideo.setVideoType("0");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String format = sdf.format(new Date());
			long time = sdf.parse(format).getTime() + 2 * 24 * 60 * 60 * 1000;
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date apply_date = sdf2.parse(applyDate);
			long time2 = apply_date.getTime();
			if (time2 >= time) {
				liveVideo.setTitle(title);
				liveVideo.setDuration(0l);
				liveVideo.setViewers(0l);
				liveVideo.setLargeCover("");
				liveVideo.setMiddleCover("");
				liveVideo.setDetailCover("");
				liveVideo.setInvitedCover("");
				liveVideo.setCover("");
				liveVideo.setClassify(4); // 单一直播
				liveVideo.setFatherId(-1l);
				liveVideo.setVideoUrl("");
				liveVideo.setIntroduct(introduct);
				liveVideo.setTerminal(terminal);
				liveVideo.setPrice(new BigDecimal(0));
				liveVideo.setLinePrice(new BigDecimal(0));
				liveVideo.setApplyDate(apply_date);
				liveVideo.setExpertId(Long.valueOf(expertId));
				liveVideo.setChieflyShow("1");
				liveVideo.setExpertName(expertName);
				liveVideo.setCheckRemark("");
				liveVideo.setFlvUrl("");
				liveVideo.setHlsUrl("");
				liveVideo.setRtmpUrl("");
				liveVideo.setPhotos("");
				liveVideo.setIsHot("1"); // 热门
				liveVideo.setOnlyWholeBuy("0");
				liveVideo.setVideoStatus("0");
				liveVideo.setIsSilence(0);
				liveVideo.setStudyCount(0L);
				liveVideo.setSubjectId(0);
				liveVideo.setCourseShow(0);
				liveVideo.setIsVIPView(0);
				liveVideo.setConsultMemberId(0L);
				if (StringUtils.isNotBlank(id)) {
					liveVideo.setId(Long.valueOf(id));
				}
				Long id = liveService.insertOrUpdate(liveVideo);

				LiveVideoBespeakVo result = new LiveVideoBespeakVo();
				result.setApplyDate(applyDate);
				result.setBespeakStatus("0");
				result.setExpertId(expertId);
				result.setIntroduct(introduct);
				result.setTerminal(terminal);
				result.setTitle(title);
				result.setId(id.toString());
				result.setIsBeginLive("0");
				result.setIsCancelBespeak("0");
				object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, result);
			} else {
				object = new ResponseBean(Constants.EXCEPTION_CODE_600, Constants.MSG_FAIL);
			}

		} catch (Exception e) {
			object = new ResponseBean(Constants.EXCEPTION_CODE_600, Constants.MSG_FAIL);
		}
		return "success";
	}

	/**
	 * 7.4 专家查看预约直播的详情
	 */
	public String viewBespeakLive() throws Exception {
		try {
			LiveVideo liveVideo = liveService.getLiveVideoById(Long.valueOf(id));
			LiveVideoBespeakVo result = new LiveVideoBespeakVo();
			result.setApplyDate(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(liveVideo.getApplyDate()));
			result.setBespeakStatus(liveVideo.getBespeakStatus());
			result.setExpertId(liveVideo.getExpertId().toString());
			result.setIntroduct(liveVideo.getIntroduct());
			result.setTitle(liveVideo.getTitle());
			result.setTerminal(liveVideo.getTerminal());
			result.setId(id);
			result.setCheckRemark(liveVideo.getCheckRemark());
			if (liveVideo.getBespeakDate().getTime() + 24 * 60 * 60 * 1000 >= new Date().getTime()) {
				result.setIsCancelBespeak("0");
			} else {
				result.setIsCancelBespeak("1");
			}
			if (new Date().getTime() >= (liveVideo.getApplyDate().getTime() - 15 * 60 * 1000)) {
				result.setIsBeginLive("1");
			} else {
				result.setIsBeginLive("0");
			}

			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, result);
		} catch (Exception e) {
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_FAIL);
		}
		return "success";
	}

	/**
	 * 7.5 视频直播结束，腾讯云回调，生成录播地址
	 */
	public String tencentCloudCallback() throws IOException {
		log.error("进入腾讯云直播结束回调>>>>>>>>>>>>>>>>>>>>>>");
		// 解析json数据
		int contentLength = request.getContentLength();
		if (contentLength < 0) {
			return null;
		}
		byte buffer[] = new byte[contentLength];
		for (int i = 0; i < contentLength;) {
			int len = request.getInputStream().read(buffer, i, contentLength - i);
			if (len == -1) {
				break;
			}
			i += len;
		}
		String str1 = new String(buffer, "UTF-8");
		JSON json = (JSON) JSON.parse(str1);
		EventMessageNotification notification = (EventMessageNotification) JSON.toJavaObject(json,
				EventMessageNotification.class);
		if (notification != null) {
			if (notification.getEvent_type() == 100) { // 正常直播完成，新的录制文件已生成
				// 根据stream_id可知这个视频是属于哪位专家的
				String streamId = notification.getStream_id();
				String str[] = streamId.split("_");
				Long experId = 0L;
				for (int i = 0; i < str.length; i++) {
					experId = Long.valueOf(str[1]);
				}

				// 直播结束，统计直播期间的观看人数
				Long videoId = liveService.getWatchMark(experId);
				int rowCount = liveService.countViewers(videoId);

				// 然后根据专家的专家id以及专家申请直播开始的顺序，把这个录播url，直播时长更新到对应的记录里
				LiveVideo liveVideo = liveService.afterLiveVideo(experId);
				liveVideo.setViewers((long) rowCount);
				liveVideo.setExpertId(experId);
				liveVideo.setVideoType("1");// 把直播修改为录播
				liveVideo.setVideoUrl(notification.getVideo_url());
				liveVideo.setDuration((long) notification.getDuration());
				liveVideo.setOnlyWholeBuy("0");
				liveVideo.setIsHot("1");
				liveVideo.setBespeakStatus("4");
				liveService.update(liveVideo);
			} else if (notification.getEvent_type() == 0) { // 代表视频录播中断

			}
		}
		
		//把這個聊天室的禁言名單清空。這位專家的下一場直播，用戶可以正常發言
		//根據聊天名字，把禁言名單清空
		String groupId = "weikai_" + expertId;
		liveService.relieveSilenceByGroupId(groupId);
		return "success";
	}

	/**
	 * 7.6 观看直播增加观看人数
	 * 
	 * @return
	 */
	public synchronized String watchTimes() {// 调这个方法的时候，传一个视频类型和视频id
		if(videoId == null || memberId == null) {
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_PARAMETER_EMPTY);
			return "success";
		}
		List<ViewerBetweenLive> record = liveService.watchRecord(videoId, memberId);
		if (record == null || record.size() < 1) {
			ViewerBetweenLive vbLive = new ViewerBetweenLive();
			vbLive.setMemberId(memberId);
			vbLive.setVideoId(videoId);
			liveService.addWatchRecord(vbLive);

			LiveVideo video = liveService.getViewers(videoId);
			// 修改视频的观看次数
//			video.setViewers(video.getViewers() + 1);
			video.setStudyCount(video.getStudyCount() + 1);
			// 把修改后的值存回数据库
			liveService.update(video);
		}
		return "success";
	}

	/**
	 * HLS视频自动拼接 回调接口(当推流端中间有间隔5分钟之内的中断直播时, 腾讯会把分自片的录播视频自动拼接)
	 */
	@Override
	public String execute() throws Exception {
		log.error("回调进入excute>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		// 解析json数据
		int contentLength = request.getContentLength();
		if (contentLength < 0) {
			return null;
		}
		byte buffer[] = new byte[contentLength];
		for (int i = 0; i < contentLength;) {
			int len = request.getInputStream().read(buffer, i, contentLength - i);
			if (len == -1) {
				break;
			}
			i += len;
		}
		String str1 = new String(buffer, "UTF-8");
		JSON json = (JSON) JSON.parse(str1);
		log.error(json.toString() + ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>json");
		DibblerResponse notification = (DibblerResponse) JSON.toJavaObject(json, DibblerResponse.class);
		notification.getFileId();
		System.out.println(
				notification.getFileId() + ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		return super.execute();
	}

	/**
	 * 7.7 获取视频列表(bespeakType=0:直播,bespeakType=1:录播)
	 */
	@SuppressWarnings("unchecked")
	public String bespeakList() {
		LiveVideoListResult result = new LiveVideoListResult();
		Member member = memberManagerDao.retrieveMember(memberId);
		if (member != null && member.getAccessToken().equals(accessToken)) {
			if (bespeakType != null) {
				Pager pager = new Pager();
				if (bespeakType.equals("0")) {
					String[] bespeakStatus = new String[] { "1", "0" };
					pager = liveService.getLiveVideos(bespeakStatus, pageNo, pageSize, memberId);
				} else if (bespeakType.equals("1")) {
					String[] bespeakStatus = new String[] { "2", "4" };
					pager = liveService.getLiveVideos(bespeakStatus, pageNo, pageSize, memberId);
					List<LiveVideo> list = pager.getResultList();
					for (LiveVideo liveVideo : list) {
						liveVideo.setViewers(liveVideo.getStudyCount());
					}
				}
				result.setVideos(pager.getResultList());
				result.setRowCount(pager.getRowCount());
				object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, result);
			} else {
				object = new ResponseBean(Constants.FAIL_CODE_400, Constants.MSG_PARAMETER_EMPTY);
			}
		} else {
			object = new ResponseBean(Constants.FAIL_CODE_400, "登录token错误");
		}
		return "success";
	}

	/**
	 * 7.8 专家取消预约直播
	 */
	public String cancelBespeakLive() throws Exception {

		try {
			LiveVideo liveVideo = liveService.getLiveVideoById(Long.valueOf(id));
			liveVideo.setBespeakStatus("2");
			liveService.updateBespeakStatus(liveVideo);
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS);
		} catch (Exception e) {
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_FAIL);
		}
		return "success";
	}

	/**
	 * 7.9 今日直播
	 */
	@SuppressWarnings("unchecked")
	public String todayLive() {
		LiveVideoListResult result = new LiveVideoListResult();
		String chieflyShow = "1";
		pager = liveService.getTodayLive(pageNo, pageSize, chieflyShow);
		result.setVideos(pager.getResultList());
		result.setRowCount(pager.getRowCount());
		object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, result);
		return "success";
	}

	/**
	 * 7.10 热门视频(chieflyShow(0:更多,1:首页)
	 */
	@SuppressWarnings("unchecked")
	public String getHotVideos() {
		LiveVideoListResult result = new LiveVideoListResult();
		List<LiveVideoListVo3> resultList = new ArrayList<LiveVideoListVo3>();
		if (chieflyShow != null) {
			if (chieflyShow.equals("0")) {// 热门课程查看更多
				if (isSeries != null) {
					pager = liveService.getMoreHotVideos(pageNo, pageSize, isSeries);
					List<LiveVideo> list = pager.getResultList();
					for (LiveVideo liveVideo : list) {
						LiveVideoListVo3 vo = new LiveVideoListVo3();
						Long durTime = 0l;
						// 若为系列课程，页面显示的时长则是子视频的时长的总和
						if (liveVideo.getFatherId() == 0) {
							Long id2 = liveVideo.getId();
							Long[] dd = new Long[] { id2 };
							List<LiveVideo> l = this.liveService.getLiveVideosByFatherId(dd);
							for (LiveVideo liveVideo2 : l) {// 遍历系列下的章节
								Long id3 = liveVideo2.getId();
								Long[] ddd = new Long[] { id3 };
								List<LiveVideo> lll = this.liveService.getLiveVideosByFatherId(ddd);
								if (lll != null) {// 遍历章节下的子视频
									for (LiveVideo liveVideo3 : lll) {
										durTime += liveVideo3.getDuration();
									}
								}
							}
							vo.setDuration(durTime);
						} else {
							vo.setDuration(liveVideo.getDuration());
						}

						ExpenseTotal records = expenseTotalManagerDao.checkBuyStatus(memberId, liveVideo.getId());
						if (records != null) {
							vo.setBuyStatus(1);
						} else {
							vo.setBuyStatus(0);
						}

						vo.setVideoStatus(Integer.valueOf(liveVideo.getVideoStatus()));
						vo.setVideoType(Integer.valueOf(liveVideo.getVideoType()));
						vo.setCover(liveVideo.getCover() == null || liveVideo.getCover().isEmpty() ? ""
								: jointUrl + liveVideo.getCover());
						vo.setFatherId(liveVideo.getFatherId());
						vo.setId(liveVideo.getId());
						vo.setLargeCover(liveVideo.getLargeCover() == null || liveVideo.getLargeCover().isEmpty() ? ""
								: jointUrl + liveVideo.getLargeCover());
						vo.setLinePrice(liveVideo.getLinePrice());
						vo.setPrice(liveVideo.getPrice());
						vo.setTitle(liveVideo.getTitle() == null ? "" : liveVideo.getTitle());
						vo.setViewers(liveVideo.getStudyCount());
						vo.setIsVIPView(liveVideo.getIsVIPView());
						resultList.add(vo);
					}
					result.setVideos(resultList);
					result.setRowCount(pager.getRowCount());
					object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, result);
				} else {
					object = new ResponseBean(Constants.FAIL_CODE_400, Constants.MSG_PARAMETER_EMPTY);
					return "success";
				}
			} else if (chieflyShow.equals("1")) {// 热门课程展示
				pager = liveService.getHotVideosChieflyShow(pageNo, pageSize);
				List<LiveVideo> list = pager.getResultList();
				for (LiveVideo liveVideo : list) {
					LiveVideoListVo3 vo = new LiveVideoListVo3();
					Long durTime = 0l;
					// 若为系列课程，页面显示的时长则是子视频的时长的总和
					if (liveVideo.getFatherId() == 0) {
						Long id2 = liveVideo.getId();
						Long[] dd = new Long[] { id2 };
						List<LiveVideo> l = this.liveService.getLiveVideosByFatherId(dd);
						for (LiveVideo liveVideo2 : l) {// 遍历系列下的章节
							Long id3 = liveVideo2.getId();
							Long[] ddd = new Long[] { id3 };
							List<LiveVideo> lll = this.liveService.getLiveVideosByFatherId(ddd);
							if (lll != null) {// 遍历章节下的子视频
								for (LiveVideo liveVideo3 : lll) {
									durTime += liveVideo3.getDuration() == null ? 0 : liveVideo3.getDuration();
								}
							}
						}
						vo.setDuration(durTime);
					} else {
						vo.setDuration(liveVideo.getDuration());
					}
					ExpenseTotal records = expenseTotalManagerDao.checkBuyStatus(memberId, liveVideo.getId());
					if (records != null) {
						vo.setBuyStatus(1);
					} else {
						vo.setBuyStatus(0);
					}
					vo.setVideoStatus(Integer.valueOf(liveVideo.getVideoStatus()));
					vo.setVideoType(Integer.valueOf(liveVideo.getVideoType()));
					vo.setCover(liveVideo.getCover() == null || liveVideo.getCover().isEmpty() ? ""
							: jointUrl + liveVideo.getCover());
					vo.setFatherId(liveVideo.getFatherId());
					vo.setId(liveVideo.getId());
					vo.setLargeCover(liveVideo.getLargeCover() == null || liveVideo.getLargeCover().isEmpty() ? ""
							: jointUrl + liveVideo.getLargeCover());
					vo.setLinePrice(liveVideo.getLinePrice());
					vo.setPrice(liveVideo.getPrice());
					vo.setTitle(liveVideo.getTitle() == null ? "" : liveVideo.getTitle());
					vo.setViewers(liveVideo.getStudyCount());
					vo.setIsVIPView(liveVideo.getIsVIPView());
					resultList.add(vo);
				}
				result.setVideos(resultList);
				result.setRowCount(pager.getRowCount());
				object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, result);
			}
		} else {
			object = new ResponseBean(Constants.FAIL_CODE_400, Constants.MSG_PARAMETER_EMPTY);
		}
		return "success";
	}

	/**
	 * 7.11 系列课程(chieflyShow(0:更多,1:首页)
	 */
	@SuppressWarnings("unchecked")
	public String getSeriesVideo() {
		LiveVideoListResult result = new LiveVideoListResult();
		List<LiveVideo> videos = new ArrayList<LiveVideo>();
		List<LiveVideoListVo3> resultList = new ArrayList<LiveVideoListVo3>();
		if (chieflyShow != null) {
			if (chieflyShow.equals("0")) {// 查看更多，做分页
				pager = liveService.getMoreSeriesVideos(chieflyShow, pageNo, pageSize);
				List<LiveVideo> list = pager.getResultList();
				for (LiveVideo liveVideo : list) {// 遍历系列
					LiveVideoListVo3 vo = new LiveVideoListVo3();
					Long durTime = 0l;
					Long id2 = liveVideo.getId();
					Long[] dd = new Long[] { id2 };
					List<LiveVideo> l = this.liveService.getLiveVideosByFatherId(dd);
					if (l != null) {
						for (LiveVideo liveVideo2 : l) {// 遍历系列下的章节
							Long id3 = liveVideo2.getId();
							Long[] ddd = new Long[] { id3 };
							List<LiveVideo> lll = this.liveService.getLiveVideosByFatherId(ddd);
							if (lll != null) {// 遍历章节下的子视频
								for (LiveVideo liveVideo3 : lll) {
									durTime += liveVideo3.getDuration();
								}
							}
						}
					}

					ExpenseTotal records = expenseTotalManagerDao.checkBuyStatus(memberId, liveVideo.getId());
					if (records != null) {
						vo.setBuyStatus(1);
					} else {
						vo.setBuyStatus(0);
					}
					vo.setVideoStatus(Integer.valueOf(liveVideo.getVideoStatus()));
					vo.setVideoType(Integer.valueOf(liveVideo.getVideoType()));
					vo.setCover(liveVideo.getCover() == null || liveVideo.getCover().isEmpty() ? ""
							: jointUrl + liveVideo.getCover());
					vo.setDuration(durTime);
					vo.setFatherId(liveVideo.getFatherId());
					vo.setId(liveVideo.getId());
					vo.setMiddleCover(liveVideo.getMiddleCover() == null || liveVideo.getMiddleCover().isEmpty() ? ""
							: jointUrl + liveVideo.getMiddleCover());
					vo.setLinePrice(liveVideo.getLinePrice());
					vo.setPrice(liveVideo.getPrice());
					vo.setTitle(liveVideo.getTitle() == null ? "" : liveVideo.getTitle());
					vo.setViewers(liveVideo.getStudyCount());
					vo.setIsVIPView(liveVideo.getIsVIPView());
					resultList.add(vo);
				}
				result.setVideos(resultList);
				result.setRowCount(pager.getRowCount());
			} else if (chieflyShow.equals("1")) { // 首页
				videos = liveService.seriesVideosChieflyShow(chieflyShow);
				for (LiveVideo liveVideo : videos) {// 遍历系列
					LiveVideoListVo3 vo = new LiveVideoListVo3();
					Long durTime = 0l;
					Long id2 = liveVideo.getId();
					Long[] dd = new Long[] { id2 };
					List<LiveVideo> l = this.liveService.getLiveVideosByFatherId(dd);
					if (l != null) {
						for (LiveVideo liveVideo2 : l) {// 遍历系列下的章节
							Long id3 = liveVideo2.getId();
							Long[] ddd = new Long[] { id3 };
							List<LiveVideo> lll = this.liveService.getLiveVideosByFatherId(ddd);
							if (lll != null) {// 遍历章节下的子视频
								for (LiveVideo liveVideo3 : lll) {
									durTime += liveVideo3.getDuration();
								}
							}
						}
					}
					ExpenseTotal records = expenseTotalManagerDao.checkBuyStatus(memberId, liveVideo.getId());
					if (records != null) {
						vo.setBuyStatus(1);
					} else {
						vo.setBuyStatus(0);
					}
					vo.setVideoStatus(Integer.valueOf(liveVideo.getVideoStatus()));
					vo.setVideoType(Integer.valueOf(liveVideo.getVideoType()));
					vo.setCover(liveVideo.getCover() == null || liveVideo.getCover().isEmpty() ? ""
							: jointUrl + liveVideo.getCover());
					vo.setDuration(durTime);
					vo.setFatherId(liveVideo.getFatherId());
					vo.setId(liveVideo.getId());
					vo.setMiddleCover(liveVideo.getMiddleCover() == null || liveVideo.getMiddleCover().isEmpty() ? ""
							: jointUrl + liveVideo.getMiddleCover());
					vo.setLinePrice(liveVideo.getLinePrice());
					vo.setPrice(liveVideo.getPrice());
					vo.setTitle(liveVideo.getTitle() == null ? "" : liveVideo.getTitle());
					vo.setViewers(liveVideo.getStudyCount());
					vo.setIsVIPView(liveVideo.getIsVIPView());
					resultList.add(vo);
				}
				result.setVideos(resultList);
				result.setRowCount(videos.size());
			}
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, result);
		} else {
			object = new ResponseBean(Constants.FAIL_CODE_400, Constants.MSG_PARAMETER_EMPTY);
		}
		return "success";
	}

	/**
	 * 7.12 获取系列下的子视频
	 */
	public String seriesDetails() {// videoId-书的id，fatherId为0则为系列。
		Member member = memberManagerDao.retrieveMember(memberId);
		if (member != null && member.getAccessToken().equals(accessToken)) {
			if (videoId != null) {
				SeriesVideoVo seriesVideoVo = new SeriesVideoVo();
				List<ChapterVideoVo> chapterVideoVos = new ArrayList<ChapterVideoVo>();
				LiveVideo seriesVideo = liveService.getLiveVideoById(videoId);// 系列
				List<LiveVideo> videos = liveService.getSeriesVideoByFatherID(videoId);// 章节
				if (videos != null) {
					long duraTime = 0L; // 时长
					for (LiveVideo liveVideo : videos) {
						List<LiveVideo> singleVideos = liveService.getSeriesVideoByFatherID(liveVideo.getId());// 子视频
						List<SimpleVideoVo> simpleVideoVos = new ArrayList<>();
						// 单个视频
						if (singleVideos != null) {// 章节视频
							for (LiveVideo singleVideo : singleVideos) {// 遍历章节下面的所有子视频
								SimpleVideoVo simpleVideoVo = new SimpleVideoVo();
								simpleVideoVo.setId(singleVideo.getId());
								simpleVideoVo.setTitle(singleVideo.getTitle());
								if (singleVideo.getVideoType().equals("0")) {
									simpleVideoVo.setVideoUrl(singleVideo.getFlvUrl());
								} else {
									simpleVideoVo.setVideoUrl(singleVideo.getVideoUrl());
								}
								simpleVideoVo.setVideoType(singleVideo.getVideoType());
								simpleVideoVo.setVideoStatus(singleVideo.getVideoStatus());
								duraTime += singleVideo.getDuration(); // 拼接时长
								simpleVideoVos.add(simpleVideoVo);
							}
							ChapterVideoVo chapterVideoVo = new ChapterVideoVo();
							chapterVideoVo.setVideos(simpleVideoVos);
							chapterVideoVo.setTitle(liveVideo.getTitle());
							chapterVideoVo.setPrice(liveVideo.getPrice());
							chapterVideoVo.setId(liveVideo.getId());
							// 查看购买状态
							ExpenseTotal record = expenseTotalManagerDao.checkBuyStatus(memberId, liveVideo.getId());
							ExpenseTotal serRecord = expenseTotalManagerDao.checkBuyStatus(memberId,
									seriesVideo.getId());
							if (serRecord != null) {
								chapterVideoVo.setBuyStatus("1");
							} else {
								if (record != null) {
									chapterVideoVo.setBuyStatus("1");
								} else {
									chapterVideoVo.setBuyStatus("0");
								}
							}
							chapterVideoVo.setIsVIPView(liveVideo.getIsVIPView());
							chapterVideoVos.add(chapterVideoVo);
						}
					}
					seriesVideoVo.setApplyDate(DateHelper.dataToString(seriesVideo.getApplyDate(), "yyyy-MM-dd HH:mm:ss"));
					seriesVideoVo.setShareUrl(seriesCourse + seriesVideo.getId());
					// 根据videoId去查询收藏表，看是否已收藏
					boolean collect = collectManagerDao.getCollect(memberId, videoId, "7");
					if (collect) {
						seriesVideoVo.setCollectStatus("1");// 已收藏
					} else {
						seriesVideoVo.setCollectStatus("0");
					}
					// 查看购买状态
					ExpenseTotal record = expenseTotalManagerDao.checkBuyStatus(memberId, videoId);
					if (record != null) {
						seriesVideoVo.setBuyStatus("1");
					} else {
						seriesVideoVo.setBuyStatus("0");
					}
					String freeImg="";
					//若视频有免单活动，  则要去免单表(live_video_free)查免单图片
					if(seriesVideo.getFreeOrder()==1) {
						FreeVideo order=liveService.getFreeVideo(videoId);
						if(order!=null) {
							if(order.getDeadline().after(new Date())){
								freeImg=order.getImgUrl();
							}
						}
					}
					String photoUrl = seriesVideo.getPhotos();
					if (photoUrl != null && photoUrl.contains(",")) {
						String[] url = photoUrl.split(",");
						StringBuffer sbf = new StringBuffer();
						if (url.length > 0) {
							for (int i = 0; i <= url.length - 1; i++) {
								sbf.append("," + jointUrl + url[i]);
							}
						}
						if(!"".equals(freeImg)) {
							String[] url2 = sbf.toString().replaceFirst(",", freeImg+",").split(",");
							seriesVideoVo.setPhotos(url2);
						}else {
							String[] url2 = sbf.toString().replaceFirst(",", "").split(",");
							seriesVideoVo.setPhotos(url2);
						}
					} else {
						String[] skt = { ("".equals(freeImg)?"":jointUrl+freeImg) +("".equals(photoUrl) || photoUrl.isEmpty()?"":jointUrl+photoUrl)};
						seriesVideoVo.setPhotos(skt);
					}
					seriesVideoVo.setDetailCover(
							seriesVideo.getDetailCover() == null || seriesVideo.getDetailCover().isEmpty() ? ""
									: jointUrl + seriesVideo.getDetailCover()); // 详情页的背景图
					seriesVideoVo.setOnlyWholeBuy(seriesVideo.getOnlyWholeBuy());
					seriesVideoVo.setChapterVideoVos(chapterVideoVos);
					seriesVideoVo.setCover(seriesVideo.getCover() == null || seriesVideo.getCover().isEmpty() ? ""
							: jointUrl + seriesVideo.getCover());
					seriesVideoVo.setDuration(duraTime);
					seriesVideoVo.setIntroduct(seriesVideo.getIntroduct());
					seriesVideoVo.setPrice(seriesVideo.getPrice());
					seriesVideoVo.setLinePrice(seriesVideo.getLinePrice());
					seriesVideoVo.setTitle(seriesVideo.getTitle());
					seriesVideoVo.setIsVIPView(seriesVideo.getIsVIPView());
					if (seriesVideo.getVideoType().equals("0")) {
						seriesVideoVo.setVideoUrl(seriesVideo.getFlvUrl());
					} else {
						seriesVideoVo.setVideoUrl(seriesVideo.getVideoUrl());
					}
					seriesVideoVo.setViewers(seriesVideo.getStudyCount());
				}
				object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, seriesVideoVo);
			} else {
				object = new ResponseBean(Constants.FAIL_CODE_400, Constants.MSG_PARAMETER_EMPTY);
			}
		} else {
			object = new ResponseBean(Constants.FAIL_CODE_400, "登录token错误");
		}
		return "success";
	}

	/**
	 * 7.13 单一直播和单一视频详情
	 */
	public String videoDetails() {
		Member audience = new Member();
		if (memberId > 0) { // memberId为-1,则为游客身份登录, 直接判断,不用再去数据库查询是否有此会员
			audience = memberManagerDao.retrieveMember(memberId);
			if (ObjValid.isValid(audience)) {
				if (!accessToken.equals(audience.getAccessToken())) {
					object = new ResponseBean(Constants.EXCEPTION_CODE_600, Constants.LOGIN_VERIFY_TOKEN_ERROR);
					return "success";
				}
			}
		}
		if (videoId != null) {
			LiveVideo video = liveService.getLiveVideoById(videoId);
			if (video != null) {
				String photoUrl = video.getPhotos();
				VideoDetailVo videoDetailVo = new VideoDetailVo();
				videoDetailVo.setApplyDate(DateHelper.dataToString(video.getApplyDate(), "MM月dd日 HH:mm"));
				videoDetailVo.setCover(video.getCover() == null || video.getCover().isEmpty() ? "" : jointUrl + video.getCover());
				String freeImg="";
				if(video.getFreeOrder()==1) {
					FreeVideo order=liveService.getFreeVideo(videoId);
					if(order!=null) {
						if(order.getDeadline().after(new Date())){
							freeImg=order.getImgUrl();
						}
					}
				}
				if (photoUrl != null && photoUrl.contains(",")) {
					String[] url = photoUrl.split(",");
					StringBuffer sbf = new StringBuffer();
					if (url.length > 0) {
						for (int i = 0; i <= url.length - 1; i++) {
							sbf.append("," + jointUrl + url[i]);
						}
					}
					if(!"".equals(freeImg)) {
						String[] url2 = sbf.toString().replaceFirst(",", freeImg+",").split(",");
						videoDetailVo.setPhotos(url2);
					}else {
						String[] url2 = sbf.toString().replaceFirst(",", "").split(",");
						videoDetailVo.setPhotos(url2);
					}
				} else {
					String[] skt = { ("".equals(freeImg)?"":jointUrl+freeImg) +("".equals(photoUrl) || photoUrl.isEmpty()?"":jointUrl+photoUrl)};
					videoDetailVo.setPhotos(skt);
				}
				videoDetailVo.setDetailCover(video.getDetailCover() == null || video.getDetailCover().isEmpty() ? ""
						: jointUrl + video.getDetailCover()); // 详情页的背景图
				videoDetailVo.setId(String.valueOf(video.getId()));
				videoDetailVo.setDuration(video.getDuration());
				videoDetailVo.setIntroduct(video.getIntroduct());
				videoDetailVo.setPrice(video.getPrice());
				videoDetailVo.setLinePrice(video.getLinePrice());
				videoDetailVo.setTitle(video.getTitle());
				videoDetailVo.setExpertId(video.getExpertId());
				videoDetailVo.setFlvUrl(video.getFlvUrl());
				videoDetailVo.setHlsUrl(video.getHlsUrl());
				videoDetailVo.setRtmpUrl(video.getRtmpUrl());
				videoDetailVo.setVideoType(video.getVideoType());
				videoDetailVo.setVideoStatus(video.getVideoStatus());
				videoDetailVo.setIsVIPView(video.getIsVIPView());
				if (video.getVideoType().equals("0")) {
					 videoDetailVo.setShareURL(singleLive + video.getId());
					 videoDetailVo.setVideoUrl(video.getFlvUrl());
				} else {
					videoDetailVo.setShareURL(singleCourse + video.getId());
					videoDetailVo.setVideoUrl(video.getVideoUrl());
				}
				videoDetailVo.setViewers(video.getStudyCount());
				// 根据videoId去查询收藏表，看是否已收藏
				Collect collect = collectManagerDao.getCollect(memberId, videoId);
				if (collect != null) {
					videoDetailVo.setCollectStatus("1");// 已收藏
				} else {
					videoDetailVo.setCollectStatus("0");
				}
				// 查看购买状态
				ExpenseTotal record = expenseTotalManagerDao.checkBuyStatus(memberId, videoId);
				if (record != null) {
					videoDetailVo.setBuyStatus("1");
				} else {
					videoDetailVo.setBuyStatus("0");
				}
				// 直播状态 0-直播前 1-直播中
			/*	if (video.getVideoType().equals("0")) {// 直播
					if (video.getApplyDate().before(new Date())) {
						videoDetailVo.setLiveStatus("1");
					} else {
						videoDetailVo.setLiveStatus("0");
					}
				} else { // 需求改动,所以加了这个字段
					videoDetailVo.setLiveStatus(video.getVideoStatus());
				}*/
				videoDetailVo.setLiveStatus(video.getVideoStatus());
				object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, videoDetailVo);
			} else {
				object = new ResponseBean(Constants.FAIL_CODE_400, Constants.MSG_FIND_NULL);
			}
		} else {
			object = new ResponseBean(Constants.FAIL_CODE_400, Constants.MSG_PARAMETER_EMPTY);
		}
		return "success";
	}

	/**
	 * 7.14 销毁聊天室
	 */
	public String destroyChat() {
		try {
			String userSig = "eJx10EtPg0AUBeA9v2IyW4wdWsoUd2ghmcRHHyoZNhPKTOW2ymMYW0rjf1fbJrLxbs*XnJx7tBBC*Pl*eZ1mWflZGGEOlcLoBmFnMvR9fPUHqgqkSI0YaXkGLiHE9YhHe0q1FWgl0rVR*qzG1JuQ3*spkKowsIaL0dBC0YsbuRWnuv97Gng7hQ8hv2Pz6WA36JJ6O7PtcrTYRIvVKgATd9R5z-O9mbXNklH*9FqXTcDygEFdJ5l83McQ8jAqszD2D1xHG2InvHtpu9tE29M5Z86uV2ng4-KZMfF-Ng1diq0v6xteXlmC";
			String method = "https://console.tim.qq.com/v4/group_open_http_svc/destroy_group";
			String groupUrl = method + "?userSig=" + userSig
					+ "&identifier=rixin&sdkappid=1400046067&apn=1&contenttype=json";
			JSONObject jsonParam = new JSONObject();
			jsonParam.put("GroupId", "weikai_" + expertId);
			String str = GetJsonData.getJsonData(jsonParam, groupUrl);
			JSONObject dataJson = new JSONObject(str);
			String ActionStatus = dataJson.getString("ActionStatus");
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, ActionStatus);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
	}

	/**
	 * 7.16 聊天室发送系统通知
	 */
	public String sendSysNoti() {
		System.out.println("进入weikai_server"+groupId+id);
		String method = "https://console.tim.qq.com/v4/group_open_http_svc/send_group_system_notification";
		String sendUrl = method + "?userSig=" + userSig
				+ "&identifier=admin&sdkappid=1400049670&apn=1&contenttype=json";
		JSONObject jsonParam = new JSONObject();
		String[] account = {};// 空数组代表全部
		jsonParam.put("GroupId", groupId);
		jsonParam.put("ToMembers_Account", account);
		LiveVideo video = liveService.sendSysNoti(Long.valueOf(id));
		if (video.getIsSilence() == 1) {
			jsonParam.put("Content", "{\"action\":\"shutup\"}");
		} else {
			jsonParam.put("Content", "{\"action\":\"chat\"}");
		}
		String str = GetJsonData.getJsonData(jsonParam, sendUrl);
		JSONObject dataJson = new JSONObject(str);
		Integer errorCode = dataJson.getInt("ErrorCode");
		if (errorCode == 0) {
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, "");
		} else {
			object = new ResponseBean(Constants.FAIL_CODE_400, Constants.MSG_FAIL, "");
		}
		return "success";
	}

	/**
	 * 7.17 专家结束直播
	 */
	public String stopLive() {
		try {
			LiveVideo liveVideo = liveService.getLiveVideoById(Long.valueOf(id));
			StringBuffer param = new StringBuffer();
			String stream_id = "12009_" + liveVideo.getExpertId();
			String url = "http://fcgi.video.qcloud.com/common_access";
			param.append("appid=1253855950&interface=Live_Channel_SetStatus");
			param.append("&Param.s.channel_id=").append(stream_id);
			int t = (int) (System.currentTimeMillis() / 1000);
			String sign = MD5.GetMD5Code("bc2373dcd14c6c7eccb6a5586a10827f");
			param.append("&Param.n.status=2&t=").append(t);
			param.append("&sign=").append(sign);
			if (liveVideo != null) {
				liveVideo.setBespeakStatus("4");
				liveVideo.setIsHot("1");
				liveVideo.setOnlyWholeBuy("0");
				liveService.updateBespeakStatus(liveVideo);
			}
			HttpRequest.sendGet(url, param.toString());

			/*
			 * 结束之后给客户端发送一个isFinish的系统消息
			 */
			String method = "https://console.tim.qq.com/v4/group_open_http_svc/send_group_system_notification";
			String sendUrl = method + "?userSig=" + userSig
					+ "&identifier=admin&sdkappid=1400049670&apn=1&contenttype=json";
			JSONObject jsonParam = new JSONObject();
			String[] account = {};// 空数组代表全部
			String chatRoomId = "weikai_" + liveVideo.getExpertId();
			jsonParam.put("GroupId", chatRoomId);
			jsonParam.put("ToMembers_Account", account);
			jsonParam.put("Content", "{\"action\":\"finish\"}");
			String str = GetJsonData.getJsonData(jsonParam, sendUrl);
			JSONObject dataJson = new JSONObject(str);
			dataJson.getInt("ErrorCode");
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, "");
		} catch (Exception e) {
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_FAIL);
		}
		return "success";
	}

	/**
	 * 7.18 他的视频课程(专家)
	 * 
	 */
	public String getCourseByExpertId() {
		Member member = memberManagerDao.retrieveMember(memberId);
		if (member != null && member.getAccessToken().equals(accessToken)) {
			List<ExpertCourseVo> expertCourseVos = liveService.getCourse(memberId, pageNo, pageSize);
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, expertCourseVos);
		} else {
			object = new ResponseBean(Constants.EXCEPTION_CODE_600, Constants.LOGIN_VERIFY_TOKEN_ERROR);
		}
		return "success";
	}

	/**
	 * 5.11 首页专题详情
	 */
	@SuppressWarnings("unchecked")
	public String getSubjectDetail() {
		LiveVideoListResult2 resultList = new LiveVideoListResult2();
		LiveVideoSubject entity = liveService.getSubject(subjectId);
		Pager pager = liveService.getSubjectDetail(pageNo, pageSize, subjectId);
		resultList.setLiveVideoList(pager.getResultList());
		if (entity.getDetailCover() != null && !"".equals(entity.getDetailCover())) {
			resultList.setSubjectDetailCover(jointUrl + entity.getDetailCover());
		} else {
			resultList.setSubjectDetailCover("");
		}
		resultList.setRowCount(pager.getRowCount());
		object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, resultList);
		return "success";
	}

	/**
	 * 7.19 单一直播和单一视频详情(H5专用)
	 */
	public String videoDetailsH5() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("textml;charset=UTF-8");
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		response.setHeader("Access-Control-Max-Age", "0");
		response.setHeader("Access-Control-Allow-Headers",
				"Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("XDomainRequestAllowed", "1");
		Member audience = new Member();
		if (memberId > 0) { // memberId为-1,则为游客身份登录, 直接判断,不用再去数据库查询是否有此会员
			audience = memberManagerDao.retrieveMember(memberId);
			if (ObjValid.isValid(audience)) {
				if (!accessToken.equals(audience.getAccessToken())) {
					object = new ResponseBean(Constants.EXCEPTION_CODE_600, Constants.LOGIN_VERIFY_TOKEN_ERROR);
					return "success";
				}
			}
		}
		if (videoId != null) {
			LiveVideo video = liveService.getLiveVideoById(videoId);
			if (video != null) {
				String photoUrl = video.getPhotos();
				VideoDetailVo videoDetailVo = new VideoDetailVo();
				videoDetailVo.setApplyDate(DateHelper.dataToString(video.getApplyDate(), "yyyy-MM-dd HH:mm:ss"));
				videoDetailVo.setCover(video.getCover() == null || video.getCover().isEmpty() ? "" : jointUrl + video.getCover());
				String freeImg="";
				//若视频有免单活动，  则要去免单表(live_video_free)查免单图片
				if(video.getFreeOrder()==1) {
					FreeVideo order=liveService.getFreeVideo(videoId);
					if(order!=null) {
						if(order.getDeadline().after(new Date())){
							freeImg=order.getImgUrl();
						}
					}
				}
				if (photoUrl != null && photoUrl.contains(",")) {
					String[] url = photoUrl.split(",");
					StringBuffer sbf = new StringBuffer();
					if (url.length > 0) {
						if(!"".equals(freeImg)){
							sbf.append(","+jointUrl+freeImg);
						}
						for (int i = 0; i <= url.length - 1; i++) {
							sbf.append("," + jointUrl + url[i]);
						}
					}
					String[] url2 = sbf.toString().replaceFirst(",", "").split(",");
					videoDetailVo.setPhotos(url2);
				} else {
					String[] skt = { ("".equals(freeImg)?"":jointUrl+freeImg) +("".equals(photoUrl) || photoUrl.isEmpty()?"":jointUrl+photoUrl)};
					videoDetailVo.setPhotos(skt);
				}
				videoDetailVo.setDetailCover(video.getDetailCover() == null || video.getDetailCover().isEmpty() ? ""
						: jointUrl + video.getDetailCover()); // 详情页的背景图
				videoDetailVo.setId(String.valueOf(video.getId()));
				videoDetailVo.setDuration(video.getDuration());
				videoDetailVo.setIntroduct(video.getIntroduct());
				videoDetailVo.setPrice(video.getPrice());
				videoDetailVo.setLinePrice(video.getLinePrice());
				videoDetailVo.setTitle(video.getTitle());
				videoDetailVo.setExpertId(video.getExpertId());
				videoDetailVo.setFlvUrl(video.getFlvUrl());
				videoDetailVo.setHlsUrl(video.getHlsUrl());
				videoDetailVo.setRtmpUrl(video.getRtmpUrl());
				if (video.getPrice().compareTo(new BigDecimal(0)) == 0) {
					videoDetailVo.setVideoUrl(video.getVideoUrl());
				} else {
					videoDetailVo.setVideoUrl("");
				}
				videoDetailVo.setVideoType(video.getVideoType());
				videoDetailVo.setVideoStatus(video.getVideoStatus());
				if (video.getVideoType().equals("0")) {
					 videoDetailVo.setShareURL(singleLive + video.getId());
				} else {
					 videoDetailVo.setShareURL(singleCourse + video.getId());
				}
				videoDetailVo.setViewers(video.getStudyCount());

				// 根据videoId去查询收藏表，看是否已收藏
				Collect collect = collectManagerDao.getCollect(memberId, videoId);
				if (collect != null) {
					videoDetailVo.setCollectStatus("1");// 已收藏
				} else {
					videoDetailVo.setCollectStatus("0");
				}
				// 查看购买状态
				ExpenseTotal record = expenseTotalManagerDao.checkBuyStatus(memberId, videoId);
				if (record != null) {
					videoDetailVo.setBuyStatus("1");
				} else {
					videoDetailVo.setBuyStatus("0");
				}
				// 直播状态 0-直播前 1-直播中
				if (video.getVideoType().equals("0")) {// 直播
					if (video.getApplyDate().before(new Date())) {
						videoDetailVo.setLiveStatus("1");
					} else {
						videoDetailVo.setLiveStatus("0");
					}
				}
				if (video.getFreeOrder() == 1) {
					videoDetailVo.setFreeOrder(true);
				} else {
					videoDetailVo.setFreeOrder(false);
				}
				object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, videoDetailVo);
			} else {
				object = new ResponseBean(Constants.FAIL_CODE_400, Constants.MSG_FIND_NULL);
			}
		} else {
			object = new ResponseBean(Constants.FAIL_CODE_400, Constants.MSG_PARAMETER_EMPTY);
		}
		return "success";
	}

	/**
	 * 7.20 获取课程人气榜
	 */
	public String getPopularList() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("textml;charset=UTF-8");
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		response.setHeader("Access-Control-Max-Age", "0");
		response.setHeader("Access-Control-Allow-Headers",
				"Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("XDomainRequestAllowed", "1");
		PopularListResult resultList = liveService.getPopularList(pageNo, pageSize, videoId, memberId);
		object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, resultList);
		return "success";
	}

	/**
	 * 7.21 获取视频的PPT
	 */
	public String getVideoAdjuct() {
		LiveVideoVo2 resultList = liveService.getVideoAdjunct(videoId);
		object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, resultList);
		return "success";
	}

	/**
	 * 7.22 视频课程首页
	 */
	public String getVideoCourse() {

		return "";
	}

	/**
	 * 7.23 视频课程查看更多
	 */
	public String getVideoCourseMore() {

		return "";
	}

	/**
	 * 7.24 视频课程详情
	 */
	public String getVideoCourseDetail() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("textml;charset=UTF-8");
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		response.setHeader("Access-Control-Max-Age", "0");
		response.setHeader("Access-Control-Allow-Headers",
				"Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("XDomainRequestAllowed", "1");
		Member audience = new Member();
		if (memberId > 0) { // memberId为-1,则为游客身份登录, 直接判断,不用再去数据库查询是否有此会员
			audience = memberManagerDao.retrieveMember(memberId);
			if (ObjValid.isValid(audience)) {
				if (!accessToken.equals(audience.getAccessToken())) {
					object = new ResponseBean(Constants.EXCEPTION_CODE_600, Constants.LOGIN_VERIFY_TOKEN_ERROR);
					return "success";
				}
			}
		}
		LiveVideoVo3 result = new LiveVideoVo3();
		if (port.equals(appAccessor)) {
			result = liveService.getVideoCourseDetail(memberId, videoId, "app");
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, result);
		} else if (port.equals(webAccessor)) {
			result = liveService.getVideoCourseDetail(memberId, videoId, "web");
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, result);
		} else {
			object = new ResponseBean(Constants.FAIL_CODE_400, Constants.MSG_FIND_FAIL, "port值输入错误");
		}
		return "success";
	}

	/**
	 * 把弹幕内容保存到数据库里
	 * @return
	 * videoId
	 * memberId
	 * content
	 */
	public String saveVideoBullet() {
		if(videoId == null || memberId == null || bulletContent == null || "".equals(bulletContent.trim())) {
			object = new ResponseBean(Constants.FAIL_CODE_400, Constants.MSG_PARAMETER_EMPTY);
		}else {
			LiveVideo video = liveService.getLiveVideoById(videoId);
			Member member = memberManagerDao.getMobileById(memberId);
			if(video == null || member == null) {
				object = new ResponseBean(Constants.FAIL_CODE_400,Constants.MSG_DATA_EXCEPT);
			}else {
				VideoBullet bullet = new VideoBullet();
				bullet.setContent(EmojiParser.parseToAliases(bulletContent));
				bullet.setGmtCreate(new Date());
				bullet.setGmtModified(new Date());
				bullet.setMemberId(memberId);
				bullet.setUsable(1);
				bullet.setVideoId(videoId);
				bullet.setBulletId(bulletId);
				liveService.saveBullet(bullet);
				VideoBullet theNewest = liveService.getNewBullet();
				try {
					JSONObject jsonParam = new JSONObject();
					jsonParam.put("bulletId",theNewest.getId());
					jsonParam.put("content", theNewest.getContent());
					String str=GetJsonData.getJsonData(jsonParam, saveBulletUrl);
					object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.SUCCESS);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return "success";
	}
	
	/**
	 * 回放时拉取弹幕内容
	 * @return
	 * videoId
	 * pageNo
	 * pageSize
	 */
	public String pullBullet() {
		Pager pager = liveService.pullBullet(videoId,pageNo,pageSize);
		List<VideoBullet> videoBullets = pager.getResultList();
		VideoBulletListResult result = new VideoBulletListResult();
		List<VideoBulletVo> bulletVos = new ArrayList<>();
		result.setVideoBulletVos(bulletVos);
		for (VideoBullet videoBullet : videoBullets) {
			VideoBulletVo bulletVo = new VideoBulletVo();
			bulletVo.setContent(EmojiParser.parseToUnicode(videoBullet.getContent()));
			bulletVos.add(bulletVo);
			result.setVideoBulletVos(bulletVos);
		}
		result.setRowCount(pager.getRowCount());
		object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.SUCCESS, result);
		return "success";
	}
	
	
	/**
	 * @return
	 * videoId
	 * pageNo
	 * pageSize
	 */
	public String pullWall() {
		Pager pager = liveService.pullWalls(videoId, pageNo, pageSize);
		List<VideoWall> videoWalls = pager.getResultList();
		VideoWallListResult result = new VideoWallListResult();
		List<VideoWallVo> wallVos = new ArrayList<>();
		int rowCount = pager.getRowCount();
		for (VideoWall videoWall : videoWalls) {
			if(videoWall.getContent() != null) {
				VideoWallVo wallVo = new VideoWallVo();
				wallVo.setContent(EmojiParser.parseToUnicode(videoWall.getContent()));
				wallVo.setWallImg("");
				wallVo.setHeadPhoto(videoWall.getPhoto() == null ? memberImgUrl : jointUrl+videoWall.getPhoto());
				wallVo.setNickName(videoWall.getNickName() == null ? "" : videoWall.getNickName());
				wallVos.add(wallVo);
				rowCount ++;
			} 
			if (videoWall.getReplyContent() != null) {
				VideoWallVo wallVo = new VideoWallVo();
				wallVo.setContent(videoWall.getReplyContent());
				wallVo.setWallImg("");
				wallVo.setHeadPhoto(videoWall.getPhoto() == null ? memberImgUrl : jointUrl+videoWall.getPhoto());
				wallVo.setNickName(videoWall.getNickName() == null ? "" : videoWall.getNickName());
				wallVos.add(wallVo);
				rowCount ++;
			} 
			if (videoWall.getWallImg() != null) {
				VideoWallVo wallVo = new VideoWallVo();
				wallVo.setWallImg(jointUrl+videoWall.getWallImg());
				wallVo.setContent("");
				wallVo.setHeadPhoto(videoWall.getPhoto() == null ? memberImgUrl : jointUrl+videoWall.getPhoto());
				wallVo.setNickName(videoWall.getNickName() == null ? "" : videoWall.getNickName());
				wallVos.add(wallVo);
				rowCount ++;
			} 
			if (videoWall.getReplyImg() != null) {
				VideoWallVo wallVo = new VideoWallVo();
				wallVo.setContent("");
				wallVo.setWallImg(jointUrl+videoWall.getReplyImg());
				wallVo.setHeadPhoto(videoWall.getPhoto() == null ? memberImgUrl : jointUrl+videoWall.getPhoto());
				wallVo.setNickName(videoWall.getNickName() == null ? "" : videoWall.getNickName());
				wallVos.add(wallVo);
				rowCount ++;
			}
			rowCount --;
		}
		result.setRowCount(rowCount);
		result.setWallVos(wallVos);
		object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.SUCCESS, result);
		return "success";
	}
	/////////////////// 以下方法为录播视频评价的方法///////////////////////

	// 录播视频的评分方法(打星)

	public String recordGrade() {

		// 先判断用户是否登录
		Member audience = new Member();
		if (memberId > 0) { // memberId为-1,则为游客身份登录, 直接判断,不用再去数据库查询是否有此会员
			audience = memberManagerDao.retrieveMember(memberId);
			if (ObjValid.isValid(audience)) {
				if (!accessToken.equals(audience.getAccessToken())) {
					object = new ResponseBean(Constants.EXCEPTION_CODE_600, Constants.LOGIN_VERIFY_TOKEN_ERROR);
					return "success";
				}
			}
		}

		// 先查询打星的该视频是否应该有了评价，如果有，就找到这条记录，然后把打星的分数存进去，如果没有，直接生成一个评价记录
		RecordEstimate recordEstimate = this.liveService.getRecordEstimate(memberId, videoId, seriesVideoId);
		
		if (recordEstimate != null) {// 编辑

			recordEstimate.setEditTime(new Date());
			// 计算总评分（打星）

			recordEstimate.setGrade(grade);

			double totalGrade = getTotalGrade(videoId, seriesVideoId);

			// 保证系列下的视频的总评价率一样
			List<RecordEstimate> recordEstimates = this.liveService.getRecordEstimate(videoId.intValue(),seriesVideoId);
			for (RecordEstimate recordEstimate2 : recordEstimates) {
				recordEstimate2.setTotalGrade(totalGrade);
				liveService.updateRecordEstimate(recordEstimate2);
			}

			// recordEstimate.setTotalGrade(totalGrade);

			this.liveService.updateRecordEstimate(recordEstimate);

		} else {// 保存

			RecordEstimate ret = new RecordEstimate();

			ret.setAddTime(new Date());
			ret.setEditTime(new Date());
			ret.setCheckStatus("2"); // 0代表审核不通过，1代表审核通过，2代表未审核
			ret.setIsSeriesRecord("0"); // 1代表为该子视频的评价为系列视频的评价，0为否，默认为0
			ret.setStatus("1"); // 1为启用，0为禁用
			ret.setMemberId(memberId.intValue());
			ret.setVideoId(videoId.intValue());
			ret.setSeriesVideoId(seriesVideoId);

			ret.setGrade(grade);

			// 计算总评分（打星）

			double tg = getTotalGrade(videoId, seriesVideoId);

			// 保证系列下的视频的总评价率一样
			List<RecordEstimate> recordEstimates = this.liveService.getRecordEstimate(videoId.intValue(),seriesVideoId);

			int count = 0;
			
			for (RecordEstimate recordEstimate2 : recordEstimates) {
				if(recordEstimate2.getGrade()!=null && recordEstimate2.getGrade()>0){
					++count;
				}
			}
			
			double totalGrade = (grade + tg * count) / (count + 1);

			for (RecordEstimate recordEstimate2 : recordEstimates) {
				recordEstimate2.setTotalGrade(totalGrade);
				liveService.updateRecordEstimate(recordEstimate2);
			}

			ret.setTotalGrade(totalGrade);

			// 根据用户id获取用户昵称 与头像

			Member member = memberManagerDao.getMobileById(memberId);
			String memberName = "";
			if(member.getUuid()==null&&member.getMobile()==null){
				memberName = "游客"+memberId;
			}else if(member.getRealname()==null&& member.getMobile()!=null){
				memberName = member.getMobile();
				String substring1 = memberName.substring(0,3);
				String substring2 = memberName.substring(7);
				memberName = substring1+"****"+substring2;
				
			}else{
				memberName = member.getRealname();
			}
			ret.setMemberName(memberName);
			String photo = memberManagerDao.getMobileById(memberId).getPhoto();
			ret.setPhoto(photo);

			// 根据视频id获取视频课程名称

			String videoTitle = liveService.getLiveVideoById(videoId).getTitle();
			ret.setVideoTitle(videoTitle);

			if (seriesVideoId != -1) {
				String seriesVideoTitle = liveService.getLiveVideoById((long) seriesVideoId).getTitle();
				ret.setSeriesVideoTitle(seriesVideoTitle);
			} else {
				ret.setSeriesVideoTitle("单一视频，没有系列标题");
			}

			this.liveService.saveRecordEstimate(ret);
		}

		object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.SUCCESS);
		return "success";
	}

	// 计算总评分（打星）
	private double getTotalGrade(Long videoId, int seriesVideoId) {

		return this.liveService.getSumGrade(videoId.intValue(), seriesVideoId);

	}

	// 录播视频的评价方法(文字)

	public String recordEstimate() {

		// 先判断用户是否登录
		Member audience = new Member();
		if (memberId > 0) { // memberId为-1,则为游客身份登录, 直接判断,不用再去数据库查询是否有此会员
			audience = memberManagerDao.retrieveMember(memberId);
			if (ObjValid.isValid(audience)) {
				if (!accessToken.equals(audience.getAccessToken())) {
					object = new ResponseBean(Constants.EXCEPTION_CODE_600, Constants.LOGIN_VERIFY_TOKEN_ERROR);
					return "success";
				}
			}
		}

		// 先查询打星的该视频是否应该有了评价，如果有，就找到这条记录，然后把打星的分数存进去，如果没有，直接生成一个评价记录
		RecordEstimate recordEstimate = this.liveService.getRecordEstimate(memberId, videoId, seriesVideoId);

		RecordEstimateVo result = new RecordEstimateVo();

		if (recordEstimate != null) {// 编辑

			recordEstimate.setContent(content);

			recordEstimate.setContentTime(new Date());

			recordEstimate.setEditTime(new Date());

			String photo = memberManagerDao.getMobileById(memberId).getPhoto();

			Integer id2 = recordEstimate.getId();

			this.liveService.updateRecordEstimate(recordEstimate);

			RecordEstimate ret = this.liveService.getRecordEstimate(id2);

			result.setContent(ret.getContent());
			result.setContentTime(DateHelper.dataToString(ret.getContentTime(), "yyyy-MM-dd"));
			result.setMemberName(ret.getMemberName());
			result.setPhoto(photo);
			result.setReply(ret.getReply());
			result.setReplyTime(DateHelper.dataToString(ret.getReplyTime(), "yyyy-MM-dd"));
			result.setTotalGradel(ret.getTotalGrade() == null ? "" : ret.getTotalGrade().toString());

		} else {// 保存

			RecordEstimate ret = new RecordEstimate();

			ret.setMemberId(memberId.intValue());
			ret.setVideoId(videoId.intValue());
			ret.setSeriesVideoId(seriesVideoId);

			ret.setContentTime(new Date());
			ret.setAddTime(new Date());
			ret.setEditTime(new Date());
			ret.setCheckStatus("2"); // 0代表审核不通过，1代表审核通过，2代表未审核
			ret.setIsSeriesRecord("0"); // 1代表为该子视频的评价为系列视频的评价，0为否，默认为0
			ret.setStatus("1"); // 1为启用，0为禁用

			// 根据用户id获取用户昵称

			Member member = memberManagerDao.getMobileById(memberId);
			String memberName = "";
			if(member.getUuid()==null&&member.getMobile()==null){
				memberName = "游客"+memberId;
			}else if(member.getRealname()==null&& member.getMobile()!=null){
				memberName = member.getMobile();
				String substring1 = memberName.substring(0,3);
				String substring2 = memberName.substring(7);
				memberName = substring1+"****"+substring2;
				
			}else{
				memberName = member.getRealname();
			}

			String photo = memberManagerDao.getMobileById(memberId).getPhoto();

			ret.setMemberName(memberName);
			ret.setPhoto(photo);
			ret.setContent(content);

			// 根据视频id获取视频课程名称
			String videoTitle = liveService.getLiveVideoById(videoId).getTitle();
			ret.setVideoTitle(videoTitle);

			if (seriesVideoId != -1) {
				String seriesVideoTitle = liveService.getLiveVideoById((long) seriesVideoId).getTitle();
				ret.setSeriesVideoTitle(seriesVideoTitle);
			} else {
				ret.setSeriesVideoTitle("单一视频，没有系列标题");
			}

			int id = this.liveService.saveRecordEstimate(ret);

			ret = this.liveService.getRecordEstimate(id);

			result.setContent(ret.getContent());
			result.setContentTime(DateHelper.dataToString(ret.getContentTime(), "yyyy-MM-dd"));
			result.setMemberName(ret.getMemberName());
			result.setPhoto(photo);
			result.setReply(ret.getReply());
			result.setReplyTime(DateHelper.dataToString(ret.getReplyTime(), "yyyy-MM-dd"));
			result.setTotalGradel(ret.getTotalGrade() == null ? "" : ret.getTotalGrade().toString());

		}

		object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.SUCCESS, result);
		return "success";

	}
	// 获取视频评价的内容
	@SuppressWarnings("unchecked")
	public String getRecordEstimateList() {
		Pager pager = liveService.getRecordEstimate(memberId,videoId,pageSize, pageNo);
		List<RecordEstimateVo> recordEstimateVos = pager.getResultList();
		
		RecordEstimateVoList result = new RecordEstimateVoList();
 
		double totalGrade =3.0;//先设定为好评
		
		if(recordEstimateVos.size()<1){
			totalGrade = getTotalGrade(videoId,0);
			if(totalGrade==0.0){
				totalGrade =3.0;
			}
		}else{
			
			totalGrade = Double.parseDouble(recordEstimateVos.get(0).getTotalGradel()==""? "3" : recordEstimateVos.get(0).getTotalGradel());
		}
		
		if (totalGrade >= 3.0) {
			result.setTotalGrade("100%");
		} else {
			result.setTotalGrade(Math.round((totalGrade / 3.0) * 100) + "%");
		}

		result.setRecordEstimateVos(recordEstimateVos);

		result.setRowCount(pager.getRowCount());

		object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.SUCCESS, result);

		return "success";

	}
	
	
	// 判断视频是否已经评价 或 已经打星
	
	public String isEstimateOrGrade(){
			
		EstimateOrGradeVo result = new EstimateOrGradeVo();
		
		 RecordEstimate rd = this.liveService.getRecordEstimate(memberId, videoId, seriesVideoId);
		 if(rd!=null){
			 
			 if(StringUtils.isNotBlank(rd.getContent())){
				 result.setIsEstimate("1"); 
			 }else{
				 result.setIsEstimate("0");
			 }
			 
			 if(rd.getGrade()==null || rd.getGrade()<0){
				 result.setIsGrade("0");
			 }else{
				 result.setIsGrade("1");
			 }
			 
		 }else{
			 result.setIsEstimate("0");
			 result.setIsGrade("0");
		 }
		
		object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.SUCCESS, result);
		
		return "success";
	}
	
	
	/////////////////// 各种getter/setter方法///////////////////////
 	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public LiveVideo getLiveVideo() {
		return liveVideo;
	}

	public void setLiveVideo(LiveVideo liveVideo) {
		this.liveVideo = liveVideo;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIntroduct() {
		return introduct;
	}

	public void setIntroduct(String introduct) {
		this.introduct = introduct;
	}

	public String getTerminal() {
		return terminal;
	}

	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}

	public String getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(String applyDate) {
		this.applyDate = applyDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getExpertName() {
		return expertName;
	}

	public void setExpertName(String expertName) {
		this.expertName = expertName;
	}

	public String getExpertId() {
		return expertId;
	}

	public void setExpertId(String expertId) {
		this.expertId = expertId;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getVideoType() {
		return videoType;
	}

	public void setVideoType(String videoType) {
		this.videoType = videoType;
	}

	public Long getVideoId() {
		return videoId;
	}

	public void setVideoId(Long videoId) {
		this.videoId = videoId;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public HttpSession getSession() {
		return session;
	}

	public void setSession(HttpSession session) {
		this.session = session;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public String getBespeakType() {
		return bespeakType;
	}

	public void setBespeakType(String bespeakType) {
		this.bespeakType = bespeakType;
	}

	public String getChieflyShow() {
		return chieflyShow;
	}

	public void setChieflyShow(String chieflyShow) {
		this.chieflyShow = chieflyShow;
	}

	public String getIsSeries() {
		return isSeries;
	}

	public void setIsSeries(String isSeries) {
		this.isSeries = isSeries;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public static String getUserSig() {
		return userSig;
	}

	public static void setUserSig(String userSig) {
		LiveAction.userSig = userSig;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public Integer getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Integer subjectId) {
		this.subjectId = subjectId;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getAppearTime() {
		return appearTime;
	}

	public void setAppearTime(Date appearTime) {
		this.appearTime = appearTime;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public int getSeriesVideoId() {
		return seriesVideoId;
	}

	public void setSeriesVideoId(int seriesVideoId) {
		this.seriesVideoId = seriesVideoId;
	}
	public RecordEstimate getRecordEstimate() {
		return recordEstimate;
	}
	public void setRecordEstimate(RecordEstimate recordEstimate) {
		this.recordEstimate = recordEstimate;
	}
	
	public String getBulletContent() {
		return bulletContent;
	}

	public void setBulletContent(String bulletContent) {
		this.bulletContent = bulletContent;
	}

	public String getBulletId() {
		return bulletId;
	}

	public void setBulletId(String bulletId) {
		this.bulletId = bulletId;
	}
	
}
