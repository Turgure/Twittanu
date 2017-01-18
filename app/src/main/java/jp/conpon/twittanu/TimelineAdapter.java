package jp.conpon.twittanu;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by mirage-residence on 2017/01/10.
 */
public class TimelineAdapter extends ArrayAdapter<twitter4j.Status> {
	private final int layoutRes;

	public TimelineAdapter(@NonNull Context context, @LayoutRes int resource) {
		super(context, resource);
		layoutRes = resource;
	}

	@NonNull
	@Override
	public View getView(int position, View convertView, @NonNull ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(layoutRes, null);
		}

		final twitter4j.Status status = getItem(position);
		if (status != null) {
			TextView content = (TextView) convertView.findViewById(R.id.content);
			content.setText(status.getText());
		}

		return convertView;
	}
}
