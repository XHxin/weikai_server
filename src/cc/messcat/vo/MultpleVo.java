package cc.messcat.vo;

import java.util.List;

public class MultpleVo {

	private long rowCount;   //查询总条数
	private String searchKeyWord;  //查询关键字
	private List<StandReadingVo> standReadingShare; //质量分享
	private List<ExpertVo> experts;  //全部专家
	private List<ReplyVo> hotReplyVos;		   //回答问题
	private List<LiveVo> liveVideos;			//视频课程(包括直播)
	private List<SpecialVo> specialList;		//专题--大集合
	private List<StandardReadListVo3>  standReadingColumn; //专栏--连载
	public long getRowCount() {
		return rowCount;
	}
	public void setRowCount(long rowCount) {
		this.rowCount = rowCount;
	}
	public String getSearchKeyWord() {
		return searchKeyWord;
	}
	public void setSearchKeyWord(String searchKeyWord) {
		this.searchKeyWord = searchKeyWord;
	}
	public List<StandReadingVo> getStandReadingShare() {
		return standReadingShare;
	}
	public void setStandReadingShare(List<StandReadingVo> standReadingShare) {
		this.standReadingShare = standReadingShare;
	}
	public List<ExpertVo> getExperts() {
		return experts;
	}
	public void setExperts(List<ExpertVo> experts) {
		this.experts = experts;
	}
	public List<ReplyVo> getHotReplyVos() {
		return hotReplyVos;
	}
	public void setHotReplyVos(List<ReplyVo> hotReplyVos) {
		this.hotReplyVos = hotReplyVos;
	}
	public List<LiveVo> getLiveVideos() {
		return liveVideos;
	}
	public void setLiveVideos(List<LiveVo> liveVideos) {
		this.liveVideos = liveVideos;
	}
	public List<SpecialVo> getSpecialList() {
		return specialList;
	}
	public void setSpecialList(List<SpecialVo> specialList) {
		this.specialList = specialList;
	}
	public List<StandardReadListVo3> getStandReadingColumn() {
		return standReadingColumn;
	}
	public void setStandReadingColumn(List<StandardReadListVo3> standReadingColumn) {
		this.standReadingColumn = standReadingColumn;
	}
	
	
	
}
