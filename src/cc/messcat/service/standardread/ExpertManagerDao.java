package cc.messcat.service.standardread;

import java.util.Date;
import java.util.List;

import cc.messcat.entity.Member;
import cc.messcat.vo.ExpertVo;
import cc.messcat.vo.HotExpertListVo;
import cc.messcat.vo.PayConsultExpertVo;
import cc.modules.commons.Pager;

public interface ExpertManagerDao {
    /**
     * 首页默认的四位推荐专家信息
     */
	Pager getHotExpertList(int pageNo, int pageSize1,Member member);
	
	/**
	 *  热门专家推荐专栏上  点击"查看更多" 之后的专家列表
	 */
	Pager getHotExpertList2(int pageNo, int pageSize,Member member);

	boolean clickAttention(Long memberId,Long expertId,String attentionStatus);
    /*
     * 得到"我的关注"列表
     */
	Pager getMyAttention(int pageNo, int pageSize, Member member);
    /*
     * 点赞功能,一天仅可点赞一次,不可取消点赞
     */
	boolean clickLike(Long memberId, Long expertId, Date today);
    
	/*
	 * 获取付费咨询模块的 推荐专家
	 */
	List<HotExpertListVo> getPayExpertList(int pageNo, int pageSize2, Member member);
    /*
     * 获取分类专家列表
     */
	Pager getClassifyList(int pageNo, int pageSize, Long classifyId, Member member);
    //查看更多的提问专家
	Pager getProExpertList(int pageNo, int pageSize, Member member);

	PayConsultExpertVo getPayExpertDetail(Long expertId, Member member);

	HotExpertListVo getHotExpertDetail(Long expertId,Member member);

	//根据关键字查询专家
	List<ExpertVo> getExpertBySearchKeyWord(String searchKeyWord);

	//根据关键字查询更多的专家
	Pager getMoreExpert(String searchKeyWord, int pageNo, int pageSize);
}
