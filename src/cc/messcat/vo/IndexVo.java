package cc.messcat.vo;

import java.util.List;

public class IndexVo {
	
	private Long tradeId;
	private Long technologyId;
	private Long qualityId;
	private Long tendencyId;
	private List<EnterpriseNewsVo> news;
	private List<EnterpriseNewsVo> tradeList;
	private List<EnterpriseNewsVo> technologyList;
	private List<EnterpriseNewsVo> qualityList;
	private List<EnterpriseNewsVo> tendencyList;
	
	public List<EnterpriseNewsVo> getNews() {
		return news;
	}
	public void setNews(List<EnterpriseNewsVo> news) {
		this.news = news;
	}
	public List<EnterpriseNewsVo> getTradeList() {
		return tradeList;
	}
	public void setTradeList(List<EnterpriseNewsVo> tradeList) {
		this.tradeList = tradeList;
	}
	public List<EnterpriseNewsVo> getTechnologyList() {
		return technologyList;
	}
	public void setTechnologyList(List<EnterpriseNewsVo> technologyList) {
		this.technologyList = technologyList;
	}
	public List<EnterpriseNewsVo> getQualityList() {
		return qualityList;
	}
	public void setQualityList(List<EnterpriseNewsVo> qualityList) {
		this.qualityList = qualityList;
	}
	public List<EnterpriseNewsVo> getTendencyList() {
		return tendencyList;
	}
	public Long getTradeId() {
		return tradeId;
	}
	public void setTradeId(Long tradeId) {
		this.tradeId = tradeId;
	}
	public Long getTechnologyId() {
		return technologyId;
	}
	public void setTechnologyId(Long technologyId) {
		this.technologyId = technologyId;
	}
	public Long getQualityId() {
		return qualityId;
	}
	public void setQualityId(Long qualityId) {
		this.qualityId = qualityId;
	}
	public Long getTendencyId() {
		return tendencyId;
	}
	public void setTendencyId(Long tendencyId) {
		this.tendencyId = tendencyId;
	}
	public void setTendencyList(List<EnterpriseNewsVo> tendencyList) {
		this.tendencyList = tendencyList;
	}
	
}
