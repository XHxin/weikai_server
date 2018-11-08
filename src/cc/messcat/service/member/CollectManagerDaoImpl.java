package cc.messcat.service.member;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cc.messcat.bases.BaseManagerDaoImpl;
import cc.messcat.entity.*;
import cc.messcat.vo.CollectList;
import cc.messcat.vo.CollectListVo;
import cc.messcat.vo.CollectVo;
import cc.messcat.vo.QualityShareListVo;
import cc.messcat.vo.ResponseBean;
import cc.modules.commons.Pager;
import cc.modules.constants.Constants;
import cc.modules.util.ObjValid;
import cc.modules.util.PropertiesFileReader;
import cc.modules.util.StandardReadingUtil;

public class CollectManagerDaoImpl extends BaseManagerDaoImpl implements CollectManagerDao {

	private static final long serialVersionUID = 1L;

	private static String defaultPhoto = PropertiesFileReader.getByKey("default.photo.url");// 默认头像

	public CollectManagerDaoImpl() {
	}

	public void addCollect(Collect collect) {
		this.collectDao.save(collect);
	}

	public void modifyCollect(Collect collect) {
		this.collectDao.update(collect);
	}

	public void removeCollect(Collect collect) {
		this.collectDao.delete(collect);
	}

	public void removeCollect(Long id) {
		this.collectDao.delete(id);
	}

	public Collect retrieveCollect(Long id) {
		return (Collect) this.collectDao.get(id);
	}

	public List retrieveAllCollects() {
		return this.collectDao.findAll();
	}

	public Pager retrieveCollectsPager(int pageSize, int pageNo) {
		return this.collectDao.getPager(pageSize, pageNo);
	}

	public Pager findCollects(int pageSize, int pageNo, String statu) {
		Pager pager = collectDao.getObjectListByClass(pageSize, pageNo, Collect.class, statu);
		return pager;
	}

	/**
	 * 根据地区ID和产品ID查询(用于产品输入模糊查询时)
	 */
	public Collect getStandardByConSimple(Long regionId, Long productId, Long memberId) {
		StringBuffer sb = new StringBuffer("from Collect where status = 1 ");
		if (ObjValid.isValid(regionId))
			sb.append(" and regionId=").append(regionId).append(" ");
		if (ObjValid.isValid(productId))
			sb.append(" and relatedId=").append(productId).append(" ");
		if (ObjValid.isValid(memberId))
			sb.append(" and memberId=").append(memberId).append(" ");
		List<Collect> list = collectDao.findByhql(sb.toString());
		if (ObjValid.isValid(list)) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 根据会员ID查询
	 */
	public CollectListVo getStandardByCon(Long memberId, int pageNo, int pageSize, String type) {
		CollectListVo collectListVo = new CollectListVo();
		try {
			List<CollectList> collectlists = new ArrayList<>();

			StringBuffer sb = new StringBuffer("from Collect where status = 1 ");
			if (ObjValid.isValid(memberId))
				sb.append(" and memberId=").append(memberId);
			if (!"0".equals(type)) {
				sb.append(" and type = ").append(type);
			}
			sb.append(" order by addTime desc");
			List<Collect> list = collectDao.findByhql(sb.toString());
			int rowCount = list.size();
			int startIndex = pageSize * (pageNo - 1);
			int count = (int) rowCount;
			if (startIndex > count) {
				list = new ArrayList<Collect>();
			} else {
				list = list.subList(startIndex,
						(pageSize + startIndex) <= rowCount ? (pageSize + startIndex) : rowCount); // 子集合
			}
			if (ObjValid.isValid(list)) {
				collectListVo.setPageNo(pageNo);
				collectListVo.setPageSize(pageSize);
				collectListVo.setRowCount(rowCount);
				for (Collect c : list) {
					CollectList collectlist = new CollectList();
					collectlist.setCollectId(c.getId());
					collectlist.setType(c.getType().toString());
					collectlist.setCollectStatus("1");
					if (ObjValid.isValid(c.getAddTime())) {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
						String time = sdf.format(c.getAddTime());
						collectlist.setCollectTime(time);
					} else {
						collectlist.setCollectTime("");
					}
					if (ObjValid.isValid(c.getType())) {
						if (c.getType() == 1) {// 市场准入报告
							Region region = regionDao.get(c.getRegionId());
							Product product = productDao.get(c.getRelatedId());
							collectlist.setTitle(product.getName() + region.getName() + "市场准入报告");
							collectlist.setRegionId(region.getId());
							collectlist.setProductId(product.getId());
							// 访问网页用的url
							collectlist.setUrl("http://www.cert-map.com/epReportAction!getAppReport.action?regionId="
									+ region.getId() + "&productId=" + product.getId() + "&memberId=" + memberId);

							// 查询购买状态
							ExpenseTotal record = new ExpenseTotal();
							record.setType("1");// 购买类型为报告购买
							record.setMemberId(memberId);
							record.setRegionId(region.getId());
							record.setRelatedId(productDao.get(product.getId()).getId());
							List<ExpenseTotal> records = expenseTotalDao.getBuyRecordByCon(record); // 返回购买状态
							DivideScaleCommon divideScaleCommon = memberDao.getDivideScaleCommon(); // 返回准入报告价格
							if (ObjValid.isValid(records)) {
								collectlist.setBuyStatus("1");// 已购买
								collectlist.setMoney(divideScaleCommon.getUnifyFee()); // 填入价格
							} else {
								collectlist.setBuyStatus("0");// 未购买
								collectlist.setMoney(divideScaleCommon.getUnifyFee()); // 填入价格
							}
							// 分享用的URL
							String shareURL = productDao.getShareURL();
							if (shareURL != null) {
								collectlist.setShareURL(
										shareURL + "&regionId=" + region.getId() + "&productId=" + product.getId());
							} else {
								collectlist.setShareURL("无此分享URL");
							}
						} else if (c.getType() == 2 || c.getType() == 3) {// 标准解读或质量分享
							StandardReading read = standardReadDao.get(c.getRelatedId());
							Member member = memberDao.get(memberId);
							ExpenseTotal buys = expenseTotalDao.getBuys(memberId, read.getId(), type);
							String shareURL = qualityShareDao.getShareURL() + "&standardReadingId=" + c.getRelatedId(); // 分享URL
							QualityShareListVo standardReadListVo = StandardReadingUtil.setStandardReadingVoInfo(read,
									member, buys, "1", null, shareURL);

							collectlist.setTitle(standardReadListVo.getTitle());
							collectlist.setStandardReadListVo(standardReadListVo);
							collectlist.setAuthor(read.getAuthorName());
						} else if (c.getType() == 4) { // 付费咨询
							/**
							 * 收藏表里面放的 是回复表的ID, 而购买记录表里面放的是问题表ID
							 */
							HotReply hotReply = hotReplyDao.getHotReply(c.getRelatedId()); // 拿到回答对象
							collectlist.setTitle(hotReply.getProblemName());
							HotReplyFees fees = hotReplyDao.getReplyFees(hotReply.getExpertId()); // 拿到专家提问收费信息
							// 有可能出现,用户向不同的专家提同样的问题,所以用这个字段区别不同的专家
							ExpenseTotal buys = expenseTotalDao.getReplyBuys(memberId, hotReply.getExpertId(),
									hotReply.getProblemId());
							HotProblem hotProblem = hotReplyDao.getProblem(hotReply.getProblemId()); // 拿到提问对象
							if (hotProblem.getMemberId().equals(memberId)) {// 收藏自己提出的付费咨询问题
								collectlist.setMoney(fees.getMoney());
							} else { // 收藏他人提出的问题
								// 能被他人收藏的问题都是公开提问的问题
								DivideScaleCommon divideScaleCommon = webSiteDao.gainDivideScaleCommon();
								// 目前显示的价格是系统统一设置的付费咨询购买他人问题的价格（现在使用，第三期转充值时弃用）
								collectlist.setMoney(divideScaleCommon.getPayOtherProblemUnifyFee());
								// 这个是三期转充值时使用
								// collectlist.setMoney(fees.getMoney() *
								// site.getPayOthersProblemDiscount() * 0.01);
							}

							collectlist.setReplyId(c.getRelatedId()); // 用于访问热门问答详情的Id

							if (buys != null) { // 若购买记录表记录,则代表已购
								collectlist.setBuyStatus("1");
							} else {
								collectlist.setBuyStatus("0");
							}
							// 获取分享URL
							Share share = hotReplyDao.getShare();
							collectlist.setShareURL(share.getShareURL() + "&replyId=" + c.getRelatedId());

						} else if (c.getType() == 5) {// 新闻详情
							EnterpriseNews news = epNewsDao.get(c.getRelatedId());
							if (news != null) {
								collectlist.setTitle(news.getTitle());
								collectlist.setUrl("http://www.cert-map.com/epFrontNewsAction!news.action?selectNewsId="
										+ news.getId());
								collectlist.setIntro(news.getShortMeta());
								if (ObjValid.isValid(news.getPhoto())) {
									collectlist.setPhoto("http:www.cert-map.com/upload/enterprice/" + news.getPhoto());
								} else {
									collectlist.setPhoto(defaultPhoto);
								}
								collectlist.setShortMeta(news.getShortMeta());
							}
						} else if (c.getType() == 6) {// 电商准入报告
							EBusinessProduct businessProduct = businessProductDao.get(c.getRelatedId()); // 返回电商产品价格
							ExpenseTotal buys = expenseTotalDao.getEbusinessBuys(memberId, c.getRelatedId(), type); // 返回电商产品购买状态
							collectlist.setTitle(businessProduct.getName() + "电商准入报告");
							collectlist.setEbusinessId(businessProduct.getId());
							if (ObjValid.isValid(c.getAddTime())) {
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
								String time = sdf.format(c.getAddTime());
								collectlist.setCollectTime(time);
							} else {
								collectlist.setCollectTime("");
							}
							if (ObjValid.isValid(buys)) {
								collectlist.setBuyStatus("1");
								collectlist.setMoney(businessProduct.getMoney());
							} else {
								collectlist.setBuyStatus("0");
								collectlist.setMoney(businessProduct.getMoney());
							}
							String shareURL = businessInfoDao.getShareURL(); // 分享URL
							if (shareURL != null) {
								collectlist.setShareURL(shareURL + "&reportId=" + c.getRelatedId());
							} else {
								collectlist.setShareURL("无此分享URL");
							}
						} else if (7 == c.getType()) { // 视频
							LiveVideo liveVideo = liveDao.getLiveVideoById(c.getRelatedId());
							if (liveVideo != null) {
								if (liveVideo.getFatherId() != -1) {
									collectlist.setFatherId(0l);
								} else {
									collectlist.setFatherId(-1l);
								}
								collectlist.setVideoType(liveVideo.getVideoType());
								collectlist.setTitle(liveVideo.getTitle());
								collectlist.setLiveVideoId(c.getRelatedId());
								collectlist.setDuration(liveVideo.getDuration());
								collectlist.setViewers(liveVideo.getViewers());
								if (ObjValid.isValid(c.getAddTime())) {
									SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
									String time = sdf.format(c.getAddTime());
									collectlist.setCollectTime(time);
								} else {
									collectlist.setCollectTime("");
								}
								collectlist.setMoney(liveVideo.getPrice());
								collectlist.setPhoto(liveVideo.getCover());
							}
						}
					}
					collectlists.add(collectlist);
				}
				collectListVo.setCollectList(collectlists);
			} else {
				collectListVo.setCollectList(collectlists);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return collectListVo;
	}

	@Override
	public Object addCollection(Collect collect, Member member) {
		Object object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS);
		if (ObjValid.isValid(collect.getStatus()) && collect.getStatus().equals("0")) {
			Collect con = new Collect();
			con.setMemberId(member.getId());
			con.setType(collect.getType());
			con.setRelatedId(collect.getRelatedId());
			if (ObjValid.isValid(collect.getRegionId()))
				con.setRegionId(collect.getRegionId());
			List<Collect> conlist = collectDao.getCollectByCon(con);
			if (ObjValid.isValid(conlist)) {
				for (Collect co : conlist) {
					collectDao.delete(co.getId());
				}
			}
			return object;
		}
		// 查询是否已有收藏记录
		Collect con2 = new Collect();
		con2.setMemberId(member.getId());
		con2.setType(collect.getType());
		con2.setRelatedId(collect.getRelatedId());
		if (ObjValid.isValid(collect.getRegionId()))
			con2.setRegionId(collect.getRegionId());
		List<Collect> conlist2 = collectDao.getCollectByCon(con2);
		if (ObjValid.isValid(conlist2)) {
			if (conlist2.size() > 2) {
				conlist2.remove(0);
				for (Collect c : conlist2) {
					collectDao.delete(c.getId());
				}
			}
			return object;
		}

		collect.setMemberId(member.getId());
		collect.setAddTime(new Date());
		collect.setEditTime(new Date());
		collect.setStatus("1");

		if (1 == collect.getType() && null != collect.getRegionId()) {// 收藏类型1:准入报告
			Standard standard = collectDao.getByRefionIdAndProductId(collect.getRegionId(), collect.getRelatedId());
			if (null == standard) {
				return object;
			}
		} else if (2 == collect.getType()) {// 收藏类型 2：标准解读
			// StandardReading sr =
			// collectDao.getByStandardReadingIdAndType(collect.getRelatedId(),
			// "1");
			// if (null == sr) {
			// return object;
			// }
		} else if (3 == collect.getType()) {// 收藏类型 3：质量分享
			// StandardReading sr =
			// collectDao.getByStandardReadingIdAndType(collect.getRelatedId(),
			// "2");
			// if (null == sr) {
			// return object;
			// }
		} else if (4 == collect.getType()) {// 收藏类型4：付费咨询

		} else if (5 == collect.getType()) {// 收藏类型 5：新闻
			EnterpriseNews en = (EnterpriseNews) this.getObjectById(collect.getRelatedId(),
					EnterpriseNews.class.getName());
			if (null == en) {
				return object;
			}
		} else if (6 == collect.getType()) {// 收藏类型 6=电商
			EBusinessProduct en = (EBusinessProduct) this.getObjectById(collect.getRelatedId(),
					EBusinessProduct.class.getName());
			if (null == en) {
				return object;
			}
		} else if (7 == collect.getType()) {// 收藏类型7=视频
			LiveVideo liveVideo = (LiveVideo) this.getObjectById(collect.getRelatedId(), LiveVideo.class.getName());
			if (null == liveVideo) {
				return object;
			}
		} else if (9 == collect.getType()) {// 收藏类型9=专栏

		} else {
			return object;
		}
		this.saveObject(collect);
		return object;
	}

	@Override
	public Collect getCollect(Long memberId, Long videoId) {
		return collectDao.getCollect(memberId, videoId);
	}

	@Override
	public int findAllCollectByMemberId(Long memberId) {
		return collectDao.findAllCollectByMemberId(memberId);
	}

	@Override
	public boolean getCollect(Long memberId, Long videoId, String type) {
		return collectDao.getCollect(memberId, videoId, type);
	}

	@Override
	public List<CollectVo> getStandardByCon(Long memberId, int pageNo, int pageSize) {
		List<CollectVo> collectListVo = new ArrayList<>();
		try {
			StringBuffer sb = new StringBuffer("from Collect where status = 1 ");
			if (ObjValid.isValid(memberId)) {
				sb.append(" and memberId=").append(memberId);
			}
			sb.append(" order by addTime desc");
			List<Collect> list = collectDao.findByhql(sb.toString());
			int rowCount = list.size();
			int startIndex = pageSize * (pageNo - 1);
			int count = (int) rowCount;
			if (startIndex > count) {
				list = new ArrayList<Collect>();
			} else {
				list = list.subList(startIndex,
						(pageSize + startIndex) <= rowCount ? (pageSize + startIndex) : rowCount); // 子集合
			}
			if (ObjValid.isValid(list)) {
				for (Collect c : list) {
					CollectVo collectVo = new CollectVo();
					collectVo.setId(c.getRelatedId());
					if (c.getType() == 1) {// 市场准入报告
						Region region = regionDao.get(c.getRegionId());
						Product product = productDao.get(c.getRelatedId());
						if (product != null && region != null) { // 有可能收藏的数据已经被删除了,所以就加多判断,避免空指针
							collectVo.setTitle(product.getName());
							collectVo.setRegion(region.getId().intValue());
							collectVo.setType(1);
							collectVo.setVideoType("1");
							collectListVo.add(collectVo);//没有被删除掉的收藏商品才会出现在用户的收藏列表里面
						}
					} else if (c.getType() == 2 || c.getType() == 3) {// 标准解读或质量分享
						StandardReading read = standardReadDao.get(c.getRelatedId());
						if (read != null) {// 有可能收藏的数据已经被删除了,所以就加多判断,避免空指针
							collectVo.setTitle(read.getTitle());
							collectVo.setType(2);
							collectVo.setVideoType("1");
							collectListVo.add(collectVo);//没有被删除掉的收藏商品才会出现在用户的收藏列表里面
						}
					} else if (c.getType() == 4) { // 付费咨询
						/**
						 * 收藏表里面放的 是回复表的ID, 而购买记录表里面放的是问题表ID
						 */
						HotReply hotReply = hotReplyDao.getHotReply(c.getRelatedId()); // 拿到回答对象
						if (hotReply != null) {// 有可能收藏的数据已经被删除了,所以就加多判断,避免空指针
							collectVo.setTitle(hotReply.getProblemName());
							collectVo.setType(4);
							collectVo.setVideoType("1");
							collectListVo.add(collectVo);
						}
					} else if (c.getType() == 5) {// 动态咨询
						EnterpriseNews news = epNewsDao.get(c.getRelatedId());
						if (news != null) {// 有可能收藏的数据已经被删除了,所以就加多判断,避免空指针
							collectVo.setTitle(news.getTitle());
							collectVo.setType(5);
							collectVo.setVideoType("1");
							collectListVo.add(collectVo);//没有被删除掉的收藏商品才会出现在用户的收藏列表里面
						}
					} else if (c.getType() == 6) {// 电商准入报告
						EBusinessProduct businessProduct = businessProductDao.get(c.getRelatedId()); // 返回电商产品价格
						if (businessProduct != null) {// 有可能收藏的数据已经被删除了,所以就加多判断,避免空指针
							collectVo.setTitle(businessProduct.getName());
							collectVo.setType(6);
							collectVo.setVideoType("1");
							collectListVo.add(collectVo);
						}
					} else if (7 == c.getType()) { // 视频
						LiveVideo liveVideo = liveDao.getLiveVideoById(c.getRelatedId());
						if (liveVideo != null) {// 有可能收藏的数据已经被删除了,所以就加多判断,避免空指针
							collectVo.setTitle(liveVideo.getTitle());
							collectVo.setType(8);
							if(liveVideo.getBespeakStatus().equals("1") && liveVideo.getVideoType().equals("0")) {
								collectVo.setVideoType("0");
							}else {
								collectVo.setVideoType("1");
							}
							if (liveVideo.getFatherId() == 0L) {
								collectVo.setRegion(0);
							} else {
								collectVo.setRegion(1);
							}
							if (liveVideo.getVideoType().equals("0")) {
								collectVo.setVideoType("0");// 直播
							} else {
								collectVo.setVideoType("1");// 非直播
							}
							collectListVo.add(collectVo);//没有被删除掉的收藏商品才会出现在用户的收藏列表里面
						}
					} else if (9 == c.getType()) {// 专栏
						StandardReading read = standardReadDao.get(c.getRelatedId());
						if (read != null) {// 有可能收藏的数据已经被删除了,所以就加多判断,避免空指针
							collectVo.setTitle(read.getTitle());
							collectVo.setType(9);
							collectVo.setVideoType("1");
							collectListVo.add(collectVo);//没有被删除掉的收藏商品才会出现在用户的收藏列表里面
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return collectListVo;
	}

}