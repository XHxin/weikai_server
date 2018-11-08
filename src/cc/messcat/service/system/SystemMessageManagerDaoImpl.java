package cc.messcat.service.system;

import java.util.List;

import com.mipush.MiPushHelper;

import cc.messcat.bases.BaseManagerDaoImpl;
import cc.messcat.entity.Member;
import cc.messcat.entity.SystemMessage;
import cc.modules.commons.Pager;

public class SystemMessageManagerDaoImpl extends BaseManagerDaoImpl implements SystemMessageManagerDao {

	private static final long serialVersionUID = 1L;

	public SystemMessageManagerDaoImpl() {
	}

	public void addSystemMessage(SystemMessage systemMessage) {
		this.systemMessageDao.save(systemMessage);
	}

	public void modifySystemMessage(SystemMessage systemMessage) {
		this.systemMessageDao.update(systemMessage);
	}

	public void removeSystemMessage(SystemMessage systemMessage) {
		this.systemMessageDao.delete(systemMessage);
	}

	public void removeSystemMessage(Long id) {
		this.systemMessageDao.delete(id);
	}

	public SystemMessage retrieveSystemMessage(Long id) {
		return (SystemMessage) this.systemMessageDao.get(id);
	}

	public List retrieveAllSystemMessages() {
		return this.systemMessageDao.findAll();
	}

	public Pager retrieveSystemMessagesPager(int pageSize, int pageNo) {
		return this.systemMessageDao.getPager(pageSize, pageNo);
	}

	public Pager findSystemMessages(int pageSize, int pageNo, String statu) {
		Pager pager = systemMessageDao.getObjectListByClass(pageSize, pageNo, SystemMessage.class, statu);
		return pager;
	}

	public List<SystemMessage> getByMemberId(int pageSize, int pageNo, Long memberId) {
		StringBuffer sb = new StringBuffer("FROM SystemMessage WHERE status = 1 ");
		sb.append(" AND (pushType = 0 ");
		sb.append(" OR (pushType = 1 AND memberIds like '%").append(memberId).append("%')) ");
		sb.append(" ORDER BY editTime DESC");
		
		List<SystemMessage> list = systemMessageDao.findByhql(sb.toString() , pageSize, pageNo);
		
		return list;
	}

	@Override
	public void sendSysNotify(String content, Member member, Long toUser) {
		Member bebMember = systemMessageDao.getMemberById(toUser);
		String beName = bebMember.getRealname() == null ? "" : bebMember.getRealname();
		String ext = "weikai://cert-map?target=chat&sender=" + member.getId() + "&name=" + beName;
		MiPushHelper.sendAndroidUserAccount(bebMember.getRealname(), content, ext, bebMember.getMobile());
		// String description=member.getRealname()+"\n"+content;
		// MiPushHelper.sendIOSSysMsg(description, member.getMobile());
	}

	@Override
	public void updateSystemMessage(SystemMessage message) {
		systemMessageDao.update(message);
	}

}