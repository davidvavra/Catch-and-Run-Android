package cz.destil.catchandrun;

import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposePathEffect;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class BorderOverlay extends Overlay {

	private List<GeoPoint> geoPoints;
	Paint paint;
	GameActivity context;

	public BorderOverlay(GameActivity context) {
		this.context = context;
		// line style
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setPathEffect(new ComposePathEffect(new CornerPathEffect(10),new DashPathEffect(new float[] {10, 5, 5, 5}, 1)));
		paint.setColor(Color.MAGENTA);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(6);
	}
	
	public void setGeoPoints(List<GeoPoint> geoPoints) {
		this.geoPoints = geoPoints;
		
		//zoom the map so whole border can be seen
		int maxLat = -Integer.MAX_VALUE, maxLon = -Integer.MAX_VALUE, minLat = Integer.MAX_VALUE, minLon = Integer.MAX_VALUE;
		for (GeoPoint geoPoint : geoPoints) {
			if (geoPoint.getLatitudeE6()>maxLat)
				maxLat = geoPoint.getLatitudeE6();
			if (geoPoint.getLatitudeE6()<minLat)
				minLat = geoPoint.getLatitudeE6();
			if (geoPoint.getLongitudeE6()<minLon)
				minLon = geoPoint.getLongitudeE6();
			if (geoPoint.getLongitudeE6()>maxLon)
				maxLon = geoPoint.getLongitudeE6();
		}
		context.mapView.getController().zoomToSpan(maxLat-minLat, maxLon-minLon);
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		super.draw(canvas, mapView, shadow);
		int i = 0;
		Point start = new Point();
		mapView.getProjection().toPixels(geoPoints.get(0), start);
		Path path = new Path();
		for (GeoPoint geoPoint : geoPoints) {

			if (i==0)
			{
				path.moveTo(start.x, start.y);
			}
			else
			{
				Point point = new Point();
				mapView.getProjection().toPixels(geoPoint, point);
				path.lineTo(point.x, point.y);
				if (i == geoPoints.size()-1)
				{
					path.lineTo(start.x, start.y);
				}
			}
			i++;
		}
		canvas.drawPath(path, paint);
	}
}
