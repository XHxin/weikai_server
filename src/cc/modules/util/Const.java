package cc.modules.util;

/**
 * @author handsomeWS
 * 
 */
public class Const {

	// 防止SQL注入的方法
	public static boolean voidSQL(String str) {

		boolean str2 = true;
		for (int i = 0; i < str.trim().length(); i++) {
			String tempStr = str.substring(i, i + 1);
			if (tempStr.equals("=") || tempStr.equals("'") || tempStr.equals(":")) {
				str2 = false;
				break;
			}
		}
		return str2;
	}

}
