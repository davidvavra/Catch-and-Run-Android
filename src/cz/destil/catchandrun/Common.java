package cz.destil.catchandrun;

import android.location.Location;

import com.google.android.maps.GeoPoint;

public class Common {

	// constants
	public static final int IDLER = 0;
	public static final int RUNNER = 1;
	public static final int CATCHER = 2;
	public static final int OUTSIDE_AREA = 3;
	public static final int HIDDEN = 4;
	
	public static int distance(GeoPoint from, GeoPoint to)
	{
		float[] distance = new float[3];
		Location.distanceBetween(from.getLatitudeE6() / 1e6, from.getLongitudeE6() / 1e6, to.getLatitudeE6() / 1e6,
				to.getLongitudeE6() / 1e6, distance);
		return (int)distance[0];
	}
}
