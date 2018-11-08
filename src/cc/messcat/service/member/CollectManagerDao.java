package cc.messcat.service.member;

import java.util.List;

import cc.messcat.entity.Collect;
import cc.messcat.entity.Member;
import cc.messcat.vo.CollectListVo;
import cc.messcat.vo.CollectVo;
import cc.modules.commons.Pager;

public interface CollectManagerDao{

	public abstract void addCollect(Collect collect);
	
	Object addCollection(Collect collect,Member member);
	
	public abstract void modifyCollect(Collect collect);
	
	public abstract void removeCollect(Collect collect);
	
	public abstract void removeCollect(Long id);
	
	public abstract Collect retrieveCollect(Long id);
	
	public abstract List retrieveAllCollects();
	
	public abstract Pager retrieveCollectsPager(int pageSize, int pageNo);
	
	public abstract Pager findCollects(int i, int j, String s);
	
	/**
	 * 根据地区ID和产品ID查询(用于产品输入模糊查询时)
	 */
	public Collect getStandardByConSimple(Long regionId, Long productId, Long memberId);
	
	/**
	 * 根据会员ID查询
	 */
	public CollectListVo getStandardByCon(Long memberId, int pageNo, int pageSize, String type);

	/**
	 * 根据会员id与视频id，查询该会员是否有收藏该视频
	 * @param memberId
	 * @param videoId
	 * @return
	 */
	public abstract Collect getCollect(Long memberId, Long videoId);

	/**
	 * 根据会员id查询该会员的收藏总数
	 * @param memberId
	 */
	public abstract int findAllCollectByMemberId(Long memberId);

	public abstract boolean getCollect(Long memberId, Long videoId, String type);

	public abstract List<CollectVo> getStandardByCon(Long memberId, int pageNo, int pageSize);
	
	
}