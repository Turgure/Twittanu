package jp.conpon.twittanu;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.lang.ref.WeakReference;

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

        adapter = new TimelineAdapter();
        RecyclerView timeline = (RecyclerView) findViewById(R.id.timeline);
        timeline.setLayoutManager(new LinearLayoutManager(this));
        timeline.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        GetTimelineTask task = new GetTimelineTask(adapter);
        task.execute();
    }

    private static class GetTimelineTask extends AsyncTask<Void, Void, ResponseList<twitter4j.Status>> {
        WeakReference<TimelineAdapter> adapterRef;

        GetTimelineTask(@NonNull TimelineAdapter adapter) {
            adapterRef = new WeakReference<>(adapter);
        }

        @Override
        protected ResponseList<twitter4j.Status> doInBackground(Void[] params) {
            return TwitterManager.INSTANCE.getHomeTimeline();
        }

        @Override
        protected void onPostExecute(ResponseList<twitter4j.Status> statuses) {
            if (statuses != null) {
                Log.d("tweets", "size = " + statuses.size());

                TimelineAdapter adapter = adapterRef.get();
                if (adapter != null) {
                    adapter.clear();
                    adapter.addAll(statuses);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }
}
