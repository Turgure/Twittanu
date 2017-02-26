package jp.conpon.twittanu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Shigure on 2016/08/12.
 */
public class MainActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!TwitterManager.INSTANCE.isLoggedIn()) {
            Intent intent = new Intent(MainActivity.this, TwitterOAuthActivity.class);
            startActivity(intent);
            finish();
        }

        // ツイートボタン
        findViewById(R.id.tweet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), TweetActivity.class);
                startActivity(intent);
            }
        });

		// タイムラインボタン
		findViewById(R.id.timeline).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(view.getContext(), TimelineActivity.class);
				startActivity(intent);
			}
		});
    }
}
