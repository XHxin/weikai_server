package cc.messcat.dao.payconsult;

import java.util.List;

import cc.messcat.entity.ExpertClassify;
import cc.messcat.entity.Member;
import cc.modules.commons.Pager;

public interface ExpertClassifyDao {

	List<ExpertClassify> getList();

	Pager getAllList(int pageNo, int pageSize);

	List<ExpertClassify> getAllClassify();

	Member getMember(Long expertId);

	ExpertClassify getExpertClassify(String strId);

	void updateMembers(Member member);

}
