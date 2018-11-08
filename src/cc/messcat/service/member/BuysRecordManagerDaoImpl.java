package cc.messcat.service.member;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.baidubce.services.doc.model.GetDocumentResponse;

import cc.messcat.bases.BaseManagerDaoImpl;
import cc.messcat.entity.BuysRecord;
import cc.messcat.entity.ExpenseTotal;
import cc.messcat.entity.Collect;
import cc.messcat.entity.EBusinessInfo;
import cc.messcat.entity.EBusinessProduct;
import cc.messcat.entity.HotProblem;
import cc.messcat.entity.HotReply;
import cc.messcat.entity.Member;
import cc.messcat.entity.Product;
import cc.messcat.entity.Region;
import cc.messcat.entity.HotReplyFees;
import cc.messcat.entity.Share;
import cc.messcat.entity.StandardReading;
import cc.messcat.vo.Adjunct;
import cc.messcat.vo.BuysRecordListVo;
import cc.messcat.vo.EBusinessInfoVo2;
import cc.messcat.vo.HotReplyPayVo;
import cc.messcat.vo.MyBuysListResult;
import cc.messcat.vo.PlatformVo;
import cc.messcat.vo.QualityShareListVo;
import cc.modules.commons.Pager;
import cc.modules.util.BDocHelper;
import cc.modules.util.DateHelper;
import cc.modules.util.ObjValid;
import cc.modules.util.PropertiesFileReader;

public class BuysRecordManagerDaoImpl extends BaseManagerDaoImpl implements BuysRecordManagerDao {

	private static final long serialVersionUID = 1L;

	private static String defaultPhoto = PropertiesFileReader.getByKey("default.photo.url");// 默认图片
	private static String jointUrl = PropertiesFileReader.getByKey("static.domain");// 图片拼接

	public BuysRecordManagerDaoImpl() {
	}

	public void addBuysRecord(BuysRecord record) {
		this.buysRecordDao.save(record);
	}

	public void modifyBuysRecord(BuysRecord record) {
		this.buysRecordDao.update(record);
	}

	public void removeBuysRecord(BuysRecord record) {
		this.buysRecordDao.delete(record);
	}

	public void removeBuysRecord(Long id) {
		this.buysRecordDao.delete(id);
	}

	public BuysRecord retrieveBuysRecord(Long id) {
		return (BuysRecord) this.buysRecordDao.get(id);
	}

	public List retrieveAllBuysRecords() {
		return this.buysRecordDao.findAll();
	}

	public Pager retrieveBuysRecordsPager(int pageSize, int pageNo) {
		return this.buysRecordDao.getPager(pageSize, pageNo);
	}

	public Pager findBuysRecords(int pageSize, int pageNo, String statu) {
		Pager pager = buysRecordDao.getObjectListByClass(pageSize, pageNo, BuysRecord.class, statu);
		return pager;
	}

	/**
	 * 根据地区ID和产品ID查询(用于产品输入模糊查询时)
	 */
	public BuysRecord getBuysRecordByConSimple(Long regionId, Long productId, Long memberId) {
		StringBuffer sb = new StringBuffer("from BuysRecord where status = 1 ");
		if (ObjValid.isValid(regionId))
			sb.append(" and regionId.id=").append(regionId).append(" ");
		if (ObjValid.isValid(productId))
			sb.append(" and productId.id=").append(productId).append(" ");
		if (ObjValid.isValid(memberId))
			sb.append(" and memberId.id=").append(memberId).append(" ");
		List<BuysRecord> list = buysRecordDao.findByhql(sb.toString());
		if (ObjValid.isValid(list)) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 根据会员ID查询
	 */
	public List getBuysRecordByCon(Long memberId, String type) {
		StringBuffer sb = new StringBuffer("from BuysRecord where status = 1 ");
		if (ObjValid.isValid(type))
			sb.append(" and type = '").append(type).append("' ");
		sb.append(" and payStatus = '1' ");
		if (ObjValid.isValid(memberId))
			sb.append(" and memberId.id = ").append(memberId).append(" ");
		List<BuysRecord> list = buysRecordDao.findByhql(sb.toString());
		return list;
	}

	/**
	 * 根据条件查询
	 * （修改了购买记录表）
	 */
	public List<ExpenseTotal> getBuyRecordByCon(ExpenseTotal record) {
		return buysRecordDao.getBuyRecordByCon(record);
	}

	/**
	 * 根据订单号查询
	 */
	public BuysRecord getBuysRecordByNumber(String number) {
		StringBuffer sb = new StringBuffer("from BuysRecord where status = 1 ");
		if (ObjValid.isValid(number))
			sb.append(" and number = '").append(number).append("' ");
		List<BuysRecord> list = buysRecordDao.findByhql(sb.toString());
		if (ObjValid.isValid(list)) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 综合已购列表接口
	 */
	@SuppressWarnings("unchecked")
	@Override
	public MyBuysListResult findBuysRecordsByCondiction(Long memberId, String type, int pageNo, int pageSize) {
		MyBuysListResult buysListResult = new MyBuysListResult();

		Pager pager = buysRecordDao.findBuysRecordsByCondiction(memberId, type, pageNo, pageSize);
		List<BuysRecord> buysRecords = pager.getResultList();

		buysListResult.setPageNo(pager.getPageNo());
		buysListResult.setPageSize(pager.getPageSize());
		buysListResult.setRowCount(pager.getRowCount());

		List<BuysRecordListVo> buysRecordListVos = new ArrayList<>();
		if (buysRecords != null && buysRecords.size() > 0) {
			for (BuysRecord buysRecord : buysRecords) {
				if (!"0".equals(buysRecord.getType())) {
					BuysRecordListVo buysRecordListVo2 = new BuysRecordListVo();
					// type 0：购买会员套餐 1：购买报告 2：购买标准解读 3：购买质量分享 4：付费咨询 6：电商信息
					buysRecordListVo2.setBuysRecordId(buysRecord.getId());
					buysRecordListVo2.setMoney(buysRecord.getMoney() == null ? new BigDecimal(0) : buysRecord.getMoney());
					buysRecordListVo2.setType(buysRecord.getType());
					buysRecordListVo2.setBuyStatus("1");
					if ("1".equals(buysRecord.getType())) {
						if (ObjValid.isValid(buysRecord.getRegionId()) && ObjValid.isValid(buysRecord.getProductId())
								&& ObjValid.isValid(buysRecord.getRegionId().getId())
								&& ObjValid.isValid(buysRecord.getProductId().getId())) {
							Region region = regionDao.get(buysRecord.getRegionId().getId());
							Product product = productDao.get(buysRecord.getProductId().getId());
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
							//分享用的URL
							String shareURL=productDao.getShareURL();
							if(shareURL!=null){
								buysRecordListVo2.setShareURL(shareURL+"&regionId="+region.getId()+"&productId="+product.getId());
							}else{
								buysRecordListVo2.setShareURL("无此分享URL");
							}
						}

					} else if ("2".equals(buysRecord.getType()) || "3".equals(buysRecord.getType())) {
						if (ObjValid.isValid(buysRecord.getStandardReadId())
								&& ObjValid.isValid(buysRecord.getStandardReadId().getId())) {
							StandardReading standardReading = standardReadDao
									.get(buysRecord.getStandardReadId().getId());
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
							String shareURL = qualityShareDao.getShareURL()+"&standardReadingId=" + buysRecord.getStandardReadId().getId();   //分享URL
							QualityShareListVo standardReadListVo = this.setStandardReadingVoInfo(standardReading,
									member, buysRecord, collectStatus, "1",shareURL);

							buysRecordListVo2.setStandardReadListVo(standardReadListVo);
							buysRecordListVo2.setIsDown("1");
						}
					} else if ("4".equals(buysRecord.getType())) {
						if (ObjValid.isValid(buysRecord.getHotProblemId())) {
							HotReplyPayVo payProblem = new HotReplyPayVo();

							HotProblem problem = hotReplyDao.getProblem(buysRecord.getHotProblemId());
							// 用于判断问题是否被回答,若已回答,则一并获取replyId
							HotReply hotReply = hotReplyDao.getReply(problem.getExpertId(), problem.getId());
							// 获取专家名字
							Member expert = hotReplyDao.getMember(problem.getExpertId());
							payProblem.setAddTime(DateHelper.dataToString(problem.getAddTime(), "yyyy-MM-dd HH:mm:ss"));
							payProblem.setProblem(problem.getName());
							/**
							 * 首先判断购买问题的提问者是不是自己,从而区分是否是围观问题
							 */
							Share share = hotReplyDao.getShare();    // 获取分享URL
							if (!problem.getMemberId().equals(memberId)) { //若memberId不一样,则该问题不是当前用户提出的
								payProblem.setReplyStatus("3");     //围观问题 ,一定是已经被回答的问题
								payProblem.setExpertName("");
								payProblem.setIsReply("true");
								payProblem.setMoney(new BigDecimal(0));
								/*
								 * 围观问题只能是围观别人已经被回答的问题
								 */
								payProblem.setReplyId(String.valueOf(hotReply.getId()));
								buysRecordListVo2.setShareURL(share.getShareURL() + "&replyId=" + String.valueOf(hotReply.getId()));
							} else {        //若相等,则该问题是当前用户提出的
								payProblem.setExpertName(expert.getRealname());
								if (hotReply != null) {   //判断问题是否被回答
									payProblem.setIsReply("true");
									// 获取分享URL
									buysRecordListVo2.setShareURL(share.getShareURL() + "&replyId=" + String.valueOf(hotReply.getId()));
									payProblem.setReplyId(String.valueOf(hotReply.getId()));
									payProblem.setReplyStatus("2");    //已回答的问题
									 HotReplyFees fees=hotReplyDao.getReplyFees(problem.getExpertId());
									 if(problem.getType().equals("0")){
									 payProblem.setMoney(fees.getMoney());
									 }else{
									 payProblem.setMoney(fees.getPrivateMoney());
									 }
								} else {
									Date addTime=problem.getAddTime();    //问题创建时间
									//注意这里一定要转换成Long类型，要不然小时超过25时会出现范围溢出，从而得不到想要的日期值
									Date twoDaysAgo=new Date(new Date().getTime() - 2 * 24 * 60 * 60 * 1000L);
									/*
									 * 若当前日期减两天, 在问题创建之后,说明该问题已经失效,  否则就有效
									 */
									if(twoDaysAgo.after(addTime)){
										payProblem.setIsReply("");
										payProblem.setReplyId("");
										payProblem.setMoney(new BigDecimal(0));
										payProblem.setReplyStatus("1");       //问题已失效
									}else{
										payProblem.setIsReply("false");
										payProblem.setReplyId("");
										payProblem.setMoney(new BigDecimal(0));
										payProblem.setReplyStatus("0");       //有效期的未答问题
									}
								}
							}
							buysRecordListVo2.setPayProblem(payProblem);
						}

					} else if ("6".equals(buysRecord.getType())) {
						if (ObjValid.isValid(buysRecord.getEbusinessProductId())) {
							EBusinessProduct businessProduct = businessProductDao
									.get(buysRecord.getEbusinessProductId());
							buysRecordListVo2.setTitle(businessProduct.getName() + "电商准入报告");
							buysRecordListVo2.setEbusinessId(businessProduct.getId());
							buysRecordListVo2.setIsDown("1");
							// 获取电商信息
							EBusinessInfoVo2 vo = new EBusinessInfoVo2();
							// 根据产品id查出
							List<EBusinessInfo> ebs0 = new ArrayList<EBusinessInfo>();
							StringBuffer sb0 = new StringBuffer("from EBusinessInfo where status = 1 ");
							sb0.append(" and ebusinessProductId = ").append(buysRecord.getEbusinessProductId());
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
							String shareURL=businessInfoDao.getShareURL();      //分享URL
							if(shareURL!=null){
								buysRecordListVo2.setShareURL(shareURL+"&reportId="+buysRecord.getEbusinessProductId());
							}else{
								buysRecordListVo2.setShareURL("无此分享URL");
							}
						}

					}
					buysRecordListVos.add(buysRecordListVo2);
				}

			}
			buysListResult.setBuysRecordList(buysRecordListVos);
		}
		return buysListResult;
	}

	/**
	 * 公共方法，处理各处得到的list
	 */
	public QualityShareListVo setStandardReadingVoInfo(StandardReading read, Member member, BuysRecord buys,
			String collect, String portType, String shareURL) {
		QualityShareListVo vo = new QualityShareListVo();
		try {
			/**
			 * NUll值转换
			 */
			vo.setStandardReadingId(String.valueOf(read.getId()) == null ? "" : String.valueOf(read.getId()));
			vo.setTitle(read.getTitle() == null ? "" : read.getTitle());
			vo.setAuthor(read.getAuthorName());
			String time = DateHelper.dataToString(read.getAddTime(), "yyyy-MM-dd HH:mm:ss");
			vo.setTime(time);
			vo.setAuthorIntro(member.getIntro() == null ? "" : member.getIntro());
			vo.setPhoto(read.getPhoto() == null ? "" : jointUrl + read.getPhoto());
			vo.setPhoto2(read.getPhoto2() == null ? "" : jointUrl + read.getPhoto2());
			vo.setPhoto3(read.getPhoto3() == null ? "" : jointUrl + read.getPhoto3());
			vo.setPhoto4(read.getPhoto4() == null ? "" : jointUrl + read.getPhoto4());
			vo.setCover(read.getCover() == null ? defaultPhoto : jointUrl + read.getCover());
			vo.setMoney(read.getMoney());
			vo.setType(read.getType() == null ? "" : read.getType());
			vo.setIntro(read.getIntro() == null ? "" : read.getIntro());
			vo.setContentType(read.getContentType() == null ? "" : read.getContentType());
			vo.setContent(read.getContent() == null ? "" : read.getContent());
			vo.setVoice(read.getVoice() == null ? "" : read.getVoice());
			vo.setVoiceDuration(read.getVoiceDuration() == null ? "" : read.getVoiceDuration());
			vo.setQualityId(read.getQualityId());
			if (buys == null) {
				vo.setBuyStatus("0");
			} else {
				vo.setBuyStatus(buys.getPayStatus());
			}
			vo.setCollectStatus(collect);
			vo.setShareURL(shareURL);        //分享URL
			if (portType != null && "1".equals(portType)) {
				List<Adjunct> adjunctList = new ArrayList<Adjunct>();
				/**
				 * 把多个附件的信息拼接起来
				 */
				String documentID = read.getDocumentId();
				String[] strList = documentID.split(",");
				StringBuffer file = new StringBuffer();
				StringBuffer document = new StringBuffer();
				StringBuffer format = new StringBuffer();
				StringBuffer pageCount = new StringBuffer();

				Adjunct adjunct = new Adjunct();
				for (String str : strList) {
					GetDocumentResponse doc = BDocHelper.readDocument(str);
					file.append(",").append(doc.getTitle());
					document.append(",").append(doc.getDocumentId());
					format.append(",").append(doc.getFormat());
					pageCount.append(",").append(doc.getPublishInfo().getPageCount());
				}
				adjunct.setTitle(read.getTitle());
				adjunct.setIntro(read.getIntro());
				adjunct.setFileName(file.toString().replaceFirst(",", ""));
				adjunct.setDocumentID(document.toString().replaceFirst(",", ""));
				adjunct.setFormat(format.toString().replaceFirst(",", ""));
				adjunct.setPageCount(pageCount.toString().replaceFirst(",", ""));
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
							for (String str : strList) {
								GetDocumentResponse doc = BDocHelper.readDocument(str);
								file.append(",").append(doc.getTitle());
								document.append(",").append(doc.getDocumentId());
								format.append(",").append(doc.getFormat());
								pageCount.append(",").append(doc.getPublishInfo().getPageCount());
							}
							adjunct.setTitle(standardRead.getTitle());
							adjunct.setIntro(standardRead.getIntro());
							adjunct.setFileName(file.toString().replaceFirst(",", ""));
							adjunct.setDocumentID(document.toString().replaceFirst(",", ""));
							adjunct.setFormat(format.toString().replaceFirst(",", ""));
							adjunct.setPageCount(pageCount.toString().replaceFirst(",", ""));
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
	public BuysRecord getProblemRecord(Long expertId, Long problemId) {
		return buysRecordDao.getProblemRecord(expertId,problemId) ;
	}

	@Override
	public void updateProblemRecord(BuysRecord buyRecord) {
		buyRecord.setPayStatus("2");   //退款
		buysRecordDao.update(buyRecord);
	}

	@Override
	public int findAllBuysByMemberId(Long memberId) {
		return expenseTotalDao.findAllBuysByMemberId(memberId);
	}
}