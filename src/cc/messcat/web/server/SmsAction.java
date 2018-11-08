package cc.messcat.web.server;

import java.io.IOException;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.interceptor.ServletRequestAware;
import cc.messcat.bases.BaseAction;
import cc.messcat.entity.SmsReply;
import cc.messcat.entity.SmsReport;
import cc.messcat.vo.ResponseBean;
import cc.modules.constants.Constants;
import cc.modules.util.DateHelper;
import cc.modules.util.EmojiUtils;
import cc.modules.util.SmsUtil;

/**
 * 专用于收集互亿无线短信发送的回调信息接收
 * @author leo
 *
 */
@SuppressWarnings("serial")
public class SmsAction extends BaseAction  implements ServletRequestAware {
	
	private HttpServletRequest request;
	private Object object;
	private Integer code;
	private String msg;
	private String mobilephone;
	private String smsid;
	private String content;
	private Date report_time;
	private Date replyTime;
	private String wechatName;
	
	
	
	/**
	 *  互亿无线的回执推送
	 * @throws IOException
	 */
	public void reportData() throws IOException {
//		String str=getReportData();
//		JSON json=(JSON) JSON.parse(str);
//		SmsReport report=JSON.toJavaObject(json, SmsReport.class);
		SmsReport report=new SmsReport();
		report.setCode(code);
		report.setMobilephone(mobilephone);
		report.setMsg(msg);
		report.setReportTime(report_time);
		report.setSmsid(smsid);
		int exist=smsService.isExistSmsReport(smsid);
		if(exist==0) {
			smsService.saveSmsReport(report);
		}
	}
	
	/**
	 * 互亿无线的上行回复推送
	 * @throws IOException
	 */
	public void replyData() throws IOException {
//		String str=getReportData();
//		JSON json=(JSON) JSON.parse(str);
//		SmsReply reply=JSON.toJavaObject(json, SmsReply.class);
		SmsReply reply=new SmsReply();
		reply.setContent(content);
		reply.setMobilephone(mobilephone);
		reply.setReplyTime(replyTime);
		reply.setSmsid(smsid);
		int exist=smsService.isExistSmsReply(smsid);
		if(exist==0) {
			smsService.saveSmsReply(reply);
		}
	}
	
	//解析请求的Json数据
	public String getReportData() throws IOException {
        int contentLength = request.getContentLength();
        if(contentLength<0){
            return null;
        }
        byte buffer[] = new byte[contentLength];
        for (int i = 0; i < contentLength;) {
            int len = request.getInputStream().read(buffer, i, contentLength - i);
           if (len == -1) {
               break;
           }
           i += len;
       }
       return new String(buffer, "utf-8");
   }

	public String sendEmoji() {
		String smsTitle="成员加入提醒：\r\n";
		String nowTime=DateHelper.dataToString(new Date(), "yyyy年MM月dd日  HH:mm:ss");
		String name="恭喜！有一位新朋友扫码支持您啦！\r\n" + 
				"姓名："+EmojiUtils.filterEmoji(wechatName)+"\r\n" + 
				"时间： "+nowTime+"\r\n" + 
				"已完成"+1+"人，"+3+"位小伙伴支持，即可￥"+0.1+"元购买《PSE认证》 ！\r\n" + 
				"[活动有效期]截止"+nowTime;
		System.out.println(EmojiUtils.filterEmoji(wechatName));
//	    System.out.println(SmsUtil.sendMessage("18819218215","您的验证码是："+name+"。请不要把验证码泄露给其他人。"));
	    System.out.println(SmsUtil.sendMessage("13677336042",smsTitle+name));
	    object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS);
		return "success";
	}
	

	public String getMsg() {
		return msg;
	}
	public void setMsg(String type) {
		this.msg = type;
	}
	public String getMobilephone() {
		return mobilephone;
	}
	public void setMobilephone(String message) {
		this.mobilephone = message;
	}
	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getSmsid() {
		return smsid;
	}

	public void setSmsid(String smsid) {
		this.smsid = smsid;
	}

	public Date getReport_time() {
		return report_time;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getReplyTime() {
		return replyTime;
	}

	public void setReplyTime(Date replyTime) {
		this.replyTime = replyTime;
	}

	public void setReport_time(Date reportTime) {
		this.report_time = reportTime;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
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

	public String getWechatName() {
		return wechatName;
	}

	public void setWechatName(String wechatName) {
		this.wechatName = wechatName;
	}
}
