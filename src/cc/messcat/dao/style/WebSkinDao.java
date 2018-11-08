package cc.messcat.dao.style;

import java.util.List;

import cc.modules.commons.Pager;
import cc.messcat.entity.WebSkin;

public interface WebSkinDao {

	public WebSkin get(Long long1);

	public void save(WebSkin webSkin);

	public void update(WebSkin webSkin);

	public void delete(WebSkin webSkin);

	public void delete(Long long1);

	public List findAll();

	public boolean isNameUnique(String s);

	public Pager getObjectListByClass(int i, int j, Class class1, String s);

}