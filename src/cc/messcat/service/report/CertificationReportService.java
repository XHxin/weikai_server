package cc.messcat.service.report;

import java.util.List;

import cc.messcat.entity.CertificationReport;

public interface CertificationReportService {

	
	//根据地区id,产品id查询机构（为了用机构的id去找到机构表对应的环信账号）
	List<CertificationReport> findCertificationReportByRidPid(Long regionId,Long productId);

}
