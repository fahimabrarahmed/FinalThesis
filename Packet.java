/**
 * 
 */

/**
 * @author Fahim
 *
 */
public class Packet 
{
	private String packetInfo;
	private int packetNum;
	
	public Packet()
	{
		packetInfo = "";
		packetNum = -1;
	}
	
	public Packet(String info, int num)
	{
		packetInfo = info;
		packetNum = num;
	}
	
	public String getPacketInfo()
	{
		return packetInfo;
	}
	
	public int getPacketNum()
	{
		return packetNum;
	}
	
	public void setPacketInfo(String newInfo)
	{
		packetInfo = newInfo;
	}
	
	public void setPacketNum(int newNum)
	{
		packetNum = newNum;
	}
	
	public String toString()
	{
		return getPacketInfo() + ": " + getPacketNum();
	}
}
