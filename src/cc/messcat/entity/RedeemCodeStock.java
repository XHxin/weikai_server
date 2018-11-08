package cc.messcat.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="redeem_code_stock")
public class RedeemCodeStock implements Serializable{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="product_type")
	private Long productType;  //产品类型 0：卡券 1：质量分享 2：视频课程
	
	@Column(name="acti_type")
	private Long actiType;   //活动类型
	
	@Column(name="related_id")
	private Long relatedId;  //产品关联id
	
	@Column(name="number")
    private Integer number;  //数量
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getProductType() {
		return productType;
	}
	public void setProductType(Long productType) {
		this.productType = productType;
	}
	public Long getActiType() {
		return actiType;
	}
	public void setActiType(Long actiType) {
		this.actiType = actiType;
	}
	public Long getRelatedId() {
		return relatedId;
	}
	public void setRelatedId(Long relatedId) {
		this.relatedId = relatedId;
	}
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
}
