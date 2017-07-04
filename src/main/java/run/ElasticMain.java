package run;

import java.io.IOException;

import handlers.elastic.ElasticHandler;
import helpers.redis.RedisHelper;
import helpers.redis.RedisHelperImpl;

public class ElasticMain {

	public static void main(String[] args) throws IOException {

		RedisHelper redis = new RedisHelperImpl();
		String json = redis.lpop("aaa");
		
		ElasticHandler elasticHandler = new ElasticHandler();
//		elasticHandler.createIndex("some_index");
		System.out.println("written");
		
		elasticHandler.searchIndex("some_index");
		elasticHandler.addToIndex("some_index", "my_type", "2", json);
		
		elasticHandler.searchById("some_index", "my_type", "3");
		elasticHandler.deleteIndex("some_index", "my_type", "2");
		
		elasticHandler.closeConnection();
	}

}
