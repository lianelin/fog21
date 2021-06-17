import org.json.simple.JSONObject;

public class DataPoint {
	public dataSource source;
	public double value;
	public double time;
	
	public DataPoint(dataSource source, double value, double time)
	{
		this.source = source;
		this.value = value;
		this.time = time;
	}
	
	public JSONObject getJSON()
	{
		JSONObject data = new JSONObject();
		data.put("timestamp", this.time);
		data.put("type", this.source);
		data.put("value", this.value);
		return data;
	}
	
}
