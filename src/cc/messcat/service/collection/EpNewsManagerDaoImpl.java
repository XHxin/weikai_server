package cc.messcat.service.collection;

import java.util.ArrayList;
import java.util.List;

import cc.messcat.bases.BaseManagerDaoImpl;
import cc.messcat.entity.Collect;
import cc.messcat.entity.EnterpriseColumn;
import cc.messcat.entity.EnterpriseNews;
import cc.messcat.entity.IndexCarousel;
import cc.messcat.entity.Member;
import cc.messcat.vo.EnterpriseNewsListVo;
import cc.messcat.vo.EnterpriseNewsVo;
import cc.messcat.vo.FrontColumnNewsVo;
import cc.messcat.vo.IndexCarouselVo;
import cc.modules.commons.Pager;
import cc.modules.util.NewsUtil;
import cc.modules.util.ObjValid;
import cc.modules.util.PropertiesFileReader;
@SuppressWarnings("serial")
public class EpNewsManagerDaoImpl extends BaseManagerDaoImpl implements EpNewsManagerDao {
	private static String defaultPhoto = PropertiesFileReader.getByKey("default.photo.url");// 默认会员头像
	private static String jointUrl = PropertiesFileReader.getByKey("static.domain");// 图片拼接
	/**
	 * 查看新闻 根据ID
	 */
	public EnterpriseNewsVo getEpNews(Long id,Long memberId) {
		EnterpriseNews en = epNewsDao.get(id);
		boolean isCollect = collectDao.getCollect(memberId, en.getId(), "5");
		EnterpriseNewsVo env = NewsUtil.setEnterpriseNewsVoInfo(en,isCollect);
		return env;
	}

	
	@SuppressWarnings("unchecked")
	public EnterpriseNewsListVo getEpNewsByColumnId(Integer pageSize,Integer pageNo,Long columnId,Member member){
		EnterpriseNewsListVo listVo = new EnterpriseNewsListVo();
		StringBuffer SQL = new StringBuffer(" where 1 = 1 ");
		SQL = SQL.append(" and enterpriseColumn.id = ").append(columnId);
		List<EnterpriseNews> result = epNewsDao.findNewsByWhere(SQL.toString());
		List<EnterpriseNewsVo> result0 = new ArrayList<EnterpriseNewsVo>();
		for(EnterpriseNews en:result){
			boolean isCollect = false;
			if(member != null) {
				isCollect = collectDao.getCollect(member.getId(), en.getId(), "5");
			}
			EnterpriseNewsVo env = NewsUtil.setEnterpriseNewsVoInfo(en,isCollect);
			env.setStatus(0);   //没有此会员信息,默认为未收藏状态
			if(null!=member){
				Collect c = epNewsDao.getByTypeAndRelatedId(5, en.getId(), member.getId());
				if(null!=c){
					env.setStatus(1);
				}
				
			}
			result0.add(env);
		}
		int rowCount = result0.size();
		int startIndex = pageSize * (pageNo - 1);
		int count = (int) rowCount;
		if(startIndex > count) {
			result0 = new ArrayList<EnterpriseNewsVo>();
		}else{
			result0 = result0.subList(startIndex, (pageSize + startIndex) <= rowCount ? (pageSize + startIndex) : rowCount);
		}
		listVo.setPageNo(pageNo);
		listVo.setPageSize(pageSize);
		listVo.setRowCount(rowCount);
		if(result0!=null){
			listVo.setEnterpriseNewsVo(result0);
		}
		return listVo;
	}


	@Override
	public List<EnterpriseNewsVo> listEnterpriseNews(Integer pageSize,String frontNum,Member member) {
		EnterpriseColumn column = epColumnDao.getColumnByFrontNum(frontNum);
		StringBuffer SQL = new StringBuffer(" where 1 = 1 ");
		SQL = SQL.append(" and enterpriseColumn.id = ").append(column.getId());
		List<EnterpriseNews> result = epNewsDao.findNewsByWhere(SQL.toString());
		List<EnterpriseNewsVo> result0 = new ArrayList<EnterpriseNewsVo>();
		for(EnterpriseNews en:result){
			boolean isCollect = false;
			if(member != null) {
				isCollect = collectDao.getCollect(member.getId(), en.getId(), "5");
			}
			EnterpriseNewsVo env = NewsUtil.setEnterpriseNewsVoInfo(en,isCollect);
			env.setStatus(0);
			if(null!=member){
				Collect c = epNewsDao.getByTypeAndRelatedId(5, en.getId(), member.getId());
				if(null!=c){
					env.setStatus(1);
				}
				
			}
			if(en.getEnterpriseColumn().getId()==17){
				env.setIsPara(en.getIsPara());
			}
			result0.add(env);
		}
		int rowCount = result0.size();
		Pager pager = new Pager(pageSize, 1, rowCount, result0);
		return pager.getResultList();
	}


	@SuppressWarnings("unchecked")
	@Override
	public FrontColumnNewsVo listFrontColumnNewsVo(Integer pageSize, Integer pageNo, String frontNum,Member member) {
		EnterpriseColumn column = epColumnDao.getColumnByFrontNum(frontNum);
		StringBuffer SQL = new StringBuffer(" where 1 = 1 ");
		SQL = SQL.append(" and enterpriseColumn.id = ").append(column.getId());
		List<EnterpriseNews> result = epNewsDao.findNewsByWhere(SQL.toString());
		int rowCount = result.size();
		if (rowCount < pageSize)
			pageNo = 1;
		int startIndex = pageSize * (pageNo - 1);
		result = result.subList(startIndex, (pageSize + startIndex) <= result.size() ? (pageSize + startIndex) : result.size());
		
		FrontColumnNewsVo fcn = new FrontColumnNewsVo();
		fcn.setColumnId(column.getId());
		fcn.setColumnName(column.getShortName());
		List<EnterpriseNewsVo> result0 = new ArrayList<EnterpriseNewsVo>();
		for(EnterpriseNews en:result){
			boolean isCollect = false;
			if(member != null) {
				isCollect = collectDao.getCollect(member.getId(), en.getId(), "5");
			}
			EnterpriseNewsVo env = NewsUtil.setEnterpriseNewsVoInfo(en,isCollect);
			env.setStatus(0);   //没有此会员信息,默认为未收藏状态
			if(null!=member){
				Collect c = epNewsDao.getByTypeAndRelatedId(5, en.getId(), member.getId());
				if(null!=c){
					env.setStatus(1);
				}
				
			}
			result0.add(env);
		}
		Pager pager = new Pager(pageSize, 1, rowCount, result0);
		fcn.setNews(pager.getResultList());
		return fcn;
	}
	
	/*
	 * 根据栏目模块名称查询
	 */
	public EnterpriseNews getByFrontNum(String frontNum){
		StringBuffer sb = new StringBuffer("where 1=1 ");
		sb.append(" and enterpriseColumn.frontNum = '").append(frontNum).append("' ");
		List<EnterpriseNews> list = epNewsDao.findNewsByWhere(sb.toString());
		if(ObjValid.isValid(list)){
			return list.get(0);
		}
		return null;
	}


	@Override
	public List<IndexCarouselVo> getCarousel() {
		List<IndexCarousel> carousels = epNewsDao.getCarousel();
		if(carousels != null) {
			List<IndexCarouselVo> carouselVos = new ArrayList<>();
			for (IndexCarousel indexCarousel : carousels) {
				IndexCarouselVo vo = new IndexCarouselVo();
				vo.setId(indexCarousel.getId());
				if(ObjValid.isValid(indexCarousel.getPhoto())){
					vo.setPhoto(jointUrl+indexCarousel.getPhoto());
				}else{
					vo.setPhoto(defaultPhoto);
				}
				vo.setShareUrl(indexCarousel.getShareUrl());
				vo.setStatus(indexCarousel.getStatus());
				vo.setTitle(indexCarousel.getTitle());
				String str=indexCarousel.getUri();
				/**
				 * 直播转录播时， uri自动把直播(live) 转为 课程(course)
				 */
//				if(str.contains("live")==true) {  //判断uri中是否含有live
//					if(str.contains("&")){
//						String [] strArray=str.split("&"); //把字符串按照 & 去拆分
//						for(String s:strArray){
//							if(s.contains("id")){  //找到含有id的字符串片段
//								LiveVideo video=liveDao.getLiveVideoById(Long.valueOf(s.replace("id=","")));  //截取出id值
//								if(video!=null){
//									if(!"0".equals(video.getVideoType())) {
//										vo.setUri(str.replace("live", "course"));
//									}else {
//										vo.setUri(indexCarousel.getUri());
//									}
//								}
//							}
//						}
//					}
//				}else {
					vo.setUri(indexCarousel.getUri());	
//				}
				vo.setIsPara(indexCarousel.getIsPara());
				carouselVos.add(vo);
			}
			return carouselVos;
		}
		return null;
	}

}