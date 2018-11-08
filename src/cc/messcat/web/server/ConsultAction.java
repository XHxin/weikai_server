package cc.messcat.web.server;

import java.util.Date;
import java.util.List;
import cc.messcat.bases.BaseAction;
import cc.messcat.entity.ConsultRecord;
import cc.messcat.entity.ConsultService;
import cc.messcat.vo.ResponseBean;
import cc.modules.constants.Constants;
@SuppressWarnings("serial")
public class ConsultAction extends BaseAction{

	private Object object;
	private String serverType;
	private Long memberId;
	private String serverEas;

	//获取环信账号
	public String getEasemob(){
		/*//"联系我们"模块穿过来的服务类型以逗号分隔
		String[] strs=serverType.split(",");
		for(int i=0,len=strs.length;i<len;i++){
		  System.out.println(strs[i].toString());
		}*/
		Date betweenTime = new Date(new Date().getTime() - 7*24*60*60*1000);
		//之前咨询过的客服
		ConsultService usedConsultService = null;
		//新分配的客服
		ConsultService newConsultService = null;
		ConsultRecord consulltRecord = consultRecordManagerDao.existRecord(memberId, betweenTime, serverType);
		if(consulltRecord!=null){
			//获取到七天之内咨询过的客服环信账号
			String consultedEas = consulltRecord.getServerEas();
			usedConsultService = consultServerManagerDao.getConsultServer(consultedEas);
			//判断该客服在不在线	0-不在线
			if(usedConsultService!=null){
				if(usedConsultService.getIsOnline().equals("0")){
					//分配其他该类型的客服
					newConsultService = consultServerManagerDao.getLeisureServer(serverType);
					if(newConsultService!=null){
						object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, newConsultService);
					}else{
						object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_NULL);
					}
				}else {
					object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, usedConsultService);
				}
			}else{
				object = new ResponseBean(Constants.FAIL_CODE_400, Constants.MSG_DATA_EXCEPT);
 			}
		}else {
			//7天内没咨询过的客服
			newConsultService = consultServerManagerDao.getLeisureServer(serverType);
			if(newConsultService!=null){
				object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, newConsultService);
			}else {
				object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_NULL);
			}
		}
		return "success";
	}
	
	public String getContactUs(){
		List<ConsultService> consultServices = consultServerManagerDao.getConsultServerByModleType();
		if(consultServices!=null){
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, consultServices);
		}else {
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_NULL);
		}
		return "success";
	}
	
	//客户退出会话时调用的方法
	public String finishConsult(){	//用户memberid，客服的环信账号
		//当客户退出会话时，客户的receptionNum减1
		ConsultService consultService = consultServerManagerDao.getConsultServer(serverEas);
		if(consultService!=null){
			int receptionNum = consultService.getReceptionNum();
			if(receptionNum>0){
				receptionNum--;
				consultService.setReceptionNum(receptionNum);
				consultServerManagerDao.updateConsultServer(consultService);
				object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_SUCCESS);
			}else {
				object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_DATA_EXCEPT);
			}
		}else {
			object = new ResponseBean(Constants.SUCCESS_CODE_200,Constants.MSG_FIND_NULL);
		}
		return "success";
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public String getServerType() {
		return serverType;
	}

	public void setServerType(String serverType) {
		this.serverType = serverType;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public String getServerEas() {
		return serverEas;
	}

	public void setServerEas(String serverEas) {
		this.serverEas = serverEas;
	}

}
