package cc.messcat.vo;

import java.util.List;

public class NationalDifferenceVo{

	private String voltage; 	//电压（V）
	private String hz;	 //频率（Hz）
	private String officialLanguage; //官方语言
	private String type; //类型
	private List<String> form; //形式（图片名称）

	public String getVoltage() {
		return voltage;
	}

	public void setVoltage(String voltage) {
		this.voltage = voltage;
	}

	public String getHz() {
		return hz;
	}

	public void setHz(String hz) {
		this.hz = hz;
	}

	public String getOfficialLanguage() {
		return officialLanguage;
	}

	public void setOfficialLanguage(String officialLanguage) {
		this.officialLanguage = officialLanguage;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<String> getForm() {
		return form;
	}

	public void setForm(List<String> form) {
		this.form = form;
	}
}