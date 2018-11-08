package cc.messcat.entity;

import java.util.Set;

/**
 * PageType entity. @author MyEclipse Persistence Tools
 */

public class PageType implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = -794279933290833875L;
	private Long id;
	private String name;
	private String templateType;
	private String templateUrl;
	private String featuresUrl;
	private String intro;
	private Set<EnterpriseColumn> enterpriseColumns;

	// Constructors

	/** default constructor */
	public PageType() {
	}

	/** minimal constructor */
	public PageType(String name) {
		this.name = name;
	}

	/** full constructor */
	public PageType(String name, String templateType, String templateUrl, String featuresUrl, String intro) {
		this.name = name;
		this.templateType = templateType;
		this.templateUrl = templateUrl;
		this.featuresUrl = featuresUrl;
		this.intro = intro;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTemplateType() {
		return this.templateType;
	}

	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}

	public String getTemplateUrl() {
		return this.templateUrl;
	}

	public void setTemplateUrl(String templateUrl) {
		this.templateUrl = templateUrl;
	}

	public String getFeaturesUrl() {
		return this.featuresUrl;
	}

	public void setFeaturesUrl(String featuresUrl) {
		this.featuresUrl = featuresUrl;
	}

	public String getIntro() {
		return this.intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public Set<EnterpriseColumn> getEnterpriseColumns() {
		return enterpriseColumns;
	}

	public void setEnterpriseColumns(Set<EnterpriseColumn> enterpriseColumns) {
		this.enterpriseColumns = enterpriseColumns;
	}

}