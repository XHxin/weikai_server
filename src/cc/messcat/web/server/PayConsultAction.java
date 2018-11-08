package cc.messcat.web.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import cc.messcat.entity.*;
import cc.messcat.vo.ExpertClassifyResult;
import cc.messcat.vo.ExpertClassifyVo;
import cc.messcat.vo.HotExpertListVo;
import cc.messcat.vo.HotProblemResult;
import cc.messcat.vo.HotReplyDetailVo;
import cc.messcat.vo.HotReplyPaidVo;
import cc.messcat.vo.HotReplyVo;
import cc.messcat.vo.HotReplyVoResult;
import cc.messcat.vo.MoneyAndFieldResult;
import cc.messcat.vo.PayConsultExpertResult;
import cc.messcat.vo.PayConsultExpertVo;
import cc.messcat.vo.PayConsultListVo;
import cc.messcat.vo.PayConsultResult;
import cc.messcat.vo.PayExpertDetailResult;
import cc.messcat.vo.ResponseBean;
import cc.modules.commons.PageAction;
import cc.modules.commons.Pager;
import cc.modules.constants.Constants;
import cc.modules.util.AliyunOOSUtil;
import cc.modules.util.DateHelper;
import cc.modules.util.MD5;
import cc.modules.util.ObjValid;

@SuppressWarnings("serial")
public class PayConsultAction extends PageAction {

	private Object object;
	private String accessToken;
	private Long memberId;
	private ExpertClassify expertClassify; // 专家分类实体
	private int pageNo2;
	private int pageSize3 = 20; // 热门问题推荐专栏(首页的热门问题在二期部署的时候说不用分页,所以就设了一个20,避免修改代码)
	private Long expertId; // 专家Id
	private HotProblem problem;
	private FeedBack feedBack;
	private HotReply hotReply;
	private HotReplyLiked hotReplyLiked;
	private String flag;
	private HotReplyFees hotReplyFees; // 专家收费
	private String[] classifyIds;
	private Long replyId;      //访问问答详情的Id

	/*
	 * 音频文件上传
	 */
	private File voice; // 音频文件
	private String voiceFileName; // 音频文件名
	private File saveVoice; // 保存文档
	/*
	 * 图片上传
	 */
	private File[] pic;
	private String[] picFileName;

	/**
	 * 2.18 付费咨询 模块首页
	 */
	@Override
	public String execute() {
		try {
			Member member=new Member();
			if(memberId != null && memberId>0){   //memberId为-1,则为游客身份登录, 直接判断,不用再去数据库查询是否有此会员
				member = memberManagerDao.retrieveMember(memberId);
				if (ObjValid.isValid(member)) {
					if (!accessToken.equals(member.getAccessToken())) {
						object = new ResponseBean(Constants.EXCEPTION_CODE_600, Constants.LOGIN_VERIFY_TOKEN_ERROR);
						return "success";
					}
				}
			}
			PayConsultResult resultList = new PayConsultResult();
			List<ExpertClassifyVo> classifyList = expertClassifyService.getList();
			pageNo2 = 1; // 设定热门专家起始值,因为这里不用分页,与热门回答区分
			List<HotExpertListVo> expertList = expertManagerDao.getPayExpertList(pageNo2, pageSize, member);
			// 根据问题去关联回答
			Pager pager = hotReplyService.getRecommendList(pageNo, pageSize3, member);
			resultList.setClassifyList(classifyList);
			resultList.setExpertList(expertList);
			resultList.setHotReplyList(pager.getResultList());
			resultList.setReplyRowCount(pager.getRowCount());
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, resultList);

		} catch (Exception e) {
			e.printStackTrace();
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
		}
		return "success";
	}

	/**
	 * 2.19 单 一专家分类
	 */
	@SuppressWarnings("unchecked")
	public String getClassifyList() {
		try {
			Member member=new Member();
			if(memberId>0){   //memberId为-1,则为游客身份登录, 直接判断,不用再去数据库查询是否有此会员
				member = memberManagerDao.retrieveMember(memberId);
				if (ObjValid.isValid(member)) {
					if (!accessToken.equals(member.getAccessToken())) {
						object = new ResponseBean(Constants.EXCEPTION_CODE_600, Constants.LOGIN_VERIFY_TOKEN_ERROR);
						return "success";
					}
				}
			}
			PayConsultExpertResult resultList = new PayConsultExpertResult();
			Pager pager = expertManagerDao.getClassifyList(pageNo, pageSize, expertClassify.getId(), member);
			resultList.setResultList(pager.getResultList());
			resultList.setRowCount(pager.getRowCount());
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, resultList);

		} catch (Exception e) {
			e.printStackTrace();
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
		}
		return "success";
	}

	/**
	 * 2.20 全部专家分类
	 */
	@SuppressWarnings("unchecked")
	public String getAllClassifyList() {
		try {
			ExpertClassifyResult resultList = new ExpertClassifyResult();
			Pager pager = expertClassifyService.getAllList(pageNo, pageSize);
			resultList.setClassifyList(pager.getResultList());
			resultList.setRowCount(pager.getRowCount());
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, resultList);

		} catch (Exception e) {
			e.printStackTrace();
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
		}
		return "success";
	}

	/**
	 * 2.21 查看更多的提问专家(不包括外面的推荐专家)
	 */
	@SuppressWarnings("unchecked")
	public String getProExpertList() {
		try {
			Member member=new Member();
			if(memberId>0){   //memberId为-1,则为游客身份登录, 直接判断,不用再去数据库查询是否有此会员
				member = memberManagerDao.retrieveMember(memberId);
				if (ObjValid.isValid(member)) {
					if (!accessToken.equals(member.getAccessToken())) {
						object = new ResponseBean(Constants.EXCEPTION_CODE_600, Constants.LOGIN_VERIFY_TOKEN_ERROR);
						return "success";
					}
				}
			}
			PayConsultExpertResult resultList = new PayConsultExpertResult();
			Pager pager = expertManagerDao.getProExpertList(pageNo, pageSize, member);
			resultList.setResultList(pager.getResultList());
			resultList.setRowCount(pager.getRowCount());
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, resultList);

		} catch (Exception e) {
			e.printStackTrace();
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
		}
		return "success";
	}

	/**
	 * 2.22 付费咨询专家详情
	 */
	@SuppressWarnings("unchecked")
	public String getPayExpertDetail() {
		try {
			Member member=new Member();
			if(memberId>0){   //memberId为-1,则为游客身份登录, 直接判断,不用再去数据库查询是否有此会员
				member = memberManagerDao.retrieveMember(memberId);
				if (ObjValid.isValid(member)) {
					if (!accessToken.equals(member.getAccessToken())) {
						object = new ResponseBean(Constants.EXCEPTION_CODE_600, Constants.LOGIN_VERIFY_TOKEN_ERROR);
						return "success";
					}
				}
			}
			PayExpertDetailResult resultList = new PayExpertDetailResult();
			PayConsultExpertVo expert = expertManagerDao.getPayExpertDetail(expertId, member);
			Pager pager = hotReplyService.getReplyList(pageNo, pageSize, expertId, member);
			resultList.setReplyList(pager.getResultList());
			resultList.setRowCount(pager.getRowCount());
			resultList.setExpert(expert);
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, resultList);

		} catch (Exception e) {
			e.printStackTrace();
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
		}
		return "success";
	}

	/**
	 * 3.24 热门问答专栏查看更多
	 */
	@SuppressWarnings("unchecked")
	public String getAllReply() {
		try {
			Member member=new Member();
			if(memberId>0){   //memberId为-1,则为游客身份登录, 直接判断,不用再去数据库查询是否有此会员
				member = memberManagerDao.retrieveMember(memberId);
				if (ObjValid.isValid(member)) {
					if (!accessToken.equals(member.getAccessToken())) {
						object = new ResponseBean(Constants.EXCEPTION_CODE_600, Constants.LOGIN_VERIFY_TOKEN_ERROR);
						return "success";
					}
				}
			}
			HotReplyVoResult resultList = new HotReplyVoResult();
			Pager pager = hotReplyService.getAllReply(pageNo, pageSize, member);
			resultList.setReplyList(pager.getResultList());
			resultList.setRowCount(pager.getRowCount());
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, resultList);

		} catch (Exception e) {
			e.printStackTrace();
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
		}
		return "success";
	}

	/**
	 * 2.25 售后
	 */
	public String commitFeedBack() {
		try {
			Member member = memberManagerDao.retrieveMember(memberId);
			if (member == null) {
				object = new ResponseBean(Constants.EXCEPTION_CODE_600, "用户不存在");
				return "success";
			}
			String array = member.getAccessToken();
			if (!accessToken.equals(array)) {
				object = new ResponseBean(Constants.EXCEPTION_CODE_600, Constants.LOGIN_VERIFY_TOKEN_ERROR);// 给Action的已经定义好的object对象赋值
				return "success";
			}
			FeedBack entity = new FeedBack();
			entity.setContent(feedBack.getContent());
			entity.setProblemId(feedBack.getProblemId());
			entity.setAddTime(new Date());
			entity.setEditTime(new Date());
			entity.setMemberId(member.getId());
			entity.setExpertId(expertId);
			entity.setStatus("1");
			hotReplyService.commitFeedBack(entity);
			object = new ResponseBean(Constants.SUCCESS_CODE_200, "提交成功");

		} catch (Exception e) {
			e.printStackTrace();
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
		}
		return "success";
	}

	/**
	 * 2.26 专家提交回复信息
	 */
	public String commitReply() {
		try {
			Member member = memberManagerDao.retrieveMember(memberId);
			if (member == null) {
				object = new ResponseBean(Constants.EXCEPTION_CODE_600, "用户不存在");
				return "success";
			}
			if (!accessToken.equals(member.getAccessToken())) {
				object = new ResponseBean(Constants.EXCEPTION_CODE_600, Constants.LOGIN_VERIFY_TOKEN_ERROR);// 给Action的已经定义好的object对象赋值
				return "success";
			}
			if (voice != null) {
				hotReply.setVoice(savePic(voiceFileName, voice)); // 处理上传的音频文件
			}
			StringBuffer sbf = new StringBuffer();
			if (pic != null) {
				if (pic.length > 0) {
					System.out.println(picFileName.length);
					for (int i = 0; i <= Math.min(pic.length - 1, picFileName.length - 1); i++) {
						sbf.append(",").append(savePic(picFileName[i], pic[i]));
					}
				}
			}
			// 有就拼接,没有就传空值
			hotReply.setDispose(0);
			hotReply.setPicture(sbf.toString().length() < 1 ? "" : sbf.toString().replaceFirst(",", ""));
			hotReplyService.commitReply(hotReply, member.getId());
			/**
			 * 专家回答了问题，得到用户支付的money
			 */
			HotProblem hotProblem=hotReplyService.getHotProblem(hotReply.getProblemId());
			ExpenseTotal expenseTotal=payManagerDao.getPayConsultExpenseTotal(hotProblem);
			Member questioner=memberManagerDao.retrieveMember(expenseTotal.getMemberId());
//			payManagerDao.addExpertWallet(expenseTotal, member,questioner);
			payManagerDao.addIncomeByPayNew(0,questioner,expenseTotal);
			object = new ResponseBean(Constants.SUCCESS_CODE_200, "提交成功");

		} catch (Exception e) {
			e.printStackTrace();
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
		}
		return "success";
	}

	/**
	 * 3.18 我的问答--未答问答
	 */
	@SuppressWarnings("unchecked")
	public String getNotReply() {
		try {
			Member member = memberManagerDao.retrieveMember(memberId);
			if (member == null) {
				object = new ResponseBean(Constants.EXCEPTION_CODE_600, "用户不存在");
				return "success";
			}
			if (!accessToken.equals(member.getAccessToken())) {
				object = new ResponseBean(Constants.EXCEPTION_CODE_600, Constants.LOGIN_VERIFY_TOKEN_ERROR);// 给Action的已经定义好的object对象赋值
				return "success";
			}
			HotProblemResult resultList = new HotProblemResult();
			Pager pager = hotReplyService.getNotReply(pageNo, pageSize, member.getId());
			resultList.setResultList(pager.getResultList());
			resultList.setRowCount(pager.getRowCount());
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, resultList);

		} catch (Exception e) {
			e.printStackTrace();
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
		}
		return "success";
	}

	/**
	 * 3.19 我的问答--历史问答
	 */
	@SuppressWarnings("unchecked")
	public String getHistoryReply() {
		try {
			Member member = memberManagerDao.retrieveMember(memberId);
			if (ObjValid.isValid(member)) {
				if (!accessToken.equals(member.getAccessToken())) {
					object = new ResponseBean(Constants.EXCEPTION_CODE_600, Constants.LOGIN_VERIFY_TOKEN_ERROR);
					return "success";
				}
			}
			HotReplyVoResult resultList = new HotReplyVoResult();
			Pager pager = hotReplyService.getHistoryReply(pageNo, pageSize, member);
			resultList.setReplyList(pager.getResultList());
			resultList.setRowCount(pager.getRowCount());
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, resultList);
		} catch (Exception e) {
			e.printStackTrace();
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
		}
		return "success";
	}

	/**
	 * 3.20  历史问答详情(个人中心)
	 */
	public String getReplyDetail() throws Exception {
		try {
			Member member = memberManagerDao.retrieveMember(memberId);
			if (member == null) {
				object = new ResponseBean(Constants.EXCEPTION_CODE_600, "用户不存在");
				return "success";
			}
			if (!accessToken.equals(member.getAccessToken())) {
				object = new ResponseBean(Constants.EXCEPTION_CODE_600, Constants.LOGIN_VERIFY_TOKEN_ERROR);// 给Action的已经定义好的object对象赋值
				return "success";
			}
			HotReplyDetailVo detailVo = hotReplyService.getReplyDetail(replyId);
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, detailVo);
		} catch (Exception e) {
			e.printStackTrace();
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
		}
		return "success";
	}

	/**
	 * 2.27 热门问题详情(未支付)
	 */
	public String notPayDetail() throws Exception {
		try {
			Member member=new Member();
			if(memberId>0){   //memberId为-1,则为游客身份登录, 直接判断,不用再去数据库查询是否有此会员
				member = memberManagerDao.retrieveMember(memberId);
				if (ObjValid.isValid(member)) {
					if (!accessToken.equals(member.getAccessToken())) {
						object = new ResponseBean(Constants.EXCEPTION_CODE_600, Constants.LOGIN_VERIFY_TOKEN_ERROR);
						return "success";
					}
				}
			}
			HotReplyVo detailVo = hotReplyService.getNotPayDetail(replyId, member);
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, detailVo);

		} catch (Exception e) {
			e.printStackTrace();
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
		}
		return "success";

	}

	/**
	 * 2.28 热门问题详情(已支付)
	 */
	public String paidDetail() throws Exception {
		try {
			Member member = memberManagerDao.retrieveMember(memberId);
			if (member == null) {
				object = new ResponseBean(Constants.EXCEPTION_CODE_600, "用户不存在");
				return "success";
			}
			if (!accessToken.equals(member.getAccessToken())) {
				object = new ResponseBean(Constants.EXCEPTION_CODE_600, Constants.LOGIN_VERIFY_TOKEN_ERROR);// 给Action的已经定义好的object对象赋值
				return "success";
			}
			HotReplyPaidVo detailVo = hotReplyService.getPaidDetail(replyId,member.getId());
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, detailVo);

		} catch (Exception e) {
			e.printStackTrace();
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
		}
		return "success";

	}

	/**
	 * 2.29 热门回复的点赞功能
	 */
	public String replyLiked() throws Exception {
		try {
			Member member = memberManagerDao.retrieveMember(memberId);
			if (member == null) {
				object = new ResponseBean(Constants.EXCEPTION_CODE_600, "用户不存在");
				return "success";
			}
			if (!accessToken.equals(member.getAccessToken())) {
				object = new ResponseBean(Constants.EXCEPTION_CODE_600, Constants.LOGIN_VERIFY_TOKEN_ERROR);// 给Action的已经定义好的object对象赋值
				return "success";
			}
			hotReplyService.addReplyLiked(hotReply.getId(), hotReply.getExpertId(), member.getId(), flag);
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS);

		} catch (Exception e) {
			e.printStackTrace();
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
		}
		return "success";

	}

	/**
	 * 2.30 专家设置模块显示
	 */
	public String settingsView() throws Exception {
		try {
			Member member = memberManagerDao.retrieveMember(memberId);
			if (member == null) {
				object = new ResponseBean(Constants.EXCEPTION_CODE_600, "用户不存在");
				return "success";
			}
			String array = member.getAccessToken();
			if (!accessToken.equals(array)) {
				object = new ResponseBean(Constants.EXCEPTION_CODE_600, Constants.LOGIN_VERIFY_TOKEN_ERROR);// 给Action的已经定义好的object对象赋值
				return "success";
			}
			MoneyAndFieldResult result = new MoneyAndFieldResult();
			HotReplyFees fees = hotReplyService.getReplyFees(member.getId());
			List<ExpertClassifyVo> classifyList = expertClassifyService.getClassifyList(member.getId());
			if (fees != null) {
				if (fees.getMemberId() != null) {
					result.setOpenMoney(fees.getMoney());
				}
				if (fees.getPrivateMoney() != null) {
					result.setPrivateMoney(fees.getPrivateMoney());
				}
			}
			result.setClassifyList(classifyList);
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, result);

		} catch (Exception e) {
			e.printStackTrace();
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
		}
		return "success";

	}

	/**
	 * 2.31 专家设置提问价格和所属领域
	 */
	public String setMoneyAndField() throws Exception {
		try {
			Member member = memberManagerDao.retrieveMember(memberId);
			if (member == null) {
				object = new ResponseBean(Constants.EXCEPTION_CODE_600, "用户不存在");
				return "success";
			}
			String array = member.getAccessToken();
			if (!accessToken.equals(array)) {
				object = new ResponseBean(Constants.EXCEPTION_CODE_600, Constants.LOGIN_VERIFY_TOKEN_ERROR);// 给Action的已经定义好的object对象赋值
				return "success";
			}
			hotReplyService.settingsMoney(member.getId(), hotReplyFees.getMoney(), hotReplyFees.getPrivateMoney());
			expertClassifyService.settingsField(classifyIds, member.getId());
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS);

		} catch (Exception e) {
			e.printStackTrace();
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
		}
		return "success";

	}
	
	/**
	 * 2.41  他的回答（5期）  
	 */
	@SuppressWarnings("unchecked")
	public String hisAnswer() {
		Member member=new Member();
		if(memberId>0){   //memberId为-1,则为游客身份登录, 直接判断,不用再去数据库查询是否有此会员
			member = memberManagerDao.retrieveMember(memberId);
			if (ObjValid.isValid(member)) {
				if (!accessToken.equals(member.getAccessToken())) {
					object = new ResponseBean(Constants.EXCEPTION_CODE_600, Constants.LOGIN_VERIFY_TOKEN_ERROR);
					return "success";
				}
			}
		}
		PayConsultListVo resultList = new PayConsultListVo();
		Pager pager = hotReplyService.getReplyList2(pageNo, pageSize, expertId, member);
		resultList.setReplyList(pager.getResultList());
		resultList.setRowCount(pager.getRowCount());
		object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, resultList);
		return "success";
	}

	/**
	 * 上传图片  ,因为想把图片和音频文件的路径区分开, 所以就另写了一个另外一个方法
	 */
	public String savePic(String name, File file) {
		String inputFileExt = name.substring(name.lastIndexOf(".") + 1);
		String fileName=MD5.GetMD5Code(new DateHelper().getRandomNum()).substring(8,24)+"."+inputFileExt;
		InputStream is = null;
		try {
			is = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String key="images/"+inputFileExt+"/"+fileName;
		AliyunOOSUtil.upload(key, is);
		return key;
	}

	
	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public ExpertClassify getExpertClassify() {
		return expertClassify;
	}

	public void setExpertClassify(ExpertClassify expertClassify) {
		this.expertClassify = expertClassify;
	}
	public int getPageSize3() {
		return pageSize3;
	}

	public void setPageSize3(int pageSize3) {
		this.pageSize3 = pageSize3;
	}

	public int getPageNo2() {
		return pageNo2;
	}

	public void setPageNo2(int pageNo2) {
		this.pageNo2 = pageNo2;
	}

	public Long getExpertId() {
		return expertId;
	}

	public void setExpertId(Long expertId) {
		this.expertId = expertId;
	}

	public HotProblem getProblem() {
		return problem;
	}

	public void setProblem(HotProblem problem) {
		this.problem = problem;
	}

	public FeedBack getFeedBack() {
		return feedBack;
	}

	public void setFeedBack(FeedBack feedBack) {
		this.feedBack = feedBack;
	}

	public HotReply getHotReply() {
		return hotReply;
	}

	public void setHotReply(HotReply hotReply) {
		this.hotReply = hotReply;
	}

	public File getVoice() {
		return voice;
	}

	public void setVoice(File voice) {
		this.voice = voice;
	}

	public String getVoiceFileName() {
		return voiceFileName;
	}

	public void setVoiceFileName(String voiceFileName) {
		this.voiceFileName = voiceFileName;
	}

	public File getSaveVoice() {
		return saveVoice;
	}

	public void setSaveVoice(File saveVoice) {
		this.saveVoice = saveVoice;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public HotReplyLiked getHotReplyLiked() {
		return hotReplyLiked;
	}

	public void setHotReplyLiked(HotReplyLiked hotReplyLiked) {
		this.hotReplyLiked = hotReplyLiked;
	}

	public File[] getPic() {
		return pic;
	}

	public void setPic(File[] pic) {
		this.pic = pic;
	}

	public String[] getPicFileName() {
		return picFileName;
	}

	public void setPicFileName(String[] picFileName) {
		this.picFileName = picFileName;
	}

	public HotReplyFees getHotReplyFees() {
		return hotReplyFees;
	}

	public void setHotReplyFees(HotReplyFees hotReplyFees) {
		this.hotReplyFees = hotReplyFees;
	}

	public String[] getClassifyIds() {
		return classifyIds;
	}

	public void setClassifyIds(String[] classifyIds) {
		this.classifyIds = classifyIds;
	}
	public Long getReplyId() {
		return replyId;
	}

	public void setReplyId(Long replyId) {
		this.replyId = replyId;
	}
}
