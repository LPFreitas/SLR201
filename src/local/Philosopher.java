package local;

import java.util.Random;

import utils.Logger;

public class Philosopher extends Thread {
	
	private int n = 0;
	private int timesEating = 0;
	private int leftFork = 0;
	private int rightFork = 0;
	private Table table = null;
	private Logger logger = null;

	Philosopher(int n, int leftFork, int rightFork, Table table, Logger logger) {
		this.n = n;
		this.leftFork = leftFork;
		this.rightFork = rightFork;
		this.timesEating = 0;
		this.table = table;
		this.logger = logger;
	}
	
	private void log(String message) {
		this.logger.log("Philosopher " + this.n + " " + message + "\n");
	}
	
	public int getNumber() {
		return this.n;
	}
	
	public int getTimesEating() {
		return this.timesEating;
	}
	
	public void run() {
		int eatTime = 0;
		int thinkTime = 0;
		
		while(true) {			
			thinkTime = new Random().nextInt(256);
			this.log("thinks for " + thinkTime + " ms");
			try {
				Thread.sleep(thinkTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.log("is hungry");
			this.log("wants to eat with forks " + this.leftFork + " and " + this.rightFork);
			this.table.getForks(this.leftFork, this.rightFork);
			eatTime = new Random().nextInt(256);
			this.log("eats for " + Integer.toString(eatTime) + " ms");
			try {
				Thread.sleep(eatTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.timesEating++;
			this.log("has eaten " + timesEating + " times");
			this.table.releaseForks(this.leftFork, this.rightFork);
			this.log("releases forks " + this.leftFork + " and " + this.rightFork);			
		}
	}
}
