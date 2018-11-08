package cc.messcat.vo;

public class EnterpriseNewsVo {

	private Long id;
	
	private String title;
	private String priKey;
	private String shortMeta;
	private String contents;
	private String photo;
	private String url;
	private String editeTime;
	private Integer status;//收藏状态：0=无；1=有
	private String isPara;
	private Long videoId;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getShortMeta() {
		return shortMeta;
	}

	public void setShortMeta(String shortMeta) {
		this.shortMeta = shortMeta;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getEditeTime() {
		return editeTime;
	}

	public void setEditeTime(String editeTime) {
		this.editeTime = editeTime;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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
