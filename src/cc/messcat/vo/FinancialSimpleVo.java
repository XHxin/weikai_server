package cc.messcat.vo;

public class FinancialSimpleVo {
	
	// Fields
		private Long id;
		
		private Integer chargeType;		//收费类型
		private long charge;			//收费金额
		private String stuId;		//学员号
		private String name;			//学员名称
		private String mobile;
		private String cardNo;
		
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public Integer getChargeType() {
			return chargeType;
		}
		public void setChargeType(Integer chargeType) {
			this.chargeType = chargeType;
		}
		public long getCharge() {
			return charge;
		}
		public void setCharge(long charge) {
			this.charge = charge;
		}
		
		public String getStuId() {
			return stuId;
		}
		public void setStuId(String stuId) {
			this.stuId = stuId;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getMobile() {
			return mobile;
		}
		public void setMobile(String mobile) {
			this.mobile = mobile;
		}
		public String getCardNo() {
			return cardNo;
		}
		public void setCardNo(String cardNo) {
			this.cardNo = cardNo;
		}
}
