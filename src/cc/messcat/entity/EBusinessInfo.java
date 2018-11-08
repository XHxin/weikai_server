package cc.messcat.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="e_business_info")
public class EBusinessInfo implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;     
    
    @Column(name="eBusinessProductID")
	private Long ebusinessProductId;		//产品统称ID
    
	private String ebusinessProductName;	//产品统称
	
	@Column(name="subEBusinessProductID")
	private String subEbusinessProductId;	//子产品ID（用;号隔开）
	private String subEbusinessProductName;	//子产品名
	
	@Column(name="big_class")
	private String bigClass;				//大类
	private String platform;				//平台
	
	@Column(name="industrial_standard_name")
	private String industrialStandardName;	//行业标准（文件显示名称）
	
	@Column(name="industrial_standard_file")
	private String industrialStandardFile;	//行业标准（文件）
	
	@Column(name="industrial_standard_documentId")
	private String industrialStandardDocumentId;	//行业标准百度云id
	
	@Column(name="industrial_standard_pageCount")
	private Integer industrialStandardPageCount;	//行业标准百度云页码
	
	@Column(name="check_standard_name")
	private String checkStandardName;		//抽查规范（文件显示名称）
	
	@Column(name="check_standard_file")
	private String checkStandardFile;		//抽查规范（文件）
	
	@Column(name="check_standard_documentId")
	private String checkStandardDocumentId;	//抽查规范百度云id
	
	@Column(name="check_standard_pageCount")
	private Integer checkStandardPageCount;	//抽查规范百度云页码
	
	@Column(name="platform_standard_name")
	private String platformStandardName;	//平台标准（文件显示名称）
	
	@Column(name="platform_standard_file")
	private String platformStandardFile;	//平台标准（文件）
	
	@Column(name="platform_standard_documentId")
	private String platformStandardDocumentId;	//平台标准百度云id
	
	@Column(name="platform_standard_pageCount")
	private Integer platformStandardPageCount;	//平台标准百度云页码
	private String remark;					//备注
	
	@Column(name="add_time")
	private Date addTime;				//新增时间
	
	@Column(name="edit_time")
	private Date editTime;				//编辑时间
	private String status;					//状态（0：停用  1：启用）

	
	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public String getEbusinessProductName() {
		return this.ebusinessProductName;
	}

	public void setEbusinessProductName(String ebusinessProductName) {
		this.ebusinessProductName = ebusinessProductName;
	}

	public Long getEbusinessProductId() {
		return ebusinessProductId;
	}

	public void setEbusinessProductId(Long ebusinessProductId) {
		this.ebusinessProductId = ebusinessProductId;
	}

	public String getSubEbusinessProductId() {
		return subEbusinessProductId;
	}

	public void setSubEbusinessProductId(String subEbusinessProductId) {
		this.subEbusinessProductId = subEbusinessProductId;
	}

	public String getSubEbusinessProductName() {
		return this.subEbusinessProductName;
	}

	public void setSubEbusinessProductName(String subEbusinessProductName) {
		this.subEbusinessProductName = subEbusinessProductName;
	}

	public String getBigClass() {
		return this.bigClass;
	}

	public void setBigClass(String bigClass) {
		this.bigClass = bigClass;
	}

	public String getPlatform() {
		return this.platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getIndustrialStandardName() {
		return this.industrialStandardName;
	}

	public void setIndustrialStandardName(String industrialStandardName) {
		this.industrialStandardName = industrialStandardName;
	}

	public String getIndustrialStandardFile() {
		return this.industrialStandardFile;
	}

	public void setIndustrialStandardFile(String industrialStandardFile) {
		this.industrialStandardFile = industrialStandardFile;
	}

	public String getCheckStandardName() {
		return this.checkStandardName;
	}

	public void setCheckStandardName(String checkStandardName) {
		this.checkStandardName = checkStandardName;
	}

	public String getCheckStandardFile() {
		return this.checkStandardFile;
	}

	public void setCheckStandardFile(String checkStandardFile) {
		this.checkStandardFile = checkStandardFile;
	}

	public String getPlatformStandardName() {
		return this.platformStandardName;
	}

	public void setPlatformStandardName(String platformStandardName) {
		this.platformStandardName = platformStandardName;
	}

	public String getPlatformStandardFile() {
		return this.platformStandardFile;
	}

	public void setPlatformStandardFile(String platformStandardFile) {
		this.platformStandardFile = platformStandardFile;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public String getIndustrialStandardDocumentId() {
		return industrialStandardDocumentId;
	}

	public void setIndustrialStandardDocumentId(String industrialStandardDocumentId) {
		this.industrialStandardDocumentId = industrialStandardDocumentId;
	}

	public Integer getIndustrialStandardPageCount() {
		return industrialStandardPageCount;
	}

	public void setIndustrialStandardPageCount(Integer industrialStandardPageCount) {
		this.industrialStandardPageCount = industrialStandardPageCount;
	}

	public String getCheckStandardDocumentId() {
		return checkStandardDocumentId;
	}

	public void setCheckStandardDocumentId(String checkStandardDocumentId) {
		this.checkStandardDocumentId = checkStandardDocumentId;
	}

	public Integer getCheckStandardPageCount() {
		return checkStandardPageCount;
	}

	public void setCheckStandardPageCount(Integer checkStandardPageCount) {
		this.checkStandardPageCount = checkStandardPageCount;
	}

	public String getPlatformStandardDocumentId() {
		return platformStandardDocumentId;
	}

	public void setPlatformStandardDocumentId(String platformStandardDocumentId) {
		this.platformStandardDocumentId = platformStandardDocumentId;
	}

	public Integer getPlatformStandardPageCount() {
		return platformStandardPageCount;
	}

	public void setPlatformStandardPageCount(Integer platformStandardPageCount) {
		this.platformStandardPageCount = platformStandardPageCount;
	}
}