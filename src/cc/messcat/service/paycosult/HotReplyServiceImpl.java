package cc.messcat.service.paycosult;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mipush.MiPushHelper;

import cc.messcat.bases.BaseManagerDaoImpl;
import cc.messcat.entity.ExpenseTotal;
import cc.messcat.entity.ExpertClassify;
import cc.messcat.entity.FeedBack;
import cc.messcat.entity.HotProblem;
import cc.messcat.entity.HotReply;
import cc.messcat.entity.Member;
import cc.messcat.entity.HotReplyFees;
import cc.messcat.entity.HotReplyLiked;
import cc.messcat.entity.Share;
import cc.messcat.vo.HotProblemVo;
import cc.messcat.vo.HotReplyDetailVo;
import cc.messcat.vo.HotReplyPaidVo;
import cc.messcat.vo.HotReplyVo;
import cc.messcat.vo.PayConsultVo;
import cc.messcat.vo.ReplyVo;
import cc.modules.commons.Pager;
import cc.modules.util.DateHelper;
import cc.modules.util.PropertiesFileReader;
import cc.modules.util.SmsUtil;

@SuppressWarnings("serial")
public class HotReplyServiceImpl extends BaseManagerDaoImpl implements HotReplyService {
	private String defaultMemberPhoto = PropertiesFileReader.getByKey("member.photo.url");// 默认会员头像
	private static String jointUrl = PropertiesFileReader.getByKey("static.domain");// 图片拼接

	@SuppressWarnings("unchecked")
	@Override
	public Pager getRecommendList(int pageNo, int pageSize3, Member puMember) {
		List<HotReplyVo> resultList = new ArrayList<HotReplyVo>();
		Pager pager = hotReplyDao.getRecommendList(pageNo, pageSize3);
		List<HotReply> list = pager.getResultList();
		if (!list.isEmpty() && list.size() != 0) {
			resultList = nullConvert(list, puMember);
		}
		return new Pager(pageSize3, pageNo, pager.getRowCount(), resultList);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Pager getReplyList(int pageNo, int pageSize, Long expertId, Member puMember) {
		List<HotReplyVo> resultList = new ArrayList<HotReplyVo>();
		Pager pager = hotReplyDao.getReplyList(pageNo, pageSize, expertId);
		List<HotReply> list = pager.getResultList();
		if (!list.isEmpty() && list.size() != 0) {
			resultList = nullConvert(list, puMember);
		}
		return new Pager(pageSize, pageNo, pager.getRowCount(), resultList);
	}

	@Override
	public void commitProblem(HotProblem entity) {
		hotReplyDao.commitProblem(entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Pager getAllReply(int pageNo, int pageSize, Member puMember) {
		List<HotReplyVo> resultList = new ArrayList<HotReplyVo>();
		Pager pager = hotReplyDao.getAllReply(pageNo, pageSize);
		List<HotReply> list = pager.getResultList();
		if (!list.isEmpty() && list.size() != 0) {
			resultList = nullConvert(list, puMember);
		}
		return new Pager(pageSize, pageNo, pager.getRowCount(), resultList);
	}

	@Override
	public boolean commitFeedBack(FeedBack feedBack) {
		return hotReplyDao.commitFeedBack(feedBack);
	}

	/*
	 * 获取未答问答列表
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Pager getNotReply(int pageNo, int pageSize, Long memberId) {
		List<HotProblemVo> resultList = new ArrayList<HotProblemVo>();
		Pager pager = hotReplyDao.getNotReply(pageNo, pageSize, memberId);
		List<HotProblem> list = pager.getResultList();
		if (list != null && list.size() != 0) {
			for (HotProblem problem : list) {
				HotReply hotReply = hotReplyDao.getReply(problem.getExpertId(), problem.getId());
				if (hotReply == null) {
					HotProblemVo vo = new HotProblemVo();
					vo.setId(problem.getId());
					vo.setProblem(problem.getName());
					String addTime = DateHelper.dataToString(problem.getAddTime(), "yyyy-MM-dd HH:mm:ss");
					vo.setAddTime(addTime);
					Member member = hotReplyDao.getMember(problem.getMemberId());
					vo.setMemberId(member.getId());
					// 若用户没有设名字,则用手机号代替
					if(member.getMobile() == null){
						vo.setMemberName(member.getRealname() == null ? "游客用户"+member.getUuid() : member.getRealname()); // 提问者的名字
					}else {
						vo.setMemberName(member.getRealname() == null ? member.getMobile() : member.getRealname()); // 提问者的名字
					}
					vo.setMemberPhoto(member.getPhoto() == null ? defaultMemberPhoto : jointUrl + member.getPhoto());
					Member expert = hotReplyDao.getMember(problem.getExpertId());
					vo.setPhoto(expert.getPhoto() == null ? defaultMemberPhoto : jointUrl + expert.getPhoto());
					HotReplyFees fees = hotReplyDao.getReplyFees(problem.getExpertId());
					vo.setMoney(String.valueOf(fees.getMoney()));
					resultList.add(vo);
				}
			}
		}
		return new Pager(pageSize, pageNo, resultList.size(), resultList);
	}

	/*
	 * 获取历史问答列表
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Pager getHistoryReply(int pageNo, int pageSize, Member puMember) {
		List<HotReplyVo> resultList = new ArrayList<HotReplyVo>();
		Pager pager = hotReplyDao.getHistoryReply(pageNo, pageSize, puMember.getId());
		List<HotReply> list = pager.getResultList();
		if (!list.isEmpty() && list.size() != 0) {
			resultList = nullConvert(list, puMember);
		}
		return new Pager(pageSize, pageNo, pager.getRowCount(), resultList);
	}

	/**
	 * 公共方法---null值转换
	 * 
	 */
	private List<HotReplyVo> nullConvert(List<HotReply> list, Member puMember) {
		List<HotReplyVo> resultList = new ArrayList<HotReplyVo>();
		for (HotReply reply : list) {
			HotReplyVo entity = new HotReplyVo();
			// 拿到问题
			HotProblem problem = hotReplyDao.getProblem(reply.getProblemId());
			entity.setAddTime(DateHelper.dataToString(reply.getAddTime(), "yyyy-MM-dd HH:mm:ss"));
			entity.setReplyId(reply.getId());
			Member expert = hotReplyDao.getMember(reply.getExpertId()); // 专家
			entity.setExpertId(String.valueOf(expert.getId())); // 专家Id
			entity.setExpert(expert.getRealname()); // 专家名字
			/*
			 * 专家分类列表
			 */
			List<ExpertClassify> classifyList = expert.getClassifys();
			StringBuffer sbf = new StringBuffer();
			if (classifyList != null && classifyList.size() > 0) {
				for (int i = 0; i <= classifyList.size() - 1; i++) {
					sbf.append(",").append(classifyList.get(i).getName());
				}
			}
			entity.setClassify(sbf.toString().length() < 0 ? "" : sbf.toString().replaceFirst(",", "")); // 把专家分类信息拼接
			entity.setPhoto(expert.getPhoto() == null ? defaultMemberPhoto : jointUrl + expert.getPhoto()); // 专家头像
			HotReplyFees fees = hotReplyDao.getReplyFees(reply.getExpertId());
			entity.setMoney(fees.getMoney()); // 只要不是在个人中心,显示的都是专家的公开提问价格收费
			Member member = hotReplyDao.getMember(problem.getMemberId()); // 提问者
			/*
			 * 游客身份访问
			 */
			if (puMember.getId()!=null) {
				/*
				 * 若当前用户不是回答此问题的专家,则查看购买状态(通过专家Id和问题的Id)
				 * 
				 * 若此问题是专家本人回答的,则把支付状态改"已支付", 专家可以直接查看
				 */
				if(!reply.getExpertId().equals(puMember.getId())){   
					ExpenseTotal record = expenseTotalDao.getReplyBuys(puMember.getId(), expert.getId(), reply.getProblemId());
					if (record != null) {
						entity.setPayStatus("1");
					} else {
						entity.setPayStatus("0");
					}
				}else{
					entity.setPayStatus("1");
				}
				// 根据提问者的Id和回复列表的Id去查收藏状态
				String collectStatus = hotReplyDao.getCollectStatus(puMember.getId(), reply.getId());
				entity.setCollectStatus(collectStatus);
				// 获取点赞状态
				HotReplyLiked hotReplyLiked = hotReplyDao.getReplyLiked(reply.getId(), reply.getExpertId(), puMember.getId());
				if (hotReplyLiked != null) {
					entity.setLikeStatus("1");
				} else {
					entity.setLikeStatus("0");
				}
			}else{
				entity.setPayStatus("0");
				entity.setCollectStatus("0");
				entity.setLikeStatus("0");
			}
			// 获取分享URL
			Share share = hotReplyDao.getShare();
			entity.setShareURL(share.getShareURL() + "&replyId=" + reply.getId());
			// 若用户没有设名字,则用手机号代替
			entity.setMemberName(member.getRealname() == null || member.getRealname().isEmpty()? "用户"+member.getId() : member.getRealname()); // 提问者的名字
			entity.setProblemId(problem.getId()); // 问题表Id
			entity.setProblem(problem.getName());
			/*
			 * 内容代号(由三位数字组成,百位上代表音频,十位代表图片,个位代表文字; 0代表没有该类型的内容,1代表有内容)
			 */
			StringBuffer sbf2 = new StringBuffer();
			if (reply.getVoice() != null && !"".equals(reply.getVoice())) {
				sbf2.append("1");
			} else {
				sbf2.append("0");
			}
			if (reply.getPicture() != null && !"".equals(reply.getPicture())) {
				sbf2.append("1");
			} else {
				sbf2.append("0");
			}
			if (reply.getContent() != null && !"".equals(reply.getContent())) {
				sbf2.append("1");
			} else {
				sbf2.append("0");
			}
			entity.setContentCode(sbf2.toString());
			// 根据问题Id和回答问题的专家Id去统计观看量
			int viewSum = 0;
			try {
				viewSum = hotReplyDao.getViewSum(expert.getId(), reply.getProblemId());
			} catch (Exception e) {
				e.printStackTrace();
			}
			entity.setViewSum(viewSum); // 观看量
			int replyLikedSum = hotReplyDao.getReplyLikedSum(expert.getId(), reply.getId());
			entity.setReplyLikedSum(replyLikedSum); // 多少人觉得值
			resultList.add(entity);
		}
		return resultList;
	}

	/**
	 * 公共方法---null值转换
	 * 
	 */
	private List<PayConsultVo> nullConvert2(List<HotReply> list, Member puMember) {
		List<PayConsultVo> resultList = new ArrayList<PayConsultVo>();
		for (HotReply reply : list) {
			PayConsultVo entity = new PayConsultVo();
			// 拿到问题
			HotProblem problem = hotReplyDao.getProblem(reply.getProblemId());
			entity.setAddTime(DateHelper.dataToString(reply.getAddTime(), "yyyy-MM-dd"));
			entity.setReplyId(reply.getId());
			Member expert = hotReplyDao.getMember(reply.getExpertId()); // 专家
			entity.setExpertId(String.valueOf(expert.getId())); // 专家Id
			entity.setExpert(expert.getRealname()); // 专家名字
			/*
			 * 专家分类列表
			 */
			List<ExpertClassify> classifyList = expert.getClassifys();
			StringBuffer sbf = new StringBuffer();
			if (classifyList != null && classifyList.size() > 0) {
				for (int i = 0; i <= classifyList.size() - 1; i++) {
					sbf.append(",").append(classifyList.get(i).getName());
				}
			}
			entity.setPhoto(expert.getPhoto() == null ? defaultMemberPhoto : jointUrl + expert.getPhoto()); // 专家头像
			HotReplyFees fees = hotReplyDao.getReplyFees(reply.getExpertId());
			entity.setMoney(fees.getMoney()); // 只要不是在个人中心,显示的都是专家的公开提问价格收费
			/*
			 * 游客身份访问
			 */
			if (puMember.getId()!=null) {
				// 根据提问者的Id和回复列表的Id去查收藏状态
				String collectStatus = hotReplyDao.getCollectStatus(puMember.getId(), reply.getId());
				entity.setCollectStatus(collectStatus);
				// 获取点赞状态
				HotReplyLiked hotReplyLiked = hotReplyDao.getReplyLiked(reply.getId(), reply.getExpertId(), puMember.getId());
				if (hotReplyLiked != null) {
					entity.setLikeStatus("1");
				} else {
					entity.setLikeStatus("0");
				}
			}else{
				entity.setCollectStatus("0");
				entity.setLikeStatus("0");
			}
			// 若用户没有设名字,则用手机号代替
			entity.setProblem(problem.getName());
			/*
			 * 内容代号(由三位数字组成,百位上代表音频,十位代表图片,个位代表文字; 0代表没有该类型的内容,1代表有内容)
			 */
			StringBuffer sbf2 = new StringBuffer();
			if (reply.getVoice() != null && !"".equals(reply.getVoice())) {
				sbf2.append("1");
			} else {
				sbf2.append("0");
			}
			if (reply.getPicture() != null && !"".equals(reply.getPicture())) {
				sbf2.append("1");
			} else {
				sbf2.append("0");
			}
			if (reply.getContent() != null && !"".equals(reply.getContent())) {
				sbf2.append("1");
			} else {
				sbf2.append("0");
			}
			entity.setContentCode(sbf2.toString());
			// 根据问题Id和回答问题的专家Id去统计观看量
			int viewSum = 0;
			try {
				viewSum = hotReplyDao.getViewSum(expert.getId(), reply.getProblemId());
			} catch (Exception e) {
				e.printStackTrace();
			}
			entity.setViewSum(viewSum); // 观看量
			resultList.add(entity);
		}
		return resultList;
	}
	
	@Override
	public void commitReply(HotReply hotReply, Long expertId) {
		HotProblem problem=hotReplyDao.getProblem(hotReply.getProblemId());
		Member member = hotReplyDao.getMember(problem.getMemberId()); //用户
		Member expert = hotReplyDao.getMember(problem.getExpertId()); //专家
		HotReply entity=hotReplyDao.isResubmit(hotReply.getProblemId(),expertId);
		if(entity==null) {
			hotReplyDao.commitReply(hotReply, expertId,problem);
			HotReply newReply=hotReplyDao.getHotReply(problem.getId(), expertId);
			SmsUtil.sendMessage(member.getMobile(), "尊敬的用户您好，"+expert.getRealname()+"已回答您的问题，请至APP“已购”-“付费咨询”里查看回复信息！");
			String description="尊敬的用户：您好！"+expert.getRealname()+"已回答了您的问题，请至世界认证地图APP“已购”-“付费咨询”中打开查看详细信息！";
			String openFile="weikai://cert-map?target=consult&replyId="+newReply.getId();
			MiPushHelper.sendAndroidUserAccount("您的提问已被解答", description, openFile, member.getMobile());
			MiPushHelper.sendIOSUserAccount(description, openFile, member.getMobile());
		}
	}

	// 根据问题id获取单个问题信息
	@Override
	public HotProblem getHotProblemById(Long id) {
		return hotReplyDao.getProblem(id);
	}

	// 获取最大的问题id
	@Override
	public Long getMaxHotProblemId() {
		return hotReplyDao.getMaxHotProblemId();
	}

	// 根据专家id获取专家咨询收费表
	@Override
	public HotReplyFees getReplyFeesByMemberId(Long memberId) {
		return hotReplyDao.getReplyFees(memberId);
	}

	// 获取历史回答问题详情
	@Override
	public HotReplyDetailVo getReplyDetail(Long replyId) {
		HotReplyDetailVo entity = new HotReplyDetailVo();
		HotReply reply = hotReplyDao.getHotReply(replyId);
		HotProblem problem = hotReplyDao.getProblem(reply.getProblemId());
		List<FeedBack> list = hotReplyDao.getFeedBackList(problem.getMemberId(), reply.getExpertId());
		entity.setAddTime(DateHelper.dataToString(reply.getAddTime(), "yyyy-MM-dd HH:mm:ss"));
		entity.setContent(reply.getContent() == null ? "" : reply.getContent());
		entity.setFeedBackList(list);
		Member member = hotReplyDao.getMember(problem.getMemberId()); // 用户
		// 若用户没有设名字,则 用手机号代替
		entity.setMemberName(member.getRealname() == null ? member.getMobile() : member.getRealname());
		/*
		 * 把数据库中存储形如(201781919436885.jpg,201781919431215.png) 图片数据 解析成一个图片列表
		 */
		List<String> pictures = new ArrayList<String>();
		String str = reply.getPicture();
		if (str == null || "".equals(str)) {
			entity.setPictures(pictures);
		} else if (str.length() == 1) {
			pictures.add(jointUrl + str);
			entity.setPictures(pictures);
		} else {
			String[] subf = str.split(",");
			for (String stf1 : subf) {
				pictures.add(jointUrl + stf1);
			}
			entity.setPictures(pictures);
		}
		Member expert = hotReplyDao.getMember(problem.getExpertId()); // 专家
		entity.setProblem(problem.getName() == null ? "" : problem.getName());
		int viewSum = hotReplyDao.getViewSum(expert.getId(), reply.getProblemId());
		entity.setViewSum(viewSum); // 观看量
		int replyLikedSum = hotReplyDao.getReplyLikedSum(expert.getId(), reply.getId());
		entity.setReplyLikedSum(replyLikedSum); // 多少人觉得值
		// 根据问题列表的Id去查看有多少收藏了该问答
		int collectSum = hotReplyDao.getCollectSum(reply.getId());
		entity.setCollectSum(collectSum);
		entity.setVoice(reply.getVoice() == null ? "" : jointUrl + "voice/" + reply.getVoice());
		entity.setVoiceDuration(reply.getVoiceDuration() == null ? "" : reply.getVoiceDuration());
		return entity;
	}

	/*
	 * 热门问题详情(未支付)
	 */
	@Override
	public HotReplyVo getNotPayDetail(Long replyId, Member puMember) {
		// 为了调取公用方法,减少代码量, 所以才把详情放入List集合里面
		List<HotReply> list = hotReplyDao.getHotReplyList(replyId);
		List<HotReplyVo> resultList = new ArrayList<HotReplyVo>();
		resultList = nullConvert(list, puMember);
		return resultList.get(0);
	}

	/*
	 * 热门问题详情(已支付)
	 */
	@Override
	public HotReplyPaidVo getPaidDetail(Long replyId, Long memberId) {
		HotReplyPaidVo paidVo = new HotReplyPaidVo();
		HotReply reply = hotReplyDao.getHotReply(replyId);
		HotProblem problem = hotReplyDao.getProblem(reply.getProblemId());

		paidVo.setReplyId(reply.getId());
		Member expert = hotReplyDao.getMember(reply.getExpertId()); // 专家
		/*
		 * 专家分类列表
		 */
		List<ExpertClassify> classifyList = expert.getClassifys();
		StringBuffer sbf = new StringBuffer();
		if (classifyList != null && classifyList.size() > 0) {
			for (int i = 0; i <= classifyList.size() - 1; i++) {
				sbf.append(",").append(classifyList.get(i).getName());
			}
		}
		paidVo.setClassify(sbf.toString().length() < 0 ? "" : sbf.toString().replaceFirst(",", ""));
		paidVo.setPhoto(expert.getPhoto() == null ? defaultMemberPhoto : jointUrl + expert.getPhoto());
		paidVo.setExpertId(String.valueOf(expert.getId())); // 专家Id
		paidVo.setExpert(expert.getRealname() == null ? "" : expert.getRealname()); // 专家名字
		Member member = hotReplyDao.getMember(problem.getMemberId()); // 用户
		// 若用户没有设名字,则用手机号代替
		paidVo.setMemberName(member.getRealname() == null ? member.getMobile() : member.getRealname()); // 提问者的名字
		paidVo.setProblem(problem.getName() == null ? "" : problem.getName());
		paidVo.setContent(reply.getContent() == null ? "" : reply.getContent());
		/*
		 * 把数据库中存储形如(201781919436885.jpg,201781919431215.png) 图片数据 解析成一个图片列表
		 */
		List<String> pictures = new ArrayList<String>();
		String str = reply.getPicture();
		if (str == null || "".equals(str)) {
			paidVo.setPictures(pictures);
		} else if (str.length() == 1) {
			pictures.add(jointUrl + str);
			paidVo.setPictures(pictures);
		} else {
			String[] subf = str.split(",");
			for (String stf1 : subf) {
				pictures.add(jointUrl + stf1);
			}
			paidVo.setPictures(pictures);
		}
		// 音频文件的目录在 图片目录的子文件夹 voice里面
		paidVo.setVoice(reply.getVoice() == null ? "" : jointUrl + "voice/" + reply.getVoice());
		paidVo.setVoiceDuration(reply.getVoiceDuration() == null ? "" : reply.getVoiceDuration());
		paidVo.setAddTime(DateHelper.dataToString(reply.getAddTime(), "yyyy-MM-dd HH:mm:ss"));
		int replyLikedSum = hotReplyDao.getReplyLikedSum(expert.getId(), reply.getId());
		paidVo.setReplyLikedSum(replyLikedSum); // 多少人觉得值
		// 根据提问者的Id和回复列表的Id去查收藏状态
		String collectStatus = hotReplyDao.getCollectStatus(memberId, reply.getId());
		paidVo.setCollectStatus(collectStatus);
		// 获取点赞状态
		HotReplyLiked hotReplyLiked = hotReplyDao.getReplyLiked(reply.getId(), reply.getExpertId(), memberId);
		if (hotReplyLiked != null) {
			paidVo.setLikeStatus("1");
		} else {
			paidVo.setLikeStatus("0");
		}
		return paidVo;
	}

	@Override
	public void addReplyLiked(Long replyId, Long expertId, Long memberId, String flag) {
		HotReplyLiked hotReplyLiked = hotReplyDao.getReplyLiked(replyId, expertId, memberId);
		if (hotReplyLiked != null) {
			hotReplyDao.delete(hotReplyLiked);
		}else{
			HotReplyLiked like = new HotReplyLiked();
			like.setAddTime(new Date());
			like.setBeMemberId(expertId);
			like.setEditTime(new Date());
			like.setMemberId(memberId);
			like.setReplyId(replyId);
			like.setStatus("1");
			hotReplyDao.addReplyLiked(like);
		}
	}

	@Override
	public void settingsMoney(Long expertId, BigDecimal money, BigDecimal privateMoney) {
		HotReplyFees hotReplyFees = hotReplyDao.getReplyFees(expertId);
		/*
		 * 当前专家若已经添加了价格,则做更新操作,否则就新增一条价格记录
		 */
		if (hotReplyFees != null) {
			if (money != null) {
				hotReplyFees.setMoney(money);
			} else {
				hotReplyFees.setMoney(hotReplyFees.getMoney());
			}
			if (privateMoney != null) {
				hotReplyFees.setPrivateMoney(privateMoney);
			} else {
				hotReplyFees.setPrivateMoney(hotReplyFees.getPrivateMoney());
			}
			hotReplyDao.updateReplyFees(hotReplyFees);
		} else {
			HotReplyFees entity = new HotReplyFees();
			entity.setMemberId(expertId);
			entity.setMoney(money);
			entity.setPrivateMoney(privateMoney);
			hotReplyDao.saveReplyFees(entity);
		}
	}

	@Override
	public HotReplyFees getReplyFees(Long expertId) {
		return hotReplyDao.getReplyFees(expertId);
	}

	/*
	 * 获取问题列表,用于判断哪些问题已失效
	 */
	@Override
	public List<HotProblem> getProblemList() {
		return hotReplyDao.getProblemList();
	}

	@Override
	public HotReply getHotReply(Long problemId, Long expertId) {
		return hotReplyDao.getHotReply(problemId, expertId);
	}

	@Override
	public void updateProblem(HotProblem problem) {
		hotReplyDao.updateProblem(problem);
	}

	@Override
	public List<ReplyVo> getReplyBySearchKeyWord(String searchKeyWord) {
		List<ReplyVo> replyVos = new ArrayList<>();
		List<HotReply> hotReplys =hotReplyDao.getReplyBySearchKeyWord(searchKeyWord);
		for (HotReply hotReply : hotReplys) {
			ReplyVo replyVo = new ReplyVo();
			replyVo.setReplyId(hotReply.getId());
			replyVo.setExpertName(hotReplyDao.getMember(hotReply.getExpertId()).getRealname());
			//replyVo.setMoney(hotReplyDao.getReplyFees(hotReply.getExpertId()).getMoney());
			//replyVo.setReplyLikedSum(hotReplyDao.getReplyLikedSum(hotReply.getExpertId(), hotReply.getId()));
			//replyVo.setViewSum(hotReplyDao.getViewSum(hotReply.getExpertId(), hotReply.getProblemId()));
			replyVo.setProblem(hotReplyDao.getProblem(hotReply.getProblemId()).getName());
			replyVo.setAnswerTime(hotReply.getAddTime().toString());
			replyVo.setPicture(jointUrl+hotReply.getPicture());
			replyVos.add(replyVo);
		}
		return replyVos;
	}

	@Override
	public void updateHotReply(HotReply hotReply) {
		hotReplyDao.updateHotReply(hotReply);
	}

	@Override
	public Pager getMoreHotReply(String searchKeyWord, int pageNo, int pageSize) {
		List<ReplyVo> replyVos = new ArrayList<>();
		List<HotReply> hotReplys =hotReplyDao.getMoreHotReply(searchKeyWord,pageNo,pageSize);
		for (HotReply hotReply : hotReplys) {
			ReplyVo replyVo = new ReplyVo();
			replyVo.setReplyId(hotReply.getId());
			replyVo.setExpertName(hotReplyDao.getMember(hotReply.getExpertId()).getRealname());
//			replyVo.setMoney(hotReplyDao.getReplyFees(hotReply.getExpertId()).getMoney());
//			replyVo.setReplyLikedSum(hotReplyDao.getReplyLikedSum(hotReply.getExpertId(), hotReply.getId()));
//			replyVo.setViewSum(hotReplyDao.getViewSum(hotReply.getExpertId(), hotReply.getProblemId()));
			replyVo.setProblem(hotReplyDao.getProblem(hotReply.getProblemId()).getName());
			replyVo.setAnswerTime(hotReply.getAddTime().toString());
			replyVo.setPicture(jointUrl+hotReply.getPicture());
			replyVos.add(replyVo);
		}
		return new Pager(pageSize, pageNo, replyVos.size(), replyVos);
	}
	@SuppressWarnings("unchecked")
	@Override
	public Pager getReplyList2(int pageNo, int pageSize, Long expertId, Member puMember) {
		List<PayConsultVo> resultList = new ArrayList<PayConsultVo>();
		Pager pager = hotReplyDao.getReplyList(pageNo, pageSize, expertId);
		List<HotReply> list = pager.getResultList();
		if (!list.isEmpty() && list.size() != 0) {
			resultList = nullConvert2(list, puMember);
		}
		return new Pager(pageSize, pageNo, pager.getRowCount(), resultList);
	}

	@Override
	public HotProblem getHotProblem(Long problemId) {
		return hotReplyDao.getProblem(problemId);
	}
}
