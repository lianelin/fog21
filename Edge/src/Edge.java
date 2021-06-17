import java.util.*;

import org.json.simple.JSONObject;

import java.io.*;
import java.net.*;

public class Edge {
	private List<DataPoint> outBuffer = new ArrayList<DataPoint>();
	private List<Sensor> sensors = new ArrayList<Sensor>();
	String ip = "35.198.79.71";
	private int port = 3389;
	private Socket socket;
	private DataInputStream inputstream;
	private DataOutputStream outputstream;
	private int ID;
	private String name;
	private int[] location = new int[2];
	
	boolean connected;

	public Edge() {
		sensors.add(new BrightnessSensor());
		sensors.add(new HumiditySensor());
		sensors.add(new MoistureSensor());
		sensors.add(new TemperatureSensor());
		
		this.ID = (int) Math.floor(Math.random()*65535);
        this.name = ("Node " + this.ID);
        this.location[0] = (int)Math.floor(Math.random()*10);
        this.location[1] = (int)Math.floor(Math.random()*10);
	}

	private boolean receiveData() {
		try
		{
			System.out.println(inputstream.readUTF());
			return true;
		} catch (Exception e){
	        e.printStackTrace();
	        return false;
		}
	}

	private void collectData() {
		for (Sensor s : sensors) {
			outBuffer.add(new DataPoint(s.getType(), s.getData(), System.currentTimeMillis())); // read sensor values and add them to the outBuffer
		}
	}

	private boolean sendData() {
		try {
			ListIterator<DataPoint> iterator = outBuffer.listIterator();
			if(!connected)
			{
				connect();
			}
			while (iterator.hasNext()) {
				DataPoint dp = iterator.next();
				JSONObject data = dp.getJSON();
				System.out.println(data);
				
				outputstream.writeUTF("Hallöchen");
				
				if(inputstream.available() > 0)
				{
					receiveData();
				}
				
				iterator.remove();
			}
		} catch (Exception e){
            e.printStackTrace();
            return false;
		}
		return true;
	}

	private void connect() throws IOException {
		InetAddress address = InetAddress.getByName(ip);
		socket = new Socket(address, port);
		inputstream = new DataInputStream(socket.getInputStream());
        outputstream = new DataOutputStream(socket.getOutputStream());
        JSONObject registration = new JSONObject();
        registration.put("ID", this.ID);
        registration.put("name", this.name);
        registration.put("pos", this.location);
        outputstream.writeUTF(registration.toString());
        connected = true;
    }
	
	public static void main(String[] args) {
		Edge edge = new Edge();
		int interval = 1000;
		
		long lastSent = System.currentTimeMillis();
		while (true) {
			// establish conection
			Socket clientSocket;			
			edge.collectData();
			edge.sendData();
			
			long millis = System.currentTimeMillis(); // send regularly independent of data collection &
														// sending time
			long timeSinceLast = millis - lastSent;
			lastSent = millis;
			try {
				Thread.sleep(Utils.clamp(interval - timeSinceLast, 0, interval));
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}

		}
	}
}