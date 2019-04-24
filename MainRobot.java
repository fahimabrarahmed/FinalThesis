import java.io.*; 
import java.net.*; 

/**
 * @author Fahim
 * This main class sets up a server program. Everytime a robot connects a new RobotHandler
 * class is created. This allows for multiple robots to connect.
 */

public class MainRobot 
{
	public static void main(String[] args) throws IOException
	{
		//creating a server socket for future listening
		ServerSocket serverSoc = new ServerSocket(5056);
		System.out.println("Main Robot up and running. Waiting for connection from another robot..."); 
		
		//this is an infinite loop for receiving clients
		while(true)
		{
			//this socket will be used to receive incoming clients
			Socket incomSoc = null;
			
			try
			{
				//establishing connection with clients
				incomSoc = serverSoc.accept();
				System.out.println("New connection established with Robot: "+ incomSoc);
				
				//creating input and output streams for communication 
				DataInputStream dis = new DataInputStream(incomSoc.getInputStream());
				DataOutputStream dos = new DataOutputStream(incomSoc.getOutputStream());
				
				//creating a new thread object in case of multiple clients
				//clientHandler will oversee communication between server and clients
				Thread thread = new RobotHandler(incomSoc, dis, dos);
				System.out.println("New thread assigned for Robot: "+ incomSoc);
				System.out.println("\n");
				thread.start();
				
			}
			catch (Exception e)
			{
				incomSoc.close();
				e.printStackTrace();
			}
		}
		
	}
}
