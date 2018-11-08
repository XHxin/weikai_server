package cc.messcat.service.system;

import java.util.List;

import cc.messcat.entity.Member;
import cc.messcat.entity.SystemMessage;
import cc.modules.commons.Pager;

public interface SystemMessageManagerDao {

	public abstract void addSystemMessage(SystemMessage systemMessage);
	
	public abstract void modifySystemMessage(SystemMessage systemMessage);
	
	public abstract void removeSystemMessage(SystemMessage systemMessage);
	
	public abstract void removeSystemMessage(Long id);
	
	public abstract SystemMessage retrieveSystemMessage(Long id);
	
	public abstract List retrieveAllSystemMessages();
	
	public abstract Pager retrieveSystemMessagesPager(int pageSize, int pageNo);
	
	public abstract Pager findSystemMessages(int i, int j, String s);
	
	public List getByMemberId(int pageSize, int pageNo, Long memberId);

	public abstract void sendSysNotify(String content, Member member, Long toUser);

	public abstract void updateSystemMessage(SystemMessage message);
	
}