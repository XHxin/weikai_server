package cc.modules.uploadfile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import cc.modules.security.ExceptionManager;

public class FilesUploadModel {
	
	private String wtpDeploy;// 配置文件路径
	private String originFileName;// 文件名
	private String newFileName;// 文件新名
	private InputStream fileInputStream;
	private String base64FileContent;// base64位编码内容
	private String fileRealPath;// 文件真实路径
	private String fileUri;// 文件网络路径
	private String projectName;// 项目名
	private String folderName;// 文件夹路径
	private boolean isFileExists = false;

	public FilesUploadModel(String projectName, String fileUri, String wtpDeploy) {
		this.wtpDeploy = wtpDeploy;
		this.fileRealPath = ContainerUtil.getContainerRealPath(projectName, fileUri.replace("/", File.separator), wtpDeploy);
	}
	public FilesUploadModel(String originFileName, InputStream fileInputStream, String projectName, String folderName) {
		this.originFileName = originFileName;
		this.fileInputStream = fileInputStream;
		this.projectName = projectName;
		this.folderName = folderName;
		this.newFileName = FileHandler.createFileNameByTimeRole(originFileName);
	}

	public FilesUploadModel(String originFileName, String base64FileContent, String projectName, String folderName,
		String wtpDeploy) {
		this.wtpDeploy = wtpDeploy;
		this.originFileName = originFileName;
		this.base64FileContent = base64FileContent;
		this.projectName = projectName;
		this.folderName = folderName;
		this.newFileName = FileHandler.createFileNameByTimeRole(originFileName);
	}

	public FilesUploadModel(String originFileName, InputStream fileInputStream, String projectName, String folderName,
		String wtpDeploy) {
		this.wtpDeploy = wtpDeploy;
		this.originFileName = originFileName;
		this.fileInputStream = fileInputStream;
		this.projectName = projectName;
		this.folderName = folderName;
		this.newFileName = FileHandler.createFileNameByTimeRole(originFileName);
	}

	public FilesUploadModel(InputStream fileInputStream, String projectName, String folderName, String newFileName,
		String wtpDeploy) {
		this.wtpDeploy = wtpDeploy;
		this.newFileName = newFileName;
		this.fileInputStream = fileInputStream;
		this.projectName = projectName;
		this.folderName = folderName;
	}

	public void writeFileByBinary() throws ExceptionManager, IOException {
		this.fileRealPath = ContainerUtil.getContainerRealPath(projectName, folderName, newFileName, wtpDeploy);
		this.isFileExists = FileHandler.createFileByBinary(this.fileInputStream, fileRealPath);
		this.fileUri = FileHandler.getFileUri(newFileName);
	}

	public void writeFileByBase64() {

		this.fileRealPath = ContainerUtil.getContainerRealPath(projectName, folderName, newFileName, wtpDeploy);
		this.isFileExists = FileHandler.createFileByBase64(base64FileContent, fileRealPath);
		this.fileUri = FileHandler.getFileUri(newFileName);
		/*// ftp
		FtpUpload ftpUpload = new FtpUpload();
		Properties prop = new Properties();
		InputStream in = FilesUploadModel.class.getClassLoader().getResourceAsStream("ftp.properties");
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		try {
			prop.load(br);
			ftpUrl = prop.getProperty("ftpUrl").trim();
			ftpUserName = prop.getProperty("ftpUserName").trim();
			ftpPassword = prop.getProperty("ftpPassword").trim();
			ftpPort = prop.getProperty("ftpPort").trim();
			ftpServerWorkingDirectory = prop.getProperty("ftpServerWorkingDirectory").trim();

			ftpUpload.setFtpUrl(ftpUrl);
			ftpUpload.setFtpUser(ftpUserName);
			ftpUpload.setFtpPassword(ftpPassword);
			ftpUpload.setFtpPort(ftpPort);
			ftpUpload.setFtpWorkingDirectory(ftpServerWorkingDirectory);
			ftpUpload.ftpUploadToNginx(fileRealPath, fileUri);
		} catch (Exception e) {
			e.printStackTrace();
		}*/

	}

	public void deleteFile() {
		new java.io.File(this.fileRealPath).deleteOnExit();
	}

	public String getOriginFileName() {
		return originFileName;
	}

	public String getNewFileName() {
		return newFileName;
	}

	public String getFileRealPath() {
		return fileRealPath;
	}

	public String getFileUri() {
		return fileUri;
	}

	public boolean isFileExists() {
		return isFileExists;
	}

	public String getWtpDeploy() {
		return wtpDeploy;
	}

}
