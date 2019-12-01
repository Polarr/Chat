import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.io.*;

public class Server {
	private static int idCounter;
	
	private ArrayList<User> users;
	private int port;
	
	public Server(int newPort) throws IOException 
	{
		users = new ArrayList<User>();
		port = newPort;
	}
	
	public void Start() {
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			while(true) 
			{
				Socket socket = serverSocket.accept();
				User t = new User(socket);
				users.add(t);		
				t.start();
			}
		}
		catch (IOException e) {
			System.out.println(e);
		}
	}
	
	private synchronized boolean Broadcast(String message) {
		if (users.size() > 0) {
			for(int x = 0; x < users.size(); x++) 
			{
				User ct = users.get(x);
				if(!ct.Message(message)) 
				{
					users.remove(x);
				}
			}
			return true;
		}
		return false;
	}
	
	class User extends Thread {
		private final Socket socket;
		private ObjectInputStream input;
		private ObjectOutputStream output;
		private String username;
		private String message;
		private int id;
		
		public User(Socket s) {
			socket = s;
			id = idCounter++;
			
			try
			{
				output = new ObjectOutputStream(socket.getOutputStream());
				input  = new ObjectInputStream(socket.getInputStream());
				username = (String)input.readObject();
				Broadcast(username + " has joined the chat room.");
			}
			catch (IOException e) {
				return;
			}
			catch (ClassNotFoundException e) {
			}
		}
		
		public void run() {
			while(true) 
			{
				try 
				{
					message = (String) input.readObject();
				}
				catch (IOException e) 
				{
					break;				
				}
				catch(ClassNotFoundException e2) 
				{
					break;
				}
				Broadcast(username + ": " + message);
			}
		}
		
		public String Username() {
			return username;
		}
		
		public void SetUsername(String s) {
			username = s;
		}
		
		private boolean Message(String msg) {
			if (!socket.isConnected())
				return false;
			try 
			{
				output.writeObject(msg);
			}
			catch(IOException e) 
			{
				System.out.println(e);
			}
			return true;
		}
	}
}
