package cc.modules.util;

import java.util.List;

/**
 * @author Michael
 * @version 1.0
 */
public class CollectionUtil {

	@SuppressWarnings("unchecked")
	public static boolean isListEmpty(List list) {
		if (list == null || list.isEmpty()) {
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public static boolean isListNotEmpty(List list) {
		if (list != null && !list.isEmpty()) {
			return true;
		}
		return false;
	}

}
