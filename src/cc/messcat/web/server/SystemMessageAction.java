package cc.messcat.web.server;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import cc.messcat.entity.LiveVideo;
import cc.messcat.entity.Member;
import cc.messcat.entity.StandardReading;
import cc.messcat.entity.SystemMessage;
import cc.messcat.vo.EnterpriseNewsVo;
import cc.messcat.vo.ResponseBean;
import cc.messcat.vo.SystemMessageVo;
import cc.modules.commons.PageAction;
import cc.modules.constants.Constants;
import cc.modules.util.DateHelper;
import cc.modules.util.ObjValid;
import cc.modules.util.PropertiesFileReader;

public class SystemMessageAction extends PageAction {

	/**
	 * 系统消息模块
	 */
	private static final long serialVersionUID = 1L;

	private static String jointUrl = PropertiesFileReader.getByKey("static.domain");// 图片拼接
	private static String webDomain = PropertiesFileReader.getByKey("web.domain");// 项目域名
	private Object object;
	private String accessToken;
	private Long memberId;
	private String content; // 发送的内容
	private Long toUser;  //把消息发送给哪个用户
	private long videoId; //课程Id
	private String wechatName;  //微信昵称

	/**
	 * 列表显示
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String execute() throws Exception {
		List<SystemMessageVo> systemMessages = new ArrayList<>();
		Member member = memberManagerDao.retrieveMember(memberId);
		if (member != null && member.getAccessToken().equals(accessToken)) {
			List<SystemMessage> list = this.systemMessageManagerDao.getByMemberId(pageSize, pageNo, memberId);
			if (list!=null && list.size()>0) {
				for (SystemMessage message : list) {
					SystemMessageVo messageVo = new SystemMessageVo();
					messageVo.setTitle(message.getTitle() == null ? "" :URLDecoder.decode(message.getTitle(), "utf-8") );
					messageVo.setRemark(message.getRemark() == null ? "" : message.getRemark());
					messageVo.setTime(DateHelper.dataToString(message.getEditTime(), "yyyy-MM-dd HH:mm"));
					
					//对cover进行处理
					messageVo.setCover(message.getPhoto() == null || message.getPhoto().isEmpty()? "" : jointUrl + message.getPhoto());
					if("239".contains(message.getType())){
						StandardReading read = standardReadManagerDao.retrieveStandardReading(message.getRelateId().intValue());
						if (ObjValid.isValid(read.getCover()))
							messageVo.setCover(jointUrl + read.getCover());
						
					}else if("5".equals(message.getType())){
						EnterpriseNewsVo newsVo = epNewsManagerDao.getEpNews(message.getRelateId(), memberId);
						if (ObjValid.isValid(newsVo.getPhoto()))
							messageVo.setCover(newsVo.getPhoto());
						
					}else if("7".equals(message.getType())){
						Member member0 = memberManagerDao.retrieveMember(message.getRelateId());
						if (ObjValid.isValid(member0.getPhoto()))
							messageVo.setCover(jointUrl + member0.getPhoto());
						
					}else if("10".equals(message.getType())) {
						LiveVideo video=liveService.getLiveVideoById(message.getRelateId());
						if(video!=null) {
							messageVo.setCover(jointUrl+video.getCover());
						}
					}
					
					//对dataUrl进行处理
					if(StringUtils.isNotBlank(message.getDataUrl())){
						messageVo.setData(message.getDataUrl());
					}else{
						String data=SystemMessageVo.partData;
						switch (message.getType()) {
						
							case "1":// 准入报告weikai://cert-map?target=report&productId=?&regionId=?
								messageVo.setData(data +"report&productId="+ message.getRelateId() + "&regionId="
										+ message.getRegionId());
								break;
								
							case "2":// 标准解读weikai://cert-map?target=standardReading&standardReadingId=?
								messageVo.setData(data +"standardReading&standardReadingId="+ message.getRelateId());
								break;
								
							case "3":// 质量分享 weikai://cert-map?target=standardReading&standardReadingId=?
								messageVo.setData(data +"standardReading&standardReadingId="+ message.getRelateId());
								break;
								
							case "4":// 付费咨询 weikai://cert-map?target=consult&replyId=?
								messageVo.setData(data +"consult&replyId="+ message.getRelateId());
								break;
								
							case "5":// 新闻 weikai://cert-map?target=web&url=?
								messageVo.setData(data +"web&url="+ webDomain + "/epFrontNewsAction!news.action?selectNewsId="
										+ message.getRelateId());
								break;
								
							case "6":// 电商报告 weikai://cert-map?target=ebReport&reportId=?
								messageVo.setData(data +"ebReport&reportId="+ message.getRelateId());
								break;
								
							case "7":// 专家 weikai://cert-map?target=expert&expertId=?
								messageVo.setData(data +"expert&expertId="+ message.getRelateId());
								break;
							case "8":// 购买信息 weikai://cert-map?target=bought
								messageVo.setData(data+"bought");
								break;
							case "9":// 订阅专栏 weikai://cert-map?target=standardReading&standardReadingId=?
								messageVo.setData(data +"standardReading&standardReadingId="+ message.getRelateId());
								break;
							case "10":// 课程 weikai://cert-map?target=course&id={课程id}&type={类型 1：单一视频 2：系列视频}
								      // 直播 weikai://cert-map?target=live&id={直播id}&type={类型 1：单一视频 2：系列视频}
								String type="";
								LiveVideo video=liveService.getLiveVideoById(message.getRelateId());
								if(video!=null) {
									if(video.getClassify().equals("1")) {
										type="&type=2";
									}else if(video.getClassify().equals("4")){
										type="&type=1";
									}
									if(video.getVideoType().equals("0")) {
										messageVo.setData(data +"course&id="+ message.getRelateId()+type);	
									}else {
										messageVo.setData(data +"live&id="+ message.getRelateId()+type);	
									}
								}
								break;
							case "11":// 卡券weikai://cert-map?target=card
								messageVo.setData(data +"card");
								break;
							case "12":// 会员weikai://cert-map?target=vip
								messageVo.setData(data +"vip");
								break;
							default:
								messageVo.setData("");
						}
						message.setDataUrl(messageVo.getData());
						systemMessageManagerDao.updateSystemMessage(message);
					}
					
					systemMessages.add(messageVo);
				}
			}
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, systemMessages);
		}else{
			object = new ResponseBean(Constants.FAIL_CODE_400, "登录token错误","");
		}
		return "success";
	}

	/**
	 * 发送消息
	 */
	public String sendSysNotify() {
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
		systemMessageManagerDao.sendSysNotify(content,member,toUser);
		object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, "OK");
		return "success";
	}
	
	/**
	 * 发送特价短信 和 推送
	 */
	public String sendDiscountMsg() {
		int code=liveService.sendDiscountMsg(videoId,memberId,wechatName);
		if(code==200) {
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, "OK");
		}else if(code==400){
			object = new ResponseBean("400", "该用户已经可以打折购买了，请不要再请求此接口了");
		}
		return "success";
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Long getToUser() {
		return toUser;
	}
	public void setToUser(Long toUser) {
		this.toUser = toUser;
	}

	public long getVideoId() {
		return videoId;
	}

	public void setVideoId(long videoId) {
		this.videoId = videoId;
	}

	public String getWechatName() {
		return wechatName;
	}

	public void setWechatName(String wechatName) {
		this.wechatName = wechatName;
	}

}