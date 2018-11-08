package cc.modules.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vdurmont.emoji.EmojiManager;
import com.vdurmont.emoji.EmojiParser;


/**
 * 手机短信接口util
 * @author Jesse
 * 2014-07-23
 */
public class SmsUtil {
	private static Logger log = LoggerFactory.getLogger(SmsUtil.class); 

	/**
	 * 固定地址串，后面方法名需要传参数
	 */
	private static final String  address = "http://120.55.205.5/webservice/sms.php?method=";
	
	/**
	 * 用户名
	 */
	private static final String account = "C63740997"; //账号
	//private static final String account = "hy_xunmao";
	
	/**
	 * 密码
	 */
	private static final String password = "8a9fb5a5b88e5ff16527f6a61f680419";//密码
	//private static final String password = "xunmao123";
	
	private static final String pid = "21";//PID
	
	
	
	public static StringBuffer sendMessage(String mobile,String content){
	
		String result="";
		HttpClient client = new HttpClient(); 
		PostMethod method = new PostMethod(address+"Submit");
		client.getParams().setContentCharset("UTF-8");
		method.setRequestHeader("ContentType","application/x-www-form-urlencoded;charset=UTF-8");
		
		//多个手机号码请用英文,号隔开		
		
		NameValuePair[] data = {//提交短信
			    new NameValuePair("account", account), 
			    new NameValuePair("password", password), 			    
			    new NameValuePair("mobile", mobile),
				new NameValuePair("content", content),
		};
		
		method.setRequestBody(data);
		
		try {
			client.executeMethod(method);	
			result = method.getResponseBodyAsString();
			
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
		return new StringBuffer(result);
	}
	
	
	
	 /**
	  * 获取剩余可发信息数量
	  * @return
	  */
	public static String getSurplusMessageNum() {
		String result="";
		HttpClient client = new HttpClient(); 
		PostMethod method = new PostMethod(address+"GetNum");
		client.getParams().setContentCharset("UTF-8");
		method.setRequestHeader("ContentType","application/x-www-form-urlencoded;charset=UTF-8");
		
		//多个手机号码请用英文,号隔开		
		
		NameValuePair[] data = {//提交短信
			    new NameValuePair("account", account), 
			    new NameValuePair("password", password),
			    //new NameValuePair("pid", pid),
		};
		
		method.setRequestBody(data);
		
		try {
			client.executeMethod(method);	
			result = method.getResponseBodyAsString();
			
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		return result;
	}
	
	/**
	 * 短信接口处理重复代码
	 * @param addressReal
	 * @param paramString
	 * @return
	 */
	private static Map<String,Object> connectDoSomething(StringBuffer addressReal,StringBuffer paramString) {
		BufferedReader in = null;
		URL url;
		byte[] xmlData = paramString.toString().getBytes();
		Map<String,Object> result = new HashMap<String, Object>();
		try {
			url = new URL(addressReal.toString());
			HttpURLConnection urlConn = (HttpURLConnection)url.openConnection();
			urlConn.setDoOutput(true);
			urlConn.setRequestMethod("POST");
			urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8"); 
			urlConn.setRequestProperty("Content-length",String.valueOf(xmlData.length)); 
			DataOutputStream printout = new DataOutputStream(urlConn.getOutputStream()); 
			printout.write(xmlData);
			printout.flush();
			in = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));  
			StringBuffer sb2 = new StringBuffer();  
			String lines = "";  
			while(null!=(lines = in.readLine()))  {  
				sb2.append(lines);  
			}  
			//获取返回结果
	//		log.info(sb2.toString());  
			//获取真正可处理结果  code--msg -- smsid
			result = XmlUtil.parseStringXmlToMap(sb2);
			//遍历
			Iterator it = result.keySet().iterator();
			while (it.hasNext()) {
				Object obj = result.get(it.next());
	//			log.info(obj);
			}
			// in.read(b)  
			in.close();
			printout.close();  
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	public static void main(String[] args) {
//		System.out.println(sendMessage("13677336042","您的验证码是：□□。请不要把验证码泄露给其他人。"));
		String str="/([0-9#][\\x{20E3}])|[\\x{00ae}\\x{00a9}\\x{203C}\\x{2047}\\x{2048}\\x{2049}\\x{3030}\\x{303D}\\x{2139}\\x{2122}\\x{3297}\\x{3299}][\\x{FE00}-\\x{FEFF}]?|[\\x{2190}-\\x{21FF}][\\x{FE00}-\\x{FEFF}]?|[\\x{2300}-\\x{23FF}][\\x{FE00}-\\x{FEFF}]?|[\\x{2460}-\\x{24FF}][\\x{FE00}-\\x{FEFF}]?|[\\x{25A0}-\\x{25FF}][\\x{FE00}-\\x{FEFF}]?|[\\x{2600}-\\x{27BF}][\\x{FE00}-\\x{FEFF}]?|[\\x{2900}-\\x{297F}][\\x{FE00}-\\x{FEFF}]?|[\\x{2B00}-\\x{2BF0}][\\x{FE00}-\\x{FEFF}]?|[\\x{1F000}-\\x{1F6FF}][\\x{FE00}-\\x{FEFF}]?/u";
		str.matches(str);
//		String s="😏 😏 😏";
		String s="🍋";

		System.out.println(EmojiUtils.filterEmoji(s));
		System.out.println(EmojiUtils.emojiChange(s));
		
		System.out.println(EmojiManager.isEmoji(s));
		//log.info(getSurplusMessageNum());
//		System.out.println(sendMessage("13677336042", "尊敬的专家：您好！飘渺云轩向您提了一个价值😏 😏 😏元的问题，请您在48小时有效期内解答，打开APP，快快回答问题吧！"));
//		System.out.println(sendMessage("15827625767", "尊敬的专家：您好！飘渺云轩向您提了一个价值8.0元的问题，请您在48小时有效期内解答，打开APP，快快回答问题吧！"));  //提醒运营
//		System.out.println(sendMessage("13073008457", "尊敬的专家：您好！飘渺云轩向您提了一个价值8.0元的问题，请您在48小时有效期内解答，打开APP，快快回答问题吧！"));  //提醒杨哥
	}
}
