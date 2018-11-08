package cc.messcat.vo;

import java.util.List;

public class FindCoachImgsResult {
   
	private String result;
	private String code;
	private List<CoachPicsVo> coachPicsVoList;
	
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public List<CoachPicsVo> getCoachPicsVoList() {
		return coachPicsVoList;
	}
	public void setCoachPicsVoList(List<CoachPicsVo> coachPicsVoList) {
		this.coachPicsVoList = coachPicsVoList;
	}
}
