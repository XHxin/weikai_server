package cc.messcat.quartz;

import java.io.*;
import java.math.BigDecimal;
import java.net.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import cc.modules.constants.Constants;
import cc.modules.util.*;
import com.alibaba.fastjson.JSON;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import com.alipay.config.AlipayConfig;
import com.alipay.util.AlipaySubmit;
import com.wxpay.Configuration;
import com.wxpay.pay.SignUtil;
import cc.messcat.dao.system.ExpenseExpertDao;
import cc.messcat.dao.system.ExpensePlatDao;
import cc.messcat.entity.ExpenseTotal;
import cc.messcat.entity.ExpenseExpert;
import cc.messcat.entity.HotProblem;
import cc.messcat.entity.HotReply;
import cc.messcat.entity.Member;
import cc.messcat.entity.ExpensePlatform;
import cc.messcat.entity.Wallet;
import cc.messcat.entity.WechatBillRows;
import cc.messcat.service.member.BuysRecordManagerDao;
import cc.messcat.service.member.ExpenseTotalManagerDao;
import cc.messcat.service.member.MemberManagerDao;
import cc.messcat.service.member.WalletManagerDao;
import cc.messcat.service.paycosult.HotReplyService;

/**
 * 编写任务类
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class ProblemInvalidTrigger extends QuartzJobBean {

    private static final Logger log = LoggerFactory.getLogger(ProblemInvalidTrigger.class);
    private static String domain = PropertiesFileReader.getByKey("web.domain");// 域名地址
    private static String weiXinAppid = Configuration.getProperty("weixin4j.appid");
    private static String weixinMchid = Configuration.getProperty("weixin4j.mchid");
    private HotReplyService hotReplyService;
    private BuysRecordManagerDao buysRecordManagerDao;
    private ExpenseTotalManagerDao expenseTotalManagerDao;
    private MemberManagerDao memberManagerDao;
    private WalletManagerDao walletManagerDao;
    private ExpenseExpertDao expertIEDao;
    private ExpensePlatDao expensePlatDao;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
    }

    /**
     * 问题超过48小时未回答,则问题失效,退还用户虚拟币
     */
    public void problemInvalid() throws Exception {
        // log.error("开始检查有没有超过48小时的问题.............");
        Date twoDayAgo = new Date(System.currentTimeMillis() - 2 * 24 * 60 * 60 * 1000L);
        List<HotProblem> problemList = hotReplyService.getProblemList();
        if (CollectionUtil.isListNotEmpty(problemList)) {
            for (HotProblem problem : problemList) {
                HotReply hotReply = hotReplyService.getHotReply(problem.getId(), problem.getExpertId());
                if (hotReply == null) { // 判断问题是否被回答,这个定时任务主要是针对未回答的问题
                    Date addTime = problem.getAddTime();
                    if (addTime != null) {
                        if (twoDayAgo.after(addTime)) { // 已过48小时
                            /**
                             * 操作新交易记录表
                             */
                            ExpenseTotal buyRecordNew = expenseTotalManagerDao.getProblemRecord(problem.getExpertId(),problem.getId());
                            if (buyRecordNew != null) {
                                expenseTotalManagerDao.updateProblemRecord(buyRecordNew); // 把交易记录状态更改为退款
                                problem.setStatus("2"); // status=2为 退款状态
                                hotReplyService.updateProblem(problem); // 把问题状态更改为退款
                                /**
                                 * 把钱退还到用户的钱包
                                 */
                                Wallet wallet = walletManagerDao.get(buyRecordNew.getMemberId());
                                BigDecimal balance = wallet.getBalance().add(buyRecordNew.getMoney());
                                wallet.setBalance(balance);
                                walletManagerDao.update(wallet);
                            }
                            problem.setDispose(1);
                            hotReplyService.updateProblem(problem);
                        }
                    }
                }
            }
        }
    }

    /**
     * 卡券到期了,更改为不可用状态
     */
    public void updateCouponExpire() {
        memberManagerDao.updateCouponExpire();
    }

    /**
     * 卡券到期提醒：【到期前3天发送】
     */
    public void couponExpireRemind() {
        if (domain.equals("http://www.cert-map.com") || domain.equals("www.cert-map.com")) {
            memberManagerDao.couponExpireRemind();
        }
    }

    /**
     * 会员到期提醒：【到期前3天发送】
     */
    public void memberExpireRemind() {
        if (domain.equals("http://www.cert-map.com") || domain.equals("www.cert-map.com")) {
            memberManagerDao.memberExpireRemind();
        }
    }

    /**
     * 开课提醒：【开课前30分钟发送】
     */
//	public void classesToRemind() {
//		memberManagerDao.classesToRemind();
//	}


    /**
     * 微信对账：【】
     */
    @Test
    public void checkTheBillToWechat() {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>进入微信对账<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        JSONObject jsonParam = new JSONObject();
        String nonceStr = java.util.UUID.randomUUID().toString().substring(0, 15);
        /**
         * 只能获取前一天或之前的账单
         */
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -3);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String date = sdf.format(calendar.getTime());

        Map<String, String> signMap = new HashMap();
        signMap.put("appid", weiXinAppid);
        signMap.put("mch_id", weixinMchid);
        signMap.put("nonce_str", nonceStr);
        signMap.put("bill_date", date);
        signMap.put("bill_type", "ALL");

        String paternerKey = Configuration.getProperty("weixin4j.pay.partner.key");
        String sign = SignUtil.getSign(signMap, paternerKey);
        jsonParam.put("sign", sign);

        /**
         * 以xml形式提交
         */
        String paramXml = "<xml>"
                + "<appid>" + signMap.get("appid") + "</appid>"
                + "<bill_date>" + date + "</bill_date>"
                + "<bill_type>ALL</bill_type>"
                + "<mch_id>" + signMap.get("mch_id") + "</mch_id>"
                + "<nonce_str>" + nonceStr + "</nonce_str>"
                + "<sign>" + sign + "</sign>"
                + "</xml>";

        String json = GetJsonData.getXmlData(paramXml, "https://api.mch.weixin.qq.com/pay/downloadbill");
        log.info("微信对账请求返回结果: " + json);

        /**
         * 对返回信息进行校验
         * 失败时才返回xml格式，成功时直接返回对账信息
         */
        if (json.length() > 0 && json.contains("<xml>")) {
            StringBuffer sb = new StringBuffer(json);
            Map<String, Object> result = XmlUtil.parseStringXmlToMap(sb);
            log.error("账单请求失败：" + result.get("return_msg"));
            return;
        } else if(json.length()<=0 || json.equals("")){
            log.error("获取微信账单失败！");
            return;
        } else{
            //这里校验一次表头是否一致的原因是担心微信修改了返回的数据，从而有导致以下逻辑混乱
            //130为表头的最后一个字符的索引
            String header = json.substring(0, 130);
            if (!header.equals("﻿交易时间,公众账号ID,商户号,子商户号,设备号,微信订单号,商户订单号,用户标识,交易类型,交易状态,付款银行,货币种类,总金额,企业红包金额,微信退款单号,商户退款单号,退款金额,企业红包退款金额,退款类型,退款状态,商品名称,商户数据包,手续费,费率`")) {
                log.error("表头不一致");
                return;
            }
        }


        int endIndex = json.indexOf("总交易单数,总交易额,总退款金额,总企业红包退款金额,手续费总金额");
        //-1是为了把最后一个%去掉
        String billStr = json.substring(130, endIndex - 1);
        //以费率字段的百分号作为分隔,如果对字符串的处理不明白为什么这样处理，可以观察一下返回的对账单的详细内容
        String[] aBill = billStr.split("%`");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int i = 0; i < aBill.length; i++) {
            log.info("账单" + i + "： " + aBill[i]);
            String[] billColumn = aBill[i].split(",`");
            WechatBillRows billRow = new WechatBillRows();
            Date dealTime;
            try {
                dealTime = sdf2.parse(billColumn[0]);
                billRow.setDealTime(dealTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            billRow.setOfficialAccountsId(billColumn[1]);
            billRow.setMerchantCode(billColumn[2]);
            billRow.setSonOfMerchantCode(billColumn[3]);
            billRow.setEquipmentNum(billColumn[4]);
            billRow.setWechatOrderNum(billColumn[5]);
            billRow.setMerchantOrderNum(billColumn[6]);
            billRow.setUserMark(billColumn[7]);
            billRow.setDealType(billColumn[8]);
            //我们的数据库定义0:未付款 1:已付款
            billRow.setDealStatus(billColumn[9].equals("SUCCESS") ? "1" : "0");
            billRow.setBank(billColumn[10]);
            billRow.setCurrencyType(billColumn[11]);
            billRow.setTotalMoney(billColumn[12]);
            billRow.setEnterpriseRedEnvelopeMoney(billColumn[13]);
            billRow.setWechatRefundOrderNum(billColumn[14]);
            billRow.setMerchantRefundOrderNum(billColumn[15]);
            billRow.setRefundMoney(billColumn[16]);
            billRow.setEnterpriseRedEnvelopeRefundMoney(billColumn[17]);
            billRow.setRefundType(billColumn[18]);
            billRow.setRefundStatus(billColumn[19]);
            billRow.setGoodsName(billColumn[20]);
            billRow.setMerchantDataPackage(billColumn[21]);
            billRow.setServiceCharge(billColumn[22]);
            billRow.setRate(billColumn[23]);

            /**
             * 根据账单的结果做自己的业务逻辑
             */
            ExpenseTotal expenseTotal = expenseTotalManagerDao.getBuysRecordByOrderNum(billRow.getMerchantOrderNum());
            String message = null;
            if (expenseTotal != null) {
                if (!expenseTotal.getPayStatus().equals(billRow.getDealStatus())) {
                    if (expenseTotal.getPayStatus().equals("1") && billRow.getDealStatus().equals("0")) {
                        message = "【发现有异常的交易记录】此交易记录实际为未支付，单号为：" + expenseTotal.getNumber();
                    } else if (expenseTotal.getPayStatus().equals("0") && billRow.getDealStatus().equals("1")) {
                        message = "【发现有异常的交易记录】发生掉单现象,单号为：" + expenseTotal.getNumber();
                        expenseTotal.setPayStatus("1");
                        expenseTotal.setPayTime(billRow.getDealTime());
                        expenseTotalManagerDao.updateExpenseTotal(expenseTotal);
                    } else if (!expenseTotal.getMoney().toString().equals(billRow.getTotalMoney())) {
                        message = "【发现有异常的交易记录】实际交易金额与我服务器金额不一致,单号为：" + expenseTotal.getNumber();
                    }
                }
            } else {
                message = "【发现有异常的交易记录】我服务器上找不到此订单,单号为：" + billRow.getMerchantOrderNum();
            }
            //在系统里保存异常记录
            log.error(message);
            //把异常情况告知开发者
            SmsUtil.sendMessage("13427644456", message);
        }
    }

    /**
     * 获取支付宝的对账文件下载地址
     *
     * @param billDate 要下载的账单的时间 格式为 yyyy-MM-dd
     * @return
     */
    public String getAliPayBillDownloadUrl(String billDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String requestTime = sdf.format(new Date());
        String billDownLoadUrl = "";

        try {

            Map<String, String> signParam = new HashMap<>();
            //发送请求的时间，格式"yyyy-MM-dd HH:mm:ss"
            signParam.put("timestamp", requestTime);
            //接口名称
            signParam.put("method", "alipay.data.dataservice.bill.downloadurl.query");
            //支付宝分配给开发者的应用ID
            signParam.put("app_id", AlipayConfig.appid);
            //商户生成签名字符串所使用的签名算法类型，目前支持RSA2和RSA，推荐使用RSA2
            signParam.put("sign_type", "RSA2");
            //调用的接口版本，固定为：1.0
            signParam.put("version", "1.0");
            //	请求使用的编码格式，如utf-8,gbk,gb2312等
            signParam.put("charset", "utf-8");
            //请求参数的集合，最大长度不限，除公共参数外所有请求参数都必须放在这个参数中传递，具体参照各产品快速接入文档
            signParam.put("biz_content", "{\"bill_type\":\"trade\",\"bill_date\":\"" + billDate + "\"}");
            log.info("signParam: " + signParam.toString());
            //获取sign
            Map<String, String> sPara = AlipaySubmit.buildRequestPara(signParam);

            //获取到签名之后还需要URLEncoder处理
            String urlencodeSign = URLEncoder.encode(sPara.get("sign"), "UTF-8");
            //时间磋"yyyy-MM-dd HH:mm:ss"参数中存在空格，请求是需要先URLEncoder处理
            String urlencodeTimestamp = URLEncoder.encode(requestTime, "UTF-8");
            String paramStr = "timestamp=" + urlencodeTimestamp + "&method=alipay.data.dataservice.bill.downloadurl.query&app_id=" + AlipayConfig.appid + "&sign_type=" + sPara.get("sign_type") + "&sign=" + urlencodeSign + "&version=1.0&charset=utf-8&biz_content=" + "{\"bill_type\":\"trade\",\"bill_date\":\"" + billDate + "\"}";
            String response = GetJsonData.sendGet("https://openapi.alipay.com/gateway.do", paramStr);
            log.info("getAliPayBillDownloadUrl.response：" + response);
            if (!"".equals(response)) {
                /**
                 * 获取账单的下载地址billDownLoadUrl
                 */
                com.alibaba.fastjson.JSONObject respJsonObject = JSON.parseObject(response);
                com.alibaba.fastjson.JSONObject alipayDataDataserviceBillDownloadurlQueryResponse = respJsonObject.getJSONObject("alipay_data_dataservice_bill_downloadurl_query_response");
                String code = alipayDataDataserviceBillDownloadurlQueryResponse.getString("code");
                String msg = alipayDataDataserviceBillDownloadurlQueryResponse.getString("msg");
                String sub_code = alipayDataDataserviceBillDownloadurlQueryResponse.getString("sub_code");
                String sub_msg = alipayDataDataserviceBillDownloadurlQueryResponse.getString("sub_msg");
                if (code.equals("10000")) {
                    billDownLoadUrl = alipayDataDataserviceBillDownloadurlQueryResponse.getString("bill_download_url");
                } else {
                    log.error("支付宝账单下载地址获取失败：" + response);
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return billDownLoadUrl;
    }

    /**
     * 下载支付宝对账文件，下载后为zip文件
     *
     * @param downloadUrl 下载对账文件的url
     * @param fileName    下载后的文件存放路径和文件名 例如 D:\\billDir\\2018-08-03.zip
     */
    public void downloadAliPayBill(String downloadUrl, String fileName) {
        log.error("downloadURL：" + downloadUrl + "      fileName：" + fileName);
        URL url = null;
        HttpURLConnection httpUrlConnection = null;
        InputStream fis = null;
        FileOutputStream fos = null;
        try {
            url = new URL(downloadUrl);
            httpUrlConnection = (HttpURLConnection) url.openConnection();
            httpUrlConnection.setConnectTimeout(5 * 1000);
            httpUrlConnection.setDoInput(true);
            httpUrlConnection.setDoOutput(true);
            httpUrlConnection.setUseCaches(false);
            httpUrlConnection.setRequestMethod("GET");
            httpUrlConnection.setRequestProperty("Charsert", "UTF-8");
            httpUrlConnection.connect();
            fis = httpUrlConnection.getInputStream();
            byte[] temp = new byte[1024];
            int b;
            fos = new FileOutputStream(new File(fileName));
            while ((b = fis.read(temp)) != -1) {
                fos.write(temp, 0, b);
                fos.flush();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (fos != null) {
                    fos.close();
                }
                if (httpUrlConnection != null) {
                    httpUrlConnection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 支付宝对账【】
     */
    @Test
    public void checkTheBillToAliPay() {
        log.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>支付宝对账<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        //下载后账单zip的存放路径
        String billDownloadDir = Constants.SAVE_BILL_PATH;
        //解压后的账单csv文件存放路径
        String unCompressDir = Constants.UN_COMPRESS_DIR;
        //线程监控的文件路径---->为了监控代码重用（因为是先监控该文件夹是否有新的zip文件生成，然后解压zip文件都会触发回调），这里三个路径保持一致
        String listenDir = Constants.SAVE_BILL_PATH;
        SimpleDateFormat billDateSDF = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        //查询前一天的账单
        calendar.add(Calendar.DATE, -1);
        String billDownLoadUrl = getAliPayBillDownloadUrl(billDateSDF.format(calendar.getTime()));
        if (billDownLoadUrl.equals("")) {
            return;
        }
        //下载后的账单压缩包的文件名，带绝对路径
        String fileName = billDownloadDir + File.separator + billDateSDF.format(calendar.getTime()) + ".zip";

        /**
         * 在下载对账文件前先启动一个线程去监测存放对账文件的文件夹，当对账文件下载完毕后自动解压并读取对账文件做逻辑处理
         */
        watchDir(listenDir, fileName, unCompressDir, calendar.getTime());

        downloadAliPayBill(billDownLoadUrl, fileName);
    }


    /**
     * 此方法用于监控文件夹，文件夹里有文件生成、修改、删除都会触发相应的回调
     *
     * @param listenDir     要监控的文件夹
     * @param fileName      要解压的文件
     * @param unCompressDir 要解压到哪个目录
     * @param billDate      具体某一天的账单
     */
    public void watchDir(String listenDir, String fileName, String unCompressDir, Date billDate) {

        //支付宝账户对应的数字码/解压后的csv文件的前缀---->登录b.alipay.com-账户管理-商户信息管理-查看PID/KEY
        String prefix = "20887019483452540156";
        SimpleDateFormat csvFileNameSDF = new SimpleDateFormat("yyyyMMdd");
        String billCheckingFileName = unCompressDir + prefix + "_" + csvFileNameSDF.format(billDate) + "_业务明细.csv";
        File file = new File(listenDir);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    WatchDir watchDir = new WatchDir(file, true, new FileActionCallback() {
                        //文件解压会多次调用modify，所以自己定义一个标记让它只执行一次对账
                        boolean checked = false;

                        @Override
                        public void create(File file) {
                            log.error("文件已创建\t" + file.getName() + "   " + System.currentTimeMillis());
                            try {
                                //解压zip文件
                                ZipCompressor.unCompress(fileName, unCompressDir);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void modify(File file) {
                            log.error("文件已修改\t" + file.getName() + "   " + System.currentTimeMillis());
                            if (checked == false && billCheckingFileName.equals(file.getAbsolutePath())) {
                                billChecking(unCompressDir, billDate, billCheckingFileName);
                                checked = true;
                            }
                        }
                    }
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 对账逻辑
     *
     * @param unCompressDir        账单csv文件解压的目标文件
     * @param billDate             账单的时间
     * @param billCheckingFileName 对账的csv文件 例如"D:\\watch\\20887019483452540156_20180802_业务明细.csv"
     */
    public void billChecking(String unCompressDir, Date billDate, String billCheckingFileName) {
        log.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>开始支付宝对账<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");

        SimpleDateFormat billDateSDF = new SimpleDateFormat("yyyy-MM-dd");
        /**
         * 读取解压后的csv对账文件
         */
        File csv = new File(billCheckingFileName);
        BufferedReader br = null;
        try {
            DataInputStream in = new DataInputStream(new FileInputStream(csv));
            br = new BufferedReader(new InputStreamReader(in, "gbk"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String line = "";
        //csv文件里的一行数据
        String everyLine = "";
        try {
            List<String> allString = new ArrayList<>();
            boolean dataFormat = false;
            String[] timeCondition = billDateSDF.format(billDate).split("-");
            List<ExpenseTotal> buysRecordList = expenseTotalManagerDao.getOneDayExpenseTotal(Integer.valueOf(timeCondition[0]), Integer.valueOf(timeCondition[1]), Integer.valueOf(timeCondition[2]), 2);
            List<AliPayBillRow> billRowList = new ArrayList<>();
            while ((line = br.readLine()) != null)  //读取到的内容给line变量
            {
                everyLine = line;
                log.error(everyLine);
                allString.add(everyLine);

                //确保表头信息一致，避免数据错乱。定义对账逻辑的开始标记
                if (allString.size() == 5 && everyLine.equals("支付宝交易号,商户订单号,业务类型,商品名称,创建时间,完成时间,门店编号,门店名称,操作员,终端号,对方账户,订单金额（元）,商家实收（元）,支付宝红包（元）,集分宝（元）,支付宝优惠（元）,商家优惠（元）,券核销金额（元）,券名称,商家红包消费金额（元）,卡消费金额（元）,退款批次号/请求号,服务费（元）,分润（元）,备注")) {
                    dataFormat = true;
                }
                //定义对账逻辑的结束标记，读到这一行就把标记改成false
                if (everyLine.equals("#-----------------------------------------业务明细列表结束------------------------------------")) {
//                    dataFormat = false;
                    //直接跳出循环，不需要再读取剩下的内容
                    break;
                }
                //第五行为表头，表头的下一行才是具体账单记录
                if (allString.size() > 5 && dataFormat == true) {
                    String[] billColumn = everyLine.split(",");
                    AliPayBillRow billRow = new AliPayBillRow();
                    //这里只set里其中我需要用到的字段，其他字段根据自己的需要可自行赋值
                    billRow.setMerchantOrderNum(billColumn[1].trim());
                    billRow.setDealType(billColumn[2].trim());
                    billRow.setOfficialReceipts(billColumn[12].trim());
                    billRowList.add(billRow);
                }
            }
            boolean target = false;
            String message;
            for (ExpenseTotal buysRecord : buysRecordList) {
                for (AliPayBillRow billRow : billRowList) {
                    if (buysRecord.getNumber().equals(billRow.getMerchantOrderNum())) {
                        if (billRow.getDealType().equals("交易") && buysRecord.getPayStatus().equals("0")) {
                            message = "【发现有异常的交易记录】发生掉单现象,单号为：" + buysRecord.getNumber();
                        }
                        if (!Double.valueOf(billRow.getOfficialReceipts()).equals(buysRecord.getMoney())) {
                            message = "【发现有异常的交易记录】实际交易金额与我服务器金额不一致,单号为：" + buysRecord.getNumber();
                        }
                        target = true;
                    }
                }
                if (target == false) {
                    message = "【发现有异常的交易记录】发现无中生有的购买记录，订单号为：" + buysRecord.getNumber();
                }
                //复位
                target = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HotReplyService getHotReplyService() {
        return hotReplyService;
    }

    public void setHotReplyService(HotReplyService hotReplyService) {
        this.hotReplyService = hotReplyService;
    }

    public BuysRecordManagerDao getBuysRecordManagerDao() {
        return buysRecordManagerDao;
    }

    public void setBuysRecordManagerDao(BuysRecordManagerDao buysRecordManagerDao) {
        this.buysRecordManagerDao = buysRecordManagerDao;
    }

    public MemberManagerDao getMemberManagerDao() {
        return memberManagerDao;
    }

    public void setMemberManagerDao(MemberManagerDao memberManagerDao) {
        this.memberManagerDao = memberManagerDao;
    }

    public ExpenseTotalManagerDao getExpenseTotalManagerDao() {
        return expenseTotalManagerDao;
    }

    public void setExpenseTotalManagerDao(ExpenseTotalManagerDao expenseTotalManagerDao) {
        this.expenseTotalManagerDao = expenseTotalManagerDao;
    }

    public WalletManagerDao getWalletManagerDao() {
        return walletManagerDao;
    }

    public void setWalletManagerDao(WalletManagerDao walletManagerDao) {
        this.walletManagerDao = walletManagerDao;
    }

    public ExpenseExpertDao getExpertIEDao() {
        return expertIEDao;
    }

    public void setExpertIEDao(ExpenseExpertDao expertIEDao) {
        this.expertIEDao = expertIEDao;
    }

    public ExpensePlatDao getExpensePlatDao() {
        return expensePlatDao;
    }

    public void setExpensePlatDao(ExpensePlatDao expensePlatDao) {
        this.expensePlatDao = expensePlatDao;
    }

}
