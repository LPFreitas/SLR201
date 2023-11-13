package server;

import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import utils.Logger;

public class ClientHandler extends Thread {
	
	private Socket socket = null;
	private int n = 0;
	private Table table = null;
	private DataOutputStream outStream = null;
	private DataInputStream inputStream = null;
	private Logger logger = null;
	
	public ClientHandler(Socket socket, Table table, int n, Logger logger) throws IOException {
		this.socket = socket;
		this.outStream = new DataOutputStream(socket.getOutputStream());
		this.inputStream = new DataInputStream(socket.getInputStream());
		this.table = table;
		this.n = n;		
		this.logger = logger;
		this.send(Integer.toString(this.n));
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
	
	public void closeSocket() {
		try {
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int getNumber() {
		return this.n;
	}
	
	public void run() {
		String command = null;
		int tableSize = this.table.getSize();
		int leftFork = 0;
		int rightFork = 0;

		if (this.n == this.table.getSize() - 1) {
			rightFork = n;
			leftFork = (n + 1) % tableSize;
		} else {
			leftFork = n;
			rightFork = (n + 1) % tableSize;
		}
		
		while(true) {
			try {
				this.send("start");
				break;
			} catch(IOException e) {
				e.printStackTrace();
				continue;
			}
		}
		
		while(true) {
			try {
				command = this.receive();
			} catch(IOException e) {
				this.table.releaseForks(leftFork, rightFork);
				e.printStackTrace();
				break;
			}
			
			try {
				if(command.equals("eat request")) {
					this.log("wants to eat with forks " + leftFork + " and " + rightFork);
					this.table.getForks(leftFork, rightFork);
					this.log("begins to eat");
					this.send("eat permission");
				} else if (command.equals("finished eating")) {
					this.table.releaseForks(leftFork, rightFork);
					this.log("releases forks " + leftFork + " and " + rightFork);
					this.send("think permission");
				} else if (command.equals("exit")) {
					this.log("exits");
					System.out.println("Philosopher " + this.n + " exits");
					break;
				} else {
					this.log("sends unknown command to server: " + command);
					continue;
				}
			} catch(IOException e) {
				e.printStackTrace();
				break;
			}
		}
	}
	
}
