package cc.modules.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @auther xinye
 * @create 2017 12 18
 */
public class SnsAPI {

    private static final String OPEN_URI = "https://open.weixin.qq.com";
    private static final String APPID = "wx4e844032c24a4508";

    /**
     * 生成授权URL
     * snsapiUserinfo base：只获取openId无需关注公众号   userinfo：获取openId、昵称、性别等信息需要关注公众号
     */
    public static String generateOAuth2Url(String appid, String redirectUri, boolean snsapiUserinfo, String state) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(OPEN_URI + "/connect/oauth2/authorize?")
                    .append("appid=").append(appid)
                    .append("&redirect_uri=").append(URLEncoder.encode(redirectUri, "utf-8"))
                    .append("&response_type=code")
                    .append("&scope=").append(snsapiUserinfo ? "snsapi_userinfo" : "snsapi_base")
                    .append("&state=").append(state == null ? "" : state)
                    .append("#wechat_redirect");
            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String generateOAuth2UrlScopeUser(String redirectUri) {
        return generateOAuth2Url(APPID, redirectUri, true, "");
    }

    public static void main(String[] args) {
        String url = SnsAPI.generateOAuth2UrlScopeUser("http://www.cert-map.com/h5/course/single_course.html?type=1&id=33");
        System.out.println(url);
    }
}
