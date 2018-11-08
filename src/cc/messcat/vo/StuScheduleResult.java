package cc.messcat.vo;

import java.util.List;

public class StuScheduleResult extends CommonResult {
	
	private List<ScheduleVo> scheduleList;

	public List<ScheduleVo> getScheduleList() {
		return scheduleList;
	}

	public void setScheduleList(List<ScheduleVo> scheduleList) {
		this.scheduleList = scheduleList;
	}

}
