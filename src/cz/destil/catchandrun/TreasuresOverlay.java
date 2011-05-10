package cz.destil.catchandrun;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class TreasuresOverlay extends ItemizedOverlay<Treasure> {

	ArrayList<Treasure> treasures = new ArrayList<Treasure>();
	GameActivity context;
	Paint paint;

	public TreasuresOverlay(GameActivity context) {
		super(boundCenterBottom(context.getResources().getDrawable(
				R.drawable.treasure)));
		this.context = context;

		// navigational line style
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setPathEffect(new PathDashPathEffect(makePathDash(), 12, 1,
				PathDashPathEffect.Style.ROTATE));
		paint.setColor(Color.BLUE);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(2);
	}

	@Override
	protected Treasure createItem(int i) {
		return treasures.get(i);
	}

	@Override
	public int size() {
		return treasures.size();
	}

	public void addTreasure(Treasure treasure) {
		treasures.add(treasure);
		populate();
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		// draw navigational line
		for (Treasure treasure : treasures) {
			if (treasure.navigateTo) {
				Point from = new Point();
				Point to = new Point();
				Path path = new Path();
				mapView.getProjection().toPixels(context.getMyGeoPoint(), from);
				mapView.getProjection().toPixels(treasure.getPoint(), to);
				path.moveTo(from.x, from.y);
				path.lineTo(to.x, to.y);
				canvas.drawPath(path, paint);
				break;
			}
		}
		super.draw(canvas, mapView, shadow);
	}

	@Override
	protected boolean onTap(int index) {
		final Treasure treasure = treasures.get(index);

		// building message
		int distance = Common.distance(context.getMyGeoPoint(),
				treasure.getPoint());
		String message = context.getString(R.string.treasure_value) + " ("
				+ distance + " m)";
		
		context.tracker.trackEvent("Clicks", "Treasure", message, treasure.money);
		
		int navigateButton;
		if (treasure.navigateTo)
			navigateButton = R.string.stop_navigation;
		else
			navigateButton = R.string.navigate;

		// building alert
		new AlertDialog.Builder(context)
				.setTitle(context.getString(R.string.treasure))
				.setMessage(message)
				.setPositiveButton(navigateButton,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								if (treasure.navigateTo) {
									treasure.navigateTo = false;
								} else {
									treasure.navigateTo = true;
								}
							}
						})
				.setNegativeButton(R.string.close,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						}).create().show();
		return true;
	}

	private static Path makePathDash() {
		Path p = new Path();
		p.moveTo(4, 0);
		p.lineTo(0, -4);
		p.lineTo(8, -4);
		p.lineTo(12, 0);
		p.lineTo(8, 4);
		p.lineTo(0, 4);
		return p;
	}

}

class Treasure extends OverlayItem {
	int money;
	boolean navigateTo;

	public Treasure(GeoPoint geoPoint, int money) {
		super(geoPoint, null, null);
		this.money = money;
		this.navigateTo = false;
	}
}
