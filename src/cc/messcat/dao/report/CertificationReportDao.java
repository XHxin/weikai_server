package cc.messcat.dao.report;

import java.util.List;

import cc.messcat.entity.CertificationReport;

public interface CertificationReportDao {

	List<CertificationReport> findCertificationReportByRidPid(Long regionId, Long productId);

}
