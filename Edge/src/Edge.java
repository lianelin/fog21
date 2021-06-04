import java.util.*;
import java.io.*;
import java.net.*;

public class Edge {
	private List<DataPoint> outBuffer = new ArrayList<DataPoint>();
	private List<Sensor> sensors = new ArrayList<Sensor>();
	String ip = "35.198.79.71";
	private int port = 3389;
	Socket socket;
	DataInputStream inputstream;
	DataOutputStream outputstream;
	
	boolean connected;

	public Edge() {
		sensors.add(new BrightnessSensor());
		sensors.add(new HumiditySensor());
		sensors.add(new MoistureSensor());
		sensors.add(new TemperatureSensor());
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
			outBuffer.add(new DataPoint(s.getType(), s.getData())); // read sensor values and add them to the outBuffer
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
				System.out.println(dp.value + " " + dp.source);
				//create json here
				
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
        connected = true;
    }
	
	public static void main(String[] args) {
		Edge edge = new Edge();

		long lastSent = System.currentTimeMillis();
		while (true) {
			// establish conection
			Socket clientSocket;			
			edge.collectData();
			edge.sendData();
			
			long millis = System.currentTimeMillis(); // send every 100ms independent of data collection &
														// sending time
			long timeSinceLast = millis - lastSent;
			lastSent = millis;
			try {
				Thread.sleep(Utils.clamp(100 - timeSinceLast, 0, 100));
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}

		}
	}
}