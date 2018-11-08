package cc.modules.util;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ExcelHandler {

	/**
	 * @param fileName
	 *            String 文件路径
	 * @param data
	 *            Map 数据文件，类型是Map<List<Object>>，每个map是一个sheet，
	 *            sheet包含list，list的泛型类型是一个Object，利用反射机制获取值
	 */
	public static void exportExcel(String fileName, Map data) {

		// 数据为空就不要浪费时间了
		if (data == null) {
			return;
		}

		WritableWorkbook wwb;
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(fileName);
			wwb = Workbook.createWorkbook(fos);

			// 全局设置单元格的文字格式
			WritableCellFormat wcf = new WritableCellFormat();

			int sheetId = 0;
			for (Iterator dataItr = data.entrySet().iterator(); dataItr.hasNext();) {
				Entry sheetEntry = (Entry) dataItr.next();
				String sheetName = (String) sheetEntry.getKey();
				List dataList = (List) sheetEntry.getValue();

				// sheet不存在，循环吓一跳
				if (StringUtil.isBlank(sheetName)) {
					continue;
				}

				// 创建sheet
				WritableSheet ws = wwb.createSheet(sheetName, sheetId);

				// 数据为空则循环吓一跳
				if (dataList == null || dataList.isEmpty()) {
					continue;
				}

				// 操作数据
				int dataSize = dataList.size();
				for (int i = 0; i < dataSize; i++) {

					// 获取class
					Object javaBean = (Object) dataList.get(i);
					if (javaBean == null) {
						continue;
					}

					// 读取class里面的字段，然后根据字段去获取get方法
					Field[] fields = javaBean.getClass().getDeclaredFields();

					// 表头标题
					if (i == 0) {

						// 列表序号
						int columnId = 0;
						for (int j = 0; j < fields.length; j++) {
							Field field = fields[j];
							String fieldName = field.getName();
							if ("serialVersionUID".equals(fieldName)) {
								continue;
							}
							String fieldType = field.getType().getName();
							if (getClassType(fieldType) == 1) {
								continue;
							}
							ws.addCell(new Label(columnId++, 0, fieldName, wcf));
						}
					}

					// 数据
					int columnId = 0; // 列表序号
					for (int j = 0; j < fields.length; j++) {
						Field field = fields[j];
						String fieldName = field.getName();
						if ("serialVersionUID".equals(fieldName)) {
							continue;
						}

						// 获取字段的值
						String fieldType = field.getType().getName();
						String value = "";
						if (getClassType(fieldType) == 0) {
							Object valueObj = getFieldValueByName(fieldName, javaBean);
							value = String.valueOf(valueObj);
						} else if (getClassType(fieldType) == 1) {
							continue;
						} else if (getClassType(fieldType) == 2) {
							Object valueObj = (Object) getFieldValueByName(fieldName, javaBean);

							// 日期类型，需要转换
							value = String.valueOf(valueObj);
							if (StringUtil.isNotBlank(value)) {
								if (value.length() > 19) {
									value = value.substring(0, 19);
								} else if (value.length() > 10) {
									value = value.substring(0, 10);
								}
							}
						} else {
							Object valueObj = getFieldValueByNameForJB(fieldName, javaBean);
							value = String.valueOf(valueObj);
						}
						ws.addCell(new Label(columnId++, i + 1, value, wcf));
					}
				}

				++sheetId;
			}

			wwb.write();
			wwb.close();

		} catch (Exception e) {
		}
	}

	/**
	 * 从Excel文件里读取数据保存到Map里
	 * 
	 * @param fileName
	 *            Excel文件的名称
	 * @return Map 数据文件，类型是Map<List<Object>>，每个map是一个sheet，
	 *         sheet包含list，list的泛型类型是一个Object，利用反射机制设置值
	 */
	@SuppressWarnings("unchecked")
	public static Map readExcel(String fileName) throws Exception {
		Map data = new HashMap();
		String packageName = "cc.messcat.entity.";
		Workbook book = Workbook.getWorkbook(new File(fileName));
		String[] sheetNames = book.getSheetNames();
		for (int i = 0; i < sheetNames.length; i++) {

			// sheet的名称就是JavaBean的类名
			String sheetName = sheetNames[i];
			String className = packageName + sheetName;

			// 获取sheet
			Sheet sheet = book.getSheet(sheetName);
			int rows = sheet.getRows();

			// 表头的列序号与JavaBean属性名称的映射Map
			Map columnFieldName = new HashMap();

			// 循环行
			List dataList = new ArrayList();
			for (int rowIndex = 0; rowIndex < rows; rowIndex++) {

				// 获取行，一行包含N列，也就是Cell的数组
				Cell[] cell = sheet.getRow(rowIndex);

				// 约定第一行是表头，也就是JavaBean的属性名称，保存进columnFieldName
				if (rowIndex == 0) {
					for (int cellIndex = 0; cellIndex < cell.length; cellIndex++) {
						columnFieldName.put(cellIndex, cell[cellIndex].getContents());
					}
				}

				// 从第二行开始循环数据
				else {

					// 类名前面已经含有包名，就可以使用class获取该JavaBean的实例
					Object javaBean = Class.forName(className).newInstance();

					// 循环每列数据并set进JavaBean的属性里
					for (int cellIndex = 0; cellIndex < cell.length; cellIndex++) {

						// 格子里的值
						String value = cell[cellIndex].getContents();

						// 按照列的序号获取表头的列名或JavaBean的属性名称
						String fieldName = (String) columnFieldName.get(cellIndex);

						// 拿到JavaBean里某属性的setter方法名称
						String setter = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
						Method[] method0 = javaBean.getClass().getMethods();
						Class[] methodParaClass = null;

						// 循环找到setter里参数的类型
						for (int k = 0; k < method0.length; k++) {
							if (setter.equals(method0[k].getName())) {
								methodParaClass = method0[k].getParameterTypes();
								break;
							}
						}
						Method method = javaBean.getClass().getMethod(setter, methodParaClass);
						String methodParaClassName = methodParaClass[0].getName();

						// 如果参数类型是JavaBean类型，则设置JavaBean的id值即可
						if (getClassType(methodParaClassName) == 3) {
							if (StringUtil.isNotBlank(value) && !"null".equals(value)) {

								// 设置JavaBean的id值
								Object innerJavaBean = methodParaClass[0].newInstance();
								Method method1 = innerJavaBean.getClass().getMethod("setId", Long.class);
								method1.invoke(innerJavaBean, new Object[] { Long.parseLong(value) });

								// 再设置JavaBean对象到宿主JavaBean
								method.invoke(javaBean, new Object[] { innerJavaBean });
							}
						} else {

							// 从格子里拿到的值都是String，现在根据不同类型要转换为不同类型的值，并set进JavaBean里
							Object paraVal = getValueByClassType(methodParaClassName, value);
							method.invoke(javaBean, new Object[] { paraVal });
						}
					}

					// 一个JavaBean设置值完成后则保存进list里
					dataList.add(javaBean);
				}
			}

			// 一个sheet循环完后则保存进data里
			data.put(sheetName, dataList);
		}

		book.close();
		return data;
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
	 * 查看该字段是什么类型，并根据类型返回不同类型的值
	 * 
	 * @param classType
	 * @param value
	 * @return
	 */
	public static Object getValueByClassType(String classType, String value) throws Exception {
		Object reValue = null;

		// 如果是空，则基础数据要初始化，其它则返回null
		if (StringUtil.isBlank(value) || "null".equals(value)) {
			if ("int".equals(classType)) {
				reValue = 0;
			} else if ("byte".equals(classType)) {
				reValue = Byte.parseByte("0");
			} else if ("short".equals(classType)) {
				reValue = Short.parseShort("0");
			} else if ("float".equals(classType)) {
				reValue = Float.parseFloat("0.0");
			} else if ("long".equals(classType)) {
				reValue = Long.parseLong("0");
			} else if ("double".equals(classType)) {
				reValue = Double.parseDouble("0.0");
			} else if ("boolean".equals(classType)) {
				reValue = Boolean.valueOf("false");
			}
		} else {
			if ("int".equals(classType) || "java.lang.Integer".equals(classType)) {
				reValue = Integer.parseInt(value);
			} else if ("byte".equals(classType) || "java.lang.Byte".equals(classType)) {
				reValue = Byte.parseByte(value);
			} else if ("short".equals(classType) || "java.lang.Short".equals(classType)) {
				reValue = Short.parseShort(value);
			} else if ("float".equals(classType) || "java.lang.Float".equals(classType)) {
				reValue = Float.parseFloat(value);
			} else if ("long".equals(classType) || "java.lang.Long".equals(classType)) {
				reValue = Long.parseLong(value);
			} else if ("double".equals(classType) || "java.lang.Double".equals(classType)) {
				reValue = Double.parseDouble(value);
			} else if ("boolean".equals(classType) || "java.lang.Boolean".equals(classType)) {
				reValue = Boolean.valueOf(value);
			} else if ("java.lang.String".equals(classType)) {
				reValue = value;
			} else if ("java.util.Date".equals(classType)) {
				try {
					if (value.length() == 10) {
						reValue = DateHelper.stringToDate(value, "yyyy-MM-dd");
					} else if (value.length() == 19) {
						reValue = DateHelper.stringToDate(value, "yyyy-MM-dd HH:mm:ss");
					}
				} catch (Exception e) {
					throw new Exception("文档里的日期格式有错误");
				}
			} else {
				reValue = value;
			}
		}
		return reValue;
	}

	/**
	 * 根据属性名称和JavaBean获取属性值
	 * 
	 * @param fieldName
	 *            String 属性名称
	 * @param obj
	 *            Object JavaBean对象
	 * @return
	 * @throws Exception
	 */
	public static Object getFieldValueByName(String fieldName, Object obj) throws Exception {
		String firstLetter = fieldName.substring(0, 1).toUpperCase();
		String getter = "get" + firstLetter + fieldName.substring(1);
		Method method = obj.getClass().getMethod(getter, new Class[] {});
		Object value = method.invoke(obj, new Object[] {});
		return value;
	}

	/**
	 * 该字段是JavaBean类型，则获取ID值即可
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
		Method method2 = obj2.getClass().getMethod("getId", new Class[] {});
		Object value = method2.invoke(obj2, new Object[] {});
		return value;
	}

}