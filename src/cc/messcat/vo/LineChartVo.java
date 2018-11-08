package cc.messcat.vo;

import java.util.Date;

public class LineChartVo {

	//基础折线图
	private String typeNum;//1-微信 2-微博
	private String real_date;
	private double value;

	public String getTypeNum() {
		return typeNum;
	}
	public void setTypeNum(String typeNum) {
		this.typeNum = typeNum;
	}
	
	public String getReal_date() {
		return real_date;
	}
	public void setReal_date(String real_date) {
		this.real_date = real_date;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	
	
	
	
}
