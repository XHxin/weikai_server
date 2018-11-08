package cc.messcat.vo;

public class LiveVideoBespeakVo {

	private String id;//课程的ID
	private String title;//课程名称
	private String introduct;//视频简介
	private String terminal;//观看设备，0为移动端，1为PC端
	private String applyDate;//直播时间
	private String expertId;//直播专家的ID
	private String bespeakStatus;//预约的状态\
	private String isCancelBespeak;//0为可取消，1为不可取消
	private String isBeginLive;//0为未开始直播 ，1为开始直播 。是否开始直播
	private String checkRemark;//审核备注，审核通过与否发送给专家
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getIntroduct() {
		return introduct;
	}
	public void setIntroduct(String introduct) {
		this.introduct = introduct;
	}
	public String getTerminal() {
		return terminal;
	}
	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}
	
	public String getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(String applyDate) {
		this.applyDate = applyDate;
	}
	
	public String getExpertId() {
		return expertId;
	}
	public void setExpertId(String expertId) {
		this.expertId = expertId;
	}
	public String getBespeakStatus() {
		return bespeakStatus;
	}
	public void setBespeakStatus(String bespeakStatus) {
		this.bespeakStatus = bespeakStatus;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIsCancelBespeak() {
		return isCancelBespeak;
	}
	public void setIsCancelBespeak(String isCancelBespeak) {
		this.isCancelBespeak = isCancelBespeak;
	}
	public String getIsBeginLive() {
		return isBeginLive;
	}
	public void setIsBeginLive(String isBeginLive) {
		this.isBeginLive = isBeginLive;
	}
	public String getCheckRemark() {
		return checkRemark;
	}
	public void setCheckRemark(String checkRemark) {
		this.checkRemark = checkRemark;
	}
	
}
