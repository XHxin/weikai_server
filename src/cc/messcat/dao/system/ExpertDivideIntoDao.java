package cc.messcat.dao.system;

import cc.messcat.entity.DivideScaleExpert;

public interface ExpertDivideIntoDao {

	/**
	 * 根据memberId查找该专家的分成比例
	 * @param memberId
	 * @return
	 */
	public DivideScaleExpert getDivideIntoByMemberId(Long memberId);
}
