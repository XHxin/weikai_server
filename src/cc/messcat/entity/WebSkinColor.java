// Decompiled by DJ v2.9.9.60 Copyright 2000 Atanas Neshkov  Date: 2010-5-13 14:03:17
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   WebSite.java

package cc.messcat.entity;

import javax.persistence.*;
import java.io.Serializable;
@Entity
@Table(name="WEB_SKIN_COLOR")
public class WebSkinColor implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name="NAMES")
	private String names;

	@Column(name="FILENAME")
	private String filename;

	@Column(name="CONTENT")
	private String content;

	@Column(name="STATE")
	private String state;

	@Column(name="WEB_SKIN_ID")
	private Long webSkinId;

	@Column(name="IS_DEFAULT_ID")
	private String isDefaultId;

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

	public Long getWebSkinId() {
		return webSkinId;
	}

	public void setWebSkinId(Long webSkinId) {
		this.webSkinId = webSkinId;
	}

	public String getIsDefaultId() {
		return isDefaultId;
	}

	public void setIsDefaultId(String isDefaultId) {
		this.isDefaultId = isDefaultId;
	}

}