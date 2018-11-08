package cc.messcat.web.server;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.lang.time.DateUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;
import com.wxpay.http.MyX509TrustManager;
import cc.messcat.bases.BaseAction;
import cc.messcat.entity.Fusing;
import cc.messcat.entity.WechatWeiboAttention;
import cc.messcat.vo.LineChartVo;
import cc.messcat.vo.PieVo;
import cc.messcat.vo.TargetPieVo;
import cc.messcat.vo.WeiBoCountVo;
import cc.messcat.vo.WeixinCountVo;
import cc.messcat.vo.WeixinTokenVo;
import cc.modules.util.GetJsonData;

/**
 * @author HASEE 统计微博和微信的粉丝数
 */
public class FensCountAction extends BaseAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<PieVo> pieVos;
	private List<LineChartVo> lineChartVos;
	private List<TargetPieVo> targetPieVos;
	private String type;

	private static Logger log = LoggerFactory.getLogger(FensCountAction.class);

	public String fensCount() {
		pieVos = new ArrayList<>();
		targetPieVos = new ArrayList<>();
		lineChartVos = new ArrayList<>();

		PieVo wechatPieVo = new PieVo();
		PieVo weiboPieVo = new PieVo();
		TargetPieVo targetPieVo = new TargetPieVo();

		Calendar calendar = Calendar.getInstance();
		int wechat = 0;
		int weibo = 0;
		int sum = 0;
		// 做控制，每天只调一次微博的接口，因为折线图的数据每天只需要两条，另外微博的接口不允许频繁调用
		Date current = new Date();
		Fusing fusing = memberManagerDao.fusingTimes("FensCountAction!fensCount");
		if (fusing == null) {
			calendar.setTime(new Date());
			calendar.add(Calendar.DATE, -1);// 以为微信获取粉丝数接口不能获取当天的数据，否则会报错
			wechat = wechatAttNumber(calendar.getTime());
			weibo = weiboAttNumber();
			sum = wechat + weibo;

			// 查出来后存到数据库了，折线图要用
			WechatWeiboAttention wechatAttention = new WechatWeiboAttention();
			WechatWeiboAttention weiboAttention = new WechatWeiboAttention();
			wechatAttention.setRealDate(calendar.getTime());
			wechatAttention.setTypeNum(1);
			wechatAttention.setValue(wechat);
			weiboAttention.setRealDate(calendar.getTime());
			weiboAttention.setTypeNum(2);
			weiboAttention.setValue(weibo);
			memberManagerDao.save(wechatAttention);
			memberManagerDao.save(weiboAttention);

			Fusing fensFusing = new Fusing();
			fensFusing.setCallDate(current);
			fensFusing.setIntefaceName("FensCountAction!fensCount");
			fensFusing.setTimes(0);
			memberManagerDao.save(fensFusing);

		} else {
			if(!DateUtils.isSameDay(fusing.getCallDate(), current)) {//不是同一天
				calendar.setTime(new Date());
				calendar.add(Calendar.DATE, -1);// 以为微信获取粉丝数接口不能获取当天的数据，否则会报错
				wechat = wechatAttNumber(calendar.getTime());
				weibo = weiboAttNumber();
				sum = wechat + weibo;

				// 查出来后存到数据库了，折线图要用
				WechatWeiboAttention wechatAttention = new WechatWeiboAttention();
				WechatWeiboAttention weiboAttention = new WechatWeiboAttention();
				wechatAttention.setRealDate(calendar.getTime());
				wechatAttention.setTypeNum(1);
				wechatAttention.setValue(wechat);
				weiboAttention.setRealDate(calendar.getTime());
				weiboAttention.setTypeNum(2);
				weiboAttention.setValue(weibo);
				memberManagerDao.save(wechatAttention);
				memberManagerDao.save(weiboAttention);
				
				fusing.setTimes(1);
				fusing.setCallDate(current);
				memberManagerDao.updateFusing(fusing);
			}else {//同一天
				wechat = memberManagerDao.maxAttentionNum(1);
				weibo = memberManagerDao.maxAttentionNum(2);
			}
		}

		wechatPieVo.setType("微信关注数");
		wechatPieVo.setValue(wechat);
		pieVos.add(wechatPieVo);

		targetPieVo.setActual(wechat);
		targetPieVo.setAims(1000000);
		targetPieVos.add(targetPieVo);

		List<WechatWeiboAttention> list = memberManagerDao.retrieveAttention();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (WechatWeiboAttention wechatWeiboAttention : list) {
			LineChartVo chartVo = new LineChartVo();
			String value = Integer.toString(wechatWeiboAttention.getValue());
			Double wwaValue = Double.valueOf(value)/10000;
			chartVo.setValue(Double.valueOf(wwaValue));
			chartVo.setTypeNum(Integer.toString(wechatWeiboAttention.getTypeNum()));
			String date = sdf.format(wechatWeiboAttention.getRealDate());
			chartVo.setReal_date(date);
			lineChartVos.add(chartVo);
		}
		if (type.equals("target")) {
			return "target";
		} else if (type.equals("pie")) {
			return "pie";
		} else if (type.equals("line")) {
			return "line";
		}
		return null;
	}

	/**
	 * @return 获取微信token
	 */
	public String wechatToken() {
		try {
			StringBuffer buffer = new StringBuffer();
			String client_credential = "client_credential";
			String appid = "wx8c070053f0da642e";
			String secret = "ebc2a0634f3c6ce3b4411c489fc7aac0";
			String getTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=" + client_credential + "&appid="
					+ appid + "&secret=" + secret;
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			URL url = new URL(getTokenUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);
			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			httpUrlConn.setRequestMethod("GET");
			httpUrlConn.connect();
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			httpUrlConn.disconnect();
			JSON json = (JSON) JSON.parse(buffer.toString());
			WeixinTokenVo weixinTokenVo = (WeixinTokenVo) JSON.toJavaObject(json, WeixinTokenVo.class);
			return weixinTokenVo.getAccess_token();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @return 获取微信公众号关注人数
	 */
	public int wechatAttNumber(Date current) {
		try {
			String weixin_token = wechatToken();
			String method = "https://api.weixin.qq.com/datacube/getusercumulate";
			String groupUrl = method + "?access_token=" + weixin_token;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String date = sdf.format(current);
			JSONObject jsonParam = new JSONObject();
			jsonParam.put("begin_date", date);
			jsonParam.put("end_date", date);
			String str = GetJsonData.getJsonData(jsonParam, groupUrl);
			// 响应
			JSON json = (JSON) JSON.parse(str);
			WeixinCountVo weixinCountVo = (WeixinCountVo) JSON.toJavaObject(json, WeixinCountVo.class);
			if(weixinCountVo != null && weixinCountVo.getList().size()>0) {
				return weixinCountVo.getList().get(0).getCumulate_user();
			}else {
				//查询上一条微信记录的值并返回
				memberManagerDao.maxAttentionNum(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * @return 获取微博关注数
	 */
	public int weiboAttNumber() {
		try {
			StringBuffer buffer = new StringBuffer();
			String weibo_token = "2.00jyiAcGa5oGJDd7cd51d162_JrYWD";
			String uids = "6058470661";
			String weiboCountUrl = "https://api.weibo.com/2/users/counts.json?access_token=" + weibo_token + "&uids="
					+ uids;
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			URL url = new URL(weiboCountUrl);
			HttpsURLConnection httpsURLConn = (HttpsURLConnection) url.openConnection();
			httpsURLConn.setSSLSocketFactory(ssf);
			httpsURLConn.setDoInput(true);
			httpsURLConn.setDoOutput(true);
			httpsURLConn.setUseCaches(false);
			httpsURLConn.setRequestMethod("GET");
			httpsURLConn.connect();
			InputStream inputStream = httpsURLConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			httpsURLConn.disconnect();
			JSON json = (JSON) JSON.parse(buffer.toString());
			List<WeiBoCountVo> weiBoCountVo = JSON.parseArray(json.toString(), WeiBoCountVo.class);
			return weiBoCountVo.get(0).getFollowers_count();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public List<PieVo> getPieVos() {
		return pieVos;
	}

	public void setPieVos(List<PieVo> pieVos) {
		this.pieVos = pieVos;
	}

	public List<LineChartVo> getLineChartVos() {
		return lineChartVos;
	}

	public void setLineChartVos(List<LineChartVo> lineChartVos) {
		this.lineChartVos = lineChartVos;
	}

	public List<TargetPieVo> getTargetPieVos() {
		return targetPieVos;
	}

	public void setTargetPieVos(List<TargetPieVo> targetPieVos) {
		this.targetPieVos = targetPieVos;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
