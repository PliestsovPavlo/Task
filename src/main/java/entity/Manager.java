package entity;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import helpers.json.JsonHelper;
import helpers.json.JsonHelperImpl;
import helpers.redis.RedisHelper;
import helpers.redis.RedisHelperImpl;
import rmi.RemoteAdaptor;
import rmi.RemoteManager;

public class Manager implements RemoteManager, Serializable {

	private static final long serialVersionUID = -7780899613497451911L;
	private static final String key = "aaa";
	private int[] ports = { 2097, 2098 };
	private String ip = "localhost";
	private RedisHelper redis = new RedisHelperImpl();
	private List<Adaptor> adaptors = new ArrayList<>();
	private Adaptor adaptor;
	private RemoteAdaptor remoteAdaptor;

	public Manager() throws InterruptedException, NotBoundException, JsonParseException, JsonMappingException,
			ExecutionException, IOException {
		//create & register manager
		Registry registry = LocateRegistry.createRegistry(2099);
		RemoteManager remoteManager = (RemoteManager) UnicastRemoteObject.exportObject(this, 0);
		registry.rebind("manager", remoteManager);
		connectAdaptors();

	}

	private void connectAdaptors() throws InterruptedException, NotBoundException, JsonParseException,
			JsonMappingException, ExecutionException, IOException {
		
		//if adaptor not registered
		notifyManager();
		this.doJob();
	}

	public void doJob()
			throws InterruptedException, ExecutionException, JsonParseException, JsonMappingException, IOException {

		while (true) {
			Job job = pullJob();
			if (job != null) {
				adaptor = getAvailableAdaptor(adaptors);
				if (adaptor != null) {
					synchronized (adaptor) {
						adaptor.doWork(job);
					}
				} else {
					pushJob(job);
					System.out.println("job pushed back to redis and manager wait");
					synchronized (this) {
						System.out.println("Manager waiting");
						this.wait();
					}
				}
			} else {
				System.out.println("no jobs in queue");
				Thread.sleep(2000);
			}
		}
	}

	public void pushJob(Job job) throws JsonProcessingException {
		JsonHelper jsonHelper = new JsonHelperImpl();
		String jobString = jsonHelper.serialize(job);
		this.redis.lpush(key, jobString);
	}

	private Job pullJob() throws JsonParseException, JsonMappingException, IOException {
		String str = this.redis.lpop(key);
		JsonHelper jsonHelper = new JsonHelperImpl();
		Job job = new Job();
		job = (Job) jsonHelper.deserialize(str, job);
		return job;
	}

	private Adaptor getAvailableAdaptor(List<Adaptor> adaptors) {
		for (Adaptor adaptor : adaptors) {
			for (Worker worker : adaptor.getWorkers()) {
				if (!worker.isBuzy() && worker.getLimitCalls() > 0) {
					return adaptor;
				}
			}
		}
		System.out.println("no adaptor available");
		return null;
	}

	@Override
	public Manager takeManager() throws InterruptedException, NotBoundException, JsonParseException,
			JsonMappingException, ExecutionException, IOException {
		return new Manager();
	}

	@Override
	public void notifyManager() throws RemoteException, NotBoundException, InterruptedException {
		for (int port : this.ports) {
			try {
				Registry registry = LocateRegistry.getRegistry(ip, port);
				remoteAdaptor = (RemoteAdaptor) registry.lookup("adaptor");
				synchronized (remoteAdaptor) {
					remoteAdaptor.notify();
					adaptor = remoteAdaptor.takeAdaptor();
					this.adaptors.add(adaptor);
					System.out.println(adaptor);
				}
//				this.addAdaptor(adaptor);
				System.out.println("adap added");
				synchronized (remoteAdaptor) {
					remoteAdaptor.notify();
				}
			} catch (Exception e) {
				System.err.println(e.toString());
			}
		}
	}

	@Override
	public void addAdaptor(Adaptor adp) throws RemoteException, InterruptedException {
//		Adaptor a = adp.takeAdaptor();
		adaptors.add(adp);
	}
}
