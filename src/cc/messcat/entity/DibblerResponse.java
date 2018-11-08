package cc.messcat.entity;

/**
 * @author HASEE
 *点播服务回调传回来的参数
 */
public class DibblerResponse {

	private String version;//回调版本号，固定为4.0 
	private String eventType;//回调类型，固定为NewFileUpload
	private String fileId;//文件唯一id
	private String author;//作者信息
	private String sourceType;//文件的上传来源。目前有Record：录制，Pull：转拉，ClientUpload：客户端上传，ServerUpload：服务端上传
	private String sourceContext;//如果sourceType是Record，该字段即推流url；其它情况该字段暂未支持。该字段目前最多256字节。
	private String streamId;//推流id，录制上传特有 
	private String procedureTaskId;//该视频上传之后进行了指定流程，则该参数为流程任务id
	private String transcodeTaskId;//如果该视频上传之后发起了转码，则该参数为转码任务id
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
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getSourceType() {
		return sourceType;
	}
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	public String getSourceContext() {
		return sourceContext;
	}
	public void setSourceContext(String sourceContext) {
		this.sourceContext = sourceContext;
	}
	public String getStreamId() {
		return streamId;
	}
	public void setStreamId(String streamId) {
		this.streamId = streamId;
	}
	public String getProcedureTaskId() {
		return procedureTaskId;
	}
	public void setProcedureTaskId(String procedureTaskId) {
		this.procedureTaskId = procedureTaskId;
	}
	public String getTranscodeTaskId() {
		return transcodeTaskId;
	}
	public void setTranscodeTaskId(String transcodeTaskId) {
		this.transcodeTaskId = transcodeTaskId;
	}
	
	
	
}
