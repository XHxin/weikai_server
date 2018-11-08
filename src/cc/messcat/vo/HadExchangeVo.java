package cc.messcat.vo;

/**
 * @author HASEE
 *微信分享邀请免单活动，进入页面时检查是否已经兑换过和是否达到兑换资格
 */
public class HadExchangeVo {

	private boolean hadChanged;//是否已兑换
	private boolean finishTask;//是否已完成任务
	public boolean isHadChanged() {
		return hadChanged;
	}
	public void setHadChanged(boolean hadChanged) {
		this.hadChanged = hadChanged;
	}
	public boolean isFinishTask() {
		return finishTask;
	}
	public void setFinishTask(boolean finishTask) {
		this.finishTask = finishTask;
	}
	
	
}
