package cc.messcat.dao.system;

import java.util.List;

import cc.messcat.bases.BaseDaoImpl;
import cc.messcat.entity.DivideScaleCommon;
import cc.messcat.entity.WebSite;
import cc.modules.util.CollectionUtil;

public class WebSiteDaoImpl extends BaseDaoImpl implements WebSiteDao {

	public WebSiteDaoImpl() {
	}

	@Override
	public void delete(WebSite webSite) {
		getHibernateTemplate().delete(webSite);
	}

	@Override
	public void delete(Long id) {
		getHibernateTemplate().delete(get(id));
	}

	@Override
	public WebSite get(Long id) {
		return (WebSite) getHibernateTemplate().get(WebSite.class, id);

	}

	@Override
	public void save(WebSite webSite) {
		getHibernateTemplate().save(webSite);
	}

	@Override
	public void update(WebSite webSite) {
		getHibernateTemplate().merge(webSite);
	}

	@Override
	public WebSite findWebSite() {
		List list = getHibernateTemplate().find("from WebSite");
		if (list != null){
			return (WebSite) list.get(0);
		}else{
			return null;
		}
	}

	@Override
	public DivideScaleCommon gainDivideScaleCommon() {
		List<DivideScaleCommon> list=getSession().createCriteria(DivideScaleCommon.class).list();
		if(CollectionUtil.isListNotEmpty(list)){
			return list.get(0);
		}
		return null;
	}
}