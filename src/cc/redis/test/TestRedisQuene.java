package cc.redis.test;

import cc.redis.domain.Message;
import cc.redis.utils.JedisUtil;
import cc.redis.utils.ObjectUtil;

public class TestRedisQuene {
	public static byte[] redisKey = "testkey".getBytes();//设置key

	static {
		init();
	}

	public static void main(String[] args) {
		pop();
	}

	private static void pop() {
		long length = JedisUtil.llen(redisKey);
		Message msg;
		try {
			if(length > 0) {
				for(int i=0; i<length; i++) {
					byte[] bytes = JedisUtil.rpop(redisKey);
					msg = (Message) ObjectUtil.bytesToObject(bytes);
					if (msg != null) {
						System.out.println(msg.getId() + "   " + msg.getContent());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void init() {
		try {
			Message msg1 = new Message(1, "内容1");
			JedisUtil.lpush(redisKey, ObjectUtil.objectToBytes(msg1));
			Message msg2 = new Message(2, "内容2");
			JedisUtil.lpush(redisKey, ObjectUtil.objectToBytes(msg2));
			Message msg3 = new Message(3, "内容3");
			JedisUtil.lpush(redisKey, ObjectUtil.objectToBytes(msg3));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
