package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

import entity.Adaptor;
import entity.Manager;

public interface RemoteAdaptor extends Remote {

	public void notifyAdaptor() throws RemoteException;
	public void setManager(Manager manager) throws RemoteException;
	
	public Adaptor takeAdaptor()throws RemoteException, InterruptedException;
		
}
