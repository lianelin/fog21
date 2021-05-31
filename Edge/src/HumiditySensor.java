
import java.util.*;

public class HumiditySensor extends Sensor {
	double min = 0;
	double max = 100;
	Random r = new Random();
	double lastValue = 40;
	
	public double getData()
	{
		lastValue = lastValue + r.nextGaussian()*0.01;
		return Utils.clamp(lastValue, 0, 100);
	}
	public dataSource getType()
	{
		return dataSource.HUMIDITY;
	}
}
