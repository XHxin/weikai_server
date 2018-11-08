package cc.modules.util;

import java.io.IOException;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 
 * 提供各类加解密算法
 *
 */
public class SecurityTool {

	public static void main(String[] args) {
		String str = "888888";
		System.out.println(md5(str));
	}

	// MD5盐值字符串用来混淆MD5
	private static final String salt = "fkeopwfkwop54943r039#$%^87^JKKkll";
	private static final String xgSalt = "fkeipwfkwow54943r039#$%^87^JKKkll";

	/**
	 * 明文转化为md5加密
	 * 
	 * @param rawPass
	 * @return
	 */
	public static String md5(String rawPass) {
		return new Md5PasswordEncoder().encodePassword(rawPass, salt);
	}

	/**
	 * 
	 * 加密校验
	 * 
	 * @param passwdToken
	 * @param passwd
	 * @return
	 * @date 2016年12月23日
	 */
	public static boolean verifyMd5(String passwdToken, String passwd) {
		String token = md5(passwd);
		if (token.equals(passwdToken)) {
			return true;
		}
		return false;
	}

	/**
	 * 简单的md5加密
	 * 
	 * @param content
	 * @return
	 * @date 2016年12月23日
	 */
	public static String md5Simple(String content) {
		return new Md5PasswordEncoder().encodePassword(content, xgSalt);
	}

	// Base64编码
	public static String base64Encoder(String content) {
		BASE64Encoder b = new BASE64Encoder();
		return b.encode(content.getBytes());
	}

	public static String base64Encoder(byte[] bytes) {
		BASE64Encoder b = new BASE64Encoder();
		return b.encode(bytes);
	}

	// Base64解码
	public static String base64DecoderToString(String content) {
		BASE64Decoder d = new BASE64Decoder();
		try {
			return new String(d.decodeBuffer(content));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static byte[] base64DecoderTobytes(String content) {
		BASE64Decoder d = new BASE64Decoder();
		try {
			return d.decodeBuffer(content);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
