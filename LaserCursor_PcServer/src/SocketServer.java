import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.ArrayList;

public class SocketServer {
	public static void main(String[] args)
	{
	int[] position = new int[2];		
	String print = null;
	int k = 0;
		while(true)
		{
			try
			{
				byte[] data = new byte[4];
				DatagramPacket packet = new DatagramPacket (data,data.length);
				DatagramSocket socket = new DatagramSocket(1234);
				socket.receive(packet);
				socket.close();

				byte receive[] = packet.getData();
				position[0] = (int) receive[0] + (int)receive[1]*32 ;
				position[1] = (int) receive[2] + (int)receive[3]*32;				
				//mousePressMoveFromTo(position[0][1],position[0][0],position[1][1],position[1][0]);
				//System.out.print(position[0]+","+position[1]+"\n");					
				mouseMoveTo(position[0],position[1]);
				//System.out.print(k+"::");
				//{mousePressMoveFromTo(position[0],position[1],position[2],position[3]);}
			}catch (Exception e){e.printStackTrace();}

		}
	}
	
	 public static void mousePressMoveFromTo(int begin_x, int begin_y, int end_x, int end_y) throws AWTException
	  {	   
		  Robot robot = new Robot();
		  robot.delay(3000);
		  //System.out.println("start");
		  robot.mouseMove(begin_x, begin_y);
		  //robot.delay(100);
		  robot.mousePress(InputEvent.BUTTON1_MASK);
		 // robot.mousePress(InputEvent.BUTTON1_MASK);
		  //System.out.println("move");

		  robot.mouseMove(end_x, end_y);
		  robot.mouseRelease(InputEvent.BUTTON1_MASK);
	  }
	 public static void mouseMoveTo(int x, int y) throws AWTException
	  {	   
		 Robot robot = new Robot();
		 robot.mousePress(InputEvent.BUTTON1_MASK);
		 robot.mouseMove(x, y);
		 robot.mouseRelease(InputEvent.BUTTON1_MASK);
	  }
}


