package cc.messcat.service.sms;

import cc.messcat.entity.SmsReply;
import cc.messcat.entity.SmsReport;

public interface SmsService {

	void saveSmsReport(SmsReport report);

	void saveSmsReply(SmsReply reply);

	int isExistSmsReport(String smsid);

	int isExistSmsReply(String smsid);

}
