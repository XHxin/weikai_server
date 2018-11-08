package cc.messcat.web.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.jfree.util.Log;
import org.json.JSONObject;
import cc.messcat.bases.BaseAction;
import cc.messcat.entity.RedeemCodeOwner;
import cc.messcat.vo.ResponseBean;
import cc.modules.constants.Constants;
import cc.modules.util.GetJsonData;
import cc.modules.util.SmsUtil;

/**
 * @author leo
 * 搞活动时给用户发送特定的短信
 * 
 */
@SuppressWarnings({ "deprecation", "serial" })
public class SendMessageAction extends BaseAction{

	private static String URL="http://api.cert-map.com/redeemCode";
    private static final Integer ONE = 1;
    private Long videoId;
    private String title;
    private String time;
    private Object object;
    

    /**
     * 20180302  用户通过微信上的兑换码,兑换一张卡券,并发送短信
     */
    public String sendCodeMessage() {
        Map<String, Integer> map = new HashMap<String, Integer>();
        int i=0;
		JSONObject jsonParam = new JSONObject();
		jsonParam.put("from", 2); 
		jsonParam.put("redeemCodeId", 2); 
        /* 读取数据 */
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("C:/Users/leo/Desktop/送视频卡券.txt")),
                                                                         "UTF-8"));
            String lineTxt = null;
            while ((lineTxt = br.readLine()) != null) {
                String[] names = lineTxt.split(",");
                for (String mobile : names) {
                    if (map.keySet().contains(mobile)) {
                        map.put(mobile, (map.get(mobile) + ONE));
                        i+=1;
//                        System.out.println(mobile+"     "+i);
                        Log.error(mobile+"     "+i);
                    } else {
                        map.put(mobile, ONE);
                        i+=1;
//                        System.out.println(mobile+"     "+i);
                    }
                    System.out.println(mobile);
                    jsonParam.put("owner", mobile); 
                    GetJsonData.getJsonData(jsonParam, URL);
            		post(jsonParam);
                    RedeemCodeOwner own=liveService.getOwnCode(mobile);
                    if(own!=null) {
                    	SmsUtil.sendMessage(mobile,"感谢您填写“产品竞品分析工具使用情况调查”问卷！您已成功获得30元视频课程通用卡券，有效期至2018年3月31日，请尽快使用！卡券兑换码："+own.getRedeemCode()+"，兑换方式：请至世界认证地图APP—我的—兑换码进行兑换使用。");
                    }
                }
            }
            br.close();
        } catch (Exception e) {
            System.err.println("read errors :" + e);
        }
        object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, "");
        return "success";
    }

    
    /**
     * 发送Json格式的数据
     */
    @SuppressWarnings("resource")
	public static String post(JSONObject json) {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(URL);

		post.setHeader("Content-Type", "application/json");
		post.addHeader("Authorization", "Basic YWRtaW46");
		String result = "";

		try {

			StringEntity s = new StringEntity(json.toString(), "utf-8");
			s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			post.setEntity(s);

			// 发送请求
			HttpResponse httpResponse = client.execute(post);

			// 获取响应输入流
			InputStream inStream = httpResponse.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "utf-8"));
			StringBuilder strber = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null)
				strber.append(line + "\n");
			inStream.close();

			result = strber.toString();
			System.out.println(result);

			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

				System.out.println("请求服务器成功，做相应处理");

			} else {

				System.out.println("请求服务端失败");

			}

		} catch (Exception e) {
			System.out.println("请求异常");
			throw new RuntimeException(e);
		}

		return result;
	}
    

	/**
	 * 专用于直播课程的短信提醒,  区分升级的用户和没升级的用户
	 */
	public String sendLiveCourseMessage() {
		/*
		 * 查找出不是最新App版本的用户手机号列表
		 */
		List<String> notIosNewestVersion=liveService.getIosNotNewestVersion();
//		System.out.println(notIosNewestVersion.size());
		for(String str1:notIosNewestVersion) {
			SmsUtil.sendMessage(str1, "App课程《"+title+"》将于"+time+"准时开播，为了更好地课程体验，小伙伴们及时更新APP哟!");
		}
		List<String> notAndroidNewestVersion=liveService.getAndroidNotNewestVersion();
//		System.out.println(notAndroidNewestVersion.size());
		for(String str2:notAndroidNewestVersion) {
			SmsUtil.sendMessage(str2, "App课程《"+title+"》将于"+time+"准时开播，为了更好地课程体验，小伙伴们及时更新APP哟!");
		}
		/*
		 * 查找出最新App版本的用户手机号列表
		 */
		List<String> iosNewestVersion=liveService.getIosNewestVersion();
//		System.out.println(iosNewestVersion.size());
		for(String str3:iosNewestVersion) {
			SmsUtil.sendMessage(str3, "App课程《"+title+"》将于"+time+"准时开播，欢迎围观！");
		}
		List<String> andRoidNewestVersion=liveService.getAndroidNewestVersion();
//		System.out.println(andRoidNewestVersion.size());
		for(String str4:andRoidNewestVersion) {
			SmsUtil.sendMessage(str4, "App课程《"+title+"》将于"+time+"准时开播，欢迎围观！");
		}
		object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, "");
		return "success";
	}
	/**
	 *  发送卡券到期提醒的短信
	 */
    public String sendCoupnMessage() {
        Map<String, Integer> map = new HashMap<String, Integer>();
        int i=0;
        /* 读取数据 */
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("C:/Users/leo/Desktop/新建文本文档3.txt")),
                                                                         "UTF-8"));
            String lineTxt = null;
            while ((lineTxt = br.readLine()) != null) {
                String[] names = lineTxt.split(",");
                for (String mobile : names) {
                    if (map.keySet().contains(mobile)) {
                        map.put(mobile, (map.get(mobile) + ONE));
                        i+=1;
                        Log.error(mobile+"     "+i);
                    } else {
                        map.put(mobile, ONE);
                        i+=1;
                    }
                    SmsUtil.sendMessage(mobile,"通知：您有新的卡券可使用，有效期至2018-05-01，请查收！");  //2018-05-01
                }
            }
            br.close();
        } catch (Exception e) {
            System.err.println("read errors :" + e);
        }
        object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, "");
        return "success";
    }
    
    
    /**
     *  给还未使用兑换码  兑换卡券 的人发送短信提醒
     */
	public String sendCodeToCoupn(){
		liveService.sendCodeToCoupn();
		object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, "短信提醒成功");	
		return "success";
	}
	
	/**
	 *  给未满足"邀请三人"免费听取活动资格的用户发送短信
	 */
	public String sendInviteActivity() {
		liveService.sendInviteActivity(videoId,title,time);
		object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, "短信提醒成功");
		return "success";
	}
	
	/**
	 *  通知用户,卡券的有效期快到了,赶快去消费
	 */
	public String sendUseCoupn() {
		liveService.sendUseCoupn();
		object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, "短信提醒成功");
		return "success";
	}
	
	
	
	public Object getObject() {
		return object;
	}
	public void setObject(Object object) {
		this.object = object;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public Long getVideoId() {
		return videoId;
	}
	public void setVideoId(Long videoId) {
		this.videoId = videoId;
	}
	
}
