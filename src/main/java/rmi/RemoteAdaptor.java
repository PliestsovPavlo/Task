package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

import entity.Adaptor;
import entity.Job;
import entity.Manager;

public interface RemoteAdaptor extends Remote {
	
	public void notifyAdaptor() throws RemoteException;
	public void setManager(RemoteManager manager) throws RemoteException;
	public int getNumberOfWorkers() throws RemoteException;
	public boolean hasAvailableWorkers() throws RemoteException;
	public void doWork(Job job) throws RemoteException;
	public int incrementWorkersCount() throws RemoteException;
	public int decrementWorkersCount() throws RemoteException;	
}
