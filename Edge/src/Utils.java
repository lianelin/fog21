
public final class Utils {
	public static double  clamp(double val, double min, double max)
	{
		return Math.min(Math.max(val, min), max);
	}
	
	public static long clamp(long val, long min, long max)
	{
		return Math.min(Math.max(val, min), max);
	}
}
