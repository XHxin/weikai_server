package cc.messcat.dao.member;

import java.util.List;

import cc.messcat.bases.BaseDao;
import cc.messcat.entity.Collect;
import cc.messcat.entity.Standard;
import cc.messcat.entity.StandardReading;
import cc.modules.commons.Pager;

public interface CollectDao extends BaseDao{

	public void save(Collect collect);
	
	public void update(Collect collect);
	
	public void delete(Collect collect);
	
	public void delete(Long id);
	
	public Collect get(Long id);
	
	public List findAll();
	
	public Pager getPager(int pageSize, int pageNo);
	
	public Pager getObjectListByClass(int i, int j, Class class1, String s);
	
	/**
	 * 根据条件查询
	 */
	public List<Collect> findByhql(String string);
	
	/**
	 * 根据条件查询收藏
	 */
	public List<Collect> getCollectByCon(Collect collect);
	
	Standard getByRefionIdAndProductId(Long regionId,Long productId);
	
	StandardReading getByStandardReadingIdAndType(Long id,String type);

	public boolean getCollect(Long memberId, Long readId, String type);

	public Collect getCollect(Long memberId, Long videoId);

	public int findAllCollectByMemberId(Long memberId);
	
}