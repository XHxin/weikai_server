package cc.messcat.quartz;

import java.util.Date;

/**
 * @auther xiehuaxin
 * @create 2018-08-03 11:20
 * @todo 支付宝返回的账单记录
 */
public class AliPayBillRow {

    //支付宝交易号
    private String dealNum;

    //商户订单号
    private String merchantOrderNum;

    //业务类型（交易、退款）
    private String dealType;

    //商品名称
    private String goodsName;
    //创建时间
    private Date createTime;

    //完成时间
    private Date accomplishTime;

    //门店编号
    private String shopNum;

    //门店名称
    private String shopName;

    //操作员
    private String operator;

    //终端号
    private String equipmentNum;

    //对方账户
    private String hisAccount;

    //订单金额（元）
    private String orderMoney;

    //商家实收（元）
    private String officialReceipts;

    //支付宝红包（元）
    private String redEnvelopeMoney;

    //集分宝（元）
    private String setPointsTreasure;

    //支付宝优惠（元）
    private String alipayDiscount;

    //商家优惠（元）
    private String merchantDiscount;

    //券核销金额（元）
    private String cancelAfterVerificationMoneyOfCoupn;

    //券名称
    private String coupnName;

    //商家红包消费金额（元）
    private String merchantRedEnvelopeExpenditure;
    //卡消费金额（元）
    private String coupnExpenditure;

    //退款批次号/请求号
    private String refundRequestNum;

    //服务费（元）
    private String serviceCharge;

    //分润（元）
    private String shareBenefit;

    //备注
    private String remark;

    public String getDealNum() {
        return dealNum;
    }

    public void setDealNum(String dealNum) {
        this.dealNum = dealNum;
    }

    public String getMerchantOrderNum() {
        return merchantOrderNum;
    }

    public void setMerchantOrderNum(String merchantOrderNum) {
        this.merchantOrderNum = merchantOrderNum;
    }

    public String getDealType() {
        return dealType;
    }

    public void setDealType(String dealType) {
        this.dealType = dealType;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getAccomplishTime() {
        return accomplishTime;
    }

    public void setAccomplishTime(Date accomplishTime) {
        this.accomplishTime = accomplishTime;
    }

    public String getShopNum() {
        return shopNum;
    }

    public void setShopNum(String shopNum) {
        this.shopNum = shopNum;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getEquipmentNum() {
        return equipmentNum;
    }

    public void setEquipmentNum(String equipmentNum) {
        this.equipmentNum = equipmentNum;
    }

    public String getHisAccount() {
        return hisAccount;
    }

    public void setHisAccount(String hisAccount) {
        this.hisAccount = hisAccount;
    }

    public String getOrderMoney() {
        return orderMoney;
    }

    public void setOrderMoney(String orderMoney) {
        this.orderMoney = orderMoney;
    }

    public String getOfficialReceipts() {
        return officialReceipts;
    }

    public void setOfficialReceipts(String officialReceipts) {
        this.officialReceipts = officialReceipts;
    }

    public String getRedEnvelopeMoney() {
        return redEnvelopeMoney;
    }

    public void setRedEnvelopeMoney(String redEnvelopeMoney) {
        this.redEnvelopeMoney = redEnvelopeMoney;
    }

    public String getSetPointsTreasure() {
        return setPointsTreasure;
    }

    public void setSetPointsTreasure(String setPointsTreasure) {
        this.setPointsTreasure = setPointsTreasure;
    }

    public String getAlipayDiscount() {
        return alipayDiscount;
    }

    public void setAlipayDiscount(String alipayDiscount) {
        this.alipayDiscount = alipayDiscount;
    }

    public String getMerchantDiscount() {
        return merchantDiscount;
    }

    public void setMerchantDiscount(String merchantDiscount) {
        this.merchantDiscount = merchantDiscount;
    }

    public String getCancelAfterVerificationMoneyOfCoupn() {
        return cancelAfterVerificationMoneyOfCoupn;
    }

    public void setCancelAfterVerificationMoneyOfCoupn(String cancelAfterVerificationMoneyOfCoupn) {
        this.cancelAfterVerificationMoneyOfCoupn = cancelAfterVerificationMoneyOfCoupn;
    }

    public String getCoupnName() {
        return coupnName;
    }

    public void setCoupnName(String coupnName) {
        this.coupnName = coupnName;
    }

    public String getMerchantRedEnvelopeExpenditure() {
        return merchantRedEnvelopeExpenditure;
    }

    public void setMerchantRedEnvelopeExpenditure(String merchantRedEnvelopeExpenditure) {
        this.merchantRedEnvelopeExpenditure = merchantRedEnvelopeExpenditure;
    }

    public String getCoupnExpenditure() {
        return coupnExpenditure;
    }

    public void setCoupnExpenditure(String coupnExpenditure) {
        this.coupnExpenditure = coupnExpenditure;
    }

    public String getRefundRequestNum() {
        return refundRequestNum;
    }

    public void setRefundRequestNum(String refundRequestNum) {
        this.refundRequestNum = refundRequestNum;
    }

    public String getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(String serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public String getShareBenefit() {
        return shareBenefit;
    }

    public void setShareBenefit(String shareBenefit) {
        this.shareBenefit = shareBenefit;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
