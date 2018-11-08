package cc.messcat.dao.standardread;

import java.util.Date;
import java.util.List;

import cc.messcat.entity.Attention;
import cc.messcat.entity.ExpertClassify;
import cc.messcat.entity.Member;
import cc.messcat.entity.HotReplyFees;
import cc.messcat.entity.WebSite;
import cc.messcat.vo.HotExpertListVo;

public interface ExpertDao {

	List<HotExpertListVo> getHotExpertList(String recommendflag);

	List<HotExpertListVo> getHotExpertList2();

	boolean addAttention(Attention attention);

	Attention getAttention(Long memberId,Long expertId);

	void deleteAttention(Attention attention);
    /*
     * 获取"我的关注列表"
     */
	List<HotExpertListVo> getMyAttention(Long expertId);
    /*
     * 判断是否关注
     */
	String isAttention(String expertId,Long memberId);
    /*
     * 获取点赞数量
     */
	int getLikeSum(String expertId);
    /*
     * 获取当前用户的点赞时间,如果有,则选择最近的一条
     */
	String getLikeTime(String expertId, Long memberId);

	boolean clickLike(Long memberId, Long expertId, Date today);

	ExpertClassify getExpertList(Long classifyId);
    //获取回答收费对象
	HotReplyFees getReplyFees(Long expertId);

	int getAnswerSum(Long expertId);

	int getAttentionSum(Long expertId);

	List<Member> getMember();

	Member getExpert(Long expertId);
    //获取解读量
	int getReadSum(Long expertId);

	int getLikeSum(Long expertId);
    //专家详情
	List<HotExpertListVo> getHotExpertDetail(Long expertId);

	String getShareURL();
    //获取统一报价
	WebSite getWebSite();

	//获取专家的文章数量
	int getArticleSum(Long expertId);
	
	//根据关键字查询所有专家
	List<Member> getExpertBySearchKeyWord(String searchKeyWord);

	//根据关键字查询更多的专家
	List<Member> getMoreExpert(String searchKeyWord,int pageNo, int pageSize);
}
