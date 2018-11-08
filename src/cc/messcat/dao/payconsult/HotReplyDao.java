package cc.messcat.dao.payconsult;

import java.util.List;

import cc.messcat.entity.FeedBack;
import cc.messcat.entity.HotProblem;
import cc.messcat.entity.HotReply;
import cc.messcat.entity.Member;
import cc.messcat.entity.HotReplyFees;
import cc.messcat.entity.HotReplyLiked;
import cc.messcat.entity.Share;
import cc.messcat.entity.WebSite;
import cc.modules.commons.Pager;

public interface HotReplyDao {

	Pager getRecommendList(int pageNo, int pageSize);

	HotReplyFees getReplyFees(Long memberId);

	Pager getReplyList(int pageNo, int pageSize, Long expertId);

	HotProblem getProblem(Long problemId);

	Member getMember(Long memberId);

	void commitProblem(HotProblem entity);

	Pager getAllReply(int pageNo, int pageSize);

	HotReply getHotReply(Long id);

	boolean commitFeedBack(FeedBack feedBack);

	Pager getNotReply(int pageNo, int pageSize, Long memberId);

	Pager getHistoryReply(int pageNo, int pageSize, Long memberId);

	void commitReply(HotReply hotReply, Long expertId,HotProblem problem);
	
	//获取最大问题id
	public Long getMaxHotProblemId();

	int getViewSum(Long expertId, Long problemId);

	int getReplyLikedSum(Long expertId, Long replyId);

	List<FeedBack> getFeedBackList(Long memberId, Long expertId);

	String getCollectStatus(Long memberId, Long replyId);

	int getCollectSum(Long replyId);

	List<HotReply> getHotReplyList(Long replyId);

	void addReplyLiked(HotReplyLiked like);

	HotReplyLiked getReplyLiked(Long replyId, Long expertId, Long memberId);

	void delete(HotReplyLiked hotReplyLiked);
   
	/*
	 * 专家操作提问价格
	 */
	void updateReplyFees(HotReplyFees hotReplyFees);
	void saveReplyFees(HotReplyFees entity);
	
	//更改问题状态
	public void update(HotProblem hotProblem);
    //查询统一价格
	WebSite getWebSite();
    //用于已购列表
	HotReply getReply(Long expertId, Long problemId);

	Share getShare();
	//获取问题列表,用于判断哪些问题已失效
	List<HotProblem> getProblemList();
    //获取回答信息
	HotReply getHotReply(Long problemId, Long expertId);

	void updateProblem(HotProblem problem);
	
	//根据问题id查找回复
	HotReply getHotReplyByProblem(Long problemId);

	void updateHotReply(HotReply hotReply);

	//模糊查询热门问题
	List<HotReply> getReplyBySearchKeyWord(String searchKeyWord);

	//点击查看更多的热门问题
	List<HotReply> getMoreHotReply(String searchKeyWord,int pageNo, int pageSize);

	HotReply isResubmit(Long problemId, Long expertId);

}
