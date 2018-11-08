package cc.messcat.service.system;

import java.util.List;

import cc.messcat.bases.BaseManagerDaoImpl;
import cc.messcat.entity.ConsultService;

@SuppressWarnings("serial")
public class ConsultServerManagerDaoImpl extends BaseManagerDaoImpl implements ConsultServerManagerDao{

	@Override
	public ConsultService getConsultServer(String consultedEas) {
		return consultServerDao.getConsultServer(consultedEas);
	}

	@Override
	public ConsultService getLeisureServer(String serverType) {
		return consultServerDao.getLeisureServer(serverType);
	}

	@Override
	public void updateConsultServer(ConsultService consultService) {
		consultServerDao.updateConsultServer(consultService);
	}

	@Override
	public List<ConsultService> getConsultServerByModleType() {
		return consultServerDao.getConsultServerByModleType();
	}

	@Override
	public List<ConsultService> getConsultServerList() {
		return consultServerDao.getConsultServerList();
	}
}
