package cc.messcat.web.server;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mipush.MiPushHelper;

import cc.messcat.bases.BaseAction;
import cc.messcat.entity.ActivityExchangeRecord;
import cc.messcat.entity.BuysRecord;
import cc.messcat.entity.ExpenseTotal;
import cc.messcat.entity.Coupn;
import cc.messcat.entity.FreeVideo;
import cc.messcat.entity.LiveVideo;
import cc.messcat.entity.LiveVideoDistributor;
import cc.messcat.entity.Member;
import cc.messcat.entity.MemberMp;
import cc.messcat.entity.StandardReading;
import cc.messcat.entity.SystemMessage;
import cc.messcat.entity.CoupnUser;
import cc.messcat.entity.WechatSignature;
import cc.messcat.enums.RegistSourceEnum;
import cc.messcat.vo.ActivityMemberVo;
import cc.messcat.vo.HadExchangeVo;
import cc.messcat.vo.MemberVo;
import cc.messcat.vo.ResponseBean;
import cc.messcat.wechat.weixin.popular.api.TicketAPI;
import cc.messcat.wechat.weixin.popular.api.TokenAPI;
import cc.messcat.wechat.weixin.popular.bean.Ticket;
import cc.messcat.wechat.weixin.popular.util.SignUtil;
import cc.modules.constants.Constants;
import cc.modules.util.DateHelper;
import cc.modules.util.EmojiUtils;
import cc.modules.util.GetJsonData;
import cc.modules.util.OrderSnUtil;
import cc.modules.util.PropertiesFileReader;
import cc.modules.util.ServletContextUtil;
import cc.modules.util.SmsUtil;
import cc.modules.util.StringUtil;
import cc.modules.util.XmlUtil;

/**
 * @author nelson 活动Action,用于分享推荐活动
 */
@SuppressWarnings("serial")
public class ActivityAction extends BaseAction implements ServletRequestAware {
	private static String apiDomain = PropertiesFileReader.getByKey("api.domain");
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private HttpSession session;
	private HttpServletRequest request;
	private Object object;
	private String changeType; // 0-兑换一个月的会员 1-兑换一本书 2-兑换一次培训S
	private Long memberId;
	private String uri;
	private String mobile;
	private String mobileCode;
	private String business;
	private int inApp;// 0-外部网页调 1-app内部调（用于跳过验证码步骤）
	private Long videoId;
	private Long[] coupnIds;// 要赠送的卡券，中间用逗号隔开
	private String type;//购买记录所属东西的type
	private Long relateId;
	private String openId;//微信openId
	private String wechatPhoto;//微信头像
	private String wechatName;//微信昵称
	private String unionId;//微信unionId
	private double money;  //实际支付金额
	private double integral; //分销员获取到的积分

	/**
	 * 进入活动页时，返回此用户当前可兑换的推荐人个数
	 * 
	 * @return
	 */
	public String initExchange() {// memberid
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		response.setContentType("textml;charset=UTF-8");
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		response.setHeader("Access-Control-Max-Age", "0");
		response.setHeader("Access-Control-Allow-Headers",
				"Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("XDomainRequestAllowed", "1");
		// 根据id查找对应的member并获取他的推荐次数
		Member member = memberManagerDao.retrieveMember(memberId);
		int recommendTimes = member.getRecommend_times();
		object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, recommendTimes);
		return "success";
	}

	/**
	 * 用户点击兑换按钮
	 * 
	 * @return
	 * @throws ParseException
	 */
	public String exchangeGift() throws ParseException {// memberid changeType
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		response.setContentType("textml;charset=UTF-8");
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		response.setHeader("Access-Control-Max-Age", "0");
		response.setHeader("Access-Control-Allow-Headers",
				"Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("XDomainRequestAllowed", "1");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date endDate = sdf.parse("2017-12-31 23:59:59");
		if (new Date().before(endDate)) {
			// 先查出该用户的推荐数
			Member member = memberManagerDao.retrieveMember(memberId);
			int recommendTimes = member.getRecommend_times();
			if (recommendTimes < 0) {
				recommendTimes = 0;
			}

			List<ActivityExchangeRecord> records = activityExchangeRecordManagerDao.getExchangeCount(changeType,
					memberId);
			int exchangeCount = 0;
			if (records != null) {// 首次兑换某个类型，records为null
				exchangeCount = records.size();
				if (exchangeCount >= 12) {
					object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_EXCHANGE_EXCEED);
					return "success";
				}
			}
			if (recommendTimes >= 3 && changeType.equals("0") && exchangeCount < 12) {// ChangeCount兑换的次数，最多只能换12个月的会员
				// 往兑换表新增一条记录
				ActivityExchangeRecord record = new ActivityExchangeRecord();
				record.setMemberId(memberId);
				record.setChangeType("0");
				record.setChangeTimes(new Date());
				activityExchangeRecordManagerDao.addExchangeRecord(record);
				// 减少推荐数
				recommendTimes -= 3;
				member.setRecommend_times(recommendTimes);
				// 增加会员的到期时间
				Date endTime = member.getEndTime();
				if (endTime == null) {
					endTime = new Date();
				}
				long time = endTime.getTime();
				long currentTime = new Date().getTime();
				if (time > currentTime) {
					// 会员未过期
					endTime = new Date(time + (long) 30 * 24 * 60 * 60 * 1000);
				} else {
					// 会员过期
					endTime = new Date(currentTime + (long) 30 * 24 * 60 * 60 * 1000);
				}

				member.setEndTime(endTime);
				member.setGrade("1");
				memberManagerDao.updateMember(member);
				object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, record);
			} else if (recommendTimes >= 10 && changeType.equals("1") && exchangeCount < 1) {// 只能换一次书
				ActivityExchangeRecord record = new ActivityExchangeRecord();
				record.setMemberId(memberId);
				record.setChangeType("1");
				record.setChangeTimes(new Date());
				activityExchangeRecordManagerDao.addExchangeRecord(record);
				// 减少推荐数
				recommendTimes -= 10;
				member.setRecommend_times(recommendTimes);
				memberManagerDao.updateMember(member);
				object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, record);
				// standreading 229
				// 在购买记录表里为该用户添加一条记录<黄文秀的书>
				BuysRecord buysRecord = new BuysRecord();
				buysRecord.setType("3");
				buysRecord.setContent("推荐用户兑换所得");
				buysRecord.setAddTime(new Date());
				buysRecord.setEditTime(new Date());
				buysRecord.setStatus("1");
				buysRecord.setMemberId(member);
				buysRecord.setNumber(OrderSnUtil.generateMemberRechargeSn());
				buysRecord.setMoney(new BigDecimal(0));
				buysRecord.setOriginalPrice(new BigDecimal(40.0));
				buysRecord.setPayStatus("1");
				buysRecord.setPayTime(new Date());
				// 根据id找到实体
				StandardReading standardReading = standardReadManagerDao.retrieveStandardReading(229);
				buysRecord.setStandardReadId(standardReading);
				buysRecordManagerDao.addBuysRecord(buysRecord);

			} else if (recommendTimes >= 17 && changeType.equals("2") && exchangeCount < 1) {// 只能换一次培训
				ActivityExchangeRecord record = new ActivityExchangeRecord();
				record.setMemberId(memberId);
				record.setChangeType("2");
				record.setChangeTimes(new Date());
				activityExchangeRecordManagerDao.addExchangeRecord(record);
				// 生成一个培训码
				StringBuffer sb = new StringBuffer();
				Random random = new Random();
				for (int i = 0; i <= 10; i++) {
					int a = random.nextInt(10);
					sb.append(a);
				}
				record.setTrainCode(sb.toString());
				// 减少推荐数
				recommendTimes -= 25;
				member.setRecommend_times(recommendTimes);
				memberManagerDao.updateMember(member);

				// 给用户发送培训相关信息
				StringBuffer mesc = new StringBuffer();
				mesc.append("尊敬用户：您好，恭喜您成功兑换现场培训名额一份。现场培训由CVC威凯培训学院提供，您的兑换密码为【");
				mesc.append(sb);
				mesc.append("】。请您及时与CVC威凯培训学院联系。联系人：麦进永   电话：189-2612-8654");
				Map mcode = XmlUtil.parseStringXmlToMap(SmsUtil.sendMessage(member.getMobile(), mesc.toString()));// 第三方？
				object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, record);
			} else {
				object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_EXCHANGE_FAIL);
			}
		} else {
			object = new ResponseBean(Constants.SUCCESS_CODE_300, "活动已过期");
		}
		return "success";
	}

	/**
	 * 微信分享活动时需要
	 * 
	 * @return
	 */
	public String getToken() {
		// 给允许头，用于跨域请求
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		response.setContentType("textml;charset=UTF-8");
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		response.setHeader("Access-Control-Max-Age", "0");
		response.setHeader("Access-Control-Allow-Headers",
				"Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("XDomainRequestAllowed", "1");
		String appid = "wx4e844032c24a4508";
		String secret = "b27ab0842771ac751445ad0c043088b0";
		String wechatToken = TokenAPI.token(appid, secret).getAccess_token();
		Ticket jsapi_ticket = TicketAPI.ticketGetticket(wechatToken);
		Map<String, String> ret = SignUtil.sign(jsapi_ticket.getTicket(), uri);
		String signature = ret.get("signature");
		String randomStr = ret.get("nonceStr");
		String timeStamp = ret.get("timestamp");
		WechatSignature wSignature = new WechatSignature();
		if (wechatToken != null) {
			ServletContext sc = ServletContextUtil.get();
			sc.removeAttribute("wechatToken");
			sc.setAttribute("wechatToken", wechatToken);
			if (jsapi_ticket != null) {
				sc.removeAttribute("jsapi_ticket");
				sc.setAttribute("jsapi_ticket", jsapi_ticket);
			}
		}
		wSignature.setAppid(appid);
		wSignature.setSignature(signature);
		wSignature.setNoncestr(randomStr);
		wSignature.setTimestamp(Long.valueOf(timeStamp));
		// wSignature.setTicket(ticket);
		object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, wSignature);
		return "success";
	}

	/**
	 * 注册页需要显示推荐人的手机号码
	 * 
	 * @return
	 */
	public String getMobileByMemberId() {
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		response.setContentType("textml;charset=UTF-8");
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		response.setHeader("Access-Control-Max-Age", "0");
		response.setHeader("Access-Control-Allow-Headers",
				"Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("XDomainRequestAllowed", "1");
		if (memberId != null) {
			Member member;
			String mobile = "";
			member = memberManagerDao.getMobileById(memberId);
			mobile = member.getMobile();
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, mobile);
		} else {
			object = new ResponseBean(Constants.FAIL_CODE_400, Constants.MSG_FAIL);
		}
		return "success";
	}

	/**
	 * 分享活动，邀请有礼页面返回memberId和推荐次数
	 * 
	 * @return
	 */
	public String getIdAndRecommendTimes() {
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		response.setContentType("textml;charset=UTF-8");
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		response.setHeader("Access-Control-Max-Age", "0");
		response.setHeader("Access-Control-Allow-Headers",
				"Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("XDomainRequestAllowed", "1");
		HttpSession session1 = request.getSession();
		if (StringUtil.isNotBlank(mobile) && StringUtil.isNotBlank(mobileCode)) {
			if (!mobile.matches("\\d{11}")) {
				object = new ResponseBean(Constants.FAIL_CODE_400, "手机号码不正确");
				return "success";
			}
			// 短信验证码校验
			if (!memberManagerDao.verifyMobileCode(business, mobile, mobileCode, session1)) {
				object = new ResponseBean(Constants.FAIL_CODE_400, "验证码错误");
				return "success";
			}
			Member member = memberManagerDao.findMemberByMobile(mobile);
			int recommendTimes = member.getRecommend_times();
			ActivityMemberVo activityMemberVo = new ActivityMemberVo();
			activityMemberVo.setMemberId(member.getId());
			activityMemberVo.setRecommendTimes(recommendTimes);
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, activityMemberVo);
		}
		return "success";
	}

	/**
	 * @return 圣诞节送会员活动：老用户可获取一个月的会员，新用户注册亦可获取
	 */
	public String christmas() {
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		response.setContentType("textml;charset=UTF-8");
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		response.setHeader("Access-Control-Max-Age", "0");
		response.setHeader("Access-Control-Allow-Headers",
				"Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("XDomainRequestAllowed", "1");
		HttpSession session1 = request.getSession();
		// 活动开始和截止时间
		Date current = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date beginDate = new Date();
		Date endDate = new Date();
		try {
			beginDate = sdf.parse("2018-01-29 00:00:01");
			endDate = sdf.parse("2018-01-29 23:59:59");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (current.before(beginDate) || current.after(endDate)) {
			object = new ResponseBean(Constants.FAIL_CODE_400, "请在活动时间内参与！");
			return "success";
		}

		boolean change;// true是兑换过了，false还没兑换过
		if (StringUtil.isNotBlank(mobile) && StringUtil.isNotBlank(mobileCode)) {
			if (!mobile.matches("\\d{11}")) {
				object = new ResponseBean(Constants.FAIL_CODE_400, "手机号码不正确");
				return "success";
			}
			// 短信验证码校验
			if (!memberManagerDao.verifyMobileCode(business, mobile, mobileCode, session1)) {
				object = new ResponseBean(Constants.FAIL_CODE_400, "验证码错误");
				return "success";
			}
		}
		// 先判断该手机号码是否已注册，还需判断该用户是否已参加过该活动
		Member member = memberManagerDao.findMemberByMobile(mobile);
		// 给用户送一年会员
		ActivityExchangeRecord aer = new ActivityExchangeRecord();
		if (member != null) {
			// 注册了的会员则判断时候已经参加过活动，避免重复兑换
			List<ActivityExchangeRecord> records = activityExchangeRecordManagerDao.getExchangeCount("4",
					member.getId());
			if (records != null && records.size() > 0) {
				change = true;
				object = new ResponseBean(Constants.SUCCESS_CODE_200, "您已参加过本次活动，不可重复参加!", change);
				return "success";
			}

			// 送书
			ExpenseTotal bNew = new ExpenseTotal();
			bNew.setType("8");
			bNew.setContent("活动赠送");
			bNew.setAddTime(new Date());
			bNew.setMemberId(member.getId());
			bNew.setMoney(new BigDecimal(0));
			bNew.setPayStatus("1");// 防止赠送的购买记录被定时器清空
			bNew.setRelatedId(33L);
			memberManagerDao.save(bNew);

		} else {// 还没注册的后台为他注册

			// 注册和送年费会员
			member = new Member(mobile, "1");
			member.setBankId(0L);
			member.setGrade("1");
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.YEAR, 1);
			member.setStartTime(new Date());
			member.setYearEndTime(calendar.getTime());
			member.setType(String.valueOf(0));
			member.setRegistSource(RegistSourceEnum.ACTIVITY.getType());
			MemberVo vo = memberManagerDao.addAppMember(member, request);
			Member user = memberManagerDao.findMemberByMobile(mobile);
			// 送书
			ExpenseTotal bNew = new ExpenseTotal();
			bNew.setType("8");
			bNew.setContent("活动赠送");
			bNew.setAddTime(new Date());
			bNew.setMemberId(user.getId());
			bNew.setMoney(new BigDecimal(0));
			bNew.setPayStatus("1");// 防止赠送的购买记录被定时器清空
			bNew.setRelatedId(33L);
			memberManagerDao.save(bNew);

			aer.setMemberId(user.getId());
		}
		aer.setChangeTimes(new Date());
		aer.setChangeType("4");
		aer.setMemberId(member.getId());
		activityExchangeRecordManagerDao.addExchangeRecord(aer);
		change = false;
		object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, change);
		return "success";
	}

	/**
	 * 邀请三个人，课程免单
	 * videoId
	 * mobile	app调，这个参数为null;微信调，这个参数有值
	 * verificationCode
	 * openId	这个参数有值即为微信分享调用此接口，这个参数为null即为app分享调此接口
	 * wechatName
	 * wechatPhoto
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public Object freeOrder() throws UnsupportedEncodingException {
		//用于扫码注册的跨域请求
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("textml;charset=UTF-8");
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		response.setHeader("Access-Control-Max-Age", "0");
		response.setHeader("Access-Control-Allow-Headers",
						"Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		HttpSession session1 = request.getSession();
		Long recommendTimes = 0L;
		HadExchangeVo vo = new HadExchangeVo();
		//首先查看这个视频是否是免单课程
		FreeVideo freeVideo = liveService.getFreeVideo(videoId);
		if(freeVideo == null) {
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_COMMODITY_NOT_FREE);
			return "success";
		}
		//然后根据用户的openId查看该用户的推荐次数
		if(openId == null) {//统计app分享次数
			recommendTimes = liveService.getAppRecommendTimes(memberId,videoId);
			Member member2=memberManagerDao.getMobileById(memberId);
		} else {//统计微信分享次数
			if (!mobile.matches("\\d{11}")) {
				object = new ResponseBean(Constants.FAIL_CODE_400, "手机号码不正确");
				return "success";
			}
			// 短信验证码校验
			if (!memberManagerDao.verifyMobileCode(business, mobile, mobileCode, session1)) {
				object = new ResponseBean(Constants.FAIL_CODE_400, "验证码错误");
				return "success";
			}
			recommendTimes = liveService.getWechatRecommendTimes(openId,videoId);
		}
		LiveVideo video = liveService.getLiveVideoById(videoId);
		if(video == null) {
			object = new ResponseBean(Constants.FAIL_CODE_400, Constants.MSG_DATA_EXCEPT);
			return "success";
		}
		String dataUrl="";
		if(video.getVideoType().equals("0")) {
			if(video.getClassify()==1) {
				dataUrl="weikai://cert-map?target=live&id="+videoId+"&type=2";
			}else if(video.getClassify()==4){
				dataUrl="weikai://cert-map?target=live&id="+videoId+"&type=1";
			}
		}else {
			if(video.getClassify()==1) {
				dataUrl="weikai://cert-map?target=course&id="+videoId+"&type=2";
			}else if(video.getClassify()==4){
				dataUrl="weikai://cert-map?target=course&id="+videoId+"&type=1";
			}
		}
		Member member = null;
		//先查看该手机号码是否已注册，如果未注册就给他注册
		if(mobile != null) {
			member = memberManagerDao.findMemberByMobile(mobile);
			if(member == null) {//如果未注册便为他注册
				member = new Member(mobile, "1");
				member.setOpenId(openId);
				member.setImagePath(wechatPhoto);
				member.setRegistSource(RegistSourceEnum.ACTIVITY.getType());
				memberManagerDao.addAppMember(member, request);
				member = memberManagerDao.findMemberByMobile(mobile);
			}
		} else {
			member = memberManagerDao.getMobileById(memberId);
		}
		SystemMessage systemMessage = new SystemMessage();
		systemMessage.setAddTime(new Date());
		systemMessage.setEditTime(new Date());
		systemMessage.setMemberIds(member.getId().toString());
		systemMessage.setPushType("1");
		systemMessage.setStatus("1");
		systemMessage.setRelateId(videoId);
		systemMessage.setRegionId(0L);
		systemMessage.setType("10");
		systemMessage.setPhoto("");
		systemMessage.setDataUrl(dataUrl);
		LiveVideoDistributor distributor=activityExchangeRecordManagerDao.getLiveVideoDistributor(videoId,memberId);
		String distriStr="";
		if(distributor!=null) {
			BigDecimal totalIntegral=activityExchangeRecordManagerDao.getTotalIntegral(memberId);
			distriStr="----------------------------------\r\n" + 
				      "*进度* ： 本课程已邀请"+distributor.getBringNum()+"人扫码，"+distributor.getBoughtNum()+"人下单\r\n" + 
				      "*积分奖励* ：  本次将为您赢得10待核积分，若该好友14天内没取关公众号，即可到账。\r\n" + 
				      "*已到账积分* ： "+totalIntegral;
		}
		String endtime=DateHelper.dataToString(freeVideo.getDeadline(), "yyyy年MM月dd日  HH:mm:ss");
		if (recommendTimes < freeVideo.getTargetNum()) {
			String today=DateHelper.dataToString(new Date(), "yyyy年MM月dd日  HH:mm:ss");
			String notifyTitle="【世界认证地图】成员加入提醒：";
			String smsTitle="成员加入提醒：\r\n";
			String notifyDesc="恭喜！有一位新朋友扫码支持您啦！\r\n" + 
						 	  "姓名："+URLEncoder.encode(wechatName, "utf-8")+"\r\n" +
						 	  "时间："+today+"\r\n" + 
						 	  "已完成"+recommendTimes+"人，"+freeVideo.getTargetNum()+"位小伙伴支持，即可免费获得《"+video.getTitle()+"》 ！\r\n" + 
						 	  "【活动有效期】 ： 截止"+endtime;
			String smsDesc="恭喜！有一位新朋友扫码支持您啦！\r\n" + 
				 	  	   "姓名："+EmojiUtils.filterEmoji(wechatName)+"\r\n" +
				 	  	   "时间："+today+"\r\n" + 
				 	  	   "已完成"+recommendTimes+"人，"+freeVideo.getTargetNum()+"位小伙伴支持，即可免费获得《"+video.getTitle()+"》 ！\r\n" + 
				 	  	   "*活动有效期* ： 截止"+endtime;
			if(distributor!=null) {
				MiPushHelper.sendAndroidUserAccount(notifyTitle,smsDesc+"\r\n"+distriStr,dataUrl, member.getMobile()); 
				MiPushHelper.sendIOSUserAccount(notifyTitle+smsDesc+"\r\n"+distriStr,dataUrl, member.getMobile());
				SmsUtil.sendMessage(member.getMobile(), smsTitle+smsDesc+distriStr);
				systemMessage.setTitle(notifyTitle+"\r\n"+notifyDesc+"\r\n"+distriStr);
			}else {
				MiPushHelper.sendAndroidUserAccount(notifyTitle,smsDesc,dataUrl, member.getMobile());
				MiPushHelper.sendIOSUserAccount(notifyTitle+smsDesc,dataUrl, member.getMobile());
				SmsUtil.sendMessage(member.getMobile(), smsTitle+smsDesc);
				systemMessage.setTitle(notifyTitle+"\r\n"+notifyDesc);
			}
			systemMessageManagerDao.addSystemMessage(systemMessage);
			object = new ResponseBean(Constants.FAIL_CODE_400, Constants.MSG_NO_QUALIFICATION,vo);
			return "success";
		} else if(recommendTimes>=freeVideo.getTargetNum()){
			vo.setFinishTask(true);  //已完成任务
			ExpenseTotal buysRecord = expenseTotalManagerDao.checkBuyStatus(member.getId(), videoId);
			if(buysRecord != null) {
				vo.setHadChanged(true);  //已兑换课程
				object = new ResponseBean(Constants.FAIL_CODE_400, Constants.MSG_DATA_EXIST,vo);
				return "success";
			}
			String notifyTitle="【世界认证地图】任务完成通知：";
			String smsTitle="任务完成通知：\r\n";
			String notifyDesc="恭喜您任务完成啦！\r\n" + 
							  "任务名称：《"+video.getTitle()+"》\r\n" + 
							  "任务内容：免单挑战\r\n" + 
							  "任务完成情况：挑战成功\r\n" + 
							  "《"+video.getTitle()+"》课程已经放入APP“我的”—“已购”中，赶紧前往APP进行学习吧！\r\n" + 
							  "【活动有效期】截止"+endtime;
			String smsDesc="恭喜您任务完成啦！\r\n" + 
					  	   "任务名称：《"+video.getTitle()+"》\r\n" + 
					  	   "任务内容：免单挑战\r\n" + 
					  	   "任务完成情况：挑战成功\r\n" + 
					  	   "《"+video.getTitle()+"》课程已经放入APP“我的”—“已购”中，赶紧前往APP进行学习吧！\r\n" + 
					  	   "*活动有效期* ： 截止"+endtime;
			if(distributor!=null) {
				MiPushHelper.sendAndroidUserAccount(notifyTitle,notifyDesc+"\r\n"+distriStr,dataUrl, member.getMobile()); 
				MiPushHelper.sendIOSUserAccount(notifyTitle+notifyDesc+"\r\n"+distriStr,dataUrl, member.getMobile());
				SmsUtil.sendMessage(member.getMobile(), smsTitle+smsDesc+distriStr);
				systemMessage.setTitle(notifyTitle+"\r\n"+notifyDesc+"\r\n"+distriStr);
			}else {
				MiPushHelper.sendAndroidUserAccount(notifyTitle,notifyDesc,dataUrl, member.getMobile()); 
				MiPushHelper.sendIOSUserAccount(notifyTitle+notifyDesc,dataUrl, member.getMobile());
				SmsUtil.sendMessage(member.getMobile(), smsTitle+smsDesc);
				systemMessage.setTitle(notifyTitle+"\r\n"+notifyDesc);
			}
			systemMessageManagerDao.addSystemMessage(systemMessage);
			String type="8";
			String content="邀请三人免单";
			long memberId=member.getId();
			String number=OrderSnUtil.generateTopUpWalletSn();
			BigDecimal money = new BigDecimal(0.00);
			BigDecimal originalPrice=video.getPrice();
			String payType="4";
			long relatedId=videoId;
			ExpenseTotal expenseTotal = new ExpenseTotal(type,content,memberId,number,money,originalPrice,payType,relatedId);
			expenseTotalManagerDao.save(expenseTotal);
			ActivityExchangeRecord record = new ActivityExchangeRecord();
			record.setChangeTimes(new Date());
			record.setChangeType("3");
			record.setMemberId(member.getId());
			record.setOpenId(openId);
			record.setRelatedId(videoId);
			activityExchangeRecordManagerDao.addExchangeRecord(record);
		}
		if(openId != null) {
			//发送微信通知
			String url = apiDomain + "course/receive";
			JSONObject param = new JSONObject();
			param.put("openId", openId);
			param.put("courseId", videoId);
			GetJsonData.getJsonData(param, url);
		}
		object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.SUCCESS);
		return "success";
	}

	/**
	 * 把memberId与openId、unionId关联
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public Object freeOrder2() throws UnsupportedEncodingException {
		//用于扫码注册的跨域请求
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("textml;charset=UTF-8");
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		response.setHeader("Access-Control-Max-Age", "0");
		response.setHeader("Access-Control-Allow-Headers",
						"Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		HttpSession session1 = request.getSession();
		Member member = null;
		if (!mobile.matches("\\d{11}")) {
			object = new ResponseBean(Constants.FAIL_CODE_400, "手机号码不正确");
			return "success";
		}
		// 短信验证码校验
		if (!memberManagerDao.verifyMobileCode(business, mobile, mobileCode, session1)) {
			object = new ResponseBean(Constants.FAIL_CODE_400, "验证码错误");
			return "success";
		}
		//先查看该手机号码是否已注册，如果未注册就给他注册
		if(mobile != null) {
			member = memberManagerDao.findMemberByMobile(mobile);
			if(member == null) {//如果未注册便为他注册
				member = new Member(mobile, "1");
				member.setOpenId(openId);
				member.setRegistSource(RegistSourceEnum.ACTIVITY.getType());
				memberManagerDao.addAppMember(member, request);
				member = memberManagerDao.findMemberByMobile(mobile);
			}else {
				member.setOpenId(openId);
				memberManagerDao.updateMember(member);
			}
			
			MemberMp memberMp = memberManagerDao.findMemberMpByMemberId(member.getId());
//			MemberMp memberMp = memberManagerDao.findMemberMpByOpenId(openId);
//			MemberMp memberMp = memberManagerDao.findMemberMpByOpenIdAndMemberId(member.getId(),openId);
			if(memberMp == null) {
				memberMp = new MemberMp();
				memberMp.setMemberId(member.getId());
				memberMp.setOpenId(openId);
				memberMp.setUnionId(unionId);
				memberManagerDao.saveMemberMp(memberMp);
			}else {
				object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_MOBILE_USED);
				return "success";
			}
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS);
			return "success";
		}
		return null;
	}
	
	/**
	 * 给参加某个课程（直播/录播/回放）的观众赠送指定卡券
	 * 
	 * @return videoId coupnId
	 */
	public String giveCoupnForViewer() {
		if (coupnIds.length > 0 && relateId != null) {
			LiveVideo video = liveService.getLiveVideoById(relateId);
			if (video != null) {
//				List<ViewerBetweenLive> viewers = liveService.watchRecord(videoId);//赠送给只要点开某个视频
				List<ExpenseTotal> records = expenseTotalManagerDao.getBuysRecordByTypeAndRelatedId(type,relateId);
				if (records != null && records.size()>0) {
					List<Coupn> coupns = memberManagerDao.getCoupnById(coupnIds);
//					List<String> mobileList = new ArrayList<>();
//					List<Long> memberIds = new ArrayList<>();
					for (ExpenseTotal expenseTotal : records) {
//						memberIds.add(buysRecordNew.getMemberId().getId());
						for (Coupn coupn : coupns) {
							CoupnUser coupnUser = new CoupnUser();
							coupnUser.setCoupnId(coupn);
							coupnUser.setMemberId(expenseTotal.getMemberId());
							coupnUser.setSharer("");
							coupnUser.setUsed("0");
							memberManagerDao.save(coupnUser);
						}
					}
					/*List<Member> members = memberManagerDao.getMembersByMemberIds(memberIds);
					for (Member member : members) {
						mobileList.add(member.getMobile());
						SmsUtil.sendMessage(member.getMobile(), "直播课程《热电偶法温升试验与能力验证统计方法》福利资料全额卡券已送达您的账户，请至“我的”-“卡券”查看哟！");
					}
					MiPushHelper.sendAndroidUserAccountList("福利赠送通知", "直播课程《热电偶法温升试验与能力验证统计方法》福利资料全额卡券已送达您的账户，请至“我的”-“卡券”查看哟！", "", mobileList);
					MiPushHelper.sendIOSUserAccountList("直播课程《热电偶法温升试验与能力验证统计方法》福利资料全额卡券已送达您的账户，请至“我的”-“卡券”查看哟！", "", mobileList);*/
					object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.SUCCESS);
				}
			} else {
				object = new ResponseBean(Constants.FAIL_CODE_400, Constants.MSG_DATA_EXCEPT);
			}
		} else {
			object = new ResponseBean(Constants.FAIL_CODE_400, Constants.MSG_PARAMETER_EMPTY);
		}
		return "success";
	}

	/**
	 * 检查是否领取过这个课程
	 * @return
	 */
	public String hadExchange() {
		//用于扫码注册的跨域请求
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("textml;charset=UTF-8");
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		response.setHeader("Access-Control-Max-Age", "0");
		response.setHeader("Access-Control-Allow-Headers",
						"Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		HadExchangeVo vo = new HadExchangeVo();
		ActivityExchangeRecord record = activityExchangeRecordManagerDao.findRecordByOpenId(openId,videoId);
		if(record != null) {
			vo.setHadChanged(true);
		}
		
		//首先查看这个视频是否是免单课程
		FreeVideo freeVideo = liveService.getFreeVideo(videoId);
		if(freeVideo == null) {
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_COMMODITY_NOT_FREE);
			return "success";
		}
		//然后根据用户的openId查看该用户的推荐次数
		Long recommendTimes = 0L;
		recommendTimes = liveService.getWechatRecommendTimes(openId,videoId);
		if (recommendTimes >= freeVideo.getTargetNum()) {
			vo.setFinishTask(true);
		}
		object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, vo);
		return "success";
	}
	
	/**
	 * 发送短信: 用户扫描课程分销员的二维码，分销员获取可兑现积分
	 */
	public String gainIntegralByScan() {
		int code=activityExchangeRecordManagerDao.gainIntegralByScan(videoId,memberId,wechatName);
		if(code==200) {
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, "OK");
		}else if(code==400) {
			object = new ResponseBean(Constants.FAIL_CODE_400, "此视频或此用户不具有分销权限");
		}else if(code==401) {
			object = new ResponseBean(Constants.FAIL_CODE_400, "不存在此分销员");
		}else if(code==402) {
			object = new ResponseBean(Constants.FAIL_CODE_400, "此分销员的手机号不完整");
		}
		return "success";
	}
	
	/**
	 * 发送短信: 用户购买课程分销员推销的课程，分销员获取可兑现积分
	 */
	public String gainIntegralByPaid() {
		int code=activityExchangeRecordManagerDao.gainIntegralByPaid(videoId,memberId,wechatName,money,integral);
		if(code==200) {
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, "OK");
		}else if(code==400) {
			object = new ResponseBean(Constants.FAIL_CODE_400, "此视频或此用户不具有分销权限");
		}else if(code==401) {
			object = new ResponseBean(Constants.FAIL_CODE_400, "不存在此分销员");
		}else if(code==402) {
			object = new ResponseBean(Constants.FAIL_CODE_400, "此分销员的手机号不完整");
		}
		return "success";
	}
	
	public String changes() {

		return "";
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

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public String getChangeType() {
		return changeType;
	}

	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getMobileCode() {
		return mobileCode;
	}

	public void setMobileCode(String mobileCode) {
		this.mobileCode = mobileCode;
	}

	public String getBusiness() {
		return business;
	}

	public void setBusiness(String business) {
		this.business = business;
	}

	public int getInApp() {
		return inApp;
	}

	public void setInApp(int inApp) {
		this.inApp = inApp;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		this.session = request.getSession();
	}

	public Long getVideoId() {
		return videoId;
	}

	public void setVideoId(Long videoId) {
		this.videoId = videoId;
	}

	public Long[] getCoupnIds() {
		return coupnIds;
	}

	public void setCoupnIds(Long[] coupnIds) {
		this.coupnIds = coupnIds;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getRelateId() {
		return relateId;
	}

	public void setRelateId(Long relateId) {
		this.relateId = relateId;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getWechatPhoto() {
		return wechatPhoto;
	}

	public void setWechatPhoto(String wechatPhoto) {
		this.wechatPhoto = wechatPhoto;
	}

	public String getWechatName() {
		return wechatName;
	}

	public void setWechatName(String wechatName) {
		this.wechatName = wechatName;
	}

	public String getUnionId() {
		return unionId;
	}

	public void setUnionId(String unionId) {
		this.unionId = unionId;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public double getIntegral() {
		return integral;
	}

	public void setIntegral(double integral) {
		this.integral = integral;
	}
	
}
