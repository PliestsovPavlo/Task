package helpers.redis;

import java.io.Serializable;

public interface RedisHelper extends Serializable {
	public void lpush(String key, String value);
	public String lpop(String key);
}
