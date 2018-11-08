package cc.messcat.vo;

public class LiveVideoSubjectListVo {
	
	private Integer subjectId;
	private String subjectName;  //专题名称
	private String introduct;    //简介
	private String subjectCover; //专题封面
	
	public Integer getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(Integer id) {
		this.subjectId = id;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public String getIntroduct() {
		return introduct;
	}
	public void setIntroduct(String introduct) {
		this.introduct = introduct;
	}
	public String getSubjectCover() {
		return subjectCover;
	}
	public void setSubjectCover(String subjectCover) {
		this.subjectCover = subjectCover;
	}

}
