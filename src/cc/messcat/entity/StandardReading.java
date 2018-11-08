package cc.messcat.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="standard_reading")
public class StandardReading implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String title;			//标题
	
	@ManyToOne(cascade= {CascadeType.ALL},fetch=FetchType.EAGER)
	@JoinColumn(name="author")
	private Member author;			//作者（专家ID）
	
	@Column(name="author_name")
	private String authorName;		//作者名字
	private String photo;			//图片
	private String photo2;			//图片2
	private String photo3;			//图片3
	private String photo4;			//图片4
	private String cover;			//封面
	private String intro;			//简介
	
	@Column(name="column_intro")
	private String columnIntro;     //专栏订阅列表简介
	private BigDecimal money;			//价格
	
	@Column(name="line_price")
	private BigDecimal linePrice;       //画线价
	private String code;			//标准号
	private String type;			//解读类型（0：单一解读    1：体系化解读  )
	
	@Column(name="fatherID")
	private Long fatherId;       //解读ID(专用于体系化解读)
	
	@Column(name="content_type")
	private String contentType;		    //内容类型（1：图文  2：音频）
	private String content;				//内容（图文）
	private String voice;				//音频文件
	
	@Column(name="voice_duration")
	private String voiceDuration;		//音频时长
	
	@Column(name="file_name")
	private String fileName;			//文档名称
	private String file;				//文档
	private String documentId;			//上传百度云返回的documentId
	private String classify;			//分类（1：标准解读  2：质量分享）
	
	@Column(name="qualityID")
	private Long qualityId;				//质量分享分类ID
	
	@Column(name="is_recommend")
	private String isRecommend;			//是否推荐热门（0：否  1：是）
	
	@Column(name="add_time")
	private Date addTime;			//新增时间
	
	@Column(name="edit_time")
	private Date editTime;			//编辑时间
	private String status;				//状态（0：停用  1：启用）
	private String checked;				//审核（0：未审核  1：审核通过  2:审核不通过）
	private String remark;			    //备注(用于审核发布的解读信息)
	
	@Column(name="is_showIndex")
	private String isShowIndex;		   //发现模块首页显示(十位数上代表移动端,个位数上代表官网,用1和0代表是否在此终端显示,如11代表在两个终端都显示)
	
	
	@Column(name="is_year_free")
	private String isYearFree;      //是否年度VIP会员免费（0：否 1：是）
	
	@Column(name="is_month_free")
	private String isMonthFree;     //是否月度VIP会员免费（0：否 1：是）

	@Column(name="is_column_subscribe")
	private Integer isColumnSubscribe;   //App首页显示,订阅专栏和精品课程都可用(0:否,1:在首页,2:在首页查看更多)
	
	@Column(name="stop_update")
	private Integer stopUpdate;      //是否停止更新文档(0:否,1:是)

	// Constructors

	/** default constructor */
	public StandardReading() {
	}


	// Property accessors


	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Member getAuthor() {
		return author;
	}

	public void setAuthor(Member author) {
		this.author = author;
	}

	public String getPhoto() {
		return this.photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getPhoto2() {
		return this.photo2;
	}

	public void setPhoto2(String photo2) {
		this.photo2 = photo2;
	}

	public String getPhoto3() {
		return this.photo3;
	}

	public void setPhoto3(String photo3) {
		this.photo3 = photo3;
	}

	public String getPhoto4() {
		return this.photo4;
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

	public String getIntro() {
		return this.intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}
	public String getContentType() {
		return this.contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getVoice() {
		return this.voice;
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
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public String getDocumentId() {
		return documentId;
	}
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}
	public String getClassify() {
		return this.classify;
	}
	public void setClassify(String classify) {
		this.classify = classify;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getFatherId() {
		return fatherId;
	}
	public void setFatherId(Long fatherId) {
		this.fatherId = fatherId;
	}
	public Long getQualityId() {
		return qualityId;
	}
	public void setQualityId(Long qualityId) {
		this.qualityId = qualityId;
	}
	public String getIsRecommend() {
		return this.isRecommend;
	}
	public void setIsRecommend(String isRecommend) {
		this.isRecommend = isRecommend;
	}

	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	public Date getEditTime() {
		return editTime;
	}
	public void setEditTime(Date editTime) {
		this.editTime = editTime;
	}
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getChecked() {
		return checked;
	}

	public void setChecked(String checked) {
		this.checked = checked;
	}

	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	public String getIsShowIndex() {
		return isShowIndex;
	}

	public void setIsShowIndex(String isShowIndex) {
		this.isShowIndex = isShowIndex;
	}


	public String getIsYearFree() {
		return isYearFree;
	}


	public void setIsYearFree(String isYearFree) {
		this.isYearFree = isYearFree;
	}
	public String getIsMonthFree() {
		return isMonthFree;
	}
	public void setIsMonthFree(String isMonthFree) {
		this.isMonthFree = isMonthFree;
	}

	public String getRemark() {
		return remark;
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

	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getIsColumnSubscribe() {
		return isColumnSubscribe;
	}
	public void setIsColumnSubscribe(Integer isColumnSubscribe) {
		this.isColumnSubscribe = isColumnSubscribe;
	}

	public Integer getStopUpdate() {
		return stopUpdate;
	}

	public String getColumnIntro() {
		return columnIntro;
	}

	public void setColumnIntro(String columnIntro) {
		this.columnIntro = columnIntro;
	}
	public void setStopUpdate(Integer stopUpdate) {
		this.stopUpdate = stopUpdate;
	}
}