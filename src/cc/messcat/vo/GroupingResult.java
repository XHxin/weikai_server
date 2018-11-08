package cc.messcat.vo;

import java.util.List;

public class GroupingResult {
    private String result;
    private String code;
    private List<CoachBookVo> coachBookVos;//教练列表
   
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
	public List<CoachBookVo> getCoachBookVos() {
		return coachBookVos;
	}
	public void setCoachBookVos(List<CoachBookVo> coachBookVos) {
		this.coachBookVos = coachBookVos;
	}
	
    
	
	
}
