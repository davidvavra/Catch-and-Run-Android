package cz.destil.catchandrun;

import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.GestureDetector.OnGestureListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.markupartist.android.widget.ActionBar;

public class GameActivity extends MapActivity implements OnGestureListener {

	CurrentLocationOverlay myLocation;
	MapView mapView;
	BorderOverlay border;
	List<Overlay> overlays;
	PlayersOverlay players;
	TreasuresOverlay treasures;
	Scenario scenario;

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.map);
		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		overlays = mapView.getOverlays();
		ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
	    actionBar.setHomeLogo(R.drawable.icon_actionbar);

		// detect gestures
		overlays.add(new MapGestureDetectorOverlay(this));

		// add border overlay
		border = new BorderOverlay(this);
		overlays.add(border);

		// add treasures
		treasures = new TreasuresOverlay(this);
		overlays.add(treasures);

		// add players
		players = new PlayersOverlay(this);
		overlays.add(players);

		// add my location overlay
		myLocation = new CurrentLocationOverlay(this, mapView);
		myLocation.enableMyLocation();
		overlays.add(myLocation);

		// start scenarios
		scenario = new Scenario(this);
		scenario.start();
	}

	public GeoPoint getMyGeoPoint() {
		return myLocation.getMyLocation();
	}

	public GeoPoint getPlayerGeoPoint() {
		return players.getItem(0).getPoint();
	}

	/**
	 * Defining menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.map_menu, menu);
		return true;
	}

	/**
	 * Called when menu selected
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.my_location:
			mapView.getController().animateTo(myLocation.getMyLocation());
			myLocation.snapToLocation = true;
			return true;
		case R.id.hide:
			myLocation.toggleHide();
			return true;
		case R.id.map_mode:
			if (mapView.isSatellite()) {
				mapView.setSatellite(false);
			} else {
				mapView.setSatellite(true);
			}
			return true;
		case R.id.leave:
			checkExit();
			return true;
		case R.id.rules:
			showRules();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		if (mapView.isSatellite()) {
			menu.getItem(2).setTitle(R.string.street_map);
		} else {
			menu.getItem(2).setTitle(R.string.satellite_map);
		}

		if (myLocation.role == Common.HIDDEN) {
			menu.getItem(1).setTitle(R.string.reveal);
		} else {
			menu.getItem(1).setTitle(R.string.hide);
		}
		return super.onMenuOpened(featureId, menu);
	}
	
	@Override
	public void onBackPressed() {
		checkExit();
	}
	
	@Override
	public void setTitle(CharSequence title) {
		ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
	    actionBar.setTitle(title);
	}

	private void showRules() {
		new AlertDialog.Builder(this)
				.setTitle(R.string.rules)
				.setMessage(R.string.rules_text)
				.setNegativeButton(R.string.close,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						}).create().show();
	}

	private void checkExit() {
		if (myLocation.role == Common.CATCHER
				|| myLocation.role == Common.RUNNER) {
			new AlertDialog.Builder(this)
					.setTitle(R.string.are_you_sure)
					.setMessage(R.string.leaving_will_cause_losing_game)
					.setPositiveButton(R.string.leave,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									exit();
								}
							})
					.setNegativeButton(R.string.cancel,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							}).create().show();
		} else {
			exit();
		}
	}
	
	private void exit()
	{
		scenario.timer.cancel();
		myLocation.disableMyLocation();
		finish();
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		myLocation.snapToLocation = false;
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

}
