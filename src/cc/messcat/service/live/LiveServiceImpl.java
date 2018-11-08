package cc.messcat.service.live;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import cc.modules.util.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;
import com.baidubce.services.doc.model.GetDocumentResponse;
import com.mipush.MiPushHelper;
import cc.messcat.bases.BaseManagerDaoImpl;
import cc.messcat.entity.Attention;
import cc.messcat.entity.ExpenseTotal;
import cc.messcat.entity.ChatRoomAuth;
import cc.messcat.entity.Coupn;
import cc.messcat.entity.FreeVideo;
import cc.messcat.entity.InviteStatis;
import cc.messcat.entity.LiveVideo;
import cc.messcat.entity.LiveVideoDiscount;
import cc.messcat.entity.LiveVideoDistributor;
import cc.messcat.entity.LiveVideoSubject;
import cc.messcat.entity.Member;
import cc.messcat.entity.RecordEstimate;
import cc.messcat.entity.RedeemCodeOwner;
import cc.messcat.entity.SilenceList;
import cc.messcat.entity.SystemMessage;
import cc.messcat.entity.CoupnUser;
import cc.messcat.entity.VideoBullet;
import cc.messcat.entity.ViewerBetweenLive;
import cc.messcat.vo.ExpertCourseVo;
import cc.messcat.vo.LiveOpenVo;
import cc.messcat.vo.LiveVideoListVo;
import cc.messcat.vo.LiveVideoListVo2;
import cc.messcat.vo.LiveVideoSubjectListVo;
import cc.messcat.vo.LiveVideoVo2;
import cc.messcat.vo.LiveVideoVo3;
import cc.messcat.vo.LiveViewVo;
import cc.messcat.vo.LiveVo;
import cc.messcat.vo.PopularListResult;
import cc.messcat.vo.PopularListVo;
import cc.messcat.vo.PopularListVo2;
import cc.messcat.vo.RecordEstimateVo;
import cc.messcat.vo.SimpleVideoVo;
import cc.messcat.vo.SpecialVo;
import cc.modules.commons.Pager;
import cc.modules.util.BDocHelper;
import cc.modules.util.DateHelper;
import cc.modules.util.EmojiUtils;
import cc.modules.util.GetJsonData;
import cc.modules.util.LiveHelper;
import cc.modules.util.PropertiesFileReader;
import cc.modules.util.SmsUtil;
import net.sf.json.JSONObject;

@SuppressWarnings("serial")
public class LiveServiceImpl extends BaseManagerDaoImpl implements LiveService {

	private static String memberUrl = PropertiesFileReader.getByKey("member.photo.url"); // 会员默认头像
	private static String jointUrl = PropertiesFileReader.getByKey("static.domain");// 图片拼接
	private static String singleCourse = PropertiesFileReader.getByKey("single_course"); // 单一课程
	private static String seriesCourse = PropertiesFileReader.getByKey("series_course"); // 系列课程
	private static String singleLive = PropertiesFileReader.getByKey("single_live"); // 单一直播
	private static String apiDoMain = PropertiesFileReader.getByKey("api.domain"); // 邀请免单处的服务器
	private Logger logger = LoggerFactory.getLogger(LiveServiceImpl.class);

	@SuppressWarnings("static-access")
	@Override
	public LiveOpenVo openLive(String id, String expertId) {
		/*
		 * 推流地址 rtmp://12009.livepush.myqcloud.com/live/12009_f1c2998b06?bizid=12009&
		 * txSecret=d013420426d84336f35226eff68996b0&txTime=59EA1D7F
		 */
		LiveOpenVo vo = new LiveOpenVo();
		LiveVideo video = liveDao.getLiveVideoById(Long.valueOf(id));
		String url = "";
		Date now = new Date();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(now);
		calendar.add(calendar.DATE, 1); // 把日期往前减少一天，若想把日期向后推一天则将负数改为正数
		now = calendar.getTime();

		if (video != null) {
			String key = "bd0454bd6bb1bfb6be2bdf7616f8ae59";
			String stream_id = "12009_" + video.getExpertId();
			String url2 = LiveHelper.getSafeUrl(key, stream_id, now.getTime() / 1000);
			url = "rtmp://livepush.cert-map.com/live/12009_" + video.getExpertId() + "?bizid=12009&" + url2;
		}
		vo.setPushUrl(url);
		vo.setTitle(video.getTitle());
		vo.setShareURL(singleLive + video.getId());
		/*
		 * 更新直播详情数据
		 */
		String playUrl = "rtmp://liveplay.cert-map.com/live/12009_";// RTMP播放URL
		String playUrl2 = "http://liveplay.cert-map.com/live/12009_";// FLV和HLS播放地址
		video.setRtmpUrl(playUrl + video.getExpertId());
		video.setFlvUrl(playUrl2 + video.getExpertId() + ".flv");
		video.setHlsUrl(playUrl2 + video.getExpertId() + ".m3u8");
		video.setVideoStatus("1");
		liveDao.update(video);
		return vo;
	}

	@Override
	public LiveViewVo viewLive(Long memberId, String id) {
		LiveViewVo result = new LiveViewVo();
		LiveVideo video = liveDao.getLiveVideoById(Long.valueOf(id));
		if (video != null) {
			Member expert = liveDao.getExpert(String.valueOf(video.getExpertId()));
			String url = "rtmp://liveplay.cert-map.com/live/12009_" + expert.getId();// RTMP播放URL
			String url2 = "http://liveplay.cert-map.com/live/12009_" + expert.getId();// FLV和HLS播放地址
			result.setExpertId(String.valueOf(expert.getId()));
			result.setExpertName(expert.getRealname() == null ? "" : expert.getRealname());
			result.setExpertPhoto(jointUrl + expert.getPhoto());
			Attention attention = liveDao.isAttention(memberId, String.valueOf(video.getExpertId()));
			if (attention != null) {
				result.setIsAttention("1");
			} else {
				result.setIsAttention("0");
			}
			result.setRtmpUrl(StringUtil.isNotBlank(video.getRtmpUrl())?video.getRtmpUrl():url); // rtmp播放地址
			result.setFlvUrl(StringUtil.isNotBlank(video.getFlvUrl())?video.getFlvUrl():url2 + ".flv"); // flv播放地址
			result.setHlsUrl(StringUtil.isNotBlank(video.getHlsUrl())?video.getHlsUrl():url2 + ".m3u8"); // hls播放地址
			result.setGroupId("weikai_" + String.valueOf(video.getExpertId())); // 聊天室房间号
			result.setShareURL(singleLive + id);
			result.setTitle(video.getTitle());
			boolean bok = liveDao.isAdmin(memberId);
			if (bok == true) {
				result.setIsSilenceAuth("1");
			} else {
				result.setIsSilenceAuth("0");
			}
			if (video.getBespeakStatus().equals("4")) {
				result.setIsFinish(1);
			}
			result.setIsSilence(video.getIsSilence().toString());

			String groupId = "weikai_" + video.getExpertId();
			List<SilenceList> silenceLists = liveDao.getSilenceList(groupId);
			List<Long> sList = new ArrayList<>();
			for (SilenceList silenceList2 : silenceLists) {
				sList.add(silenceList2.getMemberId());
			}
			result.setSilenceList(sList);
		}
		return result;
	}

	@Override
	public void update(LiveVideo liveVideos) {
		liveDao.update(liveVideos);
	}

	@Override
	public Pager videoList(int pageNo, int pageSize) {
		List<LiveVideo> videos = liveDao.videoList(pageNo, pageSize);
		if (videos != null) {
			return new Pager(pageSize, pageNo, videos.size(), videos);
		}
		return new Pager();
	}

	// 专家申请预约直播或者专家修改预约直播详情
	@Override
	public Long insertOrUpdate(LiveVideo liveVideo) {
		return liveDao.insertOrUpdate(liveVideo);
	}

	// 查看预约直播的详情
	@Override
	public LiveVideo getLiveVideoById(Long id) {
		LiveVideo liveVideo = liveDao.getLiveVideoById(id);
		return liveVideo;
	}

	// 专家取消预约
	@Override
	public void updateBespeakStatus(LiveVideo liveVideo) {
		liveDao.updateBespeakStatus(liveVideo);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Pager getLiveVideos(String[] bespeakStatus, int pageNo, int pageSize, Long memberId) {
		Pager pager = liveDao.getLiveVideos(bespeakStatus, pageNo, pageSize, memberId);
		List<LiveVideo> list = pager.getResultList();
		List<LiveVideo> resultList = new ArrayList<LiveVideo>();
		if (list != null && list.size() > 0) {
			for (LiveVideo entity : list) {
				entity.setCover(
						entity.getCover() == null || entity.getCover().isEmpty() ? "" : jointUrl + entity.getCover());
				entity.setDetailCover(entity.getDetailCover() == null || entity.getDetailCover().isEmpty() ? ""
						: jointUrl + entity.getDetailCover());
				entity.setMiddleCover(entity.getMiddleCover() == null || entity.getMiddleCover().isEmpty() ? ""
						: jointUrl + entity.getMiddleCover());
				entity.setLargeCover(entity.getLargeCover() == null || entity.getLargeCover().isEmpty() ? ""
						: jointUrl + entity.getLargeCover());
				resultList.add(entity);
			}
		}
		return new Pager(pageSize, pageNo, pager.getRowCount(), resultList);
	}

	@Override
	public void addWatchRecord(ViewerBetweenLive vbLive) {
		liveDao.addWatchRecord(vbLive);
	}

	@Override
	public Long getWatchMark(Long experId) {
		return liveDao.getWatchMark(experId);
	}

	@Override
	public int countViewers(Long videoId) {
		return liveDao.countViewers(videoId);
	}

	@Override
	public LiveVideo getViewers(Long videoId) {
		return liveDao.getViewers(videoId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Pager getTodayLive(int pageNo, int pageSize, String chieflyShow) {
		List<LiveVideo> list = new ArrayList<LiveVideo>();
		List<LiveVideoListVo2> resultList = new ArrayList<LiveVideoListVo2>();
		// 今日直播查看更多(不做推荐)
		if (chieflyShow.equals("0")) {
			Pager pager = liveDao.getTodayLive(pageNo, pageSize, chieflyShow);
			list = pager.getResultList();
			// 今日直播全部做推荐(视频课程模块)
		} else if (chieflyShow.equals("1")) {
			Pager pager = liveDao.getTodayLive(pageNo, pageSize, chieflyShow);
			list = pager.getResultList();
			// 今日直播首页模块显示(部分显示)
		} else if (chieflyShow.equals("2")) {
			Pager pager = liveDao.getTodayLive(pageNo, pageSize, chieflyShow);
			list = pager.getResultList();
		}
		if (list != null) {
			for (LiveVideo entity : list) {
				LiveVideoListVo2 vo = new LiveVideoListVo2();
				vo.setCover(entity.getCarouselCover() == null || entity.getCarouselCover().isEmpty()
						? jointUrl + entity.getCover()
						: jointUrl + entity.getCarouselCover());
				/**
				 * 通过子直播的fatherId找到章节的fatherId, 即为系列Id
				 */
				if (entity.getFatherId() > 0) { // 若fatherId>0,则为章节或章节下的子直播
					LiveVideo chapter = liveDao.searchFatherVideo(entity.getFatherId()); // 找到子直播的章节
					if (entity.getFatherId() > 0) {
						vo.setFatherId(chapter.getFatherId()); // 章节的fatherId即为系列Id
					}
				} else {
					vo.setFatherId(entity.getFatherId());
				}
				vo.setVideoId(entity.getId());
				vo.setPrice(entity.getPrice());
				vo.setTitle(entity.getTitle());
				vo.setAddDate(DateHelper.dataToString(entity.getApplyDate(), "yyyy-MM-dd HH:mm:ss"));
				if (entity.getFatherId() > 0) { // 若fatherId>0,则为章节或章节下的子直播
					LiveVideo chapter = liveDao.searchFatherVideo(entity.getFatherId()); // 找到子直播的章节
					if (entity.getFatherId() > 0) {
						vo.setFatherId(chapter.getFatherId()); // 章节的fatherId即为系列Id
					}
				} else {
					vo.setFatherId(entity.getFatherId());
				}
				vo.setVideoId(entity.getId());
				vo.setPrice(entity.getPrice() == null ? new BigDecimal(0) : entity.getPrice());
				vo.setLinePrice(entity.getLinePrice() == null ? new BigDecimal(0) : entity.getLinePrice());
				vo.setTitle(entity.getTitle());
				vo.setIsVIPView(entity.getIsVIPView());
				resultList.add(vo);
			}

			return new Pager(pageSize, pageNo, list.size(), resultList);
		}
		return new Pager();
	}

	@Override
	public Pager getHotVideosChieflyShow(int pageNo, int pageSize) {
		return liveDao.getHotVideosChieflyShow(pageNo, pageSize);
	}

	@Override
	public Pager getMoreHotVideos(int pageNo, int pageSize, String isSeries) {
		return liveDao.getMoreHotVideos(pageNo, pageSize, isSeries);
	}

	@Override
	public List<LiveVideo> seriesVideosChieflyShow(String chieflyShow) {
		return liveDao.seriesVideosChieflyShow(chieflyShow);
	}

	@Override
	public List<LiveVideo> getSeriesVideoByFatherID(Long videoId) {
		return liveDao.getSeriesVideoByFatherID(videoId);
	}

	@Override
	public Pager getMoreSeriesVideos(String chieflyShow, int pageNo, int pageSize) {
		return liveDao.getMoreSeriesVideos(chieflyShow, pageNo, pageSize);
	}

	@Override
	public List<LiveVideo> getLiveVideosById(Long[] relateId) {
		return liveDao.getLiveVideosById(relateId);
	}

	@Override
	public List<LiveVideo> getLiveVideosByFatherId(Long[] chapterId) {
		return liveDao.getLiveVideosByFatherId(chapterId);
	}

	@Override
	public LiveVideo sendSysNoti(Long videoId) {
		LiveVideo video = liveDao.getLiveVideoById(videoId);
		if (video.getIsSilence() == 1) {
			video.setIsSilence(0);
		} else {
			video.setIsSilence(1);
		}
		liveDao.changeSilence(video);
		LiveVideo changedVideo = liveDao.getLiveVideoById(videoId); // 得到改变状态之后的禁言状态
		return changedVideo;
	}

	@Override
	public LiveVideo afterLiveVideo(Long experId) {
		return liveDao.afterLiveVideo(experId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Pager getSubjectList(int pageNo, int pageSize, String chiefShow) {
		Pager pager = liveDao.getSubjectList(pageNo, pageSize, chiefShow);
		List<LiveVideoSubjectListVo> resultList = new ArrayList<LiveVideoSubjectListVo>();
		List<LiveVideoSubject> list = pager.getResultList();
		if (list != null && list.size() > 0) {
			for (LiveVideoSubject entity : list) {
				LiveVideoSubjectListVo vo = new LiveVideoSubjectListVo();
				vo.setSubjectId(entity.getId());
				vo.setSubjectName(entity.getSubjectName());
				vo.setIntroduct(entity.getIntroduct());
				vo.setSubjectCover(entity.getSubjectCover() == null || entity.getSubjectCover().isEmpty() ? ""
						: jointUrl + entity.getSubjectCover());
				resultList.add(vo);
			}
			return new Pager(pageSize, pageNo, pager.getRowCount(), resultList);
		}
		return new Pager();
	}

	@Override
	public List<LiveVo> getLiveVideBySearchKeyWord(String searchKeyWord) {
		List<LiveVo> lives = new ArrayList<>();
		List<LiveVideo> liveVideos = liveDao.getLiveVideBySearchKeyWord(searchKeyWord);

		if (liveVideos != null && liveVideos.size() > 0) {
			for (LiveVideo liveVideo : liveVideos) {
				LiveVo live = new LiveVo();
				live.setCover(liveVideo.getCover() == null || liveVideo.getCover().isEmpty() ? ""
						: jointUrl + liveVideo.getCover());
				live.setPrice(liveVideo.getPrice());
				live.setLinePrice(liveVideo.getLinePrice());
				live.setStudyCount(liveVideo.getStudyCount());
				live.setTitle(liveVideo.getTitle());
				Long duration = 0l;
				if (liveVideo.getFatherId() != -1l) {// 搜索到不是单一的
					if (liveVideo.getFatherId() == 0l) {// 搜索到的是系列的
						live.setLiveId(liveVideo.getId());
						List<LiveVideo> videosByFatherId = liveDao
								.getLiveVideosByFatherId(new Long[] { liveVideo.getId() });
						for (LiveVideo liveVideo2 : videosByFatherId) {
							List<LiveVideo> videos = liveDao.getLiveVideosByFatherId(new Long[] { liveVideo2.getId() });
							for (LiveVideo liveVideo3 : videos) {
								duration += liveVideo3.getDuration();
							}
						}
					}
					if (liveVideo.getClassify() == 2) {// 搜索到的是章节
						live.setLiveId(liveVideo.getFatherId());
					}
					if (liveVideo.getClassify() == 3) {// 搜索到的是章节下面的子视频

						List<LiveVideo> ll = liveDao.getLiveVideosByFatherId(new Long[] { liveVideo.getId() });
						if (ll != null && ll.size() > 0) {
							LiveVideo l = ll.get(0);
							if (l != null) {
								Long fatherId = l.getFatherId();
								live.setLiveId(fatherId);
							} else {
								live.setFatherId(0l);
							}
						} else {
							live.setFatherId(0l);
						}
					}
					live.setDuration(duration);
					live.setFatherId(0l);
				}
				live.setFatherId(-1l);
				live.setDuration(liveVideo.getDuration());
				live.setLiveId(liveVideo.getId());
				lives.add(live);
				if (liveVideo.getVideoType() == "0") {
					live.setVideoType("0");
				} else {
					live.setVideoType("1");
				}
			}
			return lives;
		} else {
			return new ArrayList<>();
		}

	}

	@Override
	public List<SpecialVo> getSpecialBySearchKeyWord(String searchKeyWord) {
		List<SpecialVo> specialVos = new ArrayList<>();
		List<LiveVideoSubject> liveSubjects = liveDao.getAllLiveSubjectBySearchKeyWord(searchKeyWord);
		for (LiveVideoSubject liveVideoSubject : liveSubjects) {
			SpecialVo specialVo = new SpecialVo();
			specialVo.setCover(
					liveVideoSubject.getSubjectCover() == null || liveVideoSubject.getSubjectCover().isEmpty() ? ""
							: jointUrl + liveVideoSubject.getSubjectCover());
			specialVo.setId(liveVideoSubject.getId());
			specialVos.add(specialVo);
		}
		return specialVos;
	}

	@Override
	public Pager getMoreLiveVideo(String searchKeyWord, int pageNo, int pageSize) {
		List<LiveVo> lives = new ArrayList<>();
		List<LiveVideo> liveVideos = liveDao.getMoreLiveVideo(searchKeyWord, pageNo, pageSize);
		for (LiveVideo liveVideo : liveVideos) {
			LiveVo live = new LiveVo();
			live.setCover(liveVideo.getCover() == null || liveVideo.getCover().isEmpty() ? ""
					: jointUrl + liveVideo.getCover());
			live.setPrice(liveVideo.getPrice());
			live.setLinePrice(liveVideo.getLinePrice());
			live.setStudyCount(liveVideo.getStudyCount());
			live.setTitle(liveVideo.getTitle());
			Long duration = 0l;
			if (liveVideo.getFatherId() != -1l) {// 搜索到不是单一的
				if (liveVideo.getFatherId() == 0l) {// 搜索到的是系列的
					live.setLiveId(liveVideo.getId());
					List<LiveVideo> videosByFatherId = liveDao
							.getLiveVideosByFatherId(new Long[] { liveVideo.getId() });
					for (LiveVideo liveVideo2 : videosByFatherId) {
						List<LiveVideo> videos = liveDao.getLiveVideosByFatherId(new Long[] { liveVideo2.getId() });
						for (LiveVideo liveVideo3 : videos) {
							duration += liveVideo3.getDuration();
						}
					}
				}
				if (liveVideo.getClassify() == 2) {// 搜索到的是章节
					live.setLiveId(liveVideo.getFatherId());
				}
				if (liveVideo.getClassify() == 3) {// 搜索到的是章节下面的子视频

					List<LiveVideo> ll = liveDao.getLiveVideosByFatherId(new Long[] { liveVideo.getId() });
					if (ll != null && ll.size() > 0) {
						LiveVideo l = ll.get(0);
						if (l != null) {
							Long fatherId = l.getFatherId();
							live.setLiveId(fatherId);
						} else {
							live.setFatherId(0l);
						}
					} else {
						live.setFatherId(0l);
					}
				}
				live.setDuration(duration);
				live.setFatherId(0l);
			}
			live.setFatherId(-1l);
			live.setDuration(liveVideo.getDuration());
			live.setLiveId(liveVideo.getId());
			lives.add(live);
			if (liveVideo.getVideoType() == "0") {
				live.setVideoType("0");
			} else {
				live.setVideoType("1");
			}
		}
		return new Pager(pageSize, pageNo, lives.size(), lives);
	}

	@Override
	public Pager getMoreSpecial(String searchKeyWord, int pageNo, int pageSize) {
		List<SpecialVo> specialVos = new ArrayList<>();
		List<LiveVideoSubject> liveSubjects = liveDao.getMoreLiveSubject(searchKeyWord, pageNo, pageSize);
		for (LiveVideoSubject liveVideoSubject : liveSubjects) {
			SpecialVo specialVo = new SpecialVo();
			specialVo.setCover(
					liveVideoSubject.getSubjectCover() == null || liveVideoSubject.getSubjectCover().isEmpty() ? ""
							: jointUrl + liveVideoSubject.getSubjectCover());
			specialVo.setId(liveVideoSubject.getId());
			specialVos.add(specialVo);
		}
		return new Pager(pageSize, pageNo, specialVos.size(), specialVos);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Pager getExcellentCourse(int pageNo, int pageSize, String chiefShow, Member member) {
		Pager pager = liveDao.getExcellentCourse(pageNo, pageSize, chiefShow);
		List<LiveVideoListVo> resultList = new ArrayList<LiveVideoListVo>();
		List<LiveVideo> list = pager.getResultList();
		if (list != null && list.size() > 0) {
			for (LiveVideo entity : list) {
				LiveVideoListVo vo = new LiveVideoListVo();
				vo.setCover(
						entity.getCover() == null || entity.getCover().isEmpty() ? "" : jointUrl + entity.getCover());
				vo.setLargeCover(entity.getLargeCover() == null || entity.getLargeCover().isEmpty()
						? jointUrl + entity.getCover()
						: jointUrl + entity.getLargeCover());
				/*
				 * 若为系列视频,时长为各子视频时长的总和
				 */
				Long durTime = 0l;
				if (entity.getFatherId() == 0) {
					Long id2 = entity.getId();
					Long[] dd = new Long[] { id2 };
					List<LiveVideo> l = liveDao.getLiveVideosByFatherId(dd);
					for (LiveVideo liveVideo2 : l) {// 遍历系列下的章节
						Long id3 = liveVideo2.getId();
						Long[] ddd = new Long[] { id3 };
						List<LiveVideo> lll = liveDao.getLiveVideosByFatherId(ddd);
						if (lll != null) {// 遍历章节下的子视频
							for (LiveVideo liveVideo3 : lll) {
								durTime += liveVideo3.getDuration() == null ? 0 : liveVideo3.getDuration();
							}
						}
					}
					vo.setDuration(durTime);
				} else {
					vo.setDuration(entity.getDuration());
				}
				vo.setId(entity.getId());
				vo.setFatherId(entity.getFatherId());
				vo.setVideoId(entity.getId());
				vo.setPrice(entity.getPrice());
				vo.setVideoType(entity.getVideoType());
				vo.setLinePrice(entity.getLinePrice() == null ? new BigDecimal(0) : entity.getLinePrice());
				vo.setTitle(entity.getTitle());
				vo.setViewers(entity.getStudyCount()); // 字段名应该取数据库的study_count
				vo.setIsVIPView(entity.getIsVIPView());
				ExpenseTotal buys = liveDao.getBuyStatus(member.getId(), entity.getId());
				if (buys != null) {
					vo.setBuyStatus(1);
				} else {
					vo.setBuyStatus(0);
				}
				resultList.add(vo);
			}
			return new Pager(pageSize, pageNo, pager.getRowCount(), resultList);
		}
		return new Pager();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ExpertCourseVo> getCourse(Long memberId, int pageNo, int pageSize) {
		Pager pager = liveDao.getCourse(memberId, pageNo, pageSize);
		List<LiveVideo> videos = pager.getResultList();
		List<ExpertCourseVo> expertCourseVos = new ArrayList<>();
		if (pager != null) {
			for (LiveVideo video : videos) {
				ExpertCourseVo ev = new ExpertCourseVo();
				if (video.getFatherId() == -1L || video.getFatherId() == 0L) {
					ev.setId(video.getId());
					ev.setCover(jointUrl + video.getCover());
					ev.setDuration(video.getDuration().intValue());
					ev.setPrice(video.getPrice());
					ev.setStudyCount(video.getStudyCount().intValue());
					ev.setTitle(video.getTitle());
					expertCourseVos.add(ev);
				}
			}
		}
		return expertCourseVos;
	}

	/**
	 * 7.19 首页专题详情
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Pager getSubjectDetail(int pageNo, int pageSize, Integer subjectId) {
		Pager pager = liveDao.getSubjectDetail(pageNo, pageSize, subjectId);
		List<LiveVideoListVo> resultList = new ArrayList<LiveVideoListVo>();
		List<LiveVideo> list = pager.getResultList();
		for (LiveVideo entity : list) {
			LiveVideoListVo vo = new LiveVideoListVo();
			vo.setCover(entity.getCover() == null || entity.getCover().isEmpty() ? "" : jointUrl + entity.getCover());
			vo.setLargeCover(
					entity.getLargeCover() == null || entity.getLargeCover().isEmpty() ? jointUrl + entity.getCover()
							: jointUrl + entity.getLargeCover());
			Long durTime = 0l;
			// 若fatherId为0则为系列,时长这各子视频的总和
			if (entity.getFatherId() == 0) {
				Long id2 = entity.getId();
				Long[] dd = new Long[] { id2 };
				List<LiveVideo> l = this.liveDao.getLiveVideosByFatherId(dd);
				for (LiveVideo liveVideo2 : l) {// 遍历系列下的章节
					Long id3 = liveVideo2.getId();
					Long[] ddd = new Long[] { id3 };
					List<LiveVideo> lll = this.liveDao.getLiveVideosByFatherId(ddd);
					if (lll != null) {// 遍历章节下的子视频
						for (LiveVideo liveVideo3 : lll) {
							durTime += liveVideo3.getDuration() == null ? 0 : liveVideo3.getDuration();
						}
					}
				}
				vo.setDuration(durTime);
			} else {
				vo.setDuration(entity.getDuration());
			}
			vo.setFatherId(entity.getFatherId());
			vo.setPrice(entity.getPrice() == null ? new BigDecimal(0) : entity.getPrice());
			vo.setLinePrice(entity.getLinePrice() == null ?  new BigDecimal(0) : entity.getLinePrice());
			vo.setTitle(entity.getTitle() == null ? "" : entity.getTitle());
			vo.setVideoId(entity.getId());
			vo.setVideoType(entity.getVideoType());
			vo.setViewers(entity.getStudyCount()); // 学习人数从数据库的study_count字段取
			vo.setIsVIPView(entity.getIsVIPView());
			resultList.add(vo);
		}
		return new Pager(pageSize, pageNo, pager.getRowCount(), resultList);
	}

	@Override
	public LiveVideoSubject getSubject(Integer subjectId) {
		return liveDao.getSubject(subjectId);
	}

	@Override
	public PopularListResult getPopularList(int pageNo, int pageSize, Long videoId, Long memberId) {
		PopularListResult resultList0 = new PopularListResult();
		List<PopularListVo> volist = new ArrayList<PopularListVo>();
		LiveVideo video = liveDao.getLiveVideoById(videoId);
		ChatRoomAuth chatRoomAuth = liveDao.isChatAdmin(memberId);
		/**
		 * 获取顶部我的邀请信息 memberId没有,则为Web端的请求
		 */
		if (memberId != null) {
			PopularListVo popular = liveDao.getPopular(videoId, memberId);
			if (popular != null) {
				popular.setMemberName("我");
				popular.setPhoto(popular.getPhoto() == null || popular.getPhoto().isEmpty() ? memberUrl
						: jointUrl + popular.getPhoto());
				// 专家和管理员都能看到消费榜
				if (video.getExpertId().equals(memberId) || chatRoomAuth != null) {
					List<InviteStatis> list = liveDao.getPopularListVoList(videoId, memberId);
					for (InviteStatis entity : list) {
						Long paidSum = liveDao.getPaidSum(entity.getAppFromId(), videoId);
						popular.setPaidSum(paidSum);
					}
				}
				// 判断是否是课程作者
				if (video.getExpertId().equals(memberId)) {
					popular.setIsOwner(1);
				} else {
					popular.setIsOwner(0);
				}
				// 判断是否是聊天室管理员
				if (chatRoomAuth != null) {
					popular.setIsAdmin("1");
				} else {
					popular.setIsAdmin("0");
				}
				if (popular.getMobile() != null && !"".equals(popular.getMobile())) {
					popular.setMobile(popular.getMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
				}
				resultList0.setPopular(popular);
			} else {
				Member member = liveDao.getMember(memberId);
				PopularListVo popularNew = new PopularListVo();
				if(member!=null) {
					popularNew.setMemberId(memberId);
					popularNew.setMemberName("我");
					popularNew.setMobile(member.getMobile());
					popularNew.setPhoto(member.getPhoto() == null || member.getPhoto().isEmpty() ? memberUrl
							: jointUrl + member.getPhoto());
					popularNew.setAudienceCount(0);
					if (video.getExpertId().equals(memberId) || chatRoomAuth != null) {
						popularNew.setIsOwner(1);
						List<InviteStatis> list = liveDao.getPopularListVoList(videoId, memberId);
						if (list != null && list.size() > 0) {
							for (InviteStatis entity : list) {
								Long paidSum = liveDao.getPaidSum(entity.getAppFromId(), videoId);
								popularNew.setPaidSum(paidSum);
							}
						} else {
							popularNew.setPaidSum(0L);
						}
					} else {
						popularNew.setIsOwner(0);
					}
					// 判断是否是聊天室管理员
					if (chatRoomAuth != null) {
						popularNew.setIsAdmin("1");
					} else {
						popularNew.setIsAdmin("0");
					}
					if (member.getMobile() != null && !"".equals(member.getMobile())) {
						popularNew.setMobile(member.getMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
					}
				}else {
					popularNew.setMemberId(0L);
					popularNew.setIsAdmin("0");
					popularNew.setIsOwner(0);
					popularNew.setMemberName("我");
					popularNew.setMobile("");
					popularNew.setOpenId("");
					popularNew.setPaidSum(0L);
					popularNew.setPhoto(memberUrl);
					
				}
				resultList0.setPopular(popularNew);
			}
		}
		/**
		 * 获取人气排行榜
		 */
		List<PopularListVo> list = liveDao.getPopularList(videoId);
		String openIds = "";
		if (list != null && list.size() > 0) {
			/**
			 * 筛选出分页所需的结果
			 */
			outer: for (int i = (pageNo - 1) * pageSize; i <= list.size() - 1; i++) {
				PopularListVo vo = new PopularListVo();
				vo.setPhoto(list.get(i).getPhoto() == null || list.get(i).getPhoto().isEmpty() ? memberUrl
						: jointUrl + list.get(i).getPhoto());
				// 手机号中间四位用星号代替
				String mobile = "";
				if (list.get(i).getMobile() != null && !"".equals(list.get(i).getMobile())) {
					mobile = list.get(i).getMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
				}
				if (list.get(i).getMemberId() != 0) {
					if (!list.get(i).getMemberId().equals(memberId)) {
						if (chatRoomAuth != null) {
							List<InviteStatis> shareList = liveDao.getPopularListVoList(videoId,
									list.get(i).getMemberId());
							for (InviteStatis entity : shareList) {
								Long paidSum = liveDao.getPaidSum(entity.getAppFromId(), videoId);
								vo.setPaidSum(paidSum);
							}
						}
						// 若存在昵称是手机号的,则隐藏手机号中间4位数字
						if (!"".equals(list.get(i).getMemberName()) && list.get(i).getMemberName() != null) {
							if (list.get(i).getMemberName().equals(list.get(i).getMobile())) {
								vo.setMemberName(mobile);
							} else {
								vo.setMemberName(list.get(i).getMemberName());
							}
						} else {
							vo.setMemberName(mobile);
						}
					} else {
						vo.setMemberName("我");
					}
				} else {
					vo.setMemberName("微信用户");
					openIds += "&openid=" + list.get(i).getOpenId();
					vo.setOpenId(list.get(i).getOpenId());
					vo.setPhoto("");
					if (chatRoomAuth != null) {
						vo.setPaidSum(0L);
					}
				}
				vo.setMemberId(list.get(i).getMemberId());
				if (video.getExpertId().equals(list.get(i).getMemberId())) {
					vo.setIsOwner(1);
				} else {
					vo.setIsOwner(0);
				}
				vo.setAudienceCount(list.get(i).getAudienceCount());
				volist.add(vo);
				/**
				 * 判断是否达到要取的结果的条数
				 */
				if (volist.size() >= pageSize) {
					break outer;
				}
			}
		}
		resultList0.setRowCount(volist.size());
		resultList0.setPopularList(volist);
        /**
         * 把微信的用户信息拼接进来
         */
		List<PopularListVo> result=new ArrayList<PopularListVo>();
		String jsonMapStr="";
		if(!"".equals(openIds)) {
			if(apiDoMain.contains("certmaptest")) {
				jsonMapStr= GetJsonData.sendGet("http://mp.certmaptest.com/user", openIds.replaceFirst("&", ""));
			}else if(apiDoMain.contains("cert-map")) {
				jsonMapStr= GetJsonData.sendGet("http://mp.cert-map.com/user", openIds.replaceFirst("&", ""));
			}
			JSONObject jsonMap = JSONObject.fromObject(jsonMapStr);
			int code=(int) jsonMap.get("code");
			if(code==200){
				List<PopularListVo2> list1 = JSON.parseArray(jsonMap.getString("data"), PopularListVo2.class);//小集合
				List<PopularListVo> list2=resultList0.getPopularList();//大集合
				for(PopularListVo vo1:list2) {
				    for(PopularListVo2 vo2:list1) {
						if(vo1.getOpenId()!=null && vo1.getOpenId().equals(vo2.getOpenid())) {
							vo1.setMemberName(vo2.getNickname());
							vo1.setPhoto(vo2.getHeadimgurl());
						}
					}
				    result.add(vo1);  
				}
			}
		}
		PopularListResult lastResultList = new PopularListResult();
		lastResultList.setPopular(resultList0.getPopular());
		if(!"".equals(openIds)) {
			lastResultList.setPopularList(result);
		}else {
			lastResultList.setPopularList(resultList0.getPopularList());
		}
		lastResultList.setRowCount(resultList0.getRowCount());
		return lastResultList;
	}

	@Override
	public LiveVideoVo2 getVideoAdjunct(Long videoId) {
		LiveVideo video = liveDao.getLiveVideoById(videoId);
		LiveVideoVo2 vo = new LiveVideoVo2();
		if (video.getDocumentId() != null && !"".equals(video.getDocumentId())) {
			GetDocumentResponse doc = BDocHelper.readDocument(video.getDocumentId());
			vo.setDocumentID(doc.getDocumentId());
			vo.setFileName(doc.getTitle());
			vo.setFormat(doc.getFormat());
			vo.setPageCount(String.valueOf(doc.getPublishInfo().getPageCount()));
		}
		return vo;
	}

	@Override
	public List<ViewerBetweenLive> watchRecord(Long videoId, Long memberId) {
		return liveDao.watchRecord(videoId, memberId);
	}

	/**
	 * 视频课程详情(若视频免费或用户购买了该课程,则返回视频播放Url,否则不返回)
	 */
	@Override
	public LiveVideoVo3 getVideoCourseDetail(Long memberId, Long videoId, String port) {
		LiveVideoVo3 vo = new LiveVideoVo3();
		List<SimpleVideoVo> simpleList = new ArrayList<SimpleVideoVo>();
		LiveVideo video = liveDao.getLiveVideoById(videoId);
		Long duration = 0L;
		ExpenseTotal buys = liveDao.getBuyStatus(memberId, videoId);
		if (buys != null) {
			vo.setBuyStatus(1);
		} else {
			vo.setBuyStatus(0);
		}
		boolean collect = collectDao.getCollect(memberId, videoId, "7");
		if (collect) {
			vo.setCollectStatus(1);
		} else {
			vo.setCollectStatus(0);
		}
		/**
		 * 拼接时长
		 */
		if (video.getClassify() == 1) {
			List<LiveVideo> childList = liveDao.getChildVideoList(videoId);
			for (LiveVideo child : childList) {
				duration += child.getDuration();
				vo.setDuration(duration);
				SimpleVideoVo simpleVo = new SimpleVideoVo();
				simpleVo.setId(child.getId());
				simpleVo.setTitle(child.getTitle());
				simpleVo.setVideoStatus(child.getVideoStatus());
				simpleVo.setVideoType(child.getVideoType());
				if (port.equals("web")) {
					if (video.getPrice().compareTo(new BigDecimal(0)) == 0 || buys != null) {
						simpleVo.setVideoUrl(child.getVideoUrl());
					} else {
						simpleVo.setVideoUrl("");
					}
				} else if (port.equals("app")) {
					if(child.getVideoType().equals("0")){
						simpleVo.setVideoUrl(child.getFlvUrl());
					}else{
						simpleVo.setVideoUrl(child.getVideoUrl());
					}
				}
				simpleVo.setApplyDate(DateHelper.dataToString(child.getApplyDate(), "MM月dd日 HH:mm"));
				simpleList.add(simpleVo);
			}
			vo.setShareUrl(seriesCourse + video.getId());
		} else if (video.getClassify() == 4) {
			vo.setDuration(video.getDuration());
			SimpleVideoVo simpleVo = new SimpleVideoVo();
			simpleVo.setId(video.getId());
			simpleVo.setTitle(video.getTitle());
			simpleVo.setVideoStatus(video.getVideoStatus());
			simpleVo.setVideoType(video.getVideoType());
			if (port.equals("web")) {
				if (video.getPrice().compareTo(new BigDecimal(0)) == 0 || buys != null) {
					simpleVo.setVideoUrl(video.getVideoUrl());
				} else {
					simpleVo.setVideoUrl("");
				}
			} else if (port.equals("app")) {
				if(video.getVideoType().equals("0")){
					simpleVo.setVideoUrl(video.getFlvUrl());
				}else{
					simpleVo.setVideoUrl(video.getVideoUrl());
				}
			}
			simpleVo.setApplyDate(DateHelper.dataToString(video.getApplyDate(), "MM月dd日 HH:mm"));
			simpleList.add(simpleVo);
			if(!"0".equals(video.getVideoType())){
				vo.setShareUrl(singleCourse + video.getId());
			}else{
				vo.setShareUrl(singleLive + video.getId());
			}
		}
		vo.setVideos(simpleList); // 视频集
		vo.setDetailCover(
				video.getDetailCover() == null || video.getDetailCover().isEmpty() ? jointUrl + video.getCover()
						: jointUrl + video.getDetailCover());
		vo.setIsVIPView(video.getIsVIPView());
		vo.setLinePrice(video.getLinePrice());
		String freeImg="";
		//若视频有免单活动，  则要去免单表(live_video_free)查免单图片
		if(video.getFreeOrder()==1) {
			FreeVideo order=liveDao.getFreeVideo(videoId);
			if(order!=null) {
				if(order.getDeadline().after(new Date())){
					freeImg=order.getImgUrl();
				}
			}
			vo.setFreeOrder(true);
		}else {
			vo.setFreeOrder(false);
		}
		List<String> strList = new ArrayList<String>();
		if (video.getPhotos() != null && !"".equals(video.getPhotos())) {
			if(!"".equals(freeImg)) {
				strList.add(("".equals(freeImg)?"":jointUrl+freeImg));
			}
			if (video.getPhotos().contains(",")) {
				String[] photos = video.getPhotos().split(",");
				if (photos.length > 0) {
					for (String str : photos) {
						strList.add(jointUrl + str);
					}
				}
			} else {
				strList.add(jointUrl + video.getPhotos());
			}
		}
		vo.setPhotos(strList);
		vo.setPrice(video.getPrice());
		vo.setTitle(video.getTitle());
		vo.setVideoUrl(video.getVideoUrl());
		vo.setStudyCount(video.getStudyCount());
		vo.setVideoStatus(Integer.valueOf(video.getVideoStatus()));
		vo.setApplyDate(DateHelper.dataToString(video.getApplyDate(), "MM月dd日 HH:mm"));
		vo.setVideoId(video.getId());
		vo.setVideoType(video.getVideoType() == null || video.getVideoType().isEmpty() ? 0
				: Integer.valueOf(video.getVideoType()));
		vo.setConsultMemberId(video.getConsultMemberId());
		return vo;
	}

	@Override
	public RedeemCodeOwner getOwnCode(String mobile) {
		return liveDao.getOwnCode(mobile);
	}

	@Override
	public List<String> getIosNotNewestVersion() {
		return liveDao.getIosNotNewestVersion();
	}

	@Override
	public List<String> getAndroidNotNewestVersion() {
		return liveDao.getAndroidNotNewestVersion();
	}

	@Override
	public List<String> getIosNewestVersion() {
		return liveDao.getIosNewestVersion();
	}

	@Override
	public List<String> getAndroidNewestVersion() {
		return liveDao.getAndroidNewestVersion();
	}

	@Override
	public void saveBullet(VideoBullet bullet) {
		liveDao.saveBullet(bullet);
	}

	@Override
	public Pager pullBullet(Long videoId, int pageNo, int pageSize) {
		return liveDao.pullBullet(videoId, pageNo, pageSize);
	}

	@Override
	public List<ViewerBetweenLive> watchRecord(Long videoId) {
		return liveDao.watchRecord(videoId);
	}

	@Override
	public RecordEstimate getRecordEstimate(Long memberId, Long videoId, int seriesVideoId) {

		return liveDao.getRecordEstimate(memberId, videoId, seriesVideoId);
	}

	@Override
	public double getSumGrade(int videoId, int seriesVideoId) {

		List<RecordEstimate> list = liveDao.getRecordEstimate(videoId, seriesVideoId);

		double sum = 0;
		int count = 0;
		for (RecordEstimate recordEstimate : list) {

			if (recordEstimate.getGrade() != null && recordEstimate.getGrade() > 0) {
				++count;
				sum += recordEstimate.getGrade();

			}

		}

		double totalGrade = 0.0;
		if (count > 0) {
			totalGrade = sum / count;
		}
		return totalGrade;
	}

	@Override
	public void updateRecordEstimate(RecordEstimate recordEstimate) {
		liveDao.updateRecordEstimate(recordEstimate);

	}

	@Override
	public int saveRecordEstimate(RecordEstimate recordEstimate) {

		return liveDao.saveRecordEstimate(recordEstimate);
	}

	@Override
	public RecordEstimate getRecordEstimate(Integer id2) {
		return liveDao.getRecordEstimate(id2);
	}

	@Override
	public Pager getRecordEstimate(Long memberId, Long videoId, int pageSize, int pageNo) {

		List<RecordEstimateVo> recordEstimateVos = new ArrayList<>();

		// 判断用户是否购买了此视频 buyStatus=0 未购买 buyStatus=1已购买
		// BuysRecordNew buy = liveDao.getBuyStatus(memberId, videoId);

		int buyStatus = 1;
		//
		// if(buy!=null){
		// buyStatus = 1;
		// }

		Pager pager  = liveDao.getRecordEstimate(buyStatus, videoId, pageSize, pageNo);
		List<RecordEstimate> recordEstimates = pager.getResultList();
		
		for (RecordEstimate recordEstimate : recordEstimates) {

			if (recordEstimate.getCheckStatus().equals("1")
					|| recordEstimate.getMemberId().toString().equals(memberId.toString())) {

				RecordEstimateVo recordEstimateVo = new RecordEstimateVo();

				recordEstimateVo.setContent(recordEstimate.getContent());
				recordEstimateVo.setContentTime(DateHelper.dataToString(recordEstimate.getContentTime(), "yyyy-MM-dd"));
				if (recordEstimate.getSeriesVideoId() == -1) {
					recordEstimateVo.setVideoTitle("");
				} else {
					recordEstimateVo.setVideoTitle(recordEstimate.getVideoTitle());
				}
				recordEstimateVo
						.setMemberName(recordEstimate.getMemberName() == null ? "" : recordEstimate.getMemberName());
				String photo = "";
				if (StringUtils.isNotBlank(recordEstimate.getPhoto())) {
					photo = jointUrl + recordEstimate.getPhoto();
				}
				recordEstimateVo.setPhoto(photo);
				recordEstimateVo.setReply(recordEstimate.getReply() == null ? "" : recordEstimate.getReply());
				recordEstimateVo.setReplyTime(recordEstimate.getReplyTime() == null ? ""
						: DateHelper.dataToString(recordEstimate.getReplyTime(), "yyyy-MM-dd"));
				recordEstimateVo.setSeriesVideoId(recordEstimate.getSeriesVideoId());
				recordEstimateVo.setTotalGradel(
						recordEstimate.getTotalGrade() == null ? "" : recordEstimate.getTotalGrade().toString());
				recordEstimateVo.setVideoId(recordEstimate.getVideoId());

				recordEstimateVos.add(recordEstimateVo);
			}

		}
		pager.setResultList(recordEstimateVos);
		return pager;

	}

	@Override
	public List<RecordEstimate> getRecordEstimate(int videoId, int seriesVideoId) {
		return liveDao.getRecordEstimate(videoId, seriesVideoId);
	}

	@Override
	public VideoBullet getNewBullet() {
		return liveDao.getNewBullet();
	}

	@Override
	public void sendCodeToCoupn() {
		List<RedeemCodeOwner> list = liveDao.sendCodeToCoupn();
		if (list != null && list.size() > 0) {
			for (RedeemCodeOwner redeem : list) {
				SmsUtil.sendMessage(redeem.getOwner(),
						"您有30元视频课程券还未兑换，兑换码为：" + redeem.getRedeemCode() + "，将于2018年3月31日到期，请尽快登录世界认证地图APP兑换使用！\r\n"
								+ "视频课程《九阳标准研究心法及破壁机测试方法大公开》将于2018年3月16日19:00准时开播，赶紧占位吧！\r\n"
								+ "兑换方式：打开世界认证地图APP—点击底部菜单栏“我的”—点击“兑换码”，输入收到的兑换码—确认兑换—至“卡券”中查看视频课程券。");
			}
		}
	}

	@Override
	public void sendInviteActivity(Long videoId, String title, String time) {
		List<PopularListVo> list = liveDao.sendInviteActivity(videoId);
		if (list != null && list.size() > 0) {
			for (PopularListVo vo : list) {
				if (vo.getAudienceCount() > 1) {
					SmsUtil.sendMessage(vo.getMobile(),
							"您还差1位小伙伴扫描您的邀请卡就完成免单任务啦!《" + title + "》将于" + time + "准时开播，赶紧占位！");
				} else {
					SmsUtil.sendMessage(vo.getMobile(),
							"您还差2位小伙伴扫描您的邀请卡就完成免单任务啦!《" + title + "》将于" + time + "准时开播，赶紧占位！");
				}
			}
		}
	}

	@Override
	public void sendUseCoupn() {
		List<String> list = liveDao.sendUseCoupn();
		if (list != null && list.size() > 0) {
			for (String mobile : list) {
				SmsUtil.sendMessage(mobile, "您的30元视频课程卡券将于2018年3月31日到期，请尽快至世界认证地图APP使用！\r\n"
						+ "视频课程《九阳标准研究心法及破壁机测试方法大公开》将于2018年3月16日19:00准时开课，赶紧占位吧！");
			}
		}
	}

	@Override
	public Pager pullWalls(Long videoId, int pageNo, int pageSize) {
		return liveDao.pullWalls(videoId, pageNo, pageSize);
	}

	@Override
	public void relieveSilenceByGroupId(String groupId) {
		liveDao.relieveSilenceByGroupId(groupId);
	}

	@Override
	public FreeVideo getFreeVideo(Long videoId) {
		if (videoId != null) {
			return liveDao.getFreeVideo(videoId);
		}
		return null;
	}

	@Override
	public Long getAppRecommendTimes(Long memberId, Long videoId) {
		if (memberId != null && videoId != null) {
			return liveDao.getAppRecommendTimes(memberId, videoId);
		}
		return 0L;
	}

	@Override
	public Long getWechatRecommendTimes(String openId, Long videoId) {
		if (openId != null && videoId != null) {
			return liveDao.getWechatRecommendTimes(openId, videoId);
		}
		return 0L;
	}

	@SuppressWarnings("static-access")
	@Override
	public int sendDiscountMsg(long videoId, Long memberId,String wechatName) {
		LiveVideo video=liveDao.getLiveVideoById(videoId);
		LiveVideoDiscount discount=liveDao.getLiveVideoDiscountById(videoId);
		Member member=liveDao.getMember(memberId);
		int inviteSum= liveDao.getInviteStatis(memberId,videoId);
		SystemMessage msg=new SystemMessage();
		msg.setAddTime(new Date());
		msg.setEditTime(new Date());
		String dataUrl="";
		if(video.getVideoType().equals("0")) {
			if(video.getClassify()==1) {
				dataUrl="weikai://cert-map?target=live&id="+videoId+"&type=2";
			}else if(video.getClassify()==4) {
				dataUrl="weikai://cert-map?target=live&id="+videoId+"&type=1";
			}
		}else{
			if(video.getClassify()==1) {
				dataUrl="weikai://cert-map?target=course&id="+videoId+"&type=2";
			}else if(video.getClassify()==4) {
				dataUrl="weikai://cert-map?target=course&id="+videoId+"&type=1";
			}
		}
		LiveVideoDistributor distributor=activityExchangeRecordDao.getLiveVideoDistributor(videoId,memberId);
		String distriStr="";
		if(distributor!=null) {
			BigDecimal totalIntegral=activityExchangeRecordDao.getTotalIntegral(memberId);
			distriStr="----------------------------------\r\n" + 
				      "*进度* ： 本课程已邀请"+distributor.getBringNum()+"人扫码，"+distributor.getBoughtNum()+"人下单\r\n" + 
				      "*积分奖励* ： 本次将为您赢得10待核积分，若该好友14天内没取关公众号，即可到账。\r\n" + 
				      "*已到账积分* ： "+totalIntegral;
		}
		msg.setDataUrl(dataUrl);
		msg.setMemberIds(memberId.toString());
		msg.setRegionId(0L);
		msg.setRelateId(videoId);
		msg.setPhoto("");
		msg.setPushType("1");
		msg.setStatus("1");
		msg.setType("10");
		msg.setRemark("");
		String nowTime=DateHelper.dataToString(new Date(), "yyyy年MM月dd日  HH:mm:ss");
		String endTime=DateHelper.dataToString(discount.getDeadline(), "yyyy年MM月dd日  HH:mm");
		if(inviteSum<discount.getTargetNum()) {
			String notifyTitle="【世界认证地图】成员加入提醒：";
			String smsTitle="成员加入提醒：\r\n";
			String notifyDesc;
			String smsDesc;
			try {
				notifyDesc = "恭喜！有一位新朋友扫码支持您啦！\r\n" + 
						"姓名："+URLEncoder.encode(wechatName, "utf-8")+"\r\n" + 
						"时间： "+nowTime+"\r\n" + 
						"已完成"+inviteSum+"人，"+discount.getTargetNum()+"位小伙伴支持，即可￥"+discount.getFee()+"元购买《"+video.getTitle()+"》 ！\r\n" + 
						"【活动有效期】截止"+endTime;
				smsDesc="恭喜！有一位新朋友扫码支持您啦！\r\n" + 
						"姓名："+EmojiUtils.filterEmoji(wechatName)+"\r\n" + 
						"时间： "+nowTime+"\r\n" + 
						"已完成"+inviteSum+"人，"+discount.getTargetNum()+"位小伙伴支持，即可￥"+discount.getFee()+"元购买《"+video.getTitle()+"》 ！\r\n" + 
						"*活动有效期* ： 截止"+endTime;
				msg.setTitle(notifyTitle+"\r\n"+notifyDesc);  //存入数据库的时候要存加过密的微信名 ， 
				systemMessageDao.save(msg);
				if(member!=null) {
					if(distributor!=null) {
						SmsUtil.sendMessage(member.getMobile(), smsTitle+smsDesc+distriStr);
						/*
						 * 直接发送推送的时候， 要发不加密的微信名
						 */
						MiPushHelper.sendAndroidUserAccount(notifyTitle, smsDesc+"\r\n"+distriStr, dataUrl, member.getMobile());
						MiPushHelper.sendIOSUserAccount(notifyTitle+"\r\n"+smsDesc+"\r\n"+distriStr, dataUrl, member.getMobile());
					}else {
						SmsUtil.sendMessage(member.getMobile(), smsTitle+smsDesc);
						MiPushHelper.sendAndroidUserAccount(notifyTitle, smsDesc, dataUrl, member.getMobile());
						MiPushHelper.sendIOSUserAccount(notifyTitle+"\r\n"+smsDesc, dataUrl, member.getMobile());
					}
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return 200;
		}else if(inviteSum==discount.getTargetNum()) {
			DecimalFormat df = new DecimalFormat("0.00");  //金额精确到小数点后一位
			String notifyTitle="【世界认证地图】任务完成通知：";
			String smsTitle="任务完成通知：\r\n";
			BigDecimal price=video.getPrice().subtract(discount.getFee());
			String notifyDesc="\r\n" +
					"恭喜您任务完成啦！\r\n" + 
					"任务名称：《"+video.getTitle()+"》\r\n" + 
					"任务内容：特价挑战\r\n" + 
					"任务完成情况：挑战成功\r\n" + 
					"价值￥"+df.format(price)+"的优惠券已经放入APP“我的”—“卡卷”中， 可用于购买《"+video.getTitle()+"》！\r\n" + 
					"【活动有效期】截止" + endTime;
			String smsDesc="恭喜您任务完成啦！\r\n" + 
					"任务名称：《"+video.getTitle()+"》\r\n" + 
					"任务内容：特价挑战\r\n" + 
					"任务完成情况：挑战成功\r\n" + 
					"价值￥"+df.format(price)+"的优惠券已经放入APP“我的”—“卡卷”中， 可用于购买《"+video.getTitle()+"》！\r\n" + 
					"*活动有效期* ： 截止" + endTime;
			msg.setTitle(notifyTitle+"\r\n"+notifyDesc);
			systemMessageDao.save(msg);
			if(member!=null) {
				Coupn coupn=new Coupn();
				coupn.setBeginDate(new Date());
				coupn.setCoupnMoney(video.getPrice().subtract(discount.getFee()));
				Calendar calendar=new GregorianCalendar();
				calendar.setTime(new Date());
				calendar.add(calendar.MONTH, 1);
				coupn.setEndDate(calendar.getTime());
				coupn.setIsRemind(0);
				coupn.setIsShare("0");
				coupn.setOverlying("0");
				coupn.setScopeNum("8");
				coupn.setUsable("1");
				coupn.setUseScope("视频课程");
				coupn.setVideoId(Integer.valueOf(String.valueOf(videoId)));
				liveDao.saveCoupn(coupn);    //创建卡券
				CoupnUser coupnUser =new CoupnUser();
				coupnUser.setMemberId(memberId);
				coupnUser.setCoupnId(liveDao.getCoupnByVideoId(videoId,"8"));
				coupnUser.setSharerId(0L);
				coupnUser.setMemberName(member.getRealname());
				coupnUser.setSharer("");
				coupnUser.setUsed("0");    //创建用户卡券
				liveDao.saveUserCoupn(coupnUser);
				if(distributor!=null) {
					SmsUtil.sendMessage(member.getMobile(), smsTitle+smsDesc);
					/*
					 * 直接发送推送的时候， 要发不加密的微信名
					 */
					MiPushHelper.sendAndroidUserAccount(notifyTitle, smsDesc+"\r\n"+distriStr, dataUrl, member.getMobile());
					MiPushHelper.sendIOSUserAccount(notifyTitle+"\r\n"+smsDesc+"\r\n"+distriStr, dataUrl, member.getMobile());
				}else {
					SmsUtil.sendMessage(member.getMobile(), smsTitle+smsDesc);
					MiPushHelper.sendAndroidUserAccount(notifyTitle, smsDesc, dataUrl, member.getMobile());
					MiPushHelper.sendIOSUserAccount(notifyTitle+"\r\n"+smsDesc, dataUrl, member.getMobile());
				}
			}
			return 200;
		}
		return 400;
	}

	@Override
	public Integer getDistributorIntegralByMemberId(Long memberId) {
		if(memberId != null) {
			return liveDao.getDistributorIntegralByMemberId(memberId);
		}
		return null;
	}

}
