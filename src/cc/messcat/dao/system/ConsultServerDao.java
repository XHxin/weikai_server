package cc.messcat.dao.system;

import java.util.List;

import cc.messcat.entity.ConsultService;

/**
 * @author nelson
 *	咨询服务dao
 */
public interface ConsultServerDao {

	//跟据环信账号查找客服
	public ConsultService getConsultServer(String consultedEas);
	
	//查询该类型在线且工单数量最少的客服
	public ConsultService getLeisureServer(String serverType);

	public void updateConsultServer(ConsultService consultService);

	public List<ConsultService> getConsultServerByModleType();

    List<ConsultService> getConsultServerList();
}
