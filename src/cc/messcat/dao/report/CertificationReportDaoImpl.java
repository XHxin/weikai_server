package cc.messcat.dao.report;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import cc.messcat.bases.BaseDaoImpl;
import cc.messcat.entity.CertificationReport;

public class CertificationReportDaoImpl extends BaseDaoImpl  implements CertificationReportDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<CertificationReport> findCertificationReportByRidPid(Long regionId, Long productId) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(CertificationReport.class);
		if(regionId == null && productId == null){
			criteria.add(Restrictions.eq("isGlobal", "1"));
		}else{
			criteria.add(Restrictions.eq("isGlobal", "0"));
			criteria.add(Restrictions.eq("regionId", regionId));
			criteria.add(Restrictions.eq("productId", productId));
		}
		
		List<CertificationReport> certificationReports = criteria.list();
		return certificationReports;
	}

}
