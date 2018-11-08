package cc.modules.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BeanUtil {

	/**
	 * 把obj1里的值赋给obj0，然后返回obj0，excludeFields是不需要赋值的属性组合
	 * 
	 * @param obj0
	 * @param obj1
	 * @param excludeFields
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Object setBeanByOtherBean(Object obj0, Object obj1, String[] excludeFields) throws Exception {

		// 把排除属性放入set里
		Set excludeFieldsSet = new HashSet();
		if (excludeFields != null && excludeFields.length > 0) {
			for (String ef : excludeFields) {
				excludeFieldsSet.add(ef);
			}
		}

		// 把未排除的属性放入list里
		List<String> fieldList = new ArrayList<String>();
		Field[] fields = obj0.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			String fieldName = field.getName();
			if (!excludeFieldsSet.contains(fieldName)) {
				fieldList.add(fieldName);
			}

		}

		// 循环属性list进行赋值
		int size = fieldList.size();
		for (int i = 0; i < size; i++) {
			String fieldName = fieldList.get(i);
			String firstLetter = fieldName.substring(0, 1).toUpperCase();
			String getter = "get" + firstLetter + fieldName.substring(1);
			String setter = "set" + firstLetter + fieldName.substring(1);
			Method getterMethod = obj1.getClass().getMethod(getter, new Class[] {});
			Method setterMethod = obj0.getClass().getMethod(setter, getterMethod.getReturnType());
			Object value = getterMethod.invoke(obj1, new Object[] {});
			setterMethod.invoke(obj0, new Object[] { value });
		}

		return obj0;
	}

}
