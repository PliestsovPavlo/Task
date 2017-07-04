package run;

import java.rmi.RemoteException;

public class AllInOne {
	public static void main(String[] args) throws RemoteException, InterruptedException{
		new MainAdaptor().main(args);
		new MainManager().main(args);
		new MainAdaptor2().main(args);
	}

}
