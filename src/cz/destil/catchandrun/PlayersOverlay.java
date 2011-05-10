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
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class PlayersOverlay extends ItemizedOverlay<Player> {

	ArrayList<Player> players = new ArrayList<Player>();
	GameActivity context;
	Paint paint;

	public PlayersOverlay(GameActivity context) {
		super(context.getResources().getDrawable(R.drawable.idler));
		this.context = context;

		// navigational line style
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setPathEffect(new PathDashPathEffect(makePathDash(), 12, 0,
				PathDashPathEffect.Style.ROTATE));
		paint.setColor(Color.BLUE);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(2);
	}

	@Override
	protected Player createItem(int i) {
		return players.get(i);
	}

	@Override
	public int size() {
		return players.size();
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		// draw navigational line
		for (Player player : players) {
			if (player.navigateTo) {
				Point from = new Point();
				Point to = new Point();
				Path path = new Path();
				if (player.role == Common.IDLER || player.role == Common.RUNNER) {
					mapView.getProjection().toPixels(context.getMyGeoPoint(),
							from);
					mapView.getProjection().toPixels(player.getPoint(), to);
					if (player.role == Common.IDLER) {
						paint.setColor(Color.BLUE);
					} else {
						paint.setColor(Color.GREEN);
					}
				} else {
					mapView.getProjection().toPixels(player.getPoint(), from);
					mapView.getProjection().toPixels(context.getMyGeoPoint(),
							to);
					paint.setColor(Color.RED);
				}
				path.moveTo(from.x, from.y);
				path.lineTo(to.x, to.y);
				canvas.drawPath(path, paint);
				break;
			}
		}
		super.draw(canvas, mapView, shadow);
	}

	public void addPlayer(Player player) {
		changeState(player, player.role, 0);
		players.add(player);
		populate();
	}

	public void changeLocation(Player player, GeoPoint geoPoint) {
		players.clear();
		Player newPlayer = new Player(geoPoint, player.role, player.name,
				player.money, player.navigateTo);
		players.add(newPlayer);
		changeState(newPlayer, player.role, 0);
		setLastFocusedIndex(-1); 
        populate(); 
	}

	public void changeState(Player player, int role, int moneyIncrease) {
		player.role = role;
		player.money += moneyIncrease;
		int iconRes = 0;
		switch (player.role) {
		case Common.IDLER:
			iconRes = R.drawable.idler;
			break;
		case Common.CATCHER:
			iconRes = R.drawable.catcher;
			break;
		case Common.RUNNER:
			iconRes = R.drawable.runner;
			break;
		}
		Drawable icon = context.getResources().getDrawable(iconRes);
		icon.setBounds(0, -icon.getIntrinsicHeight(), icon.getIntrinsicWidth(),
				0);
		player.setMarker(icon);
	}

	@Override
	protected boolean onTap(int index) {
		final Player player = players.get(index);

		// building message
		int distance = Common.distance(context.getMyGeoPoint(),
				player.getPoint());
		String role = "";
		switch (player.role) {
		case Common.IDLER:
			role = context.getString(R.string.idling);
			break;
		case Common.CATCHER:
			role = context.getString(R.string.catching) + " "
					+ context.getString(R.string.you);
			break;
		case Common.RUNNER:
			role = context.getString(R.string.running_from) + " "
					+ context.getString(R.string.you);
			break;
		}
		String message = distance + " m, " + role + ", $" + player.money;
		
		context.tracker.trackEvent("Clicks", "Player", message, player.money);
		
		int navigateButton;
		if (player.navigateTo)
			navigateButton = R.string.stop_navigation;
		else
			navigateButton = R.string.navigate;

		// building alert
		new AlertDialog.Builder(context)
				.setTitle(player.name)
				.setMessage(message)
				.setPositiveButton(navigateButton,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								if (player.navigateTo) {
									players.get(0).navigateTo = false;
								} else {
									players.get(0).navigateTo = true;
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

class Player extends OverlayItem {

	int role;
	String name;
	int money;
	boolean navigateTo;

	public Player(GeoPoint point, int role, String name, int money, boolean navigateTo) {
		super(point, null, null);
		this.role = role;
		this.name = name;
		this.money = money;
		this.navigateTo = navigateTo;
	}

}
