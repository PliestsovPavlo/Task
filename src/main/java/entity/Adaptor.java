package entity;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import rmi.RemoteAdaptor;
import rmi.RemoteManager;

public class Adaptor implements RemoteAdaptor, Serializable {

	private static final long serialVersionUID = 1L;
	private List<Worker> workers = new ArrayList<>();
	private Worker worker;
	private Job job;
	private	Registry registry;
	private RemoteAdaptor remoteAdaptor;
	private RemoteManager manager;
	private ExecutorService pool = Executors.newFixedThreadPool(3);

	private AtomicInteger workerCount = new AtomicInteger(0);

	@Override
	public void doWork(Job job) {
		System.out.println("Now inside adaptor and will launch a worker..");
		if(pool == null) pool = Executors.newFixedThreadPool(3);
		pool.execute(new Worker(this, job));
	}

	private void connectAdaptor() throws RemoteException
	{
		registry = LocateRegistry.createRegistry(2097);
		remoteAdaptor = (RemoteAdaptor) UnicastRemoteObject.exportObject(this, 0);
		registry.rebind("adaptor", remoteAdaptor);
		System.out.println("adaptor registered");
		try{
			registry = LocateRegistry.getRegistry(2099);
			Manager remoteManager = (Manager) registry.lookup("manager");
			this.manager = remoteManager;
			remoteManager.notify();
			System.out.println("notif");
			//				this.setManager(remoteManager.takeManager());
			remoteManager.addAdaptor(this);
		}catch (Exception e) {
			System.err.println(e.toString());
		}
	}
	
	public Adaptor() throws RemoteException, InterruptedException {
		connectAdaptor();
	}

	private List<Worker> generateWorkers(Adaptor a) {
		for (int i = 0; i < 3; i++) {
			workers.add(new Worker(this));
		}
		return workers;
	}

	public List<Worker> getWorkers() {
		return workers;
	}

	@Override
	public void notifyAdaptor() throws RemoteException {
		synchronized (this) {
			this.notify();
		}		
	}

	@Override
	public void setManager(RemoteManager manager) throws RemoteException {
		this.manager = manager;
	}

	@Override
	public int getNumberOfWorkers() throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean hasAvailableWorkers() throws RemoteException {
		int calls = workerCount.get();
		System.out.println("Checking available calls... " + calls);
		return (calls < 3);
	}

	public void returnJobToManager(Job job)
	{
		try {
			System.out.println("On manager side..");
			this.manager.jobExecuted(job);
			System.out.println("Decrementing Worker Count..");
			this.decrementWorkersCount();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public int incrementWorkersCount()
	{
		System.out.println("Incrementing calls");
		return this.workerCount.incrementAndGet();
	}
	
	@Override
	public int decrementWorkersCount()
	{
		System.out.println("Decrementing calls");
		return this.workerCount.decrementAndGet();
	}
	

}
