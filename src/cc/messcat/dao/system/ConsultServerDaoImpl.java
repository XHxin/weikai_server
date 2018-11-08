package cc.messcat.dao.system;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import cc.messcat.bases.BaseDaoImpl;
import cc.messcat.entity.ConsultService;

public class ConsultServerDaoImpl extends BaseDaoImpl implements ConsultServerDao{

	@Override
	public ConsultService getConsultServer(String consultedEas) {
		Criteria criteria = getSession().createCriteria(ConsultService.class);
		criteria.add(Restrictions.eq("serverEas", consultedEas));
		List<ConsultService> list = criteria.list();
		if(!list.isEmpty() && list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public ConsultService getLeisureServer(String serverType) {
		Session session = this.getSession();
		String hql = "from ConsultService where receptionNum=(select min(receptionNum) from ConsultService where isOnline='1') and serverType=?";
		Query query = session.createQuery(hql);
		query.setParameter(0, serverType);
		List<ConsultService> list = query.list();
		if(!list.isEmpty() && list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public void updateConsultServer(ConsultService consultService) {
		getHibernateTemplate().update(consultService);
	}

	@Override
	public List<ConsultService> getConsultServerByModleType() {
		String hql = "FROM ConsultService WHERE receptionNum IN (SELECT MIN(receptionNum) FROM ConsultService GROUP BY serverType) AND isContactUs = 1 ";
		Query query = getSession().createQuery(hql);
		List<ConsultService> list = query.list();
		if(!list.isEmpty() && list.size()>0){
			return list;
		}
		return null;
	}

	@Override
	public List<ConsultService> getConsultServerList() {
		List<ConsultService> consultServiceList=getSession().createCriteria(ConsultService.class)
		    .add(Restrictions.eq("isSendMsg",1)).list();
		return consultServiceList;
	}
}
