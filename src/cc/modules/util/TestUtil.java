package cc.modules.util;

import cc.messcat.entity.CoupnUser;
import cc.messcat.vo.UserCoupnVo;

public class TestUtil {
	public static void main(String[] args) {
		CoupnUser coupnUser = new CoupnUser();
		coupnUser.setId(10L);
		coupnUser.setSharer("小米");
		
		UserCoupnVo userCoupnVo = new UserCoupnVo();
		userCoupnVo = (UserCoupnVo)PackageUtil.packageEntity(userCoupnVo, coupnUser);
	}
}
