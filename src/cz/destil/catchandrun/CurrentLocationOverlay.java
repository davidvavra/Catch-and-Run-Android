package cz.destil.catchandrun;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.Location;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

public class CurrentLocationOverlay extends MyLocationOverlay {

	public boolean snapToLocation = true;
	private MapView mapView;
	private GameActivity context;
	protected int role;
	private String name;
	protected int money;
	private String infoText;
	private int hiddenTime;
	private int previousRole;

	public CurrentLocationOverlay(GameActivity context, MapView mapView) {
		super(context, mapView);
		this.mapView = mapView;
		this.context = context;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setHiddenTime(int hiddenTime) {
		this.hiddenTime = hiddenTime;
	}

	public void decreaseHiddenTime(int seconds) {
		hiddenTime -= seconds;
		if (hiddenTime <= 0)
			toggleHide();
	}

	public void update() {
		int distance;
		switch (role) {
		case Common.IDLER:
			infoText = context.getString(R.string.idling) + "... ($" + money
					+ ")";
			break;
		case Common.CATCHER:
			distance = Common.distance(getMyLocation(),
					context.getPlayerGeoPoint());
			infoText = context.getString(R.string.catching) + " Honza123 ("
					+ distance + " m)";
			break;
		case Common.RUNNER:
			distance = Common.distance(getMyLocation(),
					context.getPlayerGeoPoint());
			infoText = context.getString(R.string.running_from) + " Honza123 ("
					+ distance + " m)";
			break;
		case Common.HIDDEN:
			infoText = context.getString(R.string.hidden) + " (" + hiddenTime
					+ " " + context.getString(R.string.sec_remain) + ")";
			break;
		case Common.OUTSIDE_AREA:
			infoText = context.getString(R.string.outside_area);
			break;
		}
		context.setTitle(infoText);
	}

	@Override
	public synchronized void onLocationChanged(Location location) {
		if (location.getProvider().equals("mockgps")) {
			super.onLocationChanged(location);
			if (snapToLocation) {
				mapView.getController().animateTo(getMyLocation());
			}
		}
	}

	public void toggleHide() {
		if (role == Common.HIDDEN) {
			role = previousRole;
		} else {
			if (this.hiddenTime <= 0) {
				new AlertDialog.Builder(context)
						.setTitle(R.string.not_possible)
						.setMessage(R.string.no_more_hidden_time)
						.setNegativeButton(R.string.close,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.cancel();
									}
								}).create().show();
			} else {
				previousRole = role;
				role = Common.HIDDEN;
			}
		}
		update();
	}

	@Override
	public boolean onTap(GeoPoint p, MapView map) {
		if (super.onTap(p, map)) {

			int hideButton;
			if (role == Common.HIDDEN) {
				hideButton = R.string.reveal;
			} else {
				hideButton = R.string.hide;
			}
			
			context.tracker.trackEvent("Clicks", "My Location", infoText, money);

			new AlertDialog.Builder(context)
					.setTitle(name)
					.setMessage(infoText)
					.setPositiveButton(hideButton,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									context.tracker.trackEvent("Clicks", "Hide/Show", "from My Location", 0);
									toggleHide();
								}
							})
					.setNegativeButton(R.string.close,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							}).create().show();
		}
		return super.onTap(p, map);
	}

}
