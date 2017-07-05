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

		new Thread(ElasticHandlerImpl.getInstance()).run();
		new Thread(ElasticHandlerImpl.getInstance()).run();
		

		
	}

}
