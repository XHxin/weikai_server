package cc.messcat.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * BackupHistory entity.
 * 
 * @author Michael Tang
 */
@Entity
@Table(name ="backuphistory")
public class BackupHistory implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name="NAME")
	private String name;

	@Column(name="DATE_TIME")
	private Date dateTime;

	@Column(name="PATH")
	private String path;

	@Column(name="TYPE")
	private String type;

	@Column(name="STATUS")
	private String status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}