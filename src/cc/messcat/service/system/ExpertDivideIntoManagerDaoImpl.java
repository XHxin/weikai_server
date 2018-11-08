package cc.messcat.service.system;

import cc.messcat.bases.BaseManagerDaoImpl;
import cc.messcat.entity.DivideScaleExpert;

@SuppressWarnings("serial")
public class ExpertDivideIntoManagerDaoImpl extends BaseManagerDaoImpl implements ExpertDivideIntoManagerDao {

	@Override
	public DivideScaleExpert getDivideIntoByMemberId(Long memberId) {
		return expertDivideIntoDao.getDivideIntoByMemberId(memberId);
	}

}
