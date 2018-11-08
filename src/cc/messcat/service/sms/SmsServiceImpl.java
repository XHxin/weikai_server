package cc.messcat.service.sms;

import cc.messcat.bases.BaseManagerDaoImpl;
import cc.messcat.entity.SmsReply;
import cc.messcat.entity.SmsReport;

@SuppressWarnings("serial")
public class SmsServiceImpl extends BaseManagerDaoImpl implements SmsService {

	@Override
	public void saveSmsReport(SmsReport report) {
		smsDao.saveSmsReport(report);
	}

	@Override
	public void saveSmsReply(SmsReply reply) {
		smsDao.saveSmsReply(reply);
	}

	@Override
	public int isExistSmsReport(String smsid) {
		return smsDao.isExistSmsReport(smsid);
	}

	@Override
	public int isExistSmsReply(String smsid) {
		return smsDao.isExistSmsReply(smsid);
	}

}
