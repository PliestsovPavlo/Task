package run;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.concurrent.ExecutionException;

import entity.Adaptor;
import entity.Manager;

public class MainManager {

	public static void main(String[] args) throws RemoteException, InterruptedException {
		Manager man = new Manager();
	}
}