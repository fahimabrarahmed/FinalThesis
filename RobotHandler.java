import java.io.*;
import java.util.*;
import java.net.*;

/**
 * @author Fahim
 * This is the RobotHandler class. It deals with the interaction of the other robots (clients)
 */

public class RobotHandler extends Thread
{
    final Socket commSoc;	//communication socket 
    final DataInputStream dis; 
    final DataOutputStream dos;
    
    // Constructor 
    public RobotHandler(Socket sentSoc, DataInputStream sentDis, DataOutputStream sentDos)
    {
        commSoc = sentSoc;
        dis = sentDis;
        dos = sentDos;
    }
    
    @Override
    public void run()
    {
    	Scanner inputScan;
    	while(true)
    	{
    		inputScan = new Scanner(System.in);
    		try
    		{
    			String clientResponse;
    			//asking Another Robot for what it wants to do
    			dos.writeUTF("Type send to send packets to Main Robot. \n"
    					+ "Type exit to terminate connection.");
    			//recording Another Robot's reply
    			clientResponse = dis.readUTF();
    			
    			//if Another Robot wants to exit
    			if(clientResponse.equalsIgnoreCase("exit"))
    			{
    				System.out.println("Main Robot is closing connection with Robot: "+ commSoc);
    				commSoc.close();
    				break;
    			}
    			//if client wants to send
    			if(clientResponse.equalsIgnoreCase("send"))
    			{
        	    	//ask user how many packets the MainRobot can accept before asking AnotherRobot to slow down
        	    	System.out.println("Set Main Robot's packet buffer to [Pick an Integer]: ");
        	    	int maxPackets = inputScan.nextInt();
        	    	//clearLine just scans the rest of the line so the next inputScan starts on a new line
        	    	String clearLine = inputScan.nextLine();
    				
    				//Setting up variables 
                	Queue<Packet> packetQueue = new LinkedList<>(), processedPacketQueue = new LinkedList<>();
                	Packet currentPacket = new Packet(), processedPacket = new Packet();
                	int speed = 1, countPackets = 0, done;

    				while(true)
    				{
    					dos.write(speed);
    					done = dis.read();
    					if(speed > 0 && done >0) //if speed is pos then accepting state
    					{
    						System.out.println("Main Robot in accepting state.");
    						currentPacket = new Packet();
    						currentPacket.setPacketInfo(dis.readUTF());
    						currentPacket.setPacketNum(dis.read());
    						System.out.println("New packet received is "+currentPacket);
    						packetQueue.add(currentPacket);
    						countPackets++;
    						speed = 1;
    						if(countPackets == maxPackets)	//when packet buffer has been reached
    						{
    							System.out.println("Main Robot's packet buffer is full, suspending connected Robot from sending packets.");
    							System.out.println("Main Robot in unaccepting state.");
        						System.out.println("Current unprocessed Queue is "+ packetQueue);
    							speed = 0;
    						}
    					}
    					else	//will come here when packet buffer is full
    					{
    						while(packetQueue.peek()!= null)
    						{
    							//modified the packets that are already received
    							processedPacket = new Packet();
    							processedPacket = packetQueue.remove();
    							processedPacket.setPacketInfo("Server Processed Packet");
    							System.out.println("New Processed Packet is "+processedPacket);
    							processedPacketQueue.add(processedPacket);
    						}
    						if (done == 0)	//if done is 0 then all packets have been received from Client
    						{
    							System.out.println("All packets have been processed.");
    							break;
    						}
    						countPackets = 0;	//buffer is empty again
    						speed = 1;
    						System.out.println("Current processed Queue is "+processedPacketQueue);
    						System.out.println("Connected Robot resumes sending packets.");
    					}
    				}
    				System.out.println("These are the final processed packets from Main Robot.");
    				System.out.println(processedPacketQueue+"\n");
    				System.out.println("Waiting for further actions from connected Robot ...");
    			}
    			else
    			{
    				System.out.println("Invalid input.");
    			}		
    		}
    		catch (IOException e)
    		{
    			e.printStackTrace();
    		}
    	}
    	try
    	{
    		dis.close();
    		dos.close();
    	}
    	catch (IOException e)
    	{
    		e.printStackTrace();
    	}
    }
}
