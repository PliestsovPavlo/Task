package run;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ExecutionException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import entity.Adaptor;
import entity.Manager;
import entity.Worker;
import rmi.RemoteAdaptor;
import rmi.RemoteManager;

public class MainAdaptor {

	public static void main(String[] args) throws RemoteException, InterruptedException {

//		Adaptor ad;
//		Registry registry;
//		RemoteManager remoteManager;
//		RemoteAdaptor remoteAdaptor;
//		
//		registry = LocateRegistry.createRegistry(2097);
//		ad = new Adaptor();
//
//		System.out.println("Adaptor registered");
//
//		remoteAdaptor = (RemoteAdaptor) UnicastRemoteObject.exportObject(ad, 0);
//		registry.rebind("adaptor", remoteAdaptor);
//		try {
//			registry = LocateRegistry.getRegistry(2099);
//			remoteManager = (RemoteManager) registry.lookup("manager");
//				
//			synchronized (remoteManager) {
//				System.out.println("Manager NOTIFIED");
//				remoteManager.notifyManager();
//			}
//						
//		} catch (Exception e) {
//			System.out.println("Adaptor waiting... ");
//			synchronized (remoteAdaptor) {
//				remoteAdaptor.wait();
//			}
//		}
		Adaptor adaptor = new Adaptor();
	}

}
