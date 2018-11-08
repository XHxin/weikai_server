package cc.messcat.vo;

import java.math.BigDecimal;
import java.util.List;

public class StandardReadListVo2 {
	
	private String standardReadingId;  //标准解读id
	private String title;			//标题
	private String author;			//作者（专家ID）
	private String time;			    //时间
	private String authorIntro;		//作者简介(从Member表中取数据)
	private String photo;			//图片
	private String photo2;			//图片2
	private String photo3;			//图片3
	private String photo4;			//图片4
	private String cover;			//封面
	private BigDecimal money;			//价格
    private String type;			//解读类型（1：体系化解读 2：单一解读）
	private String intro;			//解读简介
	private String contentType;		//解读内容类型（1：图文  2：音频）
	private String content;			//解读内容（图文）
	private String voice;			//解读音频
	private String voiceDuration;   //音频时长
	/**
	 * 个人中心处没有这两个状态,   因为IOS端没有做判断, 造成自己的文章不能看, 故加上购买状态,并默认为已购买
	 */
	private String collectStatus;	//收藏状态（0：未收藏  1：已收藏）
	private String buyStatus;		//购买状态（0：未购买  1：已购买）
	
	private List<Adjunct> subStandardReadingList;    //附件列表
	
	
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
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
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
	public List<Adjunct> getSubStandardReadingList() {
		return subStandardReadingList;
	}
	public void setSubStandardReadingList(List<Adjunct> subStandardReadingList) {
		this.subStandardReadingList = subStandardReadingList;
	}
	public String getBuyStatus() {
		return buyStatus;
	}
	public void setBuyStatus(String buyStatus) {
		this.buyStatus = buyStatus;
	}
	public String getCollectStatus() {
		return collectStatus;
	}
	public void setCollectStatus(String collectStatus) {
		this.collectStatus = collectStatus;
	}
}
