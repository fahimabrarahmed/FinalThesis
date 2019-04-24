import java.io.*;
import java.net.*; 
import java.util.Scanner;
import java.util.LinkedList; 
import java.util.Queue; 

/**
 * @author Fahim
 * This is the Another robot class(client) and deals with the communication with Robot Handler class.
 */

public class AnotherRobot 
{
    public static void main(String[] args) throws IOException  
    {
        try
        {
           	//getting ip address of client and connecting to server
        	InetAddress ipAdd = InetAddress.getByName("localhost");
        	Socket clientSoc = new Socket(ipAdd, 5056);
        	
        	//creating input/output streams for client-server communication
        	DataInputStream dis = new DataInputStream(clientSoc.getInputStream());
        	DataOutputStream dos = new DataOutputStream(clientSoc.getOutputStream());
        	Scanner inputScan = new Scanner(System.in);	//scanner to read client input
        	
        	//this is an infinite which goes on to facilitate client-server communication
        	while(true)
        	{
            	//setting up all the variables to be used later
            	int numOfPackets;
            	String clientResponse;
            	Queue<Packet> packetQueue = new LinkedList<>();
            	Packet currentPacket;
            	
            	//prints server question and sends client answer
            	System.out.println(dis.readUTF());
            	clientResponse = inputScan.nextLine();
            	dos.writeUTF(clientResponse);
            	
            	//this is to check if client wants to exit or continue
            	if(clientResponse.equalsIgnoreCase("exit"))
            	{
                	inputScan.close();
                	dis.close();
                	dos.close();
            		clientSoc.close();
            		System.out.println("Robot is closing connection with Main Robot: "+clientSoc);
            		break;
            	}
            	if(clientResponse.equalsIgnoreCase("send"))
            	{
                	//inquiring and setting up a queue for packet generation
            		System.out.println("How many packets would you like to create?");
            		numOfPackets = inputScan.nextInt();
            		clientResponse = inputScan.nextLine();

                	//generate the queue with packets
                	for (int i=1; i<=numOfPackets; i++)
                	{
                		currentPacket = new Packet("Unprocessed Packet", i);
                		packetQueue.add(currentPacket);
                	}
                	System.out.println("These are the packets before being sent to Main Robot.");
                	System.out.println(packetQueue);
                	
                	int speed, done = 1;
    				while(true)
    				{
    					speed = dis.read();
    					dos.write(done);
    					if (done == 0)
    					{
    						//if all packets have been sent
    						break;
    					}
    					if (speed > 0)	//send packets if speed positive
    					{
    						currentPacket = packetQueue.remove();
    						dos.writeUTF(currentPacket.getPacketInfo());
    						dos.write(currentPacket.getPacketNum());
    						numOfPackets--;
    						if(numOfPackets==0)
    						{
    							//all packets have been sent
    							done = 0;
    						}
    					}
    					else
    					{
    						//comes here when it can't send packets because MainRobot's buffer is full
    						System.out.println("Main Robot has suspended packet transfer, will resume shortly.");
    					}
    				}
            	}
            	System.out.println("All packets have been sent to Main Robot.\n");
            	System.out.println("Now pick from the two options below:");
        	}
        }
        catch(Exception e)
        { 
            e.printStackTrace(); 
        } 
    } 
}
