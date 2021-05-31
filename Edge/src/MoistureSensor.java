
import java.util.*;

public class MoistureSensor extends Sensor {
	double center = 3;
	double lastValue = center;
	Random r = new Random();
	
	public double getData()
	{
		lastValue = lastValue + r.nextGaussian() * (Math.abs(lastValue - center));
		return Utils.clamp(lastValue, 0, 10);
	}
	
	public dataSource getType()
	{
		return dataSource.MOISTURE;
	}
}
