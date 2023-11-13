package client;

import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

import utils.Logger;

public class Philosopher extends Thread {
	
	private Socket socket = null;
	private int n = 0;
	private int nEatTimes = 0;
	private DataOutputStream outStream = null;
	private DataInputStream inputStream = null;
	private Logger logger = null;
	
	public Philosopher(Socket socket, Logger logger) throws IOException {
		this.socket = socket;
		
		this.outStream = new DataOutputStream(socket.getOutputStream());
		this.inputStream = new DataInputStream(socket.getInputStream());
		
		this.logger = logger;
		
		String n_str = this.receive();
		try {
			this.n = Integer.valueOf(n_str);	
		} catch(NumberFormatException e) {
			System.err.println("Unable to register, incorrect index number received: " + n_str);
		}
	}
	
	private void send(String message) throws IOException {
		this.outStream.writeUTF(message);
		this.outStream.flush();
	}
	
	private String receive() throws IOException {
		return this.inputStream.readUTF();
	}	
	
	private void log(String message) {
		this.logger.log("Philosopher " + n + " " + message + "\n");
	}
	
	public int getNumber() {
		return this.n;
	}
	
	public int getNEatTimes() {
		return this.nEatTimes;
	}
	
	public void run() {
		int eatTime = 0;
		int thinkTime = 0;
		String message = null;
		
		try {
			message = this.receive();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		while(true) {		
			thinkTime = new Random().nextInt(256);
			this.log("thinks for " + thinkTime + " ms");
			try {
				Thread.sleep(thinkTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
				try {
					socket.close();
				} catch (IOException e1) {}
			}
			this.log("is hungry");
			try {
				this.send("eat request");
				message = this.receive();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
			if(!message.equals("eat permission")) {
				this.log("received a unknown message from server: " + message);
				break;
			}
			eatTime = new Random().nextInt(256);
			this.log("eats for " + Integer.toString(eatTime) + " ms");
			try {
				Thread.sleep(eatTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.log("finishes eating");
			try {
				this.send("finished eating");
				message = this.receive();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
			this.nEatTimes++;
			if(!message.equals("think permission")) {
				this.log("received a unknown message from server: " + message);
				break;
			}
		}
	}
}
