package cc.messcat.dao.system;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import cc.messcat.bases.BaseDaoImpl;
import cc.messcat.entity.Advertisement;
/**
 * @author nelson
 *
 */
public class AdvertisementDaoImpl extends BaseDaoImpl implements AdvertisementDao{

	
	@SuppressWarnings("unchecked")
	@Override
	public Advertisement getAdvertisement(int type) {
		String hql="";
		//type(0为启动页,1为弹窗)
		if(type==0) {
			hql="from Advertisement where status = 1 and type = " + type;
		}else if(type==1) {
			hql="from Advertisement where status = 1 and type = " + type;
		}
		Session session = this.getSession();
		Query query = session.createQuery(hql);
		List<Advertisement> advertisement = query.list();
		if(!advertisement.isEmpty()&&advertisement.size()>0){
			return advertisement.get(0);
		}
		return null;
	}

}
