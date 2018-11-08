package cc.messcat.vo;

import java.math.BigDecimal;
import java.util.List;

public class MoneyAndFieldResult {

	private BigDecimal openMoney; // 专家设置的公开提问价格
	private BigDecimal privateMoney; // 专家设置的私密提问价格
	private List<ExpertClassifyVo> classifyList; // 专家领域列表


	public BigDecimal getOpenMoney() {
		return openMoney;
	}

	public void setOpenMoney(BigDecimal openMoney) {
		this.openMoney = openMoney;
	}

	public BigDecimal getPrivateMoney() {
		return privateMoney;
	}

	public void setPrivateMoney(BigDecimal privateMoney) {
		this.privateMoney = privateMoney;
	}

	public List<ExpertClassifyVo> getClassifyList() {
		return classifyList;
	}
	public void setClassifyList(List<ExpertClassifyVo> classifyList) {
		this.classifyList = classifyList;
	}
}
