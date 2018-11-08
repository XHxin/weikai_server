package cc.messcat.vo;

public class TrainCommentViewResult extends CommonResult{
	
	private TrainDetailVo trainDetail;
	private TrainCommentVo trainComment;
	
	public TrainDetailVo getTrainDetail() {
		return trainDetail;
	}
	public void setTrainDetail(TrainDetailVo trainDetail) {
		this.trainDetail = trainDetail;
	}
	public TrainCommentVo getTrainComment() {
		return trainComment;
	}
	public void setTrainComment(TrainCommentVo trainComment) {
		this.trainComment = trainComment;
	}

}
