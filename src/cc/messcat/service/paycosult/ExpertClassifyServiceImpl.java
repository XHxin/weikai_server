package cc.messcat.service.paycosult;

import java.util.ArrayList;
import java.util.List;

import cc.messcat.bases.BaseManagerDaoImpl;
import cc.messcat.entity.ExpertClassify;
import cc.messcat.entity.Member;
import cc.messcat.vo.ExpertClassifyVo;
import cc.modules.commons.Pager;
import cc.modules.util.PropertiesFileReader;

@SuppressWarnings("serial")
public class ExpertClassifyServiceImpl extends BaseManagerDaoImpl implements ExpertClassifyService {
	
	private String defaultIcon= PropertiesFileReader.getByKey("default.icon.url");// 默认图标
	private static String jointUrl = PropertiesFileReader.getByKey("static.domain");// 图片拼接
	/*
	 * 获取推荐的专家分类列表
	 */
	@Override
	public List<ExpertClassifyVo> getList() {
		List<ExpertClassifyVo> resultList=new ArrayList<ExpertClassifyVo>();
		List<ExpertClassify> list=expertClassifyDao.getList();
		if(!list.isEmpty() && list.size()!=0){
			resultList=convert(list);
		}
		return resultList;
	}
	/*
	 * 获取全部的专家分类列表
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Pager getAllList(int pageNo, int pageSize) {
		List<ExpertClassifyVo> resultList=new ArrayList<ExpertClassifyVo>();
		Pager pager=expertClassifyDao.getAllList(pageNo,pageSize);
		List<ExpertClassify> list=pager.getResultList();
		if(!list.isEmpty() && list.size()!=0){
			resultList=convert(list);
		}
		return new Pager(pageSize,pageNo,pager.getRowCount(),resultList);
	}
	
	/*
	 * 公共方法,格式转换
	 */
	private List<ExpertClassifyVo> convert(List<ExpertClassify> list){
		List<ExpertClassifyVo> resultList=new ArrayList<ExpertClassifyVo>();
		for(ExpertClassify entity:list){
			ExpertClassifyVo vo=new ExpertClassifyVo();
			vo.setId(entity.getId());
			vo.setName(entity.getName());
			vo.setIcon(entity.getIcon()==null?defaultIcon:jointUrl+entity.getIcon());
			vo.setSort(entity.getSort());
			vo.setIsSelect("");   //此处不用显示是否被选中
			vo.setBlueIcon(entity.getBlueIcon()==null?defaultIcon:jointUrl+entity.getBlueIcon());
			resultList.add(vo);
		}
		return resultList;
	}
	
	/*
	 * 获取专家所属领域列表
	 */
	@Override
	public List<ExpertClassifyVo> getClassifyList(Long expertId) {
		List<ExpertClassifyVo> resultList=new ArrayList<ExpertClassifyVo>();
		// 获取被专家选中的分类列表
		Member member=expertClassifyDao.getMember(expertId);
		List<ExpertClassify> selectList=member.getClassifys();
		//获取全部的分类列表
		List<ExpertClassify> allList=expertClassifyDao.getAllClassify();
		for(ExpertClassify entity:allList){
			ExpertClassifyVo vo=new ExpertClassifyVo();
			vo.setId(entity.getId());
			vo.setIcon(jointUrl+entity.getIcon());
			vo.setName(entity.getName());
			vo.setSort(entity.getSort());
			vo.setBlueIcon(entity.getBlueIcon()==null?defaultIcon:jointUrl+entity.getBlueIcon());
			if(selectList.contains(entity)){
				vo.setIsSelect("true");
			}else{
				vo.setIsSelect("false");
			}
			resultList.add(vo);
		}
		return resultList;
	}
	
	/*
	 * 设置专家所属领域 
	 */
	@Override
	public void settingsField(String[] classifyIds,Long expertId) {
		Member member=expertClassifyDao.getMember(expertId);
        if(classifyIds!=null){
    		if(classifyIds.length>0){
    			List<ExpertClassify> list=new ArrayList<ExpertClassify>();
    			for(String strId:classifyIds){
    				ExpertClassify classify=expertClassifyDao.getExpertClassify(strId);
    				list.add(classify);
    			}
    			member.setClassifys(list);
    			expertClassifyDao.updateMembers(member);
    		}
        }
	}
}
