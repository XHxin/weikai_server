package cc.modules.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

@SuppressWarnings("unchecked")
public class ZipCompressor {

	private static final int BUFFER = 8192;

	/**
	 * 在第一级目录下， 指定压缩部分文件夹String[] designations
	 * @param srcPath
	 * @param zipFilePath
	 * @param designations
	 * @throws Exception
	 */
	public static void compress(final String srcPath, final String zipFilePath, final String[] designations) throws Exception {

		final File file = new File(srcPath);
		if (!file.exists()) {
			throw new RuntimeException(srcPath + "不存在!");
		}

		final FileOutputStream outputStream = new FileOutputStream(zipFilePath);
		final CheckedOutputStream cos = new CheckedOutputStream(outputStream, new CRC32());
		final ZipOutputStream zipOutputStream = new ZipOutputStream(cos);
		final String basedir = "";

		ZipCompressor.compressDirectoryWithDesignation(file, zipOutputStream, basedir, designations);

		zipOutputStream.close();
		outputStream.close();

	}

	/**
	 * 根据文件名列表打包
	 * @param fileNameList 文件名列表
	 * @param zipFilePath 打包文件的存放路径
	 * @param startRootName zip包里的根目录
     *                      例如文件：被打包的路径是E:\a\b\c\d.txt，如果startRootName=a，那么打包后的zip文件结构是XX.zip -> b\c\d.txt
     *                      例如文件夹：被打包的路径是E:\a\b\c，如果startRootName=a，那么打包后的zip文件结构是XX.zip -> b\c\*
	 * @throws Exception
	 */
	public static void compressFileList(final List fileNameList, final String zipFilePath, final String startRootName) throws Exception {

		if (CollectionUtil.isListEmpty(fileNameList)) {
			throw new RuntimeException("文件名列表是空");
		}

		final FileOutputStream outputStream = new FileOutputStream(zipFilePath);
		final CheckedOutputStream cos = new CheckedOutputStream(outputStream, new CRC32());
		final ZipOutputStream zipOutputStream = new ZipOutputStream(cos);
		final String basedir = "";

		ZipCompressor.compressFileList(fileNameList, zipOutputStream, basedir, startRootName);

		zipOutputStream.close();
		outputStream.close();

	}

	private static void compressDirectoryWithDesignation(final File file, final ZipOutputStream zipOutputStream,
		final String basedir, final String[] designations) throws Exception {

		final File[] files = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (ZipCompressor.existDesignFolder(files[i].getName(), designations)) {
				ZipCompressor.compressDirectoryOrFile(files[i], zipOutputStream, basedir + file.getName() + File.separator);
			}
		}
	}
	
	private static boolean existDesignFolder(final String fileName, final String[] designations) {

		boolean existFlag = false;
		for (int i = 0; i < designations.length; i++) {
			if (fileName.equalsIgnoreCase(designations[i])) {
				existFlag = true;
			}
		}

		return existFlag;
	}

	private static void compressFileList(final List fileNameList, final ZipOutputStream zipOutputStream, String basedir,
		final String startRootName) throws Exception {

		int fileNameListSize = fileNameList.size();
		for (int i = 0; i < fileNameListSize; i++) {
			String fileName = (String) fileNameList.get(i);
			File file = new File(fileName);

			// 例如文件：file.getPath()=E:/a/b/c/d.txt，startRootName=a，经过下面四步之后basedirTemp2=b/c/
			// 例如文件夹：file.getPath()=E:/a/b/c，startRootName=a，经过下面四步之后basedirTemp2=b/c/
			int rootIndex = file.getParent().indexOf(startRootName);
			String basedirTemp = file.getParent().substring(rootIndex);
			int firstSlashIndex = basedirTemp.indexOf(File.separator);
			String basedirTemp2 = basedirTemp.substring(firstSlashIndex + 1);
			if (startRootName.equals(basedirTemp2)) {
				basedirTemp2 = "";
			} else {
				basedirTemp2 = basedirTemp2 + File.separator;
			}

			ZipCompressor.compressDirectoryOrFile(file, zipOutputStream, basedirTemp2);
		}
	}

	private static void compressDirectoryOrFile(final File file, final ZipOutputStream zipOutputStream, final String basedir)
		throws Exception {

		if (file.isDirectory()) {
			ZipCompressor.compressDirectory(file, zipOutputStream, basedir);
		} else {
			ZipCompressor.compressFile(file, zipOutputStream, basedir);
		}

	}

	private static void compressDirectory(final File file, final ZipOutputStream zipOutputStream, final String basedir)
		throws Exception {

		final File[] files = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			ZipCompressor.compressDirectoryOrFile(files[i], zipOutputStream, basedir + file.getName() + File.separator);
		}
	}

	private static void compressFile(final File file, final ZipOutputStream zipOutputStream, final String basedir) throws Exception {

		final BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
		final ZipEntry zipEntry = new ZipEntry(basedir + file.getName());
		zipOutputStream.putNextEntry(zipEntry);

		int count;
		byte data[] = new byte[ZipCompressor.BUFFER];
		while ((count = bis.read(data, 0, ZipCompressor.BUFFER)) != -1) {
			zipOutputStream.write(data, 0, count);
		}

		bis.close();

	}

	/**
	 * 解压指定zip文件
	 * 
	 * @param zipfileName
	 *            需要解压的文件
	 * @param targetPath
	 *            解压后文件存放的路径
	 */
	public static void unCompress(String zipfileName, String targetPath) throws Exception {
		ZipFile zipFile = new ZipFile(zipfileName,"gbk");
		byte[] buf = new byte[1024];
		int readedBytes;
		for (Enumeration entries = zipFile.getEntries(); entries.hasMoreElements();) {
			ZipEntry entry = (ZipEntry) entries.nextElement();
			File file = new File(targetPath + entry.getName());
			if (entry.isDirectory()) {
				file.mkdirs();
			} else {

				// 如果指定文件的目录不存在，则创建
				File parent = file.getParentFile();
				if (!parent.exists()) {
					parent.mkdirs();
				}

				InputStream inputStream = zipFile.getInputStream(entry);
				FileOutputStream fileOut = new FileOutputStream(file);

				while ((readedBytes = inputStream.read(buf)) > 0) {
					fileOut.write(buf, 0, readedBytes);
				}
				fileOut.close();
				inputStream.close();
			}
		}

		zipFile.close();
	}

}
