package cc.redis.test;

import java.util.Date;

import cc.messcat.vo.SysProbeLogVo;
import cc.redis.utils.JedisUtil;
import cc.redis.utils.ObjectUtil;

public class TestSysProbeLog {
	public static byte[] redisKey = "sysProbeLog".getBytes();//设置key

	static {
		//init();
	}

	public static void main(String[] args) {
		pop();
	}

	private static void pop() {
		long length = JedisUtil.llen(redisKey);
		SysProbeLogVo sysProbeLog;
		try {
			if(length > 0) {
				for(int i=0; i<length; i++) {
					byte[] bytes = JedisUtil.rpop(redisKey);
					sysProbeLog = (SysProbeLogVo) ObjectUtil.bytesToObject(bytes);
					if (sysProbeLog != null) {
						System.out.println(sysProbeLog.getProbeEnName() + "   " + sysProbeLog.getReceiveTime());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void init() {
		try {
			Long schoolId = 1L;
			Long studentId = 15331L;
			Long coachId = 1L;
			Long newsId = 2L;
			String probeEnName = "STU_NEWS_SHARE";
			
			SysProbeLogVo sysProbeLog = new SysProbeLogVo();
			sysProbeLog.setStudentId(studentId);
			sysProbeLog.setCoachId(coachId);
			sysProbeLog.setNewsId(newsId);
			sysProbeLog.setSchoolId(schoolId);
			sysProbeLog.setProbeEnName(probeEnName);
			sysProbeLog.setReceiveTime(new Date());
			JedisUtil.lpush(redisKey, ObjectUtil.objectToBytes(sysProbeLog));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
