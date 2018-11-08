package cc.messcat.vo;

import java.util.List;

public class TrainDetailVo {
	
	private String checkInTime;
	private String checkOutTime;
	private String trainTime;
	private List<TrainActionVo> trainActionList;
	
	public String getCheckInTime() {
		return checkInTime;
	}
	public void setCheckInTime(String checkInTime) {
		this.checkInTime = checkInTime;
	}
	public String getCheckOutTime() {
		return checkOutTime;
	}
	public void setCheckOutTime(String checkOutTime) {
		this.checkOutTime = checkOutTime;
	}
	public String getTrainTime() {
		return trainTime;
	}
	public void setTrainTime(String trainTime) {
		this.trainTime = trainTime;
	}
	public List<TrainActionVo> getTrainActionList() {
		return trainActionList;
	}
	public void setTrainActionList(List<TrainActionVo> trainActionList) {
		this.trainActionList = trainActionList;
	}

}
