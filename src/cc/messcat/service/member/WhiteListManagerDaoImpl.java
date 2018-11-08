package cc.messcat.service.member;

import java.util.List;

import cc.messcat.bases.BaseManagerDaoImpl;
import cc.messcat.entity.WhiteList;

public class WhiteListManagerDaoImpl extends BaseManagerDaoImpl implements WhiteListManagerDao{

	@Override
	public WhiteList getWhiteList(String mobile) {
		return whiteListDao.getWhiteList(mobile);
	}

}
