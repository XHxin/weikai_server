package cc.messcat.service.standardread;

import java.util.List;

import cc.messcat.entity.Member;
import cc.messcat.entity.StandardReading;
import cc.messcat.vo.Adjunct;
import cc.messcat.vo.StandReadingVo;
import cc.messcat.vo.StandardReadListResultH5;
import cc.messcat.vo.StandardReadListVo;
import cc.messcat.vo.StandardReadListVo3;
import cc.modules.commons.Pager;

public interface StandardReadManagerDao {
	Pager getStandardReadingList(Integer pageNo, Integer pageSize2,Member member);   

	Pager getStandardReadingList3(String standardCode,String type, int pageNo,Integer pageSize,Member member);

	Pager getStandardReadingList5(String type, int pageNo, int pageSize,String isRecommend,Member member);
    
	/*
	 * 标准解读详情
	 */
	List<StandardReadListVo> searchStandardReading(String type, Long standardReadingId,Member member);
	
	Pager findList(String type, int pageNo, int pageSize, Long memberId);

	public StandardReading retrieveStandardReading(Integer id);


	Pager getHisStandardReadList(int pageNo, int pageSize, Long expertId, String type,Member member);

	/**
	 *   获取标准解读连载里面的解读列表
	 */
	List<Adjunct> getStandardRead(Long standardReadingId,String type);

	/**
	 * 根据关键字模糊查询标准解读
	 * @param searchKeyWord
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	Pager getStandardReadingList4(String searchKeyWord, int pageNo, int pageSize);

	StandardReadListResultH5 getStandardReadingListH5(int pageNo, int pageSize);

	List<Adjunct> getStandardReadH5(Long standardReadingId, String type);
    /**
     * 首页的专栏订阅
     */
	Pager getColumnList(int pageNo, int pageSize, String chiefShow, Member member);
    /**
     * 首页的精品文章
     */
	Pager getExcellentArticleList(int pageNo, int pageSize, String chiefShow, Member member);

	/**
	 * 根据关键字模糊查询质量分享(单一)
	 * @param searchKeyWord
	 * @return
	 */
	List<StandReadingVo> getStandardReadListBySearchKeyWord(String searchKeyWord,Long memberId);

	/**
	 * 根据关键字模糊查询标准解读质量分享(连载)
	 * @param searchKeyWord
	 * @return
	 */
	List<StandardReadListVo3> getStandardReadSerialise(String searchKeyWord,Long memberId);

	/**
	 * 点击查看更多的质量分享(单一)
	 * @param searchKeyWord
	 * @param qualityId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	Pager getMoreStandardRead(String searchKeyWord, String qualityId, int pageNo, int pageSize,Long memberId);

	/**
	 * 点击查看更多的质量分享(连载)
	 * @param searchKeyWord
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	Pager getMoreStandReadSerialise(String searchKeyWord, int pageNo, int pageSize,Long memberId);

}
