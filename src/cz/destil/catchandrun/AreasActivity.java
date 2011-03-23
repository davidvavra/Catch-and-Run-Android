package cz.destil.catchandrun;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter;

public class AreasActivity extends ListActivity implements OnItemClickListener {
	/** Called when the activity is first created. */

	ProgressDialog progressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		//this will cause conflict

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
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (progressDialog != null)
			progressDialog.hide();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage(getString(R.string.loading_area));
		progressDialog.show();
		Intent intent = new Intent(this, GameActivity.class);
		startActivity(intent);
	}
}