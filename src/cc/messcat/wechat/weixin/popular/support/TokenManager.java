package cc.messcat.wechat.weixin.popular.support;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.messcat.wechat.weixin.popular.api.TokenAPI;
import cc.messcat.wechat.weixin.popular.bean.Token;
import cc.modules.util.ObjValid;

/**
 * TokenManager token 自动刷新
 * 
 * @author LiYi
 *
 */
public class TokenManager {

	private static Logger log = LoggerFactory.getLogger(TokenManager.class);

	private static Map<String, String> tokenMap = new LinkedHashMap<String, String>();

	private static Map<String, Timer> timerMap = new HashMap<String, Timer>();

	/**
	 * 初始化token 刷新，每118分钟刷新一次。
	 * 
	 * @param appid
	 * @param secret
	 * @throws InterruptedException
	 */
	public static void init(final String appid, final String secret) throws InterruptedException {
		if(timerMap.containsKey(appid)){
			timerMap.get(appid).cancel();
		}
		Timer timer = new Timer(true);
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				// 获取access_token
				Token token = null;
				boolean flag = false;
				do {
					try {
						if (flag) {
							log.info("TokenManager类----init方法-----获取ticket 异常，5s后再尝试获取");
							Thread.sleep(5000);
						}
						token = TokenAPI.token(appid, secret);
						tokenMap.put(appid, token.getAccess_token());
						flag = true;
					} catch (InterruptedException e) {
						log.error("TokenManager类----init方法-----获取token失败：",e);
						break;
					}
				} while (!ObjValid.isValid(token) || ObjValid.isValid(token.getErrcode()));
				log.info("获取access_token:" + token.getAccess_token());
			}
		}, 0, 3600*1000);
		timerMap.put(appid,timer);
	}

	/**
	 * 获取 access_token
	 * 
	 * @param appid
	 * @return
	 * @throws Exception
	 */
	public static String getToken(String appid) {
		return tokenMap.get(appid);
	}

}
