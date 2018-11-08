package cc.messcat.vo;


import java.math.BigDecimal;

/**
 * @author Nelson
 *
 */

/*
 * 
 * 收藏实体
 */
public class CollectList {
	
	private Long collectId;
	private String type;//收藏类型(1:准入报告  2：标准解读  3：质量分享  4：付费咨询  5：新闻  6)
	
	private Long qualityId;  	    //质量分享分类ID(专供质量分享用)
	private Long replyId;           //回答表Id(用于跳转到热门回答的详情)
	private String title;           //标题
	private Long duration;			//时长
	private Long viewers;			//直播观看人数
	private String videoType;		//视频类型
	
	private Long regionId;	//地区id（用于准入报告）
	private Long productId;	//产品id（用于转入报告）
	private String url; 	//报告、新闻详情页路径
	private String collectTime;	//收藏时间
	private String photo;	//图片
	private String author;	//标准解读作者
	private BigDecimal money;	//购买金额
	private String intro;	//新闻简介
	private Long ebusinessId;	//电商产品id
	private String buyStatus;	//转入报告购买状态
	private String collectStatus;	//收藏状态
	
	private String shareURL;     //分享URL
	private String shortMeta;    //新闻详情专用
	private Long liveVideoId;	//关联ID
	private Long fatherId;	//父级ID
	
    /**
     * 因为质量分享列表是根据qualityID区分的 ,所以就重新写了一个VoList
     */
	private QualityShareListVo standardReadListVo;
	
	
	
	public Long getCollectId() {
		return collectId;
	}
	public void setCollectId(Long collectId) {
		this.collectId = collectId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCollectTime() {
		return collectTime;
	}
	public void setCollectTime(String collectTime) {
		this.collectTime = collectTime;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	//	public String getRealname() {
//		return realname;
//	}
//	public void setRealname(String realname) {
//		this.realname = realname;
//	}
//	public String getAnswerType() {
//		return answerType;
//	}
//	public void setAnswerType(String answerType) {
//		this.answerType = answerType;
//	}
//	public String getAnswer() {
//		return answer;
//	}
//	public void setAnswer(String answer) {
//		this.answer = answer;
//	}
//	public String getVoice() {
//		return voice;
//	}
//	public void setVoice(String voice) {
//		this.voice = voice;
//	}
//	public int getViewSum() {
//		return viewSum;
//	}
//	public void setViewSum(int viewSum) {
//		this.viewSum = viewSum;
//	}
//	public int getLikeSum() {
//		return likeSum;
//	}
//	public void setLikeSum(int likeSum) {
//		this.likeSum = likeSum;
//	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
//	public String getAddTime() {
//		return addTime;
//	}
//	public void setAddTime(String addTime) {
//		this.addTime = addTime;
//	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Long getEbusinessId() {
		return ebusinessId;
	}
	public void setEbusinessId(Long ebusinessId) {
		this.ebusinessId = ebusinessId;
	}
	public QualityShareListVo getStandardReadListVo() {
		return standardReadListVo;
	}
	public void setStandardReadListVo(QualityShareListVo standardReadListVo) {
		this.standardReadListVo = standardReadListVo;
	}
	public String getBuyStatus() {
		return buyStatus;
	}
	public void setBuyStatus(String buyStatus) {
		this.buyStatus = buyStatus;
	}
	public Long getRegionId() {
		return regionId;
	}
	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getCollectStatus() {
		return collectStatus;
	}
	public void setCollectStatus(String collectStatus) {
		this.collectStatus = collectStatus;
	}
	public Long getQualityId() {
		return qualityId;
	}
	public void setQualityId(Long qualityId) {
		this.qualityId = qualityId;
	}
	public Long getReplyId() {
		return replyId;
	}
	public void setReplyId(Long replyId) {
		this.replyId = replyId;
	}
	public String getShareURL() {
		return shareURL;
	}
	public void setShareURL(String shareURL) {
		this.shareURL = shareURL;
	}
	public String getShortMeta() {
		return shortMeta;
	}
	public void setShortMeta(String shortMeta) {
		this.shortMeta = shortMeta;
	}
	public Long getDuration() {
		return duration;
	}
	public void setDuration(Long duration) {
		this.duration = duration;
	}
	public Long getViewers() {
		return viewers;
	}
	public void setViewers(Long viewers) {
		this.viewers = viewers;
	}
	
	public Long getLiveVideoId() {
		return liveVideoId;
	}
	public void setLiveVideoId(Long liveVideoId) {
		this.liveVideoId = liveVideoId;
	}
	public Long getFatherId() {
		return fatherId;
	}
	public void setFatherId(Long fatherId) {
		this.fatherId = fatherId;
	}
	public String getVideoType() {
		return videoType;
	}
	public void setVideoType(String videoType) {
		this.videoType = videoType;
	}
	
}
