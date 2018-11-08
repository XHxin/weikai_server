package cc.messcat.vo;

import java.util.List;

/**
 * 教学明细返回类型
 * @author StevenWang
 *
 */
public class TeachDetailResult extends CommonResult {
	
	private List<TeachDetailVo> teachDetailList;

	public List<TeachDetailVo> getTeachDetailList() {
		return teachDetailList;
	}

	public void setTeachDetailList(List<TeachDetailVo> teachDetailList) {
		this.teachDetailList = teachDetailList;
	}

}
