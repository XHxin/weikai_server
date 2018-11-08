package cc.messcat.dao.system;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import cc.messcat.bases.BaseDaoImpl;
import cc.messcat.entity.DivideScaleExpert;

public class ExpertDivideIntoDaoImpl extends BaseDaoImpl implements ExpertDivideIntoDao{

	@Override
	public DivideScaleExpert getDivideIntoByMemberId(Long memberId) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(DivideScaleExpert.class);
		criteria.add(Restrictions.eq("memberId", memberId));
		List<DivideScaleExpert> divideScaleExperts = criteria.list();
		if(!divideScaleExperts.isEmpty() && divideScaleExperts.size() >0) {
			return divideScaleExperts.get(0);
		}
		return null;
	}

}
