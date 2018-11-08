package cc.messcat.vo;

public class LiveVideoVo2 {
	
	/**
	 * 课程的PPT专用
	 */
	private String fileName;	//附件名称
	private String documentID;	//documentID
	private String format;		//文件格式
	private String pageCount;	//页码
	
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getDocumentID() {
		return documentID;
	}
	public void setDocumentID(String documentID) {
		this.documentID = documentID;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getPageCount() {
		return pageCount;
	}
	public void setPageCount(String pageCount) {
		this.pageCount = pageCount;
	}
	
}
