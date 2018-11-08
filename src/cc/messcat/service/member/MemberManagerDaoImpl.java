package cc.messcat.service.member;

import cc.messcat.bases.BaseManagerDaoImpl;
import cc.messcat.entity.*;
import cc.messcat.enums.BuyTypeEnum;
import cc.messcat.enums.PayTypeEnum;
import cc.messcat.vo.*;
import cc.modules.commons.Pager;
import cc.modules.constants.AlipayConstants;
import cc.modules.constants.Constants;
import cc.modules.security.ExceptionManager;
import cc.modules.util.*;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.config.AlipayConfig;
import com.alipay.util.AlipayCore;
import com.alipay.util.AlipaySubmit;
import com.alipay.util.AlipayUtil;
import com.mipush.MiPushHelper;
import com.qcloud.Module.Live;
import com.wxpay.Configuration;
import com.wxpay.http.HttpsClient;
import com.wxpay.http.Response;
import com.wxpay.pay.*;
import com.wxpay.sdk.WXPayConstants;
import com.wxpay.sdk.WXPayRequest;
import com.wxpay.sdk.WXPayUtil;
import com.wxpay.util.XStreamFactory;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

import static cc.modules.constants.AlipayConstants.*;
import static com.alipay.api.AlipayConstants.CHARSET;

public class MemberManagerDaoImpl extends BaseManagerDaoImpl implements MemberManagerDao {

    private static final long serialVersionUID = 1L;
    // private String defaultMemberPhoto =PropertiesFileReader.get("member.photo.url", "/app.properties");// 默认会员头像
    private String payEnvironment = PropertiesFileReader.getByKey("pay.environment");
    private String alipayAppId = PropertiesFileReader.getByKey("alipay.appid");
    private String alipayPrivateKey = PropertiesFileReader.getByKey("alipay.key.private");
    private String alipayPublicKey = PropertiesFileReader.getByKey("alipay.key.public");
    private String alipayGetaway = PropertiesFileReader.getByKey("alipay.gateway.new");
    private String alipayTopupWalletNotify = PropertiesFileReader.getByKey("alipay.notify.wallet.url");
    private String alipayToupVipNotify = PropertiesFileReader.getByKey("alipay.notify.vip.url");
    private static Logger log = LoggerFactory.getLogger(MemberManagerDaoImpl.class);

    public MemberManagerDaoImpl() {
    }

    @Override
    public void addMember(Member member) {
        this.memberDao.save(member);
    }

    @Override
    public void modifyMember(Member member) {
        this.memberDao.update(member);
    }

    /*
     * 注册
     */
    @Override
    public MemberVo addAppMember(Member member, HttpServletRequest request) {
        MemberVo memberVo = new MemberVo();
        try {
            member.setLoginIp(IpUtil.getIpAddr(request));
            if (member.getRole().equals("2")) {
                member.setExpertCheckStatus("0");
            }
            memberDao.save(member);
            // 查找当前注册会员id
            Member newmember = null;
            if (member.getUuid() == null) {
                newmember = this.findMemberByMobile(member.getMobile());

            } else if (member.getUuid() != null) {
                newmember = this.getUUID(member.getUuid());
            }
            Wallet wallet = new Wallet();
            wallet.setMemberId(member.getId());
            wallet.setBalance(new BigDecimal(0));
            wallet.setStatus("0");// 可用
            walletDao.save(wallet);
            // 查看此登录用户是否有推荐人
            Long recomender = member.getRecommender_id();
            if (recomender != null) {
                // 新用户登录时才增加推荐人的推荐次数
                Member reMember = memberDao.getRecommend_times(recomender);
                if (reMember != null) {
                    int addRecommendTimes = reMember.getRecommend_times() + 1;
                    reMember.setRecommend_times(addRecommendTimes);
                    memberDao.update(reMember);
                }
            }
            // 创建环信平台用户
            RegisterIM.createSingleUser(newmember.getId());
            // 保存用户环信账号到数据库
            newmember.setEasemobUserName("weikai" + newmember.getId());
            newmember.setEasemobPassword(SecurityTool.md5Simple(com.em.comm.Constants.DEFAULT_PASSWORD));
            memberDao.update(newmember);
            memberVo = MemberUtil.setMemberVoInfo(newmember);
            // 获取用户的银行卡信息
            if (member.getBankId() != 0) {
                BankMember bankMember = bankDao.findBankMember(member.getBankId());
                if (bankMember != null) {
                    memberVo.setOpenBank(bankMember.getOpenBank());
                    memberVo.setBankCard(bankMember.getBankCard());
                    memberVo.setCardholder(bankMember.getCardHolder());
                    memberVo.setBankMobile(bankMember.getBankMobile());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return memberVo;
    }

    /*
     * 登录
     */
    public MemberVo updateAppMember(Member member, HttpServletRequest request) {
        // 为之前已经注册的用户添加钱包
        Wallet wallet = walletDao.get(member.getId());
        if (wallet == null) {
            Wallet myWallet = new Wallet();
            myWallet.setMemberId(member.getId());
            myWallet.setBalance(new BigDecimal(0));
            myWallet.setStatus("0");// 可用
            walletDao.save(myWallet);
        }

        MemberVo memberVo = new MemberVo();
        member.setAccessToken(TokenProccessor.getInstance().makeToken());
        // 上次登录时间和ip
        member.setOldLoginIp(member.getLoginIp());
        member.setOldLoginTime(member.getLoginTime());
        member.setLoginTime(new Date());
        member.setLoginIp(IpUtil.getIpAddr(request));

        // 查看此登录用户是否有推荐人
        Long recomender = member.getRecommender_id();
        // 判断是否是通过别人推荐注册的账号且是第一次登录。
        if (recomender != null && member.getLoginTime() == null) {
            // 新用户获得一个月会员
            Date date = member.getEndTime();
            if (date == null) {
                date = new Date();
            }
            date = new Date(date.getTime() + (long) 30 * 24 * 60 * 60 * 1000);
            member.setEndTime(date);
            member.setGrade("1");
            member.setType("1");
            member.setInitTime(new Date());
            member.setStartTime(new Date());

        }
        if (member.getRealname() == null || "".equals(member.getRealname())) {
            if (member.getMobile() != null && !"".equals(member.getMobile())) {
                member.setRealname(member.getMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
            } else {
                member.setRealname(member.getRealname());
            }
        }
        this.memberDao.update(member);
        memberVo = MemberUtil.setMemberVoInfo(member);
        // 获取用户的银行卡信息
        if (member.getBankId() != 0) {
            BankMember bankMember = bankDao.findBankMember(member.getBankId());
            if (bankMember != null) {
                memberVo.setOpenBank(bankMember.getOpenBank());
                memberVo.setBankCard(bankMember.getBankCard());
                memberVo.setCardholder(bankMember.getCardHolder());
                memberVo.setBankMobile(bankMember.getBankMobile());
            }
        }
        return memberVo;
    }

    public MemberVo updateMember(Member member) {
        this.memberDao.update(member);
        MemberVo memberVo = MemberUtil.setMemberVoInfo(member);
        return memberVo;
    }

    public void removeMember(Member member) {
        this.memberDao.delete(member);
    }

    public void removeMember(Long id) {
        this.memberDao.delete(id);
    }

    public Member retrieveMember(Long id) {
        Member currentMember = memberDao.get(id);
        // 判断当前会员是否是vip会员
        if (currentMember != null) {
            if (ObjValid.isValid(currentMember.getGrade())) {
                if (currentMember.getGrade().equals("1")) {// 判断是普通会员还是vip
                    if (currentMember.getEndTime() == null) {
                        currentMember.setEndTime(new Date());
                    }
                    if (currentMember.getYearEndTime() == null) {
                        currentMember.setYearEndTime(new Date());
                    }
                    memberDao.update(currentMember);
                }
            }
        }
        return currentMember;
    }

    public List retrieveAllMembers() {
        return this.memberDao.findAll();
    }

    public Pager retrieveMembersPager(int pageSize, int pageNo) {
        return this.memberDao.getPager(pageSize, pageNo);
    }

    public Pager findMembers(int pageSize, int pageNo, String statu) {
        Pager pager = memberDao.getObjectListByClass(pageSize, pageNo, Member.class, statu);
        return pager;
    }

    public Pager findMembersByCon(int pageSize, int pageNo, Member member) {
        Pager pager = memberDao.getPager(pageSize, pageNo, member);
        return pager;
    }

    /**
     * 查询用户根据微信号
     */
    public Member retrieveMemberByOpenId(Member member) {
        Map<String, Object> attrs = new HashMap<String, Object>();
        attrs.put("openId", member.getOpenId());
        attrs.put("status", "1");
        return memberDao.query(Member.class, attrs);
    }

    /**
     * 根据mobile查询member
     */
    public Member findMemberByMobile(String mobile) {
        List<Member> list = this.memberDao.findByhql("from Member where mobile='" + mobile + "' and status=1");
        if (list.size() == 0) {
            return null;
        }
        return list.get(0);
    }

    /**
     * 手机存在校验
     */
    public boolean isExistMobile(String mobile) {
        List<Member> list = this.memberDao.findByhql("from Member where mobile='" + mobile + "' and status=1");
        if (ObjValid.isValid(list)) {
            return true;
        }
        return false;
    }

    /**
     * 手机号码唯一性校验
     */
    public boolean isMobileUnique(String mobile, String orgMobile) {
        if (mobile.equals(orgMobile))
            return true;
        List list = this.memberDao.findByhql("from Member where mobile='" + mobile + "' and status=1");
        if (ObjValid.isValid(list)) {
            return false;
        }
        return true;
    }

    /**
     * 手机号码唯一性校验
     */
    public boolean isMobileUnique3(String openid, String mobile, String orgMobile) {
        if (mobile.equals(orgMobile))
            return true;
        List list = this.memberDao.findByhql("from Member where mobile='" + mobile + "' and status=1 and openid is not null");
        if (ObjValid.isValid(list)) {
            return false;
        }
        return true;
    }

    /**
     * 邮箱唯一性校验
     */
    public boolean isEmailUnique(String email, String orgEmail) {
        if (email.equals(orgEmail)) {
            return true;
        }
        List list = this.memberDao.findByhql("from Member where email='" + email + "'");
        if (ObjValid.isValid(list)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean verifyMobileCode(String business, String mobile, String mobileCode, HttpSession session)
            throws ExceptionManager {
        if (null != mobileCode && null != mobile) {
            String sessionMobilecode = (String) session.getAttribute(business + mobile);
            log.error("校验验证码session的key: " + business + mobile + " session的value: " + session.getAttribute(business + mobile));
            if (sessionMobilecode != null) {
                String[] array = sessionMobilecode.split(Constants.CODE_LOGINTOKEN);
                Long s = (System.currentTimeMillis() - Long.valueOf(array[1])) / (1000 * 60);
                if (s >= Long.valueOf(Constants.CODE_LIMIT_TIME)) {// 15分钟后失效
                    session.removeAttribute(business + mobile);
                    return false;
                }
                if (array[0].equals(mobileCode)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public MyBuysListResult getBuysRecordList(String type, Long memberId, int pageNo, int pageSize) {
        MyBuysListResult result = new MyBuysListResult();
        List<BuysRecord> buyList = this.memberDao.getBuysRecordList(type, memberId, pageNo, pageSize);
        if (!buyList.isEmpty() && buyList.size() > 0) {
            for (BuysRecord buy : buyList) {
                BuysRecordListVo vo = new BuysRecordListVo();
                StandardReading read = memberDao.getStandard(memberId);
                vo.setBuysRecordId(buy.getId());

                vo.setTitle(buy.getContent());
                vo.setMoney(buy.getMoney() == null ? new BigDecimal(0) : buy.getMoney());
                vo.setType(buy.getType());
                // vo.setBuyTime(buy.getPayTime());
                // vo.setPhoto(read.getPhoto());
                // vo.setReadingTime(read.getAddTime()); //解读创建时间
                // 还有一些没有添加进来
                List<BuysRecordListVo> resultList = new ArrayList<BuysRecordListVo>();
                resultList.add(vo);
                result.setBuysRecordList(resultList);
            }
        }
        return result;
    }

    /*
     *   支付宝充值
     */
    @Override
    public ResponseBean addVip(Member member, Long packagesId) throws UnsupportedEncodingException {
        // 生成充值订单
        Packages packages = (Packages) this.getObjectById(packagesId, Packages.class.getName());
        String type = "0";
        String content = "充值会员(支付宝)";
        Long memberId = member.getId();
        String orderNum = OrderSnUtil.generateMemberRechargeSn();
        BigDecimal money = packages.getMoney();
        BigDecimal originalPrice = packages.getMoney();
        String payType = "2";
        ExpenseTotal br = new ExpenseTotal(type, content, memberId, orderNum, money, originalPrice, payType);
        br.setRelatedId(packages.getId());
        super.saveObject(br);

        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient(alipayGetaway, alipayAppId, alipayPrivateKey, "json", AlipayConstants.CHARSET, alipayPublicKey, "RSA2");
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody(OBJECT);
        model.setSubject(SUBJECT);
        model.setOutTradeNo(orderNum);
        model.setTimeoutExpress(TIMEOUT_EXPRESS);
        model.setTotalAmount(money.toString());
        model.setProductCode(PRODUCT_CODE);
        request.setBizModel(model);
        request.setNotifyUrl(alipayToupVipNotify);
        //这里和普通的接口调用不同，使用的是sdkExecute
        try {
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            return new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, response.getBody());
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return new ResponseBean(Constants.FAIL_CODE_400, "订单初始化失败");
    }

    /**
     * 支付宝和微信充值vip
     *
     * @param order
     */
    private void updateIncomeExpense(ExpenseTotal order) {
        log.error("vip充值加时间。。。");
        try {
            Packages packagesId = (Packages) this.getObjectById(order.getRelatedId(), Packages.class.getName());
            if (packagesId != null) {
                // 充值年费会员，就送卡券
                if (packagesId.getType().equals("0")) {
                    Long[] coupnId = new Long[]{1L, 2L, 4L, 5L};
                    List<Coupn> coupns = memberDao.getCoupnById(coupnId);
                    for (Coupn coupn : coupns) {
                        CoupnUser uCoupn = new CoupnUser();
                        uCoupn.setCoupnId(coupn);
                        uCoupn.setMemberId(order.getMemberId());
                        uCoupn.setUsed("0");
                        memberDao.save(uCoupn);
                    }
                }
                Date now = new Date();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(now);
                Member member = (Member) this.getObjectById(order.getMemberId(), Member.class.getName());

                if (member.getYearEndTime() == null) {
                    member.setYearEndTime(new Date());
                }
                if (member.getEndTime() == null) {
                    member.setEndTime(new Date());
                }
                if (member.getYearEndTime().before(new Date())) {// 年费过期了
                    if (member.getEndTime().before(new Date())) {
                        member.setGrade("0");
                        member.setType("2");
                    } else {
                        member.setGrade("1");
                        member.setType("1");
                    }
                } else {
                    member.setGrade("1");
                    member.setType("0");
                }
                if (null != member) {
                    String grade = member.getGrade();
                    String type = packagesId.getType();
                    if ("0".equals(grade)) {
                        member.setInitTime(now);
                        member.setStartTime(now);
                        if ("0".equals(type)) {
                            calendar.add(Calendar.MONTH, packagesId.getNumber().intValue() * 12);
                            member.setYearEndTime(calendar.getTime());
                            member.setType("0");
                        } else if ("1".equals(type)) {
                            calendar.add(Calendar.MONTH, packagesId.getNumber().intValue());
                            member.setEndTime(calendar.getTime());
                            member.setType("1");
                        }
                        member.setGrade("1");// 改为VIP
                    } else if ("1".equals(grade)) {// 续费
                        member.setGrade("1");
                        if ("0".equals(type)) {// 续费套餐类型为年费
                            calendar.setTime(member.getYearEndTime());
                            calendar.add(Calendar.MONTH, packagesId.getNumber().intValue() * 12);
                            // 判断会员原本的vip类型，如果为月费vip，则用户先享受年费vip的权限，等年费过期后再享受月费的权限
                            if ("1".equals(member.getType())) {//本来是月费的会员续年费会员
                                Date endTime = member.getEndTime();// 用户的月费会员到期时间
                                Calendar calendar2 = Calendar.getInstance();
                                calendar2.setTime(new Date());
                                calendar2.add(Calendar.MONTH, packagesId.getNumber().intValue() * 12);
                                member.setYearEndTime(calendar2.getTime());
                                Date newEndTime = new Date(
                                        calendar2.getTime().getTime() + (endTime.getTime() - new Date().getTime()));// 年费会员用完后的月费会员到期时间
                                member.setEndTime(newEndTime);
                            } else if ("0".equals(member.getType())) {//本来是年费会员的续年费会员
                                if (member.getEndTime().after(new Date())) {//月费还没用完，这种情况是本来是月费还没用完的情况下续了年费然后再续年费的
                                    System.out.println("endTime = " + member.getEndTime().getTime() + "    newDate = " + new Date().getTime());
                                    Calendar calendar2 = Calendar.getInstance();
                                    calendar2.setTime(member.getEndTime());
                                    calendar2.add(Calendar.MONTH, packagesId.getNumber().intValue() * 12);
                                    member.setEndTime(calendar2.getTime());
                                }
                                member.setYearEndTime(calendar.getTime());
                            }
                            member.setType("0");//不能放在前面
                        } else if ("1".equals(type)) {// 续费套餐类型为月费
                            if ("1".equals(member.getType())) {//本来是月费的会员的续月费会员
                                member.setType("1");
                                Date endTime = member.getEndTime();
                                calendar.setTime(endTime);
                                calendar.add(Calendar.MONTH, 1);
                                member.setEndTime(calendar.getTime());
                            } else if ("0".equals(member.getType())) {//本来是年费会员的续月费会员
                                if (member.getEndTime().after(new Date())) {//月费还没用完，这种情况是本来是月费还没用完的情况下续了年费然后再续月费的
                                    Calendar calendar2 = Calendar.getInstance();
                                    calendar2.setTime(member.getEndTime());
                                    calendar2.add(Calendar.MONTH, 1);
                                    member.setEndTime(calendar2.getTime());
                                } else {
                                    member.setType("0");
                                    calendar.setTime(member.getYearEndTime());
                                    calendar.add(Calendar.MONTH, 1);
                                    member.setEndTime(calendar.getTime());
                                }

                            }

                        }
                    }
                    member.setIsRemind(0);
                    super.updateObject(member);
                }
            }
            ExpensePlatform pi = new ExpensePlatform();
            pi.setExpenseTotalId(order.getId());
            pi.setOrderNum(order.getNumber());
            pi.setMoney(order.getMoney());
            pi.setContent("充值");
            if ("1".equals(order.getPayType())) {
                pi.setRemark("支付宝充值成功");
            } else if ("2".equals(order.getPayType())) {
                pi.setRemark("微信充值成功");
                System.out.println("微信回调正确4平台流水》》》》》》》》》》》》》》");
            }
            pi.setAddTime(new Date());
            super.saveObject(pi);
            System.out.println("支付宝回调正确4平台流水》》》》》》》》》》》》》》");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void updateVip(ExpenseTotal order, String result) {
        // 充值成功
        if ("Y".equals(result)) {
            // 更新订单状态
            order.setPayTime(new Date());
            order.setPayStatus("1");
            buysRecordDao.update(order);
            this.updateIncomeExpense(order);//支付宝、微信和ios处理都一样，所以这里共用这个方法
        }
        // 充值失败
        if ("N".equals(result)) {
            // 更新订单状态
            order.setPayTime(new Date());
            order.setPayStatus("2");
            buysRecordDao.update(order);
        }

    }

    public void addHandlerOrder(Map<String, String> params, String result) throws ExceptionManager {
        String out_trade_no = params.get("out_trade_no");
        String total_fee = params.get("buyer_pay_amount"); // 交易金额
        String seller_id = params.get("seller_id");

        ExpenseTotal order = payDao.findRechargeOrderByPnNew(out_trade_no);
        if (order == null) {
            throw new ExceptionManager("订单不存在");
        }
        if ("1".equals(order.getPayStatus())) {
            return;
        }
        if (order.getMoney().compareTo(new BigDecimal(total_fee)) > 0 || order.getMoney().compareTo(new BigDecimal(total_fee)) < 0) {
            throw new ExceptionManager("订单金额与回调通知金额不一致");
        }
        if (!AlipayConfig.partner.equals(seller_id)) {
            throw new ExceptionManager("支付seller_id与回调通知seller_id不一致");
        }
        // 共用：支付宝或微信充值回调修改订单状态
        this.updateVip(order, result);
    }

    @Override
    public ResponseBean addWeChatVip(Member member, Long packagesId, HttpServletRequest request) throws Exception {
        Packages packages = (Packages) this.getObjectById(packagesId, Packages.class.getName());
        // 生成充值订单
        String type = "0";
        String content = "充值会员(微信)";
        Long memberId = member.getId();
        String number = OrderSnUtil.generateMemberRechargeSn();
        BigDecimal money = packages.getMoney();
        BigDecimal originalPrice = packages.getMoney();
        String payType = "1";
        ExpenseTotal br = new ExpenseTotal(type, content, memberId, number, money, originalPrice, payType);
        br.setRelatedId(packages.getId());
        super.saveObject(br);

        // 一、 生成预订单
        UnifiedOrderApp unifiedorder = new UnifiedOrderApp();
        String nonceStr = WXPayUtil.generateNonceStr();
        unifiedorder.setAppid(Configuration.getOAuthAppId());
        unifiedorder.setBody("VIP微信充值续费");
        unifiedorder.setMch_id(Configuration.getProperty("weixin4j.pay.partner.id"));
        unifiedorder.setNonce_str(nonceStr);
        unifiedorder.setNotify_url(Configuration.getProperty("weixin4j.pay.vip.notify_url"));
        unifiedorder.setOut_trade_no(br.getNumber());
        String ip = IpUtil.getIpAddr(request);
        unifiedorder.setSpbill_create_ip(ip);
        // 总费用
        String total = AmountUtil.changeY2F(packages.getMoney().toString());
        // 获取秘钥
        String paternerKey = null;
        if (payEnvironment.equals("sanbox")) {
            //沙箱环境规定金额只能是2.01：https://mp.weixin.qq.com/s/JmzFzL6fS-60jmJ7PW0Tlg
            total = AmountUtil.changeY2F("2.01");
            paternerKey = getPaternerKey();
        } else {
            paternerKey = Configuration.getProperty("weixin4j.pay.partner.key");
        }
        unifiedorder.setTotal_fee(total);
        unifiedorder.setTrade_type("APP");
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
        Response res = null;
        if (payEnvironment.equals("sanbox")) {
            // 提交xml格式数据
            res = http.postXml("https://api.mch.weixin.qq.com/sandboxnew/pay/unifiedorder", xmlPost);
        } else {
            // 提交xml格式数据
            res = http.postXml("https://api.mch.weixin.qq.com/pay/unifiedorder", xmlPost);
        }
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
                unifiedOrderResultApp.getPrepay_id(), paternerKey,
                br.getNumber());
        return new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, result);

    }

    @Override
    public void addNotifyResult(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            System.out.println("微信回调正确service进来》》》》》》》》》》》》》》");
            // 获取POST流
            ServletInputStream in = request.getInputStream();
            // 将流转换为字符串
            String xmlMsg = XStreamFactory.inputStream2String(in);

            Map<String, String> unifiedOrderResultAppMap = WXPayUtil.xmlToMap(xmlMsg);
            UnifiedOrderResultApp unifiedOrderResultApp = new UnifiedOrderResultApp();
            BeanUtils.populate(unifiedOrderResultApp, unifiedOrderResultAppMap);

            String return_code = unifiedOrderResultApp.getReturn_code();
            String out_trade_no = unifiedOrderResultApp.getOut_trade_no();
            // 查询订单
            log.error("微信支付*******************out_trade_no：" + out_trade_no);
            ExpenseTotal order = payDao.findRechargeOrderByPnNew(out_trade_no);
            if (order == null) {
                log.error("微信回调找不到订单XXXXXXXXXXXXXXXXXXXXX????????????????????");
                throw new ExceptionManager("订单不存在");
            }
            log.error("微信回调正确2找到订单》》》》》》》》》》》》》》");
            if ("1".equals(order.getPayStatus())) {
                return;
            }
            if ("2".equals(order.getPayStatus())) {
                return;
            }
            if (order != null) {
                // 判断签名及结果
                if ("SUCCESS".equals(return_code)) {
                    // 商户密钥
                    String paternerKey = null;
                    if (payEnvironment.equals("sanbox")) {
                        paternerKey = getPaternerKey();
                    } else {
                        paternerKey = Configuration.getProperty("weixin4j.pay.partner.key");
                    }
                    // 签名验证
                    //boolean result = PayUtil.verifySign(xmlMsg, paternerKey);//沙箱环境用此方法会验证不通过，得使用新版的sdk
                    boolean result = WXPayUtil.isSignatureValid(xmlMsg, paternerKey);
                    log.error("result:>>>>>" + result);
                    if (result) {
                        // 更新订单状态
                        order.setPayTime(new Date());
                        order.setPayStatus("1");
                        super.updateObject(order);
                        System.out.println("微信回调正确2订单修改成功》》》》》》》》》》》》》》");

                        // 共用：支付宝或微信充值回调修改订单状态
                        this.updateIncomeExpense(order);
                        response.getOutputStream()
                                .write("<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>"
                                        .getBytes());
                    } else {
                        // 更新订单状态
                        order.setPayTime(new Date());
                        order.setPayStatus("2");
                        super.updateObject(order);
                        response.getOutputStream()
                                .write("<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[签名失败]]></return_msg></xml>"
                                        .getBytes());
                    }
                } else {
                    // 更新订单状态
                    order.setPayTime(new Date());
                    order.setPayStatus("2");
                    super.updateObject(order);
                    System.out.println("微信回调失败1@@@@@@@@@@@@@@@@@@");
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

    @Override
    public ResponseBean addAppleVip(Member member, Long packagesId) throws Exception {
        if (null == packagesId) {
            return new ResponseBean(Constants.FAIL_CODE_400, Constants.MSG_PARAMETER_EMPTY);
        }
        Packages packages = (Packages) this.getObjectById(packagesId, Packages.class.getName());
        String type = BuyTypeEnum.VIP.getType();
        String content = BuyTypeEnum.VIP.getName();
        Long memberId = member.getId();
        String orderNum = OrderSnUtil.generateMemberRechargeSn();
        BigDecimal money = packages.getMoney();
        String payType = PayTypeEnum.APPLE.getKey();
        ExpenseTotal br = new ExpenseTotal(type, content, memberId, orderNum, money, money, payType);
        br.setRelatedId(packages.getId());
        super.saveObject(br);

        BuysRecordVo brv = new BuysRecordVo();
        brv.setId(br.getId());
        brv.setContent(br.getContent());
        brv.setMemberId(br.getMemberId());
        brv.setMoney(br.getMoney());
        // brv.setMoney(0.01);// 测试用
        brv.setNumber(br.getNumber());
        return new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, brv);

    }

    @Override
    public Object updateBuysRecordFromApple(Member member, IOSVerifyVo iosVerifyVo, HttpServletRequest request,
                                            HttpServletResponse response) throws ExceptionManager, IOException {

        // 苹果客户端传上来的收据,是最原据的收据
        String receipt = iosVerifyVo.getReceipt();
        // 拿到收据的MD5
        String md5_receipt = receipt;
        // 默认是无效账单
        String result = md5_receipt;
        // 查询订单

        ExpenseTotal order = payDao.findRechargeOrderByPnNew(iosVerifyVo.getOrderNum());
        if (order == null) {
            return new ResponseBean(Constants.SUCCESS_CODE_200, "验证成功.");
        }
        /**
         * 使用基于状态机的乐观锁来保证每条购买记录只被处理一次从而保证了数据的一致性
         */
        order.setVersion(0);
        if (1 == expenseTotalDao.updateVersion(order)) {
            //因为上面updateVersion把数据库对应的version的值变成了1，后面还会对order进行update操作，要保证数据的一致性
            order.setVersion(1);
            String verifyResult = null;
            if (order != null) {
                if ("sanbox".equals(payEnvironment)) {
                    verifyResult = IOSVerifyUtil.buyAppVerify(receipt, "Sandbox");  //向Sandbox服务器验证receipt
                } else {
                    verifyResult = IOSVerifyUtil.buyAppVerify(receipt, null);        //向生产服务器验证receipt
                }

            } else {
                return new ResponseBean(Constants.FAIL_CODE_400, Constants.MSG_FIND_NULL);
            }
            if (verifyResult == null) {
                //验证出错要把version的值更新回0，否则这条记录将无法再次被处理
                order.setVersion(0);
                expenseTotalDao.update(order);
                System.out.println("向苹果服务器验证没有返回结果，把version更新回0了");
                return new ResponseBean(Constants.FAIL_CODE_400, "苹果服务器没有返回验证结果.");
            } else {
                try {
                    JSONObject job = JSONObject.fromObject(verifyResult);
                    String status = job.getString("status");
                    log.error("---------服务器验证苹果内购服务器得到的状态码 （ " + status + " )");
                    if ("21007".equals(status)) {
                        // 状态码为“21007”，在测试服务器再验证一次
                        log.error("---------receipt是Sandbox receipt，但却发送至生产系统的验证服务");
                        String SandboxStraus = iOSVerifySandbox(request, response, iosVerifyVo, order);
                        if ("D".equals(SandboxStraus)) {
                            return new ResponseBean(Constants.FAIL_CODE_400, "苹果服务器没有返回验证结果.");
                        } else if ("200".equals(SandboxStraus)) {
                            return new ResponseBean(Constants.SUCCESS_CODE_200, "验证成功.");
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
                    } else if (status.equals("0")) {    // 验证成功
                        log.error("内购生产服验证成功");
                        String r_receipt = job.getString("receipt");
                        log.error("r_receipt：----------" + r_receipt);
                        JSONObject returnJson = JSONObject.fromObject(r_receipt);
                        log.error(returnJson.toString());
                        Calendar calendar = Calendar.getInstance();
                        if (returnJson.getString("receipt_creation_date_ms") != "" && !(((calendar.getTimeInMillis()
                                - Long.parseLong(returnJson.getString("receipt_creation_date_ms"))) / 1000) <= (3600 * 9))) {
                            //这里由于ios客服端做了处理：只要没返回200，客户端会拿收据重复请求。所以服务端不做处理也没关系
                            log.error(",手机号为: " + member.getMobile() + ", 的用户: " + member.getRealname() + " ,请求超时了,请联系开发人员协助处理。");
                            MiPushHelper.sendAndroidSysNotify("内购回调超时", "ID为：" + member.getId() + " ,手机号: " + member.getMobile() + " ,姓名: " + member.getRealname() + " ,的IOS用户充值请求超时了，请联系开发人员协助处理!", "13677336042");
                            MiPushHelper.sendIOSSysMsg("ID为：" + member.getId() + " ,手机号: " + member.getMobile() + " ,姓名: " + member.getRealname() + " ,的IOS用户充值请求超时了，请联系开发人员协助处理!", "13113365136");
//					return new ResponseBean(Constants.FAIL_CODE_400, "超出时间");
                        }
                        // 更新订单状态
                        if ("0".equals(order.getType())) {
                            this.updateAppleVip(order, "Y");
                        } else if ("7".equals(order.getType())) {
                            this.updateAppleWallet(order, "Y");
                        }
                        return new ResponseBean(Constants.SUCCESS_CODE_200, "验证成功.");
                    } else {
                        // 账单无效
                        result = "C#" + md5_receipt;
                        return new ResponseBean(Constants.FAIL_CODE_400, "账单无效 .");
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        } else {
            return new ResponseBean(Constants.EXCEPTION_CODE_500, "请不要重复操作");
        }
        return new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_FAIL, "在service最后");
    }

    public String iOSVerifySandbox(HttpServletRequest request, HttpServletResponse response, IOSVerifyVo iosVerifyVo,
                                   ExpenseTotal order) throws IOException {
        try {
            if (null != iosVerifyVo.getOrderNum() && null != iosVerifyVo.getReceipt()) {
                log.error(DateHelper.dataToString(new Date(), "yyyy年MM月dd日 HH时mm分ss秒") + "来自苹果端Sandbox的验证...");
                // 苹果客户端传上来的收据,是最原据的收据
                String receipt = iosVerifyVo.getReceipt();
                // 拿到收据的MD5
                // String md5_receipt = MD5.md5Digest(receipt);
                String md5_receipt = receipt;
                // 默认是无效账单
                String result = md5_receipt;
                // 查询数据库，看是否是己经验证过的账号
//				boolean isExists = false;
//				boolean isExists=DbServiceImpl_PNM.isExistsIOSReceipt(md5_receipt);
                String verifyResult = null;
                if (order != null) {
                    // verifyUrl null:苹果真实路径 Sandbox:沙盒测试
                    log.error(">>>>>>>>>>>>>>>>>>>>>>>>> Sandbox服务器验证之前的receipt:   " + receipt);
                    verifyResult = IOSVerifyUtil.buyAppVerify(receipt, "Sandbox");
                    int count = 0;//最多重试10次
                    if (verifyResult == null && count < 10) {
                        log.error("账单验证失败");
                        count++;
                        verifyResult = IOSVerifyUtil.buyAppVerify(receipt, "Sandbox");
                    }
                    if (verifyResult == null) {
                        // 苹果服务器没有返回验证结果
                        result = "D#" + md5_receipt;
                        return "D";
                    } else {
                        log.error("<<<<<<<<<<<<<<<<<<<<<<< Sandbox服务器验证之后返回的verifyResult:  " + verifyResult);
                        // 跟苹果验证有返回结果------------------
                        JSONObject job = JSONObject.fromObject(verifyResult);

                        String states = job.getString("status");
                        log.error("states:  " + states);
                        if (states.equals("0")) {
                            // 验证成功
                            String r_receipt = job.getString("receipt");
                            JSONObject returnJson = JSONObject.fromObject(r_receipt);
                            Calendar calendar = Calendar.getInstance();
                            if (returnJson.getString("receipt_creation_date_ms") != "" && !(((calendar.getTimeInMillis()
                                    - Long.parseLong(returnJson.getString("receipt_creation_date_ms")))
                                    / 1000) <= (3600 * 9))) {
                                return "E";// 超出时间
                            }
                            // String purchase_date=returnJson.getString("purchase_date");
                            // 保存到数据库 (更改流水，改为已支付)
                            try {
                                // 更新订单状态
                                if ("0".equals(order.getType())) {
                                    this.updateAppleVip(order, "Y");
                                } else if ("7".equals(order.getType())) {
                                    this.updateAppleWallet(order, "Y");
                                }

                            } catch (Exception e) {
                                return "F";
                            }
                            return "200";
                        } else {
                            // 账单无效
                            result = "C#" + md5_receipt;
                            return "C";
                        }
                        // 跟苹果验证有返回结果------------------
                    }
                    // 传上来的收据有购买信息==end=============
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


    private void updateAppleVip(ExpenseTotal order, String result) {
        // 充值成功
        if ("Y".equals(result)) {
            // 更新订单状态
            order.setPayTime(new Date());
            order.setPayStatus("1");
            buysRecordDao.update(order);
            this.updateAppleIncomeExpense(order, 3);

        }
        // 充值失败
        if ("N".equals(result)) {
            // 更新订单状态
            order.setPayTime(new Date());
            order.setPayStatus("2");
            buysRecordDao.update(order);
        }
    }


    /**
     * 新增积分
     * 此方法与payManagerDaoImpl里的addIntegral一模一样，如此方法要做修改，请两个方法一起改
     *
     * @param order
     */
    public void addIntegral(Long memberId, ExpenseTotal order) {
        // Integral integral =integralDao.findIntegralBymemberId(order.getMemberId());
        /**
         * 添加积分记录
         */
        Member member = memberDao.get(memberId);
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

    @Override
    public Member checkVipInfo(Member member) {
        if (member.getYearEndTime() == null) {
            member.setYearEndTime(new Date());
        }
        if (member.getEndTime() == null) {
            member.setEndTime(new Date());
        }
        if (member.getYearEndTime().before(new Date())) {// 年费过期了
            if (member.getEndTime().before(new Date())) {
                member.setGrade("0");
                member.setType("2");
            } else {
                member.setGrade("1");
                member.setType("1");
            }
        } else {
            member.setGrade("1");
            member.setType("0");
        }
        memberDao.update(member);
        return member;
    }

    @Override
    public String getVipEndTime(Member member, Integer dateFormat) {
        SimpleDateFormat sdf = null;
        if (dateFormat == 1) {
            new SimpleDateFormat("yyyy-MM-dd");
        } else if (dateFormat == 2) {
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
        Date endDate = null;
        if (member.getYearEndTime().before(new Date())) {
            if (member.getEndTime().before(new Date())) {
                member.setType("2");
                member.setGrade("0");
            } else {
                member.setGrade("1");
                member.setType("1");
                endDate = member.getEndTime();
            }
        } else {
            member.setGrade("1");
            member.setType("0");
            if (member.getEndTime().before(new Date())) {
                endDate = member.getYearEndTime();
            } else {
                endDate = member.getEndTime();
            }
        }
        if (endDate == null) {
            endDate = new Date();
        }
        log.error("endDate>>>>>>>>>>>>>>>>>>>>" + endDate);
        return sdf.format(endDate);
    }

    /**
     * IOS充值vip
     *
     * @param order
     * @param PayType
     */
    private void updateAppleIncomeExpense(ExpenseTotal order, Integer PayType) {
        try {
            Packages packagesId = (Packages) this.getObjectById(order.getRelatedId(), Packages.class.getName());
            if (null != packagesId) {
                // 充值年费会员，就送卡券
                if (packagesId.getType().equals("0")) {
                    Long[] coupnId = new Long[]{1L, 2L, 4L, 5L};
                    List<Coupn> coupns = memberDao.getCoupnById(coupnId);
                    for (Coupn coupn : coupns) {
                        CoupnUser uCoupn = new CoupnUser();
                        uCoupn.setCoupnId(coupn);
                        uCoupn.setMemberId(order.getMemberId());
                        uCoupn.setUsed("0");
                        memberDao.save(uCoupn);
                    }
                }
                Date now = new Date();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(now);
                Member member = (Member) this.getObjectById(order.getMemberId(), Member.class.getName());

                if (member.getYearEndTime() == null) {
                    member.setYearEndTime(new Date());
                }
                if (member.getEndTime() == null) {
                    member.setEndTime(new Date());
                }
                if (member.getYearEndTime().before(new Date())) {// 年费过期了
                    if (member.getEndTime().before(new Date())) {
                        member.setGrade("0");
                        member.setType("2");
                    } else {
                        member.setGrade("1");
                        member.setType("1");
                    }
                } else {
                    member.setGrade("1");
                    member.setType("0");
                }
                if (null != member) {
                    String grade = member.getGrade();
                    String type = packagesId.getType();
                    if ("0".equals(grade)) {
                        member.setInitTime(now);
                        member.setStartTime(now);
                        if ("0".equals(type)) {
                            calendar.add(Calendar.MONTH, packagesId.getNumber().intValue() * 12);
                            member.setYearEndTime(calendar.getTime());
                            member.setType("0");
                        } else if ("1".equals(type)) {
                            calendar.add(Calendar.MONTH, packagesId.getNumber().intValue());
                            member.setEndTime(calendar.getTime());
                            member.setType("1");
                        }
                        member.setGrade("1");// 改为VIP
                    } else if ("1".equals(grade)) {// 续费
                        member.setGrade("1");
                        if ("0".equals(type)) {// 续费套餐类型为年费
                            calendar.setTime(member.getYearEndTime());
                            calendar.add(Calendar.MONTH, packagesId.getNumber().intValue() * 12);
                            // 判断会员原本的vip类型，如果为月费vip，则用户先享受年费vip的权限，等年费过期后再享受月费的权限
                            if ("1".equals(member.getType())) {//本来是月费的会员续年费会员
                                Date endTime = member.getEndTime();// 用户的月费会员到期时间
                                Calendar calendar2 = Calendar.getInstance();
                                calendar2.setTime(new Date());
                                calendar2.add(Calendar.MONTH, packagesId.getNumber().intValue() * 12);
                                member.setYearEndTime(calendar2.getTime());
                                Date newEndTime = new Date(
                                        calendar2.getTime().getTime() + (endTime.getTime() - new Date().getTime()));// 年费会员用完后的月费会员到期时间
                                member.setEndTime(newEndTime);
                            } else if ("0".equals(member.getType())) {//本来是年费会员的续年费会员
                                if (member.getEndTime().after(new Date())) {//月费还没用完，这种情况是本来是月费还没用完的情况下续了年费然后再续年费的
                                    System.out.println("endTime = " + member.getEndTime().getTime() + "    newDate = " + new Date().getTime());
                                    Calendar calendar2 = Calendar.getInstance();
                                    calendar2.setTime(member.getEndTime());
                                    calendar2.add(Calendar.MONTH, packagesId.getNumber().intValue() * 12);
                                    member.setEndTime(calendar2.getTime());
                                }
                                member.setYearEndTime(calendar.getTime());
                            }
                            member.setType("0");//不能放在前面
                        } else if ("1".equals(type)) {// 续费套餐类型为月费
                            if ("1".equals(member.getType())) {//本来是月费的会员的续月费会员
                                member.setType("1");
                                Date endTime = member.getEndTime();
                                calendar.setTime(endTime);
                                calendar.add(Calendar.MONTH, 1);
                                member.setEndTime(calendar.getTime());
                            } else if ("0".equals(member.getType())) {//本来是年费会员的续月费会员
                                if (member.getEndTime().after(new Date())) {//月费还没用完，这种情况是本来是月费还没用完的情况下续了年费然后再续月费的
                                    Calendar calendar2 = Calendar.getInstance();
                                    calendar2.setTime(member.getEndTime());
                                    calendar2.add(Calendar.MONTH, 1);
                                    member.setEndTime(calendar2.getTime());
                                } else {
                                    member.setType("0");
                                    calendar.setTime(member.getYearEndTime());
                                    calendar.add(Calendar.MONTH, 1);
                                    member.setEndTime(calendar.getTime());
                                }

                            }

                        }
                    }
                    member.setIsRemind(0);
                    super.updateObject(member);
                }
            }
            ExpensePlatform pi = new ExpensePlatform();
            pi.setExpenseTotalId(order.getId());
            pi.setOrderNum(order.getNumber());
            Member mb = memberDao.get(order.getMemberId());
            pi.setMemberName(mb.getRealname());
            pi.setMemberId(order.getMemberId());
            pi.setOriginalPrice(order.getOriginalPrice());
            pi.setMoney(order.getMoney());
            pi.setPlatIncomeMoney(order.getMoney().multiply(new BigDecimal(1 - 0.3)));
            pi.setContent("充值");
            if (2 == PayType) {
                pi.setRemark("苹果内购充值成功");
            } else {
                pi.setRemark("");
            }
            pi.setAddTime(new Date());
            super.saveObject(pi);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public Member retrieveMember(String mobile) {

        return memberDao.retrieveMember(mobile);
    }

    @Override
    public Member getRecommend_times(Long recommender_id) {
        return memberDao.getRecommend_times(recommender_id);
    }

    @Override
    public Object haveRecommendId(Long id) {
        return memberDao.existRecommendId(id);
    }

    @Override
    public MemAuth getMemberAuth(MemAuth memAuth) {
        return memberDao.getMemberAuth(memAuth);
    }

    /**
     * 以下三个方法在三方登录和绑定手机处用到 更新登录记录, datebase为从数据库是查出来的对象, outer为外部注入的对象
     */
    @Override
    public void updateLoginThirdRecord(MemAuth datebase, MemAuth outer, HttpServletRequest request) {
        if (ObjValid.isValid(datebase.getNewLoginIP()) && ObjValid.isValid(datebase.getNewLoginTime())) {
            datebase.setOldLoginIP(datebase.getNewLoginIP());
            datebase.setOldLoginTime(datebase.getNewLoginTime());
        }
        datebase.setNewLoginIP(IpUtil.getIpAddr(request));
        datebase.setNewLoginTime(new Date());
        datebase.setMemberId(datebase.getMemberId());
        datebase.setLoginType(outer.getLoginType()); // 最新一次的登录类型(1为微信,2为QQ)
        /*
         * 若数据库里原本就有的数据,就是数据库里的,否则就用外部添加进来的
         */
        if (datebase.getWeiXinOpenId() != null && !"".equals(datebase.getWeiXinOpenId())) {
            datebase.setWeiXinOpenId(datebase.getWeiXinOpenId());
            datebase.setWeiXinNiceName(EmojiFilter.filterEmoji(datebase.getWeiXinNiceName()));
            datebase.setWeiXinPhoto(datebase.getWeiXinPhoto());
        } else {
            datebase.setWeiXinOpenId(outer.getWeiXinOpenId());
            datebase.setWeiXinNiceName(EmojiFilter.filterEmoji(outer.getWeiXinNiceName()));
            datebase.setWeiXinPhoto(outer.getWeiXinPhoto());
        }
        if (datebase.getQqOpenId() != null && !"".equals(datebase.getQqOpenId())) {
            datebase.setQqOpenId(datebase.getQqOpenId());
            datebase.setQqNickName(EmojiFilter.filterEmoji(datebase.getQqNickName()));
            datebase.setQqPhoto(datebase.getQqPhoto());
        } else {
            datebase.setQqOpenId(outer.getQqOpenId());
            datebase.setQqNickName(EmojiFilter.filterEmoji(outer.getQqNickName()));
            datebase.setQqPhoto(outer.getQqPhoto());
        }
        memberDao.updateLoginThirdRecord(datebase);
    }

    /*
     * 在MemAuth新增一条记录
     */
    @Override
    public void addMemAuth(MemAuth memAuth, Long memberId, HttpServletRequest request) {
        memberDao.addMemAuth(memAuth, memberId, request);
    }

    @Override
    public Member saveMember(Member member2) {
        return memberDao.saveMember(member2);
    }

    /*
     * 判断是否绑定过相同的微信或QQ号
     */
    @Override
    public MemAuth getMemberAuth(Long memberId, MemAuth memAuth) {
        return memberDao.getMemberAuth(memberId, memAuth);
    }

    @Override
    public void insertReplyFees(Long expertId) {
        HotReplyFees fees = new HotReplyFees();
        fees.setMemberId(expertId);
        fees.setMoney(new BigDecimal(0)); // 给专家添加一条初始的 付费咨询 收费记录
        fees.setPrivateMoney(new BigDecimal(0));
        memberDao.save(fees);
    }

    @Override
    public HotReplyFees getReplyFees(Long expertId) {
        return memberDao.getReplyFees(expertId);
    }

    @Override
    public void updateProMember(Member entity) {
        memberDao.updateProMember(entity);
    }

    @Override
    public MemAuth getMemberAuth(Member member1) {
        return memberDao.getMemberAuth(member1);
    }

    /**
     * 钱包充值回调新增系统消息
     *
     * @param order
     */
    public void addWalletSystemMessage(Long memberId, ExpenseTotal order) {
        SystemMessage message = new SystemMessage();
        message.setAddTime(new Date());
        message.setEditTime(new Date());
        message.setStatus("1");
        message.setType("8");
        message.setPushType("1");
        Member member = memberDao.get(memberId);
        message.setMemberIds(member.getId() + "");
        message.setTitle(SystemMessage.titleMap.get(message.getType()) + SystemMessage.buysMap.get(order.getType()));
        message.setRelateId(order.getRelatedId());
        message.setPhoto("");
        message.setDataUrl("");
        systemMessageDao.save(message);
        MiPushHelper.sendAndroidSysNotify("充值成功", message.getTitle(), member.getMobile());
    }

    /**
     * 支付宝充值钱包
     *
     * @param member
     * @param topUpMoney
     * @return
     * @throws UnsupportedEncodingException
     */
    @Override
    public ResponseBean alipayTopUp(Member member, BigDecimal topUpMoney) throws UnsupportedEncodingException {
        String type = BuyTypeEnum.WALLET.getType();
        String content = BuyTypeEnum.WALLET.getName();
        Long memberId = member.getId();
        String orderNum = OrderSnUtil.generateTopUpWalletSn();
        BigDecimal money = topUpMoney;
        String payType = PayTypeEnum.ALIPAY.getKey();
        ExpenseTotal br = new ExpenseTotal(type, content, memberId, orderNum, money, money, payType);
        super.saveObject(br);
        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient(alipayGetaway, alipayAppId, alipayPrivateKey, "json", AlipayConstants.CHARSET, alipayPublicKey, "RSA2");
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody(OBJECT);
        model.setSubject(SUBJECT);
        model.setOutTradeNo(orderNum);
        model.setTimeoutExpress(TIMEOUT_EXPRESS);
        model.setTotalAmount(topUpMoney.toString());
        model.setProductCode(PRODUCT_CODE);
        request.setBizModel(model);
        request.setNotifyUrl(alipayTopupWalletNotify);
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            //就是orderString 可以直接给客户端请求，无需再做处理。
            System.out.println(response.getBody());
            return new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, response.getBody());
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return new ResponseBean(Constants.FAIL_CODE_400, "订单初始化失败");
    }

    /**
     * 微信充值钱包
     *
     * @param member
     * @param topUpMoney
     * @param request
     * @return
     * @throws Exception
     */
    public ResponseBean wechatTopUp(Member member, BigDecimal topUpMoney, HttpServletRequest request) throws Exception {
        String type = BuyTypeEnum.WALLET.getType();
        String content = BuyTypeEnum.WALLET.getName();
        String nonceStr = WXPayUtil.generateNonceStr();
        Long memberId = member.getId();
        String orderNum = OrderSnUtil.generateMemberRechargeSn();
        BigDecimal money = topUpMoney;
        String payType = PayTypeEnum.WECHAT.getKey();
        ExpenseTotal br = new ExpenseTotal(type, content, memberId, orderNum, money, money, payType);
        super.saveObject(br);

        // 一、生成预订单
        UnifiedOrderApp unifiedorder = new UnifiedOrderApp();
        unifiedorder.setAppid(Configuration.getOAuthAppId());
        unifiedorder.setBody("钱包充值");
        unifiedorder.setMch_id(Configuration.getProperty("weixin4j.pay.partner.id"));
        unifiedorder.setNonce_str(nonceStr);
        unifiedorder.setNotify_url(Configuration.getProperty("weixin4j.pay.wallet.notify_url"));
        unifiedorder.setOut_trade_no(br.getNumber());
        String ip = IpUtil.getIpAddr(request);
        unifiedorder.setSpbill_create_ip(ip);
        if (topUpMoney == null) {
            throw new ExceptionManager("金额不能为空");
        }
        // 总费用
        String total = AmountUtil.changeY2F(topUpMoney.toString());
        // 获取密钥
        String paternerKey = null;
        if (payEnvironment.equals("sanbox")) {
            //沙箱环境规定金额只能是2.01：https://mp.weixin.qq.com/s/JmzFzL6fS-60jmJ7PW0Tlg
            total = AmountUtil.changeY2F("2.01");
            paternerKey = getPaternerKey();
        } else {
            paternerKey = Configuration.getProperty("weixin4j.pay.partner.key");
        }
        unifiedorder.setTotal_fee(total);
        unifiedorder.setTrade_type("APP");
        String sign = SignUtil.getSign(unifiedorder.toMap(), paternerKey);
        unifiedorder.setSign(sign);
        // 二、调用统一下单接口
        UnifiedOrderResultApp unifiedOrderResultApp = null;
        // 将统一下单对象转成XML
        String xmlPost = unifiedorder.toXML();
        if (Configuration.isDebug()) {
            System.out.println("调试模式_统一下单接口 提交XML数据：" + xmlPost);
        }
        // 创建请求对象
        HttpsClient http = new HttpsClient();
        Response res = null;
        if (payEnvironment.equals("sanbox")) {
            res = http.postXml("https://api.mch.weixin.qq.com/sandboxnew/pay/unifiedorder", xmlPost);
        } else {
            // 提交xml格式数据
            res = http.postXml("https://api.mch.weixin.qq.com/pay/unifiedorder", xmlPost);
        }
        // 获取微信平台下单接口返回数据
        String xmlResult = res.asString();
        try {
            JAXBContext context = JAXBContext.newInstance(UnifiedOrderResultApp.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            unifiedOrderResultApp = (UnifiedOrderResultApp) unmarshaller.unmarshal(new StringReader(xmlResult));
        } catch (JAXBException e) {
            return new ResponseBean(Constants.FAIL_CODE_400, Constants.MSG_FAIL);
        }
        // 三、返回支付参数
        if ("FAIL".equals(unifiedOrderResultApp.getReturn_code())) {
            return new ResponseBean("400", "调用统一下单接口失败", unifiedOrderResultApp);
        }
        // app支付需要的请求参数
        AppPay result = new AppPay(Configuration.getOAuthAppId(), Configuration.getProperty("weixin4j.pay.partner.id"),
                unifiedOrderResultApp.getPrepay_id(), paternerKey,
                br.getNumber());

        return new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, result);
    }

    /**
     * 支付宝充值钱包-回调逻辑处理
     */
    @Override
    public void topUpHanlder(Map<String, String> params, String result) {
        String out_trade_no = params.get("out_trade_no");
        BigDecimal total_fee = new BigDecimal(params.get("buyer_pay_amount"));// 交易金额
        String seller_id = params.get("seller_id");
        ExpenseTotal order = payDao.findRechargeOrderByPnNew(out_trade_no);
        if (order == null) {
            throw new ExceptionManager("订单不存在");
        }
        if ("1".equals(order.getPayStatus())) {
            return;
        }
        // 部署正式服的时候必须把这一段代码放出来
        if (order.getMoney().compareTo(total_fee) > 0 || order.getMoney().compareTo(total_fee) < 0) {
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
                expenseTotalDao.update(order);
                // 新增系统消息
                this.addWalletSystemMessage(order.getMemberId(), order);
                // 增加钱包余额
                Wallet wallet = walletDao.get(order.getMemberId());
                BigDecimal balance = wallet.getBalance();
                wallet.setBalance((balance.compareTo(new BigDecimal(0)) == 0 || balance.compareTo(new BigDecimal(0)) == -1) ? order.getMoney() : balance.add(order.getMoney()));
                walletDao.update(wallet);
                // 增加积分
                addIntegral(order.getMemberId(), order);
                /**
                 * 平台收入流水
                 */
                ExpensePlatform pi = new ExpensePlatform();
                pi.setExpenseTotalId(order.getId());
                pi.setOrderNum(order.getNumber());
                Member member = memberDao.get(order.getMemberId());
                pi.setMemberId(member.getId());
                pi.setMemberName(member.getRealname());
                pi.setOriginalPrice(order.getOriginalPrice());
                pi.setMoney(order.getMoney());
                pi.setPlatIncomeMoney(order.getMoney());
                pi.setContent("钱包充值");
                pi.setRemark("支付宝充值成功");
                pi.setAddTime(new Date());
                super.saveObject(pi);
            } else if ("N".equals(result)) {// 充值失败
                // 更新订单状态
                order.setPayTime(new Date());
                order.setPayStatus("2");
                expenseTotalDao.update(order);
            }
        }
    }

    @Override
    public void addWalletNotifyResult(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            log.error("微信回调正确service进来》》》》》》》》》》》》》》》》》");
            // 获取POST流
            ServletInputStream in = request.getInputStream();
            // 将流转换为字符串
            String xmlMsg = XStreamFactory.inputStream2String(in);
            Map<String, String> unifiedOrderResultAppMap = WXPayUtil.xmlToMap(xmlMsg);
            UnifiedOrderResultApp unifiedOrderResultApp = new UnifiedOrderResultApp();
            BeanUtils.populate(unifiedOrderResultApp, unifiedOrderResultAppMap);

            String return_code = unifiedOrderResultApp.getReturn_code();
            String out_trade_no = unifiedOrderResultApp.getOut_trade_no();
            log.error("微信支付**********************return_code:" + return_code);
            // 查询订单
            log.error("微信支付**********************out_trade_no:" + out_trade_no);
            ExpenseTotal order = payDao.findRechargeOrderByPnNew(out_trade_no);
            if (order == null) {
                log.error("微信回调找不到订单XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX???????????????????????");
                throw new ExceptionManager("订单不存在");
            }
            log.error("微信回调正确：找到订单》》》》》》》》》》》》》》》》》》");
            if ("1".equals(order.getPayStatus())) {// 已支付
                return;
            }
            if ("2".equals(order.getPayStatus())) {// 退款
                return;
            }
            if (order != null) {
                if ("SUCCESS".equals(return_code)) {
                    // 商户密钥
                    String paternerKey = null;
                    if (payEnvironment.equals("sanbox")) {
                        paternerKey = getPaternerKey();
                    } else {
                        paternerKey = Configuration.getProperty("weixin4j.pay.partner.key");
                    }
                    // 签名验证
                    //boolean result = PayUtil.verifySign(xmlMsg, paternerKey);//沙箱环境用此方法会验证不通过，得使用新版的sdk
                    boolean result = WXPayUtil.isSignatureValid(xmlMsg, paternerKey);
                    if (result) {
                        // 更新订单状态
                        order.setPayTime(new Date());
                        order.setPayStatus("1");
                        super.updateObject(order);
                        log.error("微信回调正确：订单修改成功》》》》》》》》》》》》》》》》》》》》");
                        // 增加钱包余额
                        Wallet wallet = walletDao.get(order.getMemberId());
                        BigDecimal balance = wallet.getBalance();
                        wallet.setBalance((balance.compareTo(new BigDecimal(0)) == 0 || balance.compareTo(new BigDecimal(0)) == -1) ? order.getMoney() : balance.add(order.getMoney()));
                        walletDao.update(wallet);
                        // 增加积分
//                        Integral integral = integralDao.findIntegralBymemberId(order.getMemberId());
                        addIntegral(order.getMemberId(), order);
                        /**
                         * 平台收入流水
                         */
                        ExpensePlatform pi = new ExpensePlatform();
                        pi.setExpenseTotalId(order.getId());
                        pi.setOrderNum(order.getNumber());
                        Member member = memberDao.get(order.getMemberId());
                        pi.setMemberName(member.getRealname());
                        pi.setMemberId(member.getId());
                        pi.setOriginalPrice(order.getOriginalPrice());
                        pi.setMoney(order.getMoney());
                        pi.setPlatIncomeMoney(order.getMoney());
                        pi.setContent("钱包充值");
                        pi.setRemark("微信充值成功");
                        pi.setAddTime(new Date());
                        super.saveObject(pi);
                        // 新增系统消息
                        this.addWalletSystemMessage(order.getMemberId(), order);

                        response.getOutputStream()
                                .write("<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>"
                                        .getBytes());
                    } else {
                        log.error("**************************微信回调签名校验失败fail************************************");
                        // 更新订单状态
                        order.setPayTime(new Date());
                        order.setPayStatus("2");
                        super.updateObject(order);
                        log.error("**************************微信回调签名校验失败fail************************************");
                        response.getOutputStream()
                                .write("<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[签名失败]]></return_msg></xml>"
                                        .getBytes());
                    }
                } else {
                    // 更新订单状态
                    order.setPayTime(new Date());
                    order.setPayStatus("2");
                    super.updateObject(order);
                    log.error("微信回调失败@@@@@@@@@@@@@@@@@@@@@@@");
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
     * 获取微信沙箱秘钥
     */
    private String getPaternerKey() {
        Map<String, String> getSandboxSignkeyUnifiedOrder = new HashMap<String, String>();
        getSandboxSignkeyUnifiedOrder.put("mch_id", Configuration.getProperty("weixin4j.pay.partner.id"));
        getSandboxSignkeyUnifiedOrder.put("nonce_str", WXPayUtil.generateNonceStr());
        //使用生产的key对参数进行签名
        String generateSignedXml = null;
        try {
            generateSignedXml = WXPayUtil.generateSignedXml(getSandboxSignkeyUnifiedOrder, Configuration.getProperty("weixin4j.pay.partner.key"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //获取沙箱key
        return SignUtil.getSanboxSign(generateSignedXml);
    }

    @Override
    public Object addAppleWallet(Member member, BigDecimal topUpMoney) throws Exception {
        String type = "7";
        String content = "钱包充值(苹果内购)";
        Long memberId = member.getId();
        String number = OrderSnUtil.generateTopUpWalletSn();
        BigDecimal money = topUpMoney;
        String payType = "3";
        ExpenseTotal br = new ExpenseTotal(type, content, memberId, number, money, money, payType);
        super.saveObject(br);
        return new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, br);
    }

    private void updateAppleWallet(ExpenseTotal order, String result) {
        // 充值成功
        if ("Y".equals(result)) {
            // 更新订单状态
            order.setPayTime(new Date());
            order.setPayStatus("1");
            expenseTotalDao.update(order);
            // 增加积分
            addIntegral(order.getMemberId(), order);

            // 增加钱包余额
            Wallet wallet = walletDao.get(order.getMemberId());
            BigDecimal balance = wallet.getBalance();
            wallet.setBalance(balance.compareTo(new BigDecimal(0)) == -1 ? order.getMoney() : balance.add(order.getMoney()));
            walletDao.update(wallet);
            // 平台收入流水
            ExpensePlatform pi = new ExpensePlatform();
            pi.setExpenseTotalId(order.getId());
            Member member = memberDao.get(order.getMemberId());
            pi.setMemberId(member.getId());
            pi.setMemberName(member.getRealname());
            pi.setOrderNum(order.getNumber());
            pi.setOriginalPrice(order.getOriginalPrice());
            pi.setMoney(order.getMoney());
            pi.setPlatIncomeMoney(order.getMoney());
            pi.setContent("钱包充值");
            pi.setRemark("苹果内购");
            pi.setAddTime(new Date());
            super.saveObject(pi);
        }
        // 充值失败
        if ("N".equals(result)) {
            order.setPayTime(new Date());
            order.setPayStatus("2");
            expenseTotalDao.update(order);
        }
    }

    @Override
    public Member getUUID(String uuid) {
        return memberDao.getUUID(uuid);
    }

    @Override
    public Member getMobileById(Long memberId) {
        return memberDao.getMobileById(memberId);
    }

    @Override
    public List<CoupnUser> getUserCoupnByMemberIdAndType(String type, Long memberId, int pageNo, int pageSize,
                                                         int videoId) {
        List<CoupnUser> coupns = memberDao.getUserCoupnByMemberIdAndType(type, memberId, pageNo, pageSize, videoId);
        List<CoupnUser> resultList = new ArrayList<CoupnUser>();
        List<CoupnUser> list = new ArrayList<CoupnUser>();
        if (coupns != null && coupns.size() > 0) {
            for (CoupnUser entity : coupns) {
                String scopeNum = entity.getCoupnId().getScopeNum();
                if (scopeNum.contains(",")) {
                    String[] scope = scopeNum.split(",");
                    for (int j = 0; j < scope.length; j++) {
                        if (type.equals(scope[j])) {
                            list.add(entity);
                        }
                    }
                } else {
                    if (type.equals(entity.getCoupnId().getScopeNum())) {
                        list.add(entity);
                    }
                }
            }
        }
        if (list.size() > (pageNo - 1) * pageSize) {
            return list;
        } else {
            outer:
            for (int i = (pageNo - 1) * pageSize; i <= list.size() - 1; i++) {
                resultList.add(list.get(i));
                if (resultList.size() >= pageSize) {
                    break outer;
                }
            }
            return resultList;
        }

    }

    @Override
    public List<CoupnUser> getAllUserCoupn(Long memberId, String used) {
        return memberDao.getAllUserCoupn(memberId, used);
    }

    @Override
    public List<CoupnUser> getCoupnsById(Long[] coupnId) {
        return memberDao.getCoupnsById(coupnId);
    }

    @Override
    public void updateUserCoupn(CoupnUser coupnUser) {
        memberDao.updateUserCoupn(coupnUser);
    }

    @Override
    public void updateCoupn(Coupn coupnId) {
        memberDao.updateCoupn(coupnId);
    }

    @Override
    public List<CoupnUser> getAllUserCoupn(Long memberId, int pageNo, int pageSize) {
        return memberDao.getAllUserCoupn(memberId, pageNo, pageSize);
    }

    @Override
    public List<Coupn> getCoupnById(Long[] coupnId) {
        return memberDao.getCoupnById(coupnId);
    }

    @Override
    public void save(CoupnUser uCoupn) {
        memberDao.save(uCoupn);
    }

    @Override
    public ExpenseTotal checkBuyStatus(Member memberId, String stype, Long[] relateId) {
        return memberDao.checkBuyStatus(memberId, stype, relateId);
    }

    @Override
    public int memberNum() {
        return memberDao.memberNum();
    }

    @Override
    public void save(Object object) {
        memberDao.save(object);
    }

    @Override
    public List<WechatWeiboAttention> retrieveAttention() {
        return memberDao.retrieveAttention();
    }

    @Override
    public Fusing fusingTimes(String interfaces) {
        return memberDao.fusingTimes(interfaces);
    }

    @Override
    public void updateFusing(Fusing fusing) {
        memberDao.update(fusing);
    }

    @Override
    public int maxAttentionNum(int typeNum) {
        return memberDao.maxAttentionNum(typeNum);
    }

    @SuppressWarnings("static-access")
    @Override
    public RedeemCodeVo exchangeCode(String redeemCode, Member member) {
        RedeemCodeOwner owner = memberDao.isExistUnUsedCode(redeemCode);
        boolean bok = false;
        if (owner != null) {
            bok = memberDao.isCanExchange(owner.getRedeemCodeId(), member.getId());
        }
        if (bok == true) {
            RedeemCodeVo vo = new RedeemCodeVo();
            RedeemCodeStock stock = memberDao.getRedeemStockById(owner.getRedeemCodeId());
            //产品类型 0：卡券 　1：文章 　2：视频课程  3：会员
            String number = OrderSnUtil.generateMemberRechargeSn();
            ExpenseTotal buys = new ExpenseTotal();
            buys.setAddTime(new Date());
            buys.setCoupnMoney(new BigDecimal(0));
            buys.setMemberId(member.getId());
            if (stock.getProductType() == 0) {
                vo.setUri("weikai://cert-map?target=card");  //跳转到卡券
                //0:购买会员 1:准入报告 2:标准解读 3:质量分享 4:付费咨询 5:新闻详情 6:电商准入报告 7:钱包充值 8:视频或直播 9:专栏订阅
                Coupn coupn = memberDao.getCoupnById(stock.getRelatedId());
                //先判断是否为全场通用券
                if (coupn.getScopeNum().contains(",") || coupn.getScopeNum().contains("，")) {
                    vo.setProductName("恭喜您,成功兑换一张全场通用券");
                } else {
                    //再判断是否为某一个模块的全场通用券
                    if (coupn.getVideoId() == 0) {
                        if (coupn.getScopeNum().equals("1")) {
                            vo.setProductName("恭喜您,成功兑换一张\"市场准入报告\"全场通用券");
                        } else if (coupn.getScopeNum().equals("2")) {
                            vo.setProductName("恭喜您,成功兑换一张\"标准解读\"全场通用券");
                        } else if (coupn.getScopeNum().equals("3")) {
                            vo.setProductName("恭喜您,成功兑换一张\"质量分享\"全场通用券");
                        } else if (coupn.getScopeNum().equals("4")) {
                            vo.setProductName("恭喜您,成功兑换一张\"付费咨询\"全场通用券");
                        } else if (coupn.getScopeNum().equals("6")) {
                            vo.setProductName("恭喜您,成功兑换一张\"电商准入报告\"全场通用券");
                        } else if (coupn.getScopeNum().equals("8")) {
                            vo.setProductName("恭喜您,成功兑换一张\"视频课程\"全场通用券");
                        } else if (coupn.getScopeNum().equals("9")) {
                            vo.setProductName("恭喜您,成功兑换一张\"专栏订阅\"全场通用券");
                        }
                    } else {
                        if (coupn.getScopeNum().equals("1")) {
                            vo.setProductName("恭喜您,成功兑换一张市场准入报告卡券,\r\n仅可用于\"" + coupn.getUseScope() + "\"这一份市场准入报告");
                        } else if (coupn.getScopeNum().equals("2")) {
                            vo.setProductName("恭喜您,成功兑换一张标准解读卡券,\r\n仅可用于\"" + coupn.getUseScope() + "\"这一篇文章");
                        } else if (coupn.getScopeNum().equals("3")) {
                            vo.setProductName("恭喜您,成功兑换一张质量分享卡券,\r\n仅可用于\"" + coupn.getUseScope() + "\"这一篇文章");
                        } else if (coupn.getScopeNum().equals("4")) {
                            vo.setProductName("恭喜您,成功兑换一张付费咨询卡券,\r\n仅可用于\"" + coupn.getUseScope() + "\"这一个付费咨询");
                        } else if (coupn.getScopeNum().equals("6")) {
                            vo.setProductName("恭喜您,成功兑换一张电商准入报告卡券,\r\n仅可用于\"" + coupn.getUseScope() + "\"这一份电商准入报告");
                        } else if (coupn.getScopeNum().equals("8")) {
                            vo.setProductName("恭喜您,成功兑换一张视频课程卡券,\r\n仅可用于\"" + coupn.getUseScope() + "\"这一个视频课程");
                        } else if (coupn.getScopeNum().equals("9")) {
                            vo.setProductName("恭喜您,成功兑换一张专栏订阅卡券,\r\n仅可用于\"" + coupn.getUseScope() + "\"这一张专栏订阅");
                        }

                    }
                }
                CoupnUser coupnUser = new CoupnUser();
                coupnUser.setMemberId(member.getId());
                coupnUser.setMemberName(member.getRealname());
                coupnUser.setCoupnId(coupn);
                coupnUser.setUsed("0");
                coupnUser.setSharer("");
                coupnUser.setSharerId(0L);
                memberDao.addUserCoupn(coupnUser);
            } else if (stock.getProductType() == 1) {
                vo.setUri("weikai://cert-map?target=bought");  //跳转到已购
                StandardReading reading = memberDao.getStandardById(stock.getRelatedId());
                buys.setMoney(new BigDecimal(0)); //用兑换码消费,是免费的活动
                buys.setOriginalPrice(reading.getMoney());
                if (reading.getClassify().equals("1")) {
                    buys.setType("2");
                    buys.setContent("购买标准解读(兑换码支付)");
                } else if (reading.getClassify().equals("2")) {
                    buys.setType("3");
                    buys.setContent("购买质量分享(兑换码支付)");
                }
                if (reading.getType().equals("1")) {
                    vo.setProductName("恭喜您,成功兑换了\r\n\"" + reading.getTitle() + "\"订阅专栏");
                } else if (reading.getType().equals("0")) {
                    vo.setProductName("恭喜您,成功兑换了\r\n\"" + reading.getTitle() + "\"文章");
                }

            } else if (stock.getProductType() == 2) {
                vo.setUri("weikai://cert-map?target=bought");  //跳转到已购
                LiveVideo live = memberDao.getLiveVideoById(stock.getRelatedId());
                buys.setMoney(new BigDecimal(0));//用兑换码消费,是免费的活动
                buys.setOriginalPrice(live.getPrice());
                buys.setType("8");
                buys.setContent("购买视频(兑换码支付)");
                vo.setProductName("恭喜您,成功兑换了\r\n\"" + live.getTitle() + "\"课程");
            } else if (stock.getProductType() == 3) {
                vo.setUri("weikai://cert-map?target=vip");
                buys.setMoney(new BigDecimal(0));
                Packages pack = memberDao.getPackages(stock.getRelatedId());
                buys.setOriginalPrice(pack.getMoney().multiply(new BigDecimal(stock.getNumber())));
                buys.setType("0");
                buys.setContent("兑换码充值会员");
                Date date = new Date();//取时间
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(date);
                if (stock.getRelatedId() == 0) {//年费会员
                    vo.setProductName("恭喜您,成功兑换了\r\n" + stock.getNumber() + "年会员");
                    member.setGrade("1");
                    /**
                     * 若当前是年费会员, 则在当前年费会员时间的基础上加上赠送的年费会员的时间
                     * 若当前是月费会员, 则在当前月费会员剩余时间的基础上加上赠送的年费会员的时间
                     * 若当前不是会员, 则在当前时间的基础上加上赠送的年费会员的时间
                     */
                    calendar.add(calendar.YEAR, 1 * stock.getNumber());//把日期往后增加多少年.整数往后推,负数往前移动
                    date = calendar.getTime();
                    if (member.getType().equals("0")) {
                        if (member.getEndTime().after(new Date())) {  //月费未用完
                            Calendar calendar1 = new GregorianCalendar();
                            calendar1.setTime(member.getEndTime());
                            calendar1.add(calendar1.YEAR, stock.getNumber().intValue());
                            member.setEndTime(calendar1.getTime());
                            Calendar calendar2 = new GregorianCalendar();
                            calendar2.setTime(member.getYearEndTime());
                            calendar2.add(calendar2.YEAR, stock.getNumber().intValue());
                            member.setYearEndTime(calendar2.getTime());
                            member.setType("0");
                        } else {  //月费用完
                            Calendar calendar1 = new GregorianCalendar();
                            calendar1.setTime(member.getYearEndTime());
                            calendar1.add(calendar1.YEAR, stock.getNumber().intValue());
                            member.setYearEndTime(calendar1.getTime());
                        }
                    } else if (member.getType().equals("1")) {
                        Calendar calendar1 = new GregorianCalendar();
                        calendar1.setTime(member.getEndTime());
                        calendar1.add(calendar1.YEAR, stock.getNumber().intValue());
                        member.setEndTime(calendar1.getTime());
                        member.setType("0");
                        member.setYearEndTime(date);
                    } else {
                        member.setEndTime(date);
                        member.setYearEndTime(date);
                        member.setType("0");
                    }
                } else if (stock.getRelatedId() == 1L) { //月费会员
                    vo.setProductName("恭喜您,成功兑换了\r\n" + stock.getNumber() + "个月会员");
                    member.setGrade("1");
                    /**
                     * 若当前是年费会员, 则在当前年费会员剩余时间的基础上加上赠送的月费会员的时间
                     * 若当前是月费会员, 则在当前月费会员时间的基础上加上赠送的月费会员的时间
                     * 若当前不是会员, 则在当前时间的基础上加上赠送的月费会员的时间
                     */
                    calendar.add(calendar.MONTH, 1 * stock.getNumber());//把日期往后增加多少月.整数往后推,负数往前移动
                    date = calendar.getTime();
                    if (member.getType().equals("0")) {
                        if (member.getEndTime().after(new Date())) {  //月费未用完
                            Calendar calendar1 = new GregorianCalendar();
                            calendar1.setTime(member.getEndTime());
                            calendar1.add(calendar1.MONTH, stock.getNumber().intValue());
                            member.setEndTime(calendar1.getTime());
                        } else {  //月费用完
                            Calendar calendar1 = new GregorianCalendar();
                            calendar1.setTime(member.getYearEndTime());
                            calendar1.add(calendar1.MONTH, stock.getNumber().intValue());
                            member.setEndTime(calendar1.getTime());
                        }
                    } else if (member.getType().equals("1")) {
                        Calendar calendar1 = new GregorianCalendar();
                        calendar1.setTime(member.getEndTime());
                        calendar1.add(calendar1.MONTH, stock.getNumber().intValue());
                        member.setEndTime(calendar1.getTime());
                    } else {
                        member.setType("1");
                        member.setEndTime(date);
                    }
                }
                //更改会员信息
                memberDao.save(member);
            }
            buys.setPayStatus("1");
            buys.setPayTime(new Date());
            buys.setPayType("5");
            ;  //兑换码支付
            buys.setExpertId(0L);
            buys.setNumber(number);
            buys.setRelatedId(stock.getRelatedId());
            //只有消费兑换码时,兑换的是实际产品, 才需要写入流水记录,  兑换成卡券不需要写入
            if (stock.getProductType() != 0L) {
                expenseTotalDao.save(buys);
            }
            memberDao.updateRedeemOwn(owner, member);
            vo.setRelatedId(stock.getRelatedId());
            return vo;
        }
        return null;
    }

    @Override
    public Coupn coupnByVideoId(int videoId) {
        return memberDao.coupnByVideoId(videoId);
    }

    @Override
    public int isExistCode(String redeemCode, Member member) {
        RedeemCodeOwner code = memberDao.isExistCode(redeemCode);
        //是否存在此兑换码
        if (code != null) {
            //此兑换码是否已使用
            RedeemCodeOwner owner = memberDao.isExistUnUsedCode(redeemCode);
            if (owner == null) {
                return 1;
            } else {
                //判断该用户是否已经参与过同一类型的活动
                boolean actBok = memberDao.isSameTypeActivity(owner.getRedeemCodeId(), member.getId());
                if (actBok == true) {
                    return 2;
                } else {
                    //此兑换码是否可用
                    boolean bok = memberDao.isCanExchange(owner.getRedeemCodeId(), member.getId());
                    if (bok == true) {
                        return 3;
                    }
                }
            }
        } else {
            return 0;
        }
        return 1;
    }


    @Override
    public List<Member> getMembersByMemberIds(List<Long> memberIds) {
        return memberDao.getMembersByMemberIds(memberIds);
    }

    @Override
    public void updateCouponExpire() {
        List<Coupn> list = memberDao.getCouponList(new Date());
        for (Coupn coupn : list) {
            //卡券期限已到,更改使用状态
            coupn.setUsable("0");
            memberDao.updateCoupn(coupn);
        }
    }

    @SuppressWarnings("static-access")
    @Override
    public void couponExpireRemind() {
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE, 3);//取三天之后的此时此刻
        List<Coupn> clist = memberDao.getCouponNotExpire(calendar.getTime());
        if (clist != null && clist.size() > 0) {
            for (Coupn coupn : clist) {
                if (coupn.getIsRemind() == 0) {
                    List<Long> memberList = memberDao.getUnusedCouponPerson(coupn.getId());
                    if (memberList != null && memberList.size() > 0) {
                        for (Long memberId : memberList) {
                            SystemMessage message = new SystemMessage();
                            Member member = memberDao.get(memberId);
                            String openFile = "weikai://cert-map?target=card";
                            String description = "";
                            if (coupn.getScopeNum().equals("2")) { //资料(质量分享 和 订阅专栏)
                                if (coupn.getVideoId() != 0) {
                                    StandardReading reading = standardReadDao.get(Long.valueOf(String.valueOf(coupn.getVideoId())));
                                    //券后金额有可能会出现负数的情况, 所以要判断
                                    BigDecimal money = reading.getMoney().subtract(coupn.getCoupnMoney()).compareTo(new BigDecimal(0)) == 1 ? reading.getMoney().subtract(coupn.getCoupnMoney()) : new BigDecimal(0);
                                    if (money.compareTo(new BigDecimal(0)) == 1) {
                                        description = "Hey! 您有" + coupn.getCoupnMoney() + "元卡券将于" + DateHelper.dataToString(coupn.getEndDate(), "MM月dd日") + "（3天）到期，可用于抵扣资料 《" + reading.getTitle() + "》！（券后仅" + money + "元），快打开世界认证地图APP使用吧！";
                                        message.setTitle(description);
                                    } else {
                                        description = "Hey! 您有" + coupn.getCoupnMoney() + "元卡券将于" + DateHelper.dataToString(coupn.getEndDate(), "MM月dd日") + "（3天）到期，可用于抵扣资料 《" + reading.getTitle() + "》，快打开世界认证地图APP使用吧！";
                                        message.setTitle(description);
                                    }
                                    message.setPhoto(reading.getCover());
                                    message.setRelateId(reading.getId());
                                    if (reading.getType().equals("1") && reading.getFatherId() == 0L) {
                                        message.setType("9");
                                    } else {
                                        if (reading.getClassify().equals("1")) {
                                            message.setType("2");
                                        } else if (reading.getClassify().equals("2")) {
                                            message.setType("3");
                                        }
                                    }
                                } else {
                                    description = "Hey! 您有" + coupn.getCoupnMoney() + "元卡券将于" + DateHelper.dataToString(coupn.getEndDate(), "MM月dd日") + "（3天）到期，可用于抵扣可用于抵扣质量分享、订阅专栏模块消费！快打开世界认证地图APP使用吧！";
                                    message.setTitle(description);
                                    message.setPhoto("");
                                    message.setType("3");
                                }
                            } else if (coupn.getScopeNum().equals("4")) {  //付费咨询
                                if (coupn.getVideoId() != 0) {
                                    //付费咨询暂未考虑到 券后金额,所以就暂时注释掉金额这一块
                                    HotReply hotReply = hotReplyDao.getHotReply(Long.valueOf(String.valueOf(coupn.getVideoId())));
//								double money=0.00;
//								ReplyFees fees=hotReplyDao.getReplyFees(hotReply.getExpertId());
                                    //提问方式(0:公开,1:私密)
//								if(hotReply.getType().equals("0")) {  
//									money=fees.getMemberId()-coupn.getCoupnMoney();
//								}else {
//									money=fees.getPrivateMoney()-coupn.getCoupnMoney();
//								}
                                    Member expert = memberDao.get(hotReply.getExpertId());
                                    description = "Hey! 您有" + coupn.getCoupnMoney() + "元卡券将于" + DateHelper.dataToString(coupn.getEndDate(), "MM月dd日") + "（3天）到期，可用于抵扣" + expert.getRealname() + "专家付费咨询一对一提问！快打开世界认证地图APP使用吧！";
                                    message.setTitle(description);
                                    message.setPhoto(expert.getPhoto());
                                    message.setRelateId(expert.getId());
                                } else {
                                    description = "Hey! 您有" + coupn.getCoupnMoney() + "元卡券将于" + DateHelper.dataToString(coupn.getEndDate(), "MM月dd日") + "（3天）到期，可用于抵扣付费咨询专家一对一提问！快打开世界认证地图APP使用吧！";
                                    message.setTitle(description);
                                    message.setPhoto("");
                                }
                                message.setType("4");
                            } else if (coupn.getScopeNum().equals("8")) {  //视频课程
                                if (coupn.getVideoId() != 0) {
                                    LiveVideo video = liveDao.getLiveVideoById(Long.valueOf(String.valueOf(coupn.getVideoId())));
                                    BigDecimal money = video.getPrice().subtract(coupn.getCoupnMoney()).compareTo(new BigDecimal(0)) == 1 ? video.getPrice().subtract(coupn.getCoupnMoney()) : new BigDecimal(0);
                                    if (money.compareTo(new BigDecimal(0)) == 1) {
                                        description = "Hey! 您有" + coupn.getCoupnMoney() + "元卡券将于" + DateHelper.dataToString(coupn.getEndDate(), "MM月dd日") + "（3天）到期，可用于抵扣视频课程《" + video.getTitle() + "》！（券后仅" + money + "元），快打开世界认证地图APP使用吧！";
                                        message.setTitle(description);
                                    } else {
                                        description = "Hey! 您有" + coupn.getCoupnMoney() + "元卡券将于" + DateHelper.dataToString(coupn.getEndDate(), "MM月dd日") + "（3天）到期，可用于抵扣视频课程《" + video.getTitle() + "》，快打开世界认证地图APP使用吧！";
                                        message.setTitle(description);
                                    }
                                    message.setPhoto(video.getCover());
                                    message.setRelateId(video.getId());
                                } else {
                                    description = "Hey! 您有" + coupn.getCoupnMoney() + "元卡券将于" + DateHelper.dataToString(coupn.getEndDate(), "MM月dd日") + "（3天）到期，可用于抵扣视频课程模块的消费！快打开世界认证地图APP使用吧！";
                                    message.setTitle(description);
                                    message.setPhoto("");
                                }
                                message.setType("10");
                            } else if (coupn.getScopeNum().contains(",") || coupn.getScopeNum().contains("，")) {  //全场通用券
                                description = "Hey! 您有" + coupn.getCoupnMoney() + "元卡券将于" + DateHelper.dataToString(coupn.getEndDate(), "MM月dd日") + "（3天）到期，可用于抵扣质量分享、订阅专栏、视频课程、付费咨询等各模块消费！快打开世界认证地图APP使用吧！";
                                message.setTitle(description);
                                message.setPhoto("");
                                message.setType("11");
                            }
                            message.setAddTime(new Date());
                            message.setEditTime(new Date());
                            message.setMemberIds(member.getId().toString());
                            message.setPushType("1");
                            message.setRegionId(0L);
                            message.setStatus("1");
                            systemMessageDao.save(message);
                            MiPushHelper.sendAndroidUserAccount("卡券到期提醒", description, openFile, member.getMobile());
                            MiPushHelper.sendIOSUserAccount(description, openFile, member.getMobile());
                            /*
                             * 更新卡券的推送提醒状态
                             */
                            coupn.setIsRemind(1);
                            memberDao.updateCoupn(coupn);
                        }
                    }
                }
            }
        }

    }

    @SuppressWarnings("static-access")
    @Override
    public void memberExpireRemind() {
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE, 3);//取三天之后的此时此刻
        List<Member> mlist = memberDao.getMemberNotExpire(calendar.getTime());
        if (mlist != null && mlist.size() > 0) {
            for (Member member : mlist) {
                if (member.getIsRemind() == 0) {
                    SystemMessage message = new SystemMessage();
                    String description = "";
                    String openFile = "weikai://cert-map?target=vip";
                    if (member.getType().equals("0")) {
                        description = "尊敬的年费会员，您的会员服务将于3天后到期，届时将不能享有年费VIP免费查看权限，如有需要，可至APP内续开年费VIP，感谢您的关注！";
                    } else if (member.getType().equals("1")) {
                        description = "尊敬的月费会员，您的会员服务将于3天后到期，届时将不能享有月费VIP免费查看权限，如有需要，可至APP内续开月费VIP，感谢您的关注！";
                    }
                    message.setTitle(description);
                    message.setAddTime(new Date());
                    message.setEditTime(new Date());
                    message.setMemberIds(member.getId().toString());
                    message.setPushType("1");
                    message.setStatus("1");
                    message.setType("12");
                    message.setPhoto("");
                    message.setRemark("");
                    message.setDataUrl(openFile);
                    systemMessageDao.save(message);
                    MiPushHelper.sendAndroidUserAccount("会员到期提醒", description, openFile, member.getMobile());
                    MiPushHelper.sendIOSUserAccount(description, openFile, member.getMobile());
                    /*
                     * 更新会员的推送提醒状态
                     */
                    member.setIsRemind(1);
                    memberDao.update(member);
                }
            }
        }
    }

    @SuppressWarnings("static-access")
    @Override
    public void classesToRemind() {
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.MINUTE, 30);//取30分钟之后的此时此刻
        List<LiveVideo> list = memberDao.getLiveAirOn(calendar.getTime());  //获取30分钟之后即将开播的直播列表
        if (list != null && list.size() > 0) {
            for (LiveVideo video : list) {
                SystemMessage message = new SystemMessage();
                Member member = memberDao.get(video.getExpertId());
                String description = "小图喊你上课了：您的课程《" + video.getTitle() + "》将于30分钟后开始，请及时登录APP上课哟！";
                String openFile = "";
                if (video.getClassify() == 1) {  //系列课程直播
                    openFile = "weikai://cert-map?target=live&id=" + video.getId() + "&type=2";
                } else if (video.getClassify() == 4) {  //单一课程直播
                    openFile = "weikai://cert-map?target=live&id=" + video.getId() + "&type=1";
                }
                message.setTitle(description);
                message.setAddTime(new Date());
                message.setEditTime(new Date());
                message.setMemberIds(member.getId().toString());
                message.setPushType("1");
                message.setStatus("1");
                message.setType("10");
                message.setPhoto("");
                message.setRemark("");
                message.setDataUrl(openFile);
                systemMessageDao.save(message);
                MiPushHelper.sendAndroidUserAccount("到期提醒", description, openFile, member.getMobile());
                MiPushHelper.sendIOSUserAccount(description, openFile, member.getMobile());
            }
        }
    }

    /**
     * 保存卡券并保存卡券的信息
     */
    @Override
    public Coupn saveCoupn(Coupn coupn) {
        return memberDao.saveCoupn(coupn);
    }

    @Override
    public void saveMemberMp(MemberMp mp) {
        if (mp != null) {
            memberDao.save(mp);
        }
    }

    @Override
    public MemberMp findMemberMpByMemberId(Long memberId) {
        if (memberId != null) {
            MemberMp memberMp = memberDao.findMemberMpByMemberId(memberId);
            return memberMp;
        }
        return null;
    }

    @Override
    public void updateMemberMp(MemberMp memberMp) {
        if (memberMp != null) {
            memberDao.update(memberMp);
        }
    }

    @Override
    public MemberMp findMemberMpByOpenId(String openId) {
        if (openId != null && !"".equals(openId)) {
            MemberMp memberMp = memberDao.findMemberMpByOpenId(openId);
            return memberMp;
        }
        return null;
    }

    @Override
    public BankMember saveBankMember(BankMember bankMember) {
        return memberDao.saveBankMember(bankMember);
    }

    @Override
    public MemberMp findMemberMpByOpenIdAndMemberId(Long memberId, String openId) {
        if (memberId != null && openId != null) {
            MemberMp memberMp = memberDao.findMemberMpByOpenIdAndMemberId(memberId, openId);
        }
        return null;
    }

    @Override
    public BigDecimal getDistributorIntegral(Long memberId) {
        List<LiveVideoDistributor> distribuList = activityExchangeRecordDao.getDistributorByMemberId(memberId);
        BigDecimal integral = new BigDecimal(0);
        if (CollectionUtil.isListNotEmpty(distribuList)) {
            for (LiveVideoDistributor distributor : distribuList) {
                integral = integral.add(new BigDecimal(distributor.getIntegral()));
            }
        }
        return integral;
    }

    @Override
    public void subtractDistributorIntegral(Long memberId, BigDecimal spendIntegral) {
        List<LiveVideoDistributor> distributorList = activityExchangeRecordDao.getDistributorByMemberId(memberId);
        BigDecimal myIntegral = new BigDecimal(0);
        bok:
        for (int i = 0; i <= distributorList.size() - 1; i++) {
            myIntegral = myIntegral.add(new BigDecimal(distributorList.get(i).getIntegral()));
            if (myIntegral.compareTo(spendIntegral) == -1) {
                distributorList.get(i).setIntegral(0);
                integralDao.updateLiveDistributor(distributorList.get(i));
            } else {
                //最后一条要扣除的live_video_distributor中的积分记录
                distributorList.get(i).setIntegral(myIntegral.subtract(spendIntegral).intValue());
                integralDao.updateLiveDistributor(distributorList.get(i));
                break bok;
            }
        }
    }
}
