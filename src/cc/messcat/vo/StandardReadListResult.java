package cc.messcat.vo;

import java.util.List;

public class StandardReadListResult {
   
	private List<StandardReadListVo>  standardReadingList;  //热门专栏
	private List<HotExpertListVo>  expertList;   //热门专家推荐专栏
	private List<QualityTypeListVo> qualityTypeList;   //质量分享分类
	private int readRowCount;
	private int expertRowCount;
	private int qualityRowCount;
	
	public List<StandardReadListVo> getStandardReadingList() {
		return standardReadingList;
	}
	public void setStandardReadingList(List<StandardReadListVo> standardReadingList) {
		this.standardReadingList = standardReadingList;
	}
	public List<HotExpertListVo> getExpertList() {
		return expertList;
	}
	public void setExpertList(List<HotExpertListVo> expertList) {
		this.expertList = expertList;
	}
	public List<QualityTypeListVo> getQualityTypeList() {
		return qualityTypeList;
	}
	public void setQualityTypeList(List<QualityTypeListVo> qualityTypeList) {
		this.qualityTypeList = qualityTypeList;
	}
	public int getReadRowCount() {
		return readRowCount;
	}
	public void setReadRowCount(int readRowCount) {
		this.readRowCount = readRowCount;
	}
	public int getExpertRowCount() {
		return expertRowCount;
	}
	public void setExpertRowCount(int expertRowCount) {
		this.expertRowCount = expertRowCount;
	}
	public int getQualityRowCount() {
		return qualityRowCount;
	}
	public void setQualityRowCount(int qualityRowCount) {
		this.qualityRowCount = qualityRowCount;
	}
}
