package cc.messcat.wechat.weixin.popular.api;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.struts2.ServletActionContext;

import cc.messcat.wechat.weixin.popular.bean.QrcodeTicket;
import cc.messcat.wechat.weixin.popular.client.LocalHttpClient;
import cc.modules.constants.Constants;

/**
 * 二维码API
 * @author LiYi
 *
 */
public class QrcodeAPI extends BaseAPI{


	/**
	 * 创建二维码
	 * @param access_token
	 * @param qrcodeJson json 数据 例如{"expire_seconds": 1800, "action_name": "QR_SCENE", "action_info": {"scene": {"scene_id": 123}}}
	 * @return
	 */
	private static QrcodeTicket qrcodeCreate(String access_token,String qrcodeJson){
		HttpUriRequest httpUriRequest = RequestBuilder.post()
										.setHeader(jsonHeader)
										.setUri(BASE_URI+"/cgi-bin/qrcode/create")
										.addParameter("access_token", access_token)
										.setEntity(new StringEntity(qrcodeJson,Charset.forName("utf-8")))
										.build();
		return LocalHttpClient.executeJsonResult(httpUriRequest,QrcodeTicket.class);
	}

	/**
	 * 创建二维码
	 * @param access_token
	 * @param expire_seconds 	该二维码有效时间，以秒为单位。 最大不超过1800秒。
	 * @param action_name		二维码类型，QR_SCENE为临时,QR_LIMIT_SCENE为永久
	 * @param scene_id			场景值ID，临时二维码时为32位非0整型，永久二维码时最大值为100000（目前参数只支持1--100000）
	 * @return
	 */
	private static QrcodeTicket qrcodeCreate(String access_token,Integer expire_seconds,String action_name,long scene_id){
		return qrcodeCreate(access_token,String.format("{"+(expire_seconds==null?"%1$s":"\"expire_seconds\": %1$s, ")+"\"action_name\": \"%2$s\", \"action_info\": {\"scene\": {\"scene_id\": %3$d}}}",
													expire_seconds==null?"":expire_seconds,action_name,scene_id));
	}

	/**
	 * 创建临时二维码
	 * @param access_token
	 * @param expire_seconds 不超过1800秒
	 * @param scene_id		  场景值ID，32位非0整型
	 * @return
	 */
	public static QrcodeTicket qrcodeCreateTemp(String access_token,int expire_seconds,long scene_id){
		return qrcodeCreate(access_token,expire_seconds,"QR_SCENE",scene_id);
	}

	/**
	 * 创建持久二维码
	 * @param access_token
	 * @param scene_id	场景值ID 1-100000
	 * @return
	 */
	public static QrcodeTicket qrcodeCreateFinal(String access_token,int scene_id){
		return qrcodeCreate(access_token,null,"QR_LIMIT_SCENE",scene_id);
	}

	/**
	 * 下载二维码
	 * 视频文件不支持下载
	 * @param ticket  内部自动 UrlEncode
	 * @return
	 */
	public static byte[] showqrcode(String ticket){
		HttpUriRequest httpUriRequest = RequestBuilder.post()
				.setUri(QRCODE_DOWNLOAD_URI + "/cgi-bin/showqrcode")
				.addParameter("ticket", ticket)
				.build();
		HttpResponse httpResponse = LocalHttpClient.execute(httpUriRequest);
		try {
			return EntityUtils.toByteArray(httpResponse.getEntity());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
//	public static void main(String[] args) {
//		Token token = TokenAPI.token(Constants.APPID, Constants.APPSECRET);
//		QrcodeTicket qrcodeTicket = QrcodeAPI.qrcodeCreateTemp(token.getAccess_token(), 3600, 1);
//		log.info(qrcodeTicket.getTicket());
//		log.info(qrcodeTicket.getExpire_seconds());
//		log.info(qrcodeTicket.getUrl());
//		log.info(QrcodeAPI.showqrcode(qrcodeTicket.getTicket()));
//	}
	
	
	
	 
	 
	
	    public static void main(String[] args) throws IOException {  
	    	HttpServletRequest request = ServletActionContext.getRequest();
			String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/upload/enterprice/member_qrcode/";
//	        File file1 = new File(Constants.QR_TEMP_PATH, "1.jpg");  
	        File file1 = new File(basePath, "1.jpg");  
	        URL file2 = new URL("https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=gQE78ToAAAAAAAAAASxodHRwOi8vd2VpeGluLnFxLmNvbS9xLzlVZ3d1WERsV21JalI2LUxGMlpPAAIEBaC4VQMEEA4AAA==");  
	        URL url = new URL("http://wx.qlogo.cn/mmopen/PfXicPJJjOHk7BeNv7jQrtibqXSiczrR1nv7WfBEGa7rCA5G20iaj1cY2HvS29xvfE0LhicKdfyBnBvo7OSG728HTHZc5ibfKQbnrT/0");
	        mergeImage(file1, file2, url, "小亮");  
	    }  
	    
	    /**
	     * @param background
	     * 					背景图片
	     * @param wxQRImg
	     * 					微信二维码图片
	     * @param wxImg
	     * 					微信头像
	     * @param text
	     * 					微信昵称
	     * @return
	     * @throws IOException
	     */
	    public static String mergeImage(File background, URL wxQRImg, URL wxImg, String text) throws IOException {   
	    	String qrImgName = ""; 
	        BufferedImage image1 = ImageIO.read(background);  
	        BufferedImage image2 = ImageIO.read(wxQRImg);  
	        BufferedImage image3 = ImageIO.read(wxImg);  
	  
	        BufferedImage combined = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);  
	  
	        // paint both images, preserving the alpha channels  
	        Graphics g = combined.getGraphics();  
	        g.drawImage(image1, Constants.BACKGROUND_IMG_X, Constants.BACKGROUND_IMG_Y, null);  
	        g.drawImage(image2, Constants.WX_QR_IMG_X, Constants.WX_QR_IMG_Y, Constants.WX_QR_IMG_WIDTH, Constants.WX_QR_IMG_HEIGHT, null);
//	        g.drawImage(image3, Constants.WX_IMG_X, Constants.WX_IMG_Y, Constants.WX_IMG_WIDTH, Constants.WX_IMG_HEIGHT, null);
	        //设置颜色。
//	        Color color = new Color(255,255,255);
//	        g.setColor(color);
//	        //最后一个参数用来设置字体的大小
//	        Font f = new Font("宋体",Font.BOLD,25);
//	        g.setFont(f);
//	        //10,20 表示这段文字在图片上的位置(x,y) .第一个是你设置的内容。
//	        g.drawString(text,Constants.WX_NICK_NAME_X,Constants.WX_NICK_NAME_Y);
	        g.dispose();
	        qrImgName = UUID.randomUUID().toString().replace("-", "");
	        qrImgName+=".jpg";
	        // Save as new image  
	        
	        ImageIO.write(combined, "JPG", new File(Constants.QR_TEMP_PATH, qrImgName));
	        
	        return qrImgName;
	    }  

}
