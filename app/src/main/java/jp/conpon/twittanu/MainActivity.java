package jp.conpon.twittanu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import twitter4j.auth.AccessToken;

/**
 * Created by Shigure on 2016/08/12.
 */
public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new TwitterUtils(this);
        if (!TwitterUtils.isLogined()) {
            Intent intent = new Intent(getApplicationContext(), TwitterOAuthActivity.class);
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
    }
}
