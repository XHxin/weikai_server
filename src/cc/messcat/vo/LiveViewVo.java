package cc.messcat.vo;

import java.util.List;

public class LiveViewVo {
	private String expertId;	//专家ID
	private String expertName;  //专家名字
	private String expertPhoto; //专家头像
	private String isAttention; //是否点赞
	private String rtmpUrl;    //rtpm播放地址
	private String flvUrl;     //flv播放地址
	private String hlsUrl;	   //hls播放地址
	private String groupId;	   //群组ID
	private String shareURL;   //分享url
	private String title;	   //标题
	private int isFinish;      //是否结束直播(0:否,1:是)
	private String isSilence;  //专家和管理员是否设置了禁言(0:否,1:是)
	private String isSilenceAuth;  //是否有禁言权限(0:否,1:是)
	private List<Long> silenceList; //禁言列表
	
	
	public String getExpertId() {
		return expertId;
	}
	public void setExpertId(String expertId) {
		this.expertId = expertId;
	}
	public String getExpertName() {
		return expertName;
	}
	public void setExpertName(String expertName) {
		this.expertName = expertName;
	}
	public String getExpertPhoto() {
		return expertPhoto;
	}
	public void setExpertPhoto(String expertPhoto) {
		this.expertPhoto = expertPhoto;
	}
	public String getIsAttention() {
		return isAttention;
	}
	public void setIsAttention(String isAttention) {
		this.isAttention = isAttention;
	}
	public String getRtmpUrl() {
		return rtmpUrl;
	}
	public void setRtmpUrl(String rtmpUrl) {
		this.rtmpUrl = rtmpUrl;
	}
	public String getFlvUrl() {
		return flvUrl;
	}
	public void setFlvUrl(String flvUrl) {
		this.flvUrl = flvUrl;
	}
	public String getHlsUrl() {
		return hlsUrl;
	}
	public void setHlsUrl(String hlsUrl) {
		this.hlsUrl = hlsUrl;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getShareURL() {
		return shareURL;
	}
	public void setShareURL(String shareURL) {
		this.shareURL = shareURL;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getIsSilence() {
		return isSilence;
	}
	public void setIsSilence(String isSilence) {
		this.isSilence = isSilence;
	}
	public String getIsSilenceAuth() {
		return isSilenceAuth;
	}
	public void setIsSilenceAuth(String isSilenceAuth) {
		this.isSilenceAuth = isSilenceAuth;
	}
	public int getIsFinish() {
		return isFinish;
	}
	public void setIsFinish(int isFinish) {
		this.isFinish = isFinish;
	}
	public List<Long> getSilenceList() {
		return silenceList;
	}
	public void setSilenceList(List<Long> silenceList) {
		this.silenceList = silenceList;
	}
	
}
