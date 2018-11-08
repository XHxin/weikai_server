package cc.modules.util;

import java.util.Random;

/**
 * @author Administrator
 * @version 1.1
 */
public class StringUtil {

	public static boolean isBlank(String str) {
		boolean event = false;
		if ("".equals(str) || str == null)
			event = true;
		return event;
	}

	public static boolean isNotBlank(String str) {
		boolean event = false;
		if ((!"".equals(str)) && str != null)
			event = true;
		return event;
	}

	public static String getRandomString(int length) { //length表示生成字符串的长度
	    String base = "abcdefghijklmnopqrstuvwxyz0123456789";   
	    Random random = new Random();   
	    StringBuffer sb = new StringBuffer();   
	    for (int i = 0; i < length; i++) {   
	        int number = random.nextInt(base.length());   
	        sb.append(base.charAt(number));   
	    }   
	    return sb.toString();   
	 }

	 public static Long[] splitString(String str, String regex) {
		 if (str != null && !str.equals("")) {
			 String[] id = str.split(regex);
			 Long[] coupnId = new Long[id.length];
			 for (int i = 0; i < id.length; i++) {
				 coupnId[i] = Long.valueOf(id[i]);
			 }
			 return coupnId;
		 }
		 return null;
	 }
}
