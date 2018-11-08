package cc.messcat.service.member;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cc.messcat.entity.*;
import cc.modules.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.baidubce.services.doc.model.GetDocumentResponse;
import cc.messcat.bases.BaseManagerDaoImpl;
import cc.messcat.vo.Adjunct;
import cc.messcat.vo.BuysRecordListVo;
import cc.messcat.vo.ExpenseTotalVo;
import cc.messcat.vo.EBusinessInfoVo2;
import cc.messcat.vo.HotReplyPayVo;
import cc.messcat.vo.MyBuysListResult;
import cc.messcat.vo.PlatformVo;
import cc.messcat.vo.QualityShareListVo;
import cc.modules.commons.Pager;

public class ExpenseTotalManagerDaoImpl extends BaseManagerDaoImpl implements ExpenseTotalManagerDao {
	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(ExpenseTotalManagerDaoImpl.class);
	private static String defaultPhoto = PropertiesFileReader.getByKey("default.photo.url");// 默认图片
	private static String jointUrl = PropertiesFileReader.getByKey("static.domain");// 图片拼接

	/**
	 * 综合已购列表接口
	 */
	@SuppressWarnings("unchecked")
	@Override
	public MyBuysListResult findBuysRecordsByCondiction(Long memberId, String type, int pageNo, int pageSize) {
		MyBuysListResult buysListResult = new MyBuysListResult();

		Pager pager = expenseTotalDao.findBuysRecordsByCondiction(memberId, type, pageNo, pageSize);
		List<ExpenseTotal> buysRecords = pager.getResultList();
		buysListResult.setPageNo(pager.getPageNo());
		buysListResult.setPageSize(pager.getPageSize());
		buysListResult.setRowCount(pager.getRowCount());
		List<BuysRecordListVo> buysRecordListVos = new ArrayList<>();
		if (buysRecords != null && buysRecords.size() > 0) {
			List<Long> fatherIdList = new ArrayList<>();
			for (ExpenseTotal buysRecord : buysRecords) {
				if (!"0".equals(buysRecord.getType())) {
					BuysRecordListVo buysRecordListVo2 = new BuysRecordListVo();
					// type 0：购买会员套餐 1：购买报告 2：购买标准解读 3：购买质量分享 4：付费咨询 6：电商信息
					buysRecordListVo2.setBuysRecordId(buysRecord.getId());
					buysRecordListVo2.setMoney(buysRecord.getMoney().compareTo(new BigDecimal(0)) == -1 ? new BigDecimal(0) : buysRecord.getMoney());
					buysRecordListVo2.setType(buysRecord.getType());
					buysRecordListVo2.setBuyStatus("1");
					if ("1".equals(buysRecord.getType())) {
						if (ObjValid.isValid(buysRecord.getRegionId()) && ObjValid.isValid(buysRecord.getRelatedId())
								&& ObjValid.isValid(buysRecord.getRegionId())
								&& ObjValid.isValid(buysRecord.getRelatedId())) {
							Region region = regionDao.get(buysRecord.getRegionId());
							Product product = productDao.get(buysRecord.getRelatedId());
							buysRecordListVo2.setIsDown("0");
							buysRecordListVo2.setTitle(product.getName() + region.getName() + "市场准入报告");
							buysRecordListVo2.setRegionId(region.getId());
							buysRecordListVo2.setProductId(product.getId());
							buysRecordListVo2
									.setUrl("http://www.cert-map.com/epReportAction!getAppReport.action?regionId="
											+ region.getId() + "&productId=" + product.getId() + "&memberId="
											+ memberId);
							// 查询收藏状态
							Collect collect = new Collect();
							collect.setMemberId(memberId);
							collect.setRegionId(region.getId());
							collect.setRelatedId(product.getId());
							List<Collect> collects = collectDao.getCollectByCon(collect);
							if (ObjValid.isValid(collects)) {
								buysRecordListVo2.setCollectStatus("1");// 已收藏
							} else {
								buysRecordListVo2.setCollectStatus("0");// 未收藏收藏
							}
							// 分享用的URL
							String shareURL = productDao.getShareURL();
							if (shareURL != null) {
								buysRecordListVo2.setShareURL(
										shareURL + "&regionId=" + region.getId() + "&productId=" + product.getId());
							} else {
								buysRecordListVo2.setShareURL("无此分享URL");
							}
							buysRecordListVos.add(buysRecordListVo2);
						}

					} else if ("2".equals(buysRecord.getType()) || "3".equals(buysRecord.getType())) {
						if (ObjValid.isValid(buysRecord.getRelatedId())
								&& ObjValid.isValid(buysRecord.getRelatedId())) {
							StandardReading standardReading = standardReadDao.get(buysRecord.getRelatedId());
							Member member = memberDao.get(memberId);

							boolean collect = collectDao.getCollect(memberId, standardReading.getId(),
									buysRecord.getType());
							String collectStatus = "";
							if (collect == true) {
								collectStatus = "1";
							} else {
								collectStatus = "0";
							}
							buysRecordListVo2.setTitle(standardReading.getTitle());
							String shareURL = qualityShareDao.getShareURL() + "&standardReadingId="
									+ buysRecord.getRelatedId(); // 分享URL
							QualityShareListVo standardReadListVo = this.setStandardReadingVoInfo(standardReading,
									member, buysRecord, collectStatus, "1", shareURL);

							buysRecordListVo2.setStandardReadListVo(standardReadListVo);
							buysRecordListVo2.setIsDown("1");
							buysRecordListVos.add(buysRecordListVo2);
						}
					} else if ("4".equals(buysRecord.getType())) {
						if (ObjValid.isValid(buysRecord.getRelatedId())) {
							HotReplyPayVo payProblem = new HotReplyPayVo();

							HotProblem problem = hotReplyDao.getProblem(buysRecord.getRelatedId());
							// 用于判断问题是否被回答,若已回答,则一并获取replyId
							HotReply hotReply = hotReplyDao.getReply(problem.getExpertId(), problem.getId());
							// 获取专家名字
							Member expert = hotReplyDao.getMember(problem.getExpertId());
							payProblem.setAddTime(DateHelper.dataToString(problem.getAddTime(), "yyyy-MM-dd HH:mm:ss"));
							payProblem.setProblem(problem.getName());
							/**
							 * 首先判断购买问题的提问者是不是自己,从而区分是否是围观问题
							 */
							Share share = hotReplyDao.getShare(); // 获取分享URL
							if (!problem.getMemberId().equals(memberId)) { // 若memberId不一样,则该问题不是当前用户提出的
								payProblem.setReplyStatus("3"); // 围观问题
																// ,一定是已经被回答的问题
								payProblem.setExpertName("");
								payProblem.setIsReply("true");
								payProblem.setMoney(new BigDecimal(0));
								/*
								 * 围观问题只能是围观别人已经被回答的问题
								 */
								if (hotReply != null) {
									payProblem.setReplyId(String.valueOf(hotReply.getId()));
									buysRecordListVo2.setShareURL(
											share.getShareURL() + "&replyId=" + String.valueOf(hotReply.getId()));
								}
							} else { // 若相等,则该问题是当前用户提出的
								payProblem.setExpertName(expert.getRealname());
								if (hotReply != null) { // 判断问题是否被回答
									payProblem.setIsReply("true");
									// 获取分享URL
									buysRecordListVo2.setShareURL(
											share.getShareURL() + "&replyId=" + String.valueOf(hotReply.getId()));
									payProblem.setReplyId(String.valueOf(hotReply.getId()));
									payProblem.setReplyStatus("2"); // 已回答的问题
									HotReplyFees fees = hotReplyDao.getReplyFees(problem.getExpertId());
									if (problem.getType().equals("0")) {
										payProblem.setMoney(fees.getMoney());
									} else {
										payProblem.setMoney(fees.getPrivateMoney());
									}
								} else {
									Date addTime = problem.getAddTime(); // 问题创建时间
									// 注意这里一定要转换成Long类型，要不然小时超过25时会出现范围溢出，从而得不到想要的日期值
									Date twoDaysAgo = new Date(new Date().getTime() - 2 * 24 * 60 * 60 * 1000L);
									/*
									 * 若当前日期减两天, 在问题创建之后,说明该问题已经失效, 否则就有效
									 */
									if (twoDaysAgo.after(addTime)) {
										payProblem.setIsReply("");
										payProblem.setReplyId("");
										payProblem.setMoney(new BigDecimal(0));
										payProblem.setReplyStatus("1"); // 问题已失效
									} else {
										payProblem.setIsReply("false");
										payProblem.setReplyId("");
										payProblem.setMoney(new BigDecimal(0));
										payProblem.setReplyStatus("0"); // 有效期的未答问题
									}
								}
							}
							buysRecordListVo2.setPayProblem(payProblem);
							buysRecordListVos.add(buysRecordListVo2);
						}

					} else if ("6".equals(buysRecord.getType())) {
						if (ObjValid.isValid(buysRecord.getRelatedId())) {
							EBusinessProduct businessProduct = businessProductDao.get(buysRecord.getRelatedId());
							buysRecordListVo2.setTitle(businessProduct.getName() + "电商准入报告");
							buysRecordListVo2.setEbusinessId(businessProduct.getId());
							buysRecordListVo2.setIsDown("1");
							// 获取电商信息
							EBusinessInfoVo2 vo = new EBusinessInfoVo2();
							// 根据产品id查出
							List<EBusinessInfo> ebs0 = new ArrayList<EBusinessInfo>();
							StringBuffer sb0 = new StringBuffer("from EBusinessInfo where status = 1 ");
							sb0.append(" and ebusinessProductId = ").append(buysRecord.getRelatedId());
							List<EBusinessInfo> ebs = businessInfoDao.findByhql(sb0.toString());
							if (ObjValid.isValid(ebs)) {
								for (EBusinessInfo ebi : ebs) {
									if (ebs0.contains(ebi)) {
										continue;
									}
									ebs0.add(ebi);
								}
							}
							if (ObjValid.isValid(ebs0)) {
								vo.setEbusinessProductId((Long) (ebs0.get(0).getEbusinessProductId() == null ? ""
										: ebs0.get(0).getEbusinessProductId()));
								vo.setTitle(ebs0.get(0).getEbusinessProductName() + "电商准入报告");
								vo.setMoney((businessProduct.getMoney() == null ? new BigDecimal(0)
										: businessProduct.getMoney()));
								vo.setEbusinessProductId((Long) (ebs0.get(0).getEbusinessProductId() == null ? ""
										: ebs0.get(0).getEbusinessProductId()));
								vo.setEbusinessProductName(ebs0.get(0).getEbusinessProductName() == null ? ""
										: ebs0.get(0).getEbusinessProductName());
								vo.setSubEbusinessProductId(ebs0.get(0).getSubEbusinessProductId() == null ? ""
										: ebs0.get(0).getSubEbusinessProductId());
								vo.setSubEbusinessProductName(ebs0.get(0).getSubEbusinessProductName() == null ? ""
										: ebs0.get(0).getSubEbusinessProductName());

								boolean collect = businessInfoDao.getCollect(ebs0.get(0).getEbusinessProductId(),
										memberId);
								if (collect == true) {
									vo.setCollectStatus("1"); // 收藏状态
								} else {
									vo.setCollectStatus("0");
								}
								boolean buys = businessInfoDao.getBuys(ebs0.get(0).getEbusinessProductId(), memberId);
								if (buys == true) {
									vo.setBuyStatus("1"); // 购买状态
								} else {
									vo.setBuyStatus("0");
								}
								/**
								 * 获取电商商品价格
								 */
								EBusinessProduct eProduct = businessInfoDao
										.getProduct(ebs0.get(0).getEbusinessProductId());
								if (eProduct != null) {
									vo.setMoney(eProduct.getMoney());
								} else {
									vo.setMoney(new BigDecimal(0));
								}

								List<PlatformVo> platforms = new ArrayList<>();

								for (EBusinessInfo ebi : ebs0) {
									PlatformVo platform = new PlatformVo();
									platform.setBigClass(ebi.getBigClass() == null ? "" : ebi.getBigClass());
									platform.setPlatform(ebi.getPlatform() == null ? "" : ebi.getPlatform());
									platform.setBigClass(ebi.getBigClass());
									platform.setSubProducts(ebi.getSubEbusinessProductName());
									platform.setPlatform(ebi.getPlatform());

									platform.setSubProducts(ebi.getSubEbusinessProductName());
									/**
									 * 行业标准---附件信息
									 */
									platform.setIndustrialStandardFile("");
									if (ebi.getIndustrialStandardDocumentId() != null
											&& !"".equals(ebi.getIndustrialStandardDocumentId())) {
										String industDoc = ebi.getIndustrialStandardDocumentId();
										GetDocumentResponse document = BDocHelper.readDocument(industDoc);
										platform.setIndustrialStandardDocumentId(
												document.getDocumentId() == null ? "" : document.getDocumentId());
										platform.setIndustrialStandardFiletype(
												document.getFormat() == null ? "" : document.getFormat());
										platform.setIndustrialStandardName(
												document.getTitle() == null ? "" : document.getTitle());
										platform.setIndustrialStandardPageCount(
												document.getPublishInfo().getPageCount());
									} else {
										platform.setIndustrialStandardDocumentId("");
										platform.setIndustrialStandardFiletype("");
										platform.setIndustrialStandardName("");
										platform.setIndustrialStandardPageCount(0);
									}

									/**
									 * 抽查规范 ---附件信息
									 */
									platform.setCheckStandardFile("");
									if (ebi.getCheckStandardDocumentId() != null
											&& !"".equals(ebi.getCheckStandardDocumentId())) {
										String checkDoc = ebi.getCheckStandardDocumentId();
										GetDocumentResponse document = BDocHelper.readDocument(checkDoc);
										platform.setCheckStandardDocumentId(
												document.getDocumentId() == null ? "" : document.getDocumentId());
										platform.setCheckStandardFiletype(
												document.getFormat() == null ? "" : document.getFormat());
										platform.setCheckStandardName(
												document.getTitle() == null ? "" : document.getTitle());
										platform.setCheckStandardPageCount(document.getPublishInfo().getPageCount());
									} else {
										platform.setCheckStandardDocumentId("");
										platform.setCheckStandardFiletype("");
										platform.setCheckStandardName("");
										platform.setCheckStandardPageCount(0);
									}
									/**
									 * 平台标准---附件信息
									 */
									platform.setPlatformStandardFile("");
									if ((ebi.getCheckStandardDocumentId()) != null
											&& !"".equals(ebi.getCheckStandardDocumentId())) {
										String documentID = ebi.getCheckStandardDocumentId();
										GetDocumentResponse document = BDocHelper.readDocument(documentID);
										platform.setPlatformStandardName(
												document.getTitle() == null ? "" : document.getTitle()); // 附件名称
										platform.setPlatformStandardFiletype(
												document.getDocumentId() == null ? "" : document.getDocumentId()); // 附件格式
										platform.setPlatformStandardDocumentId(
												document.getFormat() == null ? "" : document.getFormat());// 附件documentID
										platform.setPlatformStandardPageCount(document.getPublishInfo().getPageCount()); // 附件页码
									} else {
										platform.setPlatformStandardName("");
										platform.setPlatformStandardFiletype("");
										platform.setPlatformStandardDocumentId("");
										platform.setPlatformStandardPageCount(0);
									}
									platforms.add(platform);
								}
								vo.setPlatforms(platforms);
							} else {
								vo.setEbusinessProductId(0L);
								vo.setEbusinessProductName("");
								vo.setMoney(new BigDecimal(0));
								vo.setSubEbusinessProductId("");
								vo.setSubEbusinessProductName("");
								vo.setTitle("电商准入报告");
								List<PlatformVo> platforms = new ArrayList<PlatformVo>();
								vo.setPlatforms(platforms);
								vo.setCollectStatus("0");
								vo.setBuyStatus("0");
							}
							buysRecordListVo2.seteBusinessInfoVo(vo);
							String shareURL = businessInfoDao.getShareURL(); // 分享URL
							if (shareURL != null) {
								buysRecordListVo2.setShareURL(shareURL + "&reportId=" + buysRecord.getRelatedId());
							} else {
								buysRecordListVo2.setShareURL("无此分享URL");
							}
							buysRecordListVos.add(buysRecordListVo2);
						}
					} else if ("8".equals(buysRecord.getType())) {
						LiveVideo video = liveDao.getLiveVideoById(buysRecord.getRelatedId());
						if (video != null) {
							// start:-把所购买的章节对应的系列返回
							if (video.getFatherId() != null && video.getFatherId() != 0 && video.getFatherId() != -1) {// 假如买的是章节，则返回章节对应的系列
								if (liveDao.getLiveVideoById(video.getFatherId()) != null
										&& liveDao.getLiveVideoById(video.getFatherId()).getFatherId() == 0) {
									if (!fatherIdList.contains(video.getFatherId())) {
										Long[] chapterId = new Long[] { video.getFatherId() };
										List<LiveVideo> videos = liveDao.getLiveVideosById(chapterId);
										buysRecordListVo2.setUrl(videos.get(0).getVideoUrl());
										buysRecordListVo2.setTitle(videos.get(0).getTitle());
										buysRecordListVo2.setVideoId(videos.get(0).getId());
										buysRecordListVos.add(buysRecordListVo2);
									}
								}
							}
							// end:-把所购买的章节对应的系列返回
							if (video.getFatherId() == -1 || video.getFatherId() == 0) {
								// 根据videoId去查询收藏表，看是否已收藏
								boolean collect = collectDao.getCollect(memberId, buysRecord.getRelatedId(), "7");
								if (collect) {
									buysRecordListVo2.setCollectStatus("1");// 已收藏
								} else {
									buysRecordListVo2.setCollectStatus("0");
								}
								buysRecordListVo2.setBuysRecordId(buysRecord.getId());
								buysRecordListVo2.setMoney(buysRecord.getMoney());
								buysRecordListVo2.setPayTime(buysRecord.getPayTime().toString());
								buysRecordListVo2.setType(buysRecord.getType());
								buysRecordListVo2.setUrl(video.getVideoUrl());
								buysRecordListVo2.setTitle(video.getTitle());
								buysRecordListVo2.setVideoId(video.getId());
								buysRecordListVo2.setFatherId(video.getFatherId());
								buysRecordListVo2.setVideoType(video.getVideoType());
								buysRecordListVos.add(buysRecordListVo2);
								if (video.getFatherId() != null && video.getFatherId() == 0) {
									fatherIdList.add(video.getId());
								}
							}
						} else {
							log.error("视频表id为" + buysRecord.getRelatedId() + "的记录已被删除");
						}

					} // end:8
				}
			} // end:for
			buysListResult.setBuysRecordList(buysRecordListVos);
		}
		return buysListResult;
	}

	/**
	 * 公共方法，处理各处得到的list
	 */
	public QualityShareListVo setStandardReadingVoInfo(StandardReading read, Member member, ExpenseTotal buys,
			String collect, String portType, String shareURL) {
		QualityShareListVo vo = new QualityShareListVo();
		try {
			/**
			 * NUll值转换
			 */
			vo.setStandardReadingId(String.valueOf(read.getId()) == null ? "" : String.valueOf(read.getId()));
			vo.setTitle(StringUtil.isBlank(read.getTitle())? "" : read.getTitle());
			vo.setAuthor(read.getAuthorName());
			String time = DateHelper.dataToString(read.getAddTime(), "yyyy-MM-dd HH:mm:ss");
			vo.setTime(time);
			vo.setAuthorIntro(StringUtil.isBlank(member.getIntro())? "" : member.getIntro());
			vo.setPhoto(StringUtil.isBlank(read.getPhoto())? "" : jointUrl + read.getPhoto());
			vo.setPhoto2(StringUtil.isBlank(read.getPhoto2())? "" : jointUrl + read.getPhoto2());
			vo.setPhoto3(StringUtil.isBlank(read.getPhoto3())? "" : jointUrl + read.getPhoto3());
			vo.setPhoto4(StringUtil.isBlank(read.getPhoto4())? "" : jointUrl + read.getPhoto4());
			vo.setCover(StringUtil.isBlank(read.getCover())? defaultPhoto : jointUrl + read.getCover());
			vo.setMoney(read.getMoney());
			vo.setType(StringUtil.isBlank(read.getType())? "" : read.getType());
			vo.setIntro(StringUtil.isBlank(read.getIntro() )? "" : read.getIntro());
			vo.setContentType(StringUtil.isBlank(read.getContentType())? "" : read.getContentType());
			vo.setContent(StringUtil.isBlank(read.getContent())? "" : read.getContent());
			vo.setVoice(StringUtil.isBlank(read.getVoice())? "" : read.getVoice());
			vo.setVoiceDuration(StringUtil.isBlank(read.getVoiceDuration())? "" : read.getVoiceDuration());
			vo.setQualityId(read.getQualityId());
			if (buys == null) {
				vo.setBuyStatus("0");
			} else {
				vo.setBuyStatus(buys.getPayStatus());
			}
			vo.setCollectStatus(collect);
			vo.setShareURL(shareURL); // 分享URL
			if (portType != null && "1".equals(portType)) {// 体系化解读
				List<Adjunct> adjunctList = new ArrayList<Adjunct>();
				/**
				 * 把多个附件的信息拼接起来
				 */
				String documentID = read.getDocumentId();
				String voices = read.getVoice();
				String[] vocstr = voices.split(",");
				String[] strList = documentID.split(",");
				StringBuffer file = new StringBuffer();
				StringBuffer document = new StringBuffer();
				StringBuffer format = new StringBuffer();
				StringBuffer pageCount = new StringBuffer();
				StringBuffer vocsbf = new StringBuffer();

				Adjunct adjunct = new Adjunct();
				for (int i = 0; i <= strList.length - 1; i++) {
					GetDocumentResponse doc = BDocHelper.readDocument(strList[i]);
					file.append(",").append(doc.getTitle());
					document.append(",").append(doc.getDocumentId());
					format.append(",").append(doc.getFormat());
					pageCount.append(",").append(doc.getPublishInfo().getPageCount());
					if (vocstr.length > 0 && vocstr.length >= i + 1) {
						vocsbf.append(",").append(jointUrl + "voice/" + vocstr[i]); // 拼接音频文件
					} else {
						vocsbf.append(","); // 没有音频文件就用空字符串代替
					}
				}
				adjunct.setTitle(read.getTitle());
				adjunct.setIntro(read.getIntro());
				adjunct.setFileName(file.toString().replaceFirst(",", ""));
				adjunct.setDocumentID(document.toString().replaceFirst(",", ""));
				adjunct.setFormat(format.toString().replaceFirst(",", ""));
				adjunct.setPageCount(pageCount.toString().replaceFirst(",", ""));
				if (voices != null && !"".equals(voices)) {
					adjunct.setVoice(vocsbf.toString().replaceFirst(",", ""));
				} else {
					adjunct.setVoice("");
				}
				adjunctList.add(adjunct);
				// 判断该standard_reading 是否是连载
				if (read.getType().equals("1")) {
					List<StandardReading> standardReadings = standardReadDao.getSubByfatherId(read.getId(), null);
					if (ObjValid.isValid(standardReadings)) {
						for (StandardReading standardRead : standardReadings) {
							/**
							 * 把多个附件的信息拼接起来
							 */
							documentID = standardRead.getDocumentId();
							strList = documentID.split(",");
							file = new StringBuffer();
							document = new StringBuffer();
							format = new StringBuffer();
							pageCount = new StringBuffer();

							adjunct = new Adjunct();
							for (int i = 0; i <= strList.length - 1; i++) {
								GetDocumentResponse doc = BDocHelper.readDocument(strList[i]);
								file.append(",").append(doc.getTitle());
								document.append(",").append(doc.getDocumentId());
								format.append(",").append(doc.getFormat());
								pageCount.append(",").append(doc.getPublishInfo().getPageCount());
								if (vocstr.length > 0 && vocstr.length >= i + 1) {
									vocsbf.append(",").append(jointUrl + "voice/" + vocstr[i]); // 拼接音频文件
								} else {
									vocsbf.append(","); // 没有音频文件就用空字符串代替
								}
							}
							adjunct.setTitle(standardRead.getTitle());
							adjunct.setIntro(standardRead.getIntro());
							adjunct.setFileName(file.toString().replaceFirst(",", ""));
							adjunct.setDocumentID(document.toString().replaceFirst(",", ""));
							adjunct.setFormat(format.toString().replaceFirst(",", ""));
							adjunct.setPageCount(pageCount.toString().replaceFirst(",", ""));
							if (voices != null && !"".equals(voices)) {
								adjunct.setVoice(vocsbf.toString().replaceFirst(",", ""));
							} else {
								adjunct.setVoice("");
							}
							adjunctList.add(adjunct);
						}
					}
				}

				vo.setSubStandardReadingList(adjunctList); // 把附件列表封装到解读对象中去
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return vo; // 把单个解读对象封装到列表中去
	}

	@Override
	public ExpenseTotal getProblemRecord(Long expertId, Long problemId) {
		return expenseTotalDao.getProblemRecord(expertId, problemId);
	}

	@Override
	public void updateProblemRecord(ExpenseTotal buyRecord) {
		buyRecord.setPayStatus("2"); // 退款
		expenseTotalDao.update(buyRecord);
	}

	@Override
	public void save(ExpenseTotal recordNew) {
		expenseTotalDao.save(recordNew);
	}

	@Override
	public ExpenseTotal checkBuyStatus(Long memberId, Long relateId) {
		return expenseTotalDao.getBuyStatus(memberId, relateId, "8");
	}

	@Override
	public int findAllBuysByMemberId(Long memberId) {
		return expenseTotalDao.findAllBuysByMemberId(memberId);
	}

	// 根据关联id查询购买记录
	@Override
	public List<ExpenseTotal> findAllBuysByLiveVideoId(Long[] relateId) {
		return this.expenseTotalDao.findAllBuysByLiveVideoId(relateId);
	}


	@Override
	public List<ExpenseTotalVo> findExpenseTotalByCondiction(Long memberId, String type, int pageNo, int pageSize) {
		List<ExpenseTotal> buysRecords = expenseTotalDao.findExpenseTotalByCondiction(memberId, type, pageNo,
				pageSize);
		List<ExpenseTotalVo> buysRecordNewListVos = new ArrayList<>();
		int rowCount = buysRecords.size();
		int startIndex = pageSize * (pageNo - 1);
		int count = (int) rowCount;
		if (startIndex > count) {
			buysRecords = new ArrayList<ExpenseTotal>();
		} else {
			buysRecords = buysRecords.subList(startIndex,
					(pageSize + startIndex) <= rowCount ? (pageSize + startIndex) : rowCount); // 子集合
		}
		if (buysRecords != null && buysRecords.size() > 0) {
			List<Long> fatherIdList = new ArrayList<>();
			for (ExpenseTotal buysRecord : buysRecords) {
				if (!"0".equals(buysRecord.getType())) {
					if ("1".equals(buysRecord.getType())) {
						if (ObjValid.isValid(buysRecord.getRegionId()) && ObjValid.isValid(buysRecord.getRelatedId())
								&& ObjValid.isValid(buysRecord.getRegionId())) {
							Region region = regionDao.get(buysRecord.getRegionId());
							Product product = productDao.get(buysRecord.getRelatedId());
							if (product != null) { // 为了防止出错,让已经被删除掉的东西不出现在已购列表里
								ExpenseTotalVo bNewVo = new ExpenseTotalVo();
								bNewVo.setId(product.getId());
								bNewVo.setRegion(region.getId().intValue());
								bNewVo.setMoney(buysRecord.getOriginalPrice());
								bNewVo.setTitle(product.getName());
								bNewVo.setType(1);
								bNewVo.setArticleName("市场准入报告");
								buysRecordNewListVos.add(bNewVo);
							}
						}
					} else if ("2".equals(buysRecord.getType()) || "3".equals(buysRecord.getType())) {
						if (ObjValid.isValid(buysRecord.getRelatedId())) {
							StandardReading standardReading = standardReadDao.get(buysRecord.getRelatedId());
							if (standardReading != null) { // 为了防止出错,让已经被删除掉的东西不出现在已购列表里
								ExpenseTotalVo bNewVo = new ExpenseTotalVo();
								bNewVo.setId(standardReading.getId());
								bNewVo.setMoney(buysRecord.getOriginalPrice());
								bNewVo.setTitle(standardReading.getTitle());
								if (type.equals("2")) {
									bNewVo.setType(2);
									if (standardReading.getClassify().equals("1")) {
										bNewVo.setArticleType(0);
										bNewVo.setArticleName("标准解读");
									} else {
										Long qualityId = standardReading.getQualityId();
										bNewVo.setArticleType(qualityId.intValue());
										switch (qualityId.intValue()) {
										case 1:
											bNewVo.setArticleName("政策分析");
											bNewVo.setArticleType(1);
											break;
										case 2:
											bNewVo.setArticleName("质量漫谈");
											bNewVo.setArticleType(2);
											break;
										case 3:
											bNewVo.setArticleName("电商品控");
											bNewVo.setArticleType(3);
											break;
										case 4:
											bNewVo.setArticleName("能力验证");
											bNewVo.setArticleType(4);
											break;
										case 5:
											bNewVo.setArticleName("试验室运营");
											bNewVo.setArticleType(5);
											break;
										case 6:
											bNewVo.setArticleName("整改专区");
											bNewVo.setArticleType(6);
											break;
										default:
											log.error("质量分享id{}不存在", qualityId.intValue());
										}
									}
								} else if (type.equals("0")) {
									bNewVo.setArticleName("质量分享");
									bNewVo.setType(2);
								}

								buysRecordNewListVos.add(bNewVo);
							}
						}
					} else if ("4".equals(buysRecord.getType())) {
						if (ObjValid.isValid(buysRecord.getRelatedId())) {
							HotProblem problem = hotReplyDao.getProblem(buysRecord.getRelatedId());
							HotReply hotReply = hotReplyDao.getReply(problem.getExpertId(), problem.getId());
							// 获取专家名字
							Member expert = hotReplyDao.getMember(problem.getExpertId());
							ExpenseTotalVo bNewVo = new ExpenseTotalVo();
							bNewVo.setAskName(expert.getRealname() == null ? "匿名" : expert.getRealname());
							bNewVo.setMoney(buysRecord.getOriginalPrice());
							bNewVo.setTitle(problem.getName());
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
							String askDate = sdf.format(problem.getAddTime());
							bNewVo.setAskDate(askDate);
							bNewVo.setType(4);
							if (problem.getMemberId().equals(memberId)) {
								bNewVo.setAskType(0);// 提问
							} else {
								bNewVo.setAskType(1);// 围观
							}
							// 0 已回答 1专家未回答 2问题失效
							if (hotReply != null) {// 已回答
								bNewVo.setId(hotReply.getId());
								bNewVo.setAskStatus(0);
							} else {
								Date addTime = problem.getAddTime(); // 问题创建时间
								// 注意这里一定要转换成Long类型，要不然小时超过25时会出现范围溢出，从而得不到想要的日期值
								Date twoDaysAgo = new Date(new Date().getTime() - 2 * 24 * 60 * 60 * 1000L);
								/*
								 * 若当前日期减两天, 在问题创建之后,说明该问题已经失效, 否则就有效
								 */
								if (twoDaysAgo.after(addTime)) {
									bNewVo.setAskStatus(2);
								} else {
									bNewVo.setAskStatus(1);
								}
							}
							buysRecordNewListVos.add(bNewVo);
						}
					} else if ("6".equals(buysRecord.getType())) {
						if (ObjValid.isValid(buysRecord.getRelatedId())) {
							EBusinessProduct businessProduct = businessProductDao.get(buysRecord.getRelatedId());
							if (businessProduct != null) { // 为了防止出错,让已经被删除掉的东西不出现在已购列表里
								ExpenseTotalVo bNewVo = new ExpenseTotalVo();
								bNewVo.setId(businessProduct.getId());
								bNewVo.setMoney(buysRecord.getOriginalPrice());
								bNewVo.setTitle(businessProduct.getName());
								bNewVo.setType(6);
								bNewVo.setArticleName("电商准入报告");
								buysRecordNewListVos.add(bNewVo);
							}
						}
					} else if ("8".equals(buysRecord.getType())) {
						LiveVideo video = liveDao.getLiveVideoById(buysRecord.getRelatedId());
						if (video != null) { // 为了防止出错,让已经被删除掉的东西不出现在已购列表里
							if (video.getClassify() == 1 || video.getClassify() == 4) {// 系列或单一
								if (!fatherIdList.contains(video.getId())) {
									ExpenseTotalVo bNewVo = new ExpenseTotalVo();
									bNewVo.setId(video.getId());
									bNewVo.setCover(jointUrl + video.getCover());
									
									if(video.getClassify() == 1) {
										List<LiveVideo> videos = liveDao.allSonVideosByFatherId(video.getId());
										int duration = 0;
										
										for (LiveVideo liveVideo : videos) {
											duration += (liveVideo.getDuration() == null ? 0 : liveVideo.getDuration().intValue());
										}
										bNewVo.setDuration(duration);
									}else if (video.getClassify() == 4) {
										bNewVo.setDuration(video.getDuration() == null ? 0 : video.getDuration().intValue());
									}
									bNewVo.setMoney(buysRecord.getOriginalPrice());
									bNewVo.setTitle(video.getTitle());
									bNewVo.setType(8);
									bNewVo.setArticleName("视频课程");
									bNewVo.setFatherId(video.getFatherId());
									bNewVo.setVideoType(Integer.parseInt(video.getVideoType()));
									bNewVo.setDetailCover(jointUrl + video.getDetailCover());
									buysRecordNewListVos.add(bNewVo);
									fatherIdList.add(video.getId());
								}
							} else if (video.getClassify() == 2) {
								if (!fatherIdList.contains(video.getFatherId())) {
									LiveVideo v = liveDao.getLiveVideoById(video.getFatherId());
									ExpenseTotalVo bNewVo = new ExpenseTotalVo();
									bNewVo.setId(v.getId());
									bNewVo.setCover(jointUrl + v.getCover());
									bNewVo.setDuration(v.getDuration().intValue());
									bNewVo.setMoney(buysRecord.getOriginalPrice());
									bNewVo.setTitle(v.getTitle());
									bNewVo.setType(8);
									bNewVo.setArticleName("视频课程");
									bNewVo.setFatherId(v.getFatherId());
									bNewVo.setDetailCover(jointUrl + v.getDetailCover());
									buysRecordNewListVos.add(bNewVo);
									fatherIdList.add(video.getFatherId());
								}
							}
						} else {
//							log.error("视频表id为" + buysRecord.getRelatedId() + "的记录已被删除");
						}
					} else if ("9".equals(buysRecord.getType())) {
						if (ObjValid.isValid(buysRecord.getRelatedId())) {
							StandardReading standardReading = standardReadDao.get(buysRecord.getRelatedId());
							if (standardReading != null) { // 为了防止出错,让已经被删除掉的东西不出现在已购列表里
								ExpenseTotalVo bNewVo = new ExpenseTotalVo();
								bNewVo.setId(standardReading.getId());
								bNewVo.setMoney(buysRecord.getOriginalPrice());
								bNewVo.setTitle(standardReading.getTitle());
								bNewVo.setArticleType(0);
								bNewVo.setArticleName("订阅专栏");
								bNewVo.setType(9);
								buysRecordNewListVos.add(bNewVo);
							}
						}

					}
				}
			}
		}
		return buysRecordNewListVos;
	}

	@Override
	public List<Long> getRecord(String string, Long id) {
		ExpenseTotal record = new ExpenseTotal();
		record.setType(string);
		record.setRelatedId(id);
		List<ExpenseTotal> list = expenseTotalDao.getBuyRecordByCon(record);
		List<Long> members  = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			Long memberId = list.get(i).getMemberId();
			if(memberId!=null){
				members.add(memberId);
			}
		}
		return members;
	}
	@Override
	public List<ExpenseTotal> getBuysRecordByTypeAndRelatedId(String type, Long relateId) {
		return expenseTotalDao.getBuysRecordByTypeAndRelatedId(type,relateId);
	}

	@Override
	public ExpenseTotal getBuysRecordByOrderNum(String merchantOrderNum) {
		return expenseTotalDao.getBuysRecordByOrderNum(merchantOrderNum);
	}

	@Override
	public void updateExpenseTotal(ExpenseTotal expenseTotal) {
		expenseTotalDao.update(expenseTotal);
	}

	@Override
	public List<ExpenseTotal> getOneDayExpenseTotal(int year, int month, int day, Integer payType) {
		return expenseTotalDao.getOneDayExpenseTotal(year, month, day, payType);
	}
}
