package client;

import java.net.Socket;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import utils.Logger;

public class Client {
	
    private static int port = 65000;
    
    public static void main(String args[]) throws IOException {
        
    	int nPhilosophers = 0;
    	Logger logger = null;
    	
    	String host = args[0];
    	nPhilosophers = Integer.parseInt(args[1]);
    	
    	if (args.length == 2) {
    		logger = new Logger(System.out);
    	} else if (args.length == 3) {
    		logger = new Logger(new File(args[2]));
    	}
    	
    	Vector<Philosopher> philosophers = new Vector<Philosopher>();
    	
		Socket socket = null;
		
		try {
			for(int i = 0; i < nPhilosophers; i++) {
				socket = new Socket(host, Client.port);
				Philosopher philosopher = new Philosopher(socket, logger);
				philosopher.start();
				philosophers.add(philosopher);
			}
			for(int i = 0; i < nPhilosophers; i++) {
				philosophers.get(i).join();
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			for(int i = 0; i < nPhilosophers; i++) {
				philosophers.get(i).closeSocket();
			}
		}
		
		for(int i = 0; i < nPhilosophers; i++) {
			Philosopher philosopher = philosophers.get(i);
			logger.log("Philosopher " + philosopher.getNumber() + " has eaten " + philosopher.getNEatTimes() + " times\n");
		}
    }

}
