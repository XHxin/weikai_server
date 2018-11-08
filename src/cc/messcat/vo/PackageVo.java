package cc.messcat.vo;

import java.math.BigDecimal;

public class PackageVo {
	private Long packageId;//套餐id
	private String name;//套餐名称
	private BigDecimal money;//套餐价格
	
	
	public Long getPackageId() {
		return packageId;
	}
	public void setPackageId(Long packageId) {
		this.packageId = packageId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}
}
