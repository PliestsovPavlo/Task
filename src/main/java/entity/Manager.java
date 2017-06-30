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
	private List<RemoteAdaptor> adaptors = new ArrayList<>();

	public Manager()
	{
		//create & register manager
		bindSelf();
		connectAdaptors();
		this.doJob();
	}

	private boolean bindSelf()
	{
		Registry registry;
		try 
		{
			registry = LocateRegistry.createRegistry(2099);
			RemoteManager remoteManager = (RemoteManager) UnicastRemoteObject.exportObject(this, 0);
			registry.rebind("manager", remoteManager);
			return true;
		} catch (RemoteException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	private void connectAdaptors()
	{
		for (int port : this.ports) 
		{
			try 
			{
				Registry registry = LocateRegistry.getRegistry(ip, port);
				RemoteAdaptor remoteAdaptor = (RemoteAdaptor) registry.lookup("adaptor");
				remoteAdaptor.setManager(this);
				remoteAdaptor.notifyAdaptor();

				System.out.println(remoteAdaptor);
//				this.adaptors.add(adaptor);
				this.addAdaptor(remoteAdaptor);
				System.out.println("adap added");
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public void doJob(){

		while (true) {
			Job job = pullJob();
			if (job != null) {
				RemoteAdaptor adaptor = getAvailableAdaptor();
				if (adaptor != null) {
					try {
						adaptor.doWork(job);
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					pushJob(job);
					System.out.println("job pushed back to redis and manager wait");
					synchronized (this) {
						try {
							this.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			} else {
				System.out.println("no jobs in queue");
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public void pushJob(Job job){
		JsonHelper jsonHelper = new JsonHelperImpl();
		String jobString;
		try {
			jobString = jsonHelper.serialize(job);
			this.redis.lpush(key, jobString);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Job pullJob(){
		String str = this.redis.lpop(key);
		JsonHelper jsonHelper = new JsonHelperImpl();
		Job job = new Job();
		try {
			job = (Job) jsonHelper.deserialize(str, job);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return job;
	}

	private RemoteAdaptor getAvailableAdaptor() {
		for (RemoteAdaptor adaptor : this.adaptors) {
			try {
				if(adaptor.hasAvailableWorkers()) return adaptor;
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("no adaptor available");
		return null;
	}

	@Override
	public void addAdaptor(RemoteAdaptor adp) throws RemoteException, InterruptedException {
//		Adaptor a = adp.takeAdaptor();
		adaptors.add(adp);
		synchronized (this) {
			this.notify();
		}
	}

	@Override
	public void jobExecuted(Job job) throws RemoteException
	{
		
	}
}
