package cc.messcat.service.paycosult;

import java.math.BigDecimal;
import java.util.List;

import cc.messcat.entity.FeedBack;
import cc.messcat.entity.HotProblem;
import cc.messcat.entity.HotReply;
import cc.messcat.entity.Member;
import cc.messcat.entity.HotReplyFees;
import cc.messcat.vo.HotReplyDetailVo;
import cc.messcat.vo.HotReplyPaidVo;
import cc.messcat.vo.HotReplyVo;
import cc.messcat.vo.ReplyVo;
import cc.modules.commons.Pager;

public interface HotReplyService {
    
	//获取热门推荐的回答
	Pager getRecommendList(int pageNo, int pageSize, Member member);
    //获取某个专家的全部回答
	Pager getReplyList(int pageNo, int pageSize, Long expertId, Member member);
	//提交问题
	void commitProblem(HotProblem entity);
	// 热门问答专栏查看更多
	Pager getAllReply(int pageNo, int pageSize, Member member);
	//提交反馈信息
	boolean commitFeedBack(FeedBack feedBack);
	//获取未答问答列表
	Pager getNotReply(int pageNo, int pageSize, Long memberId);
	//获取历史问答列表
	Pager getHistoryReply(int pageNo, int pageSize, Member member);
	//提交回复信息
	void commitReply(HotReply hotReply, Long expertId);
	
	//根据问题id获取单个问题信息
	public HotProblem getHotProblemById(Long id);
	
	//获取最大的问题id
	public Long getMaxHotProblemId();
	
	//根据专家id获取专家咨询收费表
	public HotReplyFees getReplyFeesByMemberId(Long memberId);
	//获取回答问题详情
	HotReplyDetailVo getReplyDetail(Long replyId);
	//热门问题详情(未支付)
	HotReplyVo getNotPayDetail(Long replyId, Member puMember);
	//热门问题详情(已支付)
	HotReplyPaidVo getPaidDetail(Long replyId, Long memberId);
	//热门回复中的点赞
	void addReplyLiked(Long replyId, Long expertId, Long memberId, String flag);
	//专家设置金额
	HotReplyFees getReplyFees(Long expertId);
	void settingsMoney(Long expertId, BigDecimal money, BigDecimal privateMoney);
	//获取问题列表,用于判断哪些问题已失效
	List<HotProblem> getProblemList();
	//获取回答信息
	HotReply getHotReply(Long problemId, Long expertId);
	//问题过了48小时委未被回答,则把状态也改为 退款状态
	void updateProblem(HotProblem problem);
	//模糊查询热门问题
	List<ReplyVo> getReplyBySearchKeyWord(String searchKeyWord);
	void updateHotReply(HotReply hotReply);
	//点击查看更多的热门问题
	Pager getMoreHotReply(String searchKeyWord, int pageNo, int pageSize);
	Pager getReplyList2(int pageNo, int pageSize, Long expertId, Member member);

    HotProblem getHotProblem(Long problemId);
}
