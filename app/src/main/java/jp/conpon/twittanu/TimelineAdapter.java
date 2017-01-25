package jp.conpon.twittanu;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/**
 * Created by mirage-residence on 2017/01/10.
 */
public class TimelineAdapter extends ArrayAdapter<twitter4j.Status> {
	private final LayoutInflater inflater;
	private final int layoutRes;

	public TimelineAdapter(@NonNull Context context, @LayoutRes int resource) {
		super(context, resource);
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layoutRes = resource;
	}

	@NonNull
	@Override
	public View getView(int position, View convertView, @NonNull ViewGroup parent) {
		TimelineItemLayout layout;
		if (convertView == null) {
			layout = (TimelineItemLayout) inflater.inflate(layoutRes, null);
		} else {
			layout = (TimelineItemLayout) convertView;
		}

		layout.bindView(getItem(position));

		return layout;
	}
}
