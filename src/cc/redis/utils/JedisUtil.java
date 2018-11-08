package cc.redis.utils;

import java.util.List;
import java.util.Map;
import java.util.Set;

import cc.modules.util.PropertiesFileReader;
import cc.modules.util.StringUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Jedis工具类，
 * @author StevenWang
 *
 */
public class JedisUtil {
	
	private static String PASSWORD = PropertiesFileReader.get("redis.password","/redis.properties");
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
 
    private static JedisPool jedisPool;
 
    static {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxActive(MAX_ACTIVE);
        config.setMaxIdle(MAX_IDLE);
        config.setMaxWait(MAX_WAIT);
        config.setMinIdle(MIN_IDLE);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
        config.setTestWhileIdle(true);
        config.setMinEvictableIdleTimeMillis(60000l);
        config.setTimeBetweenEvictionRunsMillis(3000l);
        config.setNumTestsPerEvictionRun(-1);
        if(StringUtil.isBlank(PASSWORD))
        	jedisPool = new JedisPool(config, HOST, PORT, 60000);
        else
        	jedisPool = new JedisPool(config, HOST, PORT, 60000, PASSWORD);
    }
    
    public static Jedis getJedis() {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
        } catch (Exception e) {
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        }
        return jedis;
    }
 
    /**
     * 获取数据
     * @param key
     * @return
     */
    public static String get(String key) {
 
        String value = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            value = jedis.get(key);
        } catch (Exception e) {
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            close(jedis);
        }
 
        return value;
    }
 
    public static void close(Jedis jedis) {
        try {
            jedisPool.returnResource(jedis);
 
        } catch (Exception e) {
            if (jedis.isConnected()) {
                jedis.quit();
                jedis.disconnect();
            }
        }
    }
 
    /**
     * 获取数据
     * 
     * @param key
     * @return
     */
    public static byte[] get(byte[] key) {
 
        byte[] value = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            value = jedis.get(key);
        } catch (Exception e) {
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            close(jedis);
        }
 
        return value;
    }
    
    public static void set(String key, String value) {
    	 
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.set(key, value);
        } catch (Exception e) {
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            close(jedis);
        }
    }
 
    public static void set(byte[] key, byte[] value) {
 
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.set(key, value);
        } catch (Exception e) {
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            close(jedis);
        }
    }
 
    public static void set(byte[] key, byte[] value, int time) {
 
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.set(key, value);
            jedis.expire(key, time);
        } catch (Exception e) {
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            close(jedis);
        }
    }
 
    public static void hset(byte[] key, byte[] field, byte[] value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.hset(key, field, value);
        } catch (Exception e) {
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            close(jedis);
        }
    }
 
    public static void hset(String key, String field, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.hset(key, field, value);
        } catch (Exception e) {
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            close(jedis);
        }
    }
 
    /**
     * 获取数据
     * 
     * @param key
     * @return
     */
    public static String hget(String key, String field) {
 
        String value = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            value = jedis.hget(key, field);
        } catch (Exception e) {
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            close(jedis);
        }
 
        return value;
    }
 
    /**
     * 获取数据
     * 
     * @param key
     * @return
     */
    public static byte[] hget(byte[] key, byte[] field) {
 
        byte[] value = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            value = jedis.hget(key, field);
        } catch (Exception e) {
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            close(jedis);
        }
 
        return value;
    }
 
    public static void hdel(byte[] key, byte[] field) {
 
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.hdel(key, field);
        } catch (Exception e) {
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            close(jedis);
        }
    }
    
    public static void hdel(String key, String field) {
    	 
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.hdel(key, field);
        } catch (Exception e) {
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            close(jedis);
        }
    }
 
    /**
     * 存储REDIS队列 顺序存储
     * @param byte[] key reids键名
     * @param byte[] value 键值
     */
    public static void lpush(byte[] key, byte[] value) {
 
        Jedis jedis = null;
        try {
 
            jedis = jedisPool.getResource();
            jedis.lpush(key, value);
 
        } catch (Exception e) {
 
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
 
        } finally {
 
            //返还到连接池
            close(jedis);
 
        }
    }
 
    /**
     * 存储REDIS队列 反向存储
     * @param byte[] key reids键名
     * @param byte[] value 键值
     */
    public static void rpush(byte[] key, byte[] value) {
 
        Jedis jedis = null;
        try {
 
            jedis = jedisPool.getResource();
            jedis.rpush(key, value);
 
        } catch (Exception e) {
 
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
 
        } finally {
 
            //返还到连接池
            close(jedis);
 
        }
    }
 
    /**
     * 将列表 source 中的最后一个元素(尾元素)弹出，并返回给客户端
     * @param byte[] key reids键名
     * @param byte[] value 键值
     */
    public static void rpoplpush(byte[] key, byte[] destination) {
 
        Jedis jedis = null;
        try {
 
            jedis = jedisPool.getResource();
            jedis.rpoplpush(key, destination);
 
        } catch (Exception e) {
 
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
 
        } finally {
 
            //返还到连接池
            close(jedis);
 
        }
    }
 
    /**
     * 获取队列数据
     * @param byte[] key 键名
     * @return
     */
    public static List<byte[]> lpopList(byte[] key) {
 
        List<byte[]> list = null;
        Jedis jedis = null;
        try {
 
            jedis = jedisPool.getResource();
            list = jedis.lrange(key, 0, -1);
 
        } catch (Exception e) {
 
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
 
        } finally {
 
            //返还到连接池
            close(jedis);
 
        }
        return list;
    }
 
    /**
     * 获取队列数据
     * @param byte[] key 键名
     * @return
     */
    public static byte[] rpop(byte[] key) {
 
        byte[] bytes = null;
        Jedis jedis = null;
        try {
 
            jedis = jedisPool.getResource();
            bytes = jedis.rpop(key);
 
        } catch (Exception e) {
 
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
 
        } finally {
 
            //返还到连接池
            close(jedis);
 
        }
        return bytes;
    }
 
    public static void hmset(Object key, Map<String, String> hash) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.hmset(key.toString(), hash);
        } catch (Exception e) {
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
 
        } finally {
            //返还到连接池
            close(jedis);
 
        }
    }
 
    public static void hmset(Object key, Map<String, String> hash, int time) {
        Jedis jedis = null;
        try {
 
            jedis = jedisPool.getResource();
            jedis.hmset(key.toString(), hash);
            jedis.expire(key.toString(), time);
        } catch (Exception e) {
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
 
        } finally {
            //返还到连接池
            close(jedis);
 
        }
    }
 
    public static List<String> hmget(Object key, String... fields) {
        List<String> result = null;
        Jedis jedis = null;
        try {
 
            jedis = jedisPool.getResource();
            result = jedis.hmget(key.toString(), fields);
 
        } catch (Exception e) {
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
 
        } finally {
            //返还到连接池
            close(jedis);
 
        }
        return result;
    }
 
    public static Set<String> hkeys(String key) {
        Set<String> result = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.hkeys(key);
 
        } catch (Exception e) {
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
 
        } finally {
            //返还到连接池
            close(jedis);
 
        }
        return result;
    }
 
    public static List<byte[]> lrange(byte[] key, int from, int to) {
        List<byte[]> result = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.lrange(key, from, to);
 
        } catch (Exception e) {
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
 
        } finally {
            //返还到连接池
            close(jedis);
 
        }
        return result;
    }
 
    public static Map<byte[],byte[]> hgetAll(byte[] key) {
        Map<byte[],byte[]> result = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.hgetAll(key);
        } catch (Exception e) {
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
 
        } finally {
            //返还到连接池
            close(jedis);
        }
        return result;
    }
 
    public static void del(byte[] key) {
 
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.del(key);
        } catch (Exception e) {
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            close(jedis);
        }
    }
    
    public static void del(String key) {
    	 
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.del(key);
        } catch (Exception e) {
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            close(jedis);
        }
    }
 
    public static long llen(byte[] key) {
 
        long len = 0;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            len = jedis.llen(key);
        } catch (Exception e) {
            //释放redis对象
            jedisPool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            close(jedis);
        }
        return len;
    }

}
