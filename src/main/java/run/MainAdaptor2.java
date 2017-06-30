package run;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import entity.Adaptor;
import rmi.RemoteAdaptor;
import rmi.RemoteManager;

public class MainAdaptor2 {

	public static void main(String[] args) throws RemoteException, InterruptedException {

		Adaptor ad;
		Registry registry;
		RemoteManager remoteManager;
		RemoteAdaptor remoteAdaptor;
		
		registry = LocateRegistry.createRegistry(2098);
		ad = new Adaptor();

		System.out.println("Adaptor registered");

		remoteAdaptor = (RemoteAdaptor) UnicastRemoteObject.exportObject(ad, 0);
		registry.rebind("adaptor", remoteAdaptor);
		try {
			registry = LocateRegistry.getRegistry(2099);
			remoteManager = (RemoteManager) registry.lookup("manager");
				
			synchronized (remoteManager) {
				System.out.println("Manager NOTIFIED");
				//remoteManager.notifyManager();
			}
						
		} catch (Exception e) {
			System.out.println("Adaptor waiting... ");
			synchronized (remoteAdaptor) {
				remoteAdaptor.wait();
			}
		}
//		Adaptor adaptor = new Adaptor();
	}

}