package cc.messcat.dao.standardread;

import java.util.List;

import cc.messcat.entity.ExpenseTotal;
import cc.messcat.entity.Member;
import cc.messcat.entity.StandardReading;
import cc.messcat.entity.StandardReadingCatalog;
import cc.messcat.vo.QualityTypeListVo;
import cc.modules.commons.Pager;

public interface QualityShareDao {

	List<QualityTypeListVo> getQualityTypeList(Integer pageNo, Integer pageSize3);

	Pager getQualityTypeList2(int pageNo, int pageSize,String qualityId,String type);

	Member getMember(Long memberId);

	boolean getCollect(Long memberId,Long standardId);

	ExpenseTotal getBuys(Long memberId, Long standardId);

	List<StandardReading> getSubQualityShare(Long standardId, Long memberID);

	/**
	 *   获取连载里面的单一解读
	 */
	List<StandardReading> getQualityShare(Long standardId,String qualityId);
    /**
     *    拿到连载里面的附件信息
     */
	List<StandardReading> getQualityShare2(Long standardId, String qualityId);

	List<StandardReading> getQualityShare3(Long standardId, String qualityId);

	StandardReading searchDetail(Long standardReadingId);
    /*
     * 获取分享的URL
     */
	String getShareURL();

	List<StandardReadingCatalog> getFirstLevel(Long standardReadingId);

	List<StandardReadingCatalog> getSecondLevel(Long standardReadingId, Long id);

	List<StandardReadingCatalog> getThreeLevel(Long standardReadingId, Long id);

	Pager getArticleList(int pageNo, int pageSize, String qualityId);

	Pager getHisArticleList(int pageNo, int pageSize, Long expertId);

	int getIsAttention(Member puMember, Member member);

}
