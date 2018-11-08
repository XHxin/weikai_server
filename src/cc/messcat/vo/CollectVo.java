package cc.messcat.vo;

/**
 * @author HASEE
 *5期，我的收藏列表返回实体
 */
public class CollectVo {

	private Long id;
	private int region;
	private int type;
	private String title;
	private String videoType;//0--直播   1--非直播
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public int getRegion() {
		return region;
	}
	public void setRegion(int region) {
		this.region = region;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getVideoType() {
		return videoType;
	}
	public void setVideoType(String videoType) {
		this.videoType = videoType;
	}
}
