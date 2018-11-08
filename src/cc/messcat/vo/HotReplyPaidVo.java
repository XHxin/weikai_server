package cc.messcat.vo;

import java.util.List;

public class HotReplyPaidVo {

	private Long replyId;           //回复表Id(用来给收藏功能传参)
	private String photo;			//专家头像
	private String expertId;        //专家Id
	private String expert;			//专家名字
	private String memberName;      //提问者的名字
	private String problem;			//问题
	private String content;			//内容(图文)
	private List<String> pictures;			//图片
	private String voice;			//音频文件
	private String voiceDuration;	//音频时长
	private String addTime;			//创建时间
	private int replyLikedSum;		//多少人觉得值
    private String collectStatus;   //收藏状态(0代表未收藏,1代表已收藏)
    private String likeStatus;      //点赞状态(0代表未点赞,1代表已点赞)
    private String classify;        //专家分类信息
	
	public Long getReplyId() {
		return replyId;
	}
	public void setReplyId(Long id) {
		this.replyId = id;
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
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getExpert() {
		return expert;
	}
	public void setExpert(String expert) {
		this.expert = expert;
	}
	public int getReplyLikedSum() {
		return replyLikedSum;
	}
	public void setReplyLikedSum(int likedSum) {
		this.replyLikedSum = likedSum;
	}
	public String getCollectStatus() {
		return collectStatus;
	}
	public void setCollectStatus(String collectStatus) {
		this.collectStatus = collectStatus;
	}
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
	public String getClassify() {
		return classify;
	}
	public void setClassify(String classify) {
		this.classify = classify;
	}
	public String getExpertId() {
		return expertId;
	}
	public void setExpertId(String expertId) {
		this.expertId = expertId;
	}
	public String getLikeStatus() {
		return likeStatus;
	}
	public void setLikeStatus(String likeStatus) {
		this.likeStatus = likeStatus;
	}
}
