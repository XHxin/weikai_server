package cc.messcat.vo;

/**
 * @author HASEE
 *批量获取用户的粉丝数、关注数、微博数  接口返回实体
 */
public class WeiBoCountVo {

	private int id;
	private int followers_count;
	private int friends_count;
	private int statuses_count;
	private int pagefriends_count;
	private int private_friends_count;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getFollowers_count() {
		return followers_count;
	}
	public void setFollowers_count(int followers_count) {
		this.followers_count = followers_count;
	}
	public int getFriends_count() {
		return friends_count;
	}
	public void setFriends_count(int friends_count) {
		this.friends_count = friends_count;
	}
	public int getStatuses_count() {
		return statuses_count;
	}
	public void setStatuses_count(int statuses_count) {
		this.statuses_count = statuses_count;
	}
	public int getPagefriends_count() {
		return pagefriends_count;
	}
	public void setPagefriends_count(int pagefriends_count) {
		this.pagefriends_count = pagefriends_count;
	}
	public int getPrivate_friends_count() {
		return private_friends_count;
	}
	public void setPrivate_friends_count(int private_friends_count) {
		this.private_friends_count = private_friends_count;
	}
	
}
