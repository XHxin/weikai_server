package cc.messcat.wechat.weixin.popular.api;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import cc.messcat.wechat.weixin.popular.bean.Token;
import cc.messcat.wechat.weixin.popular.client.LocalHttpClient;

public class TokenAPI extends BaseAPI{

	/**
	 * 获取access_token
	 * @param appid
	 * @param secret
	 * @return
	 */
	public static Token token(String appid,String secret){
		HttpUriRequest httpUriRequest = RequestBuilder.get()
				.setUri(BASE_URI + "/cgi-bin/token")
				.addParameter("grant_type","client_credential")
				.addParameter("appid", appid)
				.addParameter("secret", secret)
				.build();
		return LocalHttpClient.executeJsonResult(httpUriRequest,Token.class);
	}

	/*public static void main(String[] args) {
		Token token = TokenAPI.token("wx2318a03a720cffd3", "7b42f9cf1f434ec0c9dde125fc223034");
		System.out.println(token.getAccess_token());
	}*/
}
