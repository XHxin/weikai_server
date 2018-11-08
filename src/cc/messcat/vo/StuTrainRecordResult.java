package cc.messcat.vo;

import java.util.List;

public class StuTrainRecordResult extends CommonResult{

	private List<TrainRecordVo> trainRecordList;

	public List<TrainRecordVo> getTrainRecordList() {
		return trainRecordList;
	}

	public void setTrainRecordList(List<TrainRecordVo> trainRecordList) {
		this.trainRecordList = trainRecordList;
	}

}
