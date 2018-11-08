package cc.messcat.service.pay;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import cc.messcat.entity.*;
import cc.modules.util.*;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alipay.config.AlipayConfig;
import com.alipay.util.AlipayCore;
import com.alipay.util.AlipaySubmit;
import com.alipay.util.AlipayUtil;
import com.mipush.MiPushHelper;
import com.wxpay.Configuration;
import com.wxpay.http.HttpsClient;
import com.wxpay.http.Response;
import com.wxpay.pay.AppPay;
import com.wxpay.pay.PayUtil;
import com.wxpay.pay.SignUtil;
import com.wxpay.pay.UnifiedOrderApp;
import com.wxpay.pay.UnifiedOrderResultApp;
import com.wxpay.sdk.WXPayUtil;
import com.wxpay.util.XStreamFactory;
import cc.messcat.bases.BaseManagerDaoImpl;
import cc.messcat.enums.PayTypeEnum;
import cc.messcat.vo.BuysRecordVo;
import cc.messcat.vo.IOSVerifyVo;
import cc.messcat.vo.ResponseBean;
import cc.modules.constants.Constants;
import cc.modules.security.ExceptionManager;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

public class PayManagerDaoImpl extends BaseManagerDaoImpl implements PayManagerDao {

    private static Logger log = LoggerFactory.getLogger(PayManagerDaoImpl.class);
    private String payEnvironment = PropertiesFileReader.getByKey("pay.environment");

    @Autowired
    private TransactionTemplate template;

    /*  *//**
     * 准入报告购买(支付宝)
     *//*
    @Override
    public Object addAliPayOrder(Member member, ExpenseTotal br)
            throws ExceptionManager, UnsupportedEncodingException {
        String str = "";
        if (br.getMoney() != null && br.getMoney().compareTo(new BigDecimal(0)) == 1) {
            // 加密前的支付宝请求参数
            Map<String, String> params = AlipayUtil.createRequestMap(br.getMoney().toString(), AlipayUtil.SUBJECT,
                    AlipayUtil.OBJECT, br.getNumber());

            // 签名结果与签名方式加入请求提交参数组中
            Map<String, String> sPara = AlipaySubmit.buildRequestPara(params);
            String sign = sPara.get("sign");
            sPara.put("sign", URLEncoder.encode(sign, "utf-8"));
            str = AlipayCore.createLinkString(sPara);
        } else {
            br.setPayStatus("1");
        }
        expenseTotalDao.save(br);
        //记录流水
        addIncomeByPayNew(0, member, br);
        return new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, str);
    }*/

    /* *//**
     *   付费咨询提问，不包括围观问题(支付宝)
     *//*
    @Override
    public Object addAliPayPayConsult(Member member, ExpenseTotal br) throws Exception{
        String str = "";
        if (br.getMoney() != null && br.getMoney().compareTo(new BigDecimal(0)) == 1) {
            // 加密前的支付宝请求参数
            Map<String, String> params = AlipayUtil.createRequestMap(br.getMoney().toString(), AlipayUtil.SUBJECT,
                    AlipayUtil.OBJECT, br.getNumber());

            // 签名结果与签名方式加入请求提交参数组中
            Map<String, String> sPara = AlipaySubmit.buildRequestPara(params);
            String sign = sPara.get("sign");
            sPara.put("sign", URLEncoder.encode(sign, "utf-8"));
            str = AlipayCore.createLinkString(sPara);
        } else {
            br.setPayStatus("1");
        }
        expenseTotalDao.save(br);
        return new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, str);
    }*/

    /**
     * 原来接口 （修改了购买记录表）
     */
    @Override
    public Object addRechargeOrder(Member member, BigDecimal changeMoney)
            throws ExceptionManager, UnsupportedEncodingException {
        ExpenseTotal br = new ExpenseTotal();
        br.setNumber(OrderSnUtil.generateMemberRechargeSn());
        br.setMemberId(member.getId());
        br.setOriginalPrice(new BigDecimal(String.valueOf(changeMoney)));
        br.setCoupnMoney(new BigDecimal(0));
        br.setMoney(new BigDecimal(String.valueOf(changeMoney)));
        Date today = new Date();
        br.setAddTime(today);
        br.setPayTime(today);
        br.setType("1");
        br.setPayStatus("0");
        br.setContent("购买报告");
        br.setPayType(PayTypeEnum.ALIPAY.getName());
        super.saveObject(br);

        // 加密前的支付宝请求参数
        Map<String, String> params = AlipayUtil.createRequestMap(changeMoney.toString(), AlipayUtil.SUBJECT,
                AlipayUtil.OBJECT, br.getNumber());
        // 签名结果与签名方式加入请求提交参数组中
        Map<String, String> sPara = AlipaySubmit.buildRequestPara(params);
        String sign = sPara.get("sign");
        sPara.put("sign", URLEncoder.encode(sign, "utf-8"));
        String str = AlipayCore.createLinkString(sPara);
        return new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, str);
    }


    @Override
    public void addHandlerOrder(Map<String, String> params, String result) throws ExceptionManager {
        log.error("PayManagerDaoImpl:158-进入回调逻辑处理");
        String out_trade_no = params.get("out_trade_no");// 订单号
        String total_fee = params.get("total_fee"); // 交易金额
        String seller_id = params.get("seller_id");
        ExpenseTotal order = payDao.findRechargeOrderByPnNew(out_trade_no);
        if (order == null) {
            throw new ExceptionManager("订单不存在");
        }
        if ("1".equals(order.getPayStatus())) {
            return;
        }
        if (!Double.valueOf(total_fee).equals(order.getMoney())) {
            throw new ExceptionManager("订单金额与回调通知金额不一致");
        }
        if (!AlipayConfig.partner.equals(seller_id)) {
            throw new ExceptionManager("支付seller_id与回调通知seller_id不一致");
        }
        if (order != null) {
            // 充值成功
            if ("Y".equals(result)) {
                // 更新订单状态
                order.setPayTime(new Date());
                order.setPayStatus("1");
                buysRecordDao.update(order);
                log.error("PayManagerDaoImpl:187-修改订单购买状态成功");
                Member member = memberDao.get(order.getMemberId());
                addIncomeByPayNew(0, member, order);// 记录流水
                addSystemMessage(member, order); // 系统消息
            }
            // 充值失败
            if ("N".equals(result)) {
                // 更新订单状态
                order.setPayTime(new Date());
                order.setPayStatus("2");
                buysRecordDao.update(order);
            }
        }
    }


    /**
     * 获取某件商品的分成比例
     * 2,3,8,9,10有默认分成比例
     *
     * @param type
     * @param relatedId
     * @param expenseTotal
     * @return
     */
    public Map<String, List> getMemberProportions(String type, Long relatedId, ExpenseTotal expenseTotal) {

        Map<String, List> memberProportionsMap = new HashMap<>();
        //参与分成的人,中间用英文逗号隔开
        String memberIdsStr;
        //分成比例，中间用逗号隔开，与memberIdsSer一一对应
        String proportionsStr;
        //1.先找出商品对应的分成比例id
        DivideScaleExpert divideScaleExpert = divideScaleExpertDao.getDivideScaleExpertByReleateIdAndType(type, relatedId);
        if (divideScaleExpert != null) {
            memberIdsStr = divideScaleExpert.getMemberIds();
            proportionsStr = divideScaleExpert.getProportions();
            expenseTotal.setDivideScaleId(Integer.valueOf(divideScaleExpert.getId().intValue()));

            /**
             * 查询该商品有没有设置了分成比例，如果有就把divide_scale表的id写到expense_total的divide_scale_id里，使用默认分成比例则写0
             */
            expenseTotal.setDivideScaleId(Integer.valueOf(String.valueOf(divideScaleExpert.getId())));
        } else {
            //默认分成比例
            if ("2".equals(type) || "3".equals(type)) {
                StandardReading standardReading = standardReadDao.get(relatedId);
                memberIdsStr = standardReading.getAuthor().getId().toString();
                proportionsStr = "0.8";
            } else if ("8".equals(type)) {
                //先找到这件商品的所属专家
                LiveVideo video = liveDao.getLiveVideoById(relatedId);
                memberIdsStr = video.getExpertId().toString();
                proportionsStr = "0.8";
            } else if ("9".equals(type)) {
                StandardReading standardReading = standardReadDao.get(relatedId);
                memberIdsStr = standardReading.getAuthor().getId().toString();
                proportionsStr = "0.8";
            } else if ("10".equals(type)) {
                LiveVideo video = liveDao.getLiveVideoById(relatedId);
                memberIdsStr = video.getExpertId().toString();
                proportionsStr = "0.7";
            } else {
                memberIdsStr = "";
                proportionsStr = "";
            }

            expenseTotal.setDivideScaleId(0);
        }
        //更新购买记录表的“分成比例表id”
        expenseTotalDao.update(expenseTotal);

        //参与分成的人合
        String[] divideMembers = null;
        //分成比例的集合
        String[] proportions = null;
        if (memberIdsStr.contains(",") && proportionsStr.contains(",")) {
            if (!memberIdsStr.isEmpty() && !proportionsStr.isEmpty()) {
                divideMembers = memberIdsStr.split(",");
                proportions = proportionsStr.split(",");
            }
        } else {
            divideMembers = new String[]{memberIdsStr};
            proportions = new String[]{proportionsStr};
        }

        /**
         * 转成list返回，为了当商品类型为4的时候可以把专家对应的分成比例添加进来。
         */
        List<Long> memberList = new ArrayList<>();
        List<Double> proportionList = new ArrayList<>();
        if (!"".equals(divideMembers[0])) {
            for (int i = 0; i < divideMembers.length; i++) {
                memberList.add(Long.valueOf(divideMembers[i]));
                proportionList.add(Double.valueOf(proportions[i]));
            }
        }
        memberProportionsMap.put("members", memberList);
        memberProportionsMap.put("proportions", proportionList);

        //有分成比例的返回对应的分成比例；没有分成比例的（商品类型为2,3,8,9,10）返回默认分成比例；如果都没有的，返回空字符串
        return memberProportionsMap;
    }

    /**
     * 通过购买增加流水
     *
     * @param distribute
     * @param member
     * @param expenseTotal
     */
    @Override
    public void addIncomeByPayNew(Integer distribute, Member member, ExpenseTotal expenseTotal) {
        expenseTotal.setVersion(0);
        if (1 == expenseTotalDao.updateVersion(expenseTotal)) {
            //这一句很重要，上面使用编程式事务已经把version设为1，这里要保持一致
            expenseTotal.setVersion(1);
            //先判断此购买记录是否做过流水处理，由于所有的购买都会写到expense_platform,所以只要expense_platform有写过就代表已经写过流水了。//部署的时候把所有写过平台流水的购买记录的version字段update为1，这行代码可去掉
            ExpensePlatform expensePlatformExist = expensePlatDao.getExpensePlatformByExpenseTotalId(expenseTotal.getId());
            if (expensePlatformExist != null || expenseTotal.getMoney().compareTo(new BigDecimal(0.01)) == -1 || "0".equals(expenseTotal.getPayStatus()) || "2".equals(expenseTotal.getPayStatus())) {
                return;
            }
            Map<String, List> memberProportionsMap = getMemberProportions(expenseTotal.getType(), expenseTotal.getRelatedId(), expenseTotal);
            List<Long> members = memberProportionsMap.get("members");
            List<Double> proportions = memberProportionsMap.get("proportions");

            //用于分成的金额，expenseTotal的money字段，但是如果是分销（distribute为1则为分销），是先扣除分销员的50%，再分成
            BigDecimal expenseMoney = expenseTotal.getMoney();
            if (distribute == 1) {
                expenseMoney = expenseMoney.multiply(new BigDecimal(0.5));
            }
            //平台的实际收入金额
            BigDecimal platIncomeMoney = null;
            BigDecimal expenseExpertMoney = null;
            //平台流水的备注信息
            String remark = "";
            //专家收入金额
            DivideScaleCommon divideScaleCommon = null;
            HotProblem hotProblem = null;
            if (expenseTotal.getType().equals("4")) {
                divideScaleCommon = webSiteDao.gainDivideScaleCommon();
                if (divideScaleCommon.getUnifyFee().compareTo(new BigDecimal(0)) == -1
                        || divideScaleCommon.getPayOtherProblemDiscount() < 0
                        || divideScaleCommon.getPayOtherProblemMember() < 0
                        || divideScaleCommon.getPayOtherProblemExpert() < 0
                        || divideScaleCommon.getPayOtherProblemDiscount() < 0
                        || divideScaleCommon.getPayOtherProblemMember() + divideScaleCommon.getPayOtherProblemExpert() > 100
                        || divideScaleCommon.getPayConsultExpert() < 0
                        || divideScaleCommon.getPayConsultExpert() > 100
                        || divideScaleCommon.getDivideScaleReward() < 0
                        || divideScaleCommon.getDivideScaleReward() > 100) {
                    log.error("divide_scale_common表的分成比例设置不正确，订单编号为-" + expenseTotal.getNumber() + "的购买记录未能成功写入流水！");
                    return;
                }
                /**
                 * 如果商品是付费咨询的话，围观问题还要给原提问者分成
                 * 所以第一步是先判断是‘提问’还是‘围观’，如果问题的购买者不是问题的提出者，就是围观问题
                 */
                hotProblem = hotReplyDao.getProblem(expenseTotal.getRelatedId());

                members.add(hotProblem.getExpertId());
                if (!hotProblem.getMemberId().equals(member.getId())) {
                    /***围观**/
                    //提问者分到多少钱
                    //divideScaleCommon表里的分成比例是以整数形式存储的，例如专家A的分成比例为80%，则存的是80，而不是0.8，所以要乘以0.01
                    BigDecimal expenseMemberMoney = expenseMoney.multiply(new BigDecimal(divideScaleCommon.getPayOtherProblemMember())).multiply(new BigDecimal(0.01));
                    //围观问题
                    ExpenseMember expenseMember = new ExpenseMember(hotProblem.getMemberId(), expenseTotal.getId(), expenseTotal.getNumber(), expenseTotal.getOriginalPrice(), expenseTotal.getMoney(), expenseMemberMoney, new Date());
                    payDao.addExpenseMember(expenseMember);

                    // 给提问人增加收益
                    Wallet wallet = walletDao.get(hotProblem.getMemberId());
                    wallet.setBalance(wallet.getBalance().compareTo(new BigDecimal(0)) == -1 ? new BigDecimal(0) : wallet.getBalance().add(expenseMemberMoney));
                    walletDao.update(wallet);

                    //专家分成比例
                    proportions.add(divideScaleCommon.getPayOtherProblemExpert().doubleValue() * 0.01);
                } else {
                    /***提问**/
                    proportions.add(divideScaleCommon.getPayConsultExpert().doubleValue() * 0.01);
                }
            }
            /**
             * 4.平台流水
             */
            //平台以外的其他各方所在的分成比例总和
            double proportionExceptPlatform = 0;
            for (int i = 0; i < proportions.size(); i++) {
                proportionExceptPlatform += proportions.get(i);
            }
            if (expenseTotal.getType().equals("4") && !hotProblem.getMemberId().equals(member.getId())) {
                //这里要加上提问者的分成比例，因为下面平台的分成比例时拿100 减去专家的分成比例的
                proportionExceptPlatform += divideScaleCommon.getPayOtherProblemMember() * 0.01;
            }
            platIncomeMoney = expenseMoney.multiply(new BigDecimal(String.valueOf((1 - proportionExceptPlatform))));
            ExpensePlatform expensePlatform = new ExpensePlatform(expenseTotal.getId(), expenseTotal.getNumber(), member.getRealname(), expenseTotal.getMemberId(), expenseTotal.getOriginalPrice(), expenseTotal.getMoney(), platIncomeMoney, expenseTotal.getContent(), remark, new Date(), 1 - proportionExceptPlatform);
            expensePlatDao.save(expensePlatform);
            /**
             * 有这几种类型是有专家流水的
             */
            if ("2,3,4,8,9,10".contains(expenseTotal.getType())) {
                List<Member> memberList = memberDao.getMembersByMemberIds(members);
                if (memberList != null && memberList.size() > 0) {
                    for (int i = 0; i < memberList.size(); i++) {
                        /**
                         * 5.专家流水
                         */
                        expenseExpertMoney = expenseMoney.multiply(new BigDecimal(proportions.get(i)));
                        ExpenseExpert expenseExpert = new ExpenseExpert(Long.valueOf(members.get(i)), expenseTotal.getId(), expenseTotal.getNumber(), "0", expenseTotal.getOriginalPrice(), expenseTotal.getMoney(), expenseExpertMoney, expenseTotal.getContent(), remark, new Date(), proportions.get(i));
                        expertIEDao.save(expenseExpert);

                        /**
                         *  6.更新专家的收入和可提现金额，打赏的钱是打赏到钱包的，不能提现
                         */
                        if ("2,3,4,8,9".contains(expenseTotal.getType())) {
                            memberList.get(i).setIncome(memberList.get(i).getIncome() == null ? expenseExpert.getExpenseExpertMoney()
                                    : memberList.get(i).getIncome().add(expenseExpert.getExpenseExpertMoney()));
                            /**
                             * 添加积分记录
                             */
                           /* Integral integral = new Integral();
                            integral.setMember(memberList.get(i));
                            integral.setMemberId(memberList.get(i).getId());
                            integral.setMemberName(memberList.get(i).getRealname());
                            integral.setScore(expenseExpert.getExpenseExpertMoney());
                            integral.setEditTime(new Date());
                            integral.setAddTime(new Date());
                            integral.setStatus("1");// 状态 1正常 0删除
                            // type;//消费类型：0：购买会员套餐 1：购买报告 2：购买标准解读 3：购买质量分享 4：付费咨询 6：电商信息
                            integral.setContent("用户" + ExpenseTotal.typeMap.get(expenseTotal.getType()) + "专家获得的积分");
                            integralDao.save(integral);*/

                            memberList.get(i).setCanWithdraw(memberList.get(i).getCanWithdraw() == null ? expenseExpert.getExpenseExpertMoney()
                                    : memberList.get(i).getCanWithdraw().add(expenseExpert.getExpenseExpertMoney()));
                            memberDao.update(memberList.get(i));
                        }

                    }
                }
            }
        }
    }

    /**
     * 生成预订单(微信)
     */
    @Override
    public Object addWechatAppOrder(Member member, BigDecimal changeMoney, HttpServletRequest request) throws Exception {
        // 生成充值订单
        String type = "1";
        String content = "购买报告";
        Long memberId = member.getId();
        String number = OrderSnUtil.generateMemberRechargeSn();
        BigDecimal money = new BigDecimal(0);
        BigDecimal originalPrice = changeMoney;
        String payType = PayTypeEnum.WECHAT.getName();
        ExpenseTotal br = new ExpenseTotal(type, content, memberId, number, money, originalPrice, payType);
        super.saveObject(br);
        // 一、 生成预订单
        UnifiedOrderApp unifiedorder = new UnifiedOrderApp();
        unifiedorder.setAppid(Configuration.getOAuthAppId());
        unifiedorder.setBody("十三行平台微信充值");
        unifiedorder.setMch_id(Configuration.getProperty("weixin4j.pay.partner.id"));
        unifiedorder.setNonce_str(java.util.UUID.randomUUID().toString().substring(0, 15));
        unifiedorder.setNotify_url(Configuration.getProperty("weixin4j.pay.notify_url"));
        unifiedorder.setOut_trade_no(br.getNumber());
        String ip = IpUtil.getIpAddr(request);
        unifiedorder.setSpbill_create_ip(ip);
        if (changeMoney == null) {
            throw new ExceptionManager("金额不能为空");
        }
        // 总费用
        String total = AmountUtil.changeY2F(changeMoney.toString());
        unifiedorder.setTotal_fee(total);
        unifiedorder.setTrade_type("APP");
        // 获取秘钥
        String paternerKey = Configuration.getProperty("weixin4j.pay.partner.key");
        String sign = SignUtil.getSign(unifiedorder.toMap(), paternerKey);
        unifiedorder.setSign(sign);

        // 二、 调用统一下单接口
        UnifiedOrderResultApp unifiedOrderResultApp = null;
        // 将统一下单对象转成XML
        String xmlPost = unifiedorder.toXML();
        if (Configuration.isDebug()) {
            System.out.println("调试模式_统一下单接口 提交XML数据：" + xmlPost);
        }
        // 创建请求对象
        HttpsClient http = new HttpsClient();
        // 提交xml格式数据
        Response res = http.postXml("https://api.mch.weixin.qq.com/pay/unifiedorder", xmlPost);
        // 获取微信平台下单接口返回数据
        String xmlResult = res.asString();
        try {
            JAXBContext context = JAXBContext.newInstance(UnifiedOrderResultApp.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            unifiedOrderResultApp = (UnifiedOrderResultApp) unmarshaller.unmarshal(new StringReader(xmlResult));
        } catch (JAXBException ex) {
            return new ResponseBean(Constants.FAIL_CODE_400, Constants.MSG_FAIL);
        }

        // 三、返回支付参数
        if ("FAIL".equals(unifiedOrderResultApp.getReturn_code())) {
            return new ResponseBean("400", "调用统一下单接口失败", unifiedOrderResultApp);
        }
        // app支付需要的请求参数
        AppPay result = new AppPay(Configuration.getOAuthAppId(), Configuration.getProperty("weixin4j.pay.partner.id"),
                unifiedOrderResultApp.getPrepay_id(), Configuration.getProperty("weixin4j.pay.partner.key"),
                br.getNumber());
        return new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, result);

    }

    /* *//**
     * 积分 （修改了购买记录表）
     *//*
    public void addIntegralByPay(Member member, ExpenseTotal order) {
        Integral integral = new Integral();
        integral.setMember(member);
        integral.setMemberId(member.getId());
        integral.setMemberName(member.getRealname());
        integral.setScore(order.getMoney());
        integral.setEditTime(new Date());
        integral.setAddTime(new Date());
        integral.setStatus("1");// 状态 1正常 0删除
        // type;//消费类型：0：购买会员套餐 1：购买报告 2：购买标准解读 3：购买质量分享 4：付费咨询 6：电商信息
        integral.setContent(ExpenseTotal.typeMap.get(order.getType()));
        integralDao.save(integral);
        log.error("PayManagerDaoImpl:336-增加积分记录成功");
        BigDecimal score = member.getIntegral();
        if (score == null || score.compareTo(new BigDecimal(0)) == 0) {
            member.setIntegral(order.getMoney());
        } else {
            member.setIntegral(member.getIntegral().add(order.getMoney()));
        }
        memberDao.update(member);
        log.error("PayManagerDaoImpl:347-修改会员积分信息成功");
    }*/

  /*  @Override
    public Object addWechatOrder(Member member, ExpenseTotal br, HttpServletRequest request)
            throws Exception {
        log.error("PayManagerDaoImpl:600-进入微信支付生成订单");
        AppPay result = null;
        if (br.getMoney() != null && br.getMoney().compareTo(new BigDecimal(0)) == 1) {
            // 一、 生成预订单
            UnifiedOrderApp unifiedorder = new UnifiedOrderApp();
            unifiedorder.setAppid(Configuration.getOAuthAppId());
            unifiedorder.setBody("微信充值");
            unifiedorder.setMch_id(Configuration.getProperty("weixin4j.pay.partner.id"));
            unifiedorder.setNonce_str(java.util.UUID.randomUUID().toString().substring(0, 15));
            unifiedorder.setNotify_url(Configuration.getProperty("weixin4j.pay.notify_url"));
            unifiedorder.setOut_trade_no(br.getNumber());
            String ip = IpUtil.getIpAddr(request);
            unifiedorder.setSpbill_create_ip(ip);
            if (br.getMoney() == null) {
                throw new ExceptionManager("金额不能为空");
            }
            String total = AmountUtil.changeY2F(br.getMoney().toString());// 总费用
            // String total = AmountUtil.changeY2F("0.01");// 总费用 测试用
            unifiedorder.setTotal_fee(total);
            unifiedorder.setTrade_type("APP");
            String paternerKey = Configuration.getProperty("weixin4j.pay.partner.key");// 获取秘钥
            String sign = SignUtil.getSign(unifiedorder.toMap(), paternerKey);
            unifiedorder.setSign(sign);

            // 二、 调用统一下单接口
            UnifiedOrderResultApp unifiedOrderResultApp = null;
            // 将统一下单对象转成XML
            String xmlPost = unifiedorder.toXML();
            if (Configuration.isDebug()) {
                System.out.println("调试模式_统一下单接口 提交XML数据：" + xmlPost);
            }
            // 创建请求对象
            HttpsClient http = new HttpsClient();
            // 提交xml格式数据
            Response res = http.postXml("https://api.mch.weixin.qq.com/pay/unifiedorder", xmlPost);
            // 获取微信平台下单接口返回数据
            String xmlResult = res.asString();
            try {
                JAXBContext context = JAXBContext.newInstance(UnifiedOrderResultApp.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                unifiedOrderResultApp = (UnifiedOrderResultApp) unmarshaller.unmarshal(new StringReader(xmlResult));
            } catch (JAXBException ex) {
                return new ResponseBean(Constants.FAIL_CODE_400, Constants.MSG_FAIL);
            }

            // 三、返回支付参数
            if ("FAIL".equals(unifiedOrderResultApp.getReturn_code())) {
                return new ResponseBean("400", "调用统一下单接口失败", unifiedOrderResultApp);
            }
            // app支付需要的请求参数
            result = new AppPay(Configuration.getOAuthAppId(), Configuration.getProperty("weixin4j.pay.partner.id"),
                    unifiedOrderResultApp.getPrepay_id(), Configuration.getProperty("weixin4j.pay.partner.key"),
                    br.getNumber());
        }
        br.setPayStatus("1");
        expenseTotalDao.save(br);
        //记录流水
        addIncomeByPayNew(0, member, br);
        return new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, result);
    }*/

    /* */

    /**
     * 付费咨询提问，不包括围观问题(微信)
     *//*
    @Override
    public Object addWechatPayConsult(Member member, ExpenseTotal br, HttpServletRequest request)
            throws Exception {
        log.error("PayManagerDaoImpl:600-进入微信支付生成订单");
        AppPay result = null;
        if (br.getMoney() != null && br.getMoney().compareTo(new BigDecimal(0)) == 1) {
            // 一、 生成预订单
            UnifiedOrderApp unifiedorder = new UnifiedOrderApp();
            unifiedorder.setAppid(Configuration.getOAuthAppId());
            unifiedorder.setBody("微信充值");
            unifiedorder.setMch_id(Configuration.getProperty("weixin4j.pay.partner.id"));
            unifiedorder.setNonce_str(java.util.UUID.randomUUID().toString().substring(0, 15));
            unifiedorder.setNotify_url(Configuration.getProperty("weixin4j.pay.notify_url"));
            unifiedorder.setOut_trade_no(br.getNumber());
            String ip = IpUtil.getIpAddr(request);
            unifiedorder.setSpbill_create_ip(ip);
            if (br.getMoney() == null) {
                throw new ExceptionManager("金额不能为空");
            }
            String total = AmountUtil.changeY2F(br.getMoney().toString());// 总费用
            // String total = AmountUtil.changeY2F("0.01");// 总费用 测试用
            unifiedorder.setTotal_fee(total);
            unifiedorder.setTrade_type("APP");
            String paternerKey = Configuration.getProperty("weixin4j.pay.partner.key");// 获取秘钥
            String sign = SignUtil.getSign(unifiedorder.toMap(), paternerKey);
            unifiedorder.setSign(sign);
            // 二、 调用统一下单接口
            UnifiedOrderResultApp unifiedOrderResultApp = null;
            // 将统一下单对象转成XML
            String xmlPost = unifiedorder.toXML();
            if (Configuration.isDebug()) {
                System.out.println("调试模式_统一下单接口 提交XML数据：" + xmlPost);
            }
            // 创建请求对象
            HttpsClient http = new HttpsClient();
            // 提交xml格式数据
            Response res = http.postXml("https://api.mch.weixin.qq.com/pay/unifiedorder", xmlPost);
            // 获取微信平台下单接口返回数据
            String xmlResult = res.asString();
            try {
                JAXBContext context = JAXBContext.newInstance(UnifiedOrderResultApp.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                unifiedOrderResultApp = (UnifiedOrderResultApp) unmarshaller.unmarshal(new StringReader(xmlResult));
            } catch (JAXBException ex) {
                return new ResponseBean(Constants.FAIL_CODE_400, Constants.MSG_FAIL);
            }
            // 三、返回支付参数
            if ("FAIL".equals(unifiedOrderResultApp.getReturn_code())) {
                return new ResponseBean("400", "调用统一下单接口失败", unifiedOrderResultApp);
            }
            // app支付需要的请求参数
            result = new AppPay(Configuration.getOAuthAppId(), Configuration.getProperty("weixin4j.pay.partner.id"),
                    unifiedOrderResultApp.getPrepay_id(), Configuration.getProperty("weixin4j.pay.partner.key"),
                    br.getNumber());
        }
        br.setPayStatus("1");
        expenseTotalDao.save(br);
        return new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, result);
    }*/
    @Override
    public void addNotifyResult(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            // 获取POST流
            ServletInputStream in = request.getInputStream();
            // 将流转换为字符串
            String xmlMsg = XStreamFactory.inputStream2String(in);
            // 商户密钥
            String paternerKey = Configuration.getProperty("weixin4j.pay.partner.key");

            Map<String, String> unifiedOrderResultAppMap = WXPayUtil.xmlToMap(xmlMsg);
            UnifiedOrderResultApp unifiedOrderResultApp = new UnifiedOrderResultApp();
            BeanUtils.populate(unifiedOrderResultApp, unifiedOrderResultAppMap);
            String return_code = unifiedOrderResultApp.getReturn_code();
            String out_trade_no = unifiedOrderResultApp.getOut_trade_no();
            String total_fee = unifiedOrderResultApp.getTotal_fee();
            log.error("payMamnagerDaoImpl-723-out_trade_no=" + out_trade_no);
            // 查询订单
            ExpenseTotal order = payDao.findRechargeOrderByPnNew(out_trade_no);
            System.err.println("out_trade_no:" + out_trade_no);
            System.err.println("order:" + order);
            if (order == null) {
                throw new ExceptionManager("订单不存在");
            }
            log.error("payMamnagerDaoImpl-732-order.getPayStatus()=" + order.getPayStatus());
            if ("1".equals(order.getPayStatus())) {        // 付款状态（0：未付款 1：已付款）
                return;
            }

            if (order != null) {
                // 判断签名及结果
                if ("SUCCESS".equals(return_code)) {
                    // 签名验证
                    boolean result = PayUtil.verifySign(xmlMsg, paternerKey);
                    if (result) {
                        // 更新订单状态
                        order.setPayTime(new Date());
                        order.setPayStatus("1");// 付款状态（0：未付款 1：已付款）
                        buysRecordDao.update(order);
                        log.error("payMamnagerDaoImpl-748-修改成功");
                        // 增加积分
                        Member member = memberDao.get(order.getMemberId());
                        addIntegral(member, order);
                        //流水
                        addIncomeByPayNew(0, member, order);
                        // 新增系统消息
                        addSystemMessage(member, order);
                        response.getOutputStream()
                                .write("<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>"
                                        .getBytes());
                    } else {
                        // 更新订单状态
                        order.setPayTime(new Date());
                        order.setPayStatus("2");
                        buysRecordDao.update(order);
                        response.getOutputStream()
                                .write("<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[签名失败]]></return_msg></xml>"
                                        .getBytes());
                    }
                } else {
                    // 更新订单状态
                    order.setPayTime(new Date());
                    order.setPayStatus("2");
                    buysRecordDao.update(order);
                    // 收到微信支付回调通知，支付失败
                    response.getOutputStream()
                            .write("<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[支付失败]]></return_msg></xml>"
                                    .getBytes());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 新增积分
     * 此方法与memberManagerDaoImpl里的addIntegral一模一样，如此方法要做修改，请两个方法一起改
     *
     * @param order
     */
    public void addIntegral(Member member, ExpenseTotal order) {
        /**
         * 添加积分记录
         */
        Integral integral = new Integral();
        integral.setMember(member);
        integral.setMemberId(member.getId());
        integral.setMemberName(member.getRealname());
        integral.setScore(order.getMoney());
        integral.setEditTime(new Date());
        integral.setAddTime(new Date());
        integral.setStatus("1");// 状态 1正常 0删除
        // type;//消费类型：0：购买会员套餐 1：购买报告 2：购买标准解读 3：购买质量分享 4：付费咨询 6：电商信息
        integral.setContent(ExpenseTotal.typeMap.get(order.getType()));
        integralDao.save(integral);

        /**
         * 给用户添加积分
         */
        BigDecimal memberIntegral = member.getIntegral();
        if (integral.getScore().compareTo(new BigDecimal(0)) == 1) {
            member.setIntegral(memberIntegral.add(integral.getScore()));
        }
        memberDao.update(member);
    }

  /*  @Override
    public Object addBuysRecordRechargeOrderFromApple(Member member, Long regionId, Long productId)
            throws ExceptionManager, UnsupportedEncodingException {
        ExpenseTotal record = new ExpenseTotal();
        record.setAddTime(new Date());
        record.setType("1");// 消费类型
        record.setPayType("3");// 付款类型（1、微信 2、支付宝 3、苹果内购）
        record.setPayStatus("0");// 付款状态（0：未付款 1：已付款）
        record.setNumber(OrderSnUtil.generateMemberRechargeSn());// 生成的单号
        record.setMemberId(member.getId());
        */

    /**
     * 准入报告购买查standard表，根据 regionId，productId 查询唯一记录。
     *//*
        // Standard standard =
        // standardManagerDao.getStandardByConSimple(regionId, productId);
        Region region = regionDao.get(regionId);
        record.setRegionId(region.getId());
        Product product = productDao.get(productId);
        record.setRelatedId(product.getId());
        record.setExpertId(0L);
        record.setCoupnMoney(new BigDecimal(0));
        record.setAddTime(new Date());
        record.setPayTime(new Date());
        // 准入报告 按照统一收费
        DivideScaleCommon divideScaleCommon = webSiteDao.gainDivideScaleCommon();
        BigDecimal changeMoney = divideScaleCommon.getUnifyFee();
        // 准入报告 是没有价格，则用changeMoney作为原价。
        record.setOriginalPrice(changeMoney);
        switch (member.getGrade()) {    //会员等级
            case "0":
                record.setMoney(changeMoney);// 金额
                break;
            case "1":
                // WebSite ws = webSiteManagerDao.getWebSite();
                // changeMoney = ws.getUnifyFee();//报告才用这个
                if ("0".equals(member.getType())) {// 年费会员
                    Integer yearVipDiscount = divideScaleCommon.getYearVipDiscount();
                    record.setMoney(changeMoney.multiply(new BigDecimal(yearVipDiscount)).multiply(new BigDecimal("0.01")));// 金额
                } else if ("1".equals(member.getType())) {// 月费会员
                    Integer monthVipDiscount = divideScaleCommon.getMonthVipDiscount();
                    record.setMoney(changeMoney.multiply(new BigDecimal(monthVipDiscount)).multiply(new BigDecimal("0.01")));// 金额
                }
                break;
        }
        record.setContent("购买准入报告");
        if (record.getMoney() == null || record.getMoney().compareTo(new BigDecimal(0)) == -1) {
            record.setPayStatus("1");
        }
        buysRecordDao.save(record);
        BuysRecordVo brv = new BuysRecordVo();
        brv.setId(record.getId());
        brv.setContent(record.getContent());
        brv.setMemberId(record.getMemberId());
        brv.setMoney(record.getMoney());
        brv.setNumber(record.getNumber());
        return new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, brv);
    }*/
    @Override
    public Object updateBuysRecordRechargeOrderFromApple(Member member, IOSVerifyVo iosVerifyVo,
                                                         HttpServletRequest request, HttpServletResponse response) throws ExceptionManager, IOException {
        // 苹果客户端传上来的收据,是最原据的收据
        String receipt = iosVerifyVo.getReceipt();
        System.out.println(receipt);
        // 拿到收据的MD5
        String md5_receipt = receipt;
        // 默认是无效账单
        String result = md5_receipt;
        // 查询订单
        ExpenseTotal order = payDao.findRechargeOrderByPnNew(iosVerifyVo.getOrderNum());
        String verifyResult = null;

        if (order != null) {
            if ("sanbox".equals(payEnvironment)) {
                verifyResult = IOSVerifyUtil.buyAppVerify(receipt, "Sandbox");
            } else {
                verifyResult = IOSVerifyUtil.buyAppVerify(receipt, null);
            }
        }
        if (verifyResult == null) {
            // 苹果服务器没有返回验证结果
            result = "D#" + md5_receipt;
            return new ResponseBean(Constants.FAIL_CODE_400, "苹果服务器没有返回验证结果.");
        } else {
            // 跟苹果验证有返回结果------------------
            JSONObject job = JSONObject.fromObject(verifyResult);
            String states = job.getString("status");
            if ("21007".equals(states)) {
                // 状态码为“21007”，在测试服务器再验证一次

                String SandboxStraus = iOSVerifySandbox(request, response, iosVerifyVo, order);
                if ("D".equals(SandboxStraus)) {
                    return new ResponseBean(Constants.FAIL_CODE_400, "苹果服务器没有返回验证结果.");
                } else if ("200".equals(SandboxStraus)) {
                    return new ResponseBean(Constants.FAIL_CODE_400, "验证成功.");
                } else if ("C".equals(SandboxStraus)) {
                    return new ResponseBean(Constants.FAIL_CODE_400, "账单无效 .");
                } else if ("F".equals(SandboxStraus)) {
                    return new ResponseBean(Constants.FAIL_CODE_400, "账单异常 .");
                } else if ("E".equals(SandboxStraus)) {
                    return new ResponseBean(Constants.FAIL_CODE_400, "账单时间无效 .");
                } else if ("B".equals(SandboxStraus)) {
                    return new ResponseBean(Constants.FAIL_CODE_400, "账单有效，但己验证过.");
                } else if ("400".equals(SandboxStraus)) {
                    return new ResponseBean(Constants.FAIL_CODE_400, "提交参数不全.");
                } else if ("500".equals(SandboxStraus)) {
                    return new ResponseBean(Constants.EXCEPTION_CODE_500, "服务器出错.");
                }
            } else if (states.equals("0")) {
                // 验证成功
                String r_receipt = job.getString("receipt");
                JSONObject returnJson = JSONObject.fromObject(r_receipt);
                Calendar calendar = Calendar.getInstance();
                if (returnJson.getString("receipt_creation_date_ms") != "" && !(((calendar.getTimeInMillis()
                        - Long.parseLong(returnJson.getString("receipt_creation_date_ms"))) / 1000) <= (3600 * 9))) {
                    return new ResponseBean(Constants.FAIL_CODE_400, "超出时间");
                }
                // 保存到数据库 (更改流水，改为已支付)returnJson.getString("unique_identifier")
                try {
                    // 更新订单状态
                    order.setPayTime(new Date());
                    order.setPayStatus("1");
                    buysRecordDao.update(order);
                    // 增加积分
                    addIntegral(member, order);
                    //记录流水
//                    addAppleIncomeByPay(member, order);
                    addIncomeByPayNew(0, member, order);
                } catch (Exception e) {
                    return new ResponseBean(Constants.FAIL_CODE_400, "账单时间无效 .");
                }
                return new ResponseBean(Constants.SUCCESS_CODE_200, "验证成功.");
            } else {
                // 账单无效
                result = "C#" + md5_receipt;
                return new ResponseBean(Constants.FAIL_CODE_400, "账单无效 .");
            }
        }
        return new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_FAIL, "在service最后");
    }

    public String iOSVerifySandbox(HttpServletRequest request, HttpServletResponse response, IOSVerifyVo iosVerifyVo,
                                   ExpenseTotal order) throws IOException {
        try {
            if (null != iosVerifyVo.getOrderNum() && null != iosVerifyVo.getReceipt()) {
                System.out.println(new Date() + "  来自苹果端的验证...");
                // 苹果客户端传上来的收据,是最原据的收据
                String receipt = iosVerifyVo.getReceipt();
                System.out.println(receipt);
                // 拿到收据的MD5
                // String md5_receipt = MD5.md5Digest(receipt);
                String md5_receipt = receipt;
                // 默认是无效账单
                String result = md5_receipt;
                // 查询数据库，看是否是己经验证过的账号
                // boolean isExists = false;
                // boolean
                // isExists=DbServiceImpl_PNM.isExistsIOSReceipt(md5_receipt);
                String verifyResult = null;
                if (order != null) {
                    // String verifyUrl=IOS_Verify.getVerifyURL();
                    if ("sanbox".equals(payEnvironment)) {
                        verifyResult = IOSVerifyUtil.buyAppVerify(receipt, "Sandbox");
                    } else {
                        verifyResult = IOSVerifyUtil.buyAppVerify(receipt, null);
                    }
                    if (verifyResult == null) {
                        // 苹果服务器没有返回验证结果
                        result = "D#" + md5_receipt;
                        return "D";
                    } else {
                        // 跟苹果验证有返回结果------------------
                        JSONObject job = JSONObject.fromObject(verifyResult);
                        String states = job.getString("status");
                        if (states.equals("0")) {
                            // 验证成功
                            String r_receipt = job.getString("receipt");
                            JSONObject returnJson = JSONObject.fromObject(r_receipt);
                            // 产品ID
                            // String
                            // product_id=returnJson.getString("product_id");
                            // 数量
                            // String quantity=returnJson.getString("quantity");
                            // 跟苹果的服务器验证成功
                            // result =
                            // "A#"+md5_receipt+"_"+product_id+"_"+quantity;
                            // 交易日期
                            // 判断有效时间
                            Calendar calendar = Calendar.getInstance();
                            if (returnJson.getString("receipt_creation_date_ms") != "" && !(((calendar.getTimeInMillis()
                                    - Long.parseLong(returnJson.getString("receipt_creation_date_ms")))
                                    / 1000) <= (3600 * 9))) {
                                return "E";// 超出时间
                            }
                            // String
                            // purchase_date=returnJson.getString("purchase_date");
                            // 保存到数据库 (更改流水，改为已支付)
                            try {
                                // 更新订单状态
                                order.setPayTime(new Date());
                                order.setPayStatus("1");
                                buysRecordDao.update(order);
                                // 增加积分
                                Integral integral = integralDao.findIntegralBymemberId(order.getMemberId());
                                Member member = memberDao.get(order.getMemberId());
                                addIntegral(member, order);
                                // 记录流水
//                                addAppleIncomeByPay(member, order);
                                addIncomeByPayNew(0, member, order);
                            } catch (Exception e) {
                                return "F";
                            }
                            return "200";
                        } else {
                            // 账单无效
                            result = "C#" + md5_receipt;
                            return "C";
                        }
                    }
                } else {
                    // 账单有效，但己验证过
                    result = "B#" + md5_receipt;
                    return "B";
                }
            }
            return "400";
        } catch (Exception e) {
            e.printStackTrace();
            return "500";
        }
    }

   /* private void addAppleIncomeByPay(Member member, ExpenseTotal buysRecord) {
        StandardReading standardRead = null;
        Member author = null;
        if (buysRecord.getRelatedId() != null) {// 说明有专家流水
            standardRead = standardReadDao.get(buysRecord.getRelatedId());
            if (standardRead.getAuthor() != null) {
                author = memberDao.get(standardRead.getAuthor().getId());
            }
        }
        DivideScaleCommon divideScaleCommon = webSiteDao.gainDivideScaleCommon();
        Double discount = divideScaleCommon.getExpertProportion() * 0.01;
        if (standardRead != null && ("2".equals(buysRecord.getType()) || "3".equals(buysRecord.getType()))) {
            *//**
     *  专家分成流水
     *//*
            ExpenseExpert expenseExpert = new ExpenseExpert();
            expenseExpert.setExpertId(member.getId());
            expenseExpert.setExpenseTotalId(buysRecord.getId());
            expenseExpert.setOrderNum(buysRecord.getNumber());
            expenseExpert.setOperate("0"); // 操作（0：收入 1：提现）
            // 实际收到金额
            expenseExpert.setMoney(buysRecord.getOriginalPrice().multiply(new BigDecimal(String.valueOf((discount * (1 - 0.3))))));
            expenseExpert.setContent(BuysRecord.typeMap.get(buysRecord.getType()));
            expenseExpert.setAddTime(new Date());
            if (standardRead != null) {
                if (standardRead.getAuthor() != null && standardRead.getAuthor().getId() != null) {
                    expenseExpert.setExpertId(standardRead.getAuthor().getId());
                    author.setCanWithdraw(author.getCanWithdraw() == null ? expenseExpert.getMoney()
                            : author.getCanWithdraw().add(expenseExpert.getMoney()));
                    memberDao.update(author);
                }
            }
            expertIEDao.save(expenseExpert);
            */

    /**
     * 平台分成流水
     *//*
            ExpensePlatform expensePlatform = new ExpensePlatform();
            expensePlatform.setExpenseTotalId(buysRecord.getId());
            expensePlatform.setOrderNum(buysRecord.getNumber());
            expensePlatform.setMemberId(member.getId());
            expensePlatform.setMemberName(member.getRealname());
            if (author != null) {
                expensePlatform.setMemberId(author.getId());
                expensePlatform.setMemberName(author.getRealname());
            }
            expensePlatform.setOriginalPrice(buysRecord.getOriginalPrice());
            // 实际收到金额
            expensePlatform.setMoney(buysRecord.getMoney().multiply(new BigDecimal((1 - discount) * (1 - 0.3))));
            expensePlatform.setContent(BuysRecord.typeMap.get(buysRecord.getType()));
            expensePlatform.setAddTime(new Date());
            expensePlatDao.save(expensePlatform);
        }
    }*/
   /* @Override
    public Object addBuysRecordRechargeOrderFromApple(Member member, Long standardReadId)
            throws ExceptionManager, UnsupportedEncodingException {
        ExpenseTotal record = new ExpenseTotal();
        record.setAddTime(new Date());
        record.setPayTime(new Date());
        record.setPayType("3");// 付款类型（1、微信 2：支付宝）
        record.setPayStatus("0");// 付款状态（0：未付款 1：已付款）
        record.setNumber(OrderSnUtil.generateMemberRechargeSn());// 生成的单号
        StandardReading standardRead = standardReadDao.get(standardReadId);
        if ("1".equals(standardRead.getClassify())) {// 1：购买标准解读
            record.setType("2");// 消费类型：0：购买会员套餐 1：购买报告 2：购买标准解读 3：购买质量分享 4：付费咨询
            record.setContent("购买标准解读");
        } else if ("2".equals(standardRead.getClassify())) {// 2：购买质量分享
            record.setType("3");
            record.setContent("购买质量分享");
        } else {
            record.setType("2");
            record.setContent("购买标准解读");
        }
        record.setRelatedId(standardRead.getId());
        record.setRegionId(0L);
        record.setCoupnMoney(new BigDecimal(0));
        record.setExpertId(0L);
        record.setMemberId(member.getId());
        record.setOriginalPrice(new BigDecimal(String.valueOf(standardRead.getMoney())));
        switch (member.getGrade()) {
            case "0":
                record.setMoney(new BigDecimal(String.valueOf(standardRead.getMoney())));// 金额
                break;
            case "1":
                DivideScaleCommon divideScaleCommon = webSiteDao.gainDivideScaleCommon();
                if ("0".equals(member.getType())) {// 年费会员
                    Integer yearVipDiscount = divideScaleCommon.getYearVipDiscount();
                    record.setMoney(new BigDecimal(String.valueOf(standardRead.getMoney())).multiply(new BigDecimal(yearVipDiscount * 0.01)));// 金额
                } else if ("1".equals(member.getType())) {// 月费会员
                    Integer monthVipDiscount = divideScaleCommon.getMonthVipDiscount();
                    record.setMoney(standardRead.getMoney().multiply(new BigDecimal(monthVipDiscount * 0.01)));// 金额
                }
                break;
        }
        // 生成充值订单
        buysRecordDao.save(record);
        BuysRecordVo brv = new BuysRecordVo();
        brv.setId(record.getId());
        brv.setContent(record.getContent());
        brv.setMemberId(record.getMemberId());
        brv.setMoney(record.getMoney());
        brv.setNumber(record.getNumber());
        //添加分成流水
        addIncomeByPayNew(0, member, record);
        return new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, brv);
    }*/

   /* @Override
    public Object addEBusinessProductBuysRecordRechargeOrderFromApple(Member member, Long eBusinessProductId)
            throws ExceptionManager, UnsupportedEncodingException {
        ExpenseTotal record = new ExpenseTotal();
        record.setAddTime(new Date());
        record.setPayTime(new Date());
        record.setType("6");
        record.setPayType("3");// 付款类型（1、微信 2：支付宝）
        record.setPayStatus("0");// 付款状态（0：未付款 1：已付款）
        record.setNumber(OrderSnUtil.generateMemberRechargeSn());// 生成的单号
        record.setMemberId(member.getId());
        EBusinessProduct eBusinessProduct = businessProductDao.get(eBusinessProductId);
        record.setRelatedId(eBusinessProduct.getId());
        record.setRegionId(0L);
        record.setCoupnMoney(new BigDecimal(0));
        record.setExpertId(0L);
        record.setOriginalPrice(eBusinessProduct.getMoney());
        record.setContent("购买电商准入报告详情");
        switch (member.getGrade()) {
            case "0":
                record.setMoney(eBusinessProduct.getMoney());// 金额
                break;
            case "1":
                DivideScaleCommon divideScaleCommon = webSiteDao.gainDivideScaleCommon();
                if ("0".equals(member.getType())) {// 年费会员
                    Integer yearVipDiscount = divideScaleCommon.getYearVipDiscount();
                    record.setMoney(eBusinessProduct.getMoney().multiply(new BigDecimal(yearVipDiscount * 0.01)));// 金额
                } else if ("1".equals(member.getType())) {// 月费会员
                    Integer monthVipDiscount = divideScaleCommon.getMonthVipDiscount();
                    record.setMoney(eBusinessProduct.getMoney().multiply(new BigDecimal(monthVipDiscount * 0.01)));// 金额
                }
                break;
        }
        buysRecordDao.save(record);
        BuysRecordVo brv = new BuysRecordVo();
        brv.setId(record.getId());
        brv.setContent(record.getContent());
        brv.setMemberId(record.getMemberId());
        brv.setMoney(record.getMoney());
        brv.setNumber(record.getNumber());
        return new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, brv);
    }*/

    /* */

    /**
     * 虚拟币统一支付接口处理
     *//*
    @Override
    public Object addVirtualCoinPay(ExpenseTotal record, Member member) {
        try {
            buysRecordDao.save(record);
            // 减少会员的虚拟币
            member.setVirtualCoin(member.getVirtualCoin().subtract(record.getMoney()));
            memberDao.update(member);
            addIncomeByPayNew(0, member, record);
            // 新增系统消息
            addSystemMessage(member, record);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseBean(Constants.FAIL_CODE_400, "虚拟币统一支付接口处理出错");
        }
        return new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS);
    }*/
    public void addSystemMessage(Member member, ExpenseTotal order) {
        if (member.getTourist() == "0") {// 因为游客没有手机号码，所以无法发送系统消息
            SystemMessage message = new SystemMessage();
            message.setAddTime(new Date());
            message.setEditTime(new Date());
            message.setStatus("1");
            message.setType("8");
            message.setPhoto("");
            message.setDataUrl("");
            message.setPushType("1");
            message.setMemberIds(member.getId() + "");
            message.setTitle(
                    SystemMessage.titleMap.get(message.getType()) + SystemMessage.buysMap.get(order.getType()));
            switch (order.getType()) {
                case "1":
                    message.setRelateId(order.getRelatedId());
                    message.setRegionId(order.getRegionId());
                    break;
                case "2":
                    message.setRelateId(order.getRelatedId());
                    break;
                case "3":
                    message.setRelateId(order.getRelatedId());
                case "4":
                    message.setRelateId(order.getRelatedId());
                case "6":
                    message.setRelateId(order.getRelatedId());
                    break;
                case "7":
                    message.setRelateId(order.getRelatedId());
            }
            systemMessageDao.save(message);
            MiPushHelper.sendAndroidSysNotify("购买成功", message.getTitle(), member.getMobile());
            MiPushHelper.sendIOSSysMsg("购买成功", member.getMobile());
        }
    }

    /**
     * 钱包统一支付接口处理
     *
     * @param record
     * @param member
     * @return
     */
    @Override
    public Object addWalletCoinPay(ExpenseTotal record, Member member) {
        try {
            // 减少会员钱包里的金钱
            Wallet wallet = walletDao.get(member.getId());
            wallet.setBalance(wallet.getBalance().subtract(record.getMoney()));
            walletDao.update(wallet);
            //新增流水
            addIncomeByPayNew(0, member, record);
            //新增系统消息
            addSystemMessage(member, record);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseBean(Constants.EXCEPTION_CODE_500, "钱包统一支付接口处理出错");
        }
        return new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS);
    }

    /* *//**
     * 给专家增加付费答疑的钱
     * @param record
     * @param member
     *//*
    @Override
    public void addExpertWallet(ExpenseTotal record, Member member,Member questioner) {
        Wallet wallet = walletDao.get(member.getId());
        if (record.getCoupnMoney() != null) {
            wallet.setBalance(wallet.getBalance().add(record.getMoney()));
        } else {
            wallet.setBalance(wallet.getBalance().add(record.getMoney()));
        }
        walletDao.update(wallet);

        *//**
     *   专家流水
     *//*
        DivideScaleCommon divideScaleCommon = webSiteDao.gainDivideScaleCommon();
        ExpenseExpert expenseExpert=new ExpenseExpert();
        expenseExpert.setExpertId(member.getId());
        expenseExpert.setExpenseTotalId(record.getId());
        expenseExpert.setOrderNum(record.getNumber());
        expenseExpert.setOperate("0");
        expenseExpert.setOriginalPrice(record.getOriginalPrice());
        expenseExpert.setMoney(record.getMoney());
        expenseExpert.setExpenseExpertMoney(record.getMoney().multiply(new BigDecimal(divideScaleCommon.getPayConsultExpert() * 0.01)));
        expenseExpert.setContent("付费咨询");
        expenseExpert.setRemark("");
        expenseExpert.setAddTime(new Date());
        payDao.addExpenseExpert(expenseExpert);
        *//**
     *  平台流水
     *//*
        ExpensePlatform expensePlatform=new ExpensePlatform();
        expensePlatform.setExpenseTotalId(record.getId());
        expensePlatform.setOrderNum(record.getNumber());
        expensePlatform.setMemberName(questioner.getRealname());
        expensePlatform.setMemberId(questioner.getId());
        expensePlatform.setOriginalPrice(record.getOriginalPrice());
        expensePlatform.setMoney(record.getMoney());
        expensePlatform.setPlatIncomeMoney(record.getMoney().multiply(new BigDecimal((100-divideScaleCommon.getPayConsultExpert()) * 0.01)));
        expensePlatform.setContent("付费咨询");
        expensePlatform.setRemark("");
        expensePlatform.setAddTime(new Date());
        payDao.addExpensePlatform(expensePlatform);
    }*/

    /**
     * 付费咨询提问（钱包）
     */
    @Override
    public Object addWalletPayConsult(ExpenseTotal record, Member member, Long[] coupnId) {
        try {
            // 减少会员钱包里的金钱
            Wallet wallet = walletDao.get(member.getId());
            wallet.setBalance(wallet.getBalance().subtract(record.getMoney()));
            walletDao.update(wallet);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseBean(Constants.EXCEPTION_CODE_500, "钱包统一支付接口处理出错");
        }
        return new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS);
    }

    @Override
    public Object addIncomeByReward(Long memberId, Long beRewardId, Double rewardMoney) {
        BigDecimal rewordMoney2 = new BigDecimal(rewardMoney);
        // 打赏人钱包
        Wallet rewardWallet = walletDao.get(memberId);
        Member member = memberDao.get(memberId);
        Member author = memberDao.get(beRewardId);
        //判断打赏者的钱包余额是否足够支付打赏金额
        if (rewardWallet.getBalance().compareTo(new BigDecimal(rewardMoney)) == 0 || rewardWallet.getBalance().compareTo(rewordMoney2) == 1) {
            //扣除打赏者的打赏金额
            rewardWallet.setBalance(rewardWallet.getBalance().subtract(rewordMoney2));
            walletDao.update(rewardWallet);

            //获取被打赏者与平台的分成比例
            DivideScaleCommon eDivideInto = webSiteDao.gainDivideScaleCommon();
            Integer divideScaleReward = eDivideInto.getDivideScaleReward();
            //给被打赏者加钱
            BigDecimal rewordToExpert = rewordMoney2.multiply(new BigDecimal(divideScaleReward).multiply(new BigDecimal(0.01)));


            /**
             * 流水处理
             */
            ExpenseTotal expenseTotal = new ExpenseTotal("10", "打赏", member.getId(), OrderSnUtil.generateMemberRechargeSn(), rewordMoney2, rewordMoney2, "4", beRewardId);
            ExpenseTotal buys = payDao.addExpenseTotal(expenseTotal);
            if (buys != null) {
                /**
                 * 进入打赏专家流水逻辑
                 *
                 * */
                ExpenseExpert expenseExpert = new ExpenseExpert();
                expenseExpert.setExpertId(buys.getRelatedId());
                expenseExpert.setExpenseTotalId(buys.getId());
                expenseExpert.setOrderNum(buys.getNumber());
                expenseExpert.setOperate("0");
                expenseExpert.setOriginalPrice(buys.getOriginalPrice());
                expenseExpert.setMoney(buys.getMoney());
                expenseExpert.setExpenseExpertMoney(rewordToExpert);
                expenseExpert.setContent("直播打赏（钱包支付）");
                expenseExpert.setRemark("");
                expenseExpert.setAddTime(new Date());
                expenseExpert.setExpertId(beRewardId);
                if (author != null) {
                    author.setIncome(author.getIncome() == null ? expenseExpert.getExpenseExpertMoney()
                            : author.getIncome().add(expenseExpert.getExpenseExpertMoney()));
                    author.setCanWithdraw(author.getCanWithdraw() == null ? expenseExpert.getExpenseExpertMoney()
                            : author.getCanWithdraw().add(expenseExpert.getExpenseExpertMoney()));
                }
                memberDao.update(author);
                expertIEDao.save(expenseExpert);
                /**
                 * 进入打赏平台流水逻辑
                 */
                ExpensePlatform expensePlatform = new ExpensePlatform();
                expensePlatform.setExpenseTotalId(buys.getId());
                expensePlatform.setOrderNum(buys.getNumber());
                expensePlatform.setMemberName(member.getRealname());
                expensePlatform.setMemberId(member.getId());
                expensePlatform.setOriginalPrice(buys.getOriginalPrice());
                expensePlatform.setMoney(rewordMoney2);
                expensePlatform.setPlatIncomeMoney(rewordMoney2.subtract(rewordToExpert));
                expensePlatform.setContent("直播打赏（钱包支付）");
                expensePlatform.setRemark("");
                expensePlatform.setAddTime(new Date());
                expensePlatDao.save(expensePlatform);
            }
            return new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS);
        } else {
            return new ResponseBean(Constants.FAIL_CODE_400, "余额不足");
        }
    }


    @Override
    public Object addIncomeByRewardV2(Long memberId, Long beRewardId, Double rewardMoney) {
        BigDecimal rewordMoney2 = new BigDecimal(rewardMoney);
        // 打赏人钱包
        Wallet rewardWallet = walletDao.get(memberId);
        Member member = memberDao.get(memberId);
        Member author = memberDao.get(beRewardId);
        //判断打赏者的钱包余额是否足够支付打赏金额
        if (rewardWallet.getBalance().compareTo(new BigDecimal(rewardMoney)) == 0 || rewardWallet.getBalance().compareTo(rewordMoney2) == 1) {
            /**
             * 流水处理
             */
            ExpenseTotal expenseTotal = new ExpenseTotal("10", "打赏", member.getId(), OrderSnUtil.generateMemberRechargeSn(), rewordMoney2, rewordMoney2, "4", beRewardId);
            ExpenseTotal buys = payDao.addExpenseTotal(expenseTotal);
            addWalletCoinPay(buys, member);
            return new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS);
        } else {
            return new ResponseBean(Constants.FAIL_CODE_400, "余额不足");
        }
    }

    @Override
    public ExpenseTotal addExpenseTotal(ExpenseTotal recordNew) {
        return payDao.addExpenseTotal(recordNew);
    }

    @Override
    public Object distributeExpense(Integer distribute, Long expenseTotalId) {
        if (distribute != null && expenseTotalId != null) {
            ExpenseTotal expenseTotal = (ExpenseTotal) expenseTotalDao.get(expenseTotalId, "cc.messcat.entity.ExpenseTotal");
            if (expenseTotal != null) {
                Member member = memberDao.get(expenseTotal.getMemberId());
                addIncomeByPayNew(distribute, member, expenseTotal);
            }
            return new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS);
        } else {
            return new ResponseBean(Constants.FAIL_CODE_400, Constants.MSG_PARAMETER_EMPTY);
        }
    }

    @Override
    public ExpenseTotal getPayConsultExpenseTotal(HotProblem hotProblem) {
        return payDao.getPayConsultExpenseTotal(hotProblem);
    }

}
