package cc.messcat.service.member;

import java.util.List;
import cc.modules.commons.Pager;
import cc.messcat.entity.Packages;
import cc.messcat.bases.BaseManagerDaoImpl;

public class PackagesManagerDaoImpl extends BaseManagerDaoImpl implements PackagesManagerDao {

	private static final long serialVersionUID = 1L;


	public PackagesManagerDaoImpl() {
	}

	public void addPackages(Packages packages) {
		this.packagesDao.save(packages);
	}
	
	public void modifyPackages(Packages packages) {
		this.packagesDao.update(packages);
	}
	
	public void removePackages(Packages packages) {
		this.packagesDao.delete(packages);
	}

	public void removePackages(Long id) {
		this.packagesDao.delete(id);
	}
	
	public Packages retrievePackages(Long id) {
		return (Packages) this.packagesDao.get(id);
	}

	public List retrieveAllPackagess() {
		return this.packagesDao.findAll();
	}
	
	public Pager retrievePackagessPager(int pageSize, int pageNo) {
		return this.packagesDao.getPager(pageSize, pageNo);
	}
	
	public Pager findPackagess(int pageSize, int pageNo, String statu) {
		Pager pager = packagesDao.getObjectListByClass(pageSize, pageNo, Packages.class, statu);
		return pager;
	}


}