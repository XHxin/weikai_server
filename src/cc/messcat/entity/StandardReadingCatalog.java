package cc.messcat.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="standard_reading_catalog")
public class StandardReadingCatalog implements Serializable{

	/*
	 * 标准解读目录
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;   
	@Column(name="standard_readingID")
	private Long standardReadId;   //标准解读Id
	@Column(name="catalog")
	private String catalog;   	   //目录
	@Column(name="first_level")
	private Long firstLevel;     //一级目录(0：否,1:是)
	@Column(name="second_level")
	private Long secondLevel;   //二级目录,存的是一级目录的Id
	@Column(name="three_level")
	private Long threeLevel;   //三级目录,存的是二级目录的id
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getStandardReadId() {
		return standardReadId;
	}
	public void setStandardReadId(Long standardReadId) {
		this.standardReadId = standardReadId;
	}
	public String getCatalog() {
		return catalog;
	}
	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}
	public Long getFirstLevel() {
		return firstLevel;
	}
	public void setFirstLevel(Long firstLevel) {
		this.firstLevel = firstLevel;
	}
	public Long getSecondLevel() {
		return secondLevel;
	}
	public void setSecondLevel(Long secondLevel) {
		this.secondLevel = secondLevel;
	}
	public Long getThreeLevel() {
		return threeLevel;
	}
	public void setThreeLevel(Long threeLevel) {
		this.threeLevel = threeLevel;
	}
}
