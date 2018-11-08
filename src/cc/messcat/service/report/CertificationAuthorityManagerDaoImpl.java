package cc.messcat.service.report;

import java.util.List;
import cc.modules.commons.Pager;
import cc.messcat.entity.CertificationAuthority;
import cc.messcat.bases.BaseManagerDaoImpl;

public class CertificationAuthorityManagerDaoImpl extends BaseManagerDaoImpl implements CertificationAuthorityManagerDao {

	private static final long serialVersionUID = 1L;

	public CertificationAuthorityManagerDaoImpl() {
	}

	
	public CertificationAuthority retrieveCertificationAuthority(Long id) {
		return (CertificationAuthority) this.certificationAuthorityDao.get(id);
	}

	public List retrieveAllCertificationAuthoritys() {
		return this.certificationAuthorityDao.findAll();
	}
	
	public Pager retrieveCertificationAuthoritysPager(int pageSize, int pageNo) {
		return this.certificationAuthorityDao.getPager(pageSize, pageNo);
	}
	
	public Pager findCertificationAuthoritys(int pageSize, int pageNo, String statu) {
		Pager pager = certificationAuthorityDao.getObjectListByClass(pageSize, pageNo, CertificationAuthority.class, statu);
		return pager;
	}

	@Override
	public CertificationAuthority findCertificationAuthorityByid(Long certificationid) {
		return this.certificationAuthorityDao.findCertificationAuthorityByid(certificationid);
	}


}