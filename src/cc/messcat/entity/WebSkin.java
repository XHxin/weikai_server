// Decompiled by DJ v2.9.9.60 Copyright 2000 Atanas Neshkov  Date: 2010-5-13 14:03:17
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   WebSite.java

package cc.messcat.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springside.modules.utils.ReflectionUtils;

public class WebSkin implements Serializable {

	private static final long serialVersionUID = 6923039931534114843L;
	private Long id;
	private String names;
	private String filename;
	private String content;
	private String state;

	private Set webSites = new HashSet(0);

	public List<Integer> getWebSiteIds() throws Exception {
		return ReflectionUtils.fetchElementPropertyToList(webSites, "id");
	}

	public Set<WebSite> getWebSites() {
		return webSites;
	}

	public void setWebSites(Set webSites) {
		this.webSites = webSites;
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

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}