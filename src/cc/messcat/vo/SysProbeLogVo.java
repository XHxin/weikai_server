package cc.messcat.vo;

import java.io.Serializable;
import java.util.Date;

public class SysProbeLogVo implements Serializable {

	private static final long serialVersionUID = -117074412498505574L;
	
	private Long studentId;
	private Long coachId; 
	private Long newsId;
	private Long schoolId;
	private String probeEnName;
	private Date ReceiveTime;
	
	public Long getStudentId() {
		return studentId;
	}
	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}
	public Long getCoachId() {
		return coachId;
	}
	public void setCoachId(Long coachId) {
		this.coachId = coachId;
	}
	public Long getNewsId() {
		return newsId;
	}
	public void setNewsId(Long newsId) {
		this.newsId = newsId;
	}
	public Long getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}
	public String getProbeEnName() {
		return probeEnName;
	}
	public void setProbeEnName(String probeEnName) {
		this.probeEnName = probeEnName;
	}
	public Date getReceiveTime() {
		return ReceiveTime;
	}
	public void setReceiveTime(Date receiveTime) {
		ReceiveTime = receiveTime;
	}

}
