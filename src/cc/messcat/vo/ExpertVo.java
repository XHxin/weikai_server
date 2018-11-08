package cc.messcat.vo;
/**
 * 
 * @author wenyu
 *
 */
public class ExpertVo {
	
	private Long expertId;//专家的id
	//private Double money;//公开提问专家的费用
	private String expertName;//专家的姓名
	private String photo;//专家的头像
	//private int readSum;//问题阅读的总数
	private int attentionSum;//问题关注的总数
	private String Intro;//专家的简介
	private int articleSum;//专家发表文章的数量
	private int answerSum;//专家回答的次数
	public Long getExpertId() {
		return expertId;
	}
	public void setExpertId(Long expertId) {
		this.expertId = expertId;
	}
//	public Double getMoney() {
//		return money;
//	}
//	public void setMoney(Double money) {
//		this.money = money;
//	}
	public String getExpertName() {
		return expertName;
	}
	public void setExpertName(String expertName) {
		this.expertName = expertName;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
//	public int getReadSum() {
//		return readSum;
//	}
//	public void setReadSum(int readSum) {
//		this.readSum = readSum;
//	}
	public int getAttentionSum() {
		return attentionSum;
	}
	public void setAttentionSum(int attentionSum) {
		this.attentionSum = attentionSum;
	}
	public String getIntro() {
		return Intro;
	}
	public void setIntro(String intro) {
		Intro = intro;
	}
	public int getArticleSum() {
		return articleSum;
	}
	public void setArticleSum(int articleSum) {
		this.articleSum = articleSum;
	}
	public int getAnswerSum() {
		return answerSum;
	}
	public void setAnswerSum(int answerSum) {
		this.answerSum = answerSum;
	}
	
}
