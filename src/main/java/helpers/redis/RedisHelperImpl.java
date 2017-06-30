package helpers.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisHelperImpl implements RedisHelper {

	private static JedisPool pool;
	
	public RedisHelperImpl(String host, int port){
		this.pool = new JedisPool(host, port);
	}
	
	public RedisHelperImpl(){
		this.pool = new JedisPool("localhost");
	}
	
	private Jedis getResource(){
		if(this.pool == null) pool = new JedisPool("localhost");
		return this.pool.getResource();
	}
	
	
	@Override
	public void lpush(String  key, String value){
		Jedis jedis = getResource();
		jedis.lpush(key, value);
		jedis.close();
	}

	@Override
	public String lpop(String key) {
		Jedis jedis = getResource();
		String value = jedis.lpop(key);
		//here we always make sure to call close.. to prevent connection leaks..
		jedis.close();
		return value;
	}

}
