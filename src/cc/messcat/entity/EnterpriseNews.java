package cc.messcat.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Messcat
 * @version 1.1
 * 
 */
public class EnterpriseNews implements Serializable {

	private static final long serialVersionUID = 948113860944504333L;
	private Long id;
	private Users users;
	private EnterpriseColumn enterpriseColumn;
	private String title;
	private String priKey;
	private String shortMeta;
	private String autor;
	private String source;
	private String contents;
	private String contents1;
	private String contents2;
	private String contents3;
	private String contents4;
	private String contents5;
	private String contents6;
	private String contents7;
	private String contents8;
	private String contents9;
	private Long clickTimes;
	private Date initTime;
	private Date endTime;
	private Date editeTime;
	private String isTop;
	private String isCommend;
	private String isPrimPhoto;
	private String isIndexPhoto;
	private Long isOnlyContent;
	private String otherURL;
	private String bors;
	private String photo;
	private String state;
	private String htmlName;
	private String isShowedOnIndex;
	private boolean isNew;
	private String fileInfo;
	private Long fileSize;
	private String shareUrl;
	private String isPara;
	private Long videoId;

	public EnterpriseNews() {
	}

	public EnterpriseNews(Long id, String title, Date initTime, String isPrimPhoto, String photo, String otherURL) {
		this.id = id;
		this.title = title;
		this.initTime = initTime;
		this.isPrimPhoto = isPrimPhoto;
		this.photo = photo;
		this.otherURL = otherURL;
	}

	public String getHtmlName() {
		return htmlName;
	}

	public void setHtmlName(String htmlName) {
		this.htmlName = htmlName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	public EnterpriseColumn getEnterpriseColumn() {
		return enterpriseColumn;
	}

	public void setEnterpriseColumn(EnterpriseColumn enterpriseColumn) {
		this.enterpriseColumn = enterpriseColumn;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPriKey() {
		return priKey;
	}

	public void setPriKey(String priKey) {
		this.priKey = priKey;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public String getContents1() {
		return contents1;
	}

	public void setContents1(String contents1) {
		this.contents1 = contents1;
	}

	public String getContents2() {
		return contents2;
	}

	public void setContents2(String contents2) {
		this.contents2 = contents2;
	}

	public Long getClickTimes() {
		return clickTimes;
	}

	public void setClickTimes(Long clickTimes) {
		this.clickTimes = clickTimes;
	}

	public Date getInitTime() {
		return initTime;
	}

	public void setInitTime(Date initTime) {
		this.initTime = initTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getEditeTime() {
		return editeTime;
	}

	public void setEditeTime(Date editeTime) {
		this.editeTime = editeTime;
	}

	public String getIsTop() {
		return isTop;
	}

	public void setIsTop(String isTop) {
		this.isTop = isTop;
	}

	public String getIsCommend() {
		return isCommend;
	}

	public void setIsCommend(String isCommend) {
		this.isCommend = isCommend;
	}

	public String getIsPrimPhoto() {
		return isPrimPhoto;
	}

	public void setIsPrimPhoto(String isPrimPhoto) {
		this.isPrimPhoto = isPrimPhoto;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getBors() {
		return bors;
	}

	public void setBors(String bors) {
		this.bors = bors;
	}

	public String getContents3() {
		return contents3;
	}

	public void setContents3(String contents3) {
		this.contents3 = contents3;
	}

	public String getContents4() {
		return contents4;
	}

	public void setContents4(String contents4) {
		this.contents4 = contents4;
	}

	public String getContents5() {
		return contents5;
	}

	public void setContents5(String contents5) {
		this.contents5 = contents5;
	}

	public String getContents6() {
		return contents6;
	}

	public void setContents6(String contents6) {
		this.contents6 = contents6;
	}

	public String getContents7() {
		return contents7;
	}

	public void setContents7(String contents7) {
		this.contents7 = contents7;
	}

	public String getContents8() {
		return contents8;
	}

	public void setContents8(String contents8) {
		this.contents8 = contents8;
	}

	public String getContents9() {
		return contents9;
	}

	public void setContents9(String contents9) {
		this.contents9 = contents9;
	}

	public String getIsIndexPhoto() {
		return isIndexPhoto;
	}

	public void setIsIndexPhoto(String isIndexPhoto) {
		this.isIndexPhoto = isIndexPhoto;
	}

	public String getShortMeta() {
		return shortMeta;
	}

	public void setShortMeta(String shortMeta) {
		this.shortMeta = shortMeta;
	}

	public Long getIsOnlyContent() {
		return isOnlyContent;
	}

	public void setIsOnlyContent(Long isOnlyContent) {
		this.isOnlyContent = isOnlyContent;
	}

	public String getOtherURL() {
		return otherURL;
	}

	public void setOtherURL(String otherURL) {
		this.otherURL = otherURL;
	}

	public String getIsShowedOnIndex() {
		return isShowedOnIndex;
	}

	public void setIsShowedOnIndex(String isShowedOnIndex) {
		this.isShowedOnIndex = isShowedOnIndex;
	}

	public boolean getIsNew() {
		return isNew;
	}

	public void setIsNew(boolean isNew) {
		this.isNew = isNew;
	}

	public String getFileInfo() {
		return fileInfo;
	}

	public void setFileInfo(String fileInfo) {
		this.fileInfo = fileInfo;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public String getShareUrl() {
		return shareUrl;
	}

	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}

	public String getIsPara() {
		return isPara;
	}

	public void setIsPara(String isPara) {
		this.isPara = isPara;
	}

	public Long getVideoId() {
		return videoId;
	}

	public void setVideoId(Long videoId) {
		this.videoId = videoId;
	}
	
	
}