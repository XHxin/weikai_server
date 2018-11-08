package cc.messcat.vo;

import java.util.List;

public class FirstPagerResult {

	private List<LiveVideoListVo> todayLive;   //今日直播
	private List<LiveVideoSubjectListVo> subject;  //专题
	private List<LiveVideoListVo> excellentCourse; //精品课程
	private List<StandardReadListVo3> columnSubscribe;   //专栏订阅
	private List<StandReadingVo> excellentArticle;  //精品文章
	
	public List<LiveVideoListVo> getTodayLive() {
		return todayLive;
	}
	public void setTodayLive(List<LiveVideoListVo> todayLive) {
		this.todayLive = todayLive;
	}
	public List<LiveVideoSubjectListVo> getSubject() {
		return subject;
	}
	public void setSubject(List<LiveVideoSubjectListVo> subject) {
		this.subject = subject;
	}
	public List<LiveVideoListVo> getExcellentCourse() {
		return excellentCourse;
	}
	public void setExcellentCourse(List<LiveVideoListVo> excellentCourse) {
		this.excellentCourse = excellentCourse;
	}
	public List<StandardReadListVo3> getColumnSubscribe() {
		return columnSubscribe;
	}
	public void setColumnSubscribe(List<StandardReadListVo3> columnSubscribe) {
		this.columnSubscribe = columnSubscribe;
	}
	public List<StandReadingVo> getExcellentArticle() {
		return excellentArticle;
	}
	public void setExcellentArticle(List<StandReadingVo> excellentArticle) {
		this.excellentArticle = excellentArticle;
	}
}
