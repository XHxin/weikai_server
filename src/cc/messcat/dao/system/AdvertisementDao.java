package cc.messcat.dao.system;

import cc.messcat.bases.BaseDao;
import cc.messcat.entity.Advertisement;

/**
 * @author nelson
 *	返回广告的接口
 */
public interface AdvertisementDao extends BaseDao{

	/**
	 * 查询当前最新的广告
	 */
	public abstract Advertisement getAdvertisement(int paramInt);

}
