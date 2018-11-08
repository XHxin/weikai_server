package cc.messcat.web.server;

import cc.messcat.entity.*;
import cc.messcat.enums.RegistSourceEnum;
import cc.messcat.vo.IOSVerifyVo;
import cc.messcat.vo.ResponseBean;
import cc.modules.commons.PageAction;
import cc.modules.constants.Constants;
import cc.modules.security.ExceptionManager;
import cc.modules.util.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.util.AlipayNotify;
import com.mipush.MiPushHelper;
import com.qcloud.Module.Live;
import com.youzan.open.sdk.client.auth.Token;
import com.youzan.open.sdk.client.core.DefaultYZClient;
import com.youzan.open.sdk.client.core.YZClient;
import com.youzan.open.sdk.client.oauth.OAuth;
import com.youzan.open.sdk.client.oauth.OAuthContext;
import com.youzan.open.sdk.client.oauth.OAuthFactory;
import com.youzan.open.sdk.client.oauth.OAuthType;
import com.youzan.open.sdk.gen.v3_0_0.api.YouzanItemGet;
import com.youzan.open.sdk.gen.v3_0_0.model.YouzanItemGetParams;
import com.youzan.open.sdk.gen.v3_0_0.model.YouzanItemGetResult;
import com.youzan.open.sdk.gen.v4_0_0.api.YouzanTradeGet;
import com.youzan.open.sdk.gen.v4_0_0.model.YouzanTradeGetParams;
import com.youzan.open.sdk.gen.v4_0_0.model.YouzanTradeGetResult;
import com.youzan.open.sdk.gen.v4_0_0.model.YouzanTradeGetResult.StructurizationOrderInfoDetail;
import com.youzan.open.sdk.gen.v4_0_0.model.YouzanTradeGetResult.StructurizationTradeItemDetail;
import com.youzan.open.sdk.gen.v4_0_0.model.YouzanTradeGetResult.StructurizationTradeOrderInfo;
import com.youzan.open.sdk.util.json.JsonUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

public class PayAction extends PageAction implements ServletRequestAware, ServletResponseAware {
	
	private static final long serialVersionUID = -3159299708113547592L;
	private static final String clientId = PropertiesFileReader.getByKey("clientId");
	private static final String clientSecret = PropertiesFileReader.getByKey("clientSecret");
	private static final String shopId = PropertiesFileReader.getByKey("youzan.shopId");
	private static final String leaveWordKey = PropertiesFileReader.getByKey("leave.word");
	private static String projectName = PropertiesFileReader.getByKey("web.domain");// 项目名
	private static Logger log = LoggerFactory.getLogger(PayAction.class);
	private String loginToken;
	private String accessToken;
	private BigDecimal changeMoney;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private Object object;
	private Member member;
	private Long memberId; // 会员id
	private Long regionId;// 区域id
	private Long productId;// 产品id
	private Long standardReadId;// 标准解读
	private Long eBusinessProductId;// 电商产品id
	private Long hotProblemId; //
	private String utype;
	private String ptype;// 1 支付宝 2微信支付
	private String stype;// 消费类型 stype （1 准入报告 2 标准解读 3 质量分享 4 付费咨询 6 电商报告）
	private Long[] relateId;// 关联Id(用于钱包支付)
	private HotProblem problem;
	private IOSVerifyVo iosVerifyVo;
	private Long[] coupnId;//用户卡券编号
	private Long beRewardId;// 被打赏人memberId
	private Double rewardMoney;// 打赏的金额
	private String relateIdStr;// ios传过来的商品id，多件以逗号分隔
	private String coupnIdStr;// ios传的卡券id，多张以逗号分隔
	private Integer distribute;//0-不是分销 1-是分销
	private Long expenseTotalId;//购买记录id

	/**
	 * 支付宝充值
	 * @author Geoff
	 * @date 2016年11月27日
	 */
	public String getPayAlipayPrams() throws ExceptionManager, UnsupportedEncodingException {
		try {
			if (changeMoney == null || changeMoney.compareTo(new BigDecimal(0)) == 0 || changeMoney.compareTo(new BigDecimal(0)) == -1) {
				object = new ResponseBean(Constants.FAIL_CODE_400, "金额有误");
				return "getPayAlipayPrams";
			}
			object = payManagerDao.addRechargeOrder(member, changeMoney);
		} catch (Exception e) {
			e.printStackTrace();
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
		}
		return "success";
	}

	/**
	 * 微信充值
	 * @author Geoff
	 * @date 2016年11月27日
	 * 
	 */
	public String payWechatApp() throws Exception {
		try {
			if (changeMoney == null || changeMoney.compareTo(new BigDecimal(0)) == 0 || changeMoney.compareTo(new BigDecimal(0)) == -1) {
				object = new ResponseBean(Constants.FAIL_CODE_400, "金额有误");
				return "payWechatApp";
			}
			object = payManagerDao.addWechatAppOrder(member, changeMoney, request);
		} catch (Exception e) {
			e.printStackTrace();
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
		}
		return "success";
	}


	/******************************************** 支付回调 ****************/
	/**
	 * 支付宝的支付回调
	 * @author Geoff
	 * @return
	 * @throws ExceptionManager
	 * @throws IOException
	 * @date 2016年11月27日
	 */
	public String returnAppNotify() throws ExceptionManager, IOException {
		log.error("PayAction:166-支付宝支付成功进入回调");
		try {
			Map<String, String> params = new HashMap<String, String>();
			Map<String, String[]> requestParams = request.getParameterMap();

			for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
				String name = iter.next();
				String[] values = (String[]) requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
				}
				params.put(name, valueStr);
			}
			log.error("PayAction:180-支付宝返回信息：params" + params);
			String tradeStatus = params.get("trade_status"); // 支付状态
			log.error("PayAction:182-支付宝返回信息-支付状态（成功TRADE_SUCCESS：）：tradeStatus" + tradeStatus);
			// 签名校验
			if (AlipayNotify.verify(params)) {
				if (tradeStatus.equals("TRADE_SUCCESS")) {
					payManagerDao.addHandlerOrder(params, "Y");
					response.getOutputStream().write("success".getBytes());
				}
			} else {
				payManagerDao.addHandlerOrder(params, "N");
				response.getOutputStream().write("failure".getBytes());
			}
		} catch (Exception e) {
			e.printStackTrace();
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
		}
		return NONE;
	}

	/**
	 * 微信支付回调接口
	 * @author Geoff
	 * @return
	 * @throws ExceptionManager
	 * @throws IOException
	 * @date 2016年11月27日
	 */
	public String getNotifyResult() throws ExceptionManager, IOException {
		try {
			payManagerDao.addNotifyResult(request, response);
		} catch (Exception e) {
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
		}
		return NONE;
	}

	/**
	 * 苹果内购回调
	 * @return
	 * @throws IOException
	 */
	public Object iOSVerify() throws IOException {
		try {
			object = payManagerDao.updateBuysRecordRechargeOrderFromApple(member, iosVerifyVo, request, response);

		} catch (Exception e) {
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_FAIL);
		}
		return "success";
	}

	/**
	 * 在有赞商城购买后，回调此方法，把订单信息同步回我们的系统
	 * @return
	 * @throws IOException 
	 */
	public synchronized void youzanNotify() throws IOException {
		int contentLength = request.getContentLength();
		byte buffer[] = new byte[contentLength];
		for(int i = 0; i < contentLength;) {
			int len = request.getInputStream().read(buffer, i, contentLength - i);
			if(len == -1) {
				break;
			}
			i += len;
		}
		String srt1 = new String(buffer, "UTF-8");
		JSON json = (JSON)JSON.parse(srt1);
		YouzanPushEntity entity = (YouzanPushEntity)JSON.toJavaObject(json, YouzanPushEntity.class);
		JSONObject returnStr = new JSONObject();
		returnStr.put("code", 0);
		returnStr.put("msg", "success");
        /**
         *  判断是否为心跳检查消息
         *  1.是则直接返回
         */
        if (entity.isTest()) {
        	HttpServletResponse response = ServletActionContext.getResponse();
    		response.setCharacterEncoding("UTF-8");
    		PrintWriter writer = response.getWriter();
    		writer.print(returnStr);
    		writer.flush();
    		writer.close();
        }
        /**
         * 解析消息推送的模式  这步判断可以省略
         * 0-商家自由消息推送 1-服务商消息推送
         * 以服务商 举例
         * 判断是否为服务商类型的消息
         * 否则直接返回
         */
        /*if (entity.getMode() != 1 ){
            return res;
        }*/

        /**
         * 判断消息是否合法
         * 解析sign
         * MD5 工具类开发者可以自行引入
         */
        String sign= MD5.GetMD5Code(clientId+entity.getMsg()+clientSecret);
        if (!sign.equals(entity.getSign().toUpperCase()) && !sign.equals(entity.getSign())){
        	HttpServletResponse response = ServletActionContext.getResponse();
    		response.setCharacterEncoding("UTF-8");
    		PrintWriter writer = response.getWriter();
    		writer.print(returnStr);
    		writer.flush();
    		writer.close();
        }
        /**
         * 对于msg 先进行URI解码
         */
        String msg="";
        try {
             msg= URLDecoder.decode(entity.getMsg(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        /**
         *  ..........
         *  接下来是一些业务处理
         *  判断当前消息的类型 比如交易
         *
         */
        log.error("有赞entity.getType***********************************" + entity.getType());
        log.error("有赞entity.getStatus***********************************" + entity.getStatus());
        if ("TRADE_ORDER_STATE".equals(entity.getType())) {//订单状态事件
        	if("WAIT_SELLER_SEND_GOODS".equals(entity.getStatus()) || "WAIT_BUYER_CONFIRM_GOODS".equals(entity.getStatus())) {//等待卖家发货，即:买家已付款 --实物商品 || --虚拟商品
        		// 再获取token
        		OAuth oauth = OAuthFactory.create(OAuthType.SELF, new OAuthContext(clientId, clientSecret, Long.valueOf(shopId)));
        		String token = JsonUtils.toJson(oauth.getToken());
        		JSONObject jsonObject = JSONObject.parseObject(token);
        		JSON json2 = (JSON) jsonObject;
        		YZAccessToken yzToken = (YZAccessToken)JSON.toJavaObject(json2, YZAccessToken.class);
        		log.error("token: " + yzToken.getAccess_token());
        		YZClient client = new DefaultYZClient(new Token(yzToken.getAccess_token())); //new Sign(appKey, appSecret)
        		YouzanTradeGetParams youzanTradeGetParams = new YouzanTradeGetParams();
				log.error("entity.getId: " + entity.getId());
        		youzanTradeGetParams.setTid(entity.getId());
        		YouzanTradeGet youzanTradeGet = new YouzanTradeGet();
        		youzanTradeGet.setAPIParams(youzanTradeGetParams);
        		YouzanTradeGetResult result = client.invoke(youzanTradeGet);
        		StructurizationTradeOrderInfo  order = result.getFullOrderInfo();//交易基础信息结构体
        		StructurizationTradeItemDetail[] refundInfo = order.getOrders();
        		String itemId = null;//商品编码（我们系统对应的商品id）

                //itemType等于31为“知识付费”，由于知识付费本来没有提供商品编码字段返回对应的系统商品编号，所以把商品编号放在简介里返回
				log.error("type : "+refundInfo[0].getItemType());
                if(refundInfo[0].getItemType()==31){
                    String title = refundInfo[0].getTitle();//商品标题
                    //【产品编号：XXXX】
                    int startIndex = title.indexOf("：");
                    int endIndex = title.indexOf("】");
                    if(startIndex > 0 && endIndex > 0) {
                        itemId = title.substring(startIndex,endIndex);
                    }else {
                        log.error("有赞商城购买回调：知识付费商品没有在标题中添加商品编码，标题为：" + title);
                        return;
                    }
                    YouzanItemGetParams youzanItemGetParams = new YouzanItemGetParams();
                    YouzanItemGet youzanItemGet = new YouzanItemGet();
                    youzanItemGet.setAPIParams(youzanItemGetParams);
                    YouzanItemGetResult yzItemDetail = client.invoke(youzanItemGet);
                    System.out.println(yzItemDetail.toString());
                }else {
                    itemId = refundInfo[0].getOuterItemId();

                }
        		String leaveWord = refundInfo[0].getBuyerMessages();//商品留言
                Long yzGoodsId = refundInfo[0].getItemId();//在有赞商城的商品id,要保存，退款时需要
        		JSONObject leaveWordObj = JSONObject.parseObject(leaveWord);
        		String mobile = leaveWordObj.getString(leaveWordKey);
        		Float money = refundInfo[0].getPayment();//商品最终均摊价
        		StructurizationOrderInfoDetail orderDetail = order.getOrderInfo();//交易基础信息结构体--交易基础信息
        		String tickid = orderDetail.getTid();//订单号
        		Long videoId = 0L;
        		try {
					videoId = Long.valueOf(itemId);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
        		LiveVideo video = liveService.getLiveVideoById(videoId);
        		if(video != null) {
        			//拿到留言输入的手机号码，未注册的先注册
        			Pattern pattern = Pattern.compile(Constants.PHONE_NUMBER_REG);
        			if(pattern.matcher(mobile).matches()) {
        				Member member = memberManagerDao.findMemberByMobile(mobile);//
        				if(member == null) {
        					member = new Member(mobile, "1");
        					member.setRegistSource(RegistSourceEnum.ACTIVITY.getType());
        					memberManagerDao.addAppMember(member, request);
        				}
        				ExpenseTotal buysRecord = new ExpenseTotal("8", "有赞商城购买", member.getId(), tickid, new BigDecimal(money), video.getPrice(), "1", video.getId());
        				buysRecord.setYouzanRelatedID(yzGoodsId);
        				ExpenseTotal existRecord = expenseTotalManagerDao.checkBuyStatus(member.getId(),video.getId());
        				if(existRecord == null) {
        					expenseTotalManagerDao.save(buysRecord);
        					//写流水
        					payManagerDao.addIncomeByPayNew(0, member, buysRecord);
        				}
        			}
        		}else {
					log.error("有赞回调***********************************商品编码有误");
				}
        	}
        }
        /**
         * 返回结果
         */
        HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		PrintWriter writer = response.getWriter();
		writer.print(returnStr);
		writer.flush();
		writer.close();
	}
	/******************************************** 支付宝支付 ****************/


	/*public void testPay() {
		System.out.println("start>>>>>>>>>>>>>>>>>>");
		ExecutorService excutor = Executors.newCachedThreadPool();
		for(int i = 0; i < relateId.length; i++) {
			final int NO = i;
			
			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					myPay(coupnId[NO],relateId[NO]);
				}
			};
			excutor.execute(runnable);
		}
		excutor.shutdown();
	}
	
	public synchronized void myPay(Long coupnId2, Long relateId2) {
		ExpenseTotal expenseTotal = expenseTotalManagerDao.checkBuyStatus(coupnId2, relateId2);
		Member member2 = memberManagerDao.retrieveMember(coupnId2);
		System.out.println("写流水开始》》》》》》》");
		payManagerDao.addIncomeByPayNew(distribute, member2, expenseTotal);
		System.out.println("写流水结束》》》》》》》");
	}*/
	
/*	*//**
	 * 使用 app 准入报告购买接口、支付宝 （修改了购买记录表）
	 *//*
	public String alipayAccessReportPay() {
		try {
			if (memberId == null || regionId == null || productId == null) {
				object = new ResponseBean(Constants.FAIL_CODE_400, "参数有误");
				return "success";
			}
			ExpenseTotal record = new ExpenseTotal();
			Date today=new Date();
			record.setAddTime(today);
			record.setPayTime(today);
			record.setType("1");// 消费类型 1为准入报告
			record.setPayType("2");// 付款类型（1、微信 2：支付宝）
			record.setPayStatus("0");// 付款状态（0：未付款 1：已付款）
			record.setNumber(OrderSnUtil.generateMemberRechargeSn());// 生成的单号
			record.setMemberId(memberId);
			record.setExpertId(0L);
			*//**
			 * 准入报告购买查standard表，根据 regionId，productId 查询唯一记录。
			 *//*
			// Standard standard =standardManagerDao.getStandardByConSimple(regionId, productId);
			record.setRegionId(regionId);
			Product product = productManagerDao.retrieveProduct(productId);
			record.setRelatedId(product.getId());
			record.setAddTime(new Date());
			// 准入报告统一收费金额
			DivideScaleCommon divideScaleCommon = webSiteManagerDao.gainDivideScaleCommon();
			changeMoney = new BigDecimal(String.valueOf(divideScaleCommon.getUnifyFee()));
			record.setOriginalPrice(changeMoney==null ? new BigDecimal(0):changeMoney);
			record.setCoupnMoney(new BigDecimal(0));
			record.setMoney(changeMoney==null ? new BigDecimal(0) :changeMoney);
			record.setContent("购买准入报告");
			object = payManagerDao.addAliPayOrder(member, record);
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}*/

	/************************************************* 微信支付 ****************/
/*
	*//**
	 * 使用 1.2准入报告购买接口开发 ，微信 （修改了购买记录表）
	 *//*
	public String wechatAccessReportPay() throws Exception {
		try {
			if (memberId == null || regionId == null || productId == null) {
				object = new ResponseBean(Constants.FAIL_CODE_400, "参数有误");
				return "success";
			}
			ExpenseTotal record = new ExpenseTotal();
			Date today=new Date();
			record.setAddTime(today);
			record.setPayTime(today);
			record.setType("1");// 消费类型 1为购买报告
			record.setPayType("1"); // 付款类型（1、微信 2：支付宝）
			record.setPayStatus("0");// 付款状态（0：未付款 1：已付款）
			record.setNumber(OrderSnUtil.generateMemberRechargeSn());// 生成的单号
			record.setMemberId(memberId);
			record.setExpertId(0L);
			*//**
			 * 准入报告购买查standard表，根据 regionId，productId 查询唯一记录。
			 *//*
			// Standard standard =standardManagerDao.getStandardByConSimple(regionId, productId);
			record.setRegionId(regionId);
			Product product = productManagerDao.retrieveProduct(productId);
			record.setRelatedId(product.getId());
			DivideScaleCommon divideScaleCommon = webSiteManagerDao.gainDivideScaleCommon();
			changeMoney = divideScaleCommon.getUnifyFee();
			record.setOriginalPrice(changeMoney);
			record.setCoupnMoney(new BigDecimal(0));
			record.setMoney(changeMoney);// 金额
			record.setContent("购买准入报告");
			object = payManagerDao.addWechatOrder(member, record, request);
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
		}
		return "success";
	}*/

	/**
	 * 购买标准解读、质量分享(微信、支付宝)
	 *//*
	@Deprecated
	public String standardAndQualityPay() {
		try {
			if (memberId == null || standardReadId == null) {
				object = new ResponseBean(Constants.FAIL_CODE_400, "参数有误");
				return "success";
			}
			ExpenseTotal record = new ExpenseTotal();
			Date today=new Date();
			record.setAddTime(today);
			record.setPayTime(today);
			record.setPayType(ptype);// 付款类型
			record.setPayStatus("0");// 付款状态
			record.setNumber(OrderSnUtil.generateMemberRechargeSn());
			StandardReading standardRead = standardReadManagerDao
					.retrieveStandardReading(Integer.valueOf(standardReadId.toString()));
			if ("1".equals(standardRead.getClassify())) {// 1：购买标准解读
				record.setType("2");
				record.setContent("购买标准解读");
			} else if ("2".equals(standardRead.getClassify())) {// 2：购买质量分享
				record.setType("3");
				record.setContent("购买质量分享");
			} else {
				record.setType("2");
				record.setContent("购买标准解读");
			}
			record.setRegionId(0L);
			record.setExpertId(0L);
			record.setRelatedId(standardRead.getId());
			record.setMemberId(memberId);
			record.setOriginalPrice(standardRead.getMoney());
			record.setCoupnMoney(new BigDecimal(0));
			record.setMoney(standardRead.getMoney());// 金额
			if ("1".equals(ptype)) {// 微信
				object = payManagerDao.addWechatOrder(member, record, request);
				return "success";
			} else if("2".equals(ptype)){// 支付宝
				object = payManagerDao.addAliPayOrder(member, record);
				return "success";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}*/

	/**
	 * 购买电商准入报告（微信、支付宝）
	 *//*
	@Deprecated
	public String shopAccessReportPay() {
		try {
			if (memberId == null || eBusinessProductId == null) {
				object = new ResponseBean(Constants.FAIL_CODE_400, "参数有误");
				return "success";
			}
			EBusinessProduct eBusinessProduct = businessProductManagerDao.retrieveEProduct(eBusinessProductId);
			if(eBusinessProduct != null) {
				BigDecimal productPrice = new BigDecimal(String.valueOf(eBusinessProduct.getMoney()));
				ExpenseTotal record = new ExpenseTotal();
				Date today=new Date();
				record.setAddTime(today);
				record.setPayTime(today);
				record.setType("6");
				record.setPayType(ptype);// 付款类型
				record.setPayStatus("0");// 付款状态
				record.setNumber(OrderSnUtil.generateMemberRechargeSn());
				record.setMemberId(memberId);
				record.setRegionId(0L);
				record.setExpertId(0L);
				record.setRelatedId(eBusinessProduct.getId());
				record.setOriginalPrice(productPrice);
				record.setCoupnMoney(new BigDecimal(0));
				record.setMoney(productPrice);// 金额
				record.setContent("购买电商准入报告详情");
				if ("1".equals(ptype)) {// 微信
					object = payManagerDao.addWechatOrder(member, record, request);
					return "success";
				} else {// 支付宝
					object = payManagerDao.addAliPayOrder(member, record);
					return "success";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}*/

	/**
	 * 购买付费咨询（微信、支付宝）
	 *//*
	public String askPayConsultPay() {
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
			Long memberId=member.getId();
			Long expertId=problem.getExpertId();
			String name=EmojiUtils.emojiChange(problem.getName()); // 提交的问题
			String type=problem.getType();  // 提问方式(0为公开,1为私密)
			String status="0";  //未付款
			HotProblem entity = new HotProblem(memberId,expertId,name,type,status);
			hotReplyService.commitProblem(entity);
			log.error(member.getRealname()+"提交问题成功"+ DateHelper.dataToString(new Date(),"yyyy年MM月dd日 HH时mm分ss秒"));

			hotProblemId = hotReplyService.getMaxHotProblemId();
			ExpenseTotal record = new ExpenseTotal();
			Date today=new Date();
			record.setAddTime(today);
			record.setPayTime(today);
			record.setType("4");
			record.setPayType(ptype);// 付款类型
			record.setPayStatus("0");// 付款状态
			record.setNumber(OrderSnUtil.generateMemberRechargeSn());
			record.setMemberId(memberId);
			record.setRelatedId(hotProblemId);
			record.setRegionId(0L);
			record.setExpertId(problem.getExpertId());
			if (problem.getType().equals("0")) {
				record.setContent("公开提问付费咨询");
			} else {
				record.setContent("私密提问付费咨询");
			}
			// 根据专家id查询专家收费
			HotReplyFees hotReplyFees = hotReplyService.getReplyFeesByMemberId(problem.getExpertId());
			record.setCoupnMoney(new BigDecimal(0));
			if (hotReplyFees == null) {
				if (problem.getType().equals("0")) {// 公开提问
					BigDecimal publicAnswerPeice = new BigDecimal("6.0");
					record.setOriginalPrice(publicAnswerPeice);
					record.setMoney(publicAnswerPeice);// 金额
				} else {
					BigDecimal priviceAnswerPrice = new BigDecimal("8.0");
					record.setOriginalPrice(priviceAnswerPrice);
					record.setMoney(priviceAnswerPrice);// 金额
				}
			} else {
				if (problem.getType().equals("0")) {// 公开提问
					BigDecimal publicAnswerPeice = new BigDecimal(String.valueOf(hotReplyFees.getMoney()));
					record.setOriginalPrice(publicAnswerPeice);
					record.setMoney(publicAnswerPeice);// 金额
				} else {
					BigDecimal priviceAnswerPrice = new BigDecimal(String.valueOf(hotReplyFees.getPrivateMoney()));
					record.setOriginalPrice(priviceAnswerPrice);
					record.setMoney(priviceAnswerPrice);// 金额
				}
			}
			if ("1".equals(ptype)) {// 微信
				object = payManagerDao.addWechatPayConsult(member, record, request);
				return "success";
			} else if("2".equals(ptype)){// 支付宝
				object = payManagerDao.addAliPayPayConsult(member, record);
				return "success";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}*/

	/**
	 * 购买围观问题（微信、支付宝）
	 *//*
	@Deprecated
	public String payConsultPay() {
		try {
			log.error("PayAction:567-进入付费咨询：购买围观问题");
			if (memberId == null || hotProblemId == null) {
				object = new ResponseBean(Constants.FAIL_CODE_400, "参数有误");
				return "success";
			}
			// 判断该会员id和问题id是否被购买过
			ExpenseTotal brcon = new ExpenseTotal();
			brcon.setType("4");
			brcon.setMemberId(memberId);
			brcon.setRelatedId(hotProblemId);
			List<ExpenseTotal> brlist = buysRecordManagerDao.getBuyRecordByCon(brcon);
			if (ObjValid.isValid(brlist)) {
				object = new ResponseBean(Constants.FAIL_CODE_400, "已购买该问题");
				return "success";
			}
			HotProblem hotProblem = hotReplyService.getHotProblemById(hotProblemId);
			if (hotProblem == null) {
				object = new ResponseBean(Constants.FAIL_CODE_400, "参数有误:hotProblem不正确");
				return "success";
			}
			if(hotProblem.getMemberId().equals(memberId)){
				object = new ResponseBean(Constants.FAIL_CODE_400, "不可以购买自己提的问题");
				return "success";
			}
			ExpenseTotal record = new ExpenseTotal();
			Date today=new Date();
			record.setAddTime(today);
			record.setPayTime(today);
			record.setType("4");
			record.setPayType(ptype);// 付款类型
			record.setPayStatus("0");// 付款状态
			record.setRegionId(0L);
			record.setNumber(OrderSnUtil.generateMemberRechargeSn());
			record.setMemberId(memberId);
			record.setExpertId(hotProblem.getExpertId());
			record.setRelatedId(hotProblem.getId());
			record.setContent("购买付费咨询");
			// 根据专家id查询专家收费
			HotReplyFees hotReplyFees = hotReplyService.getReplyFeesByMemberId(hotProblem.getExpertId());
			log.error("PayAction:591-根据专家id查询专家收费：replyFees=" + hotReplyFees);
			DivideScaleCommon divideScaleCommon = webSiteManagerDao.gainDivideScaleCommon();
			BigDecimal money = divideScaleCommon.getPayOtherProblemUnifyFee();
			record.setOriginalPrice(money);
			record.setCoupnMoney(new BigDecimal(0));
			record.setMoney(money);// 金额
			if ("1".equals(ptype)) {// 微信
				object = payManagerDao.addWechatOrder(member, record, request);
				return "success";
			} else {// 支付宝
				object = payManagerDao.addAliPayOrder(member, record);
				return "success";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}*/

	/******************************************** 苹果内购 ****************/
	/**
	 *  购买准入报告（苹果内购）
	 *//*
	@Deprecated
	public String appleAccessReportPay() {
		try {
			if (memberId == null || regionId == null || productId == null) {
				object = new ResponseBean(Constants.FAIL_CODE_400, "参数有误");
				return "success";
			}
			object = payManagerDao.addBuysRecordRechargeOrderFromApple(member, regionId, productId);
		} catch (Exception e) {
			e.printStackTrace();
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_FAIL);
		}
		return "success";
	}*/

	/**
	 * 购买标准解读、质量分享（苹果内购）
	 * 
	 *//*
	@Deprecated
	public String appleStandardAndQualityPay() {
		try {
			if (memberId == null || standardReadId == null) {
				object = new ResponseBean(Constants.FAIL_CODE_400, "参数有误");
				return "success";
			}
			object = payManagerDao.addBuysRecordRechargeOrderFromApple(member, standardReadId);
		} catch (Exception e) {
			e.printStackTrace();
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_FAIL);
		}
		return "success";
	}

	*//**
	 *  购买电商准入报告（苹果内购）
	 * 
	 * @return
	 *//*
	@Deprecated
	public String appleShopAccessReportPay() {
		try {
			if (memberId == null || eBusinessProductId == null) {
				object = new ResponseBean(Constants.FAIL_CODE_400, "参数有误");
				return "success";
			}
			object = payManagerDao.addEBusinessProductBuysRecordRechargeOrderFromApple(member, eBusinessProductId);
		} catch (Exception e) {
			e.printStackTrace();
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_FAIL);
		}
		return "success";
	}*/

	/******************************************** 虚拟币支付 ****************/

/*	*//**
	 * 虚拟币支付
	 *//*
	@Deprecated
	public String virtualCoinPay() {
		try {
			if (memberId == null || relateId == null) {
				object = new ResponseBean(Constants.FAIL_CODE_400, "参数有误");
				return "success";
			}
			ExpenseTotal record = new ExpenseTotal();
			// 准入报告 按照统一收费
			DivideScaleCommon divideScaleCommon = webSiteManagerDao.gainDivideScaleCommon();
			changeMoney = divideScaleCommon.getUnifyFee();
			// 先根据消费类型查出购买金额
			switch (stype) {
			case "1":// 准入报告
				if (regionId == null) {
					object = new ResponseBean(Constants.FAIL_CODE_400, "参数有误");
					return "success";
				}
				Product product = productManagerDao.retrieveProduct(relateId[0]);
				record.setRegionId(regionId);
				record.setRelatedId(product.getId());
				record.setContent("购买准入报告（虚拟币支付）");
				break;
			case "2":// 标准解读
				StandardReading standardRead = standardReadManagerDao.retrieveStandardReading(relateId[0].intValue());
				if (standardRead == null) {
					object = new ResponseBean(Constants.FAIL_CODE_400, "参数有误");
					return "success";
				}
				record.setRelatedId(standardRead.getId());
				record.setContent("购买标准解读（虚拟币支付）");
				changeMoney = standardRead.getMoney();
				break;
			case "3":// 质量分享
				StandardReading standardRead2 = standardReadManagerDao.retrieveStandardReading(relateId[0].intValue());
				if (standardRead2 == null) {
					object = new ResponseBean(Constants.FAIL_CODE_400, "参数有误");
					return "success";
				}
				record.setRelatedId(standardRead2.getId());
				record.setContent("购买质量分享（虚拟币支付）");
				changeMoney = standardRead2.getMoney();
				break;
			case "4":// 付费咨询（购买他人问题）
				HotProblem hotProblem = hotReplyService.getHotProblemById(relateId[0]);
				if (hotProblem == null) {
					object = new ResponseBean(Constants.FAIL_CODE_400, "参数有误");
					return "success";
				}
				record.setRelatedId(hotProblem.getId());
				record.setContent("购买付费咨询（虚拟币支付）");
				changeMoney = divideScaleCommon.getPayOtherProblemUnifyFee();
				break;
			case "6":// 电商
				EBusinessProduct eBusinessProduct = businessProductManagerDao.retrieveEProduct(relateId[0]);
				if (eBusinessProduct == null) {
					object = new ResponseBean(Constants.FAIL_CODE_400, "参数有误");
					return "success";
				}
				record.setRelatedId(eBusinessProduct.getId());
				record.setContent("购买电商准入报告（虚拟币支付）");
				changeMoney = eBusinessProduct.getMoney();
				break;
			}
			if (changeMoney == null) {
				object = new ResponseBean(Constants.FAIL_CODE_400, "参数有误");
				return "success";
			}
			// 先查询会员有多少虚拟币，然后将需购买的虚拟币和会员现有虚拟币对比
			member = memberManagerDao.retrieveMember(memberId);
			BigDecimal virtualCoin = member.getVirtualCoin();
			if (virtualCoin == null) {
				virtualCoin = new BigDecimal(0);
			}
			if (changeMoney.compareTo(virtualCoin) == 1) {
				object = new ResponseBean(Constants.FAIL_CODE_400, "虚拟币不足");
				return "success";
			}
			record.setPayTime(new Date());
			record.setMoney(changeMoney);
			record.setAddTime(new Date());
			record.setType(stype);
			record.setPayType("5");// 付款类型（1、微信 2：支付宝 3:苹果内购 4:钱包支付 5:虚拟币）
			record.setPayStatus("1");// 付款状态（0：未付款 1：已付款）
			record.setNumber(OrderSnUtil.generateMemberRechargeSn());
			record.setMemberId(memberId);
			object = payManagerDao.addVirtualCoinPay(record, member);
		} catch (Exception e) {
			e.printStackTrace();
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_FAIL);
		}
		return "success";
	}*/

/*	*//**
	 * 虚拟币支付 付费咨询提出问题购买
	 *//*
	@Deprecated
	public String virtualCoinByPayConsultPay() {
		try {
			if (memberId == null) {
				object = new ResponseBean(Constants.FAIL_CODE_400, "参数有误");
				return "success";
			}
			ExpenseTotal record = new ExpenseTotal();

			// 先查询会员有多少虚拟币，然后将需购买的虚拟币和会员现有虚拟币对比
			member = memberManagerDao.retrieveMember(memberId);
			BigDecimal virtualCoin = member.getVirtualCoin();
			if (virtualCoin == null) {
				virtualCoin = new BigDecimal(0);
			}
			// 根据专家id查询专家收费
			HotReplyFees hotReplyFees = hotReplyService.getReplyFeesByMemberId(problem.getExpertId());
			if (hotReplyFees == null) {
				if (problem.getType().equals("0")) {// 公开提问
					changeMoney = new BigDecimal("6.0");
				} else {
					changeMoney = new BigDecimal("8.0");
				}
			} else {
				if (problem.getType().equals("0")) {// 公开提问
					changeMoney = hotReplyFees.getMoney();
				} else {
					changeMoney = hotReplyFees.getPrivateMoney();
				}
			}

			if (changeMoney.compareTo(virtualCoin) == 1) {
				object = new ResponseBean(Constants.FAIL_CODE_400, "虚拟币不足");
				return "success";
			}
			Long memberId=member.getId();
			Long expertId=problem.getExpertId();
			String name=problem.getName(); // 提交的问题
			String type=problem.getType(); // 提问方式(0为公开,1为私密)
			String status="1";
			HotProblem entity = new HotProblem(memberId,expertId,name,type,status);
			hotReplyService.commitProblem(entity);
			hotProblemId = hotReplyService.getMaxHotProblemId();

			record.setOriginalPrice(changeMoney);
			record.setMoney(changeMoney);// 金额
			record.setAddTime(new Date());
			record.setType("4");
			record.setPayType("5");// 付款类型（1、微信 2：支付宝 3:苹果内购 4:钱包支付 5:虚拟币）
			record.setPayStatus("1");// 付款状态（0：未付款 1：已付款）
			String number = OrderSnUtil.generateMemberRechargeSn();
			record.setNumber(number);// 生成的单号
			record.setMemberId(memberId);
			record.setRelatedId(hotProblemId);
			record.setExpertId(problem.getExpertId());
			if (problem.getType().equals("0")) {
				record.setContent("公开提问付费咨询（虚拟币支付）");
			} else {
				record.setContent("私密提问付费咨询（虚拟币支付）");
			}
			object = payManagerDao.addVirtualCoinPay(record, member);
		} catch (Exception e) {
			e.printStackTrace();
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_FAIL);
		}
		return "success";
	}*/

	/**
	 * 计算卡券的总抵消金额
	 * 把使用了的卡券的状态改为已用
	 * @param coupnId
	 * @return
	 */
	private BigDecimal sumCoupnMoney(Long[] coupnId) {
		BigDecimal coupnMoney = new BigDecimal(0);
		// 获取卡券抵扣金额
		if (coupnId != null && coupnId.length > 0) {
			List<CoupnUser> coupns = memberManagerDao.getCoupnsById(coupnId);
			if (coupns != null) {
				for (int i = 0; i < coupns.size(); i++) {
					if (coupns.get(i).getUsed().equals("0")) {
						coupnMoney = coupnMoney.add(coupns.get(i).getCoupnId().getCoupnMoney());
						//标记为已使用
						coupns.get(i).setUsed("1");
						memberManagerDao.updateUserCoupn(coupns.get(i));
					}
				}
			}
		}
		return coupnMoney;
	}

	/******************************************** 钱包支付 ****************/
	/**
	 * 钱包支付（统一支付接口）
	 * @return
	 * @throws IOException
	 */
	public String walletPay() throws IOException {
		if (memberId == null) {
			object = new ResponseBean(Constants.SUCCESS_CODE_300, Constants.MSG_PARAMETER_EMPTY);
			return "success";
		}
		//准入报告regionId不能为null
		if ("1".equals(stype) && regionId == null) {
			object = new ResponseBean(Constants.SUCCESS_CODE_300, Constants.MSG_PARAMETER_EMPTY);
			return "success";
		}

		relateId = StringUtil.splitString(relateIdStr,",");
		coupnId = StringUtil.splitString(coupnIdStr,",");
		// 判断该商品是否已购
		ExpenseTotal buysRecord = memberManagerDao.checkBuyStatus(member, stype, relateId);
		if (buysRecord != null) {
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_REPETITIVE_BUY);
			return "success";
		}

		/**卡券总抵扣金额*/
		BigDecimal coupnMoney = sumCoupnMoney(coupnId);

		try {
			ExpenseTotal recordNew = new ExpenseTotal();
			// 准入报告 按照统一收费
			DivideScaleCommon divideScaleCommon = webSiteManagerDao.gainDivideScaleCommon();
			changeMoney = divideScaleCommon.getUnifyFee();
			// 先根据消费类型查出消费的金额
			/***********switch beging***********************************************************/
			switch (stype) {
			case "1":// 准入报告
				Product product = productManagerDao.retrieveProduct(relateId[0]);
				recordNew.setRegionId(regionId);
				recordNew.setRelatedId(product.getId());
				recordNew.setContent("购买准入报告（钱包支付）");
				recordNew.setExpertId(0L);
				break;
			case "2":// 标准解读
				StandardReading standardRead = standardReadManagerDao.retrieveStandardReading(relateId[0].intValue());
				if (standardRead == null) {
					object = new ResponseBean(Constants.SUCCESS_CODE_300, Constants.MSG_FIND_NULL);
					return "success";
				}
				recordNew.setRelatedId(standardRead.getId());
				recordNew.setContent("购买标准解读（钱包支付）");
				changeMoney = standardRead.getMoney();
				recordNew.setRegionId(0L);
				recordNew.setExpertId(standardRead.getAuthor().getId());
				break;
			case "3":// 质量分享
				StandardReading standardRead2 = standardReadManagerDao.retrieveStandardReading(relateId[0].intValue());
				if (standardRead2 == null) {
					object = new ResponseBean(Constants.SUCCESS_CODE_300, Constants.MSG_FIND_NULL);
					return "success";
				}
				recordNew.setRelatedId(standardRead2.getId());
				recordNew.setContent("购买质量分享（钱包支付）");
				changeMoney = standardRead2.getMoney();
				recordNew.setRegionId(0L);
				recordNew.setExpertId(standardRead2.getAuthor().getId());
				break;
			case "4":// 付费咨询（购买围观问题）
				HotProblem hotProblem = hotReplyService.getHotProblemById(relateId[0]);
				if (hotProblem == null) {
					object = new ResponseBean(Constants.SUCCESS_CODE_300, Constants.MSG_FIND_NULL);
					return "success";
				}
				recordNew.setRelatedId(hotProblem.getId());
				recordNew.setContent("购买付费咨询（钱包支付）");
				changeMoney = divideScaleCommon.getPayOtherProblemUnifyFee();
				recordNew.setRegionId(0L);
				recordNew.setExpertId(hotProblem.getExpertId());
				break;
			case "6":// 电商
				EBusinessProduct eBusinessProduct = businessProductManagerDao.retrieveEProduct(relateId[0]);
				if (eBusinessProduct == null) {
					object = new ResponseBean(Constants.SUCCESS_CODE_300, Constants.MSG_FIND_NULL);
					return "success";
				}
				recordNew.setRelatedId(relateId[0]);
				recordNew.setContent("购买电商准入报告（钱包支付）");
				changeMoney = eBusinessProduct.getMoney();
				recordNew.setRegionId(0L);
				recordNew.setExpertId(0L);
				break;
			case "8":// 视频,只能购买单一或系列
				List<LiveVideo> videos = liveService.getLiveVideosById(relateId);
				if (videos.size() == 0) {
					object = new ResponseBean(Constants.SUCCESS_CODE_300, Constants.MSG_FIND_NULL);
					return "success";
				}
				// 无论是系列,章节还是子视频，都要加到购买记录表里
				for (LiveVideo liveVideo : videos) {
					String content="购买视频（钱包支付）";
					String orderNum=OrderSnUtil.generateMemberRechargeSn();
					BigDecimal money= null;
					BigDecimal originalPrice=liveVideo.getPrice();
					String payType="4";
					long relatedId=liveVideo.getId();
					changeMoney = liveVideo.getPrice();
					// 先查询用户钱包余额，然后将购买所需的钱和用户钱包余额作对比
					Wallet wallet = walletManagerDao.get(memberId);
					BigDecimal balance = wallet.getBalance();
					if (changeMoney.compareTo(balance.add(coupnMoney)) == 1) {
						object = new ResponseBean(Constants.FAIL_CODE_400, "余额不足,请到钱包进行充值！");
						return "success";
					}
					BigDecimal residue = coupnMoney.subtract(changeMoney);
					if (residue.compareTo(new BigDecimal(0)) == 0 || residue.compareTo(new BigDecimal(0)) == 1) {
						money=new BigDecimal(0);
						coupnMoney=changeMoney;
					} else {
						money=changeMoney.subtract(coupnMoney);
						coupnMoney=coupnMoney;
					}
					ExpenseTotal expenseTotal = new ExpenseTotal(stype,content,memberId,orderNum,money,coupnMoney,originalPrice,payType,relatedId,liveVideo.getExpertId());
					coupnMoney = coupnMoney.subtract(changeMoney);
					//isGiftCard为1即意味着购买视频会赠送卡券一张（该视频对应的专家的公开提问卡券）
					if(liveVideo.getIsGiftCard() == 1) {
						givenUserCoupn(liveVideo);
					}
					expenseTotalManagerDao.save(expenseTotal);
					object = payManagerDao.addWalletCoinPay(expenseTotal, member);
				}
				// 查询购买记录表，把记录存入视频表中
				/*List<ExpenseTotal> buyRecordNews = expenseTotalManagerDao.findAllBuysByLiveVideoId(relateId);
				Set<Object> viewCount = new HashSet<>();
				for (ExpenseTotal buys : buyRecordNews) {
					viewCount.add(buys.getMemberId());
				}
				for (LiveVideo liveVideo : videos) {
					if (liveVideo.getFatherId() != -1L && liveVideo.getFatherId() != 0L) {//章节
						LiveVideo live = this.liveService.getLiveVideoById(liveVideo.getFatherId());
						if (live != null) {
							live.setStudyCount((long) viewCount.size() > live.getStudyCount() ? (long) viewCount.size()
									: (live.getStudyCount() + 1));
							this.liveService.update(liveVideo);
						}
					}
					liveVideo.setStudyCount((long) viewCount.size() > liveVideo.getStudyCount()
							? (long) viewCount.size() : (liveVideo.getStudyCount() + 1));
					this.liveService.update(liveVideo);
				}*/
				return "success";
			case "9":
				StandardReading standardRead3 = standardReadManagerDao.retrieveStandardReading(relateId[0].intValue());
				if (standardRead3 == null) {
					object = new ResponseBean(Constants.SUCCESS_CODE_300, "参数有误");
					return "success";
				}
				recordNew.setRelatedId(standardRead3.getId());
				recordNew.setContent("购买专栏（钱包支付）");
				changeMoney = standardRead3.getMoney();
				recordNew.setRegionId(0L);
				recordNew.setExpertId(standardRead3.getAuthor().getId());
				break;
			}
			//******************switch结束*****************

			if (changeMoney == null) {
				object = new ResponseBean(Constants.SUCCESS_CODE_300, "参数有误");
				return "success";
			}
			// 先查询用户钱包余额，然后将购买所需的钱和用户钱包余额作对比
			Wallet wallet = walletManagerDao.get(memberId);
			BigDecimal balance = wallet.getBalance();
			if (changeMoney.compareTo(balance.add(coupnMoney)) == 1) {
				object = new ResponseBean(Constants.FAIL_CODE_400, "余额不足,请到钱包进行充值！");
				return "success";
			}
			BigDecimal residue = coupnMoney.subtract(changeMoney);
			if (residue.compareTo(new BigDecimal(0)) == 1 || residue.compareTo(new BigDecimal(0)) == 0 ) {
				recordNew.setMoney(new BigDecimal(0));
			} else {
				recordNew.setMoney(changeMoney.subtract(coupnMoney));
			}

			recordNew.setCoupnMoney(coupnMoney);
			recordNew.setOriginalPrice(changeMoney);
			recordNew.setAddTime(new Date());
			recordNew.setPayTime(new Date());
			recordNew.setType(stype);
			recordNew.setPayType("4");// 付款类型（1、微信 2：支付宝 3:虚拟币支付4:钱包）
			recordNew.setPayStatus("1");// 付款状态（0：未付款 1：已付款）
			recordNew.setNumber(OrderSnUtil.generateMemberRechargeSn());
			recordNew.setMemberId(member.getId());
			expenseTotalManagerDao.save(recordNew);
			object = payManagerDao.addWalletCoinPay(recordNew, member);
		/*	// 获取卡券抵扣金额
			if (coupnId != null && coupnId.length > 0) {
				List<CoupnUser> coupns = memberManagerDao.getCoupnsById(coupnId);
				if(coupns != null && coupns.size() >0) {
					for (int i = 0; i < coupns.size(); i++) {
						if (coupns.get(i).getUsed().equals("0")) {
							// 把卡券的状态改为已用
							coupns.get(i).setUsed("1");
							memberManagerDao.updateUserCoupn(coupns.get(i));
						} else {
							object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_COUPN_NOTUSE);
						}
					}
				}
			}*/
		} catch (Exception e) {
			e.printStackTrace();
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_FAIL);
		}
		return "success";
	}


	/**
	 * 给购买视频课程的人赠送该视频对应的专家的付费咨询公开提问券一张
	 * @param video
	 */
	private void givenUserCoupn(LiveVideo video) {
		/*
		 * 赠送答疑代金券的课程, 要给用户发送推送消息
		 */
			//这个讲师的付费咨询公开提问价格是多少就赠送多少钱的卡券，先获取设置的专家的公开提问与私密提问的价格
			HotReplyFees hotReplyFees = hotReplyService.getReplyFeesByMemberId(video.getExpertId());
			BigDecimal giveCoupnMoney = null;
			if (hotReplyFees == null) {
				giveCoupnMoney = new BigDecimal("6.0");
			} else {
				giveCoupnMoney = hotReplyFees.getMoney();
			}
			String useScope="付费咨询优惠卡卷";
			//不可叠加
			String overlying="0";
			//不可分享
			String isShare="0";
			Date date=new Date();
			Calendar calendar=new GregorianCalendar();
			calendar.setTime(date);
			calendar.add(Calendar.MONTH,1);//取一个月之后的此时此刻
			Date endTime=calendar.getTime();
			String scopeNum="4";
			int videoId=video.getExpertId().intValue();

			Coupn coupn=new Coupn(giveCoupnMoney,useScope,overlying,isShare,endTime,scopeNum,videoId);
			//保存卡券,并返回卡券的信息
			Coupn newCoupn=memberManagerDao.saveCoupn(coupn);
			Long sharerId=0L;
			String sharer="";
			String memberName=member.getRealname();
			CoupnUser coupnUser =new CoupnUser(memberId,newCoupn,sharerId,sharer,memberName);
			memberManagerDao.save(coupnUser);
			String description="尊敬的用户，您好，您所购买的《"+video.getTitle()+"》课程赠送的付费咨询专家一对一优惠卡卷现已发送到您的卡包，请您查收！————世界认证地图";
			MiPushHelper.sendAndroidUserAccount("世界认证地图", description, "weikai://cert-map?target=card", member.getMobile());
			MiPushHelper.sendIOSUserAccount(description, "weikai://cert-map?target=card", member.getMobile());
			//查看此购买者是否有扫过与这课程相关的二维码，如果有，为分享者添加积分并发推送
	}


	/**
	 * 付费咨询提出问题
	 */
	public String walletByPayConsultPay() {
		if (memberId == null) {
			object = new ResponseBean(Constants.FAIL_CODE_400, "参数有误");
			return "success";
		}
		if (memberId != null && memberId.equals(problem.getExpertId())) { // 不可以向自己提问
			object = new ResponseBean(Constants.SUCCESS_CODE_300, Constants.MSG_PAYCONSULT_FAIL);
			return "success";
		}
		coupnId = StringUtil.splitString(coupnIdStr,",");
		// 获取卡券抵扣金额
		BigDecimal coupnMoney = sumCoupnMoney(coupnId);
		try {
			ExpenseTotal recordNew = new ExpenseTotal();
			// 先查询会员钱包余额，然后将会员购买所需余额与会员现有余额作对比
			Wallet wallet = walletManagerDao.get(memberId);
			BigDecimal balance = wallet.getBalance();
			// 根据专家Id查询专家收费
			HotReplyFees hotReplyFees = hotReplyService.getReplyFeesByMemberId(problem.getExpertId());
			if (hotReplyFees == null) {
				if (problem.getType().equals("0")) {// 公开提问
					changeMoney = new BigDecimal("6.0");
				} else {
					changeMoney = new BigDecimal("8.0");
				}
			} else {
				if (problem.getType().equals("0")) {// 公开提问
					changeMoney = hotReplyFees.getMoney();
				} else {
					changeMoney = hotReplyFees.getPrivateMoney();
				}
			}
			if (changeMoney.compareTo(balance.add(coupnMoney)) == 1) {
				object = new ResponseBean(Constants.FAIL_CODE_400, "余额不足,请到钱包进行充值");
				return "success";
			}
			Long memberId=member.getId();
			Long expertId=problem.getExpertId();
			String name=EmojiUtils.emojiChange(problem.getName()); // 提交的问题
			String type=problem.getType(); // 提问的方式（0：公开提问1：私密提问）
			String status="1";
			HotProblem entity = new HotProblem(memberId,expertId,name,type,status);
			hotReplyService.commitProblem(entity);

			hotProblemId = hotReplyService.getMaxHotProblemId();
			BigDecimal residue = coupnMoney.subtract(changeMoney);
			if(residue.compareTo(new BigDecimal("0")) == 1 || residue.compareTo(new BigDecimal("0")) == 0) {
				recordNew.setMoney(new BigDecimal(0));
			}else{
				recordNew.setMoney(changeMoney.subtract(coupnMoney));
			}
			recordNew.setCoupnMoney(coupnMoney);
			recordNew.setOriginalPrice(changeMoney);
			recordNew.setAddTime(new Date());
			recordNew.setRegionId(0L);
			recordNew.setDivideScaleId(0);
			recordNew.setType("4");
			recordNew.setPayType("4");	// 付款类型（1、微信 2：支付宝 3：苹果 4:钱包 5：虚拟币）
			recordNew.setPayStatus("1");// 付款状态（0：未付款 1：已付款）

			// 当提问者提问成功,购买状态为已支付, 则向回答问题的专家发送短信提醒
			if (projectName.contains("cert-map")) {
				Member expert = this.memberManagerDao.retrieveMember(this.problem.getExpertId());
				String strmem = (this.member.getRealname() == null) || (this.member.getRealname().isEmpty())
						? "用户" + this.member.getId() : this.member.getRealname();
				String strExp = expert.getRealname() == null || expert.getRealname().isEmpty()
						? "手机号为" + expert.getMobile() + "的专家"
						: expert.getRealname() + ",手机号为[" + expert.getMobile() + "]的专家";
				String strp = problem.getType().equals("0") ? "公开" : "私密"; // (0为公开,1为私密)
				String expertContent="尊敬的专家：您好！" + strmem + "向您提了一个价值" + this.changeMoney + "元的问题，请您在48小时有效期内解答，打开APP，快快回答问题吧！";
				String serviceContent=strmem + "向" + strExp + "提了一个价值" + changeMoney + "元的" + strp + "问题，请提醒该专家回答问题！";
				SmsUtil.sendMessage(expert.getMobile(),expertContent); 	//提醒专家
				List<ConsultService> list=consultServerManagerDao.getConsultServerList();
				if(CollectionUtil.isListNotEmpty(list)){
					for(ConsultService consultService:list){
						SmsUtil.sendMessage(consultService.getPhone(),serviceContent); // 提醒运营
					}
				}
			}
			recordNew.setNumber(OrderSnUtil.generateMemberRechargeSn());
			recordNew.setMemberId(member.getId());
			recordNew.setRelatedId(hotProblemId);
			recordNew.setExpertId(problem.getExpertId());
			recordNew.setPayTime(new Date());
			if (problem.getType().equals("0")) {
				recordNew.setContent("付费咨询(公开)");
			} else {
				recordNew.setContent("付费咨询(私密)");
			}
			ExpenseTotal expenseTotal =payManagerDao.addExpenseTotal(recordNew);
			object = payManagerDao.addWalletPayConsult(expenseTotal, member, coupnId);
			/*// 获取卡券抵扣金额
			if (coupnId != null && coupnId.length > 0) {
				List<CoupnUser> coupns = memberManagerDao.getCoupnsById(coupnId);
				if (coupns != null) {
					for (int i = 0; i < coupns.size(); i++) {
						if (coupns.get(i).getUsed().equals("0")) {
							// 把卡券的状态改为已用
							coupns.get(i).setUsed("1");
							memberManagerDao.updateUserCoupn(coupns.get(i));
						} else {
							object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_COUPN_NOTUSE);
							return "success";
						}
					}
				}
			}*/
		} catch (Exception e) {
			e.printStackTrace();
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_FAIL);
		}
		return "success";
	}

	/**
	 * 	打赏接口
	 * 	memberId ：打赏人
	 * 	beRewardId ：被打赏人
	 * 	rewardMoney ： 打赏金额
	 */
	public String giveAReward() {
		if (member != null && member.getAccessToken().equals(accessToken)) {
			if (memberId != null && beRewardId != null && rewardMoney != null) {
				if (rewardMoney > 0) {
					object = payManagerDao.addIncomeByReward(memberId, beRewardId, rewardMoney);
				} else {
					object = new ResponseBean(Constants.FAIL_CODE_400, "打赏金额必须大于0");
				}
			} else {
				object = new ResponseBean(Constants.FAIL_CODE_400, Constants.MSG_PARAMETER_EMPTY);
			}
		}
		return "success";
	}

	/**
	 * 视频分销流水接口
	 * @return
	 */
	public String distributeExpense(){
		object = payManagerDao.distributeExpense(distribute,expenseTotalId);
		return "success";
	}
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public String getLoginToken() {
		return loginToken;
	}

	public void setLoginToken(String loginToken) {
		this.loginToken = loginToken;
	}

	public BigDecimal getChangeMoney() {
		return changeMoney;
	}

	public void setChangeMoney(BigDecimal changeMoney) {
		this.changeMoney = changeMoney;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
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

	public Long getStandardReadId() {
		return standardReadId;
	}

	public void setStandardReadId(Long standardReadId) {
		this.standardReadId = standardReadId;
	}

	public Long geteBusinessProductId() {
		return eBusinessProductId;
	}

	public void seteBusinessProductId(Long eBusinessProductId) {
		this.eBusinessProductId = eBusinessProductId;
	}

	public String getUtype() {
		return utype;
	}

	public void setUtype(String utype) {
		this.utype = utype;
	}

	public String getPtype() {
		return ptype;
	}

	public void setPtype(String ptype) {
		this.ptype = ptype;
	}

	public String getStype() {
		return stype;
	}

	public Long getHotProblemId() {
		return hotProblemId;
	}

	public void setHotProblemId(Long hotProblemId) {
		this.hotProblemId = hotProblemId;
	}

	public void setStype(String stype) {
		this.stype = stype;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public HotProblem getProblem() {
		return problem;
	}

	public void setProblem(HotProblem problem) {
		this.problem = problem;
	}

	public Long[] getRelateId() {
		return relateId;
	}

	public void setRelateId(Long[] relateId) {
		this.relateId = relateId;
	}

	public Long[] getCoupnId() {
		return coupnId;
	}

	public void setCoupnId(Long[] coupnId) {
		this.coupnId = coupnId;
	}

	public IOSVerifyVo getIosVerifyVo() {
		return iosVerifyVo;
	}

	public void setIosVerifyVo(IOSVerifyVo iosVerifyVo) {
		this.iosVerifyVo = iosVerifyVo;
	}

	public Long getBeRewardId() {
		return beRewardId;
	}

	public void setBeRewardId(Long beRewardId) {
		this.beRewardId = beRewardId;
	}

	public Double getRewardMoney() {
		return rewardMoney;
	}

	public void setRewardMoney(Double rewardMoney) {
		this.rewardMoney = rewardMoney;
	}

	public String getRelateIdStr() {
		return relateIdStr;
	}

	public void setRelateIdStr(String relateIdStr) {
		this.relateIdStr = relateIdStr;
	}

	public String getCoupnIdStr() {
		return coupnIdStr;
	}

	public void setCoupnIdStr(String coupnIdStr) {
		this.coupnIdStr = coupnIdStr;
	}

	public Integer getDistribute() {
		return distribute;
	}

	public void setDistribute(Integer distribute) {
		this.distribute = distribute;
	}

	public Long getExpenseTotalId() {
		return expenseTotalId;
	}

	public void setExpenseTotalId(Long expenseTotalId) {
		this.expenseTotalId = expenseTotalId;
	}
}
