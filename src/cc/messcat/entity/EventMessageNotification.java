package cc.messcat.entity;

/**
 * 腾讯云回调时回传的参数封装类
 * @author HASEE
 *
 */
public class EventMessageNotification {

	private String t;//有效时间-	UNIX时间戳(十进制)
	private String sign;//安全签名-MD5(KEY+t)
	private int event_type;//事件类型-目前可能值为：(1)推流 (0)断流(100)新录制文件
	private String stream_id;//直播码-标示事件源于哪一条直播流
	private String channel_id;//直播码-同stream_id
	private String video_id;//vid-点播用vid，在点播平台可以唯一定位一个点播视频文件
	private String video_url;//下载地址-点播视频的下载地址
	private String file_size;//文件大小-
	private int start_time;//分片开始时间戳-开始时间（unix时间戳，由于I帧干扰，暂时不能精确到秒级）
	private int end_time;//分片结束时间戳-结束时间（unix时间戳，由于I帧干扰，暂时不能精确到秒级）
	private String file_id;//file_id
	private String file_format;//文件格式-	flv, hls, mp4
	private int vod2Flag;//是否开启点播2.0-0表示未开启，1表示开启
	private String record_file_id;//录制文件id-点播2.0开启时，才会有这个字段
	private int duration;//推流时长
	private String stream_param;//推流url参数
	private String sequence;//消息序列号，标识一次推流活动，一次推流活动会产生相同序列号的推流和断流消息
	public String getT() {
		return t;
	}
	public void setT(String t) {
		this.t = t;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public int getEvent_type() {
		return event_type;
	}
	public void setEvent_type(int event_type) {
		this.event_type = event_type;
	}
	public String getStream_id() {
		return stream_id;
	}
	public void setStream_id(String stream_id) {
		this.stream_id = stream_id;
	}
	public String getChannel_id() {
		return channel_id;
	}
	public void setChannel_id(String channel_id) {
		this.channel_id = channel_id;
	}
	public String getVideo_id() {
		return video_id;
	}
	public void setVideo_id(String video_id) {
		this.video_id = video_id;
	}
	public String getVideo_url() {
		return video_url;
	}
	public void setVideo_url(String video_url) {
		this.video_url = video_url;
	}
	public String getFile_size() {
		return file_size;
	}
	public void setFile_size(String file_size) {
		this.file_size = file_size;
	}
	public int getStart_time() {
		return start_time;
	}
	public void setStart_time(int start_time) {
		this.start_time = start_time;
	}
	public int getEnd_time() {
		return end_time;
	}
	public void setEnd_time(int end_time) {
		this.end_time = end_time;
	}
	public String getFile_id() {
		return file_id;
	}
	public void setFile_id(String file_id) {
		this.file_id = file_id;
	}
	public String getFile_format() {
		return file_format;
	}
	public void setFile_format(String file_format) {
		this.file_format = file_format;
	}
	public int getVod2Flag() {
		return vod2Flag;
	}
	public void setVod2Flag(int vod2Flag) {
		this.vod2Flag = vod2Flag;
	}
	public String getRecord_file_id() {
		return record_file_id;
	}
	public void setRecord_file_id(String record_file_id) {
		this.record_file_id = record_file_id;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public String getStream_param() {
		return stream_param;
	}
	public void setStream_param(String stream_param) {
		this.stream_param = stream_param;
	}
	public String getSequence() {
		return sequence;
	}
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	
	
}
