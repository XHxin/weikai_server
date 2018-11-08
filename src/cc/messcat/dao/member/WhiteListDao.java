package cc.messcat.dao.member;

import java.util.List;

import cc.messcat.bases.BaseDao;
import cc.messcat.entity.WhiteList;

public interface WhiteListDao extends BaseDao{

	WhiteList getWhiteList(String mobile);
	
}
