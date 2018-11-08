package cc.messcat.dao.collection;

import java.util.List;

import cc.messcat.entity.Collect;
import cc.messcat.entity.EnterpriseNews;
import cc.messcat.entity.IndexCarousel;

public interface EpNewsDao {
	
	public EnterpriseNews get(Long id);

	public List findNewsByWhere(String where);
	
	Collect getByTypeAndRelatedId(Integer type,Long id,Long memberId);

	public List<IndexCarousel> getCarousel();
}