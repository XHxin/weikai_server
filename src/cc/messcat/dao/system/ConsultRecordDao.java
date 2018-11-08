package cc.messcat.dao.system;

import java.util.Date;

import cc.messcat.bases.BaseDao;
import cc.messcat.entity.ConsultRecord;

/**
 * @author nelson
 *咨询记录接口
 */
public interface ConsultRecordDao extends BaseDao{

	//新增咨询记录
	public void addConsultRecord(ConsultRecord consultRecord);
	
	//查询是否表里7天内是否有它的咨询记录,切类型要当前请求的一致
	public ConsultRecord existRecord(Long memberId,Date betweenTime,String serverType);
}
