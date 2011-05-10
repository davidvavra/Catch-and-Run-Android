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
import android.view.View;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;

public class GameActivity extends MapActivity implements OnGestureListener {

	CurrentLocationOverlay myLocation;
	MapView mapView;
	BorderOverlay border;
	List<Overlay> overlays;
	PlayersOverlay players;
	TreasuresOverlay treasures;
	Scenario scenario;
	public GoogleAnalyticsTracker tracker;

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.map);
		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		overlays = mapView.getOverlays();
		ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
	    actionBar.setHomeLogo(R.drawable.icon_actionbar);
	    actionBar.addAction(new Action() {
			
			@Override
			public void performAction(View view) {
				tracker.trackEvent("Clicks", "My Location", "Action selected", 0);
				mapView.getController().animateTo(myLocation.getMyLocation());
				myLocation.snapToLocation = true;
			}
			
			@Override
			public int getDrawable() {
				return R.drawable.my_location;
			}
		});

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

		//enable tracking
		tracker = GoogleAnalyticsTracker.getInstance();
		tracker.start("UA-337476-13", 10, this);
		
		// start scenarios
		scenario = new Scenario(this);
		scenario.start();
	}
	
	@Override
	protected void onStop() {
	    tracker.dispatch();
	    super.onStop();
	}
	
	@Override
	protected void onDestroy() {
	    tracker.stop();
	    super.onDestroy();
	}
	
	@Override
	protected void onStart() {
	    tracker.trackPageView("/game");
	    super.onStart();
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
		case R.id.hide:
			myLocation.toggleHide();
			tracker.trackEvent("Clicks", "Hide/Show", "Menu selected", 0);
			return true;
		case R.id.map_mode:
			if (mapView.isSatellite()) {
				mapView.setSatellite(false);
			} else {
				mapView.setSatellite(true);
			}
			tracker.trackEvent("Clicks", "Map mode", "Menu selected", 0);
			return true;
		case R.id.leave:
			tracker.trackEvent("Clicks", "Leave", "Menu selected", 0);
			checkExit();
			return true;
		case R.id.rules:
			tracker.trackEvent("Clicks", "Rules", "Menu selected", 0);
			showRules();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		if (mapView.isSatellite()) {
			menu.getItem(1).setTitle(R.string.street_map);
		} else {
			menu.getItem(1).setTitle(R.string.satellite_map);
		}

		if (myLocation.role == Common.HIDDEN) {
			menu.getItem(0).setTitle(R.string.reveal);
		} else {
			menu.getItem(0).setTitle(R.string.hide);
		}
		return super.onMenuOpened(featureId, menu);
	}
	
	@Override
	public void onBackPressed() {
		tracker.trackEvent("Clicks", "Leave", "Back selected", 0);
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
		tracker.setCustomVar(1, "Final score $", String.valueOf(myLocation.money));
		tracker.trackEvent("Game Events", "End", "Game end", myLocation.money);
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
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		myLocation.snapToLocation = false;
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

}
