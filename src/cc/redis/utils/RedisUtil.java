package cc.redis.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import cc.modules.util.PropertiesFileReader;

/**
 * Redis操作接口
 * @author StevenWang
 *
 */
public class RedisUtil {
	private static JedisPool pool = null;
	
	private static String HOST = PropertiesFileReader.get("redis.host","/redis.properties");
	private static int PORT = PropertiesFileReader.getValueInt("redis.port","/redis.properties");
	//控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；  
    //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。 
	private static int MAX_ACTIVE = PropertiesFileReader.getValueInt("max_active","/redis.properties");
	//控制一个pool最多有多少个状态为idle(空闲的)的jedis实例
	private static int MAX_IDLE = PropertiesFileReader.getValueInt("max_idle","/redis.properties");
	//控制一个pool最少有多少个状态为idle(空闲的)的jedis实例
	private static int MIN_IDLE = PropertiesFileReader.getValueInt("min_idle","/redis.properties");
	//表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
	private static int MAX_WAIT = PropertiesFileReader.getValueInt("max_wait","/redis.properties");

	/**
	 * 构建redis连接池
	 * 
	 * @param ip
	 * @param port
	 * @return JedisPool
	 */
	public static JedisPool getPool() {
		if (pool == null) {
			JedisPoolConfig config = new JedisPoolConfig();
			
			config.setMaxActive(MAX_ACTIVE);
			
			config.setMaxIdle(MAX_IDLE);
			
			config.setMinIdle(MIN_IDLE);
			
			config.setMaxWait(MAX_WAIT);
			//
			config.setTestOnBorrow(true);
			//当调用return Object方法时，是否进行有效性检查
			config.setTestOnReturn(true);
			pool = new JedisPool(config, HOST, PORT);
		}
		return pool;
	}

	/**
	 * 返还到连接池
	 * 
	 * @param pool
	 * @param redis
	 */
	public static void returnResource(JedisPool pool, Jedis redis) {
		if (redis != null) {
			pool.returnResource(redis);
		}
	}

	/**
	 * 获取数据
	 * 
	 * @param key
	 * @return
	 */
	public static String get(String key) {
		String value = null;

		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			value = jedis.get(key);
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}

		return value;
	}
	
	/**
	 * 获取Jedis对象
	 * @return
	 */
	public static Jedis getJedis(){
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		}
		return jedis;
	}
	
	
}