package jp.conpon.twittanu;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by mirage-residence on 2017/01/25.
 */

public class TimelineItemLayout extends RelativeLayout {
	private TextView content;

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

		content = (TextView) findViewById(R.id.content);
	}

	public void bindView(@Nullable final twitter4j.Status status) {
		if (status != null) {
			content.setText(status.getText());
		}
	}
}
