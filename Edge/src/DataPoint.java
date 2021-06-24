import org.json.simple.JSONObject;

public class DataPoint {
	public dataSource source;
	public double value;
	public double time;
	public int ID;
        public static final int EDGE_ID = 1;
	
	public DataPoint(dataSource source, double value, double time)
	{
		this.source = source;
		this.value = value;
		this.time = time;
		this.ID = (int) Math.floor(Math.random()*65535);
	}
	
	public JSONObject getJSON()
	{
		JSONObject data = new JSONObject();
		data.put("EDGE_ID", EDGE_ID);
		data.put("timestamp", this.time);
		data.put("type", this.source);
		data.put("value", this.value);
		data.put("ID", this.ID);
		return data;
	}
	
}
