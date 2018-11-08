package cc.modules.util;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 此工具类用于把entity封装成vo
 * 使用此工具类的前提：entity与vo的属性名与类型保持一致
 * @author HASEE
 *
 */
public class PackageUtil {

	/**
	 * @param vo 目标vo
	 * @param entity 需要处理的实体
	 * @return
	 */
	public static Object packageEntity(Object vo, Object entity) {
		Field[] voProperties = vo.getClass().getDeclaredFields();
		Field[] enProperties = entity.getClass().getDeclaredFields();
		for (Field vofield : voProperties) {//vo的属性字段较少，所以放在外循环
			vofield.setAccessible(true);
			for (Field enField : enProperties) {
				enField.setAccessible(true);
				if(vofield.getName().equals(enField.getName())) {
					try {
						vofield.set(vo, enField.get(entity));
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return vo;
	}
}
