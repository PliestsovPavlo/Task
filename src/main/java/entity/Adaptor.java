package entity;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import rmi.RemoteAdaptor;
import rmi.RemoteManager;

public class Adaptor implements RemoteAdaptor, Serializable, Runnable {

	private static final long serialVersionUID = 1L;
	private List<Worker> workers = new ArrayList<>();
	private Worker worker;
	private Job job;
	private	Registry registry;
	private RemoteAdaptor remoteAdaptor;


	public void doWork(Job job) throws InterruptedException {
		runWorkers(workers);
		worker = getAvaliableWorker(workers);
		if (worker != null) {
			synchronized (worker) {
				worker.setJob(job);
			}
		} else {
			System.out.println("no aval worker");
		}
	}

	public void runWorkers(List<Worker> workers) throws InterruptedException {
		for (Worker worker : workers) {
			if (worker != null) {
				new Thread(worker).start();
			}
		}
	}

	public Worker getAvaliableWorker(List<Worker> list) {
		for (Worker worker2 : list) {
			if (!worker2.isBuzy() && worker2.getLimitCalls() > 0)
				return worker2;
		}
		return null;
	}

	public Adaptor() throws RemoteException, InterruptedException {
		registry = LocateRegistry.createRegistry(2097);
		remoteAdaptor = (RemoteAdaptor) UnicastRemoteObject.exportObject(this, 0);
		registry.rebind("adaptor", remoteAdaptor);
		System.out.println("adaptor registered");
		generateWorkers(this);
		runWorkers(workers);
		try{
			registry = LocateRegistry.getRegistry(2099);
			RemoteManager remoteManager = (RemoteManager) registry.lookup("manager");
			synchronized (remoteManager) {
				remoteManager.notify();
				System.out.println("notif");
//				this.setManager(remoteManager.takeManager());
				
				remoteManager.addAdaptor(this);
			}
		}catch (Exception e) {
			System.err.println(e.toString());
			synchronized (this) {
				System.out.println("wait.........");
				this.wait();
			}
		}
	}

	private List<Worker> generateWorkers(Adaptor a) {
		for (int i = 0; i < 3; i++) {
			workers.add(new Worker(this));
		}
		return workers;
	}

	@Override
	public Adaptor takeAdaptor() throws RemoteException, InterruptedException {
		return this;
	}

	public List<Worker> getWorkers() {
		return workers;
	}

	@Override
	public void run() {
		try {
			doWork(job);
		} catch (InterruptedException e) {
			System.err.println(e.toString());
		}
	}

	@Override
	public void notifyAdaptor() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setManager(Manager manager) throws RemoteException {
		this.setManager(manager);
	}

}
