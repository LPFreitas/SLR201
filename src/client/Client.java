package client;

import java.net.Socket;
import java.net.SocketAddress;
import java.net.InetSocketAddress;
import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Vector;

import utils.Logger;

public class Client {
	
    private static int port = 65000;
    
    public static void main(String args[]) throws IOException {
        
    	int nPhilosophers = 0;
    	Logger logger = null;
    	
    	String host = args[0];
//    	SocketAddress socketAddress = new InetSocketAddress(host, Client.port);
    	nPhilosophers = Integer.parseInt(args[1]);
    	
    	if (args.length == 2) {
    		logger = new Logger(System.out);
    	} else if (args.length == 3) {
    		logger = new Logger(new File(args[2]));
    	}
    	
    	Vector<Philosopher> philosophers = new Vector<Philosopher>();
    	
		Socket socket = null;
//		InetAddress address = null;
//		try {
//			address = InetAddress.getByName("localhost");	
//		} catch(UnknownHostException e) {
//			e.printStackTrace();
//			return;
//		}
		try {
			for(int i = 0; i < nPhilosophers; i++) {
				socket = new Socket(host, Client.port);
				Philosopher philosopher = new Philosopher(socket, logger);
				philosopher.start();
				philosophers.add(philosopher);
			}
		} catch(Exception e) {}
    	
		for(int i = 0; i < nPhilosophers; i++) {
			try {
				philosophers.get(i).join();
			} catch(Exception e) {}
		}
		
		for(int i = 0; i < nPhilosophers; i++) {
			Philosopher philosopher = philosophers.get(i);
			logger.log("Philosopher " + philosopher.getNumber() + " has eaten " + philosopher.getNEatTimes() + " times\n");
		}
    }

}
