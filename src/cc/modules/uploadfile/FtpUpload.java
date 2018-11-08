package cc.modules.uploadfile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.stereotype.Component;

@Component("ftpUpload")
public class FtpUpload {

	private FTPClient ftp;
	private String ftpUrl;
	private String ftpPort;
	private String ftpUser;
	private String ftpPassword;
	private String ftpWorkingDirectory;

	/**
	 * @param clientWorkingDirectory
	 *            这是要上传的目录
	 * @param parentFolderName
	 *            这是FTP服务器根目录下需要建的文件夹
	 * @throws Exception
	 */
	public void ftpUpload(String clientWorkingDirectory, String parentFolderName) throws Exception {
		if (this.connect()) {
			if (parentFolderName != null && parentFolderName.length() > 0) {
				ftp.makeDirectory(parentFolderName);
				ftp.changeWorkingDirectory(parentFolderName);
			}
			this.upload(new File(clientWorkingDirectory));
		}
	}

	public void ftpUploadToNginx(String clientWorkingDirectory, String parentFolderName) throws Exception {
		if (this.connect()) {
			if (parentFolderName != null && parentFolderName.length() > 0) {
				String[] strArr = parentFolderName.substring(0, parentFolderName.lastIndexOf("/")).split("/");
				for (String str : strArr) {
					ftp.makeDirectory(str);
					ftp.changeWorkingDirectory(str);
				}
				// ftp.makeDirectory(parentFolderName);
				// ftp.changeWorkingDirectory(parentFolderName);
			}
			this.upload(new File(clientWorkingDirectory));
		}
	}

	public InputStream ftpDownToNginx(String path, String fileName) throws Exception {
		if (this.connect()) {
			System.out.println("=========ftp连接成功===============");
			System.out.println("=========ftp路径：" + path + "===============");
			System.out.println("=========文件名：" + fileName + "===============");
			ftp.changeWorkingDirectory(path);// 转移到FTP服务器目录
			// 遍历下载的目录
			FTPFile[] fs = ftp.listFiles();
			for (FTPFile ff : fs) {
				// System.out.println(ff.getName());
				// 解决中文乱码问题，两次解码
				byte[] bytes = ff.getName().getBytes("iso-8859-1");
				String fn = new String(bytes, "utf8");
				if (fn.equals(fileName)) {
					// 读取到对应的文件
					System.out.println("============找到相同文件============");
					InputStream ins = ftp.retrieveFileStream(fileName);
					System.out.println("============返回数据流============");
					return ins;
				}
			}
		}
		return null;
	}

	private void upload(File file) throws Exception {

		if (file.isDirectory()) {
			ftp.makeDirectory(file.getName());
			ftp.changeWorkingDirectory(file.getName());
			File[] files = file.listFiles();
			if (files != null) {
				for (int i = 0; i < files.length; i++) {
					File file1 = files[i];
					if (file1.isDirectory()) {
						upload(file1);
						ftp.changeToParentDirectory();
					} else {
						FileInputStream input = new FileInputStream(file1);
						ftp.enterLocalPassiveMode();
						ftp.storeFile(file1.getName(), input);
						String replyCode = String.valueOf(ftp.getReplyCode());
						if (!replyCode.startsWith("1") && !replyCode.startsWith("2")) {
							throw new Exception(ftp.getReplyString());
						}
						input.close();
					}
				}
			}
		} else {
			FileInputStream input = new FileInputStream(file);
			ftp.enterLocalPassiveMode();
			ftp.storeFile(file.getName(), input);
			String replyCode = String.valueOf(ftp.getReplyCode());
			if (!replyCode.startsWith("1") && !replyCode.startsWith("2")) {
				throw new Exception(ftp.getReplyString());
			}
			input.close();
		}
	}

	private boolean connect() throws Exception {

		ftp = new FTPClient();

		// 连接FTP服务器, 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
		ftp.connect(ftpUrl, Integer.valueOf(ftpPort));
		ftp.login(ftpUser, ftpPassword);// 登录
		ftp.setFileType(FTPClient.BINARY_FILE_TYPE);// 用以上传图片声音之类的文件
		int reply = ftp.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
			ftp.disconnect();
			return false;
		}
		ftp.changeWorkingDirectory(ftpWorkingDirectory);
		return true;

	}

	public String getFtpUrl() {
		return ftpUrl;
	}

	public void setFtpUrl(String ftpUrl) {
		this.ftpUrl = ftpUrl;
	}

	public String getFtpPort() {
		return ftpPort;
	}

	public void setFtpPort(String ftpPort) {
		this.ftpPort = ftpPort;
	}

	public String getFtpUser() {
		return ftpUser;
	}

	public void setFtpUser(String ftpUser) {
		this.ftpUser = ftpUser;
	}

	public String getFtpPassword() {
		return ftpPassword;
	}

	public void setFtpPassword(String ftpPassword) {
		this.ftpPassword = ftpPassword;
	}

	public String getFtpWorkingDirectory() {
		return ftpWorkingDirectory;
	}

	public void setFtpWorkingDirectory(String ftpWorkingDirectory) {
		this.ftpWorkingDirectory = ftpWorkingDirectory;
	}

}
