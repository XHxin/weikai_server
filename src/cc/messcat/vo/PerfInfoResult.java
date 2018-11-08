package cc.messcat.vo;

/**
 * 绩效统计返回类型
 * @author StevenWang
 *
 */
public class PerfInfoResult extends CommonResult {
	
	private PerfInfoVo perfInfo;

	public PerfInfoVo getPerfInfo() {
		return perfInfo;
	}

	public void setPerfInfo(PerfInfoVo perfInfo) {
		this.perfInfo = perfInfo;
	}

}
