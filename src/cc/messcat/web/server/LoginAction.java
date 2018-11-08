package cc.messcat.web.server;

import cc.messcat.entity.*;
import cc.messcat.vo.MemberVo;
import cc.messcat.vo.ResponseBean;
import cc.messcat.vo.ToRegistResult;
import cc.modules.commons.PageAction;
import cc.modules.constants.Constants;
import cc.modules.uploadfile.FilesUploadModel;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;
import cc.messcat.entity.MemAuth;
import cc.messcat.entity.Member;
import cc.messcat.entity.HotReplyFees;
import cc.messcat.entity.WhiteList;
import cc.modules.util.AliyunOOSUtil;
import cc.modules.util.DateHelper;
import cc.modules.util.MD5;
import cc.modules.util.PropertiesFileReader;
import cc.modules.util.RandUtil;
import cc.modules.util.SmsUtil;
import cc.modules.util.StringUtil;
import cc.modules.util.ThridLoginUtil;
import cc.modules.util.TokenProccessor;
import cc.modules.util.XmlUtil;
import cc.messcat.enums.RegistSourceEnum;
@SuppressWarnings("serial")
public class LoginAction extends PageAction implements ServletRequestAware {

	private static String domain = PropertiesFileReader.getByKey("web.domain");
	private static Logger logger = LoggerFactory.getLogger(LoginAction.class); 
	private Object object;
	private HttpSession session;
	private HttpServletRequest request;
	private Long memberId;
	private String mobile;
	private String business;// 公司
	private String mobileCode;// 验证码
	private String role;// 会员角色
	private String terminal;// 设备类型：ios/android
	private String version;  //版本号
	private String accessToken;// 请求token
	private String photoName;// 头像图片名（需要后缀）
	private String photo;// Base编码后的头像图片
	private String realname;   //用户真实名字
	private String field; // 专业领域
	private String email;  //电子邮箱
	private String school; // 毕业学校
	private String major; // 所学专业
	private String address;  //联系地址
	private String workYears;  //工作年限
	private String intro;  //简介
	private String profession; // 职称
	private String company;    //公司
	private String position; // 职务
	private String workCardName;// 工卡图片名（需要后缀）
	private String workCard;// Base编码后的工卡图片
	private String visitCardName;// 名片图片名（需要后缀）
	private String visitCard;// Base编码后的名片图片
	private String idcardFrontName;// 身份证正面图片名（需要后缀）
	private String idcardFront;// Base编码后的身份证正面图片
	private String idcardBackName;// 身份证背面图片名（需要后缀）
	private String idcardBack;// Base编码后的身份证背面图片
	private String bankCard;  //银行卡号
	private String cardholder; //持卡人
	private String openBank;   //开户行
	private String bankMobile;  //绑定手机号
	private Member member;
	private MemAuth memAuth;
	private String recommendMobile;//推荐人手机号码
	private String uuid;
	/*
	 * 第三方登录
	 */
	private ThridLoginUtil thirdLogin = new ThridLoginUtil();
	private Long recommender_id;// 推荐人id
	private int recommend_times;// 当前ID邀请他人总次数
	private String projectName = PropertiesFileReader.getByKey("web.name");// 项目名
	private String imageFolderName = PropertiesFileReader.getByKey("upload.image.path");// 图片存放路径
	private String wtpDeploy = PropertiesFileReader.getByKey("wtpDeploy");// 项目上一级物理路径

	/**
	 * 发送手机短信验证码
	 */
	public String getMobileCode() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("textml;charset=UTF-8");
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		response.setHeader("Access-Control-Max-Age", "0");
		response.setHeader("Access-Control-Allow-Headers",
				"Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("XDomainRequestAllowed", "1");
		try {
			if (StringUtil.isBlank(business)) {
				object = new ResponseBean(Constants.FAIL_CODE_400, "business参数为空");
				return "success";
			}
			if (!mobile.matches("\\d{11}")) {
				object = new ResponseBean(Constants.FAIL_CODE_400, "手机号码不正确");
				return "success";
			}
			/**
			 *  business为regist是注册,  为login是登录, 为loginThird是三方登录(可以用来跳过手机号是否注册过的验证) ,为joinActivity是参加分享活动
			 */
			if (Constants.BUSINESS_REGIST.equals(business)) {// 注册
				if (!this.memberManagerDao.isMobileUnique(mobile, "")) {
					object = new ResponseBean(Constants.FAIL_CODE_400, "该手机号已经被注册，请输入新的手机号");
					return "success";
				}
			}
			if (Constants.BUSINESS_LOGIN.equals(business)) {// 登录
				if (!this.memberManagerDao.isExistMobile(mobile)) {
					object = new ResponseBean(Constants.FAIL_CODE_400, "该手机号未注册，请先注册");
					return "success";
				}
			}

			StringBuffer sb = new StringBuffer();
			String authCode = "";
			if(business.equals("joinActivity")) {
				authCode = RandUtil.getRandomStr(4);
			} else {
				authCode = RandUtil.getRandomStr(6);
			}
			if(Constants.BUSINESS_CHRISTMAS.equals(business)) {
				sb.append("尊敬的会员：元旦快乐！您的兑换码为").append(authCode).append("，凭兑换码可兑换年费VIP及卡券。感谢您一直以来的支持！");
			}else {
				sb.append("您的验证码是：").append(authCode).append("。请不要把验证码泄露给其他人。");
			}
			logger.error("手机号: "+mobile+" 验证码: "+authCode+" 业务: "+business);
			Map mcode = XmlUtil.parseStringXmlToMap(SmsUtil.sendMessage(mobile, sb.toString()));// 第三方？
			
			if (mcode.get("msg").equals("提交成功")) {
				session.setAttribute(business + mobile,authCode + Constants.CODE_LOGINTOKEN + System.currentTimeMillis());// 设置会话
				logger.error("获取验证码session中的key: "+business + mobile+" session中的value: "+authCode + Constants.CODE_LOGINTOKEN + System.currentTimeMillis());
				logger.error("获取验证码----------sessionId: "+session.getId());
				object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS);
			} else {
				String mobileCode = authCode;
				object = new ResponseBean(Constants.FAIL_CODE_400, mcode.get("msg").toString(), mobileCode);
			}
		} catch (Exception e) {
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
		}
		return "success";
	}

	/**
	 * 第三方登录(若是首次登录,则要提醒去绑定手机号; 若非首次登录,则通过验证后登录成功)
	 */
	public String loginThird() throws Exception {
		MemberVo memberVo = new MemberVo();
		// 登录类型(1.weixin,2.QQ)
		try {
			MemAuth entity = memberManagerDao.getMemberAuth(memAuth);
			/*
			 * 看MemAuth里面是否有第三方登录记录
			 */
			if (entity != null) {
				if (entity.getMemberId() != null) { // 判断是否有专家ID
					memberManagerDao.updateLoginThirdRecord(entity, memAuth, request);
					Member member1 = memberManagerDao.retrieveMember(entity.getMemberId());
					if (member1.getMobile() != null && !"".equals(member1.getMobile())) { // 有专家ID,还要判断是否有手机号
						memberVo = thirdLogin.setMemberVoInfo(member1, memAuth);
						object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, memberVo);
					} else {
						object = new ResponseBean(Constants.FAIL_CODE_400, "请绑定手机号");
					}
				} else {
					object = new ResponseBean(Constants.FAIL_CODE_400, "请绑定手机号");
					return "loginThird";
				}
			} else {
				object = new ResponseBean(Constants.FAIL_CODE_400, "请绑定手机号");
				return "loginThird";
			}

		} catch (Exception e) {
			e.printStackTrace();
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
		}
		return "loginThird";
	}

	/**
	 * 绑定手机号(若手机号之前未注册过, 则在用第三方身份授权后,需选择注册的身份; 
	 * 若之前已经用同样的手机号注册过,则提醒是否绑定之前注册过的手机号)
	 */
	public String bindMobile() throws Exception {
		MemberVo memberVo = new MemberVo();
		try {
			if (StringUtil.isNotBlank(mobile) && StringUtil.isNotBlank(mobileCode)) {
				if (!mobile.matches("\\d{11}")) {
					object = new ResponseBean(Constants.FAIL_CODE_400, "手机号码不正确");
					return "bindMobile";
				}
				// 短信验证码校验
				System.out.println("session的key: "+business + mobile+"----session的value: "+Constants.CODE_LOGINTOKEN + System.currentTimeMillis());
				session.setAttribute(business + mobile,mobileCode + Constants.CODE_LOGINTOKEN + System.currentTimeMillis());// 设置会话
				 if (!memberManagerDao.verifyMobileCode(business, mobile,mobileCode, session)) {
				 	object = new ResponseBean(Constants.FAIL_CODE_400, "验证码错误");
				 	return "bindMobile";
				 }
				Member member1 = memberManagerDao.findMemberByMobile(mobile);
				if (member1 == null) {
					/*
					 *  在MemAuth添加第三方登录的记录
					 */
					Member member2 = new Member(mobile, "1");
					  if(member!=null){
	    					if(member.getRole()!=null && !"".equals(member.getRole())){
	    						member2.setRole(member.getRole());// 选择注册的角色
	    					}
	                    }else {
	                    	member2.setRole("1");
	                    }
					member2.setAccessToken(TokenProccessor.getInstance().makeToken());
					member2.setRegistSource(RegistSourceEnum.APP.getType());      //注册来源为App
					Member member3 = memberManagerDao.saveMember(member2);
					if(member3.getRole().equals("2") && member3.getExpertCheckStatus().equals("1")){  //当选择注册的角色是专家的时候,需给他在问答收藏表reply_fees加一条初始记录
						memberManagerDao.insertReplyFees(member3.getId());
					}
					memberManagerDao.addMemAuth(memAuth, member3.getId(), request);
					memberVo = memberManagerDao.updateAppMember(member3, request);
					object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, memberVo);
				} else {
					MemAuth memAuth=memberManagerDao.getMemberAuth(member1);
					if(memAuth!=null){
						object = new ResponseBean(Constants.EXCEPTION_CODE_500, "该手机号已经被绑定了，请更换一个没有绑定的手机号!");
					}else{
						object = new ResponseBean(Constants.SUCCESS_CODE_300, "该手机号已注册,是否绑定之前手机号!");
					}
				}
			} else {
				object = new ResponseBean(Constants.FAIL_CODE_400, "参数为空");
			}
		} catch (Exception e) {
			e.printStackTrace();
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
		}
		return "bindMobile";
	}

	/**
	 * 绑定注册过的手机号
	 */
	public String isBind() throws Exception {
		MemberVo memberVo = new MemberVo();
		try {
			if (!mobile.matches("\\d{11}")) {
				object = new ResponseBean(Constants.FAIL_CODE_400, "手机号码不正确");
				return "bindMobile";
			}
			// 短信验证码校验
			logger.error("第三方登录验证: 手机号: "+mobile+" 验证码: "+mobileCode+" 业务: "+business+" sessionId: "+session.getId());
			if (!memberManagerDao.verifyMobileCode(business, mobile,mobileCode, session)) {
				object = new ResponseBean(Constants.FAIL_CODE_400, "验证码错误");
				return "bindMobile";
			}
			Member member1 = memberManagerDao.findMemberByMobile(mobile);
			MemAuth auth=memberManagerDao.getMemberAuth(member1);
			if(auth==null){//若之前没有绑定过,则在授权表新增一条记录
				memberManagerDao.addMemAuth(memAuth, member1.getId(), request);
			}else{//若之前已经绑定过与本次绑定不同的授权类型,则做更新操作
				memberManagerDao.updateLoginThirdRecord(auth, memAuth, request);
			}
			member1.setAccessToken(TokenProccessor.getInstance().makeToken());
			memberManagerDao.updateMember(member1);
			memberVo = memberManagerDao.updateAppMember(member1, request);
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, memberVo);
		} catch (Exception e) {
			e.printStackTrace();
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
		}
		return "bindMobile";
	}

	/**
	 * 判断手机号是否已注册
	 * code=400时,说明手机号已注册,若用户当前用微信登录,则用户之前已经用QQ授权登录过;  若用户当前用QQ登录,则用户之前已经用微信授权登录过;
	 * code=200时,说明手机号已注册,则表示当前用户既未用微信,也未用QQ授权登录过;
	 * code=300时,则表示当前手机号之前未被注册过;
	 */
	public String isRegister() throws Exception {
		Member member = memberManagerDao.findMemberByMobile(mobile);
		if (member != null) {
			MemAuth entity = memberManagerDao.getMemberAuth(member.getId(), memAuth);
			if (entity != null) {
				object = new ResponseBean(Constants.FAIL_CODE_400, "该手机已经绑定过其它的微信或QQ号");
				return "isRegister";
			} else {
				object = new ResponseBean(Constants.SUCCESS_CODE_200, "该手机号还未绑定过微信或QQ号");
				return "isRegister";
			}
		} else {
			object = new ResponseBean(Constants.SUCCESS_CODE_300, "请选择你想注册的角色");
		}
		return "isRegister";
	}

	/**
	 * 跳转注册页面，获取用户协议
	 */
	public String toRegist() {
		ToRegistResult toRegistResult = new ToRegistResult();
		toRegistResult.setResult(Constants.REQ_RESULT_SUCCESS);
		toRegistResult.setCode("0");
		String url = "http://www.cert-map.com/weikai/navigation.htm?columnId=13&columnType=content";
		toRegistResult.setUrl(url);
		object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, toRegistResult);
		return "success";
	}

	/**
	 * 注册
	 */
	@SuppressWarnings("unchecked")
	public String regist() {
		//用于扫码注册的跨域请求
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("textml;charset=UTF-8");
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		response.setHeader("Access-Control-Max-Age", "0");
		response.setHeader("Access-Control-Allow-Headers",
				"Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("XDomainRequestAllowed", "1");
		MemberVo memberVo = new MemberVo();
		try {
			if (StringUtil.isNotBlank(mobile) && StringUtil.isNotBlank(mobileCode)) {
				if (!mobile.matches("\\d{11}")) {
					object = new ResponseBean(Constants.FAIL_CODE_400, "手机号码不正确");
					return "success";
				}
				if (Constants.BUSINESS_REGIST.equals(business)) {// 注册
					if (!this.memberManagerDao.isMobileUnique(mobile, "")) {
						object = new ResponseBean(Constants.FAIL_CODE_400, "该手机号已经被注册，请输入新的手机号");
						return "success";
					}
				}
				// 短信验证码校验
				if (!memberManagerDao.verifyMobileCode(business, mobile, mobileCode, session)) {
					object = new ResponseBean(Constants.FAIL_CODE_400, "验证码错误");
					return "success";
				}
				Member member = new Member(mobile,role);
				member.setRegistSource(RegistSourceEnum.APP.getType());

				//三期分享活动，改为输入推荐人手机号码
				if(recommender_id != null){
					member.setRecommender_id(recommender_id);
				}
				memberVo = memberManagerDao.addAppMember(member, request);
                object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, memberVo);
			}
		} catch (Exception e) {
			e.printStackTrace();
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
		}
		return "success";
	}

	/**
	 * 专家注册时提交专家信息
	 */
	public String commitExpertInfo() {
		MemberVo memberVo = new MemberVo();
		try {
			Member member = memberManagerDao.retrieveMember(memberId);
			if (member != null && member.getAccessToken().equals(accessToken)) {
				if (StringUtil.isNotBlank(photo) && StringUtil.isNotBlank(photoName)) {
					FilesUploadModel fileModel = null;
					fileModel = new FilesUploadModel(photoName, photo, projectName, imageFolderName, wtpDeploy);
					fileModel.writeFileByBase64();
					File file=new File(ServletActionContext.getServletContext().getRealPath("/").replace("weikai_server", "ROOT")+"upload/enterprice/"+fileModel.getFileUri());
					member.setPhoto(savePhoto(fileModel.getFileUri(),file));
					file.delete();
				}
				if(realname!=null && !"".equals(realname)) {
					member.setRealname(realname);
				}else {
					member.setRealname(mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
				}
				if(member.getRole()==null || "".equals(member.getRole())) {
					member.setRole("1");
				}
				if(member.getExpertCheckStatus()==null || "".equals(member.getExpertCheckStatus())) {
					member.setExpertCheckStatus("0");
				}
				member.setField(field);
				member.setEmail(email);
				member.setSchool(school);
				member.setMajor(major);
				member.setAddress(address);
				member.setWorkYears(workYears);
				member.setIntro(intro);
				member.setProfession(profession);
				member.setCompany(company);
				member.setPosition(position);
				if (StringUtil.isNotBlank(workCard) && StringUtil.isNotBlank(workCardName)) {
					FilesUploadModel fileModel = null;
					fileModel = new FilesUploadModel(workCardName, workCard, projectName, imageFolderName, wtpDeploy);
					fileModel.writeFileByBase64();
					File file=new File(ServletActionContext.getServletContext().getRealPath("/").replace("weikai_server", "ROOT")+"upload/enterprice/"+fileModel.getFileUri());
					member.setWorkCard(savePhoto(fileModel.getFileUri(),file));
					file.delete();
				}
				if (StringUtil.isNotBlank(visitCard) && StringUtil.isNotBlank(visitCardName)) {
					FilesUploadModel fileModel = null;
					fileModel = new FilesUploadModel(visitCardName, visitCard, projectName, imageFolderName, wtpDeploy);
					File file=new File(ServletActionContext.getServletContext().getRealPath("/").replace("weikai_server", "ROOT")+"upload/enterprice/"+fileModel.getFileUri());
					fileModel.writeFileByBase64();
					member.setVisitCard(savePhoto(fileModel.getFileUri(),file));
					file.delete();
				}
				if (StringUtil.isNotBlank(idcardFront) && StringUtil.isNotBlank(idcardFrontName)) {
					FilesUploadModel fileModel = null;
					fileModel = new FilesUploadModel(idcardFrontName, idcardFront, projectName, imageFolderName,
							wtpDeploy);
					fileModel.writeFileByBase64();
					File file=new File(ServletActionContext.getServletContext().getRealPath("/").replace("weikai_server", "ROOT")+"upload/enterprice/"+fileModel.getFileUri());
					member.setIdcardFront(savePhoto(fileModel.getFileUri(),file));
					file.delete();
				}
				if (StringUtil.isNotBlank(idcardBack) && StringUtil.isNotBlank(idcardBackName)) {
					FilesUploadModel fileModel = null;
					fileModel = new FilesUploadModel(idcardBackName, idcardBack, projectName, imageFolderName,
							wtpDeploy);
					fileModel.writeFileByBase64();
					File file=new File(ServletActionContext.getServletContext().getRealPath("/").replace("weikai_server", "ROOT")+"upload/enterprice/"+fileModel.getFileUri());
					member.setIdcardBack(savePhoto(fileModel.getFileUri(),file));
					file.delete();
				}
				/*
				 * 填写银行卡信息
				 */
                BankMember bm=null;
                if(StringUtil.isNotBlank(bankCard) || StringUtil.isNotBlank(cardholder) || StringUtil.isNotBlank(openBank) || StringUtil.isNotBlank(bankMobile)){;
                    BankMember bankMember=new BankMember();
                    bankMember.setOpenBank(openBank);
                    bankMember.setBankCard(bankCard);
                    bankMember.setCardHolder(cardholder);
                    bankMember.setBankMobile(bankMobile);
                    bm=memberManagerDao.saveBankMember(bankMember);
                }
                if(bm!=null){
                    member.setBankId(bm.getId());
                }
				memberVo = memberManagerDao.updateMember(member);

				object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, memberVo);
			} else {
				object = new ResponseBean(Constants.FAIL_CODE_400, "登录token错误");
			}
		} catch (Exception e) {
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_EXCEPTION);
		}
		return "success";
	}

	/**
	 * 登录方法
	 * @return
	 */
	public String updateLogin() {
		//用于扫码注册的跨域请求
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("textml;charset=UTF-8");
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		response.setHeader("Access-Control-Max-Age", "0");
		response.setHeader("Access-Control-Allow-Headers",
				"Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("XDomainRequestAllowed", "1");
		MemberVo memberVo = new MemberVo();
        try {
			//白名单用户输入任意6位数字验证码即可登录
			WhiteList whiteList = whiteListManagerDao.getWhiteList(mobile);
			Member member = memberManagerDao.findMemberByMobile(mobile);
			if(whiteList != null && member != null) {
				if(member.getMobile().equals(whiteList.getMobile())){
					member.setAppVersion(version);
					if(member.getRealname()==null || "".equals(member.getRealname())) {
						member.setRealname(member.getMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
					}
					memberVo = memberManagerDao.updateAppMember(member, request);
					object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, memberVo);
					return "success";
				}
			}
			if (StringUtil.isNotBlank(mobile) && StringUtil.isNotBlank(mobileCode)) {
				if (!mobile.matches("\\d{11}")) {
					object = new ResponseBean(Constants.FAIL_CODE_400, "手机号码不正确");
					return "success";
				}
				if (!this.memberManagerDao.isExistMobile(mobile)) {
					object = new ResponseBean(Constants.FAIL_CODE_400, "该手机号未注册，请先注册");
					return "success";
				}
				// 短信验证码校验
				if (!memberManagerDao.verifyMobileCode(business, mobile, mobileCode, session)) {
					object = new ResponseBean(Constants.FAIL_CODE_400, "验证码错误");
					return "success";
				}
				if (member != null) {
					member.setRole(member.getRole()==null || member.getRole().isEmpty()?"1":member.getRole());
					member.setExpertCheckStatus(member.getExpertCheckStatus()==null || member.getExpertCheckStatus().isEmpty()?"0":member.getExpertCheckStatus());
                    if(member.getRole().equals("2") && member.getExpertCheckStatus().equals("1")){   //判断此用户是否是专家
    					HotReplyFees fees=memberManagerDao.getReplyFees(member.getId());
    					if(fees==null){   //判断此用户在问答收费表里面(reply_fees)是否有记录,若无记录,则插入一条初始值
    						memberManagerDao.insertReplyFees(member.getId());
    					}
                    }
                    member.setAppVersion(version);
					memberVo = memberManagerDao.updateAppMember(member, request);
					object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, memberVo);
				}
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
	 * ios端点击随便看看时，游客能够进行普通会员的相关操作
	 */
	public String casualLook(){
		if(uuid != null){
			//先查看表里是否有这个uuid对应的member
			Member member = memberManagerDao.getUUID(uuid);
			MemberVo memberVo = new MemberVo();
			if (member == null) {
				Member member2 = new Member("","1");
				//注册（游客注册，没手机号码）
				member2.setTerminal(terminal);
				member2.setAppVersion(version);
				member2.setUuid(uuid);
				member2.setTourist("1");//1:游客，0：非游客
				member2.setRegistSource(RegistSourceEnum.TOURIST.getType());
				memberVo = memberManagerDao.addAppMember(member2, request);
				memberVo = memberManagerDao.updateAppMember(member2, request);
				object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, memberVo);
				return "success";
			}
			//登录
			memberVo = memberManagerDao.updateAppMember(member, request);
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS, memberVo);
		}
		return "success";
	}
	
	
	/**
	 * 保存图片
	 * @param name
	 * @param file
	 * @return
	 */
	public String savePhoto(String name, File file) {
		String inputFileExt = name.substring(name.lastIndexOf(".") + 1);
		String fileName=MD5.GetMD5Code(new DateHelper().getRandomNum()).substring(8,24)+"."+inputFileExt;
		InputStream is = null;
		try {
			is = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String key="images/"+inputFileExt+"/"+fileName;
		AliyunOOSUtil.upload(key, is);
		return key;
	}
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		this.session = request.getSession();
	}

	public HttpSession getSession() {
		return session;
	}

	public void setSession(HttpSession session) {
		this.session = session;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
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

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getBusiness() {
		return business;
	}

	public void setBusiness(String business) {
		this.business = business;
	}

	public void setMobileCode(String mobileCode) {
		this.mobileCode = mobileCode;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		LoginAction.logger = logger;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
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

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
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

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
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

	public Long getRecommender_id() {
		return recommender_id;
	}

	public void setRecommender_id(Long recommender_id) {
		this.recommender_id = recommender_id;
	}

	public int getRecommend_times() {
		return recommend_times;
	}

	public void setRecommend_times(int recommend_times) {
		this.recommend_times = recommend_times;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public MemAuth getMemAuth() {
		return memAuth;
	}

	public void setMemAuth(MemAuth memAuth) {
		this.memAuth = memAuth;
	}

	public ThridLoginUtil getThirdLogin() {
		return thirdLogin;
	}

	public void setThirdLogin(ThridLoginUtil thirdLogin) {
		this.thirdLogin = thirdLogin;
	}

	public String getRecommendMobile() {
		return recommendMobile;
	}

	public void setRecommendMobile(String recommendMobile) {
		this.recommendMobile = recommendMobile;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
}