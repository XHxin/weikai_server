package cc.messcat.service.paycosult;

import java.util.List;

import cc.messcat.vo.ExpertClassifyVo;
import cc.modules.commons.Pager;

public interface ExpertClassifyService {
    
	/*
	 * 获取推荐的分类列表
	 */
	List<ExpertClassifyVo> getList();
    /*
     * 获取全部分类列表
     */
	Pager getAllList(int pageNo, int pageSize);
	//设置专家所属领域
	void settingsField(String[] classifyIds, Long expertId);
	//获取专家所属领域列表
	List<ExpertClassifyVo> getClassifyList(Long expertId);

}
