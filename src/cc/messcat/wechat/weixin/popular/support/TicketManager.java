package cc.messcat.wechat.weixin.popular.support;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.messcat.wechat.weixin.popular.api.TicketAPI;
import cc.messcat.wechat.weixin.popular.bean.Ticket;
import cc.modules.util.ObjValid;

/**
 * TicketManager ticket 自动刷新
 * 
 * @author LiYi
 *
 */
public class TicketManager {

	private static Logger log = LoggerFactory.getLogger(TicketManager.class);

	private static Map<String, String> ticketMap = new LinkedHashMap<String, String>();

	private static Map<String, Timer> timerMap = new HashMap<String, Timer>();

	/**
	 * 初始化ticket 刷新，每119分钟刷新一次。 依赖TokenManager
	 * 
	 * @param appid
	 * @throws InterruptedException
	 */
	public static void init(final String appid) throws InterruptedException {
		if(timerMap.containsKey(appid)){
			timerMap.get(appid).cancel();
		}
		Timer timer = new Timer(true);
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				// 获取 ticket
				Ticket ticket = null;
				do {
					try {
						Thread.sleep(5000);
						String access_token = TokenManager.getToken(appid);
						if (ObjValid.isValid(access_token)) {
							ticket = TicketAPI.ticketGetticket(access_token);
							ticketMap.put(appid, ticket.getTicket());
						} else {
							log.info("TicketManager类----init方法-----拿取token失败");
						}
					} catch (InterruptedException e) {
						log.error("TicketManager类----init方法-----获取ticket失败",e);
						break;
					}
				} while (!ObjValid.isValid(ticket));
				log.info("TicketManager类----init方法-----获取ticket:" + ticket.getTicket());
			}

		}, 0, 1000 * 3600);
		timerMap.put(appid, timer);
	}

	/**
	 * 获取 jsapi ticket
	 * 
	 * @param appid
	 * @return
	 * @throws Exception
	 */
	public static String getTicket(String appid) {
		return ticketMap.get(appid);
	}

}
