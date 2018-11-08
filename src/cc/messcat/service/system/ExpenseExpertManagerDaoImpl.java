package cc.messcat.service.system;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cc.messcat.bases.BaseManagerDaoImpl;
import cc.messcat.entity.ExpenseExpert;
import cc.messcat.vo.ExpertIEListVo;
import cc.messcat.vo.ExpertIEVo;
import cc.modules.commons.Pager;
import cc.modules.util.ObjValid;

public class ExpenseExpertManagerDaoImpl extends BaseManagerDaoImpl implements ExpenseExpertManagerDao {

	private static final long serialVersionUID = 1L;


	public ExpenseExpertManagerDaoImpl() {
	}

	public void addExpenseExpert(ExpenseExpert expertIE) {
		this.expertIEDao.save(expertIE);
	}
	
	public void modifyExpertIE(ExpenseExpert expertIE) {
		this.expertIEDao.update(expertIE);
	}
	
	public void removeExpertIE(ExpenseExpert expertIE) {
		this.expertIEDao.delete(expertIE);
	}

	public void removeExpertIE(Long id) {
		this.expertIEDao.delete(id);
	}
	
	public ExpenseExpert retrieveExpertIE(Long id) {
		return (ExpenseExpert) this.expertIEDao.get(id);
	}

	@SuppressWarnings("unchecked")
	public List retrieveAllExpertIEs() {
		return this.expertIEDao.findAll();
	}
	
	public Pager retrieveExpertIEsPager(int pageSize, int pageNo) {
		return this.expertIEDao.getPager(pageSize, pageNo);
	}
	
	public Pager findExpertIEs(int pageSize, int pageNo, String statu) {
		Pager pager = expertIEDao.getObjectListByClass(pageSize, pageNo, ExpenseExpert.class, statu);
		return pager;
	}
	
	/*
	 * 根据memberId查询
	 */
	public ExpertIEListVo getExpertIEByCon(Long memberId, int pageNo, int pageSize){
		ExpertIEListVo expertIEListVo = new ExpertIEListVo();
		String hql="FROM ExpenseExpert WHERE expertId="+memberId+" ORDER BY addTime DESC";
		List<ExpenseExpert> list = expertIEDao.findByhql(hql);
		
		int rowCount = list.size();
		int startIndex = pageSize * (pageNo - 1);
		if (startIndex > rowCount) {
			list = new ArrayList<>();
		} else {
			list = list.subList(startIndex, (pageSize + startIndex) <= rowCount ? (pageSize + startIndex) : rowCount);
		}
		if(ObjValid.isValid(list)){
			expertIEListVo.setPageNo(pageNo);
			expertIEListVo.setPageSize(pageSize);
			expertIEListVo.setRowCount(rowCount);
			List<ExpertIEVo> expertIEVos = new ArrayList<>();
			for(ExpenseExpert expertIE: list){
				ExpertIEVo expertIEVo = new ExpertIEVo();
				expertIEVo.setTitle(expertIE.getContent());
				String operate = "+";
				if(expertIE.getOperate().equals("1")){
					operate = "-";
				}
				expertIEVo.setMoney(operate+expertIE.getExpenseExpertMoney());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				String time = sdf.format(expertIE.getAddTime());
				expertIEVo.setTime(time);
				expertIEVos.add(expertIEVo);
			}
			expertIEListVo.setExpertIEVos(expertIEVos);
		}else{
			expertIEListVo.setExpertIEVos(new ArrayList<ExpertIEVo>());
		}
		return expertIEListVo;
	}


}