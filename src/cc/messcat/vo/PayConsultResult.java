package cc.messcat.vo;

import java.util.List;

import cc.messcat.entity.HotReply;

public class PayConsultResult {

	private List<ExpertClassifyVo> classifyList;   	//专家分类列表
	private List<HotExpertListVo>  expertList;	   //付费咨询处的热门专家推荐专栏
	private List<HotReplyVo>  hotReplyList;		   //热门问题推荐专栏
	private int replyRowCount;             		//热门推荐专栏列表大小
		
	
	public List<ExpertClassifyVo> getClassifyList() {
		return classifyList;
	}
	public void setClassifyList(List<ExpertClassifyVo> classifyList) {
		this.classifyList = classifyList;
	}
	public List<HotExpertListVo> getExpertList() {
		return expertList;
	}
	public void setExpertList(List<HotExpertListVo> expertList) {
		this.expertList = expertList;
	}
	public List<HotReplyVo> getHotReplyList() {
		return hotReplyList;
	}
	public void setHotReplyList(List<HotReplyVo> hotReplyList) {
		this.hotReplyList = hotReplyList;
	}
	public int getReplyRowCount() {
		return replyRowCount;
	}
	public void setReplyRowCount(int replyRowCount) {
		this.replyRowCount = replyRowCount;
	}
}
