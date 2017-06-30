package run;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.concurrent.ExecutionException;

import entity.Manager;

public class MainManager {

	public static void main(String[] args) {

		try {
			try {
				Manager man = new Manager();

			} catch (InterruptedException | ExecutionException | IOException e) {
				System.err.println(e.toString());
			}
		} catch (NotBoundException e) {
			System.err.println(e.toString());
		}
	}
}