package cc.messcat.dao.system;

import java.util.List;

import org.hibernate.Query;
import cc.messcat.bases.BaseDaoImpl;
import cc.messcat.entity.Versions;

public class WelcomeDaoImpl extends BaseDaoImpl implements WelcomeDao {

	@SuppressWarnings("unchecked")
	@Override
	public Versions getAllVersion(String terminal,Long versionCode) {
		Query query=getSession().createQuery("FROM Versions WHERE  terminal = ? AND Version_Code=? ORDER BY VERSION_CODE DESC");
		query.setParameter(0, terminal);
		query.setParameter(1, versionCode);
		List<Versions> list=query.list();
		if(!list.isEmpty()&&list.size()>0){
			return list.get(0);
		}
		return null;
	}



}
