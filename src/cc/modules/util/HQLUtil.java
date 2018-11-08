package cc.modules.util;

import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HQLUtil {
	private static Logger log = LoggerFactory.getLogger(HQLUtil.class);

	/**
	 * 字符串转整数数组
	 * 
	 * @param ids
	 * @return
	 */
	public static Integer[] changeToIntegerArray(String ids) {
		String[] strIds = ids.split(",");
		Integer[] conditions = new Integer[strIds.length];
		for (int i = 0; i < strIds.length; i++) {
			conditions[i] = Integer.parseInt(strIds[i]);
		}
		return conditions;
	}

	/**
	 * 字符串转Long数组
	 * 
	 * @param ids
	 * @return
	 */
	public static Long[] changeToLongArray(String ids) {
		String[] strIds = ids.split(",");
		Long[] conditions = new Long[strIds.length];
		for (int i = 0; i < strIds.length; i++) {
			conditions[i] = Long.parseLong(strIds[i]);
		}
		return conditions;
	}

	/**
	 * 根据ids构造delete删除语句
	 * 
	 * @param entityName
	 * @param ids
	 * @return
	 */
	public static String createDeleteHQL(String entityName, String ids) {
		StringBuffer hqlStr = new StringBuffer("delete ").append(entityName).append(" as o where o.id in(");
		String[] strIds = ids.split(",");
		int i = 0;
		for (; i < strIds.length - 1; i++) {
			hqlStr.append("?,");
		}
		hqlStr.append("?)");
		return hqlStr.toString();
	}

	/**
	 * 根据条件构造delete删除语句
	 * 
	 * @param entityName
	 * @param attrNames
	 * @return
	 */
	public static String createDeleteHQL(String entityName, Set<String> attrNames) {
		StringBuffer hqlStr = new StringBuffer("delete ").append(entityName).append(" as o where ");
		Object[] attrNamesArray = attrNames.toArray();
		int i = 0;
		for (; i < attrNamesArray.length - 1; i++) {
			hqlStr.append("o.").append(attrNamesArray[i]).append("=? and ");
		}
		hqlStr.append("o.").append(attrNamesArray[i]).append("=?");
		return hqlStr.toString();
	}

	/**
	 * 根据条件构造deleteOr逻辑或删除语句
	 * 
	 * @param entityName
	 * @param attrNames
	 * @return
	 */
	public static String createDeleteOrHQL(String entityName, Set<String> attrNames) {
		StringBuffer hqlStr = new StringBuffer("delete ").append(entityName).append(" as o where ");
		Object[] attrNamesArray = attrNames.toArray();
		int i = 0;
		for (; i < attrNamesArray.length - 1; i++) {
			hqlStr.append("o.").append(attrNamesArray[i]).append("=? or ");
		}
		hqlStr.append("o.").append(attrNamesArray[i]).append("=?");
		return hqlStr.toString();
	}

	/**
	 * 根据条件构造update更新语句
	 * 
	 * @param entityName
	 * @param attrNames
	 * @return
	 */
	public static String createUpdateHQL(String entityName, Set<String> propNames, Set<String> attrNames) {
		StringBuffer hqlStr = new StringBuffer("update ").append(entityName).append(" as o set ");
		Object[] propNamesArray = propNames.toArray();
		int i = 0;
		for (; i < propNamesArray.length - 1; i++) {
			hqlStr.append("o.").append(propNamesArray[i]).append("=? , ");
		}
		hqlStr.append("o.").append(propNamesArray[i]).append("=? where ");
		Object[] attrNamesArray = attrNames.toArray();
		i = 0;
		for (; i < attrNamesArray.length - 1; i++) {
			hqlStr.append("o.").append(attrNamesArray[i]).append("=? and ");
		}
		hqlStr.append("o.").append(attrNamesArray[i]).append("=?");
		return hqlStr.toString();
	}

	/**
	 * 构造query查询语句
	 * 
	 * @param entityName
	 * @param ids
	 * @return
	 */
	public static String createQueryHQL(String entityName, String ids) {
		StringBuffer hqlStr = new StringBuffer("from ").append(entityName).append(" as o where o.id in(");
		String[] strIds = ids.split(",");
		int i = 0;
		for (; i < strIds.length - 1; i++) {
			hqlStr.append("?,");
		}
		hqlStr.append("?)");
		return hqlStr.toString();
	}

	/**
	 * 构造query查询语句
	 * 
	 * @param entityName
	 * @param attrNames
	 * @return
	 */
	public static String createQueryHQL(String entityName, Set<String> attrNames) {
		StringBuffer hqlStr = new StringBuffer("from ").append(entityName).append(" as o where ");
		Object[] attrNamesArray = attrNames.toArray();
		int i = 0;
		for (; i < attrNamesArray.length - 1; i++) {
			hqlStr.append("o.").append(attrNamesArray[i]).append("=? and ");
		}
		hqlStr.append("o.").append(attrNamesArray[i]).append("=?");
		return hqlStr.toString();
	}

	/**
	 * 构造query模糊查询语句
	 * 
	 * @param entityName
	 * @param attrNames
	 * @return
	 */
	public static String createLikeQueryHQL(String entityName, String likeAttr, Set<String>... attrNames) {
		StringBuffer hqlStr = new StringBuffer("from ").append(entityName).append(" as o where o.").append(likeAttr)
			.append(" like ?");
		if (attrNames.length > 0) {
			hqlStr.append(" and ");
			Object[] attrNamesArray = attrNames[0].toArray();
			int i = 0;
			for (; i < attrNamesArray.length - 1; i++) {
				hqlStr.append("o.").append(attrNamesArray[i]).append("=? and ");
			}
			hqlStr.append("o.").append(attrNamesArray[i]).append("=?");
		}
		return hqlStr.toString();
	}

	/**
	 * 构造queryTotalRecord查询语句
	 * 
	 * @param entityName
	 * @param attrNames
	 * @return
	 */
	public static String createQueryTotalRecordHQL(String entityName, Set<String>... attrNames) {
		StringBuffer hqlStr = new StringBuffer("select count(o) from ").append(entityName).append(" as o ");
		if (attrNames.length > 0) {
			hqlStr.append("where ");
			Object[] attrNamesArray = attrNames[0].toArray();
			int i = 0;
			for (; i < attrNamesArray.length - 1; i++) {
				hqlStr.append("o.").append(attrNamesArray[i]).append("=? and ");
			}
			hqlStr.append("o.").append(attrNamesArray[i]).append("=?");
		}
		return hqlStr.toString();
	}

	/**
	 * 构造queryList查询语句
	 * 
	 * @param entityName
	 * @param attrNames
	 * @return
	 */
	public static String createQueryListHQL(String entityName, String orderAttr, String orderType, Set<String>... attrNames) {
		StringBuffer hqlStr = new StringBuffer("from ").append(entityName).append(" as o ");
		if (attrNames.length > 0) {
			hqlStr.append("where ");
			Object[] attrNamesArray = attrNames[0].toArray();
			int i = 0;
			for (; i < attrNamesArray.length - 1; i++) {
				hqlStr.append("o.").append(attrNamesArray[i]).append("=? and ");
			}
			hqlStr.append("o.").append(attrNamesArray[i]).append("=? ");
		}
		hqlStr.append("order by o.").append(orderAttr).append(" ").append(orderType);
		log.info(hqlStr.toString());
		return hqlStr.toString();
	}

	/**
	 * @param attr
	 *            map条件
	 * @param likeAttr
	 *            需要用like的属性
	 * @return 查询条件
	 */
	public static StringBuffer createQueryWhereHQL(Map<String, Object> attr, Map<String, Object> likeAttrMap) {
		StringBuffer hqlStr = new StringBuffer();
		if (attr != null) {
			hqlStr.append(" where 1=1 ");
			if (attr != null && attr.size() > 0) {
				for (String key : attr.keySet()) {
					if (likeAttrMap != null && likeAttrMap.get(key) != null) {
						hqlStr.append(" and o.").append(key).append(" like '%" + attr.get(key) + "%' ");
					} else {
						hqlStr.append("and o.").append(key).append(" ='" + attr.get(key) + "' ");
					}
				}
			}
		}
		return hqlStr;
	}

	/**
	 * 构造queryTotalRecord查询语句
	 * 
	 * @param entityName
	 * @param attrNames
	 * @return
	 */
	public static StringBuffer createQueryTotalRecordHQL(String entityName, Map<String, Object> attr,
		Map<String, Object> likeAttrMap) {
		StringBuffer hqlStr = new StringBuffer("select count (*) from ").append(entityName).append(" as o ");
		if (attr != null) {
			hqlStr.append(" where 1=1 ");
			if (attr != null && attr.size() > 0) {
				for (String key : attr.keySet()) {
					if (likeAttrMap != null && likeAttrMap.get(key) != null) {
						hqlStr.append(" and o.").append(key).append(" like '%" + attr.get(key) + "%' ");
					} else {
						hqlStr.append("and o.").append(key).append(" ='" + attr.get(key) + "' ");
					}
				}
			}
		}
		return hqlStr;
	}

	/**
	 * 构造queryOrList查询or逻辑或语句
	 * 
	 * @param entityName
	 * @param attrNames
	 * @return
	 */
	public static String createQueryOrListHQL(String entityName, String orderAttr, String orderType, Set<String>... attrNames) {
		StringBuffer hqlStr = new StringBuffer("from ").append(entityName).append(" as o ");
		if (attrNames.length > 0) {
			hqlStr.append("where ");
			Object[] attrNamesArray = attrNames[0].toArray();
			int i = 0;
			for (; i < attrNamesArray.length - 1; i++) {
				hqlStr.append("o.").append(attrNamesArray[i]).append("=? or ");
			}
			hqlStr.append("o.").append(attrNamesArray[i]).append("=? ");
		}
		hqlStr.append("order by o.").append(orderAttr).append(" ").append(orderType);
		return hqlStr.toString();
	}

	/**
	 * 构造queryList查询语句,支持分组
	 * 
	 * @param entityName
	 * @param attrNames
	 * @return
	 */
	public static String createQueryListHQL(String entityName, String orderAttr, String orderType, String groupAttr,
		Set<String>... attrNames) {
		StringBuffer hqlStr = new StringBuffer("from ").append(entityName).append(" as o ");
		if (attrNames.length > 0) {
			hqlStr.append("where ");
			Object[] attrNamesArray = attrNames[0].toArray();
			int i = 0;
			for (; i < attrNamesArray.length - 1; i++) {
				hqlStr.append("o.").append(attrNamesArray[i]).append("=? and ");
			}
			hqlStr.append("o.").append(attrNamesArray[i]).append("=? ");
		}
		hqlStr.append("group by o.").append(groupAttr);
		hqlStr.append(" order by o.").append(orderAttr).append(" ").append(orderType);
		return hqlStr.toString();
	}

	/**
	 * 构造query查询语句
	 * 
	 * @param entityName
	 * @param ids
	 * @return
	 */
	public static String createQueryNotInHQL(String entityName, String ids) {
		StringBuffer hqlStr = new StringBuffer("from ").append(entityName).append(" as o where 1=1 ");
		if (null != ids && !ids.trim().equals("")) {
			hqlStr.append(" and o.id not in( ");
			String[] strIds = ids.split(",");
			int i = 0;
			for (; i < strIds.length - 1; i++) {
				hqlStr.append("?,");
			}
			hqlStr.append("?)");
		}
		return hqlStr.toString();
	}
}
