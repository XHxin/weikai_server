package cc.messcat.vo;

import java.util.List;

import cc.messcat.entity.FeedBack;

public class HotReplyDetailVo {

	private String memberName;		//提问者名字
	private String problem;			//问题
	private String content;			//内容(图文)
	private List<String> pictures;	//图片集合
	private String voice;			//音频文件
	private String voiceDuration;	//音频时长
	private String addTime;			//创建时间
	private int viewSum;			//看过人数
	private int replyLikedSum;		//多少人觉得值
	private int collectSum;			//收藏量
	
    private List<FeedBack> feedBackList;
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public List<String> getPictures() {
		return pictures;
	}
	public void setPictures(List<String> pictures) {
		this.pictures = pictures;
	}
	public String getVoice() {
		return voice;
	}
	public void setVoice(String voice) {
		this.voice = voice;
	}
	public String getVoiceDuration() {
		return voiceDuration;
	}
	public void setVoiceDuration(String voiceDuration) {
		this.voiceDuration = voiceDuration;
	}
	public String getAddTime() {
		return addTime;
	}
	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}
	public String getProblem() {
		return problem;
	}
	public void setProblem(String problemName) {
		this.problem = problemName;
	}
	public int getViewSum() {
		return viewSum;
	}
	public void setViewSum(int heardSum) {
		this.viewSum = heardSum;
	}
	public int getReplyLikedSum() {
		return replyLikedSum;
	}
	public void setReplyLikedSum(int likedSum) {
		this.replyLikedSum = likedSum;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public int getCollectSum() {
		return collectSum;
	}
	public void setCollectSum(int collectSum) {
		this.collectSum = collectSum;
	}
	public List<FeedBack> getFeedBackList() {
		return feedBackList;
	}
	public void setFeedBackList(List<FeedBack> feedBackList) {
		this.feedBackList = feedBackList;
	}
}
