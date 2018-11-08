package cc.messcat.service.collection;

import java.util.List;

import cc.messcat.entity.EnterpriseNews;
import cc.messcat.entity.Member;
import cc.messcat.vo.EnterpriseNewsListVo;
import cc.messcat.vo.EnterpriseNewsVo;
import cc.messcat.vo.FrontColumnNewsVo;
import cc.messcat.vo.IndexCarouselVo;
public interface EpNewsManagerDao {

	public EnterpriseNewsVo getEpNews(Long long1, Long memberId);
	
	public EnterpriseNewsListVo getEpNewsByColumnId(Integer pageSize,Integer pageNo,Long columnId,Member member);


	List<EnterpriseNewsVo> listEnterpriseNews(Integer pageSize,String frontNum,Member member); 
	
	FrontColumnNewsVo listFrontColumnNewsVo(Integer pageSize,Integer pageNo,String frontNum,Member member);
	
	/*
	 * 根据栏目模块名称查询
	 */
	public EnterpriseNews getByFrontNum(String frontNum);

	/**
	 * 获取轮播图
	 * @return
	 */
	public List<IndexCarouselVo> getCarousel();
}