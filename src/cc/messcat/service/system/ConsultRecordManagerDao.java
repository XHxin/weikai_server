package cc.messcat.service.system;

import java.util.Date;

import cc.messcat.bases.BaseManagerDao;
import cc.messcat.entity.ConsultRecord;

public interface ConsultRecordManagerDao {

	//新增咨询记录
	public void addConsultRecord(ConsultRecord consultRecord);
		
	//查询是否表里7天内是否有它的咨询记录，且请求要与当前请求的一致
	public ConsultRecord existRecord(Long memberId,Date betweenTime,String serverType);
}
