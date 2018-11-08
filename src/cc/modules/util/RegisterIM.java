package cc.modules.util;

import cc.modules.security.ExceptionManager;
import com.em.api.EasemobIMUsers;
import com.em.comm.Constants;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author leo
 * @date 2018/6/12 15:54
 *  注册环信账号工具类
 */
public class RegisterIM {

    public static void main(String[] args) {
        createSingleUser(13380L);
    }

    public static void createSingleUser(Long memberId){
        String username="weikai"+memberId;
        String password=SecurityTool.md5Simple(Constants.DEFAULT_PASSWORD);
        ObjectNode datanode = JsonNodeFactory.instance.objectNode();
        datanode.put("username", username);
        datanode.put("password", password);
        // 添加环信平台账号
        ObjectNode createSingle = EasemobIMUsers.createNewIMUserSingle(datanode);
        if (createSingle.get("statusCode") == null
                || !"200".equals(createSingle.get("statusCode").toString())){
            throw new ExceptionManager("注册环信用户weikai"+memberId+"失败!该用户可能已经存在了");
        }
        // 用户添加客服为好友
        ObjectNode addFriend = EasemobIMUsers.addFriendSingle(username, "weikai2001");
        if (addFriend.get("statusCode") == null || !"200".equals(addFriend.get("statusCode").toString())){
            throw new ExceptionManager("添加好友失败!");
        }
    }
}
