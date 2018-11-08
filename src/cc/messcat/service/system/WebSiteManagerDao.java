package cc.messcat.service.system;

import cc.messcat.entity.DivideScaleCommon;
import cc.messcat.entity.WebSite;

public interface WebSiteManagerDao {
	public WebSite getWebSite();

	public void addWebSite(WebSite website);

	public void updateWebSite(WebSite website);

	public void deleteWebSite(Long long1);

    DivideScaleCommon gainDivideScaleCommon();
}