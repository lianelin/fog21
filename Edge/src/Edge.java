import java.util.*;
import java.lang.*;

public class Edge 
{
	private List<DataPoint> outBuffer=new ArrayList<DataPoint>();
	private List<Sensor> sensors = new ArrayList<Sensor>(); 
	
	public Edge()
	{
		sensors.add(new BrightnessSensor());
		sensors.add(new HumiditySensor());
		sensors.add(new MoistureSensor());
		sensors.add(new TemperatureSensor());
		
		
	}
	
	private void onReceive()
	{
		
	}
	
	private void collectData()
	{
		for(Sensor s : sensors)
		{
			outBuffer.add(new DataPoint(s.getType(), s.getData())); //read sensor values and add them to the outBuffer
		}
	}
	
	private boolean sendData()
	{
		boolean success = true;
		ListIterator<DataPoint> iterator = outBuffer.listIterator();
		while(iterator.hasNext()){
			DataPoint dp = iterator.next();
			System.out.println(dp.value + " " + dp.source);
		    //send data to cloud here
			iterator.remove(); //remove if successful
			//otherwise set success = false
		}
		return success;
			
	}
 
       public static void main (String[] args)
       {    	
    	   Edge edge = new Edge();
    	   long lastSent = System.currentTimeMillis();
    	   while(true)
    	   {
    		   edge.collectData();
    		   edge.sendData();
    		   
    		   long millis = System.currentTimeMillis(); //send every 100ms independent of data collection & sending time
    		   long timeSinceLast = millis - lastSent;
    		   lastSent = millis;
    		   try
    		   {
    		       Thread.sleep(Utils.clamp(100-timeSinceLast, 0, 100));
    		   }
    		   catch(InterruptedException ex)
    		   {
    		       Thread.currentThread().interrupt();
    		   }
    		   
    	   }
       }
}