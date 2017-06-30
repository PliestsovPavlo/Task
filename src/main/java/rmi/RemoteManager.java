package rmi;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.concurrent.ExecutionException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import entity.Adaptor;
import entity.Manager;

public interface RemoteManager extends Remote{

	public Manager takeManager()throws RemoteException, InterruptedException, NotBoundException, JsonParseException, JsonMappingException, ExecutionException, IOException;	
	public void notifyManager() throws RemoteException, NotBoundException, InterruptedException;
	public void addAdaptor(Adaptor adp) throws RemoteException, InterruptedException;

}
