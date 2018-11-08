
package cc.messcat.dao.system;

import cc.messcat.entity.DivideScaleCommon;
import cc.messcat.entity.WebSite;

public interface WebSiteDao {

	public WebSite get(Long long1);

	public void save(WebSite website);

	public void update(WebSite website);

	public void delete(WebSite website);

	public void delete(Long long1);

	public WebSite findWebSite();

    DivideScaleCommon gainDivideScaleCommon();
}