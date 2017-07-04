package run;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import handlers.elastic.ElasticHandler;
import handlers.elastic.ElasticHandlerImpl;
import helpers.redis.RedisHelper;
import helpers.redis.RedisHelperImpl;

public class ElasticMain {

	public static void main(String[] args) throws IOException {

		RedisHelper redis = new RedisHelperImpl();
		String json = redis.lpop("aaa");
		
		List<String>list = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			list.add(redis.lpop("aaa"));
		}
		
		ElasticHandler elasticHandler = new ElasticHandlerImpl();
		elasticHandler.createIndex("some_index");
		System.out.println("written");
		
		elasticHandler.searchIndex("some_index");
//		elasticHandler.addToIndex("some_index", "test", "6", json);
		
//		for (Integer i = 0; i < 10; i++) {
//			
//			elasticHandler.addToIndex("some_index", "test", i.toString(), json);
//		}
		
		elasticHandler.searchById("some_index", "test", "3");
		elasticHandler.deleteByIndexById("some_index", "test", "2");
//		elasticHandler.deleteIndexByName("some_index");
		
		elasticHandler.closeConnection();
	}

}
