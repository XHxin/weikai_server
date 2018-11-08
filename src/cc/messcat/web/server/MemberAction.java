package cc.messcat.web.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cc.messcat.entity.*;
import cc.modules.constants.AlipayConstants;
import cc.modules.util.*;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.mipush.MiPushHelper;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.alipay.util.AlipayNotify;
import com.em.api.EasemobIMUsers;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.opensymphony.xwork2.ActionContext;
import cc.messcat.enums.RegistSourceEnum;
import cc.messcat.vo.AboutusVo;
import cc.messcat.vo.ExpenseTotalVo;
import cc.messcat.vo.CollectAndBuysCountVo;
import cc.messcat.vo.CollectListVo;
import cc.messcat.vo.CollectVo;
import cc.messcat.vo.CoupnVo;
import cc.messcat.vo.EnterpriseNewsVo;
import cc.messcat.vo.ExpertIEListVo;
import cc.messcat.vo.FileResult;
import cc.messcat.vo.IOSVerifyVo;
import cc.messcat.vo.IntegralAndVirtualConVo;
import cc.messcat.vo.MemberVo;
import cc.messcat.vo.MyBuysListResult;
import cc.messcat.vo.PackageVo;
import cc.messcat.vo.RedeemCodeVo;
import cc.messcat.vo.ResponseBean;
import cc.messcat.vo.UserCoupnVo;
import cc.messcat.vo.VipInfoVo;
import cc.messcat.vo.WithdrawVo;
import cc.modules.commons.PageAction;
import cc.modules.constants.Constants;
import cc.modules.security.ExceptionManager;
import cc.modules.uploadfile.FilesUploadModel;

import static com.alipay.api.AlipayConstants.CHARSET;

/**
 * @author Nelson
 */
@SuppressWarnings("serial")
public class MemberAction extends PageAction implements ServletRequestAware, ServletResponseAware {
    private Object object;
    private String accessToken;
    private Long memberId;
    private String fileContent;// base64图片
    private String fileName;
    private MultipartFile file;
    private String projectName = PropertiesFileReader.getByKey("web.name");// 项目名
    private String imageFolderName = PropertiesFileReader.getByKey("upload.image.path");// 图片存放路径
    private String fileFolderName = PropertiesFileReader.getByKey("upload.file.path");// 文件存放路径
    private String domain = PropertiesFileReader.getByKey("web.domain");// 域名
    private String wtpDeploy = PropertiesFileReader.getByKey("wtpDeploy");// 项目上一级物理路径
    private String alipayPublicKey = PropertiesFileReader.getByKey("alipay.key.public");

    private static Logger log = LoggerFactory.getLogger(MemberAction.class);

    private String role;
    private Long collectId;
    private String type; // 类型(0=综合；1:准入报告 2：标准解读 3：质量分享 4：付费咨询 5：新闻 6：电商信息)
    private String title;
    private Date collectTime;
    private String photoName;// 头像图片名（需要后缀）
    private String photo;// Base编码后的头像图片
    private String author;
    private Double money;
    private String answerType;
    private String answer;
    private String voice;
    private int viewSum;
    private int likeSum;
    private String intro;
    private Date addTime;
    private List<Collect> collectsLict;
    private Long regionId;
    private Long productId;
    private String job;
    private String mobile;
    private String business;
    private String mobileCode;// 验证码
    private String terminal;// 设备类型：ios/android
    private String version;
    private String realname;
    private String field; // 专业领域
    private String email;
    private String school; // 毕业学校
    private String major; // 所学专业
    private String address;
    private String workYears;
    private String profession; // 职称
    private String company;
    private String position; // 职务
    private String workCardName;// 工卡图片名（需要后缀）
    private String workCard;// Base编码后的工卡图片
    private String visitCardName;// 名片图片名（需要后缀）
    private String visitCard;// Base编码后的名片图片
    private String idcardFrontName;// 身份证正面图片名（需要后缀）
    private String idcardFront;// Base编码后的身份证正面图片
    private String idcardBackName;// 身份证背面图片名（需要后缀）
    private String idcardBack;// Base编码后的身份证背面图片
    private String bankCard; //银行卡号
    private String cardholder;//银行卡持卡人
    private String openBank;//银行卡开户行
    private String bankMobile;//银行预留手机号
    private BigDecimal withdrawMoney; // 专家可提现金额
    private BigDecimal virtualCoin;// 积分
    private Collect collect;
    private Member member;
    private BigDecimal topUpMoney;
    private Long packageId;// 套餐id
    private HttpServletRequest request;
    private HttpServletResponse response;
    private IOSVerifyVo iosVerifyVo;
    private Long frequency;
    private String afterCompoundImg;
    private BigDecimal changeBalance;
    private String used;// 卡券状态：1-使用、失效 0-未使用
    private String redeemCode; // 兑换码
    private int videoId;
    private String openId;// 微信openId
    private int fromId;

    /**
     * 进入个人中心
     */
    public String excute() {
        Member member = memberManagerDao.retrieveMember(memberId);
        Member checkedMember = memberManagerDao.checkVipInfo(member);
        MemberVo memberVo = MemberUtil.setMemberVoInfo(checkedMember);
        // 获取用户的银行卡信息
        if (checkedMember.getBankId() != null && checkedMember.getBankId() != 0) {
            BankMember bankMember = bankManagerDao.findBankMember(checkedMember.getBankId());
            if (bankMember != null) {
                memberVo.setOpenBank(bankMember.getOpenBank());
                memberVo.setBankCard(bankMember.getBankCard());
                memberVo.setCardholder(bankMember.getCardHolder());
                memberVo.setBankMobile(bankMember.getBankMobile());
            }
        }
        memberVo.setEndTime(memberManagerDao.getVipEndTime(checkedMember,2));
        object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, memberVo);
        return "success";
    }


    /**
     * 注册环信账号
     *
     * @return
     */
    public String registEas() {
        List<Member> members = memberManagerDao.retrieveAllMembers();
        for (Member member : members) {
            if (member.getEasemobUserName() == null || member.getEasemobUserName().equals("")) {
                String userName = "weikai" + member.getId();
                ArrayNode dataArrayNode = JsonNodeFactory.instance.arrayNode();
                ObjectNode datanode = JsonNodeFactory.instance.objectNode();
                datanode.put("username", userName);
                datanode.put("password", SecurityTool.md5Simple(com.em.comm.Constants.DEFAULT_PASSWORD));
                dataArrayNode.add(datanode); // 添加环信平台账号 ObjectNode
                ObjectNode createNewIMUserSingleNode = EasemobIMUsers.createNewIMUserSingle(datanode);
                if (createNewIMUserSingleNode.get("statusCode") == null
                        || !"200".equals(createNewIMUserSingleNode.get("statusCode").toString()))
                    throw new ExceptionManager("注册环信用户失败!"); // 用户添加平台为好友

				/*ObjectNode addFriend = EasemobIMUsers.addFriendSingle(member.getMobile(), "admin");
				if (addFriend.get("statusCode") == null || !"200".equals(addFriend.get("statusCode").toString()))
					throw new ExceptionManager("添加好友失败!");*/
            }
        }
        return "success";
    }

    /**
     * 修改会员信息
     */
    public String updateMemberInfo() {
        MemberVo memberVo = new MemberVo();
        try {
            Member member = memberManagerDao.retrieveMember(memberId);
            if (member != null && member.getAccessToken().equals(accessToken)) {
                if (StringUtil.isNotBlank(photo) && StringUtil.isNotBlank(photoName)) {
                    FilesUploadModel fileModel = null;
                    fileModel = new FilesUploadModel(photoName, photo, projectName, imageFolderName, wtpDeploy);
                    fileModel.writeFileByBase64();
                    File file = new File(ServletActionContext.getServletContext().getRealPath("/").replace("weikai_server", "ROOT") + "upload/enterprice/" + fileModel.getFileUri());
                    member.setPhoto(savePhoto(fileModel.getFileUri(), file));
                    file.delete();
                }
                member.setRealname(realname);
                member.setJob(job);
                member.setCompany(company);
                member.setAddress(address);
                // 下面是专家信息
                if (role.equals("2")) {
                    member.setField(field);
                    member.setEmail(email);
                    member.setSchool(school);
                    member.setMajor(major);
                    member.setWorkYears(workYears);
                    member.setIntro(intro);
                    member.setProfession(profession); // 职称
                    member.setPosition(position); // 职务
                    if (member.getBankId() != null && member.getBankId() != 0) {
                        BankMember bankMember = bankManagerDao.findBankMember(member.getBankId());
                        if (bankMember != null) {
                            bankMember.setOpenBank(openBank);
                            bankMember.setBankCard(bankCard);
                            bankMember.setCardHolder(cardholder);
                            bankMember.setBankMobile(bankMobile);
                            bankManagerDao.updateBankMember(bankMember);
                        }
                    } else {
                        BankMember bm = new BankMember();
                        bm.setOpenBank(openBank);
                        bm.setBankCard(bankCard);
                        bm.setCardHolder(cardholder);
                        bm.setBankMobile(bankMobile);
                        BankMember bankMember = bankManagerDao.addBankMember(bm);
                        member.setBankId(bankMember.getId());
                    }

                    if (StringUtil.isNotBlank(workCard) && StringUtil.isNotBlank(workCardName)) {
                        FilesUploadModel fileModel = null;
                        fileModel = new FilesUploadModel(workCardName, workCard, projectName, imageFolderName,
                                wtpDeploy);
                        fileModel.writeFileByBase64();
                        File file = new File(ServletActionContext.getServletContext().getRealPath("/").replace("weikai_server", "ROOT") + "upload/enterprice/" + fileModel.getFileUri());
                        member.setWorkCard(savePhoto(fileModel.getFileUri(), file));
                        file.delete();
                    }
                    if (StringUtil.isNotBlank(visitCard) && StringUtil.isNotBlank(visitCardName)) {
                        FilesUploadModel fileModel = null;
                        fileModel = new FilesUploadModel(visitCardName, visitCard, projectName, imageFolderName,
                                wtpDeploy);
                        fileModel.writeFileByBase64();
                        File file = new File(ServletActionContext.getServletContext().getRealPath("/").replace("weikai_server", "ROOT") + "upload/enterprice/" + fileModel.getFileUri());
                        member.setVisitCard(savePhoto(fileModel.getFileUri(), file));
                        file.delete();
                    }
                    if (StringUtil.isNotBlank(idcardFront) && StringUtil.isNotBlank(idcardFrontName)) {
                        FilesUploadModel fileModel = null;
                        fileModel = new FilesUploadModel(idcardFrontName, idcardFront, projectName, imageFolderName,
                                wtpDeploy);
                        fileModel.writeFileByBase64();
                        File file = new File(ServletActionContext.getServletContext().getRealPath("/").replace("weikai_server", "ROOT") + "upload/enterprice/" + fileModel.getFileUri());
                        member.setIdcardFront(savePhoto(fileModel.getFileUri(), file));
                        file.delete();
                    }
                    if (StringUtil.isNotBlank(idcardBack) && StringUtil.isNotBlank(idcardBackName)) {
                        FilesUploadModel fileModel = null;
                        fileModel = new FilesUploadModel(idcardBackName, idcardBack, projectName, imageFolderName,
                                wtpDeploy);
                        fileModel.writeFileByBase64();
                        File file = new File(ServletActionContext.getServletContext().getRealPath("/").replace("weikai_server", "ROOT") + "upload/enterprice/" + fileModel.getFileUri());
                        member.setIdcardBack(savePhoto(fileModel.getFileUri(), file));
                        file.delete();
                    }

                    if (!realname.equals(member.getRealname()) || !profession.equals(member.getProfession())
                            || !company.equals(member.getCompany()) || !position.equals(member.getPosition())) {
                        member.setExpertCheckStatus("0");
                    }
                }
                memberVo = memberManagerDao.updateMember(member);
                if (member.getRole().equals("2")) {
                    BankMember bankM = bankManagerDao.findBankMember(member.getBankId());
                    if (bankM != null) {
                        memberVo.setBankCard(bankM.getBankCard());
                        memberVo.setCardholder(bankM.getCardHolder());
                        memberVo.setOpenBank(bankM.getOpenBank());
                        memberVo.setBankMobile(bankM.getBankMobile());
                    }
                }
                object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, memberVo);
            } else {
                object = new ResponseBean(Constants.FAIL_CODE_400, "登录token错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
        }
        return "success";
    }

    /**
     * 进入办理VIP会员页面
     */
    public String toVip() {
        List<PackageVo> packages = new ArrayList<>();
        // 查询后台的会员套餐
        try {
            Member member = memberManagerDao.retrieveMember(memberId);
            if (member != null && member.getAccessToken().equals(accessToken)) {
                List<Packages> packagess = packagesManagerDao.retrieveAllPackagess();
                if (ObjValid.isValid(packagess)) {
                    for (Packages p : packagess) {
                        PackageVo vo = new PackageVo();
                        vo.setPackageId(p.getId());
                        String type = "年";
                        if (p.getType().equals("1")) {
                            type = "个月";
                        } else if (p.getType().equals("2")) {
                            type = "天";
                        }
                        vo.setName(p.getNumber() + type);
                        vo.setMoney(p.getMoney());
                        packages.add(vo);
                    }
                }
                object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, packages);
            } else {
                object = new ResponseBean(Constants.FAIL_CODE_400, "登录token错误");
            }
        } catch (Exception e) {
            object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
        }
        return "success";
    }

    /**
     * 进入会员权益页面
     */
    public String toMemberPower() {
        try {
            Member member = memberManagerDao.retrieveMember(memberId);
            if (member != null && member.getAccessToken().equals(accessToken)) {
                String memberPowerUrl = "http://www.cert-map.com/epFrontNewsAction!toMemberPower.action";
                object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, memberPowerUrl);
            } else {
                object = new ResponseBean(Constants.FAIL_CODE_400, "登录token错误");
            }
        } catch (Exception e) {
            object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
        }
        return "success";

    }

    /**
     * 苹果内购（VIP充值续费）
     *
     * @return
     * @throws Exception
     */
    public String appleVipPay() throws Exception {

        try {
            // 从拦截器获取
            object = memberManagerDao.addAppleVip(member, packageId);
        } catch (Exception e) {
            e.printStackTrace();
            object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
        }
        return "success";
    }

    /**
     * 苹果内购（VIP充值续费）回调
     *
     * @return
     * @throws IOException
     */
    public Object iOSVerify() throws IOException {
        try {
            object = memberManagerDao.updateBuysRecordFromApple(member, iosVerifyVo, request, response);
        } catch (Exception e) {
            object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_FAIL);
        }
        return "success";
    }

    /**
     * 微信VIP充值续费
     *
     * @return
     * @throws Exception
     */
    public String payWechatApp() throws Exception {
        if (null == packageId) {
            object = new ResponseBean(Constants.FAIL_CODE_400, Constants.MSG_PARAMETER_EMPTY);
            return "success";
        }
        try {
            // 从拦截器获取
            object = memberManagerDao.addWeChatVip(member, packageId, request);
        } catch (Exception e) {
            e.printStackTrace();
            object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
        }
        return "success";
    }

    /**
     * 微信充值VIP回调
     *
     * @return
     * @throws ExceptionManager
     * @throws IOException
     */
    public String getNotifyResult() throws ExceptionManager, IOException {
        try {
            System.out.println("微信回调正确1》》》》》》》》》》》》》》");
            memberManagerDao.addNotifyResult(request, response);
        } catch (Exception e) {
            object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
        }
        return NONE;
    }

    /**
     * 支付宝VIP充值续费
     *
     * @return
     */
    public String vip() {
        if (null == packageId) {
            object = new ResponseBean(Constants.FAIL_CODE_400, Constants.MSG_PARAMETER_EMPTY);
            return "success";
        }
        try {
            // 从拦截器获取
            object = memberManagerDao.addVip(member, packageId);
        } catch (Exception e) {
            e.printStackTrace();
            object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
        }
        return "success";
    }

    /**
     * 支付宝充值VIP回调
     *
     * @return
     * @throws ExceptionManager
     * @throws IOException
     */
    public String returnAppNotify() throws ExceptionManager, IOException {
        //获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            log.error("name: " + name + " ||||value: " + valueStr);
            params.put(name, valueStr);
        }
        try {
            //切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
            boolean flag = AlipaySignature.rsaCheckV1(params, alipayPublicKey, AlipayConstants.CHARSET, "RSA2");
            String tradeStatus = params.get("trade_status"); // 支付状态
            log.error("alipay.flag>>>>>>>>>>" + flag + "    tradeStatus:" + tradeStatus);
            // 签名校验
            if (flag) {
                if (tradeStatus.equals("TRADE_SUCCESS")) {
                    memberManagerDao.addHandlerOrder(params, "Y");
                    response.getOutputStream().write("success".getBytes());
                }
            } else {
                memberManagerDao.addHandlerOrder(params, "N");
                response.getOutputStream().write("failure".getBytes());
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return NONE;
    }

    /**
     * 苹果内购（钱包充值）
     */
    public String appleWalletPay() {
        if (topUpMoney == null || topUpMoney.compareTo(new BigDecimal(0)) == -1) {
            object = new ResponseBean(Constants.FAIL_CODE_400, "金额有误");
            return "getPayAlipayPrams";
        }
        try {
            object = memberManagerDao.addAppleWallet(member, topUpMoney);
        } catch (Exception e) {
            e.printStackTrace();
            object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
        }

        return "success";
    }

    /**
     * 苹果内购（钱包充值）回调
     *
     * @return
     */
    public Object walletIOSVerify() throws IOException {
        try {
            object = memberManagerDao.updateBuysRecordFromApple(member, iosVerifyVo, request, response);

        } catch (Exception e) {
            e.printStackTrace();
            object = new ResponseBean(Constants.FAIL_CODE_400, Constants.MSG_FAIL);
        }

        return "success";
    }

    /**
     * 通过支付宝充值钱包
     *
     * @return
     */
    public String topUpByAlipay() {
        frequency = System.currentTimeMillis();
        Long current = System.currentTimeMillis();
        if (topUpMoney == null || topUpMoney.compareTo(new BigDecimal(0)) == -1) {
            object = new ResponseBean(Constants.FAIL_CODE_400, "金额有误");
            return "getPayAlipayPrams";
        }
        try {
            object = memberManagerDao.alipayTopUp(member, topUpMoney);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
        }
        return "success";
    }

    /**
     * 支付宝充值钱包回调
     *
     * @return
     */
    public String walletNotify() {
        log.error("支付宝钱包充值回调成功：生成订单===========================");
        //获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            log.error("name: " + name + " ||||value: " + valueStr);
            params.put(name, valueStr);
        }

        //切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
        //boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String publicKey, String charset, String sign_type)
        try {
            boolean flag = AlipaySignature.rsaCheckV1(params, alipayPublicKey, AlipayConstants.CHARSET, "RSA2");
            if (flag) {
                // 要写的逻辑。自己按自己的要求写
                System.out.println("支付宝回调正确》》》》》》》》》》》》》》》");
                memberManagerDao.topUpHanlder(params, "Y");
                response.getOutputStream().write("success".getBytes());
            } else {
                System.out.println("支付宝回调错误》》》》》》》》》》》》》》》》》》》》》》》》");
                // 要写的逻辑。自己按自己的要求写
                memberManagerDao.topUpHanlder(params, "N");
                response.getOutputStream().write("failure".getBytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return NONE;
    }

    /**
     * 微信充值钱包
     *
     * @return
     */
    public String topUpByWechat() {
        if (topUpMoney == null || topUpMoney.compareTo(new BigDecimal(0)) == -1) {
            object = new ResponseBean(Constants.FAIL_CODE_400, "金额有误");
            return "getPayAlipayPrams";
        }
        try {
            object = memberManagerDao.wechatTopUp(member, topUpMoney, request);
        } catch (Exception e) {
            e.printStackTrace();
            object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
        }
        return "success";
    }

    /**
     * 微信充值钱包回调
     *
     * @return
     */
    public String walletNotifyResult() {
        try {
            System.out.println("微信充值钱包回调正确************************************");
            memberManagerDao.addWalletNotifyResult(request, response);
        } catch (Exception e) {
            object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
        }
        return NONE;
    }


    /**
     * 关于我们
     */
    public String getAboutUs() {
        AboutusVo aboutusVo = new AboutusVo();
        try {
            Member member = memberManagerDao.retrieveMember(memberId);
            if (member != null && member.getAccessToken().equals(accessToken)) {
                // 获取APP简介
                List<EnterpriseNewsVo> news = epNewsManagerDao.listEnterpriseNews(1, "app_advan", member);
                if (ObjValid.isValid(news)) {
                    aboutusVo.setAppIntro(news.get(0).getContents());
                }
                // 获取服务优势
                List<EnterpriseNewsVo> news2 = epNewsManagerDao.listEnterpriseNews(1, "server_advan", member);
                if (ObjValid.isValid(news2)) {
                    aboutusVo.setSerivce(news2.get(0).getContents());
                }
                object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, aboutusVo);
            } else {
                object = new ResponseBean(Constants.FAIL_CODE_400, "登录token错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
        }
        return "success";
    }

    /**
     * 收藏功能
     *
     * @return
     */
    public String collect() throws Exception {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("textml;charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Max-Age", "0");
        response.setHeader("Access-Control-Allow-Headers",
                "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("XDomainRequestAllowed", "1");
        if (null == collect) {
            object = new ResponseBean(Constants.FAIL_CODE_400, Constants.MSG_PARAMETER_EMPTY);
            return "success";
        }
        if (null == collect.getType()) {
            object = new ResponseBean(Constants.FAIL_CODE_400, Constants.MSG_PARAMETER_EMPTY);
            return "success";
        }
        if (null == collect.getRelatedId()) {
            object = new ResponseBean(Constants.FAIL_CODE_400, Constants.MSG_PARAMETER_EMPTY);
            return "success";
        }
        try {
            object = collectManagerDao.addCollection(collect, member);
        } catch (Exception e) {
            e.printStackTrace();
            object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
        }
        return "success";
    }

    /**
     * 收藏列表
     */
    public String getMyCollectList() {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("textml;charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Max-Age", "0");
        response.setHeader("Access-Control-Allow-Headers",
                "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("XDomainRequestAllowed", "1");
        if (null == type) {
            object = new ResponseBean(Constants.FAIL_CODE_400, Constants.MSG_PARAMETER_EMPTY);
            return "success";
        }
        CollectListVo collectList = new CollectListVo();
        try {
            collectList = collectManagerDao.getStandardByCon(memberId, pageNo, pageSize, type);
            object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, collectList);
        } catch (Exception e) {
            e.printStackTrace();
            object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
        }
        return "success";
    }

    /**
     * 5期-新的收藏列表接口
     */
    public String getCollectList() {
        Member member = memberManagerDao.retrieveMember(Long.valueOf(memberId));
        if (member != null && member.getAccessToken().equals(accessToken)) {
            try {
                List<CollectVo> collectList = collectManagerDao.getStandardByCon(memberId, pageNo, pageSize);
                object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, collectList);
            } catch (Exception e) {
                e.printStackTrace();
                object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
            }
        } else {
            object = new ResponseBean(Constants.FAIL_CODE_400, "登录token错误");
        }

        return "success";
    }

    /**
     * 取消收藏
     */
    public String delCollect() {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("textml;charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Max-Age", "0");
        response.setHeader("Access-Control-Allow-Headers",
                "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("XDomainRequestAllowed", "1");
        try {
            Member member = memberManagerDao.retrieveMember(Long.valueOf(memberId));
            if (member != null && member.getAccessToken().equals(accessToken)) {
                // 取消收藏，物理删除
                Collect collect = collectManagerDao.retrieveCollect(collectId);
                if (ObjValid.isValid(collect)) {
                    Long collectmemberId = collect.getMemberId();
                    if (collectmemberId != null && memberId.equals(collectmemberId)) {
                        collectManagerDao.removeCollect(collectId);
                    }
                } else {
                    object = new ResponseBean(Constants.FAIL_CODE_400, Constants.MSG_FAIL, "没有收藏");
                }
                object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS);
            } else {
                object = new ResponseBean(Constants.FAIL_CODE_400, Constants.MSG_FAIL);
            }
        } catch (Exception e) {
            e.printStackTrace();
            object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
        }
        return "success";
    }

    /**
     * 已购列表之获取积分
     */
    public String getBuysRecoredByIntegral() {
        try {
            Member member = memberManagerDao.retrieveMember(Long.valueOf(memberId));
            if (member != null && member.getAccessToken().equals(accessToken)) {
                BigDecimal integral = member.getIntegral() == null ? new BigDecimal(0) : member.getIntegral();
                BigDecimal virtualCoin = member.getVirtualCoin() == null ? new BigDecimal(0) : member.getVirtualCoin();
                Wallet wallet = walletManagerDao.get(memberId);
                BigDecimal balance = wallet.getBalance();
                IntegralAndVirtualConVo integralAndVirtualConVo = new IntegralAndVirtualConVo(integral, virtualCoin,
                        balance);
                Integer distributorIntegral = liveService.getDistributorIntegralByMemberId(member.getId());
                if (distributorIntegral != null) {
                    integralAndVirtualConVo.setInteger(integralAndVirtualConVo.getInteger().add(new BigDecimal(distributorIntegral)));
                }
                object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, integralAndVirtualConVo);
            } else {
                object = new ResponseBean(Constants.FAIL_CODE_400, Constants.MSG_FAIL);
            }
        } catch (Exception e) {
            e.printStackTrace();
            object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
        }
        return "success";
    }

    /**
     * 已购列表(旧)
     */
    public String getBuysRecordList() {
        try {
            Member member = memberManagerDao.retrieveMember(Long.valueOf(memberId));
            if (member != null && member.getAccessToken().equals(accessToken)) {
                // 根据条件查询BuysRecord Long memberId, String type, int pageNo, int
                MyBuysListResult buysListResult = expenseTotalManagerDao.findBuysRecordsByCondiction(memberId, type,
                        pageNo, pageSize);
                object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, buysListResult);
            } else {
                object = new ResponseBean(Constants.FAIL_CODE_400, Constants.MSG_FAIL);
            }
        } catch (Exception e) {
            e.printStackTrace();
            object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
        }
        return "success";
    }

    /**
     * 新已购列表接口(把质量分享与标准解读合并，为了不影响旧版app功能使用)
     */
    public String getBuysRecordNewList() {
        try {
            Member member = memberManagerDao.retrieveMember(Long.valueOf(memberId));
            if (member != null && member.getAccessToken().equals(accessToken)) {
                // 根据条件查询BuysRecord Long memberId, String type, int pageNo, int
                List<ExpenseTotalVo> buysListResult = expenseTotalManagerDao.findExpenseTotalByCondiction(memberId,
                        type, pageNo, pageSize);
                object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, buysListResult);
            } else {
                object = new ResponseBean(Constants.FAIL_CODE_400, Constants.MSG_FAIL);
            }
        } catch (Exception e) {
            e.printStackTrace();
            object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
        }
        return "success";
    }

    /**
     * 进入专家收益提现页面之获取剩余金额
     */
    public String toExpertIEByMoney() {
        try {
            Member member = memberManagerDao.retrieveMember(Long.valueOf(memberId));
            if (member != null && member.getAccessToken().equals(accessToken)) {
                if (ObjValid.isValid(member.getRole()) && member.getRole().equals("1")) {
                    object = new ResponseBean(Constants.FAIL_CODE_400, "角色错误");
                    return "success";
                }
                BigDecimal expertEarn = member.getCanWithdraw() == null ? new BigDecimal(0) : member.getCanWithdraw();
                object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, expertEarn);
            } else {
                object = new ResponseBean(Constants.FAIL_CODE_400, Constants.MSG_FAIL);
            }
        } catch (Exception e) {
            e.printStackTrace();
            object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
        }
        return "success";
    }

    /**
     * 进入专家收益提现页面之获取流水列表
     */
    public String toExpertIEByList() {
        ExpertIEListVo expertIEListVo = new ExpertIEListVo();
        try {
            Member member = memberManagerDao.retrieveMember(Long.valueOf(memberId));
            if (member != null && member.getAccessToken().equals(accessToken)) {
                if (ObjValid.isValid(member.getRole()) && member.getRole().equals("1")) {
                    object = new ResponseBean(Constants.FAIL_CODE_400, "角色错误");
                    return "success";
                }
                expertIEListVo = expertIEManagerDao.getExpertIEByCon(memberId, pageNo, pageSize);
                object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, expertIEListVo);
            } else {
                object = new ResponseBean(Constants.FAIL_CODE_400, Constants.MSG_FAIL);
            }
        } catch (Exception e) {
            e.printStackTrace();
            object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
        }
        return "success";
    }

    /**
     * 进入专家提现页面
     */
    public String toWithdraw() {
        try {
            Member member = memberManagerDao.retrieveMember(Long.valueOf(memberId));
            if (member != null && member.getAccessToken().equals(accessToken)) {
                if (ObjValid.isValid(member.getRole()) && member.getRole().equals("1")) {
                    object = new ResponseBean(Constants.FAIL_CODE_400, "角色错误");
                    return "success";
                }
                if (member.getBankId() == null || member.getBankId() == 0) {
                    object = new ResponseBean(Constants.FAIL_CODE_400, "银行信息不全");
                    return "success";
                }
                if (member.getCanWithdraw() == null) {
                    object = new ResponseBean(Constants.FAIL_CODE_400, "可提现金额不足");
                    return "success";
                }
                if (member.getCanWithdraw().compareTo(new BigDecimal(-0.000001)) == 1 && member.getCanWithdraw().compareTo(new BigDecimal(0.000001)) == -1) {
                    object = new ResponseBean(Constants.FAIL_CODE_400, "可提现金额不足");
                    return "success";
                }
                WithdrawVo withdrawVo = new WithdrawVo();
                if (member.getBankId() != null && member.getBankId() != 0) {
                    BankMember bank = bankManagerDao.findBankMember(member.getBankId());
                    withdrawVo.setOpenBank(bank.getOpenBank());
                    withdrawVo.setBankCard(bank.getBankCard());
                }
                withdrawVo.setWithdrawMoney(member.getCanWithdraw() == null ? new BigDecimal(0) : member.getCanWithdraw());
                object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, withdrawVo);
            } else {
                object = new ResponseBean(Constants.FAIL_CODE_400, Constants.MSG_FAIL);
            }
        } catch (Exception e) {
            e.printStackTrace();
            object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
        }
        return "success";
    }

    /**
     * 专家提现
     */
    public String withdrawing() {
        try {
            Member member = memberManagerDao.retrieveMember(Long.valueOf(memberId));
            if (member != null && member.getAccessToken().equals(accessToken)) {
                if (ObjValid.isValid(member.getRole()) && member.getRole().equals("1")) {
                    object = new ResponseBean(Constants.FAIL_CODE_400, "角色错误");
                    return "success";
                }
                if (member.getBankId() == null || member.getBankId() == 0) {
                    object = new ResponseBean(Constants.FAIL_CODE_400, "银行信息不全");
                    return "success";
                }
                if (withdrawMoney.compareTo(new BigDecimal(1)) == -1) {
                    object = new ResponseBean(Constants.FAIL_CODE_400, "提现金额必须大于1元");
                    return "success";
                }
                if (withdrawMoney.compareTo(member.getCanWithdraw()) == 1) {
                    object = new ResponseBean(Constants.FAIL_CODE_400, "提现金额大于可提现金额");
                    return "success";
                }
                // 新增专家流水
                ExpenseExpert expenseExpert = new ExpenseExpert();
                expenseExpert.setExpertId(memberId);
                expenseExpert.setExpenseTotalId(0L);
                expenseExpert.setOrderNum(OrderSnUtil.generateMemberRechargeSn());
                expenseExpert.setOperate("1");
                expenseExpert.setOriginalPrice(new BigDecimal(0));
                expenseExpert.setMoney(withdrawMoney);
                expenseExpert.setAddTime(new Date());
                expenseExpert.setExpenseExpertMoney(withdrawMoney);
                expenseExpert.setContent("提现" + withdrawMoney + "元");
                expenseExpert.setRemark("");
                expertIEManagerDao.addExpenseExpert(expenseExpert);
                WithdrawRecord withdraw = new WithdrawRecord();
                Date today = new Date();
                withdraw.setAddTime(today);
                withdraw.setEditTime(today);
                withdraw.setCheckStatus("0");
                withdraw.setRemitStatus("0");
                withdraw.setStatus("1");
                withdraw.setContent(member.getRealname() + "提现了" + withdrawMoney + "元");
                withdraw.setMember(member);
                withdraw.setMoney(withdrawMoney);
                BankMember bankMember = bankManagerDao.findBankMember(member.getBankId());
                withdraw.setBankMember(bankMember);
                withdraw.setRemark("");
                member.setCanWithdraw(member.getCanWithdraw().subtract(withdrawMoney));
                withdrawRecordManagerDao.addWithdrawRecord(withdraw);
                memberManagerDao.modifyMember(member);
                String description = "专家" + member.getRealname() + "在" + DateHelper.dataToString(today, "yyyy年MM月dd日HH时mm分") + "提现金额" + withdrawMoney + "元";
                String operator1 = "15827625767";
                String operator2 = "13073008457";
                List<String> operators = new ArrayList<>();
                operators.add(operator1);
                operators.add(operator2);
                for (String mobile : operators) {
                    MiPushHelper.sendAndroidUserAccount("有专家提现啦!", description, mobile);
                    MiPushHelper.sendIOSUserAccount(description, mobile);
                }
                SmsUtil.sendMessage(member.getMobile(), "尊敬的" + member.getRealname() + "专家：您好！您的收益提现申请现在进入了系统审核期，提现金额" + withdrawMoney + "元，审核结果7个工作日会通知您。");
                SmsUtil.sendMessage(operator1, "专家" + member.getRealname() + "在" + DateHelper.dataToString(today, "yyyy年MM月dd日HH时mm分") + "提现" + withdrawMoney + "元的申请已进入系统审核，请联系客服进行核对。");
                SmsUtil.sendMessage(operator2, "专家" + member.getRealname() + "在" + DateHelper.dataToString(today, "yyyy年MM月dd日HH时mm分") + "提现" + withdrawMoney + "元的申请已进入系统审核，请联系客服进行核对。");
                object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS);
            } else {
                object = new ResponseBean(Constants.FAIL_CODE_400, Constants.MSG_FAIL);
            }
        } catch (Exception e) {
            e.printStackTrace();
            object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
        }
        return "success";

    }

    /**
     * 保存图片
     *
     * @param name
     * @param file
     * @return
     */
    public String savePhoto(String name, File file) {
        String inputFileExt = name.substring(name.lastIndexOf(".") + 1);
        String fileName = MD5.GetMD5Code(new DateHelper().getRandomNum()).substring(8, 24) + "." + inputFileExt;
        InputStream is = null;
        try {
            is = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String key = "images/" + inputFileExt + "/" + fileName;
        AliyunOOSUtil.upload(key, is);
        return key;
    }

    /**
     * 文件上传
     */
    public String fileUpload() throws Exception {
        FileResult commonResult = new FileResult();
        try {
            FilesUploadModel fileModel = null;
            if (ObjValid.isValid(file)) {
                fileModel = new FilesUploadModel(file.getOriginalFilename(), file.getInputStream(), projectName,
                        fileFolderName);
                fileModel.writeFileByBinary();
                String docId = BDocHelper.upLoadToBCE(new File(fileModel.getFileRealPath()));
                commonResult.setDocumentId(docId);
                commonResult.setFileUrl(fileModel.getFileUri());
                object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, commonResult);
                return "success";
            } else {
                object = new ResponseBean(Constants.FAIL_CODE_400, "参数为空");
            }
        } catch (Exception e) {
            e.printStackTrace();
            object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
        }
        return "success";

    }

    /**
     * 生成二维码
     */
    public String createQRCode() {

        String mobile = request.getAttribute("mobile").toString();
        Member member = memberManagerDao.retrieveMember(mobile);
        if (member != null) {
            ActionContext ac = ActionContext.getContext();
            ServletContext sc = (ServletContext) ac.get(ServletActionContext.SERVLET_CONTEXT);
            String realPath = sc.getRealPath("/");
            Long id = member.getId();
            String urlAndId = domain + "/regist/invitee.html?recommender_id=" + id;// 注册地址+用户的memberId
            String outPutPath = realPath + "qrImg\\";// 二维码的输出路径
            String compoundPath = realPath + "qrImg\\alert-img.jpg";// 用于与二维码合成的图片存放的路径
            String afterCompoundPath = realPath + "qrImg\\";// 两图片合成后存放的路径
            afterCompoundImg = QRCodeHelper.composeQRCode(urlAndId, outPutPath, compoundPath, afterCompoundPath);
        } else {
            afterCompoundImg = "0";
        }
        return "createQRCode";
    }


    /**
     * 积分兑换余额
     *
     * @return
     */
    public String integralToBalance() {
        // 先获取到用户的积分
        Member member = memberManagerDao.retrieveMember(memberId);
        //获取分销的可用积分
        BigDecimal distributorIntegral = memberManagerDao.getDistributorIntegral(memberId);
        //用户的总积分
        BigDecimal integral = member.getIntegral().add(distributorIntegral);
        BigDecimal canChangeBalance = integral.divide(new BigDecimal(10));
        if (changeBalance.compareTo(canChangeBalance) == 1) {// 判断用户的积分是否足够兑换所输入的金额
            object = new ResponseBean(Constants.FAIL_CODE_400, "您的积分不足");
        } else {
            // 增加余额
            Wallet wallet = walletManagerDao.get(memberId);
            BigDecimal oldBalance = wallet.getBalance(); //旧的余额
            wallet.setBalance((wallet.getBalance().compareTo(new BigDecimal(0)) == -1 || wallet.getBalance().compareTo(new BigDecimal(0)) == 0) ? changeBalance : wallet.getBalance().add(changeBalance));
            walletManagerDao.update(wallet);
            /**
             * 添加消费积分的记录
             */
            BigDecimal oldIntegral = member.getIntegral();
            BigDecimal exchangeIntegral = changeBalance.multiply(new BigDecimal(10));
            BigDecimal newIntegral = oldIntegral.subtract(exchangeIntegral);
            BigDecimal exchangeBalance = changeBalance;
            BigDecimal newBalance = oldBalance.add(exchangeBalance);
            IntegralExchange integralExchange = new IntegralExchange(memberId, oldIntegral, exchangeIntegral, newIntegral, oldBalance, exchangeBalance, newBalance);
            integralManagerDao.addIntegralExchange(integralExchange);
            /**
             * 先判断用户member表中的积分是否“大于或等于”要兑换的积分
             * ①若大于或等于: 直接从member表中扣除相应的积分
             * ②若小于:则先把member表中的扣完，在逐一扣除live_video_distributor中的积分
             */
            if (member.getIntegral().compareTo(exchangeIntegral) == 1 || member.getIntegral().compareTo(exchangeIntegral) == 0) {
                member.setIntegral(member.getIntegral().subtract(exchangeIntegral));
            } else {
                member.setIntegral(new BigDecimal(0));
                BigDecimal spendIntegral = exchangeIntegral.subtract(member.getIntegral());
                memberManagerDao.subtractDistributorIntegral(memberId,spendIntegral);
            }
            memberManagerDao.updateMember(member);
            object = new ResponseBean(Constants.SUCCESS_CODE_200, "兑换成功");
        }
        return "success";
    }

    /**
     * 分享活动，老用户通过点击链接进入活动页，输入手机号码参与活动
     *
     * @return
     */
    public String getMemberIdByMobile() {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("textml;charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Max-Age", "0");
        response.setHeader("Access-Control-Allow-Headers",
                "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("XDomainRequestAllowed", "1");
        Member member = memberManagerDao.findMemberByMobile(mobile);
        if (member != null) {
            Long memberId = member.getId();
            object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, memberId);
        }
        return "success";
    }

    /**
     * 钱包统一支付时使用，根据商品的类型查找该范围内可用的卡券
     *
     * @return
     */
    public String getCoupn() {
        if (memberId != null && member.getAccessToken().equals(accessToken)) {
            if (type != null) {
                // 根据memberId和商品范围（商品的类型：质量分享、标准解读等），查找是否有该范围可用的卡券
                // type 0：购买会员套餐 1：购买报告 2：购买标准解读 3：购买质量分享 4：付费咨询 6：电商信息 8：直播视频

                List<CoupnUser> coupns = memberManagerDao.getUserCoupnByMemberIdAndType(type, memberId, pageNo,
                        pageSize, videoId);

                if (coupns != null && coupns.size() > 0) {
                    List<UserCoupnVo> coupnVos = new ArrayList<>();
                    for (CoupnUser coupnUser : coupns) {
                        UserCoupnVo userCoupnVo = new UserCoupnVo();
                        userCoupnVo.setId(coupnUser.getId());
                        userCoupnVo.setMemberId(coupnUser.getMemberId());
                        userCoupnVo.setCoupnId(coupnUser.getCoupnId());
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
                        String beginDate = sdf.format(coupnUser.getCoupnId().getBeginDate());
                        String endDate = sdf.format(coupnUser.getCoupnId().getEndDate());
                        StringBuffer validityDate = new StringBuffer();
                        validityDate.append(beginDate);
                        validityDate.append(" - ");
                        validityDate.append(endDate);
                        userCoupnVo.setValidityDate(validityDate.toString());

                        if (coupnUser.getUsed().equals("0")) { // 还没使用过的卡券
                            if (coupnUser.getCoupnId().getUsable().equals("0")) {// 失效卡券：未使用过，但已经不可用
                                userCoupnVo.setUsedStatus("2");
                                coupnVos.add(userCoupnVo);
                            } else if (coupnUser.getCoupnId().getUsable().equals("1")) {// 正常卡券
                                userCoupnVo.setUsedStatus("0");
                                coupnVos.add(userCoupnVo);
                            }
                        } else if (coupnUser.getUsed().equals("1")) {// 已使用的卡券
                            userCoupnVo.setUsedStatus("1");
                            coupnVos.add(userCoupnVo);
                        }
                    }
                    object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, coupnVos);
                } else {
                    object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_NULL, new ArrayList<>());
                }
            } else {
                object = new ResponseBean(Constants.FAIL_CODE_400, Constants.MSG_PARAMETER_EMPTY);
            }
        }
        return "success";
    }

    /**
     * 返回该用户所有可用或不可用的卡券
     *
     * @return
     */
    public String getAllCoupn() {
        if (memberId != null && used != null && member.getAccessToken().equals(accessToken)) {
            List<CoupnUser> coupns = memberManagerDao.getAllUserCoupn(memberId, pageNo, pageSize);
            if (coupns != null) {
                List<UserCoupnVo> usableCoupnVos = new ArrayList<>();// 正常卡券：未使用，且未过期
                List<UserCoupnVo> unusableCoupnVos = new ArrayList<>();// 已使用或过期
                for (CoupnUser coupnUser : coupns) {
                    UserCoupnVo userCoupnVo = new UserCoupnVo();
                    userCoupnVo.setId(coupnUser.getId());
                    userCoupnVo.setMemberId(coupnUser.getMemberId());
                    userCoupnVo.setCoupnId(coupnUser.getCoupnId());

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
                    Date bDate = coupnUser.getCoupnId().getBeginDate();
                    Date eDate = coupnUser.getCoupnId().getEndDate();
                    String beginDate = sdf.format(bDate);
                    String endDate = sdf.format(eDate);
                    StringBuffer validityDate = new StringBuffer();
                    validityDate.append(beginDate);
                    validityDate.append(" - ");
                    validityDate.append(endDate);
                    userCoupnVo.setValidityDate(validityDate.toString());

                    // 卡券超过有效期，状态改为不可用
                    Coupn coupn = coupnUser.getCoupnId();
                    Date currentTime = new Date();
                    if (currentTime.after(eDate)) {
                        coupn.setUsable("0");
                    }

                    /** UsedStatus 0代表未使用 1代表已使用 2代表已过期 */
                    if (coupnUser.getUsed().equals("0")) { // 还没使用过的卡券
                        if (coupnUser.getCoupnId().getUsable().equals("0")) {// 失效卡券：未使用过，但已经不可用
                            userCoupnVo.setUsedStatus("2");
                            unusableCoupnVos.add(userCoupnVo);
                        } else if (coupnUser.getCoupnId().getUsable().equals("1")) {// 正常卡券
                            userCoupnVo.setUsedStatus("0");
                            usableCoupnVos.add(userCoupnVo);
                        }
                    } else if (coupnUser.getUsed().equals("1")) {// 已使用的卡券
                        userCoupnVo.setUsedStatus("1");
                        unusableCoupnVos.add(userCoupnVo);
                    }
                    memberManagerDao.updateCoupn(coupn);
                }
                if (used.equals("0")) {// 返回可用卡券
                    object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, usableCoupnVos);
                } else if (used.equals("1")) {// 不可用的卡券
                    object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, unusableCoupnVos);
                }
            } else {
                object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_NULL, new ArrayList<>());
            }
        } else {
            object = new ResponseBean(Constants.FAIL_CODE_400, Constants.MSG_PARAMETER_EMPTY);
        }
        return "success";
    }

    /**
     * 会员的收藏后和购买总数
     *
     * @return
     */
    public String countCollectAndBuysNum() {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("textml;charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Max-Age", "0");
        response.setHeader("Access-Control-Allow-Headers",
                "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("XDomainRequestAllowed", "1");
        if (member != null && member.getAccessToken().equals(accessToken)) {
            int countCollect = collectManagerDao.findAllCollectByMemberId(memberId);
            int countBuys = expenseTotalManagerDao.findAllBuysByMemberId(memberId);
            CollectAndBuysCountVo count = new CollectAndBuysCountVo();
            count.setBuysCount(countBuys);
            count.setCollectCount(countCollect);
            object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, count);
        } else {
            object = new ResponseBean(Constants.FAIL_CODE_400, "你的账号在别处登录，请重新登录");
        }
        return "success";
    }

    /**
     * 返回会员的类型和到期时间
     */
    public String getVipInfo() {
        Member member = memberManagerDao.retrieveMember(memberId);
        if (member != null && member.getAccessToken().equals(accessToken)) {
            VipInfoVo vipInfoVo = new VipInfoVo();
            String endDate = memberManagerDao.getVipEndTime(member,1);
            vipInfoVo.setEndDate(endDate);
            vipInfoVo.setType(Integer.parseInt(member.getType()));
            object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, vipInfoVo);
        } else {
            object = new ResponseBean(Constants.EXCEPTION_CODE_600, Constants.LOGIN_TIME_TOKEN_ERROR);
        }
        return "success";
    }

    @Deprecated
    public String changeVirtualCoin() {
        // 先查出那个人的积分有多少
        BigDecimal integral = member.getIntegral();

        // 积分：虚拟币 = 10:1，判断他的积分够不够兑换他输入的虚拟币数量
        if (integral.compareTo(virtualCoin.multiply(new BigDecimal(10))) == 1 || integral.compareTo(virtualCoin.multiply(new BigDecimal(10))) == 0) {
            /*
             * BigDecimal integrals = BigDecimal.valueOf(integral); BigDecimal
             * ratio = BigDecimal.valueOf(10); double virtualCon =
             * integrals.divide(ratio,3).doubleValue(); Integer canChange =
             * integral/10; //member表的积分要相应减少 Integer mInteger =
             * member.getIntegral(); Double mViritualCon =
             * member.getVirtualCoin();
             */
            BigDecimal mInteger = member.getIntegral(); // 本来的积分
            BigDecimal mViritualCon = member.getVirtualCoin(); // 本来的虚拟币
            if (mViritualCon == null) {
                mViritualCon = new BigDecimal(0);
            }

            // member的虚拟币要增加，积分要减少
            BigDecimal afterAddViritualCon = mViritualCon.add(virtualCoin);
            BigDecimal afterAddInteger = mInteger.subtract(virtualCoin.multiply(new BigDecimal(10)));
            member.setVirtualCoin(afterAddViritualCon);
            member.setIntegral(afterAddInteger);

            memberManagerDao.updateMember(member);

            // 往虚拟币记录表里新增一条记录
            VirtualCoinRecord virtualCoinRecord = new VirtualCoinRecord();
            virtualCoinRecord.setMemberId(member);
            virtualCoinRecord.setVirtualCoin(afterAddViritualCon);
            virtualCoinRecord.setAddTime(new Date());
            virtualCoinRecord.setContent("积分兑换虚拟币");
            virtualCoinRecordManagerDao.addVirtualCoinRecord(virtualCoinRecord);

            object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS);
        } else {
            // 你的积分不足
            object = new ResponseBean(Constants.FAIL_CODE_400);
        }

        return "success";
    }

    /**
     * 消费兑换码
     */
    public String exchangeCode() {
        try {
            Member member = memberManagerDao.retrieveMember(memberId);
            if (member == null) {
                object = new ResponseBean(Constants.EXCEPTION_CODE_600, "用户不存在");
                return "success";
            }
            if (!accessToken.equals(member.getAccessToken())) {
                object = new ResponseBean(Constants.EXCEPTION_CODE_600, Constants.LOGIN_VERIFY_TOKEN_ERROR);
                return "success";
            }
            // num (0:不存在, 1:已使用 2:已经参加过同一类型的活动 3:兑换成功)
            int num = memberManagerDao.isExistCode(redeemCode, member);
            if (num == 0) {
                RedeemCodeVo code = new RedeemCodeVo();
                code.setProductName("");
                code.setRelatedId(0L);
                object = new ResponseBean(Constants.FAIL_CODE_400, "失败原因:\r\n您的兑换码不存在", code);
            } else if (num == 1) {
                RedeemCodeVo code = new RedeemCodeVo();
                code.setProductName("");
                code.setRelatedId(0L);
                object = new ResponseBean(Constants.FAIL_CODE_400, "失败原因:\r\n您的兑换码已使用", code);
            } else if (num == 2) {
                RedeemCodeVo code = new RedeemCodeVo();
                code.setProductName("");
                code.setRelatedId(0L);
                object = new ResponseBean(Constants.FAIL_CODE_400, "失败原因:\r\n您已经参与过该类型的活动", code);
            } else if (num == 3) {
                RedeemCodeVo result = memberManagerDao.exchangeCode(redeemCode, member);
                object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
        }
        return "success";
    }

    /**
     * 根据手机号与视频领取对应视频的卡券
     */
    public String videoCoupn() {
        HttpServletResponse response = ServletActionContext.getResponse();
        HttpServletRequest request = ServletActionContext.getRequest();
        response.setContentType("textml;charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Max-Age", "0");
        response.setHeader("Access-Control-Allow-Headers",
                "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("XDomainRequestAllowed", "1");
        HttpSession session1 = request.getSession();
        if (StringUtil.isNotBlank(mobile) && StringUtil.isNotBlank(mobileCode)) {
            if (!mobile.matches("\\d{11}")) {
                object = new ResponseBean(Constants.FAIL_CODE_400, "手机号码不正确");
                return "success";
            }
            // 短信验证码校验
            if (!memberManagerDao.verifyMobileCode(business, mobile, mobileCode, session1)) {
                object = new ResponseBean(Constants.FAIL_CODE_400, "验证码错误");
                return "success";
            }
            Coupn coupn = memberManagerDao.coupnByVideoId(videoId);
            if (coupn == null) {
                object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_NULL);
                return "success";
            }
            Member member = memberManagerDao.findMemberByMobile(mobile);
            RelevanceWechat relevance = new RelevanceWechat();
            BigDecimal coupnMoney = new BigDecimal(0);// true是兑换过了，false还没兑换过

            if (member != null) {
                List<RelevanceWechat> records = activityExchangeRecordManagerDao.exchangeCount(openId, mobile, videoId);
                if (records != null && records.size() > 0) {
                    object = new ResponseBean(Constants.SUCCESS_CODE_200, "您已参加过本次活动，不可重复参加!", -1);
                    return "success";
                }
                CoupnUser coupnUser = new CoupnUser();
                coupnUser.setCoupnId(coupn);
                coupnUser.setMemberId(member.getId());
                coupnUser.setSharerId(0L);
                coupnUser.setSharer("");
                coupnUser.setUsed("0");
                memberManagerDao.save(coupnUser);
            } else {// 用户没注册，后台为他注册一个账号，并赠送视频卡券
                member = new Member(mobile, "1");
                member.setGrade("1");
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.YEAR, 1);
                member.setStartTime(new Date());
                member.setYearEndTime(calendar.getTime());
                member.setType(String.valueOf(0));
                member.setRegistSource(RegistSourceEnum.ACTIVITY.getType());
                MemberVo vo = memberManagerDao.addAppMember(member, request);
                Member user = memberManagerDao.findMemberByMobile(mobile);

                CoupnUser coupnUser = new CoupnUser();
                coupnUser.setCoupnId(coupn);
                coupnUser.setMemberId(user.getId());
                coupnUser.setSharerId(0L);
                coupnUser.setSharer("");
                coupnUser.setUsed("0");
                memberManagerDao.save(coupnUser);
                relevance.setMobile(user.getMobile());
            }
            relevance.setMobile(member.getMobile());
            relevance.setOpenId(openId);
            relevance.setVideoId(videoId);
            relevance.setFromId(fromId);
            activityExchangeRecordManagerDao.exchangeRecord(relevance);
            coupnMoney = coupn.getCoupnMoney();
            object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, coupnMoney);
            return "success";
        }
        return "success";
    }

    /**
     * 查询该视频是否有可赠送的卡券
     */
    public String haveCoupn() {
        HttpServletResponse response = ServletActionContext.getResponse();
        HttpServletRequest request = ServletActionContext.getRequest();
        response.setContentType("textml;charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Max-Age", "0");
        response.setHeader("Access-Control-Allow-Headers",
                "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("XDomainRequestAllowed", "1");

        if (videoId != 0) {
            Coupn coupn = memberManagerDao.coupnByVideoId(videoId);
            CoupnVo coupnVo = new CoupnVo();
            if (coupn != null) {
                List<RelevanceWechat> records = activityExchangeRecordManagerDao.exchangeCount(openId, mobile, videoId);
                if (records != null && records.size() > 0) {
                    coupnVo.setChange(true);
                }
                coupnVo.setHave(true);
                coupnVo.setCoupnMoney(coupn.getCoupnMoney());
                coupnVo.setUseScope(coupn.getUseScope());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
                String endDate = "";
                try {
                    endDate = sdf.format(coupn.getEndDate());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                coupnVo.setEndDate(endDate);
                object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.SUCCESS, coupnVo);
            } else {
                coupnVo.setEndDate("");
                coupnVo.setUseScope("");
                object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.SUCCESS, coupnVo);
            }
        } else {
            object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_PARAMETER_EMPTY);
        }
        return "success";
    }

    /**
     * @return 用于给运营批量注册并送卡券的
     */
    public String signAndSendCoupn() {
        String[] phoneNum = mobile.split(",");
        Long[] coupnId = new Long[]{8L};
        List<Coupn> coupn = memberManagerDao.getCoupnById(coupnId);
        for (int i = 0; i < phoneNum.length; i++) {
            Member member = memberManagerDao.findMemberByMobile(phoneNum[i]);
            if (member == null) {
                member = new Member(phoneNum[i], "1");
                member.setGrade("1");
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.YEAR, 1);
                member.setStartTime(new Date());
                member.setYearEndTime(calendar.getTime());
                member.setType(String.valueOf(0));
                member.setRegistSource(RegistSourceEnum.ACTIVITY.getType());
                MemberVo vo = memberManagerDao.addAppMember(member, request);
                Member user = memberManagerDao.findMemberByMobile(phoneNum[i]);

                CoupnUser coupnUser = new CoupnUser();
                coupnUser.setCoupnId(coupn.get(0));
                coupnUser.setMemberId(user.getId());
                coupnUser.setSharerId(0L);
                coupnUser.setSharer("");
                coupnUser.setUsed("0");
                memberManagerDao.save(coupnUser);
            } else {
                CoupnUser coupnUser = new CoupnUser();
                coupnUser.setCoupnId(coupn.get(0));
                coupnUser.setMemberId(member.getId());
                coupnUser.setSharerId(0L);
                coupnUser.setSharer("");
                coupnUser.setUsed("0");
                memberManagerDao.save(coupnUser);
            }

        }
        return "";
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getFileContent() {
        return fileContent;
    }

    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getImageFolderName() {
        return imageFolderName;
    }

    public void setImageFolderName(String imageFolderName) {
        this.imageFolderName = imageFolderName;
    }

    public String getFileFolderName() {
        return fileFolderName;
    }

    public void setFileFolderName(String fileFolderName) {
        this.fileFolderName = fileFolderName;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getWtpDeploy() {
        return wtpDeploy;
    }

    public void setWtpDeploy(String wtpDeploy) {
        this.wtpDeploy = wtpDeploy;
    }

    public Long getCollectId() {
        return collectId;
    }

    public void setCollectId(Long collectId) {
        this.collectId = collectId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Date collectTime) {
        this.collectTime = collectTime;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public String getAnswerType() {
        return answerType;
    }

    public void setAnswerType(String answerType) {
        this.answerType = answerType;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getVoice() {
        return voice;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }

    public int getViewSum() {
        return viewSum;
    }

    public void setViewSum(int viewSum) {
        this.viewSum = viewSum;
    }

    public int getLikeSum() {
        return likeSum;
    }

    public void setLikeSum(int likeSum) {
        this.likeSum = likeSum;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public List<Collect> getCollectsLict() {
        return collectsLict;
    }

    public void setCollectsLict(List<Collect> collectsLict) {
        this.collectsLict = collectsLict;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getRegionId() {
        return regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getMobileCode() {
        return mobileCode;
    }

    public void setMobileCode(String mobileCode) {
        this.mobileCode = mobileCode;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWorkYears() {
        return workYears;
    }

    public void setWorkYears(String workYears) {
        this.workYears = workYears;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getWorkCard() {
        return workCard;
    }

    public void setWorkCard(String workCard) {
        this.workCard = workCard;
    }

    public String getVisitCard() {
        return visitCard;
    }

    public void setVisitCard(String visitCard) {
        this.visitCard = visitCard;
    }

    public String getIdcardFront() {
        return idcardFront;
    }

    public void setIdcardFront(String idcardFront) {
        this.idcardFront = idcardFront;
    }

    public String getIdcardBack() {
        return idcardBack;
    }

    public void setIdcardBack(String idcardBack) {
        this.idcardBack = idcardBack;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public String getCardholder() {
        return cardholder;
    }

    public void setCardholder(String cardholder) {
        this.cardholder = cardholder;
    }

    public String getOpenBank() {
        return openBank;
    }

    public void setOpenBank(String openBank) {
        this.openBank = openBank;
    }

    public String getBankMobile() {
        return bankMobile;
    }

    public void setBankMobile(String bankMobile) {
        this.bankMobile = bankMobile;
    }

    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }

    public String getWorkCardName() {
        return workCardName;
    }

    public void setWorkCardName(String workCardName) {
        this.workCardName = workCardName;
    }

    public String getVisitCardName() {
        return visitCardName;
    }

    public void setVisitCardName(String visitCardName) {
        this.visitCardName = visitCardName;
    }

    public String getIdcardFrontName() {
        return idcardFrontName;
    }

    public void setIdcardFrontName(String idcardFrontName) {
        this.idcardFrontName = idcardFrontName;
    }

    public String getIdcardBackName() {
        return idcardBackName;
    }

    public void setIdcardBackName(String idcardBackName) {
        this.idcardBackName = idcardBackName;
    }

    public Collect getCollect() {
        return collect;
    }

    public void setCollect(Collect collect) {
        this.collect = collect;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Long getPackageId() {
        return packageId;
    }

    public void setPackageId(Long packageId) {
        this.packageId = packageId;
    }

    public BigDecimal getWithdrawMoney() {
        return withdrawMoney;
    }

    public void setWithdrawMoney(BigDecimal withdrawMoney) {
        this.withdrawMoney = withdrawMoney;
    }

    @Override
    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public void setServletResponse(HttpServletResponse response) {
        this.response = response;
    }

    public BigDecimal getVirtualCoin() {
        return virtualCoin;
    }

    public void setVirtualCoin(BigDecimal virtualCoin) {
        this.virtualCoin = virtualCoin;
    }

    public String getAfterCompoundImg() {
        return afterCompoundImg;
    }

    public void setAfterCompoundImg(String afterCompoundImg) {
        this.afterCompoundImg = afterCompoundImg;
    }

    public BigDecimal getTopUpMoney() {
        return topUpMoney;
    }

    public void setTopUpMoney(BigDecimal topUpMoney) {
        this.topUpMoney = topUpMoney;
    }

    public BigDecimal getChangeBalance() {
        return changeBalance;
    }

    public void setChangeBalance(BigDecimal changeBalance) {
        this.changeBalance = changeBalance;
    }

    public String getUsed() {
        return used;
    }

    public void setUsed(String used) {
        this.used = used;
    }

    public IOSVerifyVo getIosVerifyVo() {
        return iosVerifyVo;
    }

    public void setIosVerifyVo(IOSVerifyVo iosVerifyVo) {
        this.iosVerifyVo = iosVerifyVo;
    }

    public String getRedeemCode() {
        return redeemCode;
    }

    public void setRedeemCode(String redeemCode) {
        this.redeemCode = redeemCode;
    }

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public int getFromId() {
        return fromId;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }

    public Long getFrequency() {
        return frequency;
    }

    public void setFrequency(Long frequency) {
        this.frequency = frequency;
    }

}
