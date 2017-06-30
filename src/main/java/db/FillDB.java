package db;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import entity.Job;
import helpers.json.JsonHelper;
import helpers.json.JsonHelperImpl;
import redis.clients.jedis.Jedis;

public class FillDB {
	public static void main(String[] args) throws IOException, InterruptedException, NotBoundException, ExecutionException {
		JsonHelper handler = new JsonHelperImpl();
		Set<String> apis = new HashSet<>();
		apis.add("google");
		apis.add("yandex");
		apis.add("api");

		Jedis jedis = new Jedis();
		

		for (int i = 0; i < 100; i++) {

			Job job = new Job(i + 1, "message", apis);
			jedis.lpush("aaa", handler.serialize(job));
		}
		jedis.close();
		 

		System.out.println("ok");
	}

}
