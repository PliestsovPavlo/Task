package rmi;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.concurrent.ExecutionException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import entity.Adaptor;
import entity.Job;
import entity.Manager;

public interface RemoteManager extends Remote{

	public void addAdaptor(RemoteAdaptor adp) throws RemoteException, InterruptedException;
	public void jobExecuted(Job job) throws RemoteException;

}
