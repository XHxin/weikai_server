package cc.modules.util;

public class LiveUtil {

	public static String userSig="eJxlj11PgzAYhe-5FaS3M9oWysA7Prxo5jQ6dM4bQtbCKlupXUHI4n9XcYlNfG*f5*Sc9*S4rgvy29Vlud22nTSFGRUH7rULILj4g0oJVpSm8DT7B-mghOZFWRmuJ4gIIRhC2xGMSyMqcTZKdhDSwkfWFFPHb97-DvvBHEa2IuoJLm82KX3INote1j0b*-e0uXtqXvYsZQyRFc3H*yHA7TJQb497UtGkpruYwmj3HOXrJquyzk-0HPWYrIePMNEceVd8EcezZNa9tp492ogDPz8EwxCH2EMW7bk*ilZOAoaIIOzBnwPOp-MFHdpchg__";
	public static String identifier="admin";
	public static String sdkappid="1400046709";
	public static String apn="1";
	public static String contenttype="json";
	public static String str="?userSig="+userSig+"&identifier="+identifier+"&sdkappid="+sdkappid+"&apn="+apn+"&contenttype="+contenttype;
	
	/**
	 * 帐号管理
	 * 1.独立模式账号导入接口
	 */
	public static String accountImport() {		
		return "https://console.tim.qq.com/v4/im_open_login_svc/account_import?"+str;
	}
	
	//2.独立模式帐号批量导入接口
	public static String multiAccountImport(){
		return "https://console.tim.qq.com/v4/im_open_login_svc/multiaccount_import"+str;
	}
	
	//3.帐号登录态失效接口
	public static String kick() {
		return "https://console.tim.qq.com/v4/im_open_login_svc/kick"+str;
	}
	
	//4.托管模式存量账号导入
	public static String registerAccountV1() {
		return "https://console.tim.qq.com/v4/registration_service/register_account_v1"+str;
	}
	
	/**
	 * 单聊消息
	 * 1.单发单聊消息
	 */
	public static String sendMsg() {
		return "https://console.tim.qq.com/v4/openim/sendmsg"+str;
	}
	
	//2.批量发单聊消息
	public static String batchsendMsg() {
		return "https://console.tim.qq.com/v4/openim/batchsendmsg"+str;
	}
	
	//3.导入单聊消息
	public static String importMsg() {
		return "https://console.tim.qq.com/v4/openim/importmsg"+str;
	}
	
	/**
	 * 在线状态
	 * 获取用户在线状态
	 */
	public static String queryState() {
		return "https://console.tim.qq.com/v4/openim/querystate"+str;
	}
	
	/**
	 * 资料管理
	 * 1.拉取资料
	 */
	public static String portraitGet() {
		return "https://console.tim.qq.com/v4/profile/portrait_get"+str;
	}
	
	//2.设置资料
	public static String portraitSet() {
		return "https://console.tim.qq.com/v4/profile/portrait_set"+str;
	}
	
	/**
	 * 关系链管理
	 * 1.添加好友
	 */
	public static String friendAdd() {
		return "https://console.tim.qq.com/v4/sns/friend_add"+str;
	}
	
	//2.导入好友
	public static String friendImport() {
		return "https://console.tim.qq.com/v4/sns/friend_import"+str;
	}
	
	//3.删除好友
	public static String friendDelete() {
		return "https://console.tim.qq.com/v4/sns/friend_delete"+str;
	}
	
	//4.删除所有好友
	public static String friendDeleteAll() {
		return "https://console.tim.qq.com/v4/sns/friend_delete_all"+str;
	}
	
	//5.校验好友
	public static String friendCheck() {
		return "https://console.tim.qq.com/v4/sns/friend_check"+str;
		
	}
	
	//6.拉取好友
	public static String friendGetAll() {
		return "https://console.tim.qq.com/v4/sns/friend_get_all"+str;
	}
	
	//7.拉取指定好友
	public static String friendGetList() {
		return "https://console.tim.qq.com/v4/sns/friend_get_list"+str;
	}
	
	//8.添加黑名单
	public static String blackListAdd() {
		return "https://console.tim.qq.com/v4/sns/black_list_add"+str;
	}
	
	//9.删除黑名单
	public static String blackListDelete() {
		return "https://console.tim.qq.com/v4/sns/black_list_delete"+str;
	}
	
	//10.拉取黑名单
	public static String blackListGet() {
		return "https://console.tim.qq.com/v4/sns/black_list_get"+str;
	}
	
	//11.校验黑名单
	public static String blackListCheck() {
		return "https://console.tim.qq.com/v4/sns/black_list_check"+str;
	}
	
	//12.添加分组
	public static String groupAdd() {
		return "https://console.tim.qq.com/v4/sns/group_add"+str;
	}
	
	//13.删除分组
	public static String groupDelete() {
		return "https://console.tim.qq.com/v4/sns/group_delete"+str;
	}
	
	/**
	 * 群组管理
	 * 1.获取APP中的所有群组
	 */
	public static String getAppidGroupList() {
		return "https://console.tim.qq.com/v4/group_open_http_svc/get_appid_group_list"+str;
	}
	
	//2.创建群组
	public static String createGroup() {
		return "https://console.tim.qq.com/v4/group_open_http_svc/create_group"+str;
	}
	
	//3.获取群组详细资料
	public static String getGroupInfo() {
		return "https://console.tim.qq.com/v4/group_open_http_svc/get_group_info"+str;
	}
	
	//4.获取群组成员详细资料
	public static String getGroupMemberInfo() {
		return "https://console.tim.qq.com/v4/group_open_http_svc/get_group_member_info"+str;
	}
	
	//5.修改群组基础资料
	public static String modifyGroupBaseInfo() {
		return "https://console.tim.qq.com/v4/group_open_http_svc/modify_group_base_info"+str;
	}
	
	//6.增加群组成员
	public static String addGroupMember() {
		return "https://console.tim.qq.com/v4/group_open_http_svc/add_group_member"+str;
	}
	
	//7.删除群组成员
	public static String deleteGroupMember() {
        return "https://console.tim.qq.com/v4/group_open_http_svc/delete_group_member"+str;
	}
	
	//8.修改群成员资料
	public static String modifyGroupMemberInfo() {
		return "https://console.tim.qq.com/v4/group_open_http_svc/modify_group_member_info"+str;
	}
	
	//9.解散群组
	public static String destroyGroup() {
		return "https://console.tim.qq.com/v4/group_open_http_svc/destroy_group"+str;
	}
	
	//10.在群组中发送普通消息
	public static String sendGroupMsg() {
		return "https://console.tim.qq.com/v4/group_open_http_svc/send_group_msg"+str;
	}
	
	//11.在群组中发送系统通知
	public static String sendGroupSystemNotification() {
		return "https://console.tim.qq.com/v4/group_open_http_svc/send_group_system_notification"+str;
	}
	
	//12.批量禁言和取消禁言
	public static String forbidSendMsg() {
		return "https://console.tim.qq.com/v4/group_open_http_svc/forbid_send_msg"+str;
	}
	
	//13.获取群组被禁言用户列表
	public static String getGroupShuttedUin() {
		return "https://console.tim.qq.com/v4/group_open_http_svc/get_group_shutted_uin"+str;
	}
	
	//14.拉取群漫游消息
	public static String groupMsgGetSimple() {
		return "https://console.tim.qq.com/v4/group_open_http_svc/group_msg_get_simple"+str;
	}
	
	//15.导入群成员
	public static String importGroupMember() {
		return "https://console.tim.qq.com/v4/group_open_http_svc/import_group_member"+str;
	}
	
	/**
	 * 脏字管理
	 * 1.查询APP自定义脏字
	 */
	public static String getDirty() {
		return "https://console.tim.qq.com/v4/openim_dirty_words/get"+str;
	}
	
	//2.添加APP自定义脏字
	public static String addDirty() {
		return "https://console.tim.qq.com/v4/openim_dirty_words/add"+str;
	}
	
	//3.删除APP自定义脏字
	public static String deleteDirty() {
		return "https://console.tim.qq.com/v4/openim_dirty_words/delete"+str;
	}
	
	/**
	 * 全局禁言管理 
	 * 1.设置全局禁言
	 */
	public static String setNoSpeaking() {
		return "https://console.tim.qq.com/v4/openconfigsvr/setnospeaking"+str;
	}
	
	//2.查询全局禁言
	public static String getNoSpeaking() {
		return "https://console.tim.qq.com/v4/openconfigsvr/getnospeaking"+str;
	}
}
