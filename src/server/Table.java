package server;

import java.util.Vector;
import java.util.concurrent.locks.ReentrantLock;

public class Table {
	private Vector<ReentrantLock> forks = new Vector<ReentrantLock>();
	private int size = 0;
	
	public int getSize() {
		return this.size;
	}
	
	public Table(int size) {
		for(int i = 0; i < size; i++) {
			this.forks.add(new ReentrantLock());
		}
		this.size = size;
	}
	
	public synchronized void getForks(int leftFork, int rightFork) {	
		ReentrantLock lf = forks.get(leftFork);
		ReentrantLock rf = forks.get(rightFork);
		
		while(true){
			boolean getBothForks = false;
			
			if(lf.tryLock())
			{
				if (rf.tryLock()) {
					getBothForks = true;
				} else {
					lf.unlock();
				}
			}
			
			if(getBothForks) {
				break;
			}
			
			try { 
				wait(); 
			} catch(Exception e) {
				e.printStackTrace();
				return;
			}
		}
	}
	
	public synchronized void releaseForks(int leftFork, int rightFork) {		
		ReentrantLock lf = forks.get(leftFork);
		ReentrantLock rf = forks.get(rightFork);
		lf.unlock();
		rf.unlock();
		notifyAll();
	}
}
