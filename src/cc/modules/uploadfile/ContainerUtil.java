package cc.modules.uploadfile;

import java.io.File;
import org.springframework.stereotype.Component;
import cc.modules.constants.SystemConstant;
import cc.modules.util.StringUtil;

/**
 * 容器工具
 * 
 * @author Panda
 * @version 1.0
 */
@Component
public class ContainerUtil {

	/**
	 * 获取容器真实路径 eg.{D:\work\tomcat8.0\webapps}\{projectName}\{fileName}
	 * 
	 * @param projectName
	 * @param folderName
	 * @return
	 * @throws Exception
	 * @author Panda
	 */
	public static String getContainerRealPath(String projectName, String folderName, String fileName, String wtpDeploy) {
		StringBuffer filePath = new StringBuffer();
		filePath.append(getContainerRealPath(projectName, folderName, wtpDeploy));
		if (StringUtil.isBlank(fileName))
			throw new NullPointerException("fileName is null!");
		filePath.append(File.separator);
		filePath.append(fileName);
		return filePath.toString();
	}

	/**
	 * 获取容器真实路径 eg.{D:\work\tomcat8.0\webapps}\{projectName}\{folderName}
	 * 
	 * @param projectName
	 * @param folderName
	 * @return
	 * @throws Exception
	 * @author Panda
	 */
	public static String getContainerRealPath(String projectName, String folderName, String wtpDeploy) {
		StringBuffer folderPath = new StringBuffer();

		folderPath.append(getContainerRealPath(projectName, wtpDeploy));

		if (StringUtil.isBlank(folderName))
			throw new NullPointerException("folderName is null");

		folderPath.append(File.separator);

		folderPath.append(folderName);

		return folderPath.toString();
	}

	/**
	 * 获取容器真实路径 eg.{D:\work\tomcat8.0\webapps}\{projectName}
	 * 
	 * 优先使用app.properties 配置文件的部署路径、 其次是系统环境设置的wtp.deploy参数
	 * 最后使用默认的CATALINA_HOME/webapps路径 如果JVM中没有CATALINA参数则抛出异常
	 * 
	 * @param projectName
	 * @param folderName
	 * @return
	 * @throws Exception
	 * @author Panda
	 */
	public static String getContainerRealPath(String projectName, String wtpDeploy) {
		StringBuffer folderPath = new StringBuffer();
		String wwwRoot = null;
		if (StringUtil.isBlank(wtpDeploy)) {
			// webroot real path
			//wwwRoot = System.getProperty(SystemConstant.WTP_DEPLOY);
			wwwRoot = SystemConstant.E_WWWROOT;
			if (StringUtil.isBlank(wwwRoot)) {
				wwwRoot = System.getProperty(SystemConstant.CATALINA_HOME);
				if (StringUtil.isBlank(wwwRoot))
					throw new NullPointerException("don`t exist webroot real path in system property!");
				wwwRoot = wwwRoot + File.separator + "webapps";
			}
		}
		folderPath.append(wwwRoot);

		if (StringUtil.isBlank(projectName))
			throw new NullPointerException("projectName is null!");

		folderPath.append(File.separator);
		folderPath.append(projectName);

		return folderPath.toString();
	}

}
