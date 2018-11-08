package cc.messcat.service.standardread;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.baidubce.services.doc.model.GetDocumentResponse;
import cc.messcat.bases.BaseManagerDaoImpl;
import cc.messcat.entity.ExpenseTotal;
import cc.messcat.entity.Member;
import cc.messcat.entity.Share;
import cc.messcat.entity.StandardReading;
import cc.messcat.vo.Adjunct;
import cc.messcat.vo.StandReadingVo;
import cc.messcat.vo.StandardReadListResultH5;
import cc.messcat.vo.StandardReadListVo;
import cc.messcat.vo.StandardReadListVo2;
import cc.messcat.vo.StandardReadListVo3;
import cc.messcat.vo.StandardReadListVo5;
import cc.modules.commons.Pager;
import cc.modules.util.BDocHelper;
import cc.modules.util.CollectionUtil;
import cc.modules.util.DateHelper;
import cc.modules.util.PropertiesFileReader;

@SuppressWarnings("serial")
public class StandardReadManagerDaoImpl extends BaseManagerDaoImpl implements StandardReadManagerDao {

	private static String defaultPhoto = PropertiesFileReader.getByKey("default.photo.url");// 默认图片
	private static String jointUrl = PropertiesFileReader.getByKey("static.domain");// 图片拼接

	@Override
	public Pager getStandardReadingList(Integer pageNo, Integer pageSize2, Member puMember) {
		List<StandardReadListVo> resultList = new ArrayList<StandardReadListVo>();
		List<StandardReading> standardList = standardReadDao.getStandardReadList(pageNo, pageSize2); // 拿到分页的连载列表
		if (CollectionUtil.isListNotEmpty(standardList)) {
			resultList = this.getVoList(standardList, puMember);
		}
		return new Pager(pageSize2, pageNo, standardList.size(), resultList);
	}

	/**
	 * 按标准号搜索出列表
	 */
	@Override
	public Pager getStandardReadingList3(String standardCode, String type, int pageNo, Integer pageSize,
			Member puMember) {
		List<StandardReading> standList = standardReadDao.getStandardReadingList3(standardCode, type, pageNo, pageSize);
		List<StandardReadListVo> resultList = new ArrayList<StandardReadListVo>();
		if (!standList.isEmpty() && standList.size() > 0) {
			List<StandardReading> list = new ArrayList<StandardReading>();
			/**
			 * 定义一个跳出当前循环的标志outer
			 */
			outer: for (int i = (pageNo - 1) * pageSize; i <= standList.size() - 1; i++) {
				list.add(standList.get(i));
				if (list.size() >= pageSize) {
					break outer;
				}
			}
			resultList = this.getVoList(list, puMember);
		}
		return new Pager(pageSize, pageNo, standList.size(), resultList);
	}

	/**
	 * 根据关键字模糊查询标准解读
	 */
	@Override
	public Pager getStandardReadingList4(String searchKeyWord, int pageNo, int pageSize) {

		List<StandardReading> stand_List = standardReadDao.getStandardReadingList4(searchKeyWord, pageNo, pageSize);
		List<StandReadingVo> standList = new ArrayList<StandReadingVo>();
		if (stand_List != null && !stand_List.isEmpty() && stand_List.size() > 0) {
			/**
			 * 定义一个跳出当前循环的标志outer
			 */
			outer: for (int i = (pageNo - 1) * pageSize; i < stand_List.size(); i++) {
				StandReadingVo standReadingVo = new StandReadingVo();
				standReadingVo.setTitle(stand_List.get(i).getTitle());
				standReadingVo.setEditTime(stand_List.get(i).getAddTime());
				standReadingVo.setAuthorName(stand_List.get(i).getAuthorName());
				standReadingVo.setCover(
						stand_List.get(i).getCover() == null ? defaultPhoto : jointUrl + stand_List.get(i).getCover());
				standReadingVo.setMoney(stand_List.get(i).getMoney());
				standList.add(standReadingVo);
				if (standList.size() > pageSize - 1) {
					break outer;
				}
			}
		}
		return new Pager(pageSize, pageNo, stand_List.size(), standList);
	}

	/**
	 * 热门专栏查看更多
	 */

	@SuppressWarnings("unchecked")
	@Override
	public Pager getStandardReadingList5(String type, int pageNo, int pageSize, String isRecommend, Member puMember) {
		List<StandardReadListVo> resultList = new ArrayList<StandardReadListVo>();
		Pager pager = standardReadDao.getStandardReadingList5(type, pageNo, pageSize, isRecommend);
		List<StandardReading> standardList = pager.getResultList();
		if (CollectionUtil.isListNotEmpty(standardList)) {
			resultList = this.getVoList(standardList, puMember);
		}
		return new Pager(pageSize, pageNo, pager.getRowCount(), resultList);
	}

	/*
	 * 标准解读详情
	 */
	@Override
	public List<StandardReadListVo> searchStandardReading(String type, Long standardReadingId, Member puMember) {
		List<StandardReadListVo> resultList = new ArrayList<StandardReadListVo>();
		List<StandardReading> standardList = standardReadDao.searchStandardReading(type, standardReadingId);
		if (CollectionUtil.isListNotEmpty(standardList)) {
			resultList = this.getVoList(standardList, puMember);
		}
		return resultList;
	}

	/**
	 * 个人中心处的"我的解读"信息,与其它各处的区别就是，此处没有“收藏状态”和“购买状态”
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Pager findList(String type, int pageNo, int pageSize, Long memberId) {
		List<StandardReadListVo2> resultList = new ArrayList<StandardReadListVo2>();
		Pager pager = standardReadDao.getHisStandardReadList(pageNo, pageSize, memberId, type); // 拿到分页的连载解读
		List<StandardReading> standardList = pager.getResultList();
		if (CollectionUtil.isListNotEmpty(standardList)) {
			for (StandardReading entity : standardList) {
				StandardReadListVo2 vo = new StandardReadListVo2();
				Member member = standardReadDao.getMember(memberId);
				/**
				 * NUll值转换
				 */
				vo.setStandardReadingId(String.valueOf(entity.getId()) == null ? "" : String.valueOf(entity.getId()));
				vo.setTitle(entity.getTitle() == null ? "" : entity.getTitle());
				if (member.getRealname() != null && !"".equals(member.getRealname())) {
					vo.setAuthor(member.getRealname());
				} else {
					vo.setAuthor(entity.getAuthorName() == null ? "" : entity.getAuthorName());
				}
				vo.setAuthorIntro(member.getIntro() == null ? "" : member.getIntro());

				String time = DateHelper.dataToString(entity.getAddTime(), "yyyy-MM-dd HH:mm:ss");
				vo.setTime(time);
				vo.setPhoto(entity.getPhoto() == null ? "" : jointUrl + entity.getPhoto());
				vo.setPhoto2(entity.getPhoto2() == null ? "" : jointUrl + entity.getPhoto2());
				vo.setPhoto3(entity.getPhoto3() == null ? "" : jointUrl + entity.getPhoto3());
				vo.setPhoto4(entity.getPhoto4() == null ? "" : jointUrl + entity.getPhoto4());
				vo.setCover(entity.getCover() == null ? defaultPhoto : jointUrl + entity.getCover());
				vo.setMoney(entity.getMoney());
				vo.setType(entity.getType() == null ? "" : entity.getType());
				vo.setIntro(entity.getIntro() == null ? "" : entity.getIntro());
				vo.setContentType(entity.getContentType() == null ? "" : entity.getContentType());
				vo.setContent(entity.getContent() == null ? "" : entity.getContent());
				vo.setVoice(entity.getVoice() == null ? "" : entity.getVoice());
				vo.setVoiceDuration(entity.getVoiceDuration() == null ? "" : entity.getVoiceDuration());

				List<Adjunct> adjunctList = new ArrayList<Adjunct>();
				/**
				 * 获取解读列表
				 */
				if (entity.getType().equals("0")) {
					/**
					 * 拿到附件列表
					 */
					Adjunct subVo = new Adjunct();
					subVo.setTitle(entity.getTitle());
					subVo.setIntro(entity.getIntro());
					// 解读中可能没有附件信息,所以要判断
					if (entity.getDocumentId() != null && !"".equals(entity.getDocumentId())) {
						String documentID = entity.getDocumentId();
						String[] strList = documentID.split(",");
						StringBuffer file = new StringBuffer();
						StringBuffer document = new StringBuffer();
						StringBuffer format = new StringBuffer();
						StringBuffer pageCount = new StringBuffer();
						/**
						 * 把多个附件的信息拼接起来
						 */
						for (String str : strList) {
							GetDocumentResponse doc = BDocHelper.readDocument(str);
							file.append(",").append(doc.getTitle());
							document.append(",").append(doc.getDocumentId());
							format.append(",").append(doc.getFormat());
							pageCount.append(",").append(doc.getPublishInfo().getPageCount());
						}
						subVo.setFileName(file.toString().replaceFirst(",", ""));
						subVo.setDocumentID(document.toString().replaceFirst(",", ""));
						subVo.setFormat(format.toString().replaceFirst(",", ""));
						subVo.setPageCount(pageCount.toString().replaceFirst(",", ""));
						adjunctList.add(subVo);
					}
				}

				vo.setSubStandardReadingList(adjunctList); // 把附件列表封装到解读对象中去

				resultList.add(vo); // 把单个解读对象封装到列表中去
			}
		}
		return new Pager(pageSize, pageNo, pager.getRowCount(), resultList);
	}

	/**
	 * 得到"他的解读"信息
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Pager getHisStandardReadList(int pageNo, int pageSize, Long expertId, String type, Member puMember) {
		List<StandardReadListVo> resultList = new ArrayList<StandardReadListVo>();
		Pager pager = standardReadDao.getHisStandardReadList(pageNo, pageSize, expertId, type); // 拿到分页的连载解读
		List<StandardReading> standardList = pager.getResultList();
		if (CollectionUtil.isListNotEmpty(standardList)) {
			resultList = this.getVoList(standardList, puMember);
		}
		return new Pager(pageSize, pageNo, pager.getRowCount(), resultList);
	}

	public StandardReading retrieveStandardReading(Integer id) {
		return (StandardReading) this.standardReadDao.get(Long.valueOf(id));
	}

	/**
	 * 公共方法，处理各处得到的list memberId是为了拿到购买和收藏记录
	 */
	public List<StandardReadListVo> getVoList(List<StandardReading> standardList, Member puMember) {
		List<StandardReadListVo> resultList = new ArrayList<StandardReadListVo>();
		for (StandardReading entity : standardList) {
			StandardReadListVo vo = new StandardReadListVo();
			Member member = new Member();
			boolean collect = false;
			ExpenseTotal buys = null;
			/**
			 * 若专家拿不到,则专家名字 从StandardReading里面拿AuthorName ,专家简介为空
			 */
			if (entity.getAuthor() != null) {
				member = standardReadDao.getMember(entity.getAuthor().getId());
				vo.setExpertHeadImg(
						entity.getAuthor().getPhoto() == null ? "" : jointUrl + entity.getAuthor().getPhoto());
				vo.setExpertField(entity.getAuthor().getField() == null ? "" : entity.getAuthor().getField());
				vo.setAuthorIntro(member.getIntro() == null ? "" : member.getIntro());
				if (member.getRealname() != null && !"".equals(member.getRealname())) {
					vo.setAuthor(member.getRealname());
				} else {
					vo.setAuthor(entity.getAuthorName() == null ? "" : entity.getAuthorName());
				}
			} else {
				vo.setAuthor(entity.getAuthorName() == null ? "" : entity.getAuthorName());
				vo.setAuthorIntro("");
				vo.setExpertHeadImg("");
				vo.setExpertField("");
			}
			/*
			 * 游客访问的话,就不用查询收藏和购买信息了
			 */
			if (puMember != null && puMember.getId() != null && puMember.getId() > 0) {
				collect = standardReadDao.getCollect(puMember.getId(), entity.getId());
				buys = standardReadDao.getisBuyStand(puMember.getId(), entity.getId());
			}
			/**
			 * NUll值转换
			 */
			vo.setAuthorId(entity.getAuthor().getId().toString());
			vo.setStandardReadingId(String.valueOf(entity.getId()) == null ? "" : String.valueOf(entity.getId()));
			vo.setTitle(entity.getTitle() == null ? "" : entity.getTitle());
			String time = DateHelper.dataToString(entity.getAddTime(), "yyyy-MM-dd HH:mm:ss");
			vo.setTime(time);
			vo.setPhoto(entity.getPhoto() == null || entity.getPhoto().isEmpty()? "" : jointUrl + entity.getPhoto());
			vo.setPhoto2(entity.getPhoto2() == null || entity.getPhoto2().isEmpty()? "" : jointUrl + entity.getPhoto2());
			vo.setPhoto3(entity.getPhoto3() == null || entity.getPhoto3().isEmpty()? "" : jointUrl + entity.getPhoto3());
			vo.setPhoto4(entity.getPhoto4() == null || entity.getPhoto4().isEmpty()? "" : jointUrl + entity.getPhoto4());
			vo.setCover(entity.getCover() == null || entity.getCover().isEmpty()? defaultPhoto : jointUrl + entity.getCover());
			vo.setLinePrice(entity.getLinePrice()==null? new BigDecimal(0):entity.getLinePrice());
			vo.setMoney(entity.getMoney());
			vo.setType(entity.getType() == null ? "" : entity.getType());
			vo.setAuthorId(entity.getAuthor() == null ? "" : entity.getAuthor().getId().toString());
			vo.setIntro(entity.getIntro() == null ? "" : entity.getIntro());
			vo.setContentType(entity.getContentType() == null ? "" : entity.getContentType());
			vo.setContent(entity.getContent() == null ? "" : entity.getContent());
			vo.setVoice(entity.getVoice() == null ? "" : entity.getVoice());
			vo.setVoiceDuration(entity.getVoiceDuration() == null ? "" : entity.getVoiceDuration());
			vo.setClassify(Integer.valueOf(entity.getClassify()));
			if (entity.getQualityId() != null) {
				vo.setQualityId(Integer.valueOf(String.valueOf(entity.getQualityId())));
			} else {
				vo.setQualityId(0);// 若没有qualityId, 则返回0
			}
			/*
			 * 分享URL
			 */
			Share share = standardReadDao.getShare();
			vo.setShareURL(share.getShareURL() + "&standardReadingId=" + entity.getId());
			if (collect == true) {
				vo.setCollectStatus(1);
			}
			if (buys != null) {
				if (buys.getPayStatus().equals("1")) {
					vo.setBuyStatus(1);
					vo.setViewStatus(1);
				}
			}

			// 获取当前解读免费类型
			if (entity.getIsMonthFree().equals("1")) {// 年月共同免费
				vo.setMemberType(1);
			} else if (!entity.getIsMonthFree().equals("1") && entity.getIsYearFree().equals("1")) {
				vo.setMemberType(2);
			} else {
				vo.setMemberType(0);
			}

			/*
			 * 判断游客身份登录
			 */
			if (puMember != null && puMember.getId() != null && puMember.getId() > 0) {
				// 如果解读为未购买，则判断当前会员是否是vip会员，如果是vip会员，再判断解读是否对相应类型的vip会员免费
				if (vo.getBuyStatus() == 0) {
					vo.setViewStatus(0);
					// 判断当前会员是否是vip会员
					if (puMember.getGrade() != null && puMember.getGrade().equals("1")) {
						if (puMember.getYearEndTime().before(new Date())) {// 年费到期
							if (puMember.getEndTime().before(new Date())) {// 月费到期
								puMember.setGrade("0");
								puMember.setType("2");
							} else {
								puMember.setGrade("1");
								puMember.setType("1");
							}
						} else {
							puMember.setGrade("1");
							puMember.setType("0");
						}
						memberDao.update(puMember);
						
						// 如果是则判断目前的会员到期时间是否已到期
						Date now = new Date();
						if (now.before(puMember.getEndTime()) || now.before(puMember.getYearEndTime())) {// vip未过期
							switch (vo.getMemberType()) {
							case 1:
								vo.setViewStatus(1);
								break;
							case 2:
								if (puMember.getType().equals("0"))
									vo.setViewStatus(1);
								break;
							}
						} else {
							switch (vo.getMemberType()) {
							case 1:
								vo.setViewStatus(1);
								break;
							case 2:
								if (puMember.getType().equals("0"))
									vo.setViewStatus(1);
								break;
							}

						}
					}
				}
			} else {
				vo.setViewStatus(0); // 不可查看状态
			}
			List<Adjunct> adjunctList = new ArrayList<Adjunct>();

			vo.setSubStandardReadingList(adjunctList); // 把附件列表封装到解读对象中去

			resultList.add(vo); // 把单个解读对象封装到列表中去
		}
		return resultList;
	}

	/**
	 * 标准解读连载里面的子解读列表
	 */
	@Override
	public List<Adjunct> getStandardRead(Long standardReadingId, String type) {
		List<Adjunct> adjunctList = new ArrayList<Adjunct>();
		/**
		 * 拿到解读分享 连载中的附件信息,并拼到解读的附件列表中
		 */
		if (type.equals("1")) {
			// 获取连载中的附件
			List<StandardReading> standardList = standardReadDao.getSubStandardList3(standardReadingId);
			if (CollectionUtil.isListNotEmpty(standardList)) {
				List<Adjunct> list = this.getAdjuct(standardList);
				for (Adjunct entity : list) {
					adjunctList.add(entity);
				}
			}
			/**
			 * 获取连载里面的单一解读
			 */
			List<StandardReading> sublist = standardReadDao.getSubStandardList2(standardReadingId);
			if (CollectionUtil.isListNotEmpty(sublist)) {
				List<Adjunct> list2 = this.getAdjuct(sublist);
				for (Adjunct entity : list2) {
					adjunctList.add(entity);
				}
			}
		} else { // 获取单一解读里面的附件

			List<StandardReading> sublist = standardReadDao.getSubStandardList4(standardReadingId);
			if (CollectionUtil.isListNotEmpty(sublist)) {
				List<Adjunct> list3 = this.getAdjuct(sublist);
				for (Adjunct entity : list3) {
					adjunctList.add(entity);
				}
			}
		}
		return adjunctList;
	}

	/**
	 * 公共方法，处理附件信息
	 */
	private List<Adjunct> getAdjuct(List<StandardReading> standList) {
		List<Adjunct> resultList = new ArrayList<Adjunct>();
		for (StandardReading subRead : standList) {
			/**
			 * 拿到附件列表
			 */
			// 解读中可能没有附件信息,所以要判断
			if (subRead.getDocumentId() != null && !"".equals(subRead.getDocumentId())) {
				String documentID = subRead.getDocumentId();
				String voices = subRead.getVoice();
				String titiles=subRead.getTitle();
				String[] strList = documentID.split(",");
				String[] vocstr = (voices == null? null:voices.split(","));
				String[] titstr = titiles.split(",");
				/*StringBuffer file = new StringBuffer();
				StringBuffer document = new StringBuffer();
				StringBuffer format = new StringBuffer();
				StringBuffer pageCount = new StringBuffer();
				StringBuffer vocsbf = new StringBuffer();*/
				/**
				 * 把多个附件的信息拼接起来
				 */
				for (int i = 0; i <= strList.length - 1; i++) {
					Adjunct subVo = new Adjunct();
					subVo.setIntro(subRead.getIntro());
					GetDocumentResponse doc = BDocHelper.readDocument(strList[i]);
					/*file.append(",").append(doc.getTitle());
					document.append(",").append(doc.getDocumentId());
					format.append(",").append(doc.getFormat());
					pageCount.append(",").append(doc.getPublishInfo().getPageCount());*/
					/*if (voices != null && !"".equals(voices)) {
						if (voices.contains(",")) {
							String[] vocstr = voices.split(",");
							if (vocstr.length > 0 && vocstr.length >= i + 1) {
								vocsbf.append(",").append(jointUrl + "voice/" + vocstr[i]); // 拼接音频文件
							} else {
								vocsbf.append(","); // 没有音频文件就用空字符串代替
							}

						} else {
							vocsbf.append(jointUrl + "voice/" + subRead.getVoice());
						}
					} else {
						vocsbf.append(",");
					}
					subVo.setFileName(file.toString().replaceFirst(",", ""));
					subVo.setDocumentID(document.toString().replaceFirst(",", ""));
					subVo.setFormat(format.toString().replaceFirst(",", ""));
					subVo.setPageCount(pageCount.toString().replaceFirst(",", ""));
					if (voices != null && !"".equals(voices)) {
						subVo.setVoice(vocsbf.toString().replaceFirst(",", ""));
					} else {
						subVo.setVoice("");
					}*/
					
					subVo.setFileName(doc.getTitle());
					subVo.setDocumentID(doc.getDocumentId());
					subVo.setFormat(doc.getFormat());
					subVo.setPageCount(String.valueOf(doc.getPublishInfo().getPageCount()));
					/*if (voices != null && !"".equals(voices)) {
					if (voices.contains(",")) {
						String[] vocstr = voices.split(",");
						if (vocstr.length > 0 && vocstr.length >= i) {
							subVo.setVoice(jointUrl + "voice/"+vocstr[i]);
							} 
						} else {
						subVo.setVoice(jointUrl + "voice/" + subRead.getVoice());
						}
					} else {
						subVo.setVoice("");
					}*/
					if(vocstr[i]!=null && !"".equals(vocstr[i]) && !vocstr[i] .equals("0")) {
						subVo.setVoice(jointUrl + "voice/"+vocstr[i]);
					}else {
						subVo.setVoice("");
					}
					if(!"1".equals(titstr[i])) {
						subVo.setTitle(titstr[i]);
					}else {
						subVo.setTitle("");
					}
					resultList.add(subVo);
				}
			}
		}
		return resultList;
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<-------H5分界线---------<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	/**
	 * 专供H5的 热门专栏推荐列表
	 */
	@Override
	public StandardReadListResultH5 getStandardReadingListH5(int pageNo, int pageSize) {
		StandardReadListResultH5 resultList = new StandardReadListResultH5();
		List<StandardReadListVo> standardReadList = new ArrayList<StandardReadListVo>();// 标准解读
		List<StandardReadListVo> qualityList1 = new ArrayList<StandardReadListVo>(); // 政策分析
		List<StandardReadListVo> qualityList2 = new ArrayList<StandardReadListVo>(); // 质量漫谈
		List<StandardReadListVo> qualityList3 = new ArrayList<StandardReadListVo>(); // 电商品控
		List<StandardReadListVo> qualityList4 = new ArrayList<StandardReadListVo>(); // 能力验证
		List<StandardReadListVo> qualityList5 = new ArrayList<StandardReadListVo>(); // 试验室运营
		List<StandardReadListVo> qualityList6 = new ArrayList<StandardReadListVo>(); // 整改专区
		List<StandardReading> list = standardReadDao.getStandardReadingListH5(pageNo, pageSize);
		if (CollectionUtil.isListNotEmpty(list)) {
			for (StandardReading entity : list) {
				StandardReadListVo vo = new StandardReadListVo();
				Member member = entity.getAuthor();
				/**
				 * NUll值转换
				 */
				vo.setStandardReadingId(String.valueOf(entity.getId()) == null ? "" : String.valueOf(entity.getId()));
				vo.setTitle(entity.getTitle() == null ? "" : entity.getTitle());
				if (member.getRealname() != null && !"".equals(member.getRealname())) {
					vo.setAuthor(member.getRealname());
				} else {
					vo.setAuthor(entity.getAuthorName() == null ? "" : entity.getAuthorName());
				}
				vo.setAuthorId(entity.getAuthor().getId().toString());
				vo.setAuthorIntro(member.getIntro() == null ? "" : member.getIntro());
				String time = DateHelper.dataToString(entity.getAddTime(), "yyyy-MM-dd HH:mm:ss");
				vo.setTime(time);
				vo.setPhoto(entity.getPhoto() == null ? "" : jointUrl + entity.getPhoto());
				vo.setPhoto2(entity.getPhoto2() == null ? "" : jointUrl + entity.getPhoto2());
				vo.setPhoto3(entity.getPhoto3() == null ? "" : jointUrl + entity.getPhoto3());
				vo.setPhoto4(entity.getPhoto4() == null ? "" : jointUrl + entity.getPhoto4());
				vo.setCover(entity.getCover() == null ? defaultPhoto : jointUrl + entity.getCover());
				vo.setMoney(entity.getMoney());
				vo.setType(entity.getType() == null ? "" : entity.getType());
				vo.setIntro(entity.getIntro() == null ? "" : entity.getIntro());
				vo.setContentType(entity.getContentType() == null ? "" : entity.getContentType());
				vo.setContent(entity.getContent() == null ? "" : entity.getContent());
				vo.setVoice(entity.getVoice() == null ? "" : entity.getVoice());
				vo.setVoiceDuration(entity.getVoiceDuration() == null ? "" : entity.getVoiceDuration());
				vo.setQualityId(entity.getQualityId() == null ? 0 : entity.getQualityId().intValue());
				vo.setClassify(Integer.valueOf(entity.getClassify()));
				vo.setStandardReadingId(String.valueOf(entity.getId()));

				List<Adjunct> adjunctList = new ArrayList<Adjunct>();
				/**
				 * 获取解读列表
				 */
				if (entity.getType().equals("0")) {
					/**
					 * 拿到附件列表
					 */
					Adjunct subVo = new Adjunct();
					subVo.setTitle(entity.getTitle());
					subVo.setIntro(entity.getIntro());
					// 解读中可能没有附件信息,所以要判断
					if (entity.getDocumentId() != null && !"".equals(entity.getDocumentId())) {
						String documentID = entity.getDocumentId();
						String[] strList = documentID.split(",");
						StringBuffer file = new StringBuffer();
						StringBuffer document = new StringBuffer();
						StringBuffer format = new StringBuffer();
						StringBuffer pageCount = new StringBuffer();
						/**
						 * 把多个附件的信息拼接起来
						 */
						for (String str : strList) {
							GetDocumentResponse doc = BDocHelper.readDocument(str);
							file.append(",").append(doc.getTitle());
							document.append(",").append(doc.getDocumentId());
							format.append(",").append(doc.getFormat());
							pageCount.append(",").append(doc.getPublishInfo().getPageCount());
						}
						subVo.setFileName(file.toString().replaceFirst(",", ""));
						subVo.setDocumentID(document.toString().replaceFirst(",", ""));
						subVo.setFormat(format.toString().replaceFirst(",", ""));
						subVo.setPageCount(pageCount.toString().replaceFirst(",", ""));
						adjunctList.add(subVo);
					}
				}
				vo.setSubStandardReadingList(adjunctList); // 把附件列表封装到解读对象中去
				if (entity.getQualityId() == null) {
					standardReadList.add(vo);
				} else if (entity.getQualityId() == 1) {
					qualityList1.add(vo);
				} else if (entity.getQualityId() == 2) {
					qualityList2.add(vo);
				} else if (entity.getQualityId() == 3) {
					qualityList3.add(vo);
				} else if (entity.getQualityId() == 4) {
					qualityList4.add(vo);
				} else if (entity.getQualityId() == 5) {
					qualityList5.add(vo);
				} else if (entity.getQualityId() == 6) {
					qualityList6.add(vo);
				}
				resultList.setStandardReadList(standardReadList);
				resultList.setQualityList1(qualityList1);
				resultList.setQualityList2(qualityList2);
				resultList.setQualityList3(qualityList3);
				resultList.setQualityList4(qualityList4);
				resultList.setQualityList5(qualityList5);
				resultList.setQualityList6(qualityList6);
			}
		}
		return resultList;
	}

	@Override
	public List<Adjunct> getStandardReadH5(Long standardReadingId, String type) {
		List<Adjunct> adjunctList = new ArrayList<Adjunct>();
		/**
		 * 拿到解读分享 连载中的附件信息,并拼到解读的附件列表中
		 */
		if (type.equals("1")) {
			// 获取连载中的附件
			List<StandardReading> standardList = standardReadDao.getSubStandardList3(standardReadingId);
			if (CollectionUtil.isListNotEmpty(standardList)) {
				List<Adjunct> list = this.getAdjuctH5(standardList);
				for (Adjunct entity : list) {
					adjunctList.add(entity);
				}
			}
			/**
			 * 获取连载里面的单一解读
			 */
			List<StandardReading> sublist = standardReadDao.getSubStandardList2(standardReadingId);
			if (CollectionUtil.isListNotEmpty(sublist)) {
				List<Adjunct> list2 = this.getAdjuctH5(sublist);
				for (Adjunct entity : list2) {
					adjunctList.add(entity);
				}
			}
		} else { // 获取单一解读里面的附件

			List<StandardReading> sublist = standardReadDao.getSubStandardList4(standardReadingId);
			if (CollectionUtil.isListNotEmpty(sublist)) {
				List<Adjunct> list3 = this.getAdjuctH5(sublist);
				for (Adjunct entity : list3) {
					adjunctList.add(entity);
				}
			}

		}
		return adjunctList;
	}

	/**
	 * 公共方法，处理附件信息(专供H5用)
	 */
	private List<Adjunct> getAdjuctH5(List<StandardReading> standList) {
		List<Adjunct> resultList = new ArrayList<Adjunct>();
		for (StandardReading subRead : standList) {
			/**
			 * 拿到附件列表
			 */
			Adjunct subVo = new Adjunct();
			subVo.setTitle(subRead.getTitle());
			subVo.setIntro(subRead.getIntro());
			// 解读中可能没有附件信息,所以要判断
			if (subRead.getDocumentId() != null && !"".equals(subRead.getDocumentId())) {
				String documentID = subRead.getDocumentId();
				String voices = subRead.getVoice();
				String[] strList = documentID.split(",");
				StringBuffer file = new StringBuffer();
				StringBuffer document = new StringBuffer();
				StringBuffer format = new StringBuffer();
				StringBuffer pageCount = new StringBuffer();
				StringBuffer vocsbf = new StringBuffer();
				/**
				 * 把多个附件的信息拼接起来
				 */
				for (int i = 0; i <= strList.length - 1; i++) {
					GetDocumentResponse doc = BDocHelper.readDocument(strList[i]);
					file.append(",").append(doc.getTitle());
					document.append(",").append(doc.getDocumentId());
					format.append(",").append(doc.getFormat());
					pageCount.append(",").append(doc.getPublishInfo().getPageCount());
					if (voices != null && !"".equals(voices)) {
						if (voices.contains(",")) {
							String[] vocstr = voices.split(",");
							if (vocstr.length > 0 && vocstr.length >= i + 1) {
								vocsbf.append(",").append(jointUrl + "voice/" + vocstr[i]); // 拼接音频文件
							} else {
								vocsbf.append(","); // 没有音频文件就用空字符串代替
							}

						} else {
							vocsbf.append(jointUrl + "voice/" + subRead.getVoice());
						}
					} else {
						vocsbf.append(",");
					}
				}
				subVo.setFileName(file.toString().replaceFirst(",", ""));
				if (subRead.getMoney().compareTo(new BigDecimal(0)) == 0) {
					subVo.setDocumentID(document.toString().replaceFirst(",", ""));
				} else {
					subVo.setDocumentID("");
				}
				subVo.setFormat(format.toString().replaceFirst(",", ""));
				subVo.setPageCount(pageCount.toString().replaceFirst(",", ""));
				if (voices != null && !"".equals(voices)) {
					subVo.setVoice(vocsbf.toString().replaceFirst(",", ""));
				} else {
					subVo.setVoice("");
				}
				resultList.add(subVo);
			}
		}
		return resultList;
	}

	@Override
	public List<StandReadingVo> getStandardReadListBySearchKeyWord(String searchKeyWord, Long memberId) {
		List<StandReadingVo> standReadingVos = new ArrayList<>();
		List<StandardReading> StandardReadings = standardReadDao.getStandardReadListBySearchKeyWord(searchKeyWord);
		for (StandardReading standardReading : StandardReadings) {
			StandReadingVo standReadingVo = new StandReadingVo();
			standReadingVo.setEditTime(standardReading.getEditTime());
			standReadingVo.setLinePrice(standardReading.getLinePrice());
			standReadingVo.setAuthorName(standardReading.getAuthorName());
			standReadingVo.setCover(
					standardReading.getCover() == null ? defaultPhoto : jointUrl + standardReading.getCover());
			if (standardReading.getIsYearFree().equals("1")) {
				standReadingVo.setIsVIPView("2");
			}
			if (standardReading.getIsMonthFree().equals("1")) {
				standReadingVo.setIsVIPView("1");
			}
			if (standardReading.getIsYearFree().equals("0") && standardReading.getIsMonthFree().equals("0")) {
				standReadingVo.setIsVIPView("0");
			}
			standReadingVo.setIsMonthFree(standardReading.getIsMonthFree());
			standReadingVo.setIsYearFree(standardReading.getIsYearFree());

			if (memberId < 1) {// 游客
				standReadingVo.setBuyStatus("0");
			} else {
				// 判断该用户是否购买了这个这篇文章
				ExpenseTotal buy = standardReadDao.getisBuyStand(memberId, standardReading.getId());

				if (buy != null) {
					standReadingVo.setBuyStatus("1");
				} else {
					standReadingVo.setBuyStatus("0");
				}
			}

			standReadingVo.setMoney(standardReading.getMoney());
			standReadingVo.setStandReadingId(standardReading.getId());
			standReadingVo.setTitle(standardReading.getTitle());
			standReadingVo.setType(standardReading.getType());
			//是否存在音频文件,值为0,则不存在音频文件
			if (standardReading.getVoice() != null && !"".equals(standardReading.getVoice()) && !standardReading.getVoice().equals("0")) {
				standReadingVo.setIsExistVoice("1");
			} else {
				standReadingVo.setIsExistVoice("0");
			}

			standReadingVos.add(standReadingVo);
		}
		return standReadingVos;
	}

	/**
	 * 首页的专栏订阅和查看更多
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Pager getColumnList(int pageNo, int pageSize, String chiefShow, Member member) {
		Pager pager = standardReadDao.getColumnList(pageNo, pageSize, chiefShow);
		List<StandardReadListVo3> resultList = new ArrayList<StandardReadListVo3>();
		List<StandardReading> list = pager.getResultList();
		for (StandardReading entity : list) {
			StandardReadListVo3 vo = new StandardReadListVo3();
			vo.setArticleId(entity.getId());
			vo.setCover(entity.getCover() == null ? defaultPhoto : jointUrl + entity.getCover());
			vo.setColumnIntro(entity.getColumnIntro() == null ? "" : entity.getColumnIntro());
			if (entity.getIsMonthFree().equals("0") && entity.getIsYearFree().equals("0")) {
				vo.setIsVIPView(0);
			} else if (entity.getIsMonthFree().equals("1")) {
				vo.setIsVIPView(1);
			} else if (entity.getIsYearFree().equals("1")) {
				vo.setIsVIPView(2);
			}
			ExpenseTotal buys = standardReadDao.getColumnBuyStatus(member.getId(), entity.getId());
			if (buys != null) {
				vo.setBuyStatus(1);
			}
			vo.setIsStopUpdate(entity.getStopUpdate() == null ? 0 : entity.getStopUpdate());
			vo.setMoney(entity.getMoney());
			vo.setLinePrice(entity.getLinePrice());
			vo.setTime(DateHelper.dataToString(entity.getEditTime(), "yyyy-MM-dd HH:mm:ss"));
			vo.setTitle(entity.getTitle() == null ? "" : entity.getTitle());
			resultList.add(vo);
		}
		return new Pager(pageSize, pageNo, pager.getRowCount(), resultList);
	}

	/**
	 * 首页的精品文章和查看更多
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Pager getExcellentArticleList(int pageNo, int pageSize, String chiefShow, Member member) {
		Pager pager = standardReadDao.getExcellentArticleList(pageNo, pageSize, chiefShow);
		List<StandardReadListVo5> resultList = new ArrayList<StandardReadListVo5>();
		List<StandardReading> list = pager.getResultList();
		for (StandardReading entity : list) {
			StandardReadListVo5 vo = new StandardReadListVo5();
			vo.setArticleId(entity.getId());
			//是否存在音频文件,值为0,则不存在音频文件
			if (entity.getVoice() != null && !"".equals(entity.getVoice()) && !entity.getVoice().equals("0")) {
				vo.setIsExistVoice(1);
				vo.setExpertName(entity.getAuthorName() == null ? "" : entity.getAuthor().getRealname());
			} else {
				vo.setIsExistVoice(0);
				vo.setExpertName(entity.getAuthorName() == null ? "" : entity.getAuthor().getRealname());
			}
			if (entity.getPhoto() != null && !"".equals(entity.getPhoto())) {
				vo.setCover(jointUrl + entity.getPhoto());
			} else {
				vo.setCover(jointUrl + entity.getCover());
			}
			vo.setMoney(entity.getMoney());
			vo.setLinePrice(entity.getLinePrice());
			vo.setTitle(entity.getTitle()==null?"":entity.getTitle());
			if (entity.getIsMonthFree().equals("0") && entity.getIsYearFree().equals("0")) {
				vo.setIsVIPView(0);
			} else if (entity.getIsMonthFree().equals("1")) {
				vo.setIsVIPView(1);
			} else if (entity.getIsYearFree().equals("1")) {
				vo.setIsVIPView(2);
			}
			ExpenseTotal buys=standardReadDao.getisBuyStand(member.getId(), entity.getId());
			if(buys!=null) {
				vo.setBuyStatus(1);
			}
			resultList.add(vo);
		}
		return new Pager(pageSize, pageNo, pager.getRowCount(), resultList);
	}

	@Override
	public List<StandardReadListVo3> getStandardReadSerialise(String searchKeyWord, Long memberId) {
		List<StandardReadListVo3> standReadingVos = new ArrayList<>();
		List<StandardReading> StandardReadings = standardReadDao.getStandardReadSerialise(searchKeyWord);
		for (StandardReading standardReading : StandardReadings) {
			StandardReadListVo3 standReadingVo = new StandardReadListVo3();
			standReadingVo.setArticleId(standardReading.getId());
			standReadingVo.setCover(standardReading.getCover() == null ? defaultPhoto : standardReading.getCover());
			standReadingVo.setCover(jointUrl + standardReading.getCover());
			standReadingVo.setLinePrice(standardReading.getLinePrice());
			standReadingVo
					.setColumnIntro(standardReading.getColumnIntro() == null ? "" : standardReading.getColumnIntro());
			standReadingVo.setIsStopUpdate(standardReading.getStopUpdate());
			standReadingVo.setMoney(standardReading.getMoney());
			standReadingVo.setTime(standardReading.getAddTime().toString());
			standReadingVo.setTitle(standardReading.getTitle() == null ? "" : standardReading.getTitle());

			if (standardReading.getIsYearFree().equals("1")) {
				standReadingVo.setIsVIPView(2);
			}
			if (standardReading.getIsMonthFree().equals("1")) {
				standReadingVo.setIsVIPView(1);
			}
			if (standardReading.getIsYearFree().equals("0") && standardReading.getIsMonthFree().equals("0")) {
				standReadingVo.setIsVIPView(0);
			}

			if (memberId < 1) {
				standReadingVo.setBuyStatus(0);
			} else {
				// 判断该用户是否购买了这个这篇文章
				ExpenseTotal buy = standardReadDao.getisBuyStand(memberId, standardReading.getId());

				if (buy != null) {
					standReadingVo.setBuyStatus(1);
				} else {
					standReadingVo.setBuyStatus(0);
				}

			}
			standReadingVos.add(standReadingVo);
		}
		return standReadingVos;
	}

	@Override
	public Pager getMoreStandardRead(String searchKeyWord, String qualityId, int pageNo, int pageSize, Long memberId) {
		List<StandReadingVo> standReadingVos = new ArrayList<>();
		List<StandardReading> StandardReadings = standardReadDao.getMoreStandardRead(searchKeyWord, qualityId, pageNo,
				pageSize);
		for (StandardReading standardReading : StandardReadings) {
			StandReadingVo standReadingVo = new StandReadingVo();
			standReadingVo.setEditTime(standardReading.getEditTime());
			standReadingVo.setLinePrice(standardReading.getLinePrice());
			standReadingVo.setAuthorName(standardReading.getAuthorName());
			standReadingVo.setCover(
					standardReading.getCover() == null ? defaultPhoto : jointUrl + standardReading.getCover());
			standReadingVo.setCover(jointUrl + standardReading.getCover());
			if (standardReading.getIsYearFree().equals("1")) {
				standReadingVo.setIsVIPView("2");
			}
			if (standardReading.getIsMonthFree().equals("1")) {
				standReadingVo.setIsVIPView("1");
			}
			if (standardReading.getIsYearFree().equals("0") && standardReading.getIsMonthFree().equals("0")) {
				standReadingVo.setIsVIPView("0");
			}

			standReadingVo.setIsMonthFree(standardReading.getIsMonthFree());
			standReadingVo.setIsYearFree(standardReading.getIsYearFree());

			if (memberId < 1) {
				standReadingVo.setBuyStatus("0");
			} else {
				// 判断该用户是否购买了这个这篇文章
				ExpenseTotal buy = standardReadDao.getisBuyStand(memberId, standardReading.getId());

				if (buy != null) {
					standReadingVo.setBuyStatus("1");
				} else {
					standReadingVo.setBuyStatus("0");
				}
			}

			standReadingVo.setMoney(standardReading.getMoney());
			standReadingVo.setStandReadingId(standardReading.getId());
			standReadingVo.setTitle(standardReading.getTitle());
			standReadingVo.setType(standardReading.getType());
			//是否存在音频文件,值为0,则不存在音频文件
			if (standardReading.getVoice() != null && !"".equals(standardReading.getVoice()) && !standardReading.getVoice().equals("0")) {
				standReadingVo.setIsExistVoice("1");
			} else {
				standReadingVo.setIsExistVoice("0");
			}

			standReadingVos.add(standReadingVo);
		}
		return new Pager(pageSize, pageNo, standReadingVos.size(), standReadingVos);
	}

	@Override
	public Pager getMoreStandReadSerialise(String searchKeyWord, int pageNo, int pageSize, Long memberId) {
		List<StandardReadListVo3> standReadingVos = new ArrayList<>();
		List<StandardReading> StandardReadings = standardReadDao.getStandardReadSerialise(searchKeyWord, pageNo,
				pageSize);
		for (StandardReading standardReading : StandardReadings) {
			StandardReadListVo3 standReadingVo = new StandardReadListVo3();
			standReadingVo.setArticleId(standardReading.getId());
			standReadingVo.setCover(
					standardReading.getCover() == null ? defaultPhoto : jointUrl + standardReading.getCover());
			standReadingVo.setCover(jointUrl + standardReading.getCover());
			standReadingVo.setLinePrice(standardReading.getLinePrice());
			standReadingVo
					.setColumnIntro(standardReading.getColumnIntro() == null ? "" : standardReading.getColumnIntro());
			standReadingVo.setIsStopUpdate(standardReading.getStopUpdate());
			standReadingVo.setMoney(standardReading.getMoney());
			standReadingVo.setTime(standardReading.getAddTime().toString());
			standReadingVo.setTitle(standardReading.getTitle());

			if (standardReading.getIsYearFree().equals("1")) {
				standReadingVo.setIsVIPView(2);
			}
			if (standardReading.getIsMonthFree().equals("1")) {
				standReadingVo.setIsVIPView(1);
			}

			if (standardReading.getIsYearFree().equals("0") && standardReading.getIsMonthFree().equals("0")) {
				standReadingVo.setIsVIPView(0);
			}

			if (memberId < 1) {
				standReadingVo.setBuyStatus(0);
			} else {
				// 判断该用户是否购买了这个这篇文章
				ExpenseTotal buy = standardReadDao.getisBuyStand(memberId, standardReading.getId());

				if (buy != null) {
					standReadingVo.setBuyStatus(1);
				} else {
					standReadingVo.setBuyStatus(0);
				}
			}

			standReadingVos.add(standReadingVo);
		}
		return new Pager(pageSize, pageNo, standReadingVos.size(), standReadingVos);
	}

}
