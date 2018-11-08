package cc.messcat.service.system;

/**
 * @author xiehuaxin
 * @createDate 2018年5月30日 下午2:16:15
 * @todo TODO
 */
public interface SystemManagerDao{

	/**
	 * 根据版本号查询这个版本是否正在审核
	 * @param version
	 * @return
	 */
	Integer checkStatus(String version);

}
