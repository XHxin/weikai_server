package cc.messcat.vo;

import java.math.BigDecimal;

public class ExpenseTotalVo {
	private Long id;
	private int region;//地区id
	private String title;//标题
	private BigDecimal money;//金额
	private int type;//0:综合 1:准入报告 2:标准解读（质量分享） 4:付费咨询 5:新闻详情 6:电商准入报告 8:视频或直播
	private String askName;//被提问人
	private int askType;//问题类型 0：提问 1:围观
	private String cover;//封面
	private int duration;//时长
	private int articleType;//文章类型 0：标准解读 1：政策分析 2：质量漫谈 3：电商品控 4：能力验证 5：实验室运营 6：整改专区
	private String articleName;
	private String askDate;//提问时间
	private Long fatherId;//视频分类(1:系列,2:章节,3:章节的子视频,4:单一视频)
	private int askStatus;//问题是否已被回答0-否  1-是
	private String detailCover;
	private int videoType;//0-直播 1-录播 2-点播
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public int getRegion() {
		return region;
	}
	public void setRegion(int region) {
		this.region = region;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getAskName() {
		return askName;
	}
	public void setAskName(String askName) {
		this.askName = askName;
	}
	public int getAskType() {
		return askType;
	}
	public void setAskType(int askType) {
		this.askType = askType;
	}
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}

	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public int getArticleType() {
		return articleType;
	}
	public void setArticleType(int articleType) {
		this.articleType = articleType;
	}
	public String getArticleName() {
		return articleName;
	}
	public void setArticleName(String articleName) {
		this.articleName = articleName;
	}

	public Long getFatherId() {
		return fatherId;
	}
	public void setFatherId(Long fatherId) {
		this.fatherId = fatherId;
	}
	public String getAskDate() {
		return askDate;
	}
	public void setAskDate(String askDate) {
		this.askDate = askDate;
	}
	public int getAskStatus() {
		return askStatus;
	}
	public void setAskStatus(int askStatus) {
		this.askStatus = askStatus;
	}
	public String getDetailCover() {
		return detailCover;
	}
	public void setDetailCover(String detailCover) {
		this.detailCover = detailCover;
	}
	public int getVideoType() {
		return videoType;
	}
	public void setVideoType(int videoType) {
		this.videoType = videoType;
	}


}
