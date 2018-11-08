package cc.messcat.wechat.weixin.popular.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.messcat.wechat.weixin.popular.bean.BaseResult;
import cc.messcat.wechat.weixin.popular.bean.Menu;
import cc.messcat.wechat.weixin.popular.bean.MenuButtons;
import cc.messcat.wechat.weixin.popular.client.LocalHttpClient;
import cc.messcat.wechat.weixin.popular.support.TokenManager;
import cc.modules.constants.Constants;

public class MenuAPI extends BaseAPI {
	private static Logger log = LoggerFactory.getLogger(MenuAPI.class); 
	/**
	 * 创建菜单
	 * 
	 * @param access_token
	 * @param menuJson
	 *            菜单json 数据 例如{\"button\":[{\"type\":\"click\",\"name\":\"今日歌曲\",\"key\":\"V1001_TODAY_MUSIC\"},{\"type\":\"click\",\"name\":\"歌手简介\",\"key\":\"V1001_TODAY_SINGER\"},{\"name\":\"菜单\",\"sub_button\":[{\"type\":\"view\",\"name\":\"搜索\",\"url\":\"http://www.soso.com/\"},{\"type\":\"view\",\"name\":\"视频\",\"url\":\"http://v.qq.com/\"},{\"type\":\"click\",\"name\":\"赞一下我们\",\"key\":\"V1001_GOOD\"}]}
	 *            ] }
	 * @return
	 */
	public static BaseResult menuCreate(String access_token, String menuJson) {
		HttpUriRequest httpUriRequest = RequestBuilder.post().setHeader(jsonHeader).setUri(BASE_URI + "/cgi-bin/menu/create")
			.addParameter("access_token", access_token).setEntity(new StringEntity(menuJson, Charset.forName("utf-8"))).build();
		return LocalHttpClient.executeJsonResult(httpUriRequest, BaseResult.class);
	}

	/**
	 * 创建菜单
	 * 
	 * @param access_token
	 * @param menuButtons
	 * @return
	 */
	public static BaseResult menuCreate(String access_token, MenuButtons menuButtons) {
		String str = JsonUtil.toJSONString(menuButtons);
		return menuCreate(access_token, str);
	}

	/**
	 * 获取菜单
	 * 
	 * @param access_token
	 * @return
	 */
	public static Menu menuGet(String access_token) {
		HttpUriRequest httpUriRequest = RequestBuilder.post().setUri(BASE_URI + "/cgi-bin/menu/get").addParameter("access_token",
			access_token).build();
		return LocalHttpClient.executeJsonResult(httpUriRequest, Menu.class);
	}

	/**
	 * 删除菜单
	 * 
	 * @param access_token
	 * @return
	 */
	public static BaseResult menuDelete(String access_token) {
		HttpUriRequest httpUriRequest = RequestBuilder.post().setUri(BASE_URI + "/cgi-bin/menu/delete").addParameter(
			"access_token", access_token).build();
		return LocalHttpClient.executeJsonResult(httpUriRequest, BaseResult.class);
	}

	public static void main(String[] args) throws Exception {
		TokenManager.init(Constants.APPID, Constants.APPSECRET);
		System.out.println(Constants.APPID);
		String token = TokenAPI.token(Constants.APPID, Constants.APPSECRET).getAccess_token();
		System.out.println(token);
		BaseResult baseResult = MenuAPI.menuCreate(token, getMenu());
		log.error(baseResult.getErrcode() + "--" + baseResult.getErrmsg());
	}

	/**
	 * 组装菜单数据
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static MenuButtons getMenu() throws UnsupportedEncodingException {

		MenuButtons menuButtons = new MenuButtons();

		MenuButtons.Button mainBtn1 = new MenuButtons.Button();
		mainBtn1.setName("准入报告查询");
		mainBtn1.setType("view");
		System.out.println(Constants.WEBSITE_URL);
		String url1 = URLEncoder.encode(Constants.WEBSITE_URL + "/epIndexAction!report.action", "UTF-8");
		System.out.println(url1);
		mainBtn1.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + Constants.APPID + "&redirect_uri=" + url1
			+ "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect");
		System.out.println(mainBtn1.getUrl());
		
		MenuButtons.Button childBtn4 = new MenuButtons.Button();
		childBtn4.setName("行业新闻");
		childBtn4.setType("view");
		String url4 = URLEncoder.encode(Constants.WEBSITE_URL + "/epIndexAction!news1.action", "UTF-8");
		childBtn4.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + Constants.APPID + "&redirect_uri=" + url4
			+ "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect");
		
		MenuButtons.Button childBtn5 = new MenuButtons.Button();
		childBtn5.setName("技术动态");
		childBtn5.setType("view");
		String url5 = URLEncoder.encode(Constants.WEBSITE_URL + "/epIndexAction!news2.action", "UTF-8");
		childBtn5.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + Constants.APPID + "&redirect_uri=" + url5
			+ "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect");
		
		MenuButtons.Button childBtn6 = new MenuButtons.Button();
		childBtn6.setName("质量分析");
		childBtn6.setType("view");
		String url6 = URLEncoder.encode(Constants.WEBSITE_URL + "/epIndexAction!news3.action", "UTF-8");
		childBtn6.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + Constants.APPID + "&redirect_uri=" + url6
			+ "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect");
		
		MenuButtons.Button childBtn7 = new MenuButtons.Button();
		childBtn7.setName("电商品控动态");
		childBtn7.setType("view");
		String url7 = URLEncoder.encode(Constants.WEBSITE_URL + "/epIndexAction!news4.action", "UTF-8");
		childBtn7.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + Constants.APPID + "&redirect_uri=" + url7
			+ "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect");

		MenuButtons.Button mainBtn2 = new MenuButtons.Button();
		mainBtn2.setName("动态资讯");
		List<MenuButtons.Button> button2List = new ArrayList<MenuButtons.Button>();
		button2List.add(childBtn4);
		button2List.add(childBtn5);
		button2List.add(childBtn6);
		button2List.add(childBtn7);
		mainBtn2.setSub_button(button2List);
		
		
		MenuButtons.Button mainBtn3 = new MenuButtons.Button();
		mainBtn3.setName("个人中心");
		mainBtn3.setType("view");
		System.out.println(Constants.WEBSITE_URL);
		String url3 = URLEncoder.encode(Constants.WEBSITE_URL + "/epIndexAction!center.action", "UTF-8");
		System.out.println(url3);
		mainBtn3.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + Constants.APPID + "&redirect_uri=" + url3
			+ "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect");
		System.out.println(mainBtn3.getUrl());
		

		menuButtons.setButton(new MenuButtons.Button[] { mainBtn1,mainBtn2,mainBtn3});
		return menuButtons;
	}

}