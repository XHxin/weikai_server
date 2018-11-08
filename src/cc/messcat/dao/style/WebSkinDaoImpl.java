package cc.messcat.dao.style;

import java.util.List;

import org.hibernate.FlushMode;

import cc.messcat.bases.BaseDaoImpl;
import cc.messcat.entity.WebSite;
import cc.messcat.entity.WebSkin;

public class WebSkinDaoImpl extends BaseDaoImpl implements WebSkinDao {

	public WebSkinDaoImpl() {
	}

	public void delete(WebSkin webSkin) {
		getHibernateTemplate().delete(webSkin);
	}

	public void delete(Long id) {
		WebSkin webSkin = this.get(id);
		for (WebSite webSite : webSkin.getWebSites()) {
			webSite.getWebSkins().remove(webSkin);
		}
		getHibernateTemplate().delete(webSkin);

	}

	public List findAll() {
		List find = getHibernateTemplate().find("from WebSkin");
		return find;
	}

	public WebSkin get(Long id) {
		return (WebSkin) getHibernateTemplate().get(WebSkin.class, id);
	}

	public void save(WebSkin webSkin) {
		getSessionFactory().getCurrentSession().setFlushMode(FlushMode.AUTO);
		getHibernateTemplate().save(webSkin);
	}

	public void update(WebSkin webSkin) {
		getSessionFactory().getCurrentSession().clear();
		getHibernateTemplate().saveOrUpdate(webSkin);
	}

	public boolean isNameUnique(String names) {
		List temp = getHibernateTemplate().find("from WebSkin where names = ?", names.trim());
		return temp.size() <= 0;
	}
}