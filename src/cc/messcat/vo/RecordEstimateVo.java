package cc.messcat.vo;

public class RecordEstimateVo {
	
	private String memberName;	//用户昵称
	
	private String content;	//评价的内容
	
	private String contentTime;	//评价的时间

	private String videoTitle;//系列对应的子视频的标题
	
	private String reply;	//回复的内容
	
	private String replyTime;	//回复内容的时间
	
	private Integer videoId;	//评价对应视频的id
	
	private Integer seriesVideoId;	//对应视频的系列视频的id  -1代表这条评价的视频为单一视频，其他值代表为系列视频的id
	
	private String totalGradel;		//总的好评率
	
	private String photo;			//用户头像
	
	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContentTime() {
		return contentTime;
	}

	public void setContentTime(String contentTime) {
		this.contentTime = contentTime;
	}

	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}

	public String getReplyTime() {
		return replyTime;
	}	

	public void setReplyTime(String replyTime) {
		this.replyTime = replyTime;
	}

	public Integer getVideoId() {
		return videoId;
	}

	public void setVideoId(Integer videoId) {
		this.videoId = videoId;
	}

	public Integer getSeriesVideoId() {
		return seriesVideoId;
	}

	public void setSeriesVideoId(Integer seriesVideoId) {
		this.seriesVideoId = seriesVideoId;
	}

	public String getTotalGradel() {
		return totalGradel;
	}

	public void setTotalGradel(String totalGradel) {
		this.totalGradel = totalGradel;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getVideoTitle() {
		return videoTitle;
	}

	public void setVideoTitle(String videoTitle) {
		this.videoTitle = videoTitle;
	}
	

}
