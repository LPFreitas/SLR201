package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import utils.Logger;

public class Server {
    
    private static ServerSocket serverSocket;
    private static int port = 65000;
    
    private static Table table = null;
    
    private static ClientHandler acceptNewPhilosopher(ServerSocket serverSocket, int n, Logger logger) throws IOException {
    	Socket socket = serverSocket.accept();
    	return new ClientHandler(socket, Server.table, n, logger);
    }
    
    public static void main(String args[]) throws IOException {
        
    	int nPhilosophers = 0;
    	Logger logger = null;
    	
    	nPhilosophers = Integer.parseInt(args[0]);
    	
    	if (args.length == 1) {
    		logger = new Logger(System.out);
    	} else if (args.length == 2) {
    		logger = new Logger(new File(args[1]));
    	}
    	
    	InetAddress address = InetAddress.getLocalHost();
		System.out.println("Server on address " + address.toString() + " and on port " + Server.port);
		
		table = new Table(nPhilosophers);
		
		Vector<ClientHandler> handlers = new Vector<ClientHandler>();
		serverSocket = new ServerSocket(Server.port);
		
		try {
			for (int i = 0; i < nPhilosophers; i++) {
				ClientHandler handler = acceptNewPhilosopher(serverSocket, i, logger);
				handlers.add(handler);
				logger.log("Philopher " + i + " has joined the table\n");
			}			
			logger.log("All philosophers at the table\n");
			
			for(int i = 0; i < nPhilosophers; i++) {
				handlers.get(i).start();
			}
	    	
			for(int i = 0; i < nPhilosophers; i++) {
				try {
					handlers.get(i).join();
				} catch(Exception e) {}
			}
			logger.log("All client handlers have finished\n");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			serverSocket.close();
			for(int i = 0; i < nPhilosophers; i++) {
				handlers.get(i).closeSocket();
			}
			logger.log("Shutting down the server\n");
		}
    }
}
