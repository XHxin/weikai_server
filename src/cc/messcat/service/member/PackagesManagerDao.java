package cc.messcat.service.member;

import java.util.List;

import cc.messcat.entity.Packages;
import cc.modules.commons.Pager;

public interface PackagesManagerDao {

	public abstract void addPackages(Packages packages);
	
	public abstract void modifyPackages(Packages packages);
	
	public abstract void removePackages(Packages packages);
	
	public abstract void removePackages(Long id);
	
	public abstract Packages retrievePackages(Long id);
	
	public abstract List retrieveAllPackagess();
	
	public abstract Pager retrievePackagessPager(int pageSize, int pageNo);
	
	public abstract Pager findPackagess(int i, int j, String s);
	
	
}