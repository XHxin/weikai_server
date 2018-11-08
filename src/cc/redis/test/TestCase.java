package cc.redis.test;

import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.SortingParams;
import cc.redis.utils.RedisUtil;

public class TestCase {

	public static void main(String[] args) {

		// Hello();
		// testSort5();
		testSort2();
	}

	public static void Hello() {
		Jedis jedis = RedisUtil.getJedis();
		try {
			// 向key-->name中放入了value-->minxr
			jedis.set("name", "minxr");
			String ss = jedis.get("name");
			System.out.println(ss);

			// 很直观，类似map 将jintao append到已经有的value之后
			jedis.append("name", "jintao");
			ss = jedis.get("name");
			System.out.println(ss);

			// 2、直接覆盖原来的数据
			jedis.set("name", "jintao");
			System.out.println(jedis.get("jintao"));

			// 删除key对应的记录
			jedis.del("name");
			System.out.println(jedis.get("name"));// 执行结果：null

			/**
			 * mset相当于 jedis.set("name","minxr"); jedis.set("jarorwar","aaa");
			 */
			jedis.mset("name", "minxr", "jarorwar", "aaa");
			System.out.println(jedis.mget("name", "jarorwar"));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			RedisUtil.getPool().returnResource(jedis);
		}

	}

	public static void testSort5() {
		// 排序默认以数字作为对象，值被解释为双精度浮点数，然后进行比较
		Jedis jedis = RedisUtil.getJedis();
		// 一般SORT用法 最简单的SORT使用方法是SORT key。
		jedis.lpush("mylist", "1");
		jedis.lpush("mylist", "4");
		jedis.lpush("mylist", "6");
		jedis.lpush("mylist", "3");
		jedis.lpush("mylist", "0");
		// List<String> list = redis.sort("sort");// 默认是升序
		SortingParams sortingParameters = new SortingParams();
		sortingParameters.desc();
		// sortingParameters.alpha();//当数据集中保存的是字符串值时，你可以用 ALPHA
		// 修饰符(modifier)进行排序。
		// sortingParameters.limit(0, 2);//可用于分页查询

		// 没有使用 STORE 参数，返回列表形式的排序结果. 使用 STORE 参数，返回排序结果的元素数量。

		jedis.sort("mylist", sortingParameters, "mylist");// 排序后指定排序结果到一个KEY中，这里讲结果覆盖原来的KEY

		List<String> list = jedis.lrange("mylist", 0, -1);
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}

		jedis.sadd("tom:friend:list", "123"); // tom的好友列表
		jedis.sadd("tom:friend:list", "456");
		jedis.sadd("tom:friend:list", "789");
		jedis.sadd("tom:friend:list", "101");

		jedis.set("score:uid:123", "1000"); // 好友对应的成绩
		jedis.set("score:uid:456", "6000");
		jedis.set("score:uid:789", "100");
		jedis.set("score:uid:101", "5999");

		jedis.set("uid:123", "{'uid':123,'name':'lucy'}"); // 好友的详细信息
		jedis.set("uid:456", "{'uid':456,'name':'jack'}");
		jedis.set("uid:789", "{'uid':789,'name':'jay'}");
		jedis.set("uid:101", "{'uid':101,'name':'jolin'}");

		sortingParameters = new SortingParams();
		// sortingParameters.desc();
		sortingParameters.get("#");// GET 还有一个特殊的规则—— "GET #"
		// ，用于获取被排序对象(我们这里的例子是 user_id )的当前元素。
		sortingParameters.by("score:uid:*");
		jedis.sort("tom:friend:list", sortingParameters, "tom:friend:list");
		List<String> result = jedis.lrange("tom:friend:list", 0, -1);
		for (String item : result) {
			System.out.println("item..." + item);
		}

		jedis.flushDB();
		RedisUtil.getPool().returnResource(jedis);
	}

	public static void testSort2() {
		Jedis jedis = RedisUtil.getJedis();
		jedis.del("user:66", "user:55", "user:33", "user:22", "user:11",
				"userlist");
		jedis.lpush("userlist", "33");
		jedis.lpush("userlist", "22");
		jedis.lpush("userlist", "55");
		jedis.lpush("userlist", "11");

		jedis.hset("user:66", "name", "66");
		jedis.hset("user:55", "name", "55");
		jedis.hset("user:33", "name", "33");
		jedis.hset("user:22", "name", "79");
		jedis.hset("user:11", "name", "24");
		jedis.hset("user:66", "age", "66");
		jedis.hset("user:55", "age", "55");
		jedis.hset("user:33", "age", "33");
		jedis.hset("user:22", "age", "79");
		jedis.hset("user:11", "age", "24");
		jedis.hset("user:11", "add", "beijing");
		jedis.hset("user:22", "add", "shanghai");
		jedis.hset("user:33", "add", "guangzhou");
		jedis.hset("user:55", "add", "chongqing");
		jedis.hset("user:66", "add", "xi'an");

		SortingParams sortingParameters = new SortingParams();
		// 符号 "->" 用于分割哈希表的键名(key name)和索引域(hash field)，格式为 "key->field" 。
		sortingParameters.get("user:*->name");
		sortingParameters.get("user:*->add");
		// sortingParameters.by("user:*->name");
		// sortingParameters.get("#");
		List<String> result = jedis.sort("userlist", sortingParameters);
		for (String item : result) {
			System.out.println("item...." + item);
		}
		System.out.println(jedis.hget("user:11", "add"));
		/**
		 * 对应的redis客户端命令是：sort ml get user*->name sort ml get user:*->name get
		 * user:*->add
		 */
	}

}
