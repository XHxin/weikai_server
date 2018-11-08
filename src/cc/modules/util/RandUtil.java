package cc.modules.util;

import java.util.Random;

/**
 * 随机工具
 * @author Jesse
 *
 */
public class RandUtil {
	
	/**
	 * 随机生成指定长度的数字串
	 * @param count
	 * @return
	 */
	public static String getRandomStr(int count) { 
		StringBuffer str = new StringBuffer("");
		if (count > 0) {
			Random rand = new Random();
			for (int i = 0; i< count;i++ ) {
				str.append(rand.nextInt(10));
			}
		}
		return str.toString();
	}
	
	
	public static void main(String[] args) {
		System.out.println(getRandomStr(4));
	}
}
