package cc.messcat.bases;

import java.io.Serializable;

import cc.modules.util.StringUtil;

public class BaseManagerAction implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String INSERT = "insert";
	public static final String UPDATE = "update";
	public static final String DELETE = "delete";
	public static final String RETRIEVE = "retrieve";
	public static final String RETRIEVE_ALL = "retrieveAll";

	private BaseManagerDao baseManagerDao;

	/**
	 * 
	 * 1. 插入操作：objName和id为null
	 * 2. 更新操作：objName和id为null
	 * 3. 删除操作：obj为null
	 * 4. 取一条记录操作：obj为null
	 * 5. 取全部记录操作：obj和id为null
	 * 
	 * @param obj
	 * @param objName
	 * @param id
	 * @param handleType
	 * @return
	 */
	public Object doHandleData(Object obj, String objName, Long id, String handleType) throws Exception {

		Object object = null;
		if (BaseManagerAction.INSERT.equals(handleType)) {
			if (obj == null) {
				throw new Exception("insert error cause by obj is null");
			} else {
				try {
					this.baseManagerDao.saveObject(obj);
				} catch (Exception e) {
					throw new Exception("insert error cause by Exception is : " + e.toString());
				}
			}
		} else if (BaseManagerAction.UPDATE.equals(handleType)) {
			if (obj == null) {
				throw new Exception("update error cause by obj is null");
			} else {
				try {
					this.baseManagerDao.updateObject(obj);
				} catch (Exception e) {
					throw new Exception("update error cause by Exception is : " + e.toString());
				}
			}
		} else if (BaseManagerAction.DELETE.equals(handleType)) {
			if (id == null) {
				throw new Exception("delete error cause by id is null");
			}

			if (StringUtil.isBlank(objName)) {
				throw new Exception("delete error cause by objName is null");
			}

			try {
				this.baseManagerDao.deleteObject(id, objName);
			} catch (Exception e) {
				throw new Exception("delete error cause by Exception is : " + e.toString());
			}
		} else if (BaseManagerAction.RETRIEVE.equals(handleType)) {
			if (id == null) {
				throw new Exception("retrieve error cause by id is null");
			}

			if (StringUtil.isBlank(objName)) {
				throw new Exception("retrieve error cause by objName is null");
			}

			try {
				object = this.baseManagerDao.getObjectById(id, objName);
			} catch (Exception e) {
				throw new Exception("retrieve error cause by Exception is : " + e.toString());
			}
		} else if (BaseManagerAction.RETRIEVE_ALL.equals(handleType)) {
			if (StringUtil.isBlank(objName)) {
				throw new Exception("retrieve all error cause by objName is null");
			}

			try {
				object = this.baseManagerDao.getAllObjects(objName);
			} catch (Exception e) {
				throw new Exception("retrieve all error cause by Exception is : " + e.toString());
			}
		}
		return object;
	}

	public BaseManagerDao getBaseManagerDao() {
		return baseManagerDao;
	}

	public void setBaseManagerDao(BaseManagerDao baseManagerDao) {
		this.baseManagerDao = baseManagerDao;
	}

}