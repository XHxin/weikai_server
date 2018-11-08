package cc.messcat.vo;

import java.util.List;

/**
 * 教学明细
 * @author StevenWang
 *
 */
public class TeachDetailVo {
	
	private String stuName;
	private String mainPic;
	private String bookDate;
	private String bookTime;
	private String teachTime;
	private List<TrainActionVo> teachActionList;
	
	public String getStuName() {
		return stuName;
	}
	public void setStuName(String stuName) {
		this.stuName = stuName;
	}
	public String getMainPic() {
		return mainPic;
	}
	public void setMainPic(String mainPic) {
		this.mainPic = mainPic;
	}
	public String getBookDate() {
		return bookDate;
	}
	public void setBookDate(String bookDate) {
		this.bookDate = bookDate;
	}
	public String getBookTime() {
		return bookTime;
	}
	public void setBookTime(String bookTime) {
		this.bookTime = bookTime;
	}
	public String getTeachTime() {
		return teachTime;
	}
	public void setTeachTime(String teachTime) {
		this.teachTime = teachTime;
	}
	public List<TrainActionVo> getTeachActionList() {
		return teachActionList;
	}
	public void setTeachActionList(List<TrainActionVo> teachActionList) {
		this.teachActionList = teachActionList;
	}

}
