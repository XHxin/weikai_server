package cc.messcat.service.report;

import java.util.List;

import cc.messcat.entity.CertificationAuthority;
import cc.modules.commons.Pager;

public interface CertificationAuthorityManagerDao {

	
	public abstract CertificationAuthority retrieveCertificationAuthority(Long id);
	
	public abstract List retrieveAllCertificationAuthoritys();
	
	public abstract Pager retrieveCertificationAuthoritysPager(int pageSize, int pageNo);
	
	public abstract Pager findCertificationAuthoritys(int i, int j, String s);

	public abstract CertificationAuthority findCertificationAuthorityByid(Long certificationid);
	
	
}