package cc.messcat.service.system;

import cc.messcat.entity.DivideScaleExpert;

public interface ExpertDivideIntoManagerDao {
	public DivideScaleExpert getDivideIntoByMemberId(Long memberId);
}
