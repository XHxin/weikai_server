package cc.messcat.dao.standardread;

import org.hibernate.FlushMode;

import cc.messcat.bases.BaseDaoImpl;
import cc.messcat.entity.StandardMade;

public class StandardMadeDaoImpl extends BaseDaoImpl implements StandardMadeDao {

	public void commit(StandardMade standardMade) {
		getSession().setFlushMode(FlushMode.AUTO);
		getSession().flush();
		getHibernateTemplate().save(standardMade);
	}
}
