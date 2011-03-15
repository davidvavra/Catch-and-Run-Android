package cz.destil.catchandrun;

import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Vibrator;

public class GameEvents {

	GameActivity context;
	public static final int WON = 0;
	public static final int LOST = 1;
	public static final int BECAME_CATCHER = 2;
	public static final int BECAME_RUNNER = 3;
	public static final int TREASURE_FOUND = 4;

	public GameEvents(GameActivity context) {
		this.context = context;
	}

	public void go(int event) {
		int alertCaption = -1, sound = -1, price;
		String alertText = null;
		CurrentLocationOverlay myPlayer = context.myLocation;
		Player otherPlayer = context.players.getItem(0);
		Treasure treasure = context.treasures.getItem(0);

		// decide
		switch (event) {
		case (WON):
			price = otherPlayer.money / 2;
			context.players.changeState(otherPlayer, Common.IDLER, -price);
			otherPlayer.navigateTo = false;
			myPlayer.role = Common.IDLER;
			myPlayer.money += price;
			alertCaption = R.string.you_have_won;
			alertText = context.getString(R.string.you_took) + " $" + price
					+ " " + context.getString(R.string.from_player) + " "
					+ otherPlayer.name;
			sound = R.raw.won;
			break;
		case (LOST):
			price = myPlayer.money / 2;
			context.players.changeState(otherPlayer, Common.IDLER, price);
			otherPlayer.navigateTo = false;
			myPlayer.role = Common.IDLER;
			myPlayer.money -= price;
			alertCaption = R.string.you_have_lost;
			alertText = context.getString(R.string.you_lost) + " $" + price
					+ " " + context.getString(R.string.to_player) + " "
					+ otherPlayer.name;
			sound = R.raw.lost;
			break;
		case (BECAME_CATCHER):
			context.players.changeState(otherPlayer, Common.RUNNER, 0);
			otherPlayer.navigateTo = true;
			myPlayer.role = Common.CATCHER;
			alertCaption = R.string.become_catcher;
			alertText = context.getString(R.string.catch_player) + " "
					+ otherPlayer.name + " " + context.getString(R.string.asap);
			sound = R.raw.catcher;
			break;
		case (BECAME_RUNNER):
			context.players.changeState(otherPlayer, Common.CATCHER, 0);
			otherPlayer.navigateTo = true;
			myPlayer.role = Common.RUNNER;
			alertCaption = R.string.become_runner;
			alertText = context.getString(R.string.run_away_from) + " "
					+ otherPlayer.name + "!";
			sound = R.raw.runner;
			break;
		case (TREASURE_FOUND):
			treasure.navigateTo = false;
		myPlayer.role = Common.IDLER;
		myPlayer.money += treasure.money;
			alertCaption = R.string.treasure_found;
			alertText = context.getString(R.string.you_have_found) + " $"
					+ treasure.money + " "
					+ context.getString(R.string.inside_treasure_chest);
			sound = R.raw.tada;
			break;
		}

		// show alert
		new AlertDialog.Builder(context)
				.setTitle(alertCaption)
				.setMessage(alertText)
				.setNegativeButton(R.string.close,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						}).create().show();
		// vibrate
		Vibrator v = (Vibrator) context
				.getSystemService(Context.VIBRATOR_SERVICE);
		v.vibrate(500);
		// play sound
		Timer timer = new Timer();
		final MediaPlayer mp = MediaPlayer.create(context, sound);
		mp.setScreenOnWhilePlaying(true);
		mp.setVolume(10, 10);
		timer.schedule(new TimerTask() {
			public void run() {
				mp.start();
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}, 500);
	}
}
