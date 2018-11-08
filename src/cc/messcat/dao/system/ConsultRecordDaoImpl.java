package cc.messcat.dao.system;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import cc.messcat.bases.BaseDaoImpl;
import cc.messcat.entity.ConsultRecord;

public class ConsultRecordDaoImpl extends BaseDaoImpl implements ConsultRecordDao{

	@Override
	public void addConsultRecord(ConsultRecord consultRecord) {
		getHibernateTemplate().save(consultRecord);
	}

	@Override
	public ConsultRecord existRecord(Long memberId, Date betweenTime, String serverType) {
		Date date=null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String formatDate = sdf.format(betweenTime);
		try {
			date = sdf.parse(formatDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Session session = this.getSession();
		String hql = "from ConsultRecord c where c.memberId=? and endTime=(select max(c.endTime) from ConsultRecord) and c.serverType=? and c.endTime>?";
		Query query = session.createQuery(hql);
		query.setParameter(0, memberId);
		query.setParameter(1, serverType);
		query.setParameter(2, date);
		List<ConsultRecord> consulltRecords = query.list();
		if (!consulltRecords.isEmpty() && consulltRecords.size()>0) {
			return consulltRecords.get(0);
		}
		return null;
	}

}
