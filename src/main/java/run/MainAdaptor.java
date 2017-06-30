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
		Adaptor adaptor = new Adaptor();
	}

}
