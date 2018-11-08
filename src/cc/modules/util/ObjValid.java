package cc.modules.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * obj验证条件集合类
 * @author Andyxixi
 *
 */
public class ObjValid {
	
	private static StringBuilder fieldName = new StringBuilder();

	/**
	 * 判断条件是否有效
	 * @param obj
	 */
	public static boolean isValid(Object... objs) {
		boolean isValid = true;
		if (null != objs) {
			for (int i = 0; i < objs.length; i++) {
				if(null == objs[i]  ) {
					isValid = false;
				}
				if(objs[i] instanceof String) {
					String key = (String)objs[i];
					if(key.trim().equals("")) {
						isValid = false;
					}
					if(key.trim().equals("-1")) {
						isValid = false;
					}
				} else if(objs[i] instanceof Integer) {
					Integer key = (Integer)objs[i];
					if(key.intValue()<=0) {
						isValid = false;
					}
				} else if(objs[i] instanceof Double) {
					Double key = (Double)objs[i];
					if(key.doubleValue()<=0) {
						isValid = false;
					}
				} else if(objs[i] instanceof Byte) {
					Byte key = (Byte)objs[i];
					if(key.byteValue() < 0) {
						isValid = false;
					}
				}else if (objs[i] instanceof Long) {
					Long key = (Long)objs[i];
					if(key.longValue() <= 0) {
						isValid = false;
					}
				}else if (objs[i] instanceof List<?>) {
					List<?> key = (List<?>)objs[i];
					if(key.size() <= 0) {
						isValid = false;
					}
				}else if (objs[i] instanceof JSONArray) {
					JSONArray key = (JSONArray)objs[i];
					if(key.size() <= 0) {
						isValid = false;
					}
				}else if (objs[i] instanceof JSONObject) {
					JSONObject key = (JSONObject)objs[i];
					if(key.size() <= 0) {
						isValid = false;
					}
				}
			}
		}else {
			isValid = false;
		}
		return isValid;
	}
	
	/**
	 * 根据路径获取对象里头该路径的方面，并对该值进行判断
	 * @param obj
	 * @param path
	 * @return
	 * @author Andy Lin
	 */
	@SuppressWarnings("unchecked")
	public static boolean isValid(Object obj, String path) throws Exception{
		
		// 路径非空
		if(ObjValid.isValid(path)){
			
			// 把路径分段
			String[] paths = path.split("\\.");
			
			// 先判空当前对象
			if(ObjValid.isValid(obj)){
				
				// 当前对象
				Object currentObj = obj;
				
				//判断是否为最后一次循环
				boolean flag = true;
				
				// 循环判断路径中的方法获取的对象是否为空
				for (int i = 1; i < paths.length; i++) {
					
					String methodName = paths[i].substring(0, paths[i].indexOf("("));
					
					// 通过反射方式获取该对象的下一个方法对象
					Class objClass = currentObj.getClass();
					Method method = objClass.getMethod(methodName, null);
					currentObj = method.invoke(currentObj, null);
					
					if(!ObjValid.isValid(currentObj)){
						flag = false;
						break;
					}
				}
				
				return flag;
			}
		}
		return false;
	}
	
	/**
	 * 该方法主要思路为：循环一个对象（object）的属性，并将非空的属性名和值存入map中，其中包括属性比如：
	 * 
	 * @param map
	 * 				存值的map
	 * @param object
	 * 				需要转换的对象
	 * @param isField
	 * 				是否为该对象object的属性
	 * @param childfieldName
	 * 				该对象object的属性名称
	 * 
	 * @author Silver
	 * 
	 * @return
	 * 
	 * 		转换对象为map
	 */
	public static Map<String,Object> objectToMap(Map<String, Object> map, Object object, boolean isField, String childfieldName){
		if (null!=object) {
			if (null==map) {
				map = new HashMap<String, Object>();
			}
			
			if (null!=childfieldName) {   //构建A.B类型的字符串
				childfieldName += ".";
			}else {
				childfieldName = "";
			}
			//循环每个对象的变量
			for(Field f : object.getClass().getDeclaredFields()){
				if(!f.isAccessible()){
					f.setAccessible(true);
				}
				Object o = null;
				try {
					//获取object对象中当前循环到的变量
					o = f.get(object);			
					//排除变量为空的情况
					if (null==o || o.equals("")) {
						continue;
					}
					//排除值为serialVersionUID的变量
					if ("serialVersionUID".equals(f.getName())) {
						continue;
					}
					//如果isField为true，构建属性字符串
					if (isField) {
						fieldName.append(childfieldName);
					}
					//如果是javaBean类型
					if (3==getClassType(f.getType().getName())) { 
						childfieldName += f.getName();             //构建A.B类型的字符串
						fieldName.setLength(0);
						
						objectToMap(map,getFieldValueByNameForJB(f.getName(),object), true, childfieldName);
						continue;
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				fieldName.append(f.getName());
				//将属性名称和值存入map中；
				map.put(fieldName.toString(), o==null?"":o.toString());
				//清零，重新构建下一个变量的字符串
				fieldName.setLength(0);          
			}
		}
		return map;
	}
	
	/**
	 * 查看该字段是什么类型
	 * 
	 * 0：基本类型，1：集合类型，2：日期类型，值需要转换，3：其它JavaBean类型
	 * 
	 * @param classType
	 * @return
	 */
	public static int getClassType(String classType) {
		int reValue = 0;
		if ("int".equals(classType)) {
			reValue = 0;
		} else if ("byte".equals(classType)) {
			reValue = 0;
		} else if ("short".equals(classType)) {
			reValue = 0;
		} else if ("boolean".equals(classType)) {
			reValue = 0;
		} else if ("float".equals(classType)) {
			reValue = 0;
		} else if ("long".equals(classType)) {
			reValue = 0;
		} else if ("double".equals(classType)) {
			reValue = 0;
		} else if ("java.lang.String".equals(classType)) {
			reValue = 0;
		} else if ("java.lang.Integer".equals(classType)) {
			reValue = 0;
		} else if ("java.lang.Byte".equals(classType)) {
			reValue = 0;
		} else if ("java.lang.Short".equals(classType)) {
			reValue = 0;
		} else if ("java.lang.Boolean".equals(classType)) {
			reValue = 0;
		} else if ("java.lang.Float".equals(classType)) {
			reValue = 0;
		} else if ("java.lang.Long".equals(classType)) {
			reValue = 0;
		} else if ("java.lang.Double".equals(classType)) {
			reValue = 0;
		} else if ("java.util.Set".equals(classType)) {
			reValue = 1;
		} else if ("java.util.List".equals(classType)) {
			reValue = 1;
		} else if ("java.util.Map".equals(classType)) {
			reValue = 1;
		} else if ("java.util.Date".equals(classType)) {
			reValue = 2;
		} else {
			reValue = 3;
		}
		return reValue;
	}
	
	/**
	 * 该字段是JavaBean类型
	 * 
	 * @param fieldName
	 *            String 属性名称
	 * @param obj
	 *            Object JavaBean对象
	 * @return
	 * @throws Exception
	 */
	public static Object getFieldValueByNameForJB(String fieldName, Object obj) throws Exception {
		String firstLetter = fieldName.substring(0, 1).toUpperCase();
		String getter = "get" + firstLetter + fieldName.substring(1);
		Method method = obj.getClass().getMethod(getter, new Class[] {});
		Object obj2 = method.invoke(obj, new Object[] {});
		return obj2;
	}
	
}
