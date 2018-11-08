// Decompiled by DJ v2.9.9.60 Copyright 2000 Atanas Neshkov Date: 2010-5-13
// 14:03:17
// Home Page : http://members.fortunecity.com/neshkov/dj.html - Check often for
// new version!
// Decompiler options: packimports(3)
// Source File Name: WebSite.java

package cc.messcat.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springside.modules.utils.ReflectionUtils;

public class WebSite implements Serializable {

	private static final long serialVersionUID = 3311724165540742421L;
	private Long id;
	private String names;
	private String title;
	private String domain;
	private String port;
	private String company;
	private String copyright;
	private String recodeCode;
	private Long defaultSkin;
	private String htmlName;
	private WebSkin webSkin;

	private Set webSkins = new HashSet(0);

	public WebSite() {
	}

	public WebSite(String names) {
		this.names = names;
	}

	public List<Long> getWebSkinIds() throws Exception {
		return ReflectionUtils.fetchElementPropertyToList(webSkins, "id");
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNames() {
		return names;
	}

	public void setNames(String names) {
		this.names = names;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getCopyright() {
		return copyright;
	}

	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	public String getRecodeCode() {
		return recodeCode;
	}

	public void setRecodeCode(String recodeCode) {
		this.recodeCode = recodeCode;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public Set getWebSkins() {
		return webSkins;
	}

	public void setWebSkins(Set webSkins) {
		this.webSkins = webSkins;
	}

	public Long getDefaultSkin() {
		return defaultSkin;
	}

	public void setDefaultSkin(Long defaultSkin) {
		this.defaultSkin = defaultSkin;
	}

	public WebSkin getWebSkin() {
		return webSkin;
	}

	public void setWebSkin(WebSkin webSkin) {
		this.webSkin = webSkin;
	}

	public String getHtmlName() {
		return htmlName;
	}

	public void setHtmlName(String htmlName) {
		this.htmlName = htmlName;
	}

}