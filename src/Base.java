import java.io.IOException;
import java.util.Scanner;

public class Base {

	public static void main(String[] args) 
	{
		System.out.println("Would you like to host a server or connect to a server?");
		System.out.println("Type 'host' or 'connect'");
		
		Scanner in = new Scanner(System.in);
		String str = in.nextLine().trim();
		while (true) {
			if (str.equalsIgnoreCase("host") || str.equalsIgnoreCase("connect")) {
				break;
			}
			else {
				System.out.println("Input not recognized.");
				str = in.nextLine().trim();
			}
		}
		if (str.equalsIgnoreCase("host")) 
		{
			try 
			{
				System.out.println("What port would you like to host on?");
				int port = in.nextInt();
				Server server = new Server(port);
				server.Start();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		else
		{
			System.out.println("What would you like your username to be?");
			String username = in.nextLine().trim();
			System.out.println("What ip would you like to connect to?");
			String ip = in.nextLine().trim();
			System.out.println("What port would you like to connect to?");
			int port = in.nextInt();
			
			if (username.length() > 0 && ip.length() > 0 && port > 0) {
				Client client = new Client(ip, port, username);
			}
		}
	}

}
