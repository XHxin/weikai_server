package cc.messcat.vo;

public class CoachLoginResult {
	
	private String result;
	private String code;
	private CoachVo2 coach;
	
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
	public CoachVo2 getCoach() {
		return coach;
	}
	public void setCoach(CoachVo2 coach) {
		this.coach = coach;
	}

}
