package cc.messcat.service.system;

import java.util.List;

import cc.messcat.entity.ConsultService;

public interface ConsultServerManagerDao {

	//跟据环信账号查找客服
	public ConsultService getConsultServer(String consultedEas);

	public ConsultService getLeisureServer(String serverType);

	//更新咨询客服信息（接待数）
	public void updateConsultServer(ConsultService consultService);

	//获取联系我们里面的信息
	public List<ConsultService> getConsultServerByModleType();

	List<ConsultService> getConsultServerList();
}
