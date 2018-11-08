package cc.messcat.vo;

import java.util.List;

public class StandardReadListResult2 {
   
	private List<StandardReadListVo3>  columnSubscribe;  //专栏订阅
	private int rowCount;
	
	
	public List<StandardReadListVo3> getColumnSubscribe() {
		return columnSubscribe;
	}
	public void setColumnSubscribe(List<StandardReadListVo3> columnSubscribe) {
		this.columnSubscribe = columnSubscribe;
	}
	public int getRowCount() {
		return rowCount;
	}
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
}
