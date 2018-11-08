package cc.messcat.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author HASEE
 *专家分成比例实体
 */
@Entity
@Table(name="divide_scale")
public class DivideScaleExpert {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	/**消费类型:(-1:默认值;0:购买会员;1:准入报告;2:标准解读;3:质量分享;4:付费咨询;5:新闻详情;6:电商准入报告;7:钱包充值;8:视频课程;9:专栏订阅;10:打赏;11:付费咨询围观分成)*/
	@Column(name = "type")
	private String type;

	/**产品ID*/
	@Column(name = "relatedID")
	private Long relatedID;

	/**标签ID*/
	@Column(name = "labelID")
	private Long labelId;

	/**这个标签的生效时间*/
	@Column(name = "effective_date")
	private Date effectiveDate;

	/**参与分成的memberId集合，中间用英文逗号隔开*/
	@Column(name = "memberIds")
	private String memberIds;

	/**分成比例集合，中间用逗号隔开,与memberIds一一对应*/
	@Column(name = "proportions")
	private String proportions;

	/**0-停用，1-可用*/
	@Column(name = "status")
	private int status;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getRelatedID() {
		return relatedID;
	}

	public void setRelatedID(Long relatedID) {
		this.relatedID = relatedID;
	}

	public Long getLabelId() {
		return labelId;
	}

	public void setLabelId(Long labelId) {
		this.labelId = labelId;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public String getMemberIds() {
		return memberIds;
	}

	public void setMemberIds(String memberIds) {
		this.memberIds = memberIds;
	}

	public String getProportions() {
		return proportions;
	}

	public void setProportions(String proportions) {
		this.proportions = proportions;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
