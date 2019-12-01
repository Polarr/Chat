import java.net.*;
import java.util.Scanner;
import java.io.*;

public class Client {
	
	private ObjectInputStream input;		// to read from the socket
	private ObjectOutputStream output;		// to write on the socket
	private Socket socket;
	
	private String ip, username;
	private int port;
	
	public Client(String newIp, int newPort, String newUser) {
		ip = newIp;
		port = newPort;
		username = newUser;
		
		if (Start()) {
			ReadInput();
		}
	}
	
	public void ReadInput() {
		Scanner scan = new Scanner(System.in);
		while(true) 
		{
			System.out.print("> ");

			String msg = scan.nextLine();
			
			SendMessage(msg);
		}
	}
	
	public boolean Start() {

		try 
		{
			socket = new Socket(ip, port);
		} 

		catch(Exception ec) 
		{
			return false;
		}
		
		String msg = "Connection accepted " + socket.getInetAddress() + ":" + socket.getPort();
		System.out.println(msg);
	
		try
		{
			input  = new ObjectInputStream(socket.getInputStream());
			output = new ObjectOutputStream(socket.getOutputStream());
		}
		catch (IOException eIO) {
			System.out.println("Exception creating new Input/output Streams: " + eIO);
			return false;
		}

		new ListenFromServer().start();

		try
		{
			output.writeObject(username);
		}
		catch (IOException eIO) {
			System.out.println(eIO);
			return false;
		}

		return true;
	}
	
	void SendMessage(String msg) {
		try 
		{
			output.writeObject(msg);
		}
		catch(IOException e) {
			System.out.println(e);
		}
	}
	
	class ListenFromServer extends Thread {

		public void run() {
			while(true) {
				try {
					String msg = (String) input.readObject();

					System.out.println(msg);
					System.out.print("> ");
				}
				catch(IOException e) {
					System.out.println(e);
					break;
				}
				catch(ClassNotFoundException e2) {
				}
			}
		}
	}

}
