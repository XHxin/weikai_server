package cc.messcat.vo;

import java.util.List;

public class CoachStuNotesResult extends CommonResult {
	
	private List<StuNoteVo> stuNotelList;

	public List<StuNoteVo> getStuNotelList() {
		return stuNotelList;
	}

	public void setStuNotelList(List<StuNoteVo> stuNotelList) {
		this.stuNotelList = stuNotelList;
	}

}
