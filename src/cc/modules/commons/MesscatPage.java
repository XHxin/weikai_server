package cc.modules.commons;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class MesscatPage {

	public MesscatPage() {
	}

	public static List splitPageNews(List objectList, int perPageNum) throws Exception {
		if (objectList == null)
			return null;
		List enList = new ArrayList();
		int pageNum = objectList.size() / perPageNum;
		int num = objectList.size() % perPageNum;
		if (pageNum < 1) {
			List resultList = objectList.subList(0, objectList.size());
			enList.add(resultList);
		} else {
			int i = 0;
			int j = 0;
			i = 0;
			for (j = 0; i < pageNum; j += perPageNum) {
				List temp = objectList.subList(j, j + perPageNum);
				enList.add(i, temp);
				i++;
			}

			if (num != 0) {
				List temp = objectList.subList(j, j + num);
				enList.add(i, temp);
			}
		}
		return enList;
	}

}