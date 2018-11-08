package cc.messcat.vo;

import java.math.BigDecimal;
import java.util.Date;
/**
 * 
 * @author wenyu
 *
 */
public class StandReadingVo {

	private Long standReadingId;  //质量分享的id
	private String title;		
	private String authorName;
	private Date editTime;
	private BigDecimal money;
	private String cover;   //封面
	private String type;   //id的类型  1-政策分析  2-质量漫谈   3-电商品控    4-能力验证    5-试验室运营    6-整改专区
	private String isYearFree;      //是否年度VIP会员免费（0：否 1：是）
	private String isMonthFree;     //是否月度VIP会员免费（0：否 1：是）
	private String isVIPView;//0：无 1：月、年费VIP 2：年费VIP
	private String buyStatus;//购买状态  1-已购买    0-未购买
	private String isExistVoice;//是否有音频  1-有  0-没有
	private BigDecimal linePrice;//划线价
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	public Date getEditTime() {
		return editTime;
	}
	public void setEditTime(Date editTime) {
		this.editTime = editTime;
	}
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	public Long getStandReadingId() {
		return standReadingId;
	}
	public void setStandReadingId(Long standReadingId) {
		this.standReadingId = standReadingId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getIsYearFree() {
		return isYearFree;
	}
	public void setIsYearFree(String isYearFree) {
		this.isYearFree = isYearFree;
	}
	public String getIsMonthFree() {
		return isMonthFree;
	}
	public void setIsMonthFree(String isMonthFree) {
		this.isMonthFree = isMonthFree;
	}
	public String getIsVIPView() {
		return isVIPView;
	}
	public void setIsVIPView(String isVIPView) {
		this.isVIPView = isVIPView;
	}
	public String getBuyStatus() {
		return buyStatus;
	}
	public void setBuyStatus(String buyStatus) {
		this.buyStatus = buyStatus;
	}
	public String getIsExistVoice() {
		return isExistVoice;
	}
	public void setIsExistVoice(String isVice) {
		this.isExistVoice = isVice;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public BigDecimal getLinePrice() {
		return linePrice;
	}

	public void setLinePrice(BigDecimal linePrice) {
		this.linePrice = linePrice;
	}
}
