package db;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import entity.Job;
import entity.Manager;
import helpers.redis.RedisHelperImpl;

public class FillDB {
	public static void main(String[] args) throws IOException, InterruptedException, NotBoundException, ExecutionException {

		// apis set
		Set<String> apis = new HashSet<>();
		apis.add("google");
		apis.add("yandex");
		apis.add("api");

		Manager manager = new Manager();

		for (int i = 0; i < 100; i++) {

			Job job = new Job(i + 1, "message", apis);
			manager.pushJob(job);

		}
		 

		System.out.println("ok");
	}

}
