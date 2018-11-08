package cc.messcat.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author xiehuaxin
 * @createDate 2018年5月30日 下午2:01:16
 * @todo IOS版本上架时，传一个版本号过来，然后返回一个是否审核中
 */
@Entity
@Table(name="app_check")
public class AppCheck {

	@Id
	@GeneratedValue
	private Integer id;
	
	@Column(name="app_version")
	private String appVersion;
	
	@Column(name="is_checking")
	private Integer checking;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public Integer getChecking() {
		return checking;
	}

	public void setChecking(Integer checking) {
		this.checking = checking;
	}
	
	
}
