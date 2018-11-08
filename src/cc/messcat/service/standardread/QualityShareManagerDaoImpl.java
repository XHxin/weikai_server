package cc.messcat.service.standardread;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.baidubce.services.doc.model.GetDocumentResponse;

import cc.messcat.bases.BaseManagerDaoImpl;
import cc.messcat.entity.ExpenseTotal;
import cc.messcat.entity.Member;
import cc.messcat.entity.StandardReading;
import cc.messcat.entity.StandardReadingCatalog;
import cc.messcat.vo.Adjunct;
import cc.messcat.vo.CatalogVo1;
import cc.messcat.vo.CatalogVo2;
import cc.messcat.vo.CatalogVo3;
import cc.messcat.vo.QualityTypeListVo;
import cc.messcat.vo.StandardReadListVo;
import cc.messcat.vo.StandardReadListVo4;
import cc.modules.commons.Pager;
import cc.modules.util.BDocHelper;
import cc.modules.util.CollectionUtil;
import cc.modules.util.DateHelper;
import cc.modules.util.PropertiesFileReader;

@SuppressWarnings("serial")
public class QualityShareManagerDaoImpl extends BaseManagerDaoImpl implements QualityShareManagerDao {

	private static String defaultPhoto = PropertiesFileReader.getByKey("default.photo.url");// 默认图片
	private static String jointUrl = PropertiesFileReader.getByKey("static.domain");// 图片拼接

	@Override
	public Pager getQualityTypeList(Integer pageNo, Integer pageSize3) {
		List<QualityTypeListVo> resultList = qualityShareDao.getQualityTypeList(pageNo, pageSize3);
		List<QualityTypeListVo> resultList2 = new ArrayList<QualityTypeListVo>();
		if (!resultList.isEmpty() && resultList.size() > 0) {
			for (QualityTypeListVo vo : resultList) {
				vo.setName(vo.getName() == null ? "" : vo.getName());
				vo.setQualityTypeId(vo.getQualityTypeId() == null ? "" : vo.getQualityTypeId());
				resultList2.add(vo);
			}
		}
		return new Pager(pageSize3, pageNo, resultList.size(), resultList);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Pager getQualityTypeList2(int pageNo, int pageSize, String qualityId, String type, Member puMember) {
		List<StandardReadListVo> resultList = new ArrayList<StandardReadListVo>();
		Pager pager = qualityShareDao.getQualityTypeList2(pageNo, pageSize, qualityId, type);
		List<StandardReading> standardList = pager.getResultList();
		if (CollectionUtil.isListNotEmpty(standardList)) {
			for (StandardReading entity : standardList) {
				StandardReadListVo vo = new StandardReadListVo();
				Member member = new Member();
				boolean collect = false;
				ExpenseTotal buys = null;
				/**
				 * 若专家拿不到,则专家名字 从StandardReading里面拿AuthorName ,专家简介为空
				 */
				if (entity.getAuthor() != null) {
					member = qualityShareDao.getMember(entity.getAuthor().getId());
					vo.setAuthorIntro(member.getIntro() == null ? "" : member.getIntro());
					if (member.getId() != 9375) { // id=9375为运营专家号
						if (member.getRealname() != null && !"".equals(member.getRealname())) {
							vo.setAuthor(member.getRealname());
						} else {
							vo.setAuthor(entity.getAuthorName() == null ? "" : entity.getAuthorName());
						}
					} else {
						vo.setAuthor(entity.getAuthorName() == null ? "" : entity.getAuthorName());
					}
				} else {
					vo.setAuthor(entity.getAuthorName() == null ? "" : entity.getAuthorName());
					vo.setAuthorIntro("");
				}
				if (puMember.getId() != null) {
					collect = qualityShareDao.getCollect(puMember.getId(), entity.getId());
					buys = qualityShareDao.getBuys(puMember.getId(), entity.getId());
				}
				/**
				 * NUll值转换
				 */
				vo.setStandardReadingId(String.valueOf(entity.getId()) == null ? "" : String.valueOf(entity.getId()));
				vo.setTitle(entity.getTitle() == null ? "" : entity.getTitle());
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
				vo.setClassify(Integer.valueOf(entity.getClassify()));
				if (entity.getQualityId() != null) {
					vo.setQualityId(Integer.valueOf(String.valueOf(entity.getQualityId())));
				}
				String shareURL = qualityShareDao.getShareURL();
				vo.setShareURL(shareURL + "&standardReadingId=" + entity.getId());
				if (collect == true) {
					vo.setCollectStatus(1);
				}
				if (buys != null) {
					if (buys.getPayStatus().equals("1")) {
						vo.setBuyStatus(1);
						vo.setViewStatus(1);
					}
				} else {
					vo.setBuyStatus(0);
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
				if (puMember.getId() != null) {
					// 如果解读为未购买，则判断当前会员是否是vip会员，如果是vip会员，再判断解读是否对相应类型的vip会员免费
					if (vo.getBuyStatus()==0) {
						vo.setViewStatus(0);
						Member currentMember = memberDao.get(puMember.getId());
						// 判断当前会员是否是vip会员
						if (currentMember.getGrade() != null && currentMember.getGrade().equals("1")) {
							// 如果是则判断目前的会员到期时间是否已到期
							Date now = new Date();
							if (now.before(currentMember.getEndTime())) {// vip未过期
								switch (vo.getMemberType()) {
								case 1:
									vo.setViewStatus(1);
									break;
								case 2:
									if (currentMember.getType().equals("0"))
										vo.setViewStatus(1);
									break;
								}
							}
						}
					}
				} else {
					vo.setViewStatus(0); // 不可查看状态
				}
				/**
				 * 获取解读列表
				 */
				List<Adjunct> adjunctList = new ArrayList<Adjunct>();
				List<StandardReading> list = new ArrayList<StandardReading>();
				list.add(entity); // 把单个解读放入List里面,调取获取附件信息的公用方法
				if (entity.getType().equals("0")) {
					/**
					 * 拿到附件列表
					 */
					adjunctList = getAdjuncts(list);
				}
				vo.setSubStandardReadingList(adjunctList);
				;
				resultList.add(vo);
			}
		}
		return new Pager(pageSize, pageNo, pager.getRowCount(), resultList);
	}

	@Override
	public List<Adjunct> getQualityShare(String qualityId, Long standardReadingId, String type) {
		List<Adjunct> adjunctList = new ArrayList<Adjunct>();

		if (type.equals("1")) {
			/**
			 * 拿到质量分享连载中的附件信息,并拼到解读的附件列表中
			 */
			List<StandardReading> standardList = qualityShareDao.getQualityShare2(standardReadingId, qualityId);
			if (CollectionUtil.isListNotEmpty(standardList)) {
				List<Adjunct> list = getAdjuncts(standardList);
				for (Adjunct entity : list) {
					adjunctList.add(entity);
				}
			}
			/**
			 * 获取连载里面的单一解读
			 */
			List<StandardReading> sublist = qualityShareDao.getQualityShare(standardReadingId, qualityId);
			if (CollectionUtil.isListNotEmpty(sublist)) {
				/**
				 * 拿到附件列表
				 */
				List<Adjunct> list = getAdjuncts(sublist);
				for (Adjunct entity : list) {
					adjunctList.add(entity);
				}
			}
		} else {
			List<StandardReading> sublist = qualityShareDao.getQualityShare3(standardReadingId, qualityId);
			if (CollectionUtil.isListNotEmpty(sublist)) {
				List<Adjunct> list = getAdjuncts(sublist);
				for (Adjunct entity : list) {
					adjunctList.add(entity);
				}
			}
		}
		return adjunctList;
	}

	/*
	 * 详情接口(标准解读,质量分享共享)
	 */
	@Override
	public StandardReadListVo searchDetail(Long standardReadingId, Member puMember) {
		StandardReadListVo vo = new StandardReadListVo();
		StandardReading entity = qualityShareDao.searchDetail(standardReadingId);
		Member member = new Member();
		boolean collect = false;
		ExpenseTotal buys = null;
		/**
		 * 若专家拿不到,则专家名字 从StandardReading里面拿AuthorName ,专家简介为空
		 */
		if (entity.getAuthor() != null) {
			member = qualityShareDao.getMember(entity.getAuthor().getId());
			vo.setAuthorIntro(member.getIntro() == null ? "" : member.getIntro());
			vo.setExpertHeadImg(entity.getAuthor().getPhoto()==null?"":jointUrl+entity.getAuthor().getPhoto());
			vo.setExpertField(entity.getAuthor().getField()==null?"":entity.getAuthor().getField());
			if (member.getId() != 9375) { // id=9375为运营专家号
				if (member.getRealname() != null && !"".equals(member.getRealname())) {
					vo.setAuthor(member.getRealname());
				} else {
					vo.setAuthor(entity.getAuthorName() == null ? "" : entity.getAuthorName());
				}
			} else {
				vo.setAuthor(entity.getAuthorName() == null ? "" : entity.getAuthorName());
			}
		} else {
			vo.setAuthor(entity.getAuthorName() == null ? "" : entity.getAuthorName());
			vo.setAuthorIntro("");
			vo.setExpertHeadImg("");
			vo.setExpertField("");
		}
		vo.setClassify(Integer.valueOf(entity.getClassify()));
		/*
		 * 游客身份访问
		 */
		if (puMember!=null) {
			collect = qualityShareDao.getCollect(puMember.getId(), entity.getId());
			buys = qualityShareDao.getBuys(puMember.getId(), entity.getId());
		}
		/**
		 * NUll值转换
		 */
		int isAttention=qualityShareDao.getIsAttention(puMember,member);
		vo.setIsAttention(isAttention);
		vo.setAuthorId(entity.getAuthor().getId().toString());
		vo.setStandardReadingId(String.valueOf(entity.getId()) == null ? "" : String.valueOf(entity.getId()));
		vo.setTitle(entity.getTitle() == null ? "" : entity.getTitle());
		String time = DateHelper.dataToString(entity.getAddTime(), "yyyy-MM-dd HH:mm:ss");
		vo.setTime(time);
		vo.setPhoto(entity.getPhoto() == null ? "" : jointUrl + entity.getPhoto());
		vo.setPhoto2(entity.getPhoto2() == null ? "" : jointUrl + entity.getPhoto2());
		vo.setPhoto3(entity.getPhoto3() == null ? "" : jointUrl + entity.getPhoto3());
		vo.setPhoto4(entity.getPhoto4() == null ? "" : jointUrl + entity.getPhoto4());
		vo.setCover(entity.getPhoto() == null || entity.getPhoto().isEmpty()? jointUrl + entity.getCover():jointUrl+ entity.getPhoto());
		vo.setMoney(entity.getMoney()==null? new BigDecimal(0):entity.getMoney());
		vo.setLinePrice(entity.getLinePrice()==null? new BigDecimal(0):entity.getLinePrice());
		vo.setType(entity.getType() == null ? "" : entity.getType());
		vo.setIntro(entity.getIntro() == null ? "" : entity.getIntro());
		vo.setContentType(entity.getContentType() == null ? "" : entity.getContentType());
		vo.setContent(entity.getContent() == null ? "" : entity.getContent());
		// 三期要做音频播放器,就把音频文件放入解读附件里面去了
		if(entity.getVoice()!=null && !"".equals(entity.getVoice()) && !entity.getVoice().equals("0")) {
			vo.setVoice(entity.getVoice()); 
			vo.setVoiceDuration(entity.getVoiceDuration());
		}else {
			vo.setVoice("");
			vo.setVoiceDuration("");
		}
		String shareURL = qualityShareDao.getShareURL();
		if (shareURL != null) {
			vo.setShareURL(shareURL + "&standardReadingId=" + entity.getId());
		} else {
			vo.setShareURL("无此分享URL");
		}
		if (collect == true) {
			vo.setCollectStatus(1);
		} 
		if (buys != null) {
			if (buys.getPayStatus().equals("1")) {
				vo.setBuyStatus(1);
				vo.setViewStatus(1);
			}
		} else {
			vo.setBuyStatus(0);
		}
		// 获取当前解读免费类型
		if (entity.getIsMonthFree().equals("1")) {// 年月共同免费
			vo.setMemberType(1);
		} else if (!entity.getIsMonthFree().equals("1") && entity.getIsYearFree().equals("1")) {
			vo.setMemberType(2);
		} else {
			vo.setMemberType(0);
		}
		if (puMember.getId() != null) {
			// 如果解读为未购买，则判断当前会员是否是vip会员，如果是vip会员，在判断解读是否对相应类型的vip会员免费
			if (vo.getBuyStatus()==0) {
				// vo.setViewStatus(0);
				Member currentMember = memberDao.get(puMember.getId());
				// 判断当前会员是否是vip会员
				if (currentMember.getGrade() != null && currentMember.getGrade().equals("1")) {
					// 如果是则判断目前的会员到期时间是否已到期
					Date now = new Date();
					if (now.before(currentMember.getEndTime())) {// vip未过期
						switch (vo.getMemberType()) {
						case 1:// 年和月都免费
							vo.setViewStatus(1);
							break;
						case 2:// 年免费
							if (currentMember.getType().equals("0")) {
								vo.setViewStatus(1);
							}
							vo.setViewStatus(0);
							break;
						}
					}
				} else {
					vo.setViewStatus(0);
				}
			} else {
				vo.setViewStatus(1);
			}
		} else {
			vo.setViewStatus(0); // 不可查看状态
		}
		List<StandardReading> readList = new ArrayList<StandardReading>();
		readList.add(entity);
		List<Adjunct> adjunctList = getAdjuncts(readList);
		vo.setSubStandardReadingList(adjunctList); // 把附件列表封装到解读对象中去
		return vo;
	}

	/**
	 * 公共方法，处理附件信息
	 */
	private List<Adjunct> getAdjuncts(List<StandardReading> standList) {
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
				String[] vocstr = voices.split(",");
//				String[] titstr = titiles.split(",");
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
					if(vocstr[i]!=null && !"".equals(vocstr[i] ) && !vocstr[i] .equals("0")) {
						subVo.setVoice(jointUrl + "voice/"+vocstr[i]);
					}else {
						subVo.setVoice("");
					}
					/*if(!"0".equals(titstr[i])) {
						subVo.setTitle(titstr[i]);
					}else {
						subVo.setTitle("");
					}*/
					if(!"0".equals(titiles)) {
						subVo.setTitle(titiles);
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
	 * 以下三个方法为H5页面访问的专用方法(因为考虑到为些付费的文档,不能让别人通过网页直接就可以拿到documentId,
	 * 所以就单独处理一下网页端的请求数据)
	 */
	@Override
	public List<Adjunct> getQualityShareH5(String qualityId, Long standardReadingId, String type) {
		List<Adjunct> adjunctList = new ArrayList<Adjunct>();
		if (type.equals("1")) {
			/**
			 * 拿到质量分享连载中的附件信息,并拼到解读的附件列表中
			 */
			List<StandardReading> standardList = qualityShareDao.getQualityShare2(standardReadingId, qualityId);
			if (CollectionUtil.isListNotEmpty(standardList)) {
				List<Adjunct> list = getAdjunctsH5(standardList);
				for (Adjunct entity : list) {
					adjunctList.add(entity);
				}
			}
			/**
			 * 获取连载里面的单一解读
			 */
			List<StandardReading> sublist = qualityShareDao.getQualityShare(standardReadingId, qualityId);
			if (CollectionUtil.isListNotEmpty(sublist)) {
				/**
				 * 拿到附件列表
				 */
				List<Adjunct> list = getAdjunctsH5(sublist);
				for (Adjunct entity : list) {
					adjunctList.add(entity);
				}
			}
		} else {
			List<StandardReading> sublist = qualityShareDao.getQualityShare3(standardReadingId, qualityId);
			if (CollectionUtil.isListNotEmpty(sublist)) {
				List<Adjunct> list = getAdjunctsH5(sublist);
				for (Adjunct entity : list) {
					adjunctList.add(entity);
				}
			}
		}
		return adjunctList;
	}

	@Override
	public StandardReadListVo searchDetailH5(Long standardReadingId, Member puMember) {
		StandardReadListVo vo = new StandardReadListVo();
		StandardReading entity = qualityShareDao.searchDetail(standardReadingId);
		Member member = new Member();
		boolean collect = false;
		ExpenseTotal buys = null;
		/**
		 * 若专家拿不到,则专家名字 从StandardReading里面拿AuthorName ,专家简介为空
		 */
		if (entity.getAuthor() != null) {
			member = qualityShareDao.getMember(entity.getAuthor().getId());
			vo.setAuthorIntro(member.getIntro() == null ? "" : member.getIntro());
			if (member.getId() != 9375) { // id=9375为运营专家号
				if (member.getRealname() != null && !"".equals(member.getRealname())) {
					vo.setAuthor(member.getRealname());
				} else {
					vo.setAuthor(entity.getAuthorName() == null ? "" : entity.getAuthorName());
				}
			} else {
				vo.setAuthor(entity.getAuthorName() == null ? "" : entity.getAuthorName());
			}
		} else {
			vo.setAuthor(entity.getAuthorName() == null ? "" : entity.getAuthorName());
			vo.setAuthorIntro("");
		}
		vo.setClassify(Integer.valueOf(entity.getClassify()));
		/*
		 * 游客身份访问
		 */
		if (puMember.getId() != null) {
			collect = qualityShareDao.getCollect(puMember.getId(), entity.getId());
			buys = qualityShareDao.getBuys(puMember.getId(), entity.getId());
		}
		/**
		 * NUll值转换
		 */
		vo.setStandardReadingId(String.valueOf(entity.getId()) == null ? "" : String.valueOf(entity.getId()));
		vo.setTitle(entity.getTitle() == null ? "" : entity.getTitle());
		String time = DateHelper.dataToString(entity.getAddTime(), "yyyy-MM-dd HH:mm:ss");
		vo.setTime(time);
		vo.setPhoto(entity.getPhoto() == null ? "" : jointUrl + entity.getPhoto());
		vo.setPhoto2(entity.getPhoto2() == null ? "" : jointUrl + entity.getPhoto2());
		vo.setPhoto3(entity.getPhoto3() == null ? "" : jointUrl + entity.getPhoto3());
		vo.setPhoto4(entity.getPhoto4() == null ? "" : jointUrl + entity.getPhoto4());
		vo.setCover(entity.getCover() == null ? defaultPhoto : jointUrl + entity.getCover());
		vo.setMoney(entity.getMoney());
		vo.setAuthorId(entity.getAuthor().getId().toString());
		vo.setQualityId(entity.getQualityId()==null? 0:entity.getQualityId().intValue());
		vo.setType(entity.getType() == null ? "" : entity.getType());
		vo.setIntro(entity.getIntro() == null ? "" : entity.getIntro());
		vo.setContentType(entity.getContentType() == null ? "" : entity.getContentType());
		vo.setContent(entity.getContent() == null ? "" : entity.getContent());
		vo.setVoice(entity.getVoice() == null ? "" : entity.getVoice()); // 三期要做音频播放器,就把音频文件放入解读附件里面去了
		vo.setVoiceDuration(entity.getVoiceDuration() == null ? "" : entity.getVoiceDuration());
		String shareURL = qualityShareDao.getShareURL();
		if (shareURL != null) {
			vo.setShareURL(shareURL + "&standardReadingId=" + entity.getId());
		} else {
			vo.setShareURL("无此分享URL");
		}
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
		if (puMember.getId() != null) {
			// 如果解读为未购买，则判断当前会员是否是vip会员，如果是vip会员，在判断解读是否对相应类型的vip会员免费
			if (vo.getBuyStatus()==0) {
				// vo.setViewStatus(0);
				Member currentMember = memberDao.get(puMember.getId());
				// 判断当前会员是否是vip会员
				if (currentMember.getGrade() != null && currentMember.getGrade().equals("1")) {
					// 如果是则判断目前的会员到期时间是否已到期
					Date now = new Date();
					if (now.before(currentMember.getEndTime())) {// vip未过期
						switch (vo.getMemberType()) {
						case 1:// 年和月都免费
							vo.setViewStatus(1);
							break;
						case 2:// 年免费
							if (currentMember.getType().equals("0")) {
								vo.setViewStatus(1);
							}
							vo.setViewStatus(0);
							break;
						}
					}
				} else {
					vo.setViewStatus(0);
				}
			} else {
				vo.setViewStatus(1);
			}
		} else {
			vo.setViewStatus(0); // 不可查看状态
		}
		List<StandardReading> readList = new ArrayList<StandardReading>();
		readList.add(entity);
		List<Adjunct> adjunctList = getAdjunctsH5(readList);
		vo.setSubStandardReadingList(adjunctList); // 把附件列表封装到解读对象中去
		return vo;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Pager getQualityTypeList2H5(int pageNo, int pageSize, String qualityId, String type, Member puMember) {
		List<StandardReadListVo> resultList = new ArrayList<StandardReadListVo>();
		Pager pager = qualityShareDao.getQualityTypeList2(pageNo, pageSize, qualityId, type);
		List<StandardReading> standardList = pager.getResultList();
		if (CollectionUtil.isListNotEmpty(standardList)) {
			for (StandardReading entity : standardList) {
				StandardReadListVo vo = new StandardReadListVo();
				Member member = new Member();
				boolean collect = false;
				ExpenseTotal buys = null;
				/**
				 * 若专家拿不到,则专家名字 从StandardReading里面拿AuthorName ,专家简介为空
				 */
				if (entity.getAuthor() != null) {
					member = qualityShareDao.getMember(entity.getAuthor().getId());
					vo.setAuthorIntro(member.getIntro() == null ? "" : member.getIntro());
					if (member.getId() != 9375) { // id=9375为运营专家号
						if (member.getRealname() != null && !"".equals(member.getRealname())) {
							vo.setAuthor(member.getRealname());
						} else {
							vo.setAuthor(entity.getAuthorName() == null ? "" : entity.getAuthorName());
						}
					} else {
						vo.setAuthor(entity.getAuthorName() == null ? "" : entity.getAuthorName());
					}
				} else {
					vo.setAuthor(entity.getAuthorName() == null ? "" : entity.getAuthorName());
					vo.setAuthorIntro("");
				}
				if (puMember.getId() != null) {
					collect = qualityShareDao.getCollect(puMember.getId(), entity.getId());
					buys = qualityShareDao.getBuys(puMember.getId(), entity.getId());
				}
				/**
				 * NUll值转换
				 */
				vo.setStandardReadingId(String.valueOf(entity.getId()) == null ? "" : String.valueOf(entity.getId()));
				vo.setTitle(entity.getTitle() == null ? "" : entity.getTitle());
				String time = DateHelper.dataToString(entity.getAddTime(), "yyyy-MM-dd HH:mm:ss");
				vo.setTime(time);
				vo.setPhoto(entity.getPhoto() == null ? "" : jointUrl + entity.getPhoto());
				vo.setPhoto2(entity.getPhoto2() == null ? "" : jointUrl + entity.getPhoto2());
				vo.setPhoto3(entity.getPhoto3() == null ? "" : jointUrl + entity.getPhoto3());
				vo.setPhoto4(entity.getPhoto4() == null ? "" : jointUrl + entity.getPhoto4());
				vo.setCover(entity.getCover() == null ? defaultPhoto : jointUrl + entity.getCover());
				vo.setMoney(entity.getMoney());
				vo.setAuthorId(entity.getAuthor().getId().toString());
				vo.setType(entity.getType() == null ? "" : entity.getType());
				vo.setIntro(entity.getIntro() == null ? "" : entity.getIntro());
				vo.setContentType(entity.getContentType() == null ? "" : entity.getContentType());
				vo.setContent(entity.getContent() == null ? "" : entity.getContent());
				vo.setVoice(entity.getVoice() == null ? "" : entity.getVoice());
				vo.setVoiceDuration(entity.getVoiceDuration() == null ? "" : entity.getVoiceDuration());
				vo.setClassify(Integer.valueOf(entity.getClassify()));
				if (entity.getQualityId() != null) {
					vo.setQualityId(Integer.valueOf(String.valueOf(entity.getQualityId())));
				}
				String shareURL = qualityShareDao.getShareURL();
				vo.setShareURL(shareURL + "&standardReadingId=" + entity.getId());
				if (collect == true) {
					vo.setCollectStatus(1);
				} else {
					vo.setCollectStatus(0);
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
				if (puMember.getId() != null) {
					// 如果解读为未购买，则判断当前会员是否是vip会员，如果是vip会员，再判断解读是否对相应类型的vip会员免费
					if (vo.getBuyStatus()==0) {
						vo.setViewStatus(0);
						Member currentMember = memberDao.get(puMember.getId());
						// 判断当前会员是否是vip会员
						if (currentMember.getGrade() != null && currentMember.getGrade().equals("1")) {
							// 如果是则判断目前的会员到期时间是否已到期
							Date now = new Date();
							if (now.before(currentMember.getEndTime())) {// vip未过期
								switch (vo.getMemberType()) {
								case 1:
									vo.setViewStatus(1);
									break;
								case 2:
									if (currentMember.getType().equals("0"))
										vo.setViewStatus(1);
									break;
								}
							}
						}
					}
				} else {
					vo.setViewStatus(0); // 不可查看状态
				}
				/**
				 * 获取解读列表
				 */
				List<Adjunct> adjunctList = new ArrayList<Adjunct>();
				List<StandardReading> list = new ArrayList<StandardReading>();
				list.add(entity); // 把单个解读放入List里面,调取获取附件信息的公用方法
				if (entity.getType().equals("0")) {
					/**
					 * 拿到附件列表
					 */
					adjunctList = getAdjunctsH5(list);
				}
				vo.setSubStandardReadingList(adjunctList);
				;
				resultList.add(vo);
			}
		}
		return new Pager(pageSize, pageNo, pager.getRowCount(), resultList);
	}

	/**
	 * 公共方法，处理附件信息(专为H5页面而设)
	 */
	private List<Adjunct> getAdjunctsH5(List<StandardReading> standList) {
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

	/**
	 * 查找标准解读或质量分享的目录
	 */
	@Override
	public List<CatalogVo1> getExpertH5(Long standardReadingId) {
		List<CatalogVo1> resultList = new ArrayList<CatalogVo1>();
		List<StandardReadingCatalog> list1 = qualityShareDao.getFirstLevel(standardReadingId);// 1、查找该文章所有的一级目录
		/*
		 * 1、先把文章的所有一级目录查找出来 2、通过一级目录的Id去找该一级目录下有木有二级目录 3、通过二级目录的Id去找该二级目录下有木有三级目录
		 */
		for (StandardReadingCatalog entity : list1) {
			CatalogVo1 vo1 = new CatalogVo1();
			vo1.setCatalog(entity.getCatalog());
			vo1.setCatalogLevel("1");// 一级目录
			List<StandardReadingCatalog> list2 = qualityShareDao.getSecondLevel(standardReadingId, entity.getId()); // 2、查找该文章该一级目录的所有二级目录
			List<CatalogVo2> voList2 = new ArrayList<CatalogVo2>();
			if (list2 != null && list2.size() > 0) {
				for (StandardReadingCatalog entity2 : list2) {
					CatalogVo2 vo2 = new CatalogVo2();
					vo2.setCatalog(entity2.getCatalog());
					vo2.setCatalogLevel("2"); // 二级目录
					List<StandardReadingCatalog> list3 = qualityShareDao.getThreeLevel(standardReadingId,
							entity2.getId());// 3、查找该文章该二级目录 的所有三级目录
					List<CatalogVo3> voList3 = new ArrayList<CatalogVo3>();
					if (list3 != null && list3.size() > 0) {
						for (StandardReadingCatalog entity3 : list3) {
							CatalogVo3 vo3 = new CatalogVo3();
							vo3.setCatalog(entity3.getCatalog());
							vo3.setCatalogLevel("3");
							voList3.add(vo3);
						}
						vo2.setCatalogVoList2(voList3);// 把三级目录装入二级目录实体内
					} else {
						vo2.setCatalogVoList2(voList3);//若没有三级目录,则返回一个空集合
					}
					voList2.add(vo2);
				}
				vo1.setCatalogVoList1(voList2);// 把二级目录装入一级目录实体内
			} else {
				vo1.setCatalogVoList1(voList2);//若没有二级目录,则返回一个空集合
			}
			resultList.add(vo1); // 封装过二级和三级目录之后的 一级目录列表
		}
		return resultList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Pager getArticelList(int pageNo, int pageSize, String qualityId,Long memberId) {
		Pager pager=qualityShareDao.getArticleList(pageNo,pageSize,qualityId);
		List<StandardReading> list=pager.getResultList();
		List<StandardReadListVo4> resultList=new ArrayList<StandardReadListVo4>();
 		if(list!=null && list.size()>0) {
 			for(StandardReading entity:list) {
 				StandardReadListVo4 vo=new StandardReadListVo4();
 				//是否存在音频文件,值为0,则不存在音频文件
 				if(entity.getVoice() != null && !"".equals(entity.getVoice()) && !entity.getVoice().equals("0")) {
 					vo.setIsExistVoice(1);
 				}else {
 					vo.setIsExistVoice(0);
 				}
 				if(entity.getIsMonthFree().equals("0") && entity.getIsYearFree().equals("0")) {
 					vo.setIsVIPView(0); 
 				}else if(entity.getIsMonthFree().equals("1")) {
 					vo.setIsVIPView(1);
 				}else if(entity.getIsYearFree().equals("1")) {
 					vo.setIsVIPView(2);
 				}
 				//查询是否已购买
 				ExpenseTotal expenseTotal = expenseTotalDao.checkAriticleBuyStatus(memberId, entity.getId());
 				if(expenseTotal != null) {
 					vo.setBuyStatus(1);
 				}
 				vo.setLinePrice(entity.getLinePrice()==null? new BigDecimal(0):entity.getLinePrice());
 				vo.setTime(DateHelper.dataToString(entity.getAddTime(), "yyyy-MM-dd HH:mm:ss"));
 				vo.setIsStopUpdate(entity.getStopUpdate());
 				vo.setArticleId(entity.getId());
 				vo.setIntro(entity.getIntro()==null?"":entity.getIntro());
 				if(entity.getPhoto()!=null && !"".equals(entity.getPhoto())) {
 					vo.setCover(jointUrl+entity.getPhoto());
 				}else {
 					vo.setCover(jointUrl+entity.getCover());
 				}
 				vo.setExpertName(entity.getAuthorName()==null?entity.getAuthor().getRealname():entity.getAuthorName());
 				vo.setMoney(entity.getMoney()==null? new BigDecimal(0):entity.getMoney());
 				vo.setTitle(entity.getTitle()==null?"":entity.getTitle());
 				resultList.add(vo);
 			}
 		}
		return new Pager(pageSize,pageNo,pager.getRowCount(),resultList);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Pager getHisArticleList(int pageNo, int pageSize, Member member, Long expertId) {
		Pager pager=qualityShareDao.getHisArticleList(pageNo,pageSize,expertId);
		List<StandardReading> list=pager.getResultList();
		List<StandardReadListVo4> resultList=new ArrayList<StandardReadListVo4>();
		if(list!=null && list.size()>0) {
			for(StandardReading entity:list) {
 				StandardReadListVo4 vo=new StandardReadListVo4();
 				//是否存在音频文件,值为0,则不存在音频文件
 				if(entity.getVoice() != null && !"".equals(entity.getVoice()) && !entity.getVoice().equals("0")) {
 					vo.setIsExistVoice(1);
 				}else {
 					vo.setIsExistVoice(0);
 				}
 				if(entity.getIsMonthFree().equals("0") && entity.getIsYearFree().equals("0")) {
 					vo.setIsVIPView(0); 
 				}else if(entity.getIsMonthFree().equals("1")) {
 					vo.setIsVIPView(1);
 				}else if(entity.getIsYearFree().equals("1")) {
 					vo.setIsVIPView(2);
 				}
 				//查询是否已购买
 				ExpenseTotal expenseTotal = expenseTotalDao.checkAriticleBuyStatus(member.getId(), entity.getId());
 				if(expenseTotal != null) {
 					vo.setBuyStatus(1);
 				}
 				vo.setLinePrice(entity.getLinePrice());
 				vo.setTime(DateHelper.dataToString(entity.getAddTime(), "yyyy-MM-dd HH:mm:ss"));
 				vo.setIsStopUpdate(entity.getStopUpdate());
 				vo.setIntro(entity.getIntro()==null?"":entity.getIntro());
 				vo.setArticleId(entity.getId());
 				if(entity.getPhoto()!=null && !"".equals(entity.getPhoto())) {
 					vo.setCover(jointUrl+entity.getPhoto());
 				}else {
 					vo.setCover(jointUrl+entity.getCover());
 				}
 				vo.setExpertName(entity.getAuthorName()==null?entity.getAuthor().getRealname():entity.getAuthorName());
 				vo.setMoney(entity.getMoney()==null? new BigDecimal(0):entity.getMoney());
 				vo.setLinePrice(entity.getLinePrice()==null? new BigDecimal(0):entity.getLinePrice());
 				vo.setTitle(entity.getTitle()==null?"":entity.getTitle());
 				resultList.add(vo);
 			}
 		}
		return new Pager(pageSize,pageNo,pager.getRowCount(),resultList);
	}

}
