package cc.messcat.entity;

/**
 * 
 * @author xiehuaxin
 * @createDate 2018年5月17日 下午2:42:41
 * @todo 有赞推送消息实体
 */
public class YouzanPushEntity {

	private String msg;	//	经过UrlEncode（UTF-8）编码的消息对象，具体参数请看本页中各业务消息结构文档

    private int sendCount;//重发的次数

    private int mode; //  1-自用型/工具型/平台型消息；0-签名模式消息

    private String app_id;

    private String client_id;	//对应开发者后台的client_id

    private Long version;//消息版本号，为了解决顺序性的问题 ，高版本覆盖低版本

    private String type;	//消息业务类型：TRADE_ORDER_STATE-订单状态事件，TRADE_ORDER_REFUND-退款事件，TRADE_ORDER_EXPRESS-物流事件，ITEM_STATE-商品状态事件，ITEM_INFO-商品基础信息事件，POINTS-积分，SCRM_CARD-会员卡（商家侧），SCRM_CUSTOMER_CARD-会员卡（用户侧），TRADE-交易V1，ITEM-商品V1

    private String id;	//业务消息的标识: 如 订单消息为订单编号,会员卡消息为会员卡id标识

    private String sign;//防伪签名 ：MD5(client_id+msg+client_secrect)

    private Integer kdt_id;//店铺ID

    private boolean test = false;//false-非测试消息，true- 测试消息 ；PUSH服务会定期通过发送测试消息检查开发者服务是否正常

    private String status;	//消息状态，对应消息业务类型。如TRADE_ORDER_STATE-订单状态事件，对应有等待买家付款（WAIT_BUYER_PAY）、等待卖家发货（WAIT_SELLER_SEND_GOODS）等多种状态，详细可参考 消息结构里的描述

    private String kdt_name;

    public boolean isTest() {
        return test;
    }

    public void setTest(boolean test) {
        this.test = test;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getSendCount() {
        return sendCount;
    }

    public void setSendCount(int sendCount) {
        this.sendCount = sendCount;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public Integer getKdt_id() {
        return kdt_id;
    }

    public void setKdt_id(Integer kdt_id) {
        this.kdt_id = kdt_id;
    }

    public String getKdt_name() {
        return kdt_name;
    }

    public void setKdt_name(String kdt_name) {
        this.kdt_name = kdt_name;
    }

	@Override
	public String toString() {
		return "YouzanPushEntity [msg=" + msg + ", sendCount=" + sendCount + ", mode=" + mode + ", app_id=" + app_id
				+ ", client_id=" + client_id + ", version=" + version + ", type=" + type + ", id=" + id + ", sign="
				+ sign + ", kdt_id=" + kdt_id + ", test=" + test + ", status=" + status + ", kdt_name=" + kdt_name
				+ "]";
	}
    
}
