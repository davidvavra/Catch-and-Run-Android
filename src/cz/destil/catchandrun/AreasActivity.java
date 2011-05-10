package cz.destil.catchandrun;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;

public class AreasActivity extends ListActivity implements OnItemClickListener {
	/** Called when the activity is first created. */

	ProgressDialog progressDialog;
	GoogleAnalyticsTracker tracker;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		// different line

		List<HashMap<String, String>> areas = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> first = new HashMap<String, String>();
		first.put("name", "Stromovka challenge");
		first.put("more", "0.5 km, 1 player");
		HashMap<String, String> second = new HashMap<String, String>();
		second.put("name", "Beroun hardcore");
		second.put("more", "20 km, 0 players");
		areas.add(first);
		areas.add(second);
		setListAdapter(new SimpleAdapter(this, areas,
		        android.R.layout.simple_list_item_2, new String[] { "name",
		                "more" }, new int[] { android.R.id.text1,
		                android.R.id.text2 }));
		getListView().setOnItemClickListener(this);

		tracker = GoogleAnalyticsTracker.getInstance();
		tracker.start("UA-337476-13", 10, this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (progressDialog != null)
			progressDialog.hide();
		tracker.trackPageView("/areas");
	}
	
	protected void onStop() {
		tracker.dispatch();
		super.onStop();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage(getString(R.string.loading_area));
		progressDialog.show();
		tracker.trackEvent("Clicks", "Area", "selected", position);
		Intent intent = new Intent(this, GameActivity.class);
		startActivity(intent);
	}
	
	@Override
	protected void onDestroy() {
	    super.onDestroy();
	    tracker.stop();
	}
}