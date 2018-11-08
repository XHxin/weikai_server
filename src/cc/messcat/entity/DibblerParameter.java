package cc.messcat.entity;

import org.hibernate.mapping.Array;

/**
 * 请求腾讯云点播接口的参数实体
 * @author HASEE
 *
 */
public class DibblerParameter {

	private String version;//回调版本号，固定为4.0
	private String eventType;//	回调类型，固定为ConcatComplete
	private Object data;//具体回调数据;
	private String vodTaskId;//拼接任务ID
	private Array fileInfo;//拼接输出的视频文件信息
	private String fileType;//拼接出的文件类型
	private Integer status;//任务执行结果，0为成功，-1或者4为失败
	private String message;//错误信息
	private String fileId;//拼接出的文件的fileId
	private String fileUrl;//拼接出的文件url
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public String getVodTaskId() {
		return vodTaskId;
	}
	public void setVodTaskId(String vodTaskId) {
		this.vodTaskId = vodTaskId;
	}
	public Array getFileInfo() {
		return fileInfo;
	}
	public void setFileInfo(Array fileInfo) {
		this.fileInfo = fileInfo;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	public String getFileUrl() {
		return fileUrl;
	}
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	
}
