package cc.messcat.dao.style;

import java.util.List;

import cc.modules.commons.Pager;
import cc.messcat.entity.WebSkinColor;

public interface WebSkinColorDao {

	public WebSkinColor get(Long long1);

	public void save(WebSkinColor webSkinColor);

	public void update(WebSkinColor webSkinColor);

	public void delete(WebSkinColor webSkinColor);

	public void delete(Long long1);

	public List findAll();

	public List findAllByWebSkinId(Long webSkinId);

	public boolean isNameUnique(String s);

	public Pager getObjectListByClass(int i, int j, Class class1, String s);

	public WebSkinColor getWebSkinColorByWebSkinId(Long webSkinId);

}