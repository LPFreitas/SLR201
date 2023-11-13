package local;

import java.io.File;
import java.util.Vector;

import utils.Logger;

public class DiningPhilosophers {
	
	private static Table table = null;

	public static void main(String[] args) throws Exception {
		
		int nPhilosophers = 0;
    	Logger logger = null;
    	
    	nPhilosophers = Integer.parseInt(args[0]);
    	
    	if (args.length == 1) {
    		logger = new Logger(System.out);
    	} else if (args.length == 2) {
    		logger = new Logger(new File(args[1]));
    	}
		
		table = new Table(nPhilosophers);
		
		Vector<Philosopher> philosophers = new Vector<Philosopher>();
		
		int leftFork = 0;
		int rightFork = 0;
		for (int i = 0; i < nPhilosophers; i++) {
			if (i == nPhilosophers - 1) {
				rightFork = i;
				leftFork = (i + 1) % nPhilosophers;
			} else {
				leftFork = i;
				rightFork = (i + 1) % nPhilosophers;
			}
			Philosopher philosopher = new Philosopher(i, leftFork, rightFork, table, logger);
			philosopher.start();
			philosophers.add(philosopher);
		}
		
		for(int i = 0; i < nPhilosophers; i++) {
			try {
				philosophers.get(i).join();
			} catch(Exception e) {}
		}
		
		for(int i = 0; i < nPhilosophers; i++) {
			Philosopher philosopher = philosophers.get(i);
			logger.log("Philosopher " + philosopher.getNumber() + " has eaten " + philosopher.getTimesEating() + " times\n");
		}
	}
}