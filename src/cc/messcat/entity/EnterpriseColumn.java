package cc.messcat.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EnterpriseColumn implements Serializable {

	private static final long serialVersionUID = -3671132492286112466L;
	private Long id;
	private String names;
	private String shortName;
	private Long orderColumn;
	private PageType typeColumn;
	private String linkUrl;
	private String isValidInNav;
	private String num;
	private String intro;
	private Long father;
	private Long grade;
	private String state;
	private Set enterpriseNewses;
	private String frontNum;
	private String htmlName;
	private List<EnterpriseColumn> subColumnList;

	public EnterpriseColumn() {
		enterpriseNewses = new HashSet(0);
	}

	public EnterpriseColumn(String names, String shortName, String num, String intro, Long father, Long grade, String state,
		Set enterpriseNewses) {
		this.enterpriseNewses = new HashSet(0);
		this.names = names;
		this.shortName = shortName;
		this.num = num;
		this.intro = intro;
		this.father = father;
		this.grade = grade;
		this.state = state;
		this.enterpriseNewses = enterpriseNewses;
	}

	public EnterpriseColumn(Long id, String names, Long orderColumn, String frontNum, String isValidInNav, String state,
		Long father, PageType typeColumn) {
		this.id = id;
		this.names = names;
		this.father = father;
		this.typeColumn = typeColumn;
		this.orderColumn = orderColumn;
		this.isValidInNav = isValidInNav;
		this.state = state;
		this.frontNum = frontNum;
	}

	public String getHtmlName() {
		return htmlName;
	}

	public void setHtmlName(String htmlName) {
		this.htmlName = htmlName;
	}

	public String getFrontNum() {
		return frontNum;
	}

	public void setFrontNum(String frontNum) {
		this.frontNum = frontNum;
	}

	public String getNames() {
		return names;
	}

	public void setNames(String names) {
		this.names = names;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public Long getFather() {
		return father;
	}

	public void setFather(Long father) {
		this.father = father;
	}

	public Long getGrade() {
		return grade;
	}

	public void setGrade(Long grade) {
		this.grade = grade;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Set getEnterpriseNewses() {
		return enterpriseNewses;
	}

	public void setEnterpriseNewses(Set enterpriseNewses) {
		this.enterpriseNewses = enterpriseNewses;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public Long getOrderColumn() {
		return orderColumn;
	}

	public void setOrderColumn(Long orderColumn) {
		this.orderColumn = orderColumn;
	}

	public PageType getTypeColumn() {
		return typeColumn;
	}

	public void setTypeColumn(PageType typeColumn) {
		this.typeColumn = typeColumn;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public String getIsValidInNav() {
		return isValidInNav;
	}

	public void setIsValidInNav(String isValidInNav) {
		this.isValidInNav = isValidInNav;
	}

	public List<EnterpriseColumn> getSubColumnList() {
		return subColumnList;
	}

	public void setSubColumnList(List<EnterpriseColumn> subColumnList) {
		this.subColumnList = subColumnList;
	}

}