package cc.messcat.service.standardread;

import java.util.List;

import cc.messcat.entity.Member;
import cc.messcat.vo.Adjunct;
import cc.messcat.vo.CatalogVo1;
import cc.messcat.vo.StandardReadListVo;
import cc.modules.commons.Pager;

public interface QualityShareManagerDao {

	Pager getQualityTypeList(Integer pageNo, Integer pageSize3);

	Pager getQualityTypeList2(int pageNo, int pageSize,String qualityId,String type,Member member);

	/**
	 *   查看单一解读
	 */
	List<Adjunct> getQualityShare(String qualityId,Long standardReadingId,String type);
    /*
     * 详情接口(标准解读,质量分享共享)
     */
	StandardReadListVo searchDetail(Long standardReadingId, Member member);

	List<Adjunct> getQualityShareH5(String qualityId, Long standardReadingId, String type);

	StandardReadListVo searchDetailH5(Long standardReadingId, Member member);

	Pager getQualityTypeList2H5(int pageNo, int pageSize, String qualityId, String type, Member member);

	List<CatalogVo1> getExpertH5(Long standardReadingId);

	Pager getArticelList(int pageNo, int pageSize, String qualityId, Long memberId);

	Pager getHisArticleList(int pageNo, int pageSize, Member member, Long expertId);

}
