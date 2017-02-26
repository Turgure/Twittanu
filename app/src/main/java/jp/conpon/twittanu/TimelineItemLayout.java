package jp.conpon.twittanu;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Date;

import twitter4j.User;

/**
 * Created by mirage-residence on 2017/01/25.
 */

public class TimelineItemLayout extends RelativeLayout {
	private TextView userName;
	private TextView content;
	private TextView createdTime;
	private TextView retweetCount;
	private TextView favCount;

	public TimelineItemLayout(Context context) {
		super(context);
	}

	public TimelineItemLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TimelineItemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public TimelineItemLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		userName = (TextView) findViewById(R.id.user_name);
		content = (TextView) findViewById(R.id.content);
		createdTime = (TextView) findViewById(R.id.created_time);
		retweetCount = (TextView) findViewById(R.id.retweet_count);
		favCount = (TextView) findViewById(R.id.fav_count);
	}

	public void bindView(@Nullable final twitter4j.Status status) {
		if (status != null) {
			User user = status.getUser();

			StringBuilder builder = new StringBuilder();
			builder.append(user.getName());
			builder.append(" @");
			builder.append(user.getScreenName());
			userName.setText(builder);

			content.setText(status.getText());
			createdTime.setText(getCreatedTimeString(status.getCreatedAt()));
			retweetCount.setText(String.valueOf(status.getRetweetCount()));
			favCount.setText(String.valueOf(status.getFavoriteCount()));
		}
	}

	private CharSequence getCreatedTimeString(final Date createdAt) {
		return DateFormat.format("H:mm - yyyy年M月d日", createdAt);
	}
}
