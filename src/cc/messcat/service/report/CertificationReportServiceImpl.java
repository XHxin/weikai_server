package cc.messcat.service.report;

import java.util.List;

import cc.messcat.bases.BaseManagerDaoImpl;
import cc.messcat.entity.CertificationReport;

@SuppressWarnings("serial")
public class CertificationReportServiceImpl extends BaseManagerDaoImpl implements CertificationReportService {

	@Override
	public List<CertificationReport> findCertificationReportByRidPid(Long regionId, Long productId) {
		return certificationReportDao.findCertificationReportByRidPid(regionId, productId);
	}

}
