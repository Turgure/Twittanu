package jp.conpon.twittanu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by mirage-residence on 2016/08/13.
 */
public class TwitterOAuthActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_oauth);

        findViewById(R.id.action_start_oauth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TwitterManager.INSTANCE.startAuthorize(TwitterOAuthActivity.this);
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        TwitterManager.INSTANCE.callback(TwitterOAuthActivity.this, intent);
    }
}
