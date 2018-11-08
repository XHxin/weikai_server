package cc.messcat.service.member;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import com.mipush.MiPushHelper;

import cc.messcat.entity.ActivityExchangeRecord;
import cc.messcat.entity.LiveVideo;
import cc.messcat.entity.LiveVideoDistributor;
import cc.messcat.entity.Member;
import cc.messcat.entity.RelevanceWechat;
import cc.messcat.entity.SystemMessage;
import cc.modules.util.DateHelper;
import cc.modules.util.EmojiUtils;
import cc.modules.util.SmsUtil;

@SuppressWarnings("serial")
public class ActivityExchangeRecordManagerDaoImpl extends BankManagerDaoImpl
		implements ActivityExchangeRecordManagerDao {

	@Override
	public void addExchangeRecord(ActivityExchangeRecord activityExchangeRecord) {
		activityExchangeRecordDao.addExchangeRecord(activityExchangeRecord);
	}

	@Override
	public List<ActivityExchangeRecord> getExchangeCount(String changeType, Long memberId) {

		return activityExchangeRecordDao.getExchangeCount(changeType, memberId);
	}

	@Override
	public List<RelevanceWechat> exchangeCount(String openId, String mobile, int videoId) {
		return activityExchangeRecordDao.exchangeCount(openId,mobile,videoId);
	}

	@Override
	public void exchangeRecord(RelevanceWechat relevance) {
		activityExchangeRecordDao.exchangeRecord(relevance);
	}

	@Override
	public ActivityExchangeRecord findRecordByOpenId(String openId, Long videoId) {
		return activityExchangeRecordDao.findRecordByOpenId(openId,videoId);
	}

	@Override
	public int gainIntegralByScan(Long videoId, Long memberId, String wechatName) {
		LiveVideo video=liveDao.getLiveVideoById(videoId);
		Member member=memberDao.get(memberId);
		LiveVideoDistributor distributor=activityExchangeRecordDao.getLiveVideoDistributor(videoId,memberId);
		if(distributor!=null) {
			BigDecimal totalIntegral=activityExchangeRecordDao.getTotalIntegral(memberId);
			String notifyTitle="【世界认证地图】积分变动提醒：";
			String smsTitle="积分变动提醒：\r\n";
			String notifyMsg="";
			try {
				notifyMsg = "恭喜！有一位新朋友扫码支持您啦！\r\n" + 
						   "姓名："+URLEncoder.encode(wechatName, "utf-8")+"\r\n" + 
						   "时间："+DateHelper.dataToString(new Date(), "yyyy年MM月dd日   HH:mm:ss")+"\r\n" + 
						   "课程：《"+video.getTitle()+"》\r\n" + 
						   "----------------------------------\r\n" + 
						   "*进度* ： 本课程已邀请"+distributor.getBringNum()+"人扫码，"+distributor.getBoughtNum()+"人下单\r\n" + 
						   "*积分奖励* ： 本次将为您赢得10待核积分，若该好友14天内没取关公众号，即可到账。\r\n" + 
						   "*已到账积分* ： "+totalIntegral;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			String smsMsg="恭喜！有一位新朋友扫码支持您啦！\r\n" + 
					      "姓名："+EmojiUtils.filterEmoji(wechatName)+"\r\n" + 
					      "时间："+DateHelper.dataToString(new Date(), "yyyy年MM月dd日   HH:mm:ss")+"\r\n" + 
					      "课程：《"+video.getTitle()+"》\r\n" + 
					      "----------------------------------\r\n" + 
					      "*进度* ： 本课程已邀请"+distributor.getBringNum()+"人扫码，"+distributor.getBoughtNum()+"人下单\r\n" + 
					      "*积分奖励* ： 本次将为您赢得10待核积分，若该好友14天内没取关公众号，即可到账。\r\n" + 
					      "*已到账积分* ： "+totalIntegral;
			if(member!=null) {
				if(!"".equals(member.getMobile()) && member.getMobile()!=null) {
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
					systemMessage.setRemark("");
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
					systemMessage.setTitle(notifyTitle+"\r\n"+notifyMsg);
					systemMessage.setDataUrl(dataUrl);
					SmsUtil.sendMessage(member.getMobile(), smsTitle+smsMsg);
					MiPushHelper.sendAndroidUserAccount(notifyTitle, smsMsg, dataUrl, member.getMobile());
					MiPushHelper.sendIOSUserAccount(notifyTitle+"\r\n"+smsMsg, dataUrl, member.getMobile());
					systemMessageDao.save(systemMessage);
				}else {
					return 402;  //此分销员手机号不存在
				}
			}else {
				return 401; //此用户不存在
			}
		}else {
			return 400; //此视频或此用户不具有分销权限
		}
		return 200;
	}

	@Override
	public int gainIntegralByPaid(Long videoId, Long memberId, String wechatName,double money,double integral) {
		LiveVideo video=liveDao.getLiveVideoById(videoId);
		Member member=memberDao.get(memberId);
		LiveVideoDistributor distributor=activityExchangeRecordDao.getLiveVideoDistributor(videoId,memberId);
		if(distributor!=null) {
			BigDecimal totalIntegral=activityExchangeRecordDao.getTotalIntegral(memberId);
			String notifyTitle="【世界认证地图】积分变动提醒：";
			String smsTitle="积分变动提醒：\r\n";
			String notifyMsg="";
			try {
				notifyMsg = "积分变动提醒：\r\n" + 
						   "恭喜！有一位新朋友下单支持您啦！\r\n" + 
						   "姓名："+URLEncoder.encode(wechatName, "utf-8")+"\r\n" + 
						   "时间："+DateHelper.dataToString(new Date(), "yyyy年MM月dd日   HH:mm:ss")+"\r\n" + 
						   "订单：《"+video.getTitle()+"》\r\n" + 
						   "金额：￥"+money+"元\r\n" + 
						   "----------------------------------\r\n" + 
						   "【进度】本课程已邀请"+distributor.getBringNum()+"人扫码，"+distributor.getBoughtNum()+"人下单\r\n" + 
						   "【积分奖励】本次为您赢得"+integral+"积分，即时到账。\r\n" + 
						   "【已到账积分】 "+totalIntegral;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			String smsMsg="恭喜！有一位新朋友下单支持您啦！\r\n" + 
					      "姓名："+EmojiUtils.filterEmoji(wechatName)+"\r\n" +  
					      "时间："+DateHelper.dataToString(new Date(), "yyyy年MM月dd日   HH:mm:ss")+"\r\n" + 
					      "订单：《"+video.getTitle()+"》\r\n" + 
					      "金额：￥"+money+"元\r\n" + 
					      "----------------------------------\r\n" + 
					      "*进度* ： 本课程已邀请"+distributor.getBringNum()+"人扫码，"+distributor.getBoughtNum()+"人下单\r\n" + 
					      "*积分奖励* ： 本次为您赢得"+integral+"积分，即时到账。\r\n" + 
					      "*已到账积分* ： "+totalIntegral;
			if(member!=null) {
				if(!"".equals(member.getMobile()) && member.getMobile()!=null) {
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
					systemMessage.setRemark("");
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
					systemMessage.setTitle(notifyTitle+"\r\n"+notifyMsg);
					systemMessage.setDataUrl(dataUrl);
					SmsUtil.sendMessage(member.getMobile(), smsTitle+smsMsg);
					MiPushHelper.sendAndroidUserAccount(notifyTitle, smsMsg, dataUrl, member.getMobile());
					MiPushHelper.sendIOSUserAccount(notifyTitle+"\r\n"+smsMsg, dataUrl, member.getMobile());
					systemMessageDao.save(systemMessage);
				}else {
					return 402;  //此分销员手机号不存在
				}
			}else {
				return 401; //此用户不存在
			}
		}else {
			return 400; //此视频或此用户不具有分销权限
		}
		return 200;
	}

	@Override
	public LiveVideoDistributor getLiveVideoDistributor(Long videoId, Long memberId) {
		return activityExchangeRecordDao.getLiveVideoDistributor(videoId,memberId);
	}

	@Override
	public List<LiveVideoDistributor> getDistributorByMemberId(Long memberId) {
		return activityExchangeRecordDao.getDistributorByMemberId(memberId);
	}

	@Override
	public BigDecimal getTotalIntegral(Long memberId) {
		return activityExchangeRecordDao.getTotalIntegral(memberId);
	}
}
