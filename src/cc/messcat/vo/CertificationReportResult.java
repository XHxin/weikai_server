package cc.messcat.vo;

import java.util.List;
import java.util.Map;
import cc.messcat.entity.CertificationAuthority;
import cc.messcat.entity.NationalDifferences;

public class CertificationReportResult {
	
	private String regionName;
	private String productName;
	private List<LegalsVo> legalsList;
	private Map<String,String> standard;
	private List<AuthenticationVo> authenList;
	private List<CertificationAuthorityVo> certifiAuthList;
	private NationalDifferenceVo different;
	
	
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public Map<String, String> getStandard() {
		return standard;
	}
	public void setStandard(Map<String, String> standard) {
		this.standard = standard;
	}
	public List<LegalsVo> getLegalsList() {
		return legalsList;
	}
	public void setLegalsList(List<LegalsVo> legalsList) {
		this.legalsList = legalsList;
	}
	public List<AuthenticationVo> getAuthenList() {
		return authenList;
	}
	public void setAuthenList(List<AuthenticationVo> authenList) {
		this.authenList = authenList;
	}
	public List<CertificationAuthorityVo> getCertifiAuthList() {
		return certifiAuthList;
	}
	public void setCertifiAuthList(List<CertificationAuthorityVo> certifiAuthList) {
		this.certifiAuthList = certifiAuthList;
	}
	public NationalDifferenceVo getDifferent() {
		return different;
	}
	public void setDifferent(NationalDifferenceVo different) {
		this.different = different;
	}
}
