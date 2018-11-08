package cc.messcat.service.system;

import java.util.Date;

import cc.messcat.bases.BaseManagerDaoImpl;
import cc.messcat.entity.ConsultRecord;

@SuppressWarnings("serial")
public class ConsultRecordManagerDaoImpl extends BaseManagerDaoImpl implements ConsultRecordManagerDao{

	@Override
	public void addConsultRecord(ConsultRecord consultRecord) {
		consultRecordDao.save(consultRecord);
		
	}

	@Override
	public ConsultRecord existRecord(Long memberId, Date betweenTime, String serverType) {
		return consultRecordDao.existRecord(memberId, betweenTime, serverType);
	}

}
