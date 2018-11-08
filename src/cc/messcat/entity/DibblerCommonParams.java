package cc.messcat.entity;

/**
 * @author HASEE
 *请求点播接口公共参数,首字母要大写
 */
public class DibblerCommonParams {

	private String Action;//具体操作的指令接口名称
	private String Region;//区域参数，用来标识希望操作哪个区域的实例。
	private Long Timestamp;//	当前UNIX时间戳，可记录发起API请求的时间
	private int Nonce;//随机正整数，与 Timestamp 联合起来, 用于防止重放攻击。
	private String SecretId;//在云API密钥上申请的标识身份的 SecretId
	private String Signature;//请求签名，用来验证此次请求的合法性，需要用户根据实际的输入参数计算得出。计算方法可参考 签名方法 页面。
	private String SignatureMethod;//签名方式，目前支持HmacSHA256和HmacSHA1。只有指定此参数为HmacSHA256时，才使用HmacSHA256算法验证签名，其他情况均使用HmacSHA1验证签名。
	private String Token;//临时证书所用的Token，需要结合临时密钥一起使用。长期密钥不需要Token
	public String getAction() {
		return Action;
	}
	public void setAction(String action) {
		Action = action;
	}
	public String getRegion() {
		return Region;
	}
	public void setRegion(String region) {
		Region = region;
	}
	public Long getTimestamp() {
		return Timestamp;
	}
	public void setTimestamp(Long timestamp) {
		Timestamp = timestamp;
	}

	public int getNonce() {
		return Nonce;
	}
	public void setNonce(int nonce) {
		Nonce = nonce;
	}
	public String getSecretId() {
		return SecretId;
	}
	public void setSecretId(String secretId) {
		SecretId = secretId;
	}
	public String getSignature() {
		return Signature;
	}
	public void setSignature(String signature) {
		Signature = signature;
	}
	public String getSignatureMethod() {
		return SignatureMethod;
	}
	public void setSignatureMethod(String signatureMethod) {
		SignatureMethod = signatureMethod;
	}
	public String getToken() {
		return Token;
	}
	public void setToken(String token) {
		Token = token;
	}
	
	
}
