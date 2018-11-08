package cc.messcat.vo;

public class NewsVo {
	
	private String newsId;
	private String title;
	private String photo;
	private String clickTimes;
	private String isNew;//是否最新
	private String pbTime;//新闻时间
	private String intro;//摘要
	private String columnName;//栏目名称
	private String isShare;//是否允许分享
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getClickTimes() {
		return clickTimes;
	}
	public void setClickTimes(String clickTimes) {
		this.clickTimes = clickTimes;
	}
	public String getNewsId() {
		return newsId;
	}
	public void setNewsId(String newsId) {
		this.newsId = newsId;
	}
	public String getIsNew() {
		return isNew;
	}
	public void setIsNew(String isNew) {
		this.isNew = isNew;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public String getPbTime() {
		return pbTime;
	}
	public void setPbTime(String pbTime) {
		this.pbTime = pbTime;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getIsShare() {
		return isShare;
	}
	public void setIsShare(String isShare) {
		this.isShare = isShare;
	}

}
