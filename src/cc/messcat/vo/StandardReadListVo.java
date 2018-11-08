package cc.messcat.vo;

import java.math.BigDecimal;
import java.util.List;

public class StandardReadListVo {
	
	private String standardReadingId;  //标准解读id
	private String title;			//标题
	private String authorId;        //专家ID
	private String author;			//专家名字
	private String time;			//时间
	private String authorIntro;		//作者简介(从Member表中取数据)
	private String photo;			//图片
	private String photo2;			//图片2
	private String photo3;			//图片3
	private String photo4;			//图片4
	private String expertHeadImg;   //专家头像
	private String expertField;     //专家专业领域
	private String cover;			//封面
	private BigDecimal money;			//价格
	private BigDecimal linePrice;       //划线价
    private String type;			//解读类型（1：体系化解读 2：单一解读）
	private String intro;			//解读简介
	private String contentType;		//解读内容类型（1：图文  2：音频）
	private String content;			//解读内容（图文）
	private String voice;			//解读音频
	private String voiceDuration;   //音频时长
	private String shareURL;		//分享URL
	private List<Adjunct> subStandardReadingList;    //附件列表
	
	private int collectStatus;		//收藏状态（0：未收藏  1：已收藏）
	private int buyStatus;			//购买状态（0：未购买  1：已购买）
	private int viewStatus;  //可免费查看状态（0：不可查看  1：可查看）
	private int memberType;  //该解读免费类型（0： 无年费   1：月度会员和年度会员免费   2：只有年度会员免费）
	private int classify;     //1为标准解读,2为质量分享
	private int qualityId;	  //质量分享分类ID
	private int isAttention;      //是否关注此专家(0:否,1:是)
	
	
	public String getStandardReadingId() {
		return standardReadingId;
	}
	public void setStandardReadingId(String standardReadingId) {
		this.standardReadingId = standardReadingId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthorId() {
		return authorId;
	}
	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getAuthorIntro() {
		return authorIntro;
	}
	public void setAuthorIntro(String authorIntro) {
		this.authorIntro = authorIntro;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getPhoto2() {
		return photo2;
	}
	public void setPhoto2(String photo2) {
		this.photo2 = photo2;
	}
	public String getPhoto3() {
		return photo3;
	}
	public void setPhoto3(String photo3) {
		this.photo3 = photo3;
	}
	public String getPhoto4() {
		return photo4;
	}
	public void setPhoto4(String photo4) {
		this.photo4 = photo4;
	}
	public String getExpertHeadImg() {
		return expertHeadImg;
	}
	public void setExpertHeadImg(String expertHeadImg) {
		this.expertHeadImg = expertHeadImg;
	}
	public String getExpertField() {
		return expertField;
	}
	public void setExpertField(String expertField) {
		this.expertField = expertField;
	}
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getVoice() {
		return voice;
	}
	public void setVoice(String voice) {
		this.voice = voice;
	}
	public String getVoiceDuration() {
		return voiceDuration;
	}
	public void setVoiceDuration(String voiceDuration) {
		this.voiceDuration = voiceDuration;
	}
	public int getCollectStatus() {
		return collectStatus;
	}
	public void setCollectStatus(int collectStatus) {
		this.collectStatus = collectStatus;
	}
	public int getBuyStatus() {
		return buyStatus;
	}
	public void setBuyStatus(int buyStatus) {
		this.buyStatus = buyStatus;
	}
	public String getShareURL() {
		return shareURL;
	}
	public void setShareURL(String shareURL) {
		this.shareURL = shareURL;
	}
	public List<Adjunct> getSubStandardReadingList() {
		return subStandardReadingList;
	}
	public void setSubStandardReadingList(List<Adjunct> subStandardReadingList) {
		this.subStandardReadingList = subStandardReadingList;
	}
	public int getViewStatus() {
		return viewStatus;
	}
	public void setViewStatus(int viewStatus) {
		this.viewStatus = viewStatus;
	}
	public int getMemberType() {
		return memberType;
	}
	public void setMemberType(int memberType) {
		this.memberType = memberType;
	}
	public int getClassify() {
		return classify;
	}
	public void setClassify(int classify) {
		this.classify = classify;
	}
	public int getQualityId() {
		return qualityId;
	}
	public void setQualityId(int qualityId) {
		this.qualityId = qualityId;
	}
	public int getIsAttention() {
		return isAttention;
	}
	public void setIsAttention(int isAttention) {
		this.isAttention = isAttention;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public BigDecimal getLinePrice() {
		return linePrice;
	}

	public void setLinePrice(BigDecimal linePrice) {
		this.linePrice = linePrice;
	}
}
