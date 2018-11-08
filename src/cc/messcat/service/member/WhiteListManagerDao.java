package cc.messcat.service.member;

import java.util.List;

import cc.messcat.entity.WhiteList;

public interface WhiteListManagerDao {

	WhiteList getWhiteList(String mobile);

}
