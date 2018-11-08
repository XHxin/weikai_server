package cc.messcat.vo;

public class SimpleVideoVo {

	private Long id;  //子视频Id
	private String videoUrl;  //视频播放url
	private String title;  //标题
	private String videoType; //视频类型(0-直播 1-录播 2-点播)
	private String videoStatus; //直播状态(0:未开播 1:直播中)
	private String applyDate;  //直播时间
	
	public String getVideoUrl() {
		return videoUrl;
	}
	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getVideoType() {
		return videoType;
	}
	public void setVideoType(String videoType) {
		this.videoType = videoType;
	}
	public String getVideoStatus() {
		return videoStatus;
	}
	public void setVideoStatus(String videoStatus) {
		this.videoStatus = videoStatus;
	}
	public String getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(String applyDate) {
		this.applyDate = applyDate;
	}
	
}
