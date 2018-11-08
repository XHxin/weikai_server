package cc.messcat.dao.sms;

import java.util.List;

import org.hibernate.Query;

import cc.messcat.bases.BaseDaoImpl;
import cc.messcat.entity.SmsReply;
import cc.messcat.entity.SmsReport;

public class SmsDaoImpl extends BaseDaoImpl implements SmsDao {

	@Override
	public void saveSmsReport(SmsReport report) {
        getSession().save(report);	
	}

	@Override
	public void saveSmsReply(SmsReply reply) {
		getSession().save(reply);
	}

	@SuppressWarnings("unchecked")
	@Override
	public int isExistSmsReport(String smsid) {
		Query query=getSession().createQuery("FROM SmsReport WHERE smsid like '"+smsid+"'");
		List<SmsReport> list=query.list();
		if(list!=null && list.size()>0) {
			return 1;
		}
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int isExistSmsReply(String smsid) {
		Query query=getSession().createQuery("FROM SmsReply WHERE smsid like '"+smsid+"'");
		List<SmsReply> list=query.list();
		if(list!=null && list.size()>0) {
			return 1;
		}
		return 0;
	}

}
