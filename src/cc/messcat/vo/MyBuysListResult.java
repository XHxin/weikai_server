package cc.messcat.vo;

import java.util.List;

public class MyBuysListResult {

//	private int virtualCoin;			//虚拟币
//	private int integral;				//积分
//	private Double exchangeRatio;		//虚拟币与积分兑换比例（积分*比例=虚拟币）
//	private String explain;				//积分与虚拟币说明
	private List<BuysRecordListVo> buysRecordList;		//购买列表
	private int pageNo;
	private int pageSize;
	private int rowCount;
	
	
//	public int getVirtualCoin() {
//		return virtualCoin;
//	}
//	public void setVirtualCoin(int virtualCoin) {
//		this.virtualCoin = virtualCoin;
//	}
//	public int getIntegral() {
//		return integral;
//	}
//	public void setIntegral(int integral) {
//		this.integral = integral;
//	}
//	public Double getExchangeRatio() {
//		return exchangeRatio;
//	}
//	public void setExchangeRatio(Double exchangeRatio) {
//		this.exchangeRatio = exchangeRatio;
//	}
//	public String getExplain() {
//		return explain;
//	}
//	public void setExplain(String explain) {
//		this.explain = explain;
//	}
	public List<BuysRecordListVo> getBuysRecordList() {
		return buysRecordList;
	}
	public void setBuysRecordList(List<BuysRecordListVo> buysRecordList) {
		this.buysRecordList = buysRecordList;
	}
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getRowCount() {
		return rowCount;
	}
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
}
