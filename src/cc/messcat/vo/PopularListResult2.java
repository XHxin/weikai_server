package cc.messcat.vo;

import java.util.List;

public class PopularListResult2 {
	
	public Integer code;
	public String message;
	public List<PopularListVo2> data;
	
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public List<PopularListVo2> getData() {
		return data;
	}
	public void setData(List<PopularListVo2> data) {
		this.data = data;
	}
}
