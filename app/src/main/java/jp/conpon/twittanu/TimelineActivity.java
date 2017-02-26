package jp.conpon.twittanu;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import twitter4j.ResponseList;

/**
 * Created by mirage-residence on 2017/01/18.
 */
public class TimelineActivity extends Activity {
	private TimelineAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);

		adapter = new TimelineAdapter(this, R.layout.view_timeline_item);
		ListView timeline = (ListView) findViewById(R.id.timeline);
		timeline.setAdapter(adapter);
	}

	@Override
	protected void onResume() {
		super.onResume();
		new AsyncTask<Void, Void, ResponseList<twitter4j.Status>>() {
			@Override
			protected ResponseList<twitter4j.Status> doInBackground(Void[] params) {
				return TwitterManager.INSTANCE.getHomeTimeline();
			}

			@Override
			protected void onPostExecute(ResponseList<twitter4j.Status> statuses) {
				if (statuses != null) {
					Log.d("tweets", "size = " + statuses.size());
					adapter.clear();
					adapter.addAll(statuses);
					adapter.notifyDataSetChanged();
				}
			}
		}.execute();
	}
}
