package cc.messcat.dao.standardread;

import java.util.List;
import cc.messcat.entity.ExpenseTotal;
import cc.messcat.entity.Member;
import cc.messcat.entity.Share;
import cc.messcat.entity.StandardReading;
import cc.modules.commons.Pager;

public interface StandardReadDao {

	Member getMember(Long memberID);
	
	public StandardReading get(Long id);
	
	/**
	 *   首页中可能存在 连载 和解读 共存的情况
	 */
	List<StandardReading> getStandardReadList(Integer pageNo, Integer pageSize2);
	
	boolean getCollect(Long memberID, Long standardID);

	ExpenseTotal getBuys(Long memberID, Long standardID);
	
	List<StandardReading> getStandardReadingList3(String standardCode,String type, int pageNo, Integer pageSize);

	Pager getStandardReadingList5(String type,int pageNo, int pageSize, String isRecommend);
	List<StandardReading> searchStandardReading(String type,Long standardReadingId);

	Pager getHisStandardReadList(int pageNo, int pageSize, Long expertId, String type);

	/**
	 *   获取连载中的解读列表
	 */
	List<StandardReading> getSubStandardList2(Long standardId);
    /**
     *  拿到连载中的附件信息
     */
	List<StandardReading> getSubStandardList3(Long standardId);

	List<StandardReading> getSubStandardList4(Long standardId);
	
	/**
	 * 根据解读id查询fahterID
	 */
	public List<StandardReading> getSubByfatherId(Long fatherID,String type);

	Share getShare();

	/**
	 * 根据关键字模糊查询标准解读
	 * @param searchKeyWord
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	List<StandardReading> getStandardReadingList4(String searchKeyWord, int pageNo, int pageSize);

	List<StandardReading> getStandardReadingListH5(int pageNo, int pageSize);
    /**
     * 首页的专栏订阅和查看更多
     */
	Pager getColumnList(int pageNo, int pageSize, String chiefShow);
    /**
     * 首页的精品文章和查看更多
     */
	Pager getExcellentArticleList(int pageNo, int pageSize, String chiefShow);

	/**
	 * 根据关键字模糊查询标准解读质量分享(单一)
	 * @param searchKeyWord
	 * @return
	 */
	List<StandardReading> getStandardReadListBySearchKeyWord(String searchKeyWord);

	/**
	 * 根据关键字模糊查询标准解读质量分享(连载)
	 * @param searchKeyWord
	 * @return
	 */
	List<StandardReading> getStandardReadSerialise(String searchKeyWord);

	/**
	 * 点击查看更多质量分享(单一)
	 * @param searchKeyWord
	 * @return
	 */
	List<StandardReading> getMoreStandardRead(String searchKeyWord, String qualityId, int pageNo,
			int pageSize);

	/**
	 * 
	 * @param searchKeyWord
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	List<StandardReading> getStandardReadSerialise(String searchKeyWord, int pageNo, int pageSize);

	/**
	 * 查询是否有购买记录
	 * @param memberId
	 * @param id
	 * @return
	 */
	ExpenseTotal getisBuyStand(Long memberId, Long id);

	ExpenseTotal getColumnBuyStatus(Long memberId, Long articleId);

}
